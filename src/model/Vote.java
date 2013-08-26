package model;

import java.sql.Timestamp;


public class Vote {
	public final Integer id;
	public final Integer postId;
	public final Integer voteTypeId;
	public final Timestamp creationDate;
	public final Integer userId;
	public final Integer bountyAmount;
	public Vote(Integer id, Integer postId, Integer voteTypeId,
			Timestamp creationDate, Integer userId, Integer bountyAmount) {
		this.id = id;
		this.postId = postId;
		this.voteTypeId = voteTypeId;
		this.creationDate = creationDate;
		this.userId = userId;
		this.bountyAmount = bountyAmount;
	}
	@Override
	public String toString() {
		return "Vote [id=" + id + ", postId=" + postId + ", voteTypeId="
				+ voteTypeId + ", creationDate=" + creationDate + ", userId="
				+ userId + ", bountyAmount=" + bountyAmount + "]";
	}
	
}
