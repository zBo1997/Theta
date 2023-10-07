-- 查看主库的bing-log文件名字以及，日志文件的偏移量 在slave节点开启时候设置这个只需要用到
show master status ;

show variables like '%log_bin%';


GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;

flush privileges;


SHOW GLOBAL VARIABLES like 'server\_id';

CREATE USER data_sync IDENTIFIED BY 'v9#QKUeS*6';
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'data_sync'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'data_sync'@'%' ;


show slave status;

show grants for 'slave'@'%';