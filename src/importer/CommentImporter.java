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

import model.Comment;

public class CommentImporter {
	private static final String ROW = "row";
	private static final String ID = "Id";
	private static final String POST_ID = "PostId";
	private static final String SCORE = "Score";
	private static final String TEXT = "Text";
	private static final String CREATION_DATE = "CreationDate";
	private static final String USER_DISPLAY_NAME = "UserDisplayName";
	private static final String USER_ID = "UserId";
	
	private static final int BATCH_SIZE = 100000;
	
	public static void importComments(String filename, Connection dbConn) throws SQLException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		PreparedStatement pstmt = dbConn.prepareStatement("insert into comments values(?,?,?,?,?,?,?)");
		try {
			InputStream in = new FileInputStream(filename);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			int recordCount = 0;
			LinkedList<Comment> comments = new LinkedList<>();
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
					Integer postId = null;
					Integer score = null;
					String text = null;
					Timestamp creationDate = null;
					String userDisplayName = null;
					Integer userId = null;
					while (attributes.hasNext()) {
						Attribute attribute = attributes.next();
						switch (attribute.getName().toString()) {
							case ID: 
								id = Integer.parseInt(attribute.getValue());
								break;
							case POST_ID:
								postId = Integer.parseInt(attribute.getValue());
								break;
							case SCORE:
								score = Integer.parseInt(attribute.getValue());
								break;
							case TEXT:
								text = attribute.getValue();
								break;
							case CREATION_DATE:
								creationDate = new Timestamp(DatatypeConverter.parseDateTime(attribute.getValue()).getTimeInMillis());
								break;
							case USER_DISPLAY_NAME:
								userDisplayName = attribute.getValue();
								break;
							case USER_ID:
								userId = Integer.parseInt(attribute.getValue());
								break;
						}
					}
					Comment comment = new Comment(id, postId, score, text, creationDate, userDisplayName, userId);
					comments.add(comment);
					recordCount++;
					if (comments.size() == BATCH_SIZE) {
						insertCommentsToDb(comments, pstmt);
					}
				}
			}
			if (comments.size() > 0) {
				insertCommentsToDb(comments, pstmt);
			}
			System.out.println(recordCount + " records imported to table comments");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private static void insertCommentsToDb(LinkedList<Comment> comments,
			PreparedStatement pstmt) throws SQLException {
		Comment bufComment = null;
		while ((bufComment = comments.poll()) != null) {
			//System.out.println(bufComment.toString());	
			pstmt.setInt(1, bufComment.id);
			pstmt.setInt(2, bufComment.postId);
			if (bufComment.score == null) {
				pstmt.setNull(3, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(3, bufComment.score);
			}
			pstmt.setString(4, bufComment.text);
			pstmt.setTimestamp(5, bufComment.creationDate);
			if (bufComment.userDisplayName == null) {
				pstmt.setNull(6, java.sql.Types.VARCHAR);
			} else {
				pstmt.setString(6, bufComment.userDisplayName);
			}
			if (bufComment.userId == null) {
				pstmt.setNull(7, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(7, bufComment.userId);
			}
			pstmt.addBatch();
		}
		pstmt.executeBatch();
	}
}
