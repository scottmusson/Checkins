package com.sf.checkinactivity;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 */
public class Checkins {
    private static final Logger LOGGER = Logger.getLogger(Checkins.class.getName());

    public enum DAYS_OF_WEEK {
        SUNDAY("Sunday"), MONDAY("Monday"), TUESDAY("Tuesday"), WEDNESDAY("Wednesday"), THURSDAY("Thursday"), FRIDAY("Friday"), SATURDAY("Saturday");
        private DAYS_OF_WEEK(final String day) {
            this.day = day;
        }
        private String day;
        @Override
        public String toString() {
            return day;
        }
    }
    private Map<Calendar, Map<String, List<String>>> dateMap = new HashMap<Calendar, Map<String, List<String>>>();
    private Map<DAYS_OF_WEEK, Map<String, List<List<String>>>> detailedCheckinPerDevPerDayOfWeek = new HashMap<DAYS_OF_WEEK, Map<String, List<List<String>>>>();
    private Map<DAYS_OF_WEEK, Integer> numberOfCheckinsPerDayOfWeekPerDev = new HashMap<DAYS_OF_WEEK, Integer>();
    private Set<String> developers = new TreeSet<String>();

    /**
     * Create a map, with the key being a timestamp and the value being a map of the developer, to their list of files.
     * @param fileName an xml file created from P4 checkin logs
     * @throws IOException
     * @throws SAXException
     */
    public void parse(String fileName) throws IOException, SAXException {
        File file = new File(fileName);
        if (file.exists()) {
            DOMParser parser = new DOMParser();
            parser.parse(file.toString());
            Document doc = parser.getDocument();
            NodeList root = doc.getChildNodes();
            if (root != null && root.getLength() > 0) {
                NodeList children = root.item(0).getChildNodes();
                if (children != null) {
                    for (int i = 0; i < children.getLength(); i++) {
                        NamedNodeMap attributes = children.item(i).getAttributes();
                        if (attributes != null) {

                            Node date = attributes.getNamedItem("date");
                            if (date != null) {
                                Long timeInMillis = Long.valueOf(date.getNodeValue());
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                                cal.setTimeInMillis(timeInMillis);
                                LOGGER.log(Level.INFO, "Day: " + cal.get(Calendar.DAY_OF_WEEK));
                                Map<String, List<String>> devs = dateMap.get(cal);
                                if (devs == null) {
                                    devs = new HashMap<String, List<String>>();
                                    dateMap.put(cal, devs);
                                }
                                String developer = attributes.getNamedItem("author").getNodeValue();
                                developers.add(developer);
                                List<String> filesForDev = devs.get(developer);
                                if (filesForDev == null) {
                                    filesForDev = new ArrayList<String>();
                                    devs.put(developer, filesForDev);
                                }
                                filesForDev.add(attributes.getNamedItem("filename").getNodeValue());
                            }
                        }
                    }
                    bucketToDayOfWeekPerDev();
                    bucketTotalPerDay();
                }
            }
        } else {
            throw new FileNotFoundException("File does not exist. :" + file.getAbsolutePath());
        }
    }

    private DAYS_OF_WEEK dayOfWeekForCalDayOfWeek(int calDayOfWeek) {
        switch (calDayOfWeek) {
            case Calendar.SUNDAY:
                return DAYS_OF_WEEK.SUNDAY;
            case Calendar.MONDAY:
                return DAYS_OF_WEEK.MONDAY;
            case Calendar.TUESDAY:
                return DAYS_OF_WEEK.TUESDAY;
            case Calendar.WEDNESDAY:
                return DAYS_OF_WEEK.WEDNESDAY;
            case Calendar.THURSDAY:
                return DAYS_OF_WEEK.THURSDAY;
            case Calendar.FRIDAY:
                return DAYS_OF_WEEK.FRIDAY;
            case Calendar.SATURDAY:
            default:
                return DAYS_OF_WEEK.SATURDAY;
        }
    }


