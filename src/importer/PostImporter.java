package importer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.bind.DatatypeConverter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import model.Post;

public class PostImporter {
	private static final String ROW = "row";
	private static final String ID = "Id";
	private static final String POST_TYPE_ID = "PostTypeId";
	private static final String PARENT_ID = "ParentId";
	private static final String ACCEPTED_ANSWER_ID = "AcceptedAnswerId";
	private static final String CREATION_DATE = "CreationDate";	
	private static final String SCORE = "Score";
	private static final String VIEW_COUNT = "ViewCount";
	private static final String BODY = "Body";
	private static final String OWNER_USER_ID = "OwnerUserId";
	private static final String OWNER_DISPLAY_NAME = "OwnerDisplayName";
	private static final String LAST_EDITOR_USER_ID = "LastEditorUserId";
	private static final String LAST_EDITOR_DISPLAY_NAME = "LastEditorDisplayName";
	private static final String LAST_EDIT_DATE = "LastEditDate";
	private static final String LAST_ACTIVITY_DATE = "LastActivityDate";
	private static final String COMMUNITY_OWNED_DATE = "CommunityOwnedDate";
	private static final String CLOSED_DATE = "ClosedDate";
	private static final String TITLE = "Title";
	private static final String TAGS = "Tags";
	private static final String ANSWER_COUNT = "AnswerCount";
	private static final String COMMENT_COUNT = "CommentCount";
	private static final String FAVORITE_COUNT = "FavoriteCount";
	
	private static final int BATCH_SIZE = 100000;
	
