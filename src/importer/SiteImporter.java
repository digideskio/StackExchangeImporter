package importer;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

import utils.MySqlConnectionUtils;


public class SiteImporter {
	public static void main(String[] args) throws SQLException {
		String host = "host";
		String username = "username";
		String password = "password";
		String db = "stackexchangedb";
		Connection conn = MySqlConnectionUtils.getConnection(host, username, password, db);
		
		String path = "path/to/stack_exchange_dump";
		
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(FileSystems.getDefault().getPath(path))) {
			for (Path p: dirStream) {
				System.out.println(p);
				switch (p.getFileName().toString()) {
				case "Badges.xml":
					System.out.println("Importing badges. Please wait...");
					BadgeImporter.importBadges(p.toString(), conn);
					break;
				case "Comments.xml":
					System.out.println("Importing comments. Please wait...");
					CommentImporter.importComments(p.toString(), conn);
					break;
				case "Posts.xml":
					System.out.println("Importing posts. Please wait...");
					PostImporter.importPosts(p.toString(), conn);
					break;
				case "PostHistory.xml":
					System.out.println("Importing post histories. Not implemented yet!");
					break;
				case "Users.xml":
					System.out.println("Importing users. Please wait...");
					UserImporter.importUsers(p.toString(), conn);
					break;
				case "Votes.xml":
					System.out.println("Importing votes. Please wait...");
					VoteImporter.importVotes(p.toString(), conn);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
