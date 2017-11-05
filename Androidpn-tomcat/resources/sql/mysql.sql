/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50022
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50022
File Encoding         : 65001

Date: 2017-11-04 23:14:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for label
-- ----------------------------
DROP TABLE IF EXISTS `label`;
CREATE TABLE `label` (
`lid`  int(11) NOT NULL COMMENT '标签id' ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`create_time`  datetime NOT NULL ,
`comment`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签备注' 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
`nid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`broadcast`  int(4) NOT NULL DEFAULT 0 COMMENT '是否是广播' ,
`create_time`  datetime NOT NULL ,
`active_time`  bigint(20) NOT NULL DEFAULT 0 COMMENT '有效时间，从创建时间计算' ,
`title`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`content`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`uri`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`nid`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for notification_state
-- ----------------------------
DROP TABLE IF EXISTS `notification_state`;
CREATE TABLE `notification_state` (
`user_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`notification_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`send_time`  datetime NOT NULL ,
`arrived_time`  datetime NULL DEFAULT NULL ,
`click_time`  datetime NULL DEFAULT NULL ,
`receipt_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`user_id`, `notification_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
`uid`  bigint(20) NOT NULL ,
`username`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`password`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`alias`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`register_time`  datetime NULL DEFAULT NULL ,
`email`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`tel`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`uid`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for user_label
-- ----------------------------
DROP TABLE IF EXISTS `user_label`;
CREATE TABLE `user_label` (
`uid`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`lid`  int(11) NOT NULL ,
PRIMARY KEY (`uid`, `lid`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for user_state
-- ----------------------------
DROP TABLE IF EXISTS `user_state`;
CREATE TABLE `user_state` (
`uid`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`sid`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会话id' ,
`login_time`  datetime NOT NULL ,
`logout_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`uid`, `sid`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;
