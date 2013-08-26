package model;

import java.sql.Timestamp;

public class Post {
	public final int id;
	public final int postTypeId;
	public final Integer parentId;
	public final Integer acceptedAnswerId;
	public final Timestamp creationDate;
	public final int score;
	public final Integer viewCount;
	public final String body;
	public final Integer ownerUserId;
	public final String ownerDisplayName;
	public final Integer lastEditorUserId;
	public final String lastEditorDisplayName;
	public final Timestamp lastEditDate;
	public final Timestamp lastActivityDate;
	public final Timestamp communityOwnedDate;
	public final Timestamp closedDate;
	public final String title;
	public final String tags;
	public final Integer answerCount;
	public final Integer commentCount;
	public final Integer favoriteCount;
	public Post(int id, int postTypeId, Integer parentId, Integer acceptedAnswerId,
			Timestamp creationDate, int score, Integer viewCount, String body,
			Integer ownerUserId, String ownerDisplayName, Integer lastEditorUserId,
			String lastEditorDisplayName, Timestamp lastEditDate,
			Timestamp lastActivityDate, Timestamp communityOwnedDate,
			Timestamp closedDate, String title, String tags, Integer answerCount,
			Integer commentCount, Integer favoriteCount) {
		this.id = id;
		this.postTypeId = postTypeId;
		this.parentId = parentId;
		this.acceptedAnswerId = acceptedAnswerId;
		this.creationDate = creationDate;
		this.score = score;
		this.viewCount = viewCount;
		this.body = body;
		this.ownerUserId = ownerUserId;
		this.ownerDisplayName = ownerDisplayName;
		this.lastEditorUserId = lastEditorUserId;
		this.lastEditorDisplayName = lastEditorDisplayName;
		this.lastEditDate = lastEditDate;
		this.lastActivityDate = lastActivityDate;
		this.communityOwnedDate = communityOwnedDate;
		this.closedDate = closedDate;
		this.title = title;
		this.tags = tags;
		this.answerCount = answerCount;
		this.commentCount = commentCount;
		this.favoriteCount = favoriteCount;
	}
	@Override
	public String toString() {
		return "Post [id=" + id + ", postTypeId=" + postTypeId + ", parentId="
				+ parentId + ", acceptedAnswerId=" + acceptedAnswerId
				+ ", creationDate=" + creationDate + ", score=" + score
				+ ", viewCount=" + viewCount + ", body=" + body
				+ ", ownerUserId=" + ownerUserId + ", ownerDisplayName="
				+ ownerDisplayName + ", lastEditorUserId=" + lastEditorUserId
				+ ", lastEditorDisplayName=" + lastEditorDisplayName
				+ ", lastEditDate=" + lastEditDate + ", lastActivityDate="
				+ lastActivityDate + ", communityOwnedDate="
				+ communityOwnedDate + ", closedDate=" + closedDate
				+ ", title=" + title + ", tags=" + tags + ", answerCount="
				+ answerCount + ", commentCount=" + commentCount
				+ ", favoriteCount=" + favoriteCount + "]";
	}
	
}
