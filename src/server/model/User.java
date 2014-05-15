package server.model;

import java.sql.Timestamp;
import java.util.Date;

import server.exception.UserException;

public class User {
	
	private static final long START_COIN = 80;
	
	private String username;
	private String password;
	private long credits;
	private Timestamp lastLog;
	
	public User(String username, String password, long credits) throws UserException {
		
		if(username != null && username != ""
				&& password != null ) {
			this.username = username;
			this.password = password;
			this.lastLog = new Timestamp(new Date().getTime());
			
			if (credits > 0) {
				this.credits = credits;
			} else {
				this.credits = getDefaultCredits();
			}
		} else {
			throw new UserException(String.format("Bad user/password passed '%s'/'%s'", this.username, this.password));
		}
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.credits = getDefaultCredits();
		this.lastLog = new Timestamp(new Date().getTime());
	}
	
	public User(String username) {
		this.username = username;
		this.password = "";
		this.credits = getDefaultCredits();
		this.lastLog = new Timestamp(new Date().getTime());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getCredits() {
		return credits;
	}

	public void setCredits(long credits) {
		this.credits = credits;
	}
	
	public long addCredits(long credits) {
		
		if(credits > 0) {
			this.credits = this.credits + credits;
		}
		
		return this.credits;
	}
	
	public long getDefaultCredits() {
		return START_COIN;
	}
	
	@Override
	public String toString() {
		return String.format("User '%s'[%dc] - {%s} - Last log %s", this.username, this.credits, this.password, this.lastLog.toString());
	}
}
