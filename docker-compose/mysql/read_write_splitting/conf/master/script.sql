-- 查看主库的转u'tai
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