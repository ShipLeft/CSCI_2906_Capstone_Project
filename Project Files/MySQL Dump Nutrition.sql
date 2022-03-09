-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: nutrition
-- ------------------------------------------------------
-- Server version	5.7.37-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `groups` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name_UNIQUE` (`Name`),
  UNIQUE KEY `Id_UNIQUE` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (4,'Burger'),(10,'Burrito'),(5,'Cereal'),(9,'Ice Cream'),(1,'Meat'),(2,'Pasta'),(3,'Pizza'),(7,'Rice'),(8,'Sandwich'),(6,'Soda');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nutrients`
--

DROP TABLE IF EXISTS `nutrients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nutrients` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Calories` int(11) DEFAULT NULL,
  `Proteins` int(11) DEFAULT NULL,
  `Fats` double DEFAULT NULL,
  `Carbs` int(11) DEFAULT NULL,
  `Sodium` int(11) DEFAULT NULL,
  `GroupId` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Names_UNIQUE` (`Name`),
  KEY `GroupId_idx` (`GroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nutrients`
--

LOCK TABLES `nutrients` WRITE;
/*!40000 ALTER TABLE `nutrients` DISABLE KEYS */;
INSERT INTO `nutrients` VALUES (1,'Kraft Mac-N-Cheese',250,8,4.5,44,580,2),(2,'Bar S Hot Dog',140,4,12,4,580,1),(3,'Maruchan Beef Ramen',190,4,7,26,790,2),(4,'Pasta Roni Fettuccine Alfredo',200,7,3.5,36,590,2),(5,'USDA Choice Lean Prime Rib',330,23,27,0,75,1),(6,'Little Ceasars Sausage Pizza',270,13,11,31,510,3),(7,'Hot N Spicy McChicken',420,14,22,39,590,4),(8,'Frosted Flakes',130,2,0,33,190,5),(9,'Golden Puffs',110,2,0,24,65,5),(10,'Panda Express Orange Chicken',383,14,17.5,43,620,1),(11,'Mtn Dew Baja Blast',170,0,0,44,55,6),(12,'Dr. Pepper',150,0,0,40,55,6),(13,'Pepsi Cola',150,0,0,41,30,6),(14,'Panda Express Fried Rice',520,11,16,85,850,7),(15,'Elevation Burger Grilled Cheese',360,16,16,42,640,8),(16,'Carls Jr. Super Star',940,48,57,59,1550,4),(17,'McDonalds Vanilla Ice Cream Cone',200,5,5,33,80,9),(19,'Beef and Bean Chimichanga',330,9,17,36,440,10),(20,'Tina\'s Beef and Bean Burrito',330,9,17,37,450,10),(21,'Tina\'s Bean & Cheese Burrito',260,9,7,39,510,10),(23,'Tina\'s Green Chili Bean & Cheese Burrito',240,8,5,40,390,10),(24,'Jamoca Almond Fudge Baskin Robbins',260,5,14,30,75,9),(25,'Tina\'s Big Bean & Cheese Burrito',320,11,9,50,640,10),(26,'El Monterey Steak Burrito',280,12,8,41,440,10);
/*!40000 ALTER TABLE `nutrients` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-03-09  9:09:41
