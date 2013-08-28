SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;


CREATE TABLE IF NOT EXISTS `badges` (
  `ID` mediumint(8) unsigned NOT NULL,
  `USERID` mediumint(9) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `DATE` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `USERID` (`USERID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `comments` (
  `ID` int unsigned NOT NULL,
  `POSTID` int unsigned NOT NULL,
  `SCORE` smallint(5) unsigned NULL,
  `TEXT` text COLLATE utf8_unicode_ci NOT NULL,
  `CREATIONDATE` datetime NOT NULL,
  `USERDISPLAYNAME` varchar(30) NULL,
  `USERID` mediumint(8) unsigned NULL,
  PRIMARY KEY (`ID`),
  KEY `POSTID` (`POSTID`,`USERID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `posts` (
  `ID` int unsigned NOT NULL,
  `POSTTYPEID` enum('1','2','3','4','5','6','7','8') COLLATE utf8_unicode_ci NOT NULL,
  `PARENTID` mediumint(8) unsigned DEFAULT NULL,
  `ACCEPTEDANSWERID` mediumint(8) unsigned DEFAULT NULL,
  `CREATIONDATE` datetime NOT NULL,
  `SCORE` smallint(5) NOT NULL,
  `VIEWCOUNT` mediumint(8) unsigned NULL,
  `BODY` text COLLATE utf8_unicode_ci NOT NULL,
  `OWNERUSERID` mediumint(9) NULL,
  `OWNERDISPLAYNAME` tinytext COLLATE utf8_unicode_ci NULL,
  `LASTEDITORUSERID` mediumint(9) NULL,
  `LASTEDITORDISPLAYNAME` tinytext COLLATE utf8_unicode_ci NULL,
  `LASTEDITDATE` datetime NULL,
  `LASTACTIVITYDATE` datetime NOT NULL,
  `COMMUNITYOWNEDDATE` datetime NULL,
  `CLOSEDDATE` datetime NULL,
  `TITLE` tinytext COLLATE utf8_unicode_ci NULL,
  `TAGS` varchar(500) COLLATE utf8_unicode_ci NULL,
  `ANSWERCOUNT` smallint(5) unsigned NULL,
  `COMMENTCOUNT` tinyint(10) unsigned NULL,
  `FAVORITECOUNT` smallint(10) unsigned NULL,
  PRIMARY KEY (`ID`),
  KEY `PARENTID` (`PARENTID`),
  KEY `OWNERUSERID` (`OWNERUSERID`),
  KEY `SCORE` (`SCORE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `posthistory` (
  `ID` int unsigned NOT NULL,
  `POSTHISTORYTYPEID` tinyint unsigned NOT NULL,
  `POSTID` int unsigned NOT NULL,
  `REVISIONGUID` char(36) COLLATE utf8_unicode_ci NOT NULL,
  `CREATIONDATE` datetime NOT NULL,
  `USERID` mediumint(9) NULL,
  `USERDISPLAYNAME` tinytext COLLATE utf8_unicode_ci NULL,
  `COMMENT` varchar(400) COLLATE utf8_unicode_ci NULL,
  `TEXT` text COLLATE utf8_unicode_ci NULL,
  PRIMARY KEY (`ID`),
  KEY `USERID` (`USERID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `users` (
  `ID` mediumint(9) NOT NULL,
  `REPUTATION` mediumint(8) unsigned NOT NULL,
  `CREATIONDATE` datetime NOT NULL,
  `DISPLAYNAME` tinytext COLLATE utf8_unicode_ci NOT NULL,
  `EMAILHASH` char(32) COLLATE utf8_unicode_ci NULL,
  `LASTACCESSDATE` datetime NOT NULL,
  `WEBSITEURL` varchar(255) COLLATE utf8_unicode_ci NULL,
  `LOCATION` varchar(255) COLLATE utf8_unicode_ci NULL,
  `AGE` tinyint(3) unsigned NULL,
  `ABOUTME` text COLLATE utf8_unicode_ci NULL,
  `VIEWS` mediumint(8) unsigned NOT NULL,
  `UPVOTES` mediumint(8) unsigned NOT NULL,
  `DOWNVOTES` mediumint(8) unsigned NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `votes` (
  `ID` mediumint(8) unsigned NOT NULL,
  `POSTID` mediumint(8) unsigned NOT NULL,
  `VOTETYPEID` enum('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16') COLLATE utf8_unicode_ci NOT NULL,
  `CREATIONDATE` datetime NOT NULL,
  `USERID` mediumint(8) DEFAULT NULL,
  `BOUNTYAMOUNT` smallint(5) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `USERID` (`USERID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;