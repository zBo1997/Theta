# 首先停止数据同步相关的线程： slave I/O 线程和 slave SQL 线程
STOP SLAVE  ;

# 为了避免可能发生的错误，直接重置客户端
RESET  SLAVE  ;

#设置主从同步隶属关系
# mysql_master 为主数据库的容器服务名称，如果是非容器部署，就填写主服务器的ip
# MASTER_LOG_FILE=binlog_filename.000005 为主数据库binlog的文件名
# MASTER_LOG_POS=156 为binlog日志开始同步时的位置
# Binlog_Do_DB=db_ginskeleton  需要同步的数据库
# Binlog_Ignore_DB=mysql,test  指定忽略同步的数据库

CHANGE MASTER TO MASTER_HOST='mysql_master',MASTER_PORT=3306,MASTER_USER='data_sync',MASTER_PASSWORD='v9#QKUeS*6',MASTER_LOG_FILE='binlog.000002',MASTER_LOG_POS=904;

#启动slaver 服务
START   SLAVE  ;


# 查看从数据库的状态
SHOW   SLAVE  STATUS  ;