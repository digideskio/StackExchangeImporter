package model;

import java.sql.Timestamp;

public class Comment {
	public final int id;
	public final int postId;
	public final Integer score;
	public final String text;
	public final Timestamp creationDate;
	public final String userDisplayName;
	public final Integer userId;
	public Comment(int id, int postId, Integer score, String text,
			Timestamp creationDate, String userDisplayName, Integer userId) {
		this.id = id;
		this.postId = postId;
		this.score = score;
		this.text = text;
		this.creationDate = creationDate;
		this.userDisplayName = userDisplayName;
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", postId=" + postId + ", score=" + score
				+ ", Text=" + text + ", creationDate=" + creationDate
				+ ", userDisplayName=" + userDisplayName + ", userId=" + userId
				+ "]";
	}
	
}
