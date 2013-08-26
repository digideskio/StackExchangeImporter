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

import model.Badge;

public class BadgeImporter {
	private static final String ROW = "row";
	private static final String ID = "Id";
	private static final String USER_ID = "UserId";
	private static final String NAME = "Name";
	private static final String DATE = "Date";
	
	private static final int BATCH_SIZE = 100000;
	
	public static void importBadges(String filename, Connection dbConn) throws SQLException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		PreparedStatement pstmt = dbConn.prepareStatement("insert into badges values(?,?,?,?)");
		try {
			InputStream in = new FileInputStream(filename);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			int recordCount = 0;
			LinkedList<Badge> badges = new LinkedList<>();
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
					Integer userId = null;
					String name = null;
					Timestamp date = null;
					while (attributes.hasNext()) {
						Attribute attribute = attributes.next();
						switch (attribute.getName().toString()) {
							case ID: 
								id = Integer.parseInt(attribute.getValue());
								break;
							case USER_ID:
								userId = Integer.parseInt(attribute.getValue());
								break;
							case NAME:
								name = attribute.getValue();
								break;
							case DATE:
								date = new Timestamp(DatatypeConverter.parseDateTime(attribute.getValue()).getTimeInMillis());
								break;
						}
					}
					Badge badge = new Badge(id, userId, name, date);
					badges.add(badge);
					recordCount++;
					if (badges.size() == BATCH_SIZE) {
						insertBadgesToDb(badges, pstmt);
					}
				}
			}
			if (badges.size() > 0) {
				insertBadgesToDb(badges, pstmt);
			}
			System.out.println(recordCount + " records imported to table badges");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private static void insertBadgesToDb(LinkedList<Badge> badges,
			PreparedStatement pstmt) throws SQLException {
		Badge bufBadge = null;
		while ((bufBadge = badges.poll()) != null) {
			System.out.println(bufBadge.toString());
			pstmt.setInt(1, bufBadge.id);
			pstmt.setInt(2, bufBadge.userId);
			if (bufBadge.name == null) {
				pstmt.setNull(3, java.sql.Types.VARCHAR);
			} else {
				pstmt.setString(3, bufBadge.name);
			}
			pstmt.setTimestamp(4, bufBadge.date);
			pstmt.addBatch();
		}
		pstmt.executeBatch();
	}
}
