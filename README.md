Checkins
========

mvn clean install package
java -jar target/CheckinActivity-1.0-SNAPSHOT.jar
Usage: -Drepoprops=<path to p4 repo props> -jar  <comma separated list of devs e.g. dev1,dev2> <depot path, e.g. //app/...> <optional: startDate e.g. 12/1/2011> <optional: endDate>

repoprops should have the following info:
servername=p4java(ssl)://<server:port>
userName=p4username
password=p4password
clientname=foo-cs

for JDK 1.7 you need to install the Unlimited Crypto libraries if you are connecting to your P4 server through SSL
see: http://answers.perforce.com/articles/KB_Article/Using-P4Java-and-P4Eclipse-with-SSL-enabled-Perforce-Servers