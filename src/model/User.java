package model;

import java.sql.Timestamp;

public class User {
	public final int id;
	public final int reputation;
	public final Timestamp creationDate;
	public final String displayName;
	public final String emailHash;
	public final Timestamp lastAccessDate;
	public final String websiteUrl;
	public final String location;
	public final Integer age;
	public final String aboutMe;
	public final int views;
	public final int upVotes;
	public final int downVotes;
	public User(int id, int reputation, Timestamp creationDate,
			String displayName, String emailHash, Timestamp lastAccessDate,
			String websiteUrl, String location, Integer age, String aboutMe,
			int views, int upVotes, int downVotes) {
		this.id = id;
		this.reputation = reputation;
		this.creationDate = creationDate;
		this.displayName = displayName;
		this.emailHash = emailHash;
		this.lastAccessDate = lastAccessDate;
		this.websiteUrl = websiteUrl;
		this.location = location;
		this.age = age;
		this.aboutMe = aboutMe;
		this.views = views;
		this.upVotes = upVotes;
		this.downVotes = downVotes;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", reputation=" + reputation
				+ ", creationDate=" + creationDate + ", displayName="
				+ displayName + ", emailHash=" + emailHash
				+ ", lastAccessDate=" + lastAccessDate + ", websiteUrl="
				+ websiteUrl + ", location=" + location + ", age=" + age
				+ ", aboutMe=" + aboutMe + ", views=" + views + ", upVotes="
				+ upVotes + ", downVotes=" + downVotes + "]";
	}
	
}
