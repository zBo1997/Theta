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