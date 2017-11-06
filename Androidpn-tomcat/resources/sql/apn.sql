/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50557
Source Host           : localhost:3306
Source Database       : apn

Target Server Type    : MYSQL
Target Server Version : 50557
File Encoding         : 65001

Date: 2017-11-06 11:35:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `nid` varchar(32) NOT NULL,
  `broadcast` int(4) NOT NULL DEFAULT '0' COMMENT '是否是广播',
  `create_time` datetime NOT NULL,
  `active_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '有效时间，从创建时间计算',
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(255) NOT NULL,
  `uri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for notification_state
-- ----------------------------
DROP TABLE IF EXISTS `notification_state`;
CREATE TABLE `notification_state` (
  `user_id` varchar(255) NOT NULL,
  `notification_id` varchar(255) NOT NULL,
  `send_time` datetime NOT NULL,
  `arrived_time` datetime DEFAULT NULL,
  `click_time` datetime DEFAULT NULL,
  `receipt_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`,`notification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `lid` int(11) NOT NULL COMMENT '标签id',
  `name` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `comment` varchar(255) DEFAULT NULL COMMENT '标签备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` bigint(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `alias` varchar(255) DEFAULT NULL,
  `register_time` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `tel` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_state
-- ----------------------------
DROP TABLE IF EXISTS `user_state`;
CREATE TABLE `user_state` (
  `uid` varchar(255) NOT NULL,
  `sid` varchar(255) NOT NULL COMMENT '会话id',
  `login_time` datetime NOT NULL,
  `logout_time` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`,`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_tag
-- ----------------------------
DROP TABLE IF EXISTS `user_tag`;
CREATE TABLE `user_tag` (
  `uid` varchar(255) NOT NULL,
  `lid` int(11) NOT NULL,
  PRIMARY KEY (`uid`,`lid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
