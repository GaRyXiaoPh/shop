-- MySQL dump 10.13  Distrib 5.7.12, for osx10.11 (x86_64)
--
-- Host: 47.104.75.111    Database: lmmall
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
-- Table structure for table `bbt_system_city`
--

DROP TABLE IF EXISTS `bbt_system_city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbt_system_city` (
  `sid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `City_Name` varchar(64) NOT NULL COMMENT '城市名称',
  `Pro_ID` bigint(11) NOT NULL COMMENT '所属省份',
  `City_Sort` int(6) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=389 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='后台城市表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbt_system_country`
--

DROP TABLE IF EXISTS `bbt_system_country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbt_system_country` (
  `sid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Country_Name` varchar(64) NOT NULL COMMENT '国家名称',
  `Country_CODE` varchar(32) DEFAULT NULL COMMENT '国家代码',
  `Province_Remark` varchar(100) NOT NULL COMMENT '备注',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='所属国家';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbt_system_district`
--

DROP TABLE IF EXISTS `bbt_system_district`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbt_system_district` (
  `sid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `district_Name` varchar(64) NOT NULL COMMENT '区或街道名称',
  `city_ID` bigint(11) NOT NULL COMMENT '所属市份',
  `district_Sort` int(6) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=3245 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='后台区或街道表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbt_system_province`
--

DROP TABLE IF EXISTS `bbt_system_province`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbt_system_province` (
  `sid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Province_Name` varchar(64) NOT NULL COMMENT '省份名称',
  `Province_Sort` int(6) DEFAULT NULL COMMENT '排序',
  `Province_Remark` varchar(100) NOT NULL COMMENT '备注',
  `Country_ID` bigint(11) NOT NULL COMMENT '所属国家',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='后台省表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `btc_address`
--

DROP TABLE IF EXISTS `btc_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `btc_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(100) DEFAULT NULL COMMENT '账户ID',
  `name` varchar(32) DEFAULT NULL COMMENT '币名',
  `address` varchar(100) NOT NULL COMMENT '当前节点申请的地址',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `address` (`address`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `btc_conf_kv`
--

DROP TABLE IF EXISTS `btc_conf_kv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `btc_conf_kv` (
  `keyex` varchar(128) NOT NULL COMMENT 'key',
  `valueex` varchar(128) DEFAULT NULL COMMENT 'value',
  `mark` varchar(128) DEFAULT NULL COMMENT '说明',
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最新时间',
  PRIMARY KEY (`keyex`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `btc_transaction`
--

DROP TABLE IF EXISTS `btc_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `btc_transaction` (
  `hash` varchar(100) NOT NULL COMMENT '交易hash',
  `block` int(20) DEFAULT NULL COMMENT '区块号',
  `from` varchar(100) DEFAULT NULL,
  `to` varchar(100) DEFAULT NULL COMMENT '到账地址',
  `amount` varchar(36) DEFAULT NULL COMMENT '到账金额',
  `recvTime` datetime DEFAULT NULL COMMENT '区块时间',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`hash`),
  KEY `idx_hash` (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eth_address`
--

DROP TABLE IF EXISTS `eth_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eth_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(128) DEFAULT NULL COMMENT '账户',
  `name` varchar(128) DEFAULT NULL COMMENT '币名',
  `address` varchar(128) DEFAULT NULL COMMENT '地址(Eth)',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eth_transaction`
--

DROP TABLE IF EXISTS `eth_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eth_transaction` (
  `hash` varchar(128) NOT NULL COMMENT '交易hash',
  `from` varchar(128) DEFAULT NULL COMMENT '转出地址',
  `to` varchar(128) DEFAULT NULL COMMENT '转入地址',
  `value` varchar(36) DEFAULT NULL COMMENT '值',
  `address` varchar(128) DEFAULT NULL COMMENT '合约地址',
  `blockHash` varchar(128) DEFAULT NULL COMMENT '区块哈希',
  `blockNumber` varchar(36) DEFAULT NULL COMMENT '区块号',
  `timestamp` datetime DEFAULT NULL COMMENT '交易时间',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_admin`
--

DROP TABLE IF EXISTS `tb_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_admin` (
  `id` char(36) NOT NULL COMMENT '账号id',
  `account` varchar(30) DEFAULT NULL COMMENT '账号',
  `username` varchar(50) DEFAULT NULL COMMENT '名称',
  `position` varchar(50) DEFAULT NULL COMMENT '职位名称',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `salt` varchar(255) DEFAULT NULL COMMENT '盐值',
  `status` char(1) DEFAULT '0' COMMENT '状态(0:正常 1:禁用)',
  `creator` char(36) NOT NULL COMMENT '创建人ID',
  `remark` text COMMENT '备注',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastTime` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='管理后台用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_admin_login`
--

DROP TABLE IF EXISTS `tb_admin_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_admin_login` (
  `id` char(36) NOT NULL,
  `userId` char(36) DEFAULT NULL COMMENT '用户id',
  `token` text COMMENT 'token',
  `loginTime` datetime DEFAULT NULL COMMENT '登录时间',
  `expireTime` datetime DEFAULT NULL COMMENT '到期时间',
  `status` char(1) DEFAULT NULL COMMENT '0:登录  1:退出',
  `lastTime` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='管理后台用户登录信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_admin_resources`
--

DROP TABLE IF EXISTS `tb_admin_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_admin_resources` (
  `res_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资源名称',
  `res_level` tinyint(2) NOT NULL COMMENT '资源类别(1:一级菜单,2:二级菜单,3:三级菜单)',
  `url` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资源URL',
  `pre_id` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `sort_index` int(6) DEFAULT NULL COMMENT '排序(菜单顺序)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间 ',
  `last_up_time` datetime DEFAULT NULL COMMENT '最后更新时间 ',
  `status` tinyint(2) DEFAULT '0' COMMENT '菜单状态（0:开启，1:关闭）',
  PRIMARY KEY (`res_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_admin_role`
--

DROP TABLE IF EXISTS `tb_admin_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_admin_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `create_user_id` char(36) NOT NULL COMMENT '创建用户编号',
  `des` varchar(512) DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间 ',
  `last_up_time` datetime DEFAULT NULL COMMENT '最后更新时间 ',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '角色状态(0:正常 1:禁用)',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `inx_osr_role_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_admin_role_res`
--

DROP TABLE IF EXISTS `tb_admin_role_res`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_admin_role_res` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `res_id` int(11) NOT NULL COMMENT '资源ID',
  PRIMARY KEY (`role_id`,`res_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='角色资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_admin_user_role`
--

DROP TABLE IF EXISTS `tb_admin_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_admin_user_role` (
  `user_id` char(36) NOT NULL COMMENT '用户编号',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='用户角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_advertise`
--

DROP TABLE IF EXISTS `tb_advertise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_advertise` (
  `id` char(36) NOT NULL COMMENT 'id',
  `name` varchar(100) DEFAULT NULL COMMENT '广告名称',
  `position` varchar(50) DEFAULT NULL COMMENT '广告位置',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` int(11) DEFAULT NULL COMMENT '0:上线  1:下线',
  `url` varchar(500) DEFAULT NULL COMMENT '广告图片',
  `link` varchar(512) DEFAULT NULL COMMENT '广告链接',
  `remark` text COMMENT '备注',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `type` int(11) DEFAULT NULL COMMENT '类型：0:链接  1:图文',
  `content` text DEFAULT NULL COMMENT '图文富文本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='广告信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_app_version`
--

DROP TABLE IF EXISTS `tb_app_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_app_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platform` varchar(32) NOT NULL COMMENT '操作系统类型(ios,android)',
  `code` int(4) NOT NULL COMMENT '版本代码',
  `versionName` varchar(16) NOT NULL COMMENT '版本号',
  `force` int(1) unsigned NOT NULL COMMENT '是否强制更新, 0: 不强制 1: 强制',
  `url` varchar(256) NOT NULL COMMENT 'app下载地址',
  `content` varchar(1024) NOT NULL COMMENT '版本说明,描述这个版本改进了什么问题',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='app版本表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_circle_comment`
--

DROP TABLE IF EXISTS `tb_circle_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_circle_comment` (
  `id` varchar(50) NOT NULL COMMENT 'id',
  `order_id` varchar(100) DEFAULT NULL COMMENT '订单号',
  `shop_id` varchar(36) DEFAULT NULL COMMENT '店铺id',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户id',
  `score` int(11) DEFAULT NULL COMMENT '用户评分',
  `pics` text COMMENT '评论图片',
  `content` text COMMENT '评论内容',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='商圈用户评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_circle_goods`
--

DROP TABLE IF EXISTS `tb_circle_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_circle_goods` (
  `id` char(36) DEFAULT NULL COMMENT '商品id',
  `shopId` char(40) DEFAULT NULL COMMENT '店铺id',
  `userId` char(36) DEFAULT NULL COMMENT '商户id',
  `name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `price` double(10,2) DEFAULT '0.00' COMMENT '商品价格',
  `status` int(11) DEFAULT '1' COMMENT '商品状态( 1:待审核  2:已上架  3:已下架  4:未通过 )',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `on_time` datetime DEFAULT NULL COMMENT '上架时间',
  `off_time` datetime DEFAULT NULL COMMENT '下架时间',
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='商圈商品信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_circle_goods_pic`
--

DROP TABLE IF EXISTS `tb_circle_goods_pic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_circle_goods_pic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图片id',
  `good_id` varchar(50) DEFAULT NULL COMMENT '商品id',
  `type` int(11) DEFAULT NULL COMMENT '图片类型(0:商品图片  1:商品详情)',
  `url` varchar(100) DEFAULT NULL COMMENT '图片地址',
  `is_main` int(11) DEFAULT NULL COMMENT '主图(0:主图 1:辅图)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='商品图片表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_circle_order_detail`
--

DROP TABLE IF EXISTS `tb_circle_order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_circle_order_detail` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` varchar(50) NOT NULL COMMENT '订单号(商品)',
  `shop_id` varchar(40) NOT NULL COMMENT '店铺id',
  `userId` varchar(50) NOT NULL COMMENT '用户id',
  `good_no` varchar(50) DEFAULT NULL COMMENT '商品编号',
  `good_name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `good_main_pic` varchar(100) DEFAULT NULL COMMENT '商品主图',
  `price` double(11,2) DEFAULT NULL COMMENT '商品单价',
  `num` int(11) DEFAULT NULL COMMENT '商品数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='商圈订单详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_circle_shop_type`
--

DROP TABLE IF EXISTS `tb_circle_shop_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_circle_shop_type` (
  `id` char(36) NOT NULL COMMENT '商户类型',
  `name` varchar(50) DEFAULT NULL COMMENT '类型名称',
  `url` varchar(150) DEFAULT NULL COMMENT '图片路径',
  `sort` int(10) DEFAULT NULL COMMENT '排序',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='商圈商户分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_circle_trade`
--

DROP TABLE IF EXISTS `tb_circle_trade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_circle_trade` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `userId` varchar(50) NOT NULL COMMENT '购买者id',
  `shop_id` varchar(36) NOT NULL COMMENT '店铺id',
  `total_price` double(11,2) DEFAULT NULL COMMENT '商品总价',
  `rate` double(11,2) DEFAULT NULL COMMENT '人民币转莱姆币费率',
  `lyme` double(11,2) DEFAULT NULL COMMENT '需付莱姆币',
  `actual_lyme` double(11,2) DEFAULT NULL COMMENT '实际支付莱姆币',
  `status` int(11) DEFAULT '0' COMMENT '订单状态(0:已下单未支付  1:支付成功  2:支付失败)',
  `mark` int(11) DEFAULT '0' COMMENT '标识(0:我要买单订单  1:立即购买订单)',
  `order_time` datetime DEFAULT NULL COMMENT '下单时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='商圈订单交易流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_conf_apis`
--

DROP TABLE IF EXISTS `tb_conf_apis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_conf_apis` (
  `id` varchar(36) NOT NULL COMMENT '接口ID',
  `url` varchar(36) DEFAULT NULL COMMENT '接口URL',
  `urlMark` varchar(100) DEFAULT NULL COMMENT '接口说明',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_conf_roles`
--

DROP TABLE IF EXISTS `tb_conf_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_conf_roles` (
  `id` varchar(36) NOT NULL COMMENT '角色ID',
  `rolesName` varchar(36) DEFAULT NULL COMMENT '角色名称',
  `rolesMark` varchar(100) DEFAULT NULL COMMENT '角色说明',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_im_friend`
--

DROP TABLE IF EXISTS `tb_im_friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_im_friend` (
  `id` char(36) NOT NULL,
  `userId` char(36) DEFAULT NULL COMMENT '自己ID',
  `friendId` char(36) DEFAULT NULL COMMENT '好友ID',
  `star` char(1) DEFAULT '0' COMMENT '星级好友：1 是， 0 否',
  `invisibleFriend` char(1) DEFAULT '0',
  `invisibleMe` char(1) DEFAULT '0',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_FRIEND_FRI_USER` (`userId`,`friendId`),
  KEY `idx_friend_userId` (`userId`) USING BTREE,
  KEY `idx_friend_friendId` (`friendId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_im_friend_add`
--

DROP TABLE IF EXISTS `tb_im_friend_add`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_im_friend_add` (
  `id` char(36) NOT NULL,
  `userId` char(36) DEFAULT NULL,
  `friendId` char(36) DEFAULT NULL,
  `status` char(1) DEFAULT NULL COMMENT '0 未确认，1 接受, 2 拒绝\n            ',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_im_friend_invitation`
--

DROP TABLE IF EXISTS `tb_im_friend_invitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_im_friend_invitation` (
  `id` char(36) NOT NULL COMMENT '序列号',
  `userId` char(36) DEFAULT NULL COMMENT '用户id',
  `friendId` char(36) DEFAULT NULL COMMENT '要添加好友ID',
  `message` varchar(200) DEFAULT NULL COMMENT '消息',
  `status` char(1) DEFAULT NULL COMMENT '0 未确认，1 接受, 2 拒绝\r\n            ',
  `msgStatus` char(1) DEFAULT NULL COMMENT '消息状态(相对新朋友的状态)：0 未读，1 已读',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_friend_invitation_userId` (`userId`) USING BTREE,
  KEY `idx_friend_invitation_friendId` (`friendId`) USING BTREE,
  KEY `idx_friend_invitation_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='好友申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_im_group_apply`
--

DROP TABLE IF EXISTS `tb_im_group_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_im_group_apply` (
  `id` char(36) NOT NULL,
  `groupId` char(36) DEFAULT NULL,
  `message` varchar(200) DEFAULT NULL,
  `status` char(1) DEFAULT NULL COMMENT '申请状态:0 初始化，1 同意，2 拒绝',
  `creator` char(36) DEFAULT NULL COMMENT '申请人ID',
  `createTime` datetime DEFAULT NULL,
  `operatorId` char(36) DEFAULT NULL,
  `operateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_group_apply_groupId` (`groupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_im_group_chat`
--

DROP TABLE IF EXISTS `tb_im_group_chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_im_group_chat` (
  `id` char(36) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '群名称',
  `master` char(36) DEFAULT NULL COMMENT '群主id',
  `creator` char(36) DEFAULT NULL COMMENT '创建人id',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `board` text COMMENT '公告',
  `status` char(1) DEFAULT NULL COMMENT '群聊状态：0 正常,1 解散',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_chat_master` (`master`) USING BTREE,
  KEY `idx_group_chat_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_im_group_member`
--

DROP TABLE IF EXISTS `tb_im_group_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_im_group_member` (
  `id` char(36) NOT NULL,
  `groupId` char(36) DEFAULT NULL,
  `userId` char(36) DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL COMMENT '成员用户自定义的群昵称',
  `isManager` char(1) DEFAULT '' COMMENT '是否群管理员:0  否，1 是',
  `creator` char(36) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_group_member_groupId` (`groupId`) USING BTREE,
  KEY `idx_group_member_userId` (`userId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_im_hate_user`
--

DROP TABLE IF EXISTS `tb_im_hate_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_im_hate_user` (
  `id` char(36) NOT NULL,
  `userId` char(36) DEFAULT NULL,
  `hateId` char(36) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_hate_user_userId` (`userId`),
  KEY `idx_hate_user_hateId` (`hateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_im_moment`
--

DROP TABLE IF EXISTS `tb_im_moment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_im_moment` (
  `id` char(36) NOT NULL,
  `text` varchar(1500) DEFAULT NULL COMMENT '限制最多500个汉字',
  `img0` varchar(100) DEFAULT NULL,
  `img1` varchar(100) DEFAULT NULL,
  `img2` varchar(100) DEFAULT NULL,
  `img3` varchar(100) DEFAULT NULL,
  `img4` varchar(100) DEFAULT NULL,
  `img5` varchar(100) DEFAULT NULL,
  `img6` varchar(100) DEFAULT NULL,
  `img7` varchar(100) DEFAULT NULL,
  `img8` varchar(100) DEFAULT NULL,
  `creator` char(36) DEFAULT NULL,
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_moment_creator` (`creator`) USING HASH,
  KEY `idx_moment_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_im_moment_comment`
--

DROP TABLE IF EXISTS `tb_im_moment_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_im_moment_comment` (
  `id` char(36) NOT NULL,
  `momentId` char(36) DEFAULT NULL,
  `type` char(1) DEFAULT NULL COMMENT '类型：0 回复，1 评论',
  `parentId` char(36) DEFAULT NULL,
  `text` varchar(1000) DEFAULT NULL,
  `creator` char(36) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_moment_comment_momentId` (`momentId`),
  KEY `idx_moment_comment_createTime` (`createTime`),
  KEY `idx_moment_comment_creator` (`creator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_im_moment_thumb`
--

DROP TABLE IF EXISTS `tb_im_moment_thumb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_im_moment_thumb` (
  `id` char(36) NOT NULL,
  `momentId` char(36) DEFAULT NULL,
  `creator` char(36) DEFAULT NULL,
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_moment_thumb_momentId` (`momentId`),
  KEY `idx_moment_thumb_createTime` (`createTime`),
  KEY `idx_moment_thumb_creator` (`creator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_im_rong_token`
--

DROP TABLE IF EXISTS `tb_im_rong_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_im_rong_token` (
  `id` char(36) NOT NULL,
  `userId` char(36) DEFAULT NULL,
  `token` varchar(250) DEFAULT NULL,
  `status` char(1) DEFAULT NULL COMMENT '状态：0 正常,1 无效',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_im_user_remark`
--

DROP TABLE IF EXISTS `tb_im_user_remark`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_im_user_remark` (
  `id` char(36) NOT NULL,
  `user_id` char(36) DEFAULT NULL COMMENT '用户id',
  `friend_id` char(36) DEFAULT NULL COMMENT '好友id',
  `remark_name` varchar(100) DEFAULT NULL COMMENT '备注名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_USER_REMARK` (`user_id`,`friend_id`),
  KEY `idx_user_remark_userId` (`user_id`) USING BTREE,
  KEY `idx_user_remark_creator` (`friend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='用户好友名称备注表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_logistics`
--

DROP TABLE IF EXISTS `tb_logistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_logistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `logistics_no` varchar(45) DEFAULT NULL COMMENT '运单号',
  `com` varchar(45) DEFAULT NULL COMMENT '物流公司',
  `logistics_status` int(11) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_LOGISTICS_NO` (`logistics_no`,`com`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='物流主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_logistics_detail`
--

DROP TABLE IF EXISTS `tb_logistics_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_logistics_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `logistics_no` varchar(45) DEFAULT NULL COMMENT '物流号',
  `ftime` varchar(45) DEFAULT NULL COMMENT '格式化的时间',
  `logistics_context` varchar(200) DEFAULT NULL COMMENT '物流详情',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='物流详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_moments`
--

DROP TABLE IF EXISTS `tb_moments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_moments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(500) NOT NULL COMMENT '发布的内容',
  `address_lon` varchar(32) DEFAULT NULL COMMENT '经度',
  `address_lat` varchar(32) DEFAULT NULL COMMENT '纬度',
  `location` varchar(100) DEFAULT NULL COMMENT '详细地址',
  `user_id` varchar(36) NOT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='莱粉圈主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_moments_comment`
--

DROP TABLE IF EXISTS `tb_moments_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_moments_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reply_id` bigint(20) DEFAULT '0' COMMENT '回复ID,为0表示顶层评论',
  `moments_id` bigint(20) NOT NULL COMMENT '莱粉圈ID',
  `user_id` varchar(36) NOT NULL COMMENT '评论人ID',
  `content` varchar(500) NOT NULL COMMENT '发布的内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='莱粉圈评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_moments_img`
--

DROP TABLE IF EXISTS `tb_moments_img`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_moments_img` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `moments_id` bigint(20) NOT NULL COMMENT '莱粉圈ID',
  `url` varchar(500) NOT NULL COMMENT '图片url',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='莱粉圈信息图片表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_moments_like`
--

DROP TABLE IF EXISTS `tb_moments_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_moments_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `moments_id` bigint(20) NOT NULL COMMENT '莱粉圈ID',
  `user_id` varchar(36) NOT NULL COMMENT '点赞人的ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='莱粉圈点赞信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_national_code`
--

DROP TABLE IF EXISTS `tb_national_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_national_code` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nationalName` varchar(50) DEFAULT NULL,
  `chineseName` varchar(50) DEFAULT NULL,
  `abbre` varchar(10) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  `land` varchar(255) DEFAULT NULL,
  `price` decimal(20,8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_password_transaction`
--

DROP TABLE IF EXISTS `tb_password_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_password_transaction` (
  `id` char(36) NOT NULL,
  `password` varchar(200) DEFAULT NULL,
  `salt` varchar(100) DEFAULT NULL,
  `creator` char(36) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `lastTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_creator` (`creator`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_redpaper`
--

DROP TABLE IF EXISTS `tb_redpaper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_redpaper` (
  `id` varchar(36) NOT NULL,
  `sendId` varchar(36) NOT NULL COMMENT '发红包用户ID',
  `targetId` varchar(36) NOT NULL COMMENT '目标ID',
  `amount` decimal(20,8) NOT NULL COMMENT '红包金额',
  `number` int(11) NOT NULL COMMENT '红包个数',
  `words` varchar(150) DEFAULT NULL COMMENT '红包留言',
  `type` tinyint(4) NOT NULL COMMENT '红包类型1,单聊红包;2群聊通用红包;3群聊拼手气红包',
  `state` tinyint(4) NOT NULL COMMENT '红包状态 1:有效 2:失效',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastTime` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_redpaper_receive`
--

DROP TABLE IF EXISTS `tb_redpaper_receive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_redpaper_receive` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sendId` varchar(36) NOT NULL COMMENT '发红包用户ID',
  `redpaperId` varchar(36) NOT NULL COMMENT '红包ID',
  `receiveId` varchar(36) NOT NULL DEFAULT '0' COMMENT '接收者ID',
  `sortNo` int(11) NOT NULL DEFAULT '0' COMMENT '顺序id',
  `amount` decimal(20,8) NOT NULL COMMENT '红包金额',
  `words` varchar(150) DEFAULT NULL COMMENT '红包留言',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastTime` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=940 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_authdata`
--

DROP TABLE IF EXISTS `tb_shop_authdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_authdata` (
  `id` varchar(36) NOT NULL,
  `userId` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `shopId` varchar(36) DEFAULT NULL COMMENT '店铺ID',
  `company` varchar(255) DEFAULT NULL COMMENT '公司名称',
  `province` bigint(11) DEFAULT NULL COMMENT '省',
  `city` bigint(11) DEFAULT NULL COMMENT '市',
  `county` bigint(11) DEFAULT NULL COMMENT '县',
  `detailAddress` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `addressLon` varchar(32) DEFAULT NULL COMMENT '经度',
  `addressLat` varchar(32) DEFAULT NULL COMMENT '纬度',
  `companyType` varchar(255) DEFAULT NULL COMMENT '商户类型',
  `companyMobile` varchar(32) DEFAULT NULL COMMENT '商户电话',
  `companyEmail` varchar(32) DEFAULT NULL COMMENT 'EMAIL',
  `companyImg` varchar(255) DEFAULT NULL COMMENT '商户图片',
  `businessNo` varchar(32) DEFAULT NULL COMMENT '营业编号',
  `businessScope` varchar(255) DEFAULT NULL COMMENT '营业范围',
  `businessImg1` varchar(255) DEFAULT NULL COMMENT '营业证明图片1',
  `businessImg2` varchar(255) DEFAULT NULL,
  `businessImg3` varchar(255) DEFAULT NULL,
  `businessImg4` varchar(255) DEFAULT NULL,
  `businessImg5` varchar(255) DEFAULT NULL,
  `addressNo` varchar(45) DEFAULT NULL COMMENT '门牌号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_userid_shopid` (`userId`,`shopId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_cart`
--

DROP TABLE IF EXISTS `tb_shop_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_cart` (
  `id` varchar(36) NOT NULL,
  `userId` varchar(36) DEFAULT NULL COMMENT '商户用户ID',
  `shopId` varchar(36) DEFAULT NULL COMMENT '店铺ID',
  `goodId` varchar(36) DEFAULT NULL COMMENT '商品ID',
  `buyNum` int(11) DEFAULT NULL COMMENT '购买数量',
  `buyPrice` decimal(20,8) DEFAULT NULL COMMENT '购买价格',
  `buyUserId` varchar(36) DEFAULT NULL COMMENT '购买者ID',
  `tradeNo` varchar(40) DEFAULT NULL COMMENT '交易编号',
  `interiorNo` varchar(40) DEFAULT NULL COMMENT '内部编号',
  `pidFlag` varchar(10) COMMENT '隶属上级标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_comment`
--

DROP TABLE IF EXISTS `tb_shop_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '父级id',
  `shopId` varchar(36) NOT NULL COMMENT '商铺id',
  `tradeId` varchar(36) NOT NULL COMMENT '订单id',
  `userId` varchar(36) DEFAULT NULL COMMENT '评论人，为null则表示匿名评论',
  `commentType` char(1) DEFAULT NULL COMMENT '类型：0 回复，1 评论',
  `commentText` varchar(500) NOT NULL COMMENT '评价内容',
  `commentPic` text COMMENT '评论图片',
  `score` double NOT NULL DEFAULT '5' COMMENT '评分',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `anonymity` tinyint(4) NOT NULL DEFAULT '0' COMMENT '匿名状态:0非匿名,1匿名',
  PRIMARY KEY (`id`),
  KEY `idx_tradelog_tradeId` (`tradeId`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='商家评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_good`
--

DROP TABLE IF EXISTS `tb_shop_good`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_good` (
  `id` varchar(36) NOT NULL,
  `shopId` varchar(36) NOT NULL COMMENT '店铺ID',
  `goodNo` varchar(40) NOT NULL COMMENT '商品编号',
  `userId` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `goodName` varchar(52) NOT NULL COMMENT '商品名称',
  `goodPrice` decimal(20,8) DEFAULT NULL COMMENT '商品价格',
  `goodImg` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `firstGoodType` varchar(36) NOT NULL COMMENT '一级分类',
  `secondGoodType` varchar(36) NOT NULL COMMENT '二级分类',
  `thirdGoodType` varchar(36) NOT NULL COMMENT '三级分类',
  `goodSales` int(11) DEFAULT '0' COMMENT '销量',
  `goodStock` int(11) DEFAULT NULL COMMENT '商品库存',
  `freightFree` decimal(20,8) NOT NULL DEFAULT '0.00' COMMENT '运费',
  `status` varchar(1) NOT NULL DEFAULT '0' COMMENT '商品状态(0上传待审核，1审核通过，2审核不通过, 3商品下架)',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `auditUser` varchar(36) DEFAULT NULL COMMENT '修改用户(审核)',
  `auditTime` datetime DEFAULT NULL COMMENT '审核时间',
  `selfSupport` varchar(1) NOT NULL DEFAULT '0' COMMENT '自营状态：0否，1是',
  `delFlag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态：0否，1是',
  `unit` varchar(10) NOT NULL COMMENT '商品单位',
  `unfreezeRatio` varchar(10) NOT NULL COMMENT '解冻比率',
  `describes` longtext DEFAULT NULL COMMENT '商品描述',
  PRIMARY KEY (`id`),
  KEY `idx_good_userid` (`userId`),
  KEY `idx_good_shopid` (`shopId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='商家商品信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_good_collect`
--

DROP TABLE IF EXISTS `tb_shop_good_collect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_good_collect` (
  `id` varchar(36) NOT NULL,
  `userId` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `goodId` varchar(36) DEFAULT NULL COMMENT '商品ID',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_userid_goodid` (`userId`,`goodId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_good_img`
--

DROP TABLE IF EXISTS `tb_shop_good_img`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_good_img` (
  `id` varchar(36) NOT NULL,
  `goodId` varchar(36) DEFAULT NULL COMMENT '商品ID',
  `img` varchar(500) DEFAULT NULL COMMENT '图片',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '图片类别:1表示商品图片，2表示商品描述图片',
  PRIMARY KEY (`id`),
  KEY `idx_good_img_goodid` (`goodId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_good_property`
--

DROP TABLE IF EXISTS `tb_shop_good_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_good_property` (
  `id` varchar(36) NOT NULL,
  `goodId` varchar(36) DEFAULT NULL COMMENT '商品ID',
  `propertyName` varchar(32) DEFAULT NULL COMMENT '商品属性名',
  `propertyValue` varchar(32) DEFAULT NULL COMMENT '商品属性值',
  PRIMARY KEY (`id`),
  KEY `idx_good_property_goodid` (`goodId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_good_type`
--

DROP TABLE IF EXISTS `tb_shop_good_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_good_type` (
  `id` varchar(36) NOT NULL,
  `name` varchar(32) DEFAULT NULL COMMENT '商品类型名称',
  `img` varchar(255) DEFAULT NULL COMMENT '对应的图片',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `parentId` varchar(36) DEFAULT NULL COMMENT '上级',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_good_type_parentid` (`parentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_shop`
--

DROP TABLE IF EXISTS `tb_shop_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_shop` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户ID',
  `shopNo` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺编号',
  `shopName` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺名称',
  `shopType` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺类型（0 平台(自营)商户, 1 地面商户, 2 网上商户）',
  `shopLevel` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺等级,2:零售店 3:批发店',
  `shopPoint` double(4,1) NOT NULL DEFAULT '5.0' COMMENT '店铺积分',
  `shopConsume` decimal(20,8) DEFAULT NULL COMMENT '店铺销售总计',
  `shopSalesAmount` decimal(20,8) NOT NULL COMMENT '店铺销售额度',
  `shopRank` int(11) NOT NULL COMMENT '店铺级别',
  `shopTag` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺标签',
  `shopAddress` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺地址',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店长真实姓名',
  `mobile` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店长电话',
  `email` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'email',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通信地址',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺头像',
  `status` varchar(1) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '店铺状态(0申请, 1审核通过正常，2审核不通过, 3管理员禁用)',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `feightRate` decimal(20,8) DEFAULT NULL COMMENT '运费满减',
  `mark` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核意见',
  `whetherLogistics` varchar(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '是否发物流(0:否,1:是)',
  `whetherPay` varchar(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '是否可以提币(0:否,1:是)',
  PRIMARY KEY (`id`),
  KEY `idx_shop_userid` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_shop_collect`
--

DROP TABLE IF EXISTS `tb_shop_shop_collect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_shop_collect` (
  `id` varchar(36) NOT NULL,
  `userId` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `shopId` varchar(36) DEFAULT NULL COMMENT '店铺ID',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_userid_shopid` (`userId`,`shopId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_template`
--

DROP TABLE IF EXISTS `tb_shop_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_template` (
  `id` varchar(36) NOT NULL,
  `shopId` varchar(36) NOT NULL COMMENT '商户id',
  `name` varchar(50) NOT NULL COMMENT '模版名称',
  `freightFree` decimal(20,8) NOT NULL COMMENT '邮费金额',
  `remark` varchar(500) DEFAULT '' COMMENT '模版说明',
  `flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '启用状态：1否，0是',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='商城物流模版表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_trade`
--

DROP TABLE IF EXISTS `tb_shop_trade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_trade` (
  `id` varchar(36) NOT NULL,
  `interiorNo` varchar(40) NOT NULL COMMENT '内部编号',
  `tradeNo` varchar(40) NOT NULL COMMENT '订单编号',
  `shopId` varchar(36) NOT NULL COMMENT '店铺id',
  `buyUserId` varchar(36) NOT NULL COMMENT '购买者ID',
  `status` varchar(2) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '1.待发货 2.待收货 3.已完成 4.部分待收货（部分发货）',
  `totalPrice` decimal(20,8) NOT NULL COMMENT '总商品价格（元）',
  `totalFreightFree` decimal(20,8) NOT NULL COMMENT '总运费（元）',
  `totalCny` decimal(20,8) NOT NULL DEFAULT '0.0000' COMMENT '订单总金额(元)',
  `coinWait` decimal(20,8) DEFAULT NULL COMMENT '需付（优惠券，popc)',
  `lemRate` decimal(20,8) DEFAULT NULL COMMENT '购买的时候莱姆币兑换比例',
  `coined` decimal(20,8) DEFAULT NULL COMMENT '实付（优惠券，popc)',
  `country` bigint(11) DEFAULT '0' COMMENT '国',
  `province` bigint(11) NOT NULL COMMENT '省',
  `city` bigint(11) NOT NULL COMMENT '市',
  `county` bigint(11) NOT NULL COMMENT '县',
  `detailAddress` varchar(255) NOT NULL COMMENT '详细地址',
  `recvName` varchar(16) NOT NULL COMMENT '接收姓名',
  `recvMobile` varchar(16) NOT NULL COMMENT '接收电话',
  `zipcode` varchar(16) DEFAULT NULL COMMENT '邮编',
  `mark` varchar(255) DEFAULT NULL COMMENT '说明',
  `flag` tinyint(4) DEFAULT '0' COMMENT '删除标志（0：未删除，1用户已删除）',
  `shopFlag` tinyint(4) DEFAULT '0' COMMENT '商家端删除标志（0：未删除，1已删除）',
  `source` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示app来源',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `point` decimal(20,8) DEFAULT NULL COMMENT '实付余额',
  `provinceValue` varchar(30) DEFAULT NULL COMMENT '省名称',
  `cityValue` varchar(30) DEFAULT NULL COMMENT '市名称',
  `countyValue` varchar(30) DEFAULT NULL COMMENT '区名称',
  `payType` varchar(1) NOT NULL COMMENT '支付类型（1.余额,2.余额+优惠券）',
  `sendStatus` int(10) NOT NULL DEFAULT '0' COMMENT '0:待发货 1.部分发货 2.已发货',
  `currentTime` bigint(20) DEFAULT NULL COMMENT '用于订单排序查询',
  PRIMARY KEY (`id`),
  KEY `idx_trade_buyuserid` (`buyUserId`),
  KEY `idx_trade_shopid` (`shopId`),
  KEY `idx_trade_tradeno` (`tradeNo`),
  KEY `idx_trade_interiorNo` (`interiorNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='订单表';

/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_trade_item`
--

DROP TABLE IF EXISTS `tb_shop_trade_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_trade_item` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tradeId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `shopId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '店铺id',
  `goodId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品ID',
  `goodName` varchar(52) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `goodImg` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品主图',
  `buyNum` int(11) NOT NULL COMMENT '购买数量',
  `buyPrice` decimal(20,8) NOT NULL COMMENT '购买价格',
  `freightFree` decimal(20,8) NOT NULL COMMENT '运费',
  `buyUserId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '购买者ID',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `goodStatus` int(2) NOT NULL DEFAULT '0' COMMENT '0:该商品未发货 1.该商品已经发货',
  `payType` varchar(10) COMMENT '支付方式',
  PRIMARY KEY (`id`),
  KEY `idx_trade_item_goodid` (`goodId`),
  KEY `idx_trade_item_shopid` (`shopId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单商品详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_trade_log`
--

DROP TABLE IF EXISTS `tb_shop_trade_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_trade_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shopId` varchar(36) NOT NULL COMMENT '商铺id',
  `tradeId` varchar(36) NOT NULL COMMENT '订单id',
  `logType` tinyint(4) NOT NULL COMMENT '日志类型（0系统日志，1业务日志）',
  `logCode` tinyint(4) NOT NULL COMMENT '日志code',
  `logValue` varchar(500) DEFAULT NULL COMMENT '备注',
  `logImg1` varchar(500) DEFAULT NULL COMMENT '图片1',
  `logImg2` varchar(500) DEFAULT NULL COMMENT '图片1',
  `logImg3` varchar(500) DEFAULT NULL COMMENT '图片1',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `logValue1` varchar(500) DEFAULT NULL COMMENT '预留字段1',
  `logValue2` varchar(500) DEFAULT NULL COMMENT '预留字段2',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态（0，处理中，1同意，2拒绝，3退货中）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_tradelog_tradeId` (`shopId`,`tradeId`,`logCode`)
) ENGINE=InnoDB AUTO_INCREMENT=1028 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='订单状态日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_shop_user_address`
--

DROP TABLE IF EXISTS `tb_shop_user_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_user_address` (
  `id` varchar(36) NOT NULL,
  `userId` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `country` bigint(11) DEFAULT NULL COMMENT '国',
  `province` bigint(11) DEFAULT NULL COMMENT '省',
  `city` bigint(11) DEFAULT NULL COMMENT '市',
  `county` bigint(11) DEFAULT NULL COMMENT '县',
  `detailAddress` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `recvName` varchar(16) DEFAULT NULL COMMENT '接收姓名',
  `recvMobile` varchar(16) DEFAULT NULL COMMENT '接收电话',
  `zipcode` varchar(16) DEFAULT NULL COMMENT '邮编',
  `def` varchar(1) DEFAULT NULL COMMENT '是否默认（0默认）',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `provinceValue` varchar(30) DEFAULT NULL COMMENT '省名称',
  `cityValue` varchar(30) DEFAULT NULL COMMENT '市名称',
  `countyValue` varchar(30) DEFAULT NULL COMMENT '区名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_sms_captcha`
--

DROP TABLE IF EXISTS `tb_sms_captcha`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sms_captcha` (
  `id` char(36) NOT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `code` varchar(6) DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  `lastTime` datetime DEFAULT NULL COMMENT '短信验证码15分钟有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_sms_record`
--

DROP TABLE IF EXISTS `tb_sms_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sms_record` (
  `id` char(36) NOT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `type` char(2) DEFAULT NULL COMMENT '短信类型：\r\n            01 验证码短信',
  `content` varchar(200) DEFAULT NULL,
  `creator` char(36) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_sys_settings`
--

DROP TABLE IF EXISTS `tb_sys_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'code',
  `label` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'label',
  `mark` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '说明',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最新时间',
  PRIMARY KEY (`id`),
  KEY `label_UNIQUE` (`label`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='键值对存储表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user` (
  `id` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `nationalCode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国际电话',
  `mobile` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `salt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '盐值',
  `nick` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `trueName` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户真实姓名',
  `referrer` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '推荐人',
  `level` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '等级',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户状态,0已启用，1已禁用',
  `transactionPassword` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易密码',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `pid` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '隶属站长/主任ID',
  `standNo` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '站点编号',
  `transcationSalt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易密码盐值',
  `certificationStatus` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '实名认证状态，0未审核，1已通过，2已拒绝，3未实名',
  `xin` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT '0.00000000' COMMENT '信用积分',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`),
  UNIQUE KEY `idx_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_user_contact`
--

DROP TABLE IF EXISTS `tb_user_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_contact` (
  `id` varchar(36) NOT NULL,
  `userId` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `nick` varchar(255) DEFAULT NULL COMMENT '对手昵称',
  `address` varchar(100) DEFAULT NULL COMMENT '对手地址',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_userid` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_user_login`
--

DROP TABLE IF EXISTS `tb_user_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_login` (
  `id` char(36) NOT NULL,
  `userId` char(36) DEFAULT NULL,
  `token` text,
  `loginTime` datetime DEFAULT NULL,
  `expireTime` datetime DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  `lastTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_user_roles`
--

DROP TABLE IF EXISTS `tb_user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_roles` (
  `id` varchar(36) NOT NULL,
  `rolesId` varchar(36) DEFAULT NULL COMMENT '角色ID',
  `apisId` varchar(36) DEFAULT NULL COMMENT '接口ID',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_user_statement`
--

DROP TABLE IF EXISTS `tb_user_statement`;
CREATE TABLE `tb_user_statement` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `currency` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资产代号，比如popc',
  `availableBefore` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '修改前可用余额',
  `availableAfter` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '修改后可用余额',
  `availableChange` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '可用余额的变动金额',
  `reservedBefore` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '修改前冻结余额',
  `reservedAfter` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '修改后冻结余额',
  `reservedChange` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '冻结余额的变动金额',
  `tradeType` tinyint(4) NOT NULL COMMENT '交易类型: 1:消费 2:收款 3:充值 4:给会员充值 5:扣款 6:扣除用户资金 7:解冻 8:提现',
  `status` tinyint(4) NOT NULL COMMENT '1:待处理,2:已处理,3:已到账',
  `referenceId` char(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '导致资金变化的纪录的ID，比如订单id，比如充值纪录id，比如提现纪录id',
  `goodId` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '购物赠送优惠券关联商品Id',
  `mark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资产流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_user_transfer`
--

DROP TABLE IF EXISTS `tb_user_transfer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_transfer` (
  `hash` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '交易hash',
  `block` bigint(20) DEFAULT NULL COMMENT '区块号',
  `from` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'fromUserId',
  `to` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `amount` decimal(20,8) DEFAULT NULL COMMENT '金额',
  `mark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `status` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态：0未确认，1确认',
  `createTime` datetime DEFAULT NULL,
  `lastTime` datetime DEFAULT NULL,
  PRIMARY KEY (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_user_wallet_lem`
--

DROP TABLE IF EXISTS `tb_user_wallet_lem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_wallet_lem` (
  `id` varchar(36) DEFAULT NULL,
  `userId` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `coinAddress` varchar(100) DEFAULT NULL COMMENT '币地址',
  `coin` decimal(20,8) DEFAULT '0.00000000' COMMENT '用户对应的币',
  `coinFrozen` decimal(20,8) DEFAULT '0.00000000' COMMENT '购物冻结的币',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `remark` varchar(36) COMMENT '地址备注',
  UNIQUE KEY `idx_userid` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  ;


DROP TABLE IF EXISTS `tb_user_asset`;
CREATE TABLE `tb_user_asset` (
  `id` varchar(36) NOT NULL,
  `userId` varchar(36) DEFAULT NULL COMMENT '用户ID',
   `currency` varchar(8) DEFAULT NULL COMMENT '资产代号，比如cny, credit, coupon',
  `withdrawable` tinyint(2) DEFAULT 0 COMMENT '是否可以提现， 0:不可以 1:可以',
  `spendable` tinyint(2) DEFAULT 0 COMMENT '是否可以消费（购买东西）， 0:不可以 1:可以',
  `internalAsset` tinyint(2) DEFAULT 0 COMMENT '是否为平台内部资产， 0:不是 1:是',
  `availableBalance` decimal(20,8) DEFAULT '0.00000000' COMMENT '可用余额',
  `reservedBalance` decimal(20,8) DEFAULT '0.00000000' COMMENT '不可用余额',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_userid` (`userId`,`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  comment '用户资产表';


/*用户优惠券关联表*/;
DROP TABLE IF EXISTS `tb_user_coupon`;
CREATE TABLE `tb_user_coupon` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `cdkeyNum` bigint(10)  COMMENT '消耗cd-key数量',
  `couponType` bigint(2)  COMMENT '兑换优惠券种类：1彩票，2obb，3股权',
  `couponNum` decimal(18,8) NOT NULL COMMENT '兑换优惠券数量',
  `createTime` datetime NOT NULL,
  `status` varchar(2) COLLATE utf8mb4_unicode_ci COMMENT '状态：0未兑换，1兑换',
  `couponId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '优惠券Id',
  `currentExtractNum` decimal(18,8) DEFAULT '0.00000000' COMMENT '当前待审核的总数',
  `reservedCouponNum` decimal(18,8) DEFAULT '0.00000000' COMMENT '不可使用',
  PRIMARY KEY (`id`,`userId`,`couponId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户优惠券关联表';

/*提现*/;
DROP TABLE IF EXISTS `tb_money`;
CREATE TABLE `tb_money` (
  `id` varchar(36) NOT NULL,
  `userId` varchar(36) NOT NULL COMMENT '用户ID',
  `createTime` datetime NOT NULL COMMENT '提现日期',
  `money` bigint(10) NOT NULL COMMENT '提现金额',
  `status` varchar(2) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态：0未提现，1提现',
  `updateTime` datetime NOT NULL COMMENT '修改日期',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='提现表';

/*优惠券*/;
DROP TABLE IF EXISTS `tb_coupon`;
CREATE TABLE `tb_coupon` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `couponName` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '优惠券名称',
  `couponNum` bigint(10) DEFAULT NULL COMMENT '赠送优惠券数量',
  `unit` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '优惠券单位',
  `createTime` datetime NOT NULL,
  `ratio` decimal(20,8) NOT NULL COMMENT '比例',
  `sendDays` int(20) DEFAULT '100' COMMENT '发放天数',
  `couponType` int(10) NOT NULL DEFAULT '0' COMMENT '0:通用 1.可选 2.其他',
  `couponEnglishName` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'null' COMMENT '优惠券英文名',
  `isSend` int(2) DEFAULT '1' COMMENT '0:不可发放 1；可发放',
  `isDocking` int(2) DEFAULT '0' COMMENT '0.不可与第三方对接 1.可以与第三方对接 2.其他',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='优惠券表';

DROP TABLE IF EXISTS `tb_user_certification`;
CREATE TABLE `tb_user_certification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `trueName` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '真实姓名',
  `cardId` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证号',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `faceImg` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '身份证正面照',
  `conImg` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '身份证反面照',
  `holdImg` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手持身份证照',
  `status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核状态，0未审核，1已通过，2已拒绝，3未实名',
  `certificateType` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '证件类别  0：身份证，1，其它证件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员证件表';

DROP TABLE IF EXISTS `tb_user_bankcard`;
CREATE TABLE `tb_user_bankcard` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` varchar(36) NOT NULL COMMENT '用户ID',
  `trueName` varchar(36) NOT NULL COMMENT '真实姓名',
  `bankCard` varchar(128) NOT NULL COMMENT '银行卡号',
  `bankName` varchar(128) NOT NULL COMMENT '银行名称',
  `openBank` varchar(512)  COMMENT '开户行',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='会员银行卡表';

DROP TABLE IF EXISTS `tb_good_coupon_center`;
CREATE TABLE `tb_good_coupon_center` (
  `id` varchar(36) NOT NULL,
  `goodId` varchar(36) NOT NULL COMMENT '商品Id',
  `shopId` varchar(36) NOT NULL COMMENT '店铺id',
  `couponId` varchar(36) NOT NULL COMMENT '优惠卷id',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `couponNum` decimal(20,8) NOT NULL COMMENT '优惠券数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='商品跟优惠卷关联中间表';

DROP TABLE IF EXISTS `tb_shop_good_pay`;
CREATE TABLE `tb_shop_good_pay` (
  `id` varchar(36) NOT NULL,
  `goodId` varchar(36) NOT NULL COMMENT '商品Id',
  `goodPayId` varchar(36) NOT NULL COMMENT '支付方式id',
  `payType` varchar(16) COMMENT '支付方式, 和tb_user_asset表的currency一致',
  `price` decimal(20,8)  COMMENT '价格',
  `createTime` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='商品支付类型表';

DROP TABLE IF EXISTS `tb_user_recharge_log`;
CREATE TABLE `tb_user_recharge_log` (
  `id` varchar(36) NOT NULL,
  `userId` varchar(36) NOT NULL COMMENT '用户ID',
  `rechargeAmount` decimal(20,8) DEFAULT NULL COMMENT '⁮变动金额',
  `rechargeType` varchar(15) DEFAULT NULL COMMENT '资产类型',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `rechargeUser` varchar(36) DEFAULT NULL COMMENT '被充值人ID',
  `operationType` varchar(1) DEFAULT NULL COMMENT '操作类型1：充值2：扣除',
  `status` varchar(1) DEFAULT NULL COMMENT '0：默认 1：生效 2：失效',
  `customerType` varchar(1) DEFAULT NULL COMMENT '1为商户后台 2为总后台',
  `remarks` varchar(2000) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户充值记录';

DROP TABLE IF EXISTS `tb_user_cash_log`;
CREATE TABLE `tb_user_cash_log` (
  `id` varchar(36) NOT NULL,
  `userId` varchar(36) NOT NULL COMMENT '用户ID',
  `cashAmount` decimal(20,8) NOT NULL COMMENT '提现金额',
  `status` varchar(15) NOT NULL COMMENT '处理结果，0未处理，1同意，2拒绝',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='提现记录表';

DROP TABLE IF EXISTS `tb_user_wallet`;
CREATE TABLE `tb_user_wallet` (
  `id` varchar(36) NOT NULL,
  `userId` varchar(36) NOT NULL COMMENT '用户ID',
  `popcAddress` varchar(200) NOT NULL COMMENT 'popc钱包地址',
  `remark` text COMMENT '备注',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='用户钱包地址';

DROP TABLE IF EXISTS `tb_unfreeze_log`;
CREATE TABLE `tb_unfreeze_log` (
  `id` varchar(36) NOT NULL,
  `userId` varchar(36) NOT NULL COMMENT '用户ID',
  `currency` varchar(16) NOT NULL COMMENT '币种名称',
  `type` varchar(1) NOT NULL COMMENT '解冻类型  1:自然解冻 2:购买商品解冻',
  `unfreezeRatio` decimal(20,8) NOT NULL COMMENT '解冻百分比%',
  `profitCashTotal` decimal(20,8) NOT NULL COMMENT '盈利总金额',
  `unfreezeCashTotal` decimal(20,8) NOT NULL COMMENT '解冻总金额',
  `unfreezeCash` decimal(20,8) NOT NULL COMMENT '该用户本次的解冻金额',
  `unfreezeDate` date DEFAULT NULL COMMENT '解冻日期',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
   PRIMARY KEY (`id`),
   KEY (`userId`,`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='解冻记录表';


DROP TABLE IF EXISTS `tb_logistics_company`;
CREATE TABLE `tb_logistics_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '公司名称',
  `status` int(11) NOT NULL COMMENT '状态 0: 禁用 1:启用',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='物流公司表';

DROP TABLE IF EXISTS `tb_user_coupon_log`;
CREATE TABLE `tb_user_coupon_log` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `tradeId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单ID',
  `beforeNum` decimal(20,8) DEFAULT NULL COMMENT '⁮变动之前数量',
  `rechargeNum` decimal(20,8) DEFAULT NULL COMMENT '⁮变动数量',
  `afterNum` decimal(20,8) DEFAULT NULL COMMENT '⁮变动之后数量',
  `couponName` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '兑换券名称',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `goodId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品id',
  `couponId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '优惠劵对应的配置表Id',
  `sendFinish` int(2) NOT NULL DEFAULT '0' COMMENT '0:表示未发送完成 1.表示发放完成',
  `currentReleaseNum` int(10) NOT NULL DEFAULT '0' COMMENT '当前第几次释放',
  `nextReleaseTime` datetime DEFAULT NULL COMMENT '下次释放的时间',
  `everyTimeReleaseNum` decimal(18,8) DEFAULT '0.00000000' COMMENT '每次释放的数量',
  `speciesType` int(10) DEFAULT '0' COMMENT '优惠券类型:1.优惠券，2.彩票 3.债权 4.游戏积分',
  `needReleseDays` int(10) NOT NULL DEFAULT '100' COMMENT '需要',
  `relationUserId` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '触发赠送的人Id',
  `totalCny` decimal(20,8) DEFAULT NULL COMMENT '消费金额',
  `ratio` decimal(20,8) DEFAULT NULL COMMENT '消费返推荐人比率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='优惠券获得明细表';
/*CREATE TABLE `tb_user_coupon_log` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `tradeId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单ID',
  `beforeNum` decimal(20,8) DEFAULT NULL COMMENT '⁮变动之前数量',
  `rechargeNum` decimal(20,8) DEFAULT NULL COMMENT '⁮变动数量',
  `afterNum` decimal(20,8) DEFAULT NULL COMMENT '⁮变动之后数量',
  `couponName` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '兑换券名称',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `goodId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品id',
  `couponId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '优惠劵对应的配置表Id',
  `sendFinish` int(2) NOT NULL DEFAULT '0' COMMENT '0:表示未发送完成 1.表示发放完成',
  `currentReleaseNum` int(10) NOT NULL DEFAULT '0' COMMENT '当前第几次释放',
  `nextReleaseTime` datetime DEFAULT NULL COMMENT '下次释放的时间',
  `everyTimeReleaseNum` decimal(18,8) DEFAULT '0.00000000' COMMENT '每次释放的数量',
  `speciesType` int(10) DEFAULT '0' COMMENT '优惠券类型:1.优惠券，2.彩票 3.债权 4.游戏积分',
  `needReleseDays` int(10) NOT NULL DEFAULT '100' COMMENT '需要',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='优惠券获得明细表';*/

DROP TABLE IF EXISTS `tb_user_operator_log`;
CREATE TABLE `tb_user_operator_log` (
  `id` varchar(36)  NOT NULL COMMENT '操作记录id',
  `user_id` varchar(100)  NOT NULL COMMENT '操作玩家的id',
  `account` varchar(50)  NOT NULL COMMENT '操作人登录账号',
  `oprator_Time` datetime NOT NULL COMMENT '操作时间',
  `position` varchar(30)  DEFAULT NULL COMMENT '操作人职位(角色)',
  `operator_type` int(10) DEFAULT NULL COMMENT '1.资金记录 2.通过 3.充值。4.批量发货',
  `operator_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `tb_shop_transport`;
CREATE TABLE `tb_shop_transport` (
  `id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT ' 物流订单表主键',
  `shopId` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '商店id',
  `goodId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '物品id',
  `tradeNo` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `totalFreightFree` decimal(18,8) DEFAULT '0.00000000' COMMENT '运费(相同的物流单号，这个运费取其中任何一个都可以，因为这个记录是快递的总金额)',
  `userId` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作人Id',
  `operatorTime` datetime NOT NULL COMMENT '操作时间',
  `transportNo` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '物流编号号',
  `logisticsName` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流名称',
  PRIMARY KEY (`id`,`shopId`,`goodId`,`tradeNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `tb_user_release_coupon_log`;
CREATE TABLE `tb_user_release_coupon_log` (
  `id` bigint(30) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '玩家Id',
  `couponLogId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '玩家的优惠券日志Id',
  `amount` decimal(18,8) NOT NULL COMMENT '释放的数量',
  `createTime` datetime NOT NULL COMMENT '释放的时间',
  `releaseAfterNum` decimal(18,8) NOT NULL DEFAULT '0.00000000' COMMENT '释放后的总数量',
  `releaseBeforNum` decimal(18,8) NOT NULL DEFAULT '0.00000000' COMMENT '释放前的数量',
  `releaseType` int(1) NOT NULL DEFAULT '0' COMMENT '释放类型：0为购买的彩票积分释放，1.为基础数据释放的',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `tb_level_config`;
CREATE TABLE `tb_level_config` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `configKey` varchar(100) NOT NULL COMMENT '标识',
  `configDesc` varchar(200) NOT NULL COMMENT '说明',
  `amount` decimal(18,8) NOT NULL COMMENT '升级需要的金额',
  `configLevel` varchar(20) NOT NULL COMMENT '增加等级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='等级管理表';

DROP TABLE IF EXISTS `tb_coupon_time_config`;
CREATE TABLE `tb_coupon_time_config` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `notSendTime` datetime NOT NULL COMMENT '不发放的时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='发放记录配置表';

DROP TABLE IF EXISTS `tb_good_pay_config`;
CREATE TABLE `tb_good_pay_config` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `payName`varchar(100) NOT NULL COMMENT '支付方式名称',
  `payType`varchar(100) NOT NULL COMMENT '支付方式类型',
  `balanceRatio` decimal(10,4) NOT NULL COMMENT '余额比例值(%)',
  `otherRatio` decimal(10,4) NOT NULL COMMENT '其它比例值(%)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品支付方式配置表';

DROP TABLE IF EXISTS `tb_user_consume`;
CREATE TABLE `tb_user_consume` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户Id',
  `consume` decimal(20,4) NOT NULL COMMENT '用户消费累计',
  `userLevel` int(10) NOT NULL COMMENT '用户等级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户消费累计表';

DROP TABLE
IF EXISTS `tb_coupon_transfer_log`;

DROP TABLE IF EXISTS `tb_coupon_transfer_log`;
CREATE TABLE `tb_coupon_transfer_log` (
 `id` VARCHAR (36) COLLATE utf8mb4_unicode_ci NOT NULL,
 `rollInUserId` VARCHAR (36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '转入用户ID',
 `rollOutUserId` VARCHAR (36) NOT NULL COMMENT '转出用户Id',
 `relation` VARCHAR (2) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '转让关系',
 `amount` BIGINT (15) NOT NULL COMMENT '数量',
 `fee` decimal(18,8) NOT NULL COMMENT '手续费',
 `createTime` datetime NOT NULL,
 PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '优惠券转让记录表';

DROP TABLE IF EXISTS `tb_good_shop_center`;
CREATE TABLE `tb_good_shop_center` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `shopId` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺ID',
  `goodId` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品ID',
  `goodStatus` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `modifyTime` datetime DEFAULT NULL COMMENT '修改时间',
  `sales` int(11) DEFAULT NULL COMMENT '商品销量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品店铺中间表';

DROP TABLE IF EXISTS `tb_user_asset_base`;
CREATE TABLE `tb_user_asset_base` (
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户Id',
  `assetType` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资产类型：popc',
  `reservedBalance` decimal(18,8) NOT NULL COMMENT '不可用金额',
  `currentReleseNum` int(20) DEFAULT '0' COMMENT '当前释放的次数',
  `sendFinish` int(10) NOT NULL DEFAULT '0' COMMENT '0:未发放完成，1.发放完成',
  `createTime` datetime DEFAULT NULL COMMENT '生成时间',
  `afterAmount` decimal(18,8) NOT NULL DEFAULT '0.00000000' COMMENT '当前剩与不可用金额',
  `needReleseDays` int(10) DEFAULT '100' COMMENT '需要释放的天数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='推荐人获得赠送优惠券明细表';

DROP TABLE IF EXISTS `tb_user_return_coupon_log`;
CREATE TABLE `tb_user_return_coupon_log` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tradeId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `relationUserId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '触发赠送的人Id',
  `amount` decimal(18,8) NOT NULL DEFAULT '0.00000000',
  `couponId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '优惠券配置Id',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `currentReleaseNum` int(10) NOT NULL DEFAULT '0' COMMENT '当前释放的天数',
  `sendFinish` int(10) NOT NULL DEFAULT '0' COMMENT '0:未发送完 1.发送完',
  `afterAmount` decimal(18,8) NOT NULL DEFAULT '0.00000000' COMMENT '释放后的数量',
  `needReleaseDays` int(10) NOT NULL DEFAULT '0' COMMENT '需要释放的天数',
  `returnRatio` decimal(18,8) NOT NULL DEFAULT '0' COMMENT '返还推荐人比率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='推荐人获得赠送优惠券明细表';

DROP TABLE IF EXISTS `tb_user_extract_coupon_log`;
CREATE TABLE `tb_user_extract_coupon_log` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '提交人',
  `couponId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '优惠券id，用于获取优惠券类型',
  `applyNumBefore` decimal(18,8) NOT NULL COMMENT '提取之前的数量',
  `applyNum` decimal(18,4) NOT NULL COMMENT '申请提取的数量',
  `applyNumAfter` decimal(18,4) NOT NULL COMMENT '提取之后的数量',
  `ratio` decimal(18,8) DEFAULT NULL COMMENT '提取费用',
  `auditStatus` int(10) NOT NULL DEFAULT '0' COMMENT '0;待审核 1.通过 2.拒绝',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `operatorUserId` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核人',
  `operatorTime` datetime DEFAULT NULL,
  `poundage` decimal(18,8) DEFAULT '0.00000000' COMMENT '手续费'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='优惠券提取记录表';

DROP TABLE IF EXISTS `tb_admin_resources_operation`;
CREATE TABLE `tb_admin_resources_operation` (
  `res_id` int(11) DEFAULT '0' COMMENT '资源ID',
  `op_id` int(11) DEFAULT '0' COMMENT '操作ID',
  `role_id` int(11) DEFAULT '0' COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员资源操作权限关联表';

DROP TABLE IF EXISTS `tb_admin_operation`;
CREATE TABLE `tb_admin_operation` (
  `op_id` int(11) NOT NULL COMMENT '操作ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作名称',
  `english_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作英文名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_up_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`op_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作类型表';


DROP TABLE IF EXISTS `tb_shop_shop_sales_log`;
CREATE TABLE `tb_shop_shop_sales_log` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '销售额度记录id',
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `shopSalesAmountBefore` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '修改前销售额度',
  `shopSalesAmountChange` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '销售额度的变动金额',
  `shopSalesAmountAfter` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '修改后销售额度',
  `operateType` varchar(5) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型 ',
  `referenceId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '导致资金变化的纪录的ID',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺销售额度流水';
