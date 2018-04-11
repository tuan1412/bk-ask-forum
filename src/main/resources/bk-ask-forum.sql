-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: bk-ask-forum
-- ------------------------------------------------------
-- Server version	5.7.21-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `create_at` datetime NOT NULL,
  `last_modified` datetime NOT NULL,
  `vote` int(11) NOT NULL,
  `question_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8frr4bcabmmeyyu60qt7iiblo` (`question_id`),
  KEY `FK68tbcw6bunvfjaoscaj851xpb` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
INSERT INTO `answer` VALUES (1,'Học chắc kiến thức cơ bản. Java core và sql, ...','2018-04-10 12:55:52','2018-04-10 12:55:52',0,1,1),(2,'Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage','2018-04-10 12:57:19','2018-04-10 12:57:19',0,2,1),(3,'The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested','2018-04-10 12:57:36','2018-04-10 12:57:36',0,2,2),(4,'The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested','2018-04-10 12:58:14','2018-04-10 12:58:14',0,3,1),(5,'Content here, content here\', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for \'lorem ipsum\' will uncover many web sites still in their infancy','2018-04-10 12:58:51','2018-04-10 12:58:51',0,4,1),(6,'Content here, content here\', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for \'lorem ipsum\' will uncover many web sites still in their infancy','2018-04-10 13:06:02','2018-04-10 13:06:02',0,2,1);
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Java'),(2,'C#'),(3,'Javascript'),(4,'HTML, CSS'),(5,'SQL');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `seen` bit(1) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb0yvoep4h4k92ipon31wmdf7e` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `create_at` datetime NOT NULL,
  `last_modified` datetime NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `vote` int(11) NOT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7jaqbm9p4prg7n91dd1uabrvj` (`category_id`),
  KEY `FK4ekrlbqiybwk8abhgclfjwnmc` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'Java thì nên học bắt đầu từ đâu ạ?','2018-04-10 10:39:28','2018-04-10 10:39:28','Chia sẻ kinh nghiệm học Java',0,1,1),(2,'Các nền tảng được hỗ trợ bởi Ngôn ngữ lập trình Java?','2018-04-10 10:42:20','2018-04-10 10:42:20','Hỏi đáp về Java',0,1,1),(3,'Liệt kê 5 đặc điểm bất kỳ của Java?','2018-04-10 10:42:52','2018-04-10 10:42:52','Đặc điểm về java',0,1,1),(4,'Là sinh viên năm 4 nên chưa có nhiều kinh nghiệm phỏng vấn, ai từng phỏng vấn vị trí này có thể chia sẻ các kiến thức sẽ gặp khi apply vô vị trí này không?','2018-04-10 10:44:33','2018-04-10 10:44:33','Câu hỏi phỏng vấn cho vị trí .NET Software Engineer?',0,2,1),(5,'Có phải một trang thì có thể có nhiều heading elements; nhưng chỉ có 1 headline và 1 header? Header là phần đầu tiên của trang giống như trong Word đúng không ạ?','2018-04-10 10:45:55','2018-04-10 10:45:55','Sự khác nhau giữa heading, headline và header?',0,4,1);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN'),(2,'MEMBER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `create_at` datetime NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'avatar.png','2018-04-10 10:05:11','tuannguyenanh1412@gmail.com','Nguyễn Anh Tuấn','$2a$10$Fxf4/7HMMRR9zKGgJ6cDbu14cXZZUOjJ8.PXpfm7P3TfTzbHawTjm','tuannguyen1'),(2,'avatar.png','2018-04-10 10:06:39','trong@gmail.com','Nguyễn Văn Trọng','$2a$10$CHbAK33CBn0l7CMN2bBGLeSDM8D6dBboRacl.a5Xzlqqsc27cAXWK','trongnguyen'),(3,'avatar.png','2018-04-10 10:07:00','dangha@gmail.com','Hà Thế Đăng','$2a$10$gQMFQCc1Lp55AvtZ3f8aMeCBL1qWafAmB.TCcucD5NWHlZ0Vb6yUe','hathedang'),(4,'avatar.png','2018-04-10 10:07:14','dangha@gmail.com','Mai Tiến Dũng','$2a$10$Gd.w./W9e85bMhgxe079r.PBL5/qX4IX8q8ih2OtNCLwuL67VyD1S','maitiendung'),(5,'avatar.png','2018-04-10 10:07:41','khaiquang@gmail.com','Trần Quang Khải','$2a$10$XnORfuRgDiJ9TKYWTUQV/ei4Rz0jme8NY6WVs93ceB/eX5dPuJ4qK','tranquangkhai'),(6,'avatar.png','2018-04-10 10:08:14','vanan@gmail.com','Nguyên Văn An','$2a$10$4ZGfUFFmFnyFz4U8/90amu7oM//WX7najByDVTkE1GXMqzBPicGCi','vanan1997');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_follow`
--

DROP TABLE IF EXISTS `user_follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_follow` (
  `following_id` bigint(20) NOT NULL,
  `followed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`following_id`,`followed_id`),
  KEY `FKjqgeeftmad6okylpb11bye2n7` (`followed_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_follow`
--

LOCK TABLES `user_follow` WRITE;
/*!40000 ALTER TABLE `user_follow` DISABLE KEYS */;
INSERT INTO `user_follow` VALUES (1,2),(1,3),(2,1),(2,4),(2,5),(3,1);
/*!40000 ALTER TABLE `user_follow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_question`
--

DROP TABLE IF EXISTS `user_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_question` (
  `user_id` bigint(20) NOT NULL,
  `follow_question_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`follow_question_id`),
  KEY `FKffka9pat3vbi56ceqvmgpt86t` (`follow_question_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_question`
--

LOCK TABLES `user_question` WRITE;
/*!40000 ALTER TABLE `user_question` DISABLE KEYS */;
INSERT INTO `user_question` VALUES (1,2),(1,3),(2,1),(3,4),(5,3);
/*!40000 ALTER TABLE `user_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1),(1,2),(2,2),(3,2),(4,2),(5,2),(6,2);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-11 23:27:26
