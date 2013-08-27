package model;

import java.sql.Timestamp;

public class PostHistory {
	public final int id;
	public final int postHistoryTypeId;
	public final int postId;
	public final String revisionGUID;	
	public final Timestamp creationDate;
	public final Integer userId;
	public final String userDisplayName;
	public final String comment;
	public final String text;
	public PostHistory(int id, int postHistoryTypeId, int postId,
			String revisionGUID, Timestamp creationDate, Integer userId,
			String userDisplayName, String comment, String text) {
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
	@Override
	public String toString() {
		return "PostHistory [id=" + id + ", postHistoryTypeId="
				+ postHistoryTypeId + ", postId=" + postId + ", revisionGUID="
				+ revisionGUID + ", creationDate=" + creationDate + ", userId="
				+ userId + ", userDisplayName=" + userDisplayName
				+ ", comment=" + comment + ", text=" + text + "]";
	}
	
}
