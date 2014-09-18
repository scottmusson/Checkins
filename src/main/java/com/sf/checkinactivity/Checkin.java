package com.sf.checkinactivity;

import java.util.Date;
import java.util.List;

public class Checkin {

	private String username;
	private Date checkinDate;
	private List<String> files;
	
	public Checkin(String username, Date checkinDate, List<String> files) {
		if (username == null || username.trim().length() == 0) {
			throw new IllegalArgumentException("Username must not be null");
		}
		if (checkinDate == null) {
			throw new IllegalArgumentException("Checkin date must not be null");
		}
		this.username = username;
		this.checkinDate = checkinDate;
		this.files = files;
	}

	@Override
	public int hashCode() {
		return username.hashCode() + checkinDate.hashCode();
	}
	
	public boolean equals(Object o) {
		boolean equals = false;
		if (o instanceof Checkin) {
			Checkin c = (Checkin)o;
			equals = c.getUsername().equals(getUsername()) && c.getCheckinDate().equals(getCheckinDate());
		}
		return equals;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(Date checkinDate) {
		this.checkinDate = checkinDate;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}
	
}
