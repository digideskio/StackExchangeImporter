package model;

import java.sql.Timestamp;

public class Badge {
	final int id;
	final int userId;
	final String name;
	final Timestamp date;
	public Badge(int id, int userId, String name, Timestamp date) {
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.date = date;
	}
}
