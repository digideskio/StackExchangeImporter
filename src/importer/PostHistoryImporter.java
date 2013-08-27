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

import model.PostHistory;

public class PostHistoryImporter {
	private static final String ROW = "row";
	private static final String ID = "Id";
	private static final String POST_HISTORY_TYPE_ID = "PostHistoryTypeId";
	private static final String POST_ID = "PostId";
	private static final String REVISION_GUID = "RevisionGUID";
	private static final String CREATION_DATE = "CreationDate";
	private static final String USER_ID = "UserId";
	private static final String USER_DISPLAY_NAME = "UserDisplayName";
	private static final String COMMENT = "Comment";
	private static final String TEXT = "Text";

	
	private static final int BATCH_SIZE = 100000;
	
	public static void importPostHistories(String filename, Connection dbConn) throws SQLException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		PreparedStatement pstmt = dbConn.prepareStatement("insert into posthistory values(" +
				"?,?,?,?,?,?,?,?,?)");
		try {
			InputStream in = new FileInputStream(filename);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			int recordCount = 0;
			LinkedList<PostHistory> postHistories = new LinkedList<>();
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
					Integer postHistoryTypeId = null;
					Integer postId = null;
					String revisionGUID = null;
					Timestamp creationDate = null;
					Integer userId = null;
					String userDisplayName = null;
					String comment = null;
					String text = null;
					while (attributes.hasNext()) {
						Attribute attribute = attributes.next();
						switch (attribute.getName().toString()) {
							case ID: 
								id = Integer.parseInt(attribute.getValue());
								break;
							case POST_HISTORY_TYPE_ID:
								postHistoryTypeId = Integer.parseInt(attribute.getValue());
								break;
							case POST_ID:
								postId = Integer.parseInt(attribute.getValue());
								break;
							case REVISION_GUID:
								revisionGUID = attribute.getValue();
								break;								
							case CREATION_DATE:
								creationDate = new Timestamp(DatatypeConverter.parseDateTime(attribute.getValue()).getTimeInMillis());
								break;								
							case USER_ID:
								userId = Integer.parseInt(attribute.getValue());
								break;
							case USER_DISPLAY_NAME:
								userDisplayName = attribute.getValue();
								break;
							case COMMENT:
								comment = attribute.getValue();
								break;
							case TEXT:
								text = attribute.getValue();
								break;
						}
					}
					PostHistory postHistory = new PostHistory(id, postHistoryTypeId, postId, revisionGUID, creationDate, userId, userDisplayName, comment, text);
					postHistories.add(postHistory);
					recordCount++;
					if (postHistories.size() == BATCH_SIZE) {
						insertPostHistoriesToDb(postHistories, pstmt);
					}
				}
			}
			if (postHistories.size() > 0) {
				insertPostHistoriesToDb(postHistories, pstmt);
			}
			System.out.println(recordCount + " records imported to table posthistory");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private static void insertPostHistoriesToDb(LinkedList<PostHistory> postHistories,
			PreparedStatement pstmt) throws SQLException {
		PostHistory bufPostHistory = null;
		while ((bufPostHistory = postHistories.poll()) != null) {
			//System.out.println(bufPostHistory.toString());	
			pstmt.setInt(1, bufPostHistory.id);
			pstmt.setInt(2, bufPostHistory.postHistoryTypeId);
			pstmt.setInt(3, bufPostHistory.postId);
			pstmt.setString(4, bufPostHistory.revisionGUID);
			pstmt.setTimestamp(5, bufPostHistory.creationDate);
			if (bufPostHistory.userId == null) {
				pstmt.setNull(6, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(6, bufPostHistory.userId);
			}
			if (bufPostHistory.userDisplayName == null) {
				pstmt.setNull(7, java.sql.Types.VARCHAR);
			} else {
				pstmt.setString(7, bufPostHistory.userDisplayName);
			}
			if (bufPostHistory.comment == null) {
				pstmt.setNull(8, java.sql.Types.VARCHAR);
			} else {
				pstmt.setString(8, bufPostHistory.comment);
			}
			if (bufPostHistory.text == null) {
				pstmt.setNull(9, java.sql.Types.VARCHAR);
			} else {
				pstmt.setString(9, bufPostHistory.text);
			}
			pstmt.addBatch();
		}
		pstmt.executeBatch();
	}
}