    protected void bucketToDayOfWeekPerDev() {
        for (Calendar cal : dateMap.keySet()) {
            DAYS_OF_WEEK day_of_week = dayOfWeekForCalDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
            // convert to our enum
            Map<String, List<List<String>>> dayMap = detailedCheckinPerDevPerDayOfWeek.get(day_of_week);
            if (dayMap == null) {
                dayMap = new HashMap<String, List<List<String>>>();
                detailedCheckinPerDevPerDayOfWeek.put(day_of_week, dayMap);
            }

            Map<String, List<String>> devAtomicCheckin = dateMap.get(cal);
            for (String devName : developers) {
                List<List<String>> checkinsForDev = dayMap.get(devName);
                if (checkinsForDev == null) {
                    checkinsForDev = new ArrayList<List<String>>();
                    dayMap.put(devName, checkinsForDev);
                }
                List<String> checkins = devAtomicCheckin.get(devName);
                if (checkins != null) {
                    checkinsForDev.add(checkins);
                }
            }
        }
    }

    protected void bucketTotalPerDay() {
        for (DAYS_OF_WEEK day_of_week : detailedCheckinPerDevPerDayOfWeek.keySet()) {
            int total = 0;
            Map<String, List<List<String>>> checkinsForDev = detailedCheckinPerDevPerDayOfWeek.get(day_of_week);
            for (String dev : developers) {
                List<List<String>> atomicCheckins = checkinsForDev.get(dev);
                int atomicCheckinsSize = atomicCheckins != null ? atomicCheckins.size() : 0;
                LOGGER.log(Level.INFO, day_of_week + " " + dev + ": " + atomicCheckinsSize);
                total = total + atomicCheckinsSize;
            }
            numberOfCheckinsPerDayOfWeekPerDev.put(day_of_week, total);
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            for (DAYS_OF_WEEK days_of_week : DAYS_OF_WEEK.values()) {
                LOGGER.log(Level.INFO, days_of_week + ": " + totalForDay(days_of_week));
            }
        }
    }

    public int totalForDayForDev(DAYS_OF_WEEK day_of_week, String dev) {
        int total = 0;
        Map<String, List<List<String>>> checkinsForDev = detailedCheckinPerDevPerDayOfWeek.get(day_of_week);
        if (checkinsForDev != null) {
            List<List<String>> atomicCheckins = checkinsForDev.get(dev);
            total = atomicCheckins != null ? atomicCheckins.size() : 0;
        }
        return total;
    }

    public int totalForDay(DAYS_OF_WEEK day_of_week) {
        Integer total = numberOfCheckinsPerDayOfWeekPerDev.get(day_of_week);
        return (total != null ? total : 0);
    }

    public List<String> devWithFewestForDay(DAYS_OF_WEEK day_of_week) {
        Integer min =  Integer.MAX_VALUE;
        List<String> devsWithFewest = new ArrayList<String>();
        Map<String, List<List<String>>> map = detailedCheckinPerDevPerDayOfWeek.get(day_of_week);
        if (map != null) {
            for (String dev : map.keySet()) {
                int len = map.get(dev).size();
                if (len < min) {
                    devsWithFewest.clear();
                    devsWithFewest.add(dev);
                    min = len;
                } else if (len == min) {
                    devsWithFewest.add(dev);
                }

            }
        }
        return devsWithFewest;
    }

    public List<String> devWithMostForDay(DAYS_OF_WEEK day_of_week) {
        Integer max =  Integer.MIN_VALUE;
        List<String> devsWithMost = new ArrayList<String>();
        Map<String, List<List<String>>> map = detailedCheckinPerDevPerDayOfWeek.get(day_of_week);
        if (map != null) {
            for (String dev : map.keySet()) {
                int len = map.get(dev).size();
                if (len > max) {
                    devsWithMost.clear();
                    devsWithMost.add(dev);
                    max = len;
                } else if (len == max) {
                    devsWithMost.add(dev);
                }

            }
        }
        return devsWithMost;
    }


    public String[] getDevelopers() {
        return developers.toArray(new String[developers.size()]);
    }

    public Map<DAYS_OF_WEEK, Map<String, List<List<String>>>> getDetailedCheckinPerDevPerDayOfWeek() {
        return detailedCheckinPerDevPerDayOfWeek;
    }
    public static void main(String[] args) {
        if (args.length > 0) {
            Checkins checkins = new Checkins();
            try {
                checkins.parse(args[0]);
                for (DAYS_OF_WEEK days_of_week : DAYS_OF_WEEK.values()) {
                    System.out.println(days_of_week + ": " + checkins.totalForDay(days_of_week));
                }
                List<String> list = checkins.devWithFewestForDay(DAYS_OF_WEEK.THURSDAY);
                for (String s : list) {
                    System.out.println(s);
                }

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Usage: Checkins <depot xml file>");
        }
    }
}