	public static void importPosts(String filename, Connection dbConn) throws SQLException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		PreparedStatement pstmt = dbConn.prepareStatement("insert into posts values(" +
				"?,?,?,?,?,?,?," +
				"?,?,?,?,?,?,?," +
				"?,?,?,?,?,?,?)");
		try {
			InputStream in = new FileInputStream(filename);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			int recordCount = 0;
			LinkedList<Post> posts = new LinkedList<>();
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				//System.out.println(event);
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (!startElement.getName().getLocalPart().equals(ROW)) {
						System.err.println(startElement);
						continue;
					}
					Iterator<Attribute> attributes = startElement.getAttributes();
					Integer id = null;
					Integer postTypeId = null;
					Integer parentId = null;
					Integer acceptedAnswerId = null;
					Timestamp creationDate = null;
					Integer score = null;
					Integer viewCount = null;
					String body = null;
					Integer ownerUserId = null;
					String ownerDisplayName = null;
					Integer lastEditorUserId = null;
					String lastEditorDisplayName = null;
					Timestamp lastEditDate = null;
					Timestamp lastActivityDate = null;
					Timestamp communityOwnedDate = null;
					Timestamp closedDate = null;
					String title = null;
					String tags = null;
					Integer answerCount = null;
					Integer commentCount = null;
					Integer favoriteCount = null;
					while (attributes.hasNext()) {
						Attribute attribute = attributes.next();
						switch (attribute.getName().toString()) {
							case ID: 
								id = Integer.parseInt(attribute.getValue());
								break;
							case POST_TYPE_ID:
								postTypeId = Integer.parseInt(attribute.getValue());
								break;
							case PARENT_ID:
								parentId = Integer.parseInt(attribute.getValue());
								break;
							case ACCEPTED_ANSWER_ID:
								acceptedAnswerId = Integer.parseInt(attribute.getValue());
								break;
							case CREATION_DATE:
								creationDate = new Timestamp(DatatypeConverter.parseDateTime(attribute.getValue()).getTimeInMillis());
								break;								
							case SCORE:
								score = Integer.parseInt(attribute.getValue());
								break;
							case VIEW_COUNT:
								viewCount = Integer.parseInt(attribute.getValue());
								break;
							case BODY:
								body = attribute.getValue();
								break;
							case OWNER_USER_ID:
								ownerUserId = Integer.parseInt(attribute.getValue());
								break;
							case OWNER_DISPLAY_NAME:
								ownerDisplayName = attribute.getValue();
								break;
							case LAST_EDITOR_USER_ID:
								lastEditorUserId = Integer.parseInt(attribute.getValue());
								break;
							case LAST_EDITOR_DISPLAY_NAME:
								lastEditorDisplayName = attribute.getValue();
								break;
							case LAST_EDIT_DATE:
								lastEditDate = new Timestamp(DatatypeConverter.parseDateTime(attribute.getValue()).getTimeInMillis());
								break;
							case LAST_ACTIVITY_DATE:
								lastActivityDate = new Timestamp(DatatypeConverter.parseDateTime(attribute.getValue()).getTimeInMillis());
								break;
							case COMMUNITY_OWNED_DATE:
								communityOwnedDate = new Timestamp(DatatypeConverter.parseDateTime(attribute.getValue()).getTimeInMillis());
								break;
							case CLOSED_DATE:
								closedDate = new Timestamp(DatatypeConverter.parseDateTime(attribute.getValue()).getTimeInMillis());
								break;
							case TITLE:
								title = attribute.getValue();
								break;
							case TAGS:
								tags = attribute.getValue();
								break;
							case ANSWER_COUNT:
								answerCount = Integer.parseInt(attribute.getValue());
								break;
							case COMMENT_COUNT:
								commentCount = Integer.parseInt(attribute.getValue());
								break;
							case FAVORITE_COUNT:
								favoriteCount = Integer.parseInt(attribute.getValue());
								break;
						}
					}
//					System.out.println(id + "\t" +  postTypeId + "\t" +  parentId + "\t" +  acceptedAnswerId + "\t" +  creationDate + "\t" +  
//							score + "\t" +  viewCount + "\t" +  body + "\t" +  ownerUserId + "\t" +  ownerDisplayName + "\t" +  
//							lastEditorUserId + "\t" +  lastEditorDisplayName + "\t" +  lastEditDate + "\t" +  
//							lastActivityDate + "\t" +  communityOwnedDate + "\t" +  closedDate + "\t" +  
//							title + "\t" +  tags + "\t" +  answerCount + "\t" +  commentCount + "\t" +  favoriteCount);
					Post post = new Post(id, postTypeId, parentId, acceptedAnswerId, creationDate, 
							score, viewCount, body, ownerUserId, ownerDisplayName, 
							lastEditorUserId, lastEditorDisplayName, lastEditDate, 
							lastActivityDate, communityOwnedDate, closedDate, 
							title, tags, answerCount, commentCount, favoriteCount);
					posts.add(post);
					recordCount++;
					if (posts.size() == BATCH_SIZE) {
						insertPostsToDb(posts, pstmt);
					}
				}
			}
			if (posts.size() > 0) {
				insertPostsToDb(posts, pstmt);
			}
			System.out.println(recordCount + " records imported to table posts");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private static void insertPostsToDb(LinkedList<Post> posts,
			PreparedStatement pstmt) throws SQLException {
		Post bufPost = null;
		while ((bufPost = posts.poll()) != null) {
			//System.out.println(bufPost.toString());		
			pstmt.setInt(1, bufPost.id);
			pstmt.setInt(2, bufPost.postTypeId);
			if (bufPost.parentId == null) {
				pstmt.setNull(3, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(3, bufPost.parentId);
			}
			if (bufPost.acceptedAnswerId == null) {
				pstmt.setNull(4, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(4, bufPost.acceptedAnswerId);
			}
			pstmt.setTimestamp(5, bufPost.creationDate);
			pstmt.setInt(6, bufPost.score);
			if (bufPost.viewCount == null) {
				pstmt.setNull(7, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(7, bufPost.viewCount);
			}
			pstmt.setString(8, bufPost.body);
			if (bufPost.ownerUserId == null) {
				pstmt.setNull(9, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(9, bufPost.ownerUserId);
			}
			if (bufPost.ownerDisplayName == null) {
				pstmt.setNull(10, java.sql.Types.VARCHAR);
			} else {
				pstmt.setString(10, bufPost.ownerDisplayName);
			}
			if (bufPost.lastEditorUserId == null) {
				pstmt.setNull(11, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(11, bufPost.lastEditorUserId);
			}
			if (bufPost.lastEditorDisplayName == null) {
				pstmt.setNull(12, java.sql.Types.VARCHAR);
			} else {
				pstmt.setString(12, bufPost.lastEditorDisplayName);
			}
			if (bufPost.lastEditDate == null) {
				pstmt.setNull(13, java.sql.Types.TIMESTAMP);
			} else {
				pstmt.setTimestamp(13, bufPost.lastEditDate);
			}
			pstmt.setTimestamp(14, bufPost.lastActivityDate);
			if (bufPost.communityOwnedDate == null) {
				pstmt.setNull(15, java.sql.Types.TIMESTAMP);
			} else {
				pstmt.setTimestamp(15, bufPost.communityOwnedDate);
			}
			if (bufPost.closedDate == null) {
				pstmt.setNull(16, java.sql.Types.TIMESTAMP);
			} else {
				pstmt.setTimestamp(16, bufPost.closedDate);
			}
			if (bufPost.title == null) {
				pstmt.setNull(17, java.sql.Types.VARCHAR);
			} else {
				pstmt.setString(17, bufPost.title);
			}
			if (bufPost.tags == null) {
				pstmt.setNull(18, java.sql.Types.VARCHAR);
			} else {
				pstmt.setString(18, bufPost.tags);
			}
			if (bufPost.answerCount == null) {
				pstmt.setNull(19, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(19, bufPost.answerCount);
			}
			if (bufPost.commentCount == null) {
				pstmt.setNull(20, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(20, bufPost.commentCount);
			}
			if (bufPost.favoriteCount == null) {
				pstmt.setNull(21, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(21, bufPost.favoriteCount);
			}
			pstmt.addBatch();
		}
		pstmt.executeBatch();
	}
}
