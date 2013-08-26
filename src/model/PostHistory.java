package model;

import java.sql.Timestamp;

public class PostHistory {
	final int id;
	final int postHistoryTypeId;
	final int postId;
	final int revisionGUID;	
	final Timestamp creationDate;
	final int userId;
	final String userDisplayName;
	final String comment;
	final String text;
	public PostHistory(int id, int postHistoryTypeId, int postId,
			int revisionGUID, Timestamp creationDate, int userId,
			String userDisplayName, String comment, String text) {
		super();
		this.id = id;
		this.postHistoryTypeId = postHistoryTypeId;
		this.postId = postId;
		this.revisionGUID = revisionGUID;
		this.creationDate = creationDate;
		this.userId = userId;
		this.userDisplayName = userDisplayName;
		this.comment = comment;
		this.text = text;
	}
	
}
