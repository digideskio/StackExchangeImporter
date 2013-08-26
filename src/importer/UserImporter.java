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

import model.User;

public class UserImporter {
	private static final String ROW = "row";
	private static final String ID = "Id";
	private static final String REPUTATION = "Reputation";
	private static final String CREATION_DATE = "CreationDate";
	private static final String DISPLAY_NAME = "DisplayName";
	private static final String EMAIL_HASH = "EmailHash";
	private static final String LAST_ACCESS_DATE = "LastAccessDate";
	private static final String WEBSITE_URL = "WebsiteUrl";
	private static final String LOCATION = "Location";
	private static final String AGE = "Age";
	private static final String ABOUT_ME = "AboutMe";
	private static final String VIEWS = "Views";
	private static final String UPVOTES = "UpVotes";
	private static final String DOWNVOTES = "DownVotes";
	
	private static final int BATCH_SIZE = 100000;
	
	public static void importUsers(String filename, Connection dbConn) throws SQLException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		PreparedStatement pstmt = dbConn.prepareStatement("insert into users values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		try {
			InputStream in = new FileInputStream(filename);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			int recordCount = 0;
			LinkedList<User> users = new LinkedList<>();
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
					Integer reputation = null;
					Timestamp creationDate = null;
					String displayName = null;
					String emailHash = null;
					Timestamp lastAccessDate = null;
					String websiteUrl = null;
					String location = null;
					Integer age = null;
					String aboutMe = null;
					Integer views = null;
					Integer upVotes = null;
					Integer downVotes = null;
					while (attributes.hasNext()) {
						Attribute attribute = attributes.next();
						switch (attribute.getName().toString()) {
							case ID: 
								id = Integer.parseInt(attribute.getValue());
								break;
							case REPUTATION:
								reputation = Integer.parseInt(attribute.getValue());
								break;
							case CREATION_DATE:
								creationDate = new Timestamp(DatatypeConverter.parseDateTime(attribute.getValue()).getTimeInMillis());
								break;
							case DISPLAY_NAME:
								displayName = attribute.getValue();
								break;
							case EMAIL_HASH:
								emailHash = attribute.getValue();
								break;
							case LAST_ACCESS_DATE:
								lastAccessDate = new Timestamp(DatatypeConverter.parseDateTime(attribute.getValue()).getTimeInMillis());
								break;
							case WEBSITE_URL:
								websiteUrl = attribute.getValue();
								break;
							case LOCATION:
								location = attribute.getValue();
								break;
							case AGE:
								age = Integer.parseInt(attribute.getValue());
								break;
							case ABOUT_ME:
								aboutMe = attribute.getValue();
								break;
							case VIEWS:
								views = Integer.parseInt(attribute.getValue());
								break;
							case UPVOTES:
								upVotes = Integer.parseInt(attribute.getValue());
								break;
							case DOWNVOTES:
								downVotes = Integer.parseInt(attribute.getValue());
								break;
						}
					}
					User user = new User(id, reputation, creationDate, displayName, emailHash, lastAccessDate, 
							websiteUrl, location, age, aboutMe, views, upVotes, downVotes);
					users.add(user);
					recordCount++;
					if (users.size() == BATCH_SIZE) {
						insertUsersToDb(users, pstmt);
					}
				}
			}
			if (users.size() > 0) {
				insertUsersToDb(users, pstmt);
			}
			System.out.println(recordCount + " records imported to table users");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private static void insertUsersToDb(LinkedList<User> users,
			PreparedStatement pstmt) throws SQLException {
		User bufUser = null;
		while ((bufUser = users.poll()) != null) {
			//System.out.println(bufUser.toString());	
			pstmt.setInt(1, bufUser.id);
			pstmt.setInt(2, bufUser.reputation);
			pstmt.setTimestamp(3, bufUser.creationDate);
			pstmt.setString(4, bufUser.displayName);
			pstmt.setString(5, bufUser.emailHash);
			pstmt.setTimestamp(6, bufUser.lastAccessDate);
			if (bufUser.websiteUrl == null) {
				pstmt.setNull(7, java.sql.Types.VARCHAR);
			} else {
				pstmt.setString(7, bufUser.websiteUrl);
			}
			if (bufUser.location == null) {
				pstmt.setNull(8, java.sql.Types.VARCHAR);
			} else {
				pstmt.setString(8, bufUser.location);
			}
			if (bufUser.age == null) {
				pstmt.setNull(9, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(9, bufUser.age);
			}
			if (bufUser.aboutMe == null) {
				pstmt.setNull(10, java.sql.Types.VARCHAR);
			} else {
				pstmt.setString(10, bufUser.aboutMe);
			}
			pstmt.setInt(11, bufUser.views);
			pstmt.setInt(12, bufUser.upVotes);
			pstmt.setInt(13, bufUser.downVotes);
			pstmt.addBatch();
		}
		pstmt.executeBatch();
	}
}
