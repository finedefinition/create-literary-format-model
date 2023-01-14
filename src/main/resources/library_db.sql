CREATE SCHEMA IF NOT EXISTS `library_db` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `library_db`;
DROP TABLE IF EXISTS `authors`;
CREATE TABLE `authors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
DROP TABLE IF EXISTS `literary_formats`;
CREATE TABLE `literary_formats` (
  `format` varchar(255) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
DROP TABLE IF EXISTS `books`;
CREATE TABLE `books` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `literary_format_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `books_literary_formats_fk` (`literary_format_id`),
  CONSTRAINT `books_literary_formats_fk` FOREIGN KEY (`literary_format_id`) REFERENCES `literary_formats` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
DROP TABLE IF EXISTS `books_authors`;
CREATE TABLE `books_authors` (
  `book_id` bigint NOT NULL,
  `author_id` bigint NOT NULL,
  KEY `books_authors_books_fk` (`book_id`),
  KEY `books_authors_authors_fk` (`author_id`),
  CONSTRAINT `books_authors_authors_fk` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`),
  CONSTRAINT `books_authors_books_fk` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;







