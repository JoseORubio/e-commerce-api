CREATE DATABASE  IF NOT EXISTS `e-commerce` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `e-commerce`;
-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: e-commerce
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `carrinho`
--

DROP TABLE IF EXISTS `carrinho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrinho` (
  `id` binary(16) NOT NULL,
  `usuario_id` binary(16) NOT NULL,
  `produto_id` binary(16) NOT NULL,
  `quantidade` int NOT NULL,
  `valor_total_produto` decimal(8,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_idusu_carrinho_idx` (`usuario_id`),
  KEY `fk_idpro_carrinho_idx` (`produto_id`),
  CONSTRAINT `fk_idpro_carrinho` FOREIGN KEY (`produto_id`) REFERENCES `produtos` (`id`),
  CONSTRAINT `fk_idusu_carrinho` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrinho`
--

LOCK TABLES `carrinho` WRITE;
/*!40000 ALTER TABLE `carrinho` DISABLE KEYS */;
INSERT INTO `carrinho` VALUES (0xC7821C2068E540099355BF3925CAA17A,0x836F431F6E12412EA170AC6D39081293,0x4DC3C79033CD418FA2B3E88C6E237757,2,4.00),(0xE697C39C014142EBAE725C567F54965D,0x836F431F6E12412EA170AC6D39081293,0x8FCFE445864C4D96B8C07D75ACB45DC5,2,12.88);
/*!40000 ALTER TABLE `carrinho` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `carrinho_view`
--

DROP TABLE IF EXISTS `carrinho_view`;
/*!50001 DROP VIEW IF EXISTS `carrinho_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `carrinho_view` AS SELECT 
 1 AS `Usuario`,
 1 AS `Produto`,
 1 AS `Quantidade`,
 1 AS `Valor Total`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `papeis`
--

DROP TABLE IF EXISTS `papeis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `papeis` (
  `id` binary(16) NOT NULL,
  `nome` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome_UNIQUE` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `papeis`
--

LOCK TABLES `papeis` WRITE;
/*!40000 ALTER TABLE `papeis` DISABLE KEYS */;
INSERT INTO `papeis` VALUES (0x10F90C0015BC11EE976994C6914CB639,'ROLE_ADMIN'),(0x15124CFE15BC11EE976994C6914CB639,'ROLE_USER');
/*!40000 ALTER TABLE `papeis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `papeis_do_usuario`
--

DROP TABLE IF EXISTS `papeis_do_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `papeis_do_usuario` (
  `id` binary(16) NOT NULL,
  `id_usuario` binary(16) NOT NULL,
  `id_papel` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pdo_idusu_idx` (`id_usuario`),
  KEY `fk_pdo_idpap_idx` (`id_papel`),
  CONSTRAINT `fk_pdo_idpap` FOREIGN KEY (`id_papel`) REFERENCES `papeis` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_pdo_idusu` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='		';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `papeis_do_usuario`
--

LOCK TABLES `papeis_do_usuario` WRITE;
/*!40000 ALTER TABLE `papeis_do_usuario` DISABLE KEYS */;
INSERT INTO `papeis_do_usuario` VALUES (0x1219294A732B4D28B6F2153E5FEAAC5B,0xC786D2C4EA724C20BA53C3F4590FD29C,0x15124CFE15BC11EE976994C6914CB639),(0x1C9FE8009F0B4C0799E742F6EF9E1772,0x2ECCC0D98B8A4A5FB533D09607BE458E,0x15124CFE15BC11EE976994C6914CB639),(0x2C325CD8BEA447C9B9B26D2FB2BA0B4C,0xDCEEBA4AB80C42AC8511FAE017A42BD5,0x15124CFE15BC11EE976994C6914CB639),(0x35EE59E22C1F45D38C5F7FB325D5B053,0xF49EF2EBFE1A404C91EE912170E3C722,0x15124CFE15BC11EE976994C6914CB639),(0x41CE5E1F3E8211EE81A794C6914CB639,0x2EB8D4602AC348F0BB2E72CBD20C206C,0x10F90C0015BC11EE976994C6914CB639),(0x4D953EE71E5B11EE976994C6914CB639,0x44B2111CDE7A42B29AAF40688E59D771,0x10F90C0015BC11EE976994C6914CB639),(0x7B502C18C7A447CEB35A89A7C08D73B3,0x8F0CA30D90664245A9624F0086926E3C,0x15124CFE15BC11EE976994C6914CB639),(0x7BDE289B3E8211EE81A794C6914CB639,0x9C05AD193BA94FD4815B3DF604882C1F,0x10F90C0015BC11EE976994C6914CB639),(0xA5BE4E793E8211EE81A794C6914CB639,0x8F0CA30D90664245A9624F0086926E3C,0x10F90C0015BC11EE976994C6914CB639),(0xAD682BC8B04548D6AA6843C3DA6E6573,0x836F431F6E12412EA170AC6D39081293,0x15124CFE15BC11EE976994C6914CB639),(0xB27FE5398596407B8DE0C5BE0B59824C,0x7E14F00CAB424AD3A052091E996132A9,0x15124CFE15BC11EE976994C6914CB639),(0xBF59006C05DF4A8EB561475B4CF01747,0x2EB8D4602AC348F0BB2E72CBD20C206C,0x15124CFE15BC11EE976994C6914CB639),(0xCED0CB8320B911EE976994C6914CB639,0x45A3BF6AF0754F38A1C42E32CEF0829E,0x15124CFE15BC11EE976994C6914CB639),(0xE3539BEE66314F3181E53CBDC0910F73,0x9C05AD193BA94FD4815B3DF604882C1F,0x15124CFE15BC11EE976994C6914CB639),(0xED923EF9543A48D0B2A2D66EDA6BE758,0x9F842F3E6C834C1F89C4B520944905BA,0x15124CFE15BC11EE976994C6914CB639);
/*!40000 ALTER TABLE `papeis_do_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `papeis_do_usuario_view`
--

DROP TABLE IF EXISTS `papeis_do_usuario_view`;
/*!50001 DROP VIEW IF EXISTS `papeis_do_usuario_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `papeis_do_usuario_view` AS SELECT 
 1 AS `Usuario`,
 1 AS `Papel`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `produtos`
--

DROP TABLE IF EXISTS `produtos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produtos` (
  `id` binary(16) NOT NULL,
  `nome` varchar(80) NOT NULL,
  `quantidade_estoque` int NOT NULL,
  `preco` decimal(6,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome_UNIQUE` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtos`
--

LOCK TABLES `produtos` WRITE;
/*!40000 ALTER TABLE `produtos` DISABLE KEYS */;
INSERT INTO `produtos` VALUES (0x02E177C81D324FFB984F63E3E8A4E966,'Blusa A',495,10.00),(0x27D51B100A99476688CE62FC75CD55A9,'Blusa D',497,40.00),(0x4D82F9C60EB5404584B267B032B7D528,'Camisa B',142,33.33),(0x4DC3C79033CD418FA2B3E88C6E237757,'Calça B',18,2.00),(0x65E02E2960C742C5812CE6E09A389886,'Blusa F',0,40.00),(0x6BB93022563A4E01AC6F9752559447B7,'Bermuda B',36,87.50),(0x6CD8D2315BEE45ECAF05D2D92D52CA65,'Tenis C',7,38.99),(0x7A736314B93147DAB2EE2237EAF7D108,'Calça A',2,123.00),(0x83AC7DD301944E2F8F6B7506F4E879C8,'Tênis A',482,9599.99),(0x8675AC53A8F147C9B09E5CB24CA4C9B7,'Casaco A',149,69.44),(0x8FCFE445864C4D96B8C07D75ACB45DC5,'Tênis B',76,6.44),(0x9DA57F64DC4B4D6DB39B426FFEA66E3B,'Blusa C',36,87.50),(0xB3FB5B20A5A84C9295AAF915E622281F,'Tenis D',23,30.99),(0xB47861F25AE047719D528D45BFEC6ACB,'Bermuda A',402,900.00),(0xB7545ABEEEC2453788FC63667782789C,'Meia B',49,6.44),(0xB9A8CDD0DBFF4F578E3F5AF0C01E1CF7,'Meia C',84,0.25),(0xD390905389C249F6AE8B1931E28D7C97,'Calça D',122,2.00),(0xD3BC42767E1D418EA12720D9CD9A7204,'Meia A',2,599.99),(0xD64A5966F0B24ED7940B0E810276E547,'Camisa A',46,81.56),(0xE76F9522B4BF49569C6BC57BBFACC3EE,'Calça C',122,2.00),(0xF9C473BFAFB84EB29D32D1C2B9498408,'Blusa B',324,87.50);
/*!40000 ALTER TABLE `produtos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produtos_da_venda`
--

DROP TABLE IF EXISTS `produtos_da_venda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produtos_da_venda` (
  `id_item_venda` binary(16) NOT NULL,
  `id_venda` binary(16) NOT NULL,
  `id_produto` binary(16) NOT NULL,
  `quantidade` int NOT NULL,
  `valor_total_produto` decimal(8,2) NOT NULL,
  PRIMARY KEY (`id_item_venda`),
  KEY `id_produto_idx` (`id_produto`),
  KEY `id_venda_idx` (`id_venda`),
  CONSTRAINT `id_produto` FOREIGN KEY (`id_produto`) REFERENCES `produtos` (`id`),
  CONSTRAINT `id_venda` FOREIGN KEY (`id_venda`) REFERENCES `vendas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtos_da_venda`
--

LOCK TABLES `produtos_da_venda` WRITE;
/*!40000 ALTER TABLE `produtos_da_venda` DISABLE KEYS */;
INSERT INTO `produtos_da_venda` VALUES (0x1AAE5E62E7AD420F8EEF944EE3C713ED,0xCD7B53D1E4F847A988C3E380198BA223,0xD390905389C249F6AE8B1931E28D7C97,1,2.00),(0x1D7C197F417F4BB4B0F952BF2910CED0,0xCD7B53D1E4F847A988C3E380198BA223,0x8FCFE445864C4D96B8C07D75ACB45DC5,1,6.44),(0x3AF72C15B550437C8B993A751211DAF0,0x5B6F97B58F504A25BEA9EE2D11084711,0x6BB93022563A4E01AC6F9752559447B7,3,19.32),(0x47E7777669AD48C795DEEEB9E6FD75CF,0x1B8E49663E494BCEBFFBB475AD4CE07F,0x8675AC53A8F147C9B09E5CB24CA4C9B7,2,138.88),(0x4C9F1B978BEB42EE92B8B4942453DCAA,0x6E217BADC93A4A1E88204B90F5AF5C38,0x02E177C81D324FFB984F63E3E8A4E966,5,50.00),(0x56A16685CB434616BF289DA153794621,0x5B6F97B58F504A25BEA9EE2D11084711,0x4DC3C79033CD418FA2B3E88C6E237757,3,6.00),(0x59D22ED0D88944C4BCB499BE8523E77D,0x09922CDB68744EAEB713847139CB752B,0x6BB93022563A4E01AC6F9752559447B7,1,6.44),(0x8ADEDD95E59A4DE5A3E75A589A2D7CEB,0x3A25D82D4CCA4E4A8A4638C7A12053E8,0xB7545ABEEEC2453788FC63667782789C,7,45.08),(0x8B9E11DD8E374DFFB275020CE1252C68,0xE9EF361482CD4B6B9DEE16A6917A0670,0x8FCFE445864C4D96B8C07D75ACB45DC5,1,6.44),(0x8E6AAF6179AE425DB892B987259D045E,0x3A25D82D4CCA4E4A8A4638C7A12053E8,0xF9C473BFAFB84EB29D32D1C2B9498408,2,175.00),(0x990493080AD24B08BD26410FB5E8517B,0x4C532FF3FE044A049CE8FBE2950C7E87,0x6BB93022563A4E01AC6F9752559447B7,2,12.88),(0x9AE4E2C943F14867B95AC9D82CFB5539,0xD34C4C7C49A04B2BBDA3EFBE47B17E63,0x4DC3C79033CD418FA2B3E88C6E237757,22,44.00),(0xA2D0E04559D34688AD195551E2A646C4,0x870EA137950E4F68870E34973F896F81,0xE76F9522B4BF49569C6BC57BBFACC3EE,1,2.00),(0xA2ECE305A3F5480283AF02C365EEA139,0x46B8B304E9F1453688F0D7703520CA63,0x8FCFE445864C4D96B8C07D75ACB45DC5,4,25.76),(0xB2A2C4107EE3434585D5702CDE8C48E9,0x750B444361334F15999ECC54E1B2F579,0x6BB93022563A4E01AC6F9752559447B7,3,19.32),(0xB473450A79834E2A9180E805F6244FE6,0x6E217BADC93A4A1E88204B90F5AF5C38,0x6BB93022563A4E01AC6F9752559447B7,5,32.20),(0xB886BF55186F48A2AC95739D3C2AF598,0x46B8B304E9F1453688F0D7703520CA63,0xB47861F25AE047719D528D45BFEC6ACB,8,7200.00),(0xCE82D72046AD44589AF2ABA1EDAD058F,0x56A023B62838435580E54674587BBE95,0x6BB93022563A4E01AC6F9752559447B7,1,6.44),(0xD6F4522BD24743C2839B64043EF0F6A4,0x5B6F97B58F504A25BEA9EE2D11084711,0x27D51B100A99476688CE62FC75CD55A9,3,120.00),(0xD8CC4B54AC0246A0AF7FBD8D08B60506,0x1B8E49663E494BCEBFFBB475AD4CE07F,0x8FCFE445864C4D96B8C07D75ACB45DC5,1,6.44),(0xE366F73B12CD4A1AAE7A2EFDF7163AAA,0xD34C4C7C49A04B2BBDA3EFBE47B17E63,0x83AC7DD301944E2F8F6B7506F4E879C8,1,9599.99),(0xED0CA93274E7474CBB7BE3FA5AA18A00,0xD34C4C7C49A04B2BBDA3EFBE47B17E63,0xB7545ABEEEC2453788FC63667782789C,22,141.68);
/*!40000 ALTER TABLE `produtos_da_venda` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `produtos_da_venda_BEFORE_INSERT` BEFORE INSERT ON `produtos_da_venda` FOR EACH ROW BEGIN
declare valor_total DECIMAL(8,2) default 0;
set valor_total = completa_valor_produto(new.id_produto, new.quantidade);
set new.valor_total_produto = valor_total;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `produtos_da_venda_AFTER_INSERT` AFTER INSERT ON `produtos_da_venda` FOR EACH ROW BEGIN
declare var_valor_total DECIMAL(8,2) default 0;
set var_valor_total = (select valor_total from vendas where vendas.id = new.id_venda) + new.valor_total_produto;
update vendas set valor_total = var_valor_total where vendas.id = new.id_venda;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `produtos_da_venda_BEFORE_UPDATE` BEFORE UPDATE ON `produtos_da_venda` FOR EACH ROW BEGIN
declare valor_total DECIMAL(8,2) default 0;
set valor_total = completa_valor_produto(new.id_produto, new.quantidade);
set new.valor_total_produto = valor_total;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `produtos_da_venda_AFTER_UPDATE` AFTER UPDATE ON `produtos_da_venda` FOR EACH ROW BEGIN
declare var_valor_total DECIMAL(8,2) default 0;
set var_valor_total = (select sum(valor_total_produto) from produtos_da_venda where id_venda = new.id_venda);
update vendas set valor_total = var_valor_total where vendas.id = new.id_venda;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `produtos_da_venda_AFTER_DELETE` AFTER DELETE ON `produtos_da_venda` FOR EACH ROW BEGIN
declare var_valor_total DECIMAL(8,2) default 0;
set var_valor_total = (select sum(valor_total_produto) from produtos_da_venda where id_venda = old.id_venda);
update vendas set valor_total = var_valor_total where vendas.id = old.id_venda;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Temporary view structure for view `produtos_vendidos`
--

DROP TABLE IF EXISTS `produtos_vendidos`;
/*!50001 DROP VIEW IF EXISTS `produtos_vendidos`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `produtos_vendidos` AS SELECT 
 1 AS `Usuario`,
 1 AS `Produtos`,
 1 AS `Quantidade`,
 1 AS `Valor total`,
 1 AS `Data da venda`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` binary(16) NOT NULL,
  `nome` varchar(80) NOT NULL,
  `login` varchar(20) NOT NULL,
  `senha` varchar(70) NOT NULL,
  `cpf` varchar(11) NOT NULL,
  `data_nasc` date NOT NULL,
  `data_cadastro` datetime NOT NULL,
  `sexo` char(1) NOT NULL,
  `telefone` varchar(14) NOT NULL,
  `email` varchar(50) NOT NULL,
  `cep` varchar(9) NOT NULL,
  `uf` varchar(2) NOT NULL,
  `cidade` varchar(30) NOT NULL,
  `rua` varchar(70) NOT NULL,
  `numero_rua` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (0x2EB8D4602AC348F0BB2E72CBD20C206C,'Bernardo Kauê da Silva','berna123','$2a$10$ypcHPpQBRsRFeohPxM8m1uPCxxZlo0io7f65Hgdes/8.BXfiCp24O','59105639042','1975-01-16','2023-05-24 12:51:44','M','(45)12345-1234','bernardo-dasilva98@hotmail.co.jp','80210-140','PR','Curitiba','Jardinete Rutílio de Sá Ribas',981),(0x2ECCC0D98B8A4A5FB533D09607BE458E,'Levi Igor Osvaldo Galvão','livii123','$2a$10$0Tl/2VduEOfKDXG7qJNGWOLpxpdkyYMZUoFcpoFSyIEKnlXgGEClO','67490399874','2003-02-08','2023-07-15 18:31:50','M','(11)99822-9663','levi.igor.galvao@yahoo.com.br','06756-080','SP','Taboão da Serra','Rua Etiópia',651),(0x44B2111CDE7A42B29AAF40688E59D771,'Maya Fabiana Laura Vianaaaa','mayaf123','$2a$10$0kAMvnI4u0rilAO2brHI1eULAll.lBVCXu1J2fTgTSI19GutNT1bW','48998214091','1958-02-04','2023-05-24 12:53:45','F','(65)99693-4575','mayafabianaviaaana@trilhavitoria.com.br','73805-445','GO','Formosa','Rua 8',421),(0x45A3BF6AF0754F38A1C42E32CEF0829E,'Tomás Francisco André Novaes','tomas123','$2a$10$0kAMvnI4u0rilAO2brHI1eULAll.lBVCXu1J2fTgTSI19GutNT1bW','35848540080','1987-03-13','2023-06-17 13:33:31','M','(92)98386-3200','tomas@trilhavitoria.com.br','60510-712','CE','Fortaleza','Travessa Nostradamus',50),(0x7E14F00CAB424AD3A052091E996132A9,'Artur Ferraz','artur123','$2a$10$kEowILiUduFFpvvnVZJRGuznfdZNJrxLQoylhdcCiGx2R7C/ZfBnG','68497813081','2002-02-26','2023-05-24 12:50:05','M','(45)12345-1234','artur@pibnet.com.br','63010-010','CE','Juazeiro do Norte','Rua São Pedro',22),(0x836F431F6E12412EA170AC6D39081293,'Aurora Antonella Yasmin de Paula','aurora123','$2a$10$R6YIwkfUj2HUO0a.kbaJXurLI8eoCHRy7c7OWoeR3HWqyQfriLLiC','87773610240','1963-06-13','2023-07-15 18:25:54','F','(21)98620-6951','aurora_depaula@gustavoscoelho.com.br','21725-270','RJ','Rio de Janeiro','Rua Sândalo',981),(0x8F0CA30D90664245A9624F0086926E3C,'Benedito Juan Felipe Sales','bened123','$2a$10$K3UzNbc7TAdIiozqlOJHZe9leli3wefvke88JoFNvK/FSwNthDcAW','98153379470','2034-11-30','2023-07-09 14:16:17','M','(69)98856-5123','benedito.juan.sales@gerj.com.br','80210-140','PR','Curitiba','Jardinete Rutílio de Sá Ribas',22),(0x9C05AD193BA94FD4815B3DF604882C1F,'Tereza Vitória Sophia Pires','teres123','$2a$10$k.mQBSzIJ8y1OY6Fp8nKfOtWMo6Zft02B8us0zCRnbI4J.JAbpkQ6','94149784884','1992-01-11','2023-07-15 18:33:45','F','(34)98676-1922','tereza-pires80@yahool.com.br','38181-770','MG','Araxá','Rua Altino Costa',1621),(0x9F842F3E6C834C1F89C4B520944905BA,'Tatiane Lúcia Aparício','tatia123','$2a$10$g1fad7bFWlPHFoLl8iPHZuHRlyTCiTFTh01IEyFyF9VYiOVSGJmTq','68692178586','1961-04-19','2023-07-15 18:32:10','F','(69)98838-5024','tatiane_aparicio@wizardararaquara.com.br','76964-026','RO','Cacoal','Avenida Afonso Pena',651),(0xC786D2C4EA724C20BA53C3F4590FD29C,'Eliane Rayssa Renata Caldeira','eliane123','$2a$10$hF.fpjXhHlxwxRbRWcwY0eQFc28pmnl36tGh/PNSQd3IlvcIMIl/W','10691740097','1991-05-04','2023-07-15 18:28:07','F','(63)98273-0299','eliane_caldeira@dbacomp.com.br','77015-028','TO','Palmas','Quadra ACSO 1 Avenida LO 1',179),(0xDCEEBA4AB80C42AC8511FAE017A42BD5,'Andrea Simone Raquel Cardoso','andrea123','$2a$10$1nsF8HaOjqB7lh3fgTA0wOow62WMWJsl6k5sZYMshihTDVFM/zuoq','20259721808','1970-02-22','2023-07-15 18:34:59','F','(67)99156-1981','andrea_simone_cardoso@hp.com','79604-030','MS','Três Lagoas','Rua João de Almeida Barros',2884),(0xF49EF2EBFE1A404C91EE912170E3C722,'Nelson Ian José Barbosa','nelso123','$2a$10$G7MKnwdjpHrHGY3PeLrWHO8P4zOnICa6bK7nXv2v.83t8PDcOHQeO','42877169146','1992-02-17','2023-07-15 18:38:00','M','(82)99633-7803','nelson-barbosa71@quarttus.com.br','78080-420','MT','Cuiabá','Rua dos Tucanos',1795);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendas`
--

DROP TABLE IF EXISTS `vendas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vendas` (
  `id` binary(16) NOT NULL,
  `id_usuario` binary(16) NOT NULL,
  `valor_total` decimal(8,2) NOT NULL DEFAULT '0.00',
  `data_venda` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_id_usuario_idx` (`id_usuario`),
  CONSTRAINT `fk_id_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendas`
--

LOCK TABLES `vendas` WRITE;
/*!40000 ALTER TABLE `vendas` DISABLE KEYS */;
INSERT INTO `vendas` VALUES (0x09922CDB68744EAEB713847139CB752B,0x45A3BF6AF0754F38A1C42E32CEF0829E,6.44,'2023-07-12 13:46:42'),(0x1B8E49663E494BCEBFFBB475AD4CE07F,0x2EB8D4602AC348F0BB2E72CBD20C206C,145.32,'2023-08-06 17:49:16'),(0x3A25D82D4CCA4E4A8A4638C7A12053E8,0x2EB8D4602AC348F0BB2E72CBD20C206C,220.08,'2023-07-25 13:09:45'),(0x46B8B304E9F1453688F0D7703520CA63,0x7E14F00CAB424AD3A052091E996132A9,7225.76,'2023-08-19 12:21:14'),(0x4C532FF3FE044A049CE8FBE2950C7E87,0x45A3BF6AF0754F38A1C42E32CEF0829E,12.88,'2023-07-12 13:47:32'),(0x56A023B62838435580E54674587BBE95,0x45A3BF6AF0754F38A1C42E32CEF0829E,6.44,'2023-07-12 13:54:32'),(0x5B6F97B58F504A25BEA9EE2D11084711,0x7E14F00CAB424AD3A052091E996132A9,145.32,'2023-07-12 14:11:39'),(0x6E217BADC93A4A1E88204B90F5AF5C38,0x45A3BF6AF0754F38A1C42E32CEF0829E,82.20,'2023-07-12 13:49:36'),(0x750B444361334F15999ECC54E1B2F579,0x45A3BF6AF0754F38A1C42E32CEF0829E,19.32,'2023-07-12 13:48:29'),(0x870EA137950E4F68870E34973F896F81,0x7E14F00CAB424AD3A052091E996132A9,2.00,'2023-07-25 14:43:05'),(0xCD7B53D1E4F847A988C3E380198BA223,0x7E14F00CAB424AD3A052091E996132A9,8.44,'2023-08-06 13:19:05'),(0xD34C4C7C49A04B2BBDA3EFBE47B17E63,0x7E14F00CAB424AD3A052091E996132A9,9785.67,'2023-07-14 12:58:39'),(0xE9EF361482CD4B6B9DEE16A6917A0670,0x2EB8D4602AC348F0BB2E72CBD20C206C,6.44,'2023-08-06 14:03:38');
/*!40000 ALTER TABLE `vendas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `vendas_view`
--

DROP TABLE IF EXISTS `vendas_view`;
/*!50001 DROP VIEW IF EXISTS `vendas_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vendas_view` AS SELECT 
 1 AS `Usuario`,
 1 AS `Valor Total`,
 1 AS `Data da Venda`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'e-commerce'
--
/*!50003 DROP FUNCTION IF EXISTS `completa_valor_produto` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `completa_valor_produto`(id_produto binary(16), quantidade int) RETURNS decimal(8,2)
    DETERMINISTIC
BEGIN
declare preco_produto DECIMAL(6,2) default 0;
declare valor_total DECIMAL(8,2) default 0;
select preco into preco_produto from produtos where produtos.id = id_produto;
set valor_total = preco_produto * quantidade;
return valor_total;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `carrinho_view`
--

/*!50001 DROP VIEW IF EXISTS `carrinho_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `carrinho_view` AS select `usuarios`.`nome` AS `Usuario`,`produtos`.`nome` AS `Produto`,`carrinho`.`quantidade` AS `Quantidade`,`carrinho`.`valor_total_produto` AS `Valor Total` from ((`usuarios` join `produtos`) join `carrinho`) where ((`usuarios`.`id` = `carrinho`.`usuario_id`) and (`produtos`.`id` = `carrinho`.`produto_id`)) order by `usuarios`.`nome` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `papeis_do_usuario_view`
--

/*!50001 DROP VIEW IF EXISTS `papeis_do_usuario_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `papeis_do_usuario_view` AS select `usuarios`.`nome` AS `Usuario`,`papeis`.`nome` AS `Papel` from ((`usuarios` join `papeis`) join `papeis_do_usuario`) where ((`usuarios`.`id` = `papeis_do_usuario`.`id_usuario`) and (`papeis`.`id` = `papeis_do_usuario`.`id_papel`)) order by `usuarios`.`nome` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `produtos_vendidos`
--

/*!50001 DROP VIEW IF EXISTS `produtos_vendidos`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `produtos_vendidos` AS select `usuarios`.`nome` AS `Usuario`,`produtos`.`nome` AS `Produtos`,`pdv`.`quantidade` AS `Quantidade`,`pdv`.`valor_total_produto` AS `Valor total`,`vendas`.`data_venda` AS `Data da venda` from (((`usuarios` join `produtos`) join `vendas`) join `produtos_da_venda` `pdv`) where ((`usuarios`.`id` = `vendas`.`id_usuario`) and (`produtos`.`id` = `pdv`.`id_produto`) and (`vendas`.`id` = `pdv`.`id_venda`)) order by `usuarios`.`nome`,`produtos`.`nome`,`vendas`.`data_venda` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vendas_view`
--

/*!50001 DROP VIEW IF EXISTS `vendas_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vendas_view` AS select `usuarios`.`nome` AS `Usuario`,`vendas`.`valor_total` AS `Valor Total`,`vendas`.`data_venda` AS `Data da Venda` from (`usuarios` join `vendas`) where (`usuarios`.`id` = `vendas`.`id_usuario`) order by `usuarios`.`nome`,`vendas`.`data_venda` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-19 10:15:26
