package model;

import java.sql.Timestamp;

public class Badge {
	public final int id;
	public final int userId;
	public final String name;
	public final Timestamp date;
	public Badge(int id, int userId, String name, Timestamp date) {
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.date = date;
	}
	@Override
	public String toString() {
		return "Badge [id=" + id + ", userId=" + userId + ", name=" + name
				+ ", date=" + date + "]";
	}
}
