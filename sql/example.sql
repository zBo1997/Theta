CREATE DATABASE test_performance;

CREATE TABLE `sequence_config` (
   `ID` varchar(128) NOT NULL,
   `CURRENT` bigint(20) NOT NULL,
   `MAXIMUM` varchar(255) NOT NULL,
   `LAST_UPDATE_TIME` datetime(2) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(2),
   PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `test_user` (
`id` varchar(64) NOT NULL,
`user_id` varchar(64) NOT NULL,
`user_name` varchar(64) NOT NULL,
`phone` varchar(64) NOT NULL,
`lan_id` varchar(64) NOT NULL,
`region_id` varchar(64) NOT NULL,
`create_time` datetime(2) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(2),
PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for c_user
-- ----------------------------
DROP TABLE IF EXISTS `theta_user`;
CREATE TABLE `theta_user` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `account` varchar(30) NOT NULL DEFAULT '' COMMENT '账号',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '姓名',
  `org_id` bigint(20) DEFAULT NULL COMMENT '组织',
  `email` varchar(255) DEFAULT '' COMMENT '邮箱',
  `mobile` varchar(20) DEFAULT '' COMMENT '手机',
  `sex` varchar(1) DEFAULT 'M' COMMENT '性别',
  `state` bit(1) DEFAULT b'1' COMMENT '状态',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像',
  `nation` char(2) DEFAULT '' COMMENT '民族',
  `education` char(2) DEFAULT '' COMMENT '学历)',
  `password_error_last_time` datetime DEFAULT NULL COMMENT '最后一次输错密码时间',
  `password_error_num` int(11) DEFAULT '0' COMMENT '密码错误次数',
  `password_expire_time` datetime DEFAULT NULL COMMENT '密码过期时间',
  `password` varchar(64) NOT NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(20) NOT NULL DEFAULT '' COMMENT '密码盐',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_account` (`account`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';