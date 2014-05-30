CREATE DATABASE  IF NOT EXISTS `db19r3708gdzx5d1` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `db19r3708gdzx5d1`;
-- MySQL dump 10.13  Distrib 5.6.13, for osx10.6 (i386)
--
-- Host: 127.0.0.1    Database: db19r3708gdzx5d1
-- ------------------------------------------------------
-- Server version	5.6.13

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
-- Table structure for table `AdminAccountDao`
--

DROP TABLE IF EXISTS `AdminAccountDao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AdminAccountDao` (
  `id` bigint(8) NOT NULL AUTO_INCREMENT,
  `creationTime` datetime DEFAULT NULL,
  `lastLogin` datetime DEFAULT NULL,
  `reference` varchar(200) DEFAULT NULL,
  `privilege` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `phone_UNIQUE` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AdminAccountDao`
--

LOCK TABLES `AdminAccountDao` WRITE;
/*!40000 ALTER TABLE `AdminAccountDao` DISABLE KEYS */;
INSERT INTO `AdminAccountDao` VALUES (1,'2014-05-30 15:18:52','2014-05-30 15:18:52','dsfdsf',1,0,'Harry','123445676543','1000:f2891ecc94f9aa16ec8df509c01367596aff5db05dafd05e9eb541f7c958db4c:0c819c3d387dfb21cb61b79828344c5e80b73b7cd9288c5915b1e84db98afd57'),(2,'2014-05-30 15:18:52','2014-05-30 15:18:52','dsfsersf',2,0,'Mattan','12344','1000:d427a9a0ea36e6c00c1c177262728b6e12740d1a966319fe5d7fd5a96c317117:134585fe4b675bd6f58b98a713874c4018cb1eac1431389afe1bf373cc53645f'),(3,'2014-05-30 15:18:52','2014-05-30 15:18:52','dsfs5656ersf',0,0,'Fan','2323','1000:0bf4dcd65ead48e88b70116780a11983a50cd838653b85a7ca7f53c3095c71f6:e95c29ec4aeeb03eca2df9f380dc3a9d90ba4f4caeae2b7b297b2da17fc11f6b');
/*!40000 ALTER TABLE `AdminAccountDao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BookingDao`
--

DROP TABLE IF EXISTS `BookingDao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BookingDao` (
  `id` bigint(8) NOT NULL AUTO_INCREMENT,
  `adjustTime` datetime DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `u_Id` bigint(8) DEFAULT NULL,
  `p_Id` bigint(8) DEFAULT NULL,
  `course_Id` bigint(8) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `reference` varchar(100) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `transaction_Id` bigint(8) DEFAULT NULL,
  `coupon_Id` bigint(8) DEFAULT NULL,
  `admin_Id` bigint(8) DEFAULT NULL,
  `scheduledTime` datetime DEFAULT NULL,
  `wasConfirmed` int(3) DEFAULT NULL,
  `actionRecord` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BookingDao`
--

LOCK TABLES `BookingDao` WRITE;
/*!40000 ALTER TABLE `BookingDao` DISABLE KEYS */;
INSERT INTO `BookingDao` VALUES (1,'2014-05-30 00:00:00',1000,1,1,1,'2014-05-30 15:18:52','Harry','123545451',0,'4trg45rt','xiongchuhanplace@hotmail.com',-1,1,-1,'2014-05-30 00:00:00',0,''),(2,'2014-05-30 00:00:00',1000,1,2,2,'2014-05-30 15:18:52','Harry','12335451',1,'iuyiohy89','xiongchuhanplace@hotmail.com',-1,2,-1,'2014-05-22 00:00:00',0,''),(3,'2014-05-30 00:00:00',1000,2,3,3,'2014-05-30 15:18:52','Matt','12354',3,'ihjgijrth','uwse@me',-1,2,-1,'2014-05-22 00:00:00',0,''),(4,'2014-05-30 00:00:00',1000,3,2,2,'2014-05-30 15:18:52','Fan','12335451',4,'regtret','uwse@me',-1,-1,-1,'2014-05-22 00:00:00',0,''),(5,'2014-05-30 00:00:00',1000,4,1,1,'2014-05-30 15:18:52','Jiang','123545451',2,'klpupkl','45r4235@me',-1,-1,-1,'2014-05-30 00:00:00',0,''),(6,'2014-05-30 00:00:00',1000,2,2,2,'2014-05-30 15:18:52','Matt','12335451',5,'fgjuifug','uwse@me',-1,4,-1,'2014-05-22 00:00:00',0,'');
/*!40000 ALTER TABLE `BookingDao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CouponDao`
--

DROP TABLE IF EXISTS `CouponDao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CouponDao` (
  `couponId` bigint(8) NOT NULL AUTO_INCREMENT,
  `bookingId` bigint(8) DEFAULT NULL,
  `transactionId` bigint(8) DEFAULT NULL,
  `userId` bigint(8) NOT NULL,
  `creationTime` datetime NOT NULL,
  `expireTime` datetime NOT NULL,
  `status` int(3) NOT NULL,
  `amount` int(8) NOT NULL,
  PRIMARY KEY (`couponId`),
  UNIQUE KEY `couponId_UNIQUE` (`couponId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CouponDao`
--

LOCK TABLES `CouponDao` WRITE;
/*!40000 ALTER TABLE `CouponDao` DISABLE KEYS */;
INSERT INTO `CouponDao` VALUES (1,1,-1,1,'2014-05-30 15:18:52','2014-05-30 15:18:52',0,50),(2,3,-1,2,'2014-05-30 15:18:52','2014-05-30 15:18:52',1,50),(3,4,-1,3,'2014-05-30 15:18:52','2014-05-30 15:18:52',2,50),(4,5,-1,4,'2014-05-30 15:18:52','2014-05-30 15:18:52',0,50),(5,2,-1,1,'2014-05-30 15:18:52','2014-05-30 15:18:52',0,50);
/*!40000 ALTER TABLE `CouponDao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CourseDao`
--

DROP TABLE IF EXISTS `CourseDao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CourseDao` (
  `id` bigint(8) NOT NULL AUTO_INCREMENT,
  `p_Id` bigint(8) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `finishTime` datetime DEFAULT NULL,
  `t_Intro` varchar(500) DEFAULT NULL,
  `t_ImgUrl` varchar(500) DEFAULT NULL,
  `classroomImgUrl` varchar(500) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `seatsTotal` int(11) DEFAULT NULL,
  `seatsLeft` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `subCategory` varchar(200) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `district` varchar(50) DEFAULT NULL,
  `reference` varchar(200) DEFAULT NULL,
  `courseIntro` varchar(1000) DEFAULT NULL,
  `classModel` int(3) DEFAULT NULL,
  `hasDownloadMaterials` int(11) DEFAULT NULL,
  `quiz` varchar(200) DEFAULT NULL,
  `certification` varchar(200) DEFAULT NULL,
  `openCourseRequirement` varchar(500) DEFAULT NULL,
  `questionBank` varchar(500) DEFAULT NULL,
  `suitableStudent` varchar(500) DEFAULT NULL,
  `prerequest` varchar(200) DEFAULT NULL,
  `highScoreReward` varchar(200) DEFAULT NULL,
  `extracurricular` varchar(500) DEFAULT NULL,
  `courseName` varchar(200) DEFAULT NULL,
  `dailyStartTime` varchar(45) DEFAULT NULL,
  `dailyFinishTime` varchar(45) DEFAULT NULL,
  `studyDaysNote` varchar(200) DEFAULT NULL,
  `courseHourNum` int(3) DEFAULT NULL,
  `courseHourLength` int(3) DEFAULT NULL,
  `partnerCourseReference` varchar(200) DEFAULT NULL,
  `classroomIntro` varchar(200) DEFAULT NULL,
  `partnerQualification` int(11) DEFAULT NULL,
  `partnerIntro` varchar(500) DEFAULT NULL,
  `t_Methods` varchar(500) DEFAULT NULL,
  `t_MaterialType` int(11) DEFAULT NULL,
  `t_MaterialCost` int(11) DEFAULT NULL,
  `t_MaterialFree` int(11) DEFAULT NULL,
  `t_MaterialIntro` varchar(500) DEFAULT NULL,
  `questionBankIntro` varchar(200) DEFAULT NULL,
  `passAgreement` varchar(200) DEFAULT NULL,
  `provideAssignments` int(11) DEFAULT NULL,
  `provideMarking` int(11) DEFAULT NULL,
  `extracurricularIntro` varchar(200) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `t_MethodsIntro` varchar(200) DEFAULT NULL,
  `studyDays` varchar(45) DEFAULT NULL,
  `t_MaterialName` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CourseDao`
--

LOCK TABLES `CourseDao` WRITE;
/*!40000 ALTER TABLE `CourseDao` DISABLE KEYS */;
INSERT INTO `CourseDao` VALUES (1,1,'2014-05-30 15:18:52','2014-05-30 15:18:52','2014-06-07 15:18:52','','','',1000,50,5,0,'Physics','sub-Phy','','','','','',0,0,'','','',NULL,'','','',NULL,'','','','',-1,-1,'','',1,'',NULL,1,-1,1,'','','',0,0,'','12344565654','',NULL,''),(2,2,'2014-05-30 15:18:52','2014-05-22 15:18:52','2014-05-30 15:18:52','','','',1000,50,5,1,'Chinese','sub-Chin','','','','','',0,0,'','','',NULL,'','','',NULL,'','','','',-1,-1,'','',1,'',NULL,1,-1,1,'','','',0,0,'','12344565654','',NULL,''),(3,3,'2014-05-30 15:18:52','2014-05-22 15:18:52','2014-05-23 15:18:52','','','',1000,50,5,2,'French','sub-French','','','','','',0,0,'','','',NULL,'','','',NULL,'','','','',-1,-1,'','',1,'',NULL,1,-1,1,'','','',0,0,'','12344565654','',NULL,'');
/*!40000 ALTER TABLE `CourseDao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CreditDao`
--

DROP TABLE IF EXISTS `CreditDao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CreditDao` (
  `creditId` bigint(8) NOT NULL AUTO_INCREMENT,
  `bookingId` bigint(8) NOT NULL,
  `userId` bigint(8) NOT NULL,
  `creationTime` datetime NOT NULL,
  `expireTime` datetime NOT NULL,
  `amount` int(8) NOT NULL,
  `status` int(3) NOT NULL,
  `usableTime` datetime NOT NULL,
  PRIMARY KEY (`creditId`),
  UNIQUE KEY `creditId_UNIQUE` (`creditId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CreditDao`
--

LOCK TABLES `CreditDao` WRITE;
/*!40000 ALTER TABLE `CreditDao` DISABLE KEYS */;
INSERT INTO `CreditDao` VALUES (1,1,1,'2014-05-30 15:18:52','2014-05-30 15:18:52',50,2,'2014-05-30 15:18:52'),(2,3,2,'2014-05-30 15:18:52','2014-05-30 15:18:52',50,3,'2014-05-30 15:18:52'),(3,4,3,'2014-05-30 15:18:52','2014-05-30 15:18:52',50,1,'2014-05-30 15:18:52'),(4,5,4,'2014-05-30 15:18:52','2014-05-30 15:18:52',50,2,'2014-05-30 15:18:52'),(5,2,1,'2014-05-30 15:18:52','2014-05-30 15:18:52',50,3,'2014-05-30 15:18:52');
/*!40000 ALTER TABLE `CreditDao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PartnerDao`
--

DROP TABLE IF EXISTS `PartnerDao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PartnerDao` (
  `id` bigint(8) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `licence` varchar(200) NOT NULL,
  `organizationNum` varchar(200) DEFAULT NULL,
  `reference` varchar(100) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `lastLogin` datetime DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `instName` varchar(200) NOT NULL,
  `logoUrl` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `licence_UNIQUE` (`licence`),
  UNIQUE KEY `reference_UNIQUE` (`reference`),
  UNIQUE KEY `phone_UNIQUE` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PartnerDao`
--

LOCK TABLES `PartnerDao` WRITE;
/*!40000 ALTER TABLE `PartnerDao` DISABLE KEYS */;
INSERT INTO `PartnerDao` VALUES (1,'XDF','234fdsfsdgergf-dsv,.!@','1235454361234','dsf4r','1000:1938184f819ef177e087b04773221977352f74346f02a46447c4c085c37f5bd4:49b395775314c1d6f74dcb438e16278f2584f488417b217638a49a1deaceed73','2014-05-30 15:18:52','2014-05-30 15:18:52','123545451',0,'xiaofeng',''),(2,'HQYS','2sdfdsf34545dsfsdgergf-dsv,.!@','12334361234','dsdsfr','1000:8b5b1a6a1e89d787a2e2f80bc2276cda9a02c148a899b4301b8c265a2a495bc9:d77d0edd6ea11b2c68a597ace5d20499ffc1690ab1a86e854f54a2c4cb0e343b','2014-05-30 15:18:52','2014-05-30 15:18:52','12335451',1,'daofeng',''),(3,'XDFs','234fdsfv,.!@','1235454361234','d4r','1000:4bfcbb0c31230ed7336b1dacc75807814f2713386cd2247f4dda82f34766924d:badfe64674288c27091b5c0e80503493a3be440ef5e48e4e48c2540ca5c4d6df','2014-05-30 15:18:52','2014-05-30 15:18:52','12354',2,'xiaofeng','');
/*!40000 ALTER TABLE `PartnerDao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TransactionDao`
--

DROP TABLE IF EXISTS `TransactionDao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TransactionDao` (
  `transactionId` bigint(8) NOT NULL AUTO_INCREMENT,
  `userId` bigint(8) NOT NULL,
  `bookingId` bigint(8) NOT NULL,
  `creationTime` datetime NOT NULL,
  `amount` int(8) NOT NULL,
  `transactionType` int(3) NOT NULL,
  `couponId` bigint(8) DEFAULT NULL,
  PRIMARY KEY (`transactionId`),
  UNIQUE KEY `transactionId_UNIQUE` (`transactionId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TransactionDao`
--

LOCK TABLES `TransactionDao` WRITE;
/*!40000 ALTER TABLE `TransactionDao` DISABLE KEYS */;
INSERT INTO `TransactionDao` VALUES (1,1,1,'2014-05-30 15:18:52',2000,0,1),(2,2,3,'2014-05-30 15:18:52',20,1,2),(3,3,4,'2014-05-30 15:18:52',2300000,2,-1),(4,4,5,'2014-05-30 15:18:52',998,2,-1);
/*!40000 ALTER TABLE `TransactionDao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserDao`
--

DROP TABLE IF EXISTS `UserDao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserDao` (
  `id` bigint(8) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `lastLogin` datetime DEFAULT NULL,
  `balance` int(11) NOT NULL,
  `coupon` int(11) NOT NULL,
  `credit` int(11) NOT NULL,
  `email` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `phone_UNIQUE` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserDao`
--

LOCK TABLES `UserDao` WRITE;
/*!40000 ALTER TABLE `UserDao` DISABLE KEYS */;
INSERT INTO `UserDao` VALUES (1,'Harry','18502877744','2014-05-30 15:18:51','1000:28ed9f4f61010eb544e62c93f8bc906abd932e16e8bda477ca326368aa39e92d:3a74dd128f4aa1588fa9b9372897e522a6395a50833d466db5f8fa0126ca2d75',0,'2014-05-30 15:18:51',0,0,0,'xiongchuhanplace@hotmail.com'),(2,'Matt','1324234234','2014-05-30 15:18:52','1000:2b5c6f2f1046334e28aab3a6ea141463810896f3e89073eb0949bf28078f83c2:81e5cb90afd99434a68f0fe57a0267f6949048ac031371808ee8448da8bb442a',0,'2014-05-30 15:18:52',0,0,0,'uwse@me'),(3,'Fan','1345452342','2014-05-30 15:18:52','1000:157bc8a73e24629a8a6852e2ded7b1a8814a985ed5ce6d21a63e3eebe8d6084b:4ce4ffde398bf3327257f494ef79b7d7216d1a40a53bc6524bb97ef60d0b5a5b',1,'2014-05-30 15:22:38',0,0,0,'uwse@me'),(4,'Jiang','1344354355452','2014-05-30 15:18:52','1000:dfdc7251487f565dd02012e9970ca0bb78280a4e22b3022aa26a42c9fab5f9a0:74e9f9ac0c8dd8a1bd0caf59c2646d5c0c7e11b6e9adb4840440a372ed976f37',2,'2014-05-30 15:18:52',0,0,0,'45r4235@me');
/*!40000 ALTER TABLE `UserDao` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-05-30 15:35:38
