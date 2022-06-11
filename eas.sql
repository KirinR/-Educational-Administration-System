/*
Navicat MySQL Data Transfer

Source Server         : Mysql
Source Server Version : 80018
Source Host           : localhost:3306
Source Database       : eas

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2022-06-11 20:19:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses` (
  `cid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `teacher_id` int(11) unsigned NOT NULL,
  `lessons` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `classroom` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ctype` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `session` date DEFAULT NULL,
  `max` tinyint(4) DEFAULT NULL,
  `credit` tinyint(4) DEFAULT NULL,
  `selectable` bit(1) DEFAULT NULL,
  PRIMARY KEY (`cid`),
  KEY `teacher` (`teacher_id`),
  CONSTRAINT `teacher` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1024 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of courses
-- ----------------------------
INSERT INTO `courses` VALUES ('10', '测试', '20', '5&3', '教书院102', '专业选修', '2022-06-10', '50', '4', '\0');
INSERT INTO `courses` VALUES ('1012', '数据库系统与设计', '20', '5&3;1&2', '教书院106', '专业必修', '2022-06-09', '60', '3', '\0');
INSERT INTO `courses` VALUES ('1016', '操作系统', '21', '5&3', '教书院101', '专业必修', '2022-05-04', '100', '5', '\0');
INSERT INTO `courses` VALUES ('1021', '计算机网络', '20', '2&3;3&1', '教书院110', '专业选修', '2022-05-04', '80', '4', '\0');
INSERT INTO `courses` VALUES ('1022', '计算机网络', '20', '2&3;3&1', '教书院110', '专业选修', '2022-05-04', '1', '4', '');

-- ----------------------------
-- Table structure for scs
-- ----------------------------
DROP TABLE IF EXISTS `scs`;
CREATE TABLE `scs` (
  `scid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `student_id` int(11) unsigned NOT NULL,
  `course_id` int(11) unsigned NOT NULL,
  `usual` float(4,0) unsigned DEFAULT '0',
  `final` float(4,0) unsigned DEFAULT '0',
  `grade` float(4,0) unsigned DEFAULT '0',
  `GPA` float(4,2) unsigned DEFAULT '0.00',
  PRIMARY KEY (`scid`,`student_id`,`course_id`),
  KEY `student` (`student_id`) USING BTREE,
  KEY `course` (`course_id`),
  CONSTRAINT `course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`cid`),
  CONSTRAINT `student` FOREIGN KEY (`student_id`) REFERENCES `users` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of scs
-- ----------------------------
INSERT INTO `scs` VALUES ('1', '4', '1022', '0', '0', '0', '0.00');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `uid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `password` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` bigint(20) DEFAULT NULL,
  `department` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `permission` char(32) NOT NULL,
  `token` char(32) DEFAULT NULL,
  `loginTime` bigint(20) DEFAULT NULL,
  `regTime` date DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', '5f4dcc3b5aa765d61d8327deb882cf99', '总教务', '12345678910', '教务处', 'PERMISSION_EAS_ADMINISTRATOR', '7f6d6d0b19fa1ef3d0f40db1085f89f7', '1654949930470', '2022-05-17');
INSERT INTO `users` VALUES ('4', '5f4dcc3b5aa765d61d8327deb882cf99', '学生1', '10000', '软件工程学院', 'PERMISSION_EAS_STUDENT', '0e0430e964922e5f289101715968f3d6', '1654949930767', '2022-05-17');
INSERT INTO `users` VALUES ('20', 'fc5e038d38a57032085441e7fe7010b0', '测试2', '123456789', '软件学院', 'PERMISSION_EAS_TEACHER', 'fdf146b658ed53a3651f5db9ce620eaa', '1654949930704', '2022-05-27');
INSERT INTO `users` VALUES ('21', 'fc5e038d38a57032085441e7fe7010b0', '测试3', '123456789', '软件学院', 'PERMISSION_EAS_TEACHER', 'e71ee89b6161d197dd931727d72a19b7', '1654949706014', '2022-05-27');
INSERT INTO `users` VALUES ('26', '5f4dcc3b5aa765d61d8327deb882cf99', '学生3', '10001', '软件工程学院', 'PERMISSION_EAS_STUDENT', '766236e464e39fc97ac59a6f3f0c51d0', '1654949930673', '2022-05-17');
