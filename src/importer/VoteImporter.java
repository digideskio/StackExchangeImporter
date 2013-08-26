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

import model.Vote;

public class VoteImporter {
	private static final String ROW = "row";
	private static final String ID = "Id";
	private static final String POST_ID = "PostId";
	private static final String VOTE_TYPE_ID = "VoteTypeId";
	private static final String CREATION_DATE = "CreationDate";
	private static final String USER_ID = "UserId";
	private static final String BOUNTY_AMOUNT = "BountyAmount";
	
	private static final int BATCH_SIZE = 100000;
	
	public static void importVotes(String filename, Connection dbConn) throws SQLException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		PreparedStatement pstmt = dbConn.prepareStatement("insert into votes values(?,?,?,?,?,?)");
		try {
			InputStream in = new FileInputStream(filename);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			int recordCount = 0;
			LinkedList<Vote> votes = new LinkedList<>();
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
					Integer voteTypeId = null;
					Timestamp creationDate = null;
					Integer postId = null;
					Integer userId = null;
					Integer bountyAmount = null;
					while (attributes.hasNext()) {
						Attribute attribute = attributes.next();
						switch (attribute.getName().toString()) {
							case ID: 
								id = Integer.parseInt(attribute.getValue());
								break;
							case POST_ID:
								postId = Integer.parseInt(attribute.getValue());
								break;
							case VOTE_TYPE_ID:
								voteTypeId = Integer.parseInt(attribute.getValue());
								break;
							case CREATION_DATE:
								creationDate = new Timestamp(DatatypeConverter.parseDateTime(attribute.getValue()).getTimeInMillis());
								break;
							case USER_ID:
								userId = Integer.parseInt(attribute.getValue());
								break;
							case BOUNTY_AMOUNT:
								bountyAmount = Integer.parseInt(attribute.getValue());
								break;
						}
					}
					Vote vote = new Vote(id, postId, voteTypeId, creationDate, userId, bountyAmount);
					votes.add(vote);
					recordCount++;
					if (votes.size() == BATCH_SIZE) {
						insertVotesToDb(votes, pstmt);
					}
				}
			}
			if (votes.size() > 0) {
				insertVotesToDb(votes, pstmt);
			}
			System.out.println(recordCount + " records imported to table votes");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private static void insertVotesToDb(LinkedList<Vote> votes,
			PreparedStatement pstmt) throws SQLException {
		Vote bufVote = null;
		while ((bufVote = votes.poll()) != null) {
			//System.out.println(bufVote.toString());
			pstmt.setInt(1, bufVote.id);
			pstmt.setInt(2, bufVote.postId);
			pstmt.setInt(3, bufVote.voteTypeId);
			pstmt.setTimestamp(4, bufVote.creationDate);
			if (bufVote.userId == null) {
				pstmt.setNull(5, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(5, bufVote.userId);
			}
			if (bufVote.bountyAmount == null) {
				pstmt.setNull(6, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(6, bufVote.bountyAmount);
			}
			pstmt.addBatch();
		}
		pstmt.executeBatch();
	}
}
