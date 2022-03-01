package com.momo.theta.segment.count;

import com.momo.theta.segment.api.generator.NumberSequenceGenerator;
import com.momo.theta.segment.config.SegmentConfig;
import com.momo.theta.segment.model.DbSequenceConfig;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;
import java.util.Map;

/**
 * 多服务节点数据库Sequence生成Generator
 */
public class DbNumberSequenceGenerator implements NumberSequenceGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(DbNumberSequenceGenerator.class);

    protected int length = 0;
    /**
     * 数据库名称ORACLE
     */
    private static final String DATABASE_ORACLE = "ORACLE";
    /**
     * 数据库名称DB2
     */
    private static final String DATABASE_DB2 = "DB2";

    /**
     * 数据库名称MYSQL
     */
    private static final String DATABASE_MYSQL = "MYSQL";
    /**
     * 建序列号模板
     */
    private final static String SEQUENCE_CREATE_TEMPLATE = "INSERT INTO %s (%s, %s, %s) VALUES (%s, %s, %s)";
    /**
     * DB2查询模板
     */
    private final static String SEQUENCE_CONFIG_QUERY_TEMPLATE_DB2 = "SELECT %s,%s FROM %s WHERE %s=? FOR UPDATE WITH RS";
    /**
     * DB2更新模板
     */
    private final static String SEQUENCE_CONFIG_UPDATE_TEMPLATE_DB2 = "UPDATE %s SET %s=? WHERE %s=?";
    /**
     * ORACLE查询模板
     */
    private final static String SEQUENCE_CONFIG_QUERY_TEMPLATE_ORACLE = "SELECT %s,%s FROM %s WHERE %s=? FOR UPDATE";
    /**
     * ORACLE更新模板
     */
    private final static String SEQUENCE_CONFIG_UPDATE_TEMPLATE_ORACLE = "UPDATE %s SET %s=? WHERE %s=?";
    /**
     * MYSQL查询模板
     */
    private final static String SEQUENCE_CONFIG_QUERY_TEMPLATE_MYSQL = "SELECT %s,%s FROM %s WHERE %s=? FOR UPDATE";
    /**
     * MYSQL更新模板
     */
    private final static String SEQUENCE_CONFIG_UPDATE_TEMPLATE_MYSQL = "UPDATE %s SET %s=? WHERE %s=?";

    /**
     * DB2查询上次重置时间模板
     */
    private final static String SEQUENCE_RESETTIME_QUERY_TEMPLATE_DB2 = "SELECT %s FROM %s WHERE %s=? FOR UPDATE WITH RS";
    /**
     * DB2更新上次重置时间模板
     */
    private final static String SEQUENCE_RESETTIME_UPDATE_TEMPLATE_DB2 = "UPDATE %s SET %s=?,%s=? WHERE %s=?";
    /**
     * ORACLE查询上次重置时间模板
     */
    private final static String SEQUENCE_RESETTIME_QUERY_TEMPLATE_ORACLE = "SELECT %s FROM %s WHERE %s=? FOR UPDATE";
    /**
     * ORACLE更新上次重置时间模板
     */
    private final static String SEQUENCE_RESETTIME_UPDATE_TEMPLATE_ORACLE = "UPDATE %s SET %s=?,%s=? WHERE %s=?";
    /**
     * MYSQL查询上次重置时间模板
     */
    private final static String SEQUENCE_RESETTIME_QUERY_TEMPLATE_MYSQL = "SELECT %s FROM %s WHERE %s=? FOR UPDATE";
    /**
     * MYSQL更新上次重置时间模板
     */
    private final static String SEQUENCE_RESETTIME_UPDATE_TEMPLATE_MYSQL = "UPDATE %s SET %s=?,%s=? WHERE %s=?";

    /**
     * 序列号配置表列“上次重置时间”默认值
     */
    private final static String DEFAULT_LASTRESET_TIME_KEY = "LASTRESET_TIME";
    /**
     * 序列号配置表列“当前序列号值”默认值
     */
    private final static String DEFAULT_CURRENT_SEQUENCE_KEY = "CURRENT";
    /**
     * 序列号配置表列“最大序列号值”默认值
     */
    private final static String DEFAULT_MAXIMUM_SEQUENCE_KEY = "MAXIMUM";
    /**
     * 序列号配置表列“序列号标示”默认值
     */
    private final static String DEFAULT_ID_SEQUENCE_KEY = "ID";
    /**
     * 序列号配置表名称默认值
     */
    private final static String DEFAULT_TABLE_NAME = "SEQUENCE_CONFIG";
    /**
     * 事务定义
     */
    private final DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
    /**
     * 对象锁
     */
    private final Object lock = new Object();
    /**
     * 数据源事务控制器
     */
    private DataSourceTransactionManager dataSourceTransactionManager;
    /**
     * 数据库序列号唯一标识
     */
    private String id;
    /**
     * 序列号步长，默认为100（集群部署时，不同实例可以不一样）
     */
    private long step = 100L;
    /**
     * 当前服务器实例内存中最大序列号
     */
    private long maxSequenceInServer = 0L;
    /**
     * 当前服务器实例内存中最近使用序列号
     */
    private long currentSequenceInServer = 0L;

    /**
     * 序列号配置表列“当前序列号值”
     */
    private String currentSequenceKey = DEFAULT_CURRENT_SEQUENCE_KEY;
    /**
     * 序列号配置表列“最大序列号值”
     */
    private String maximumSequenceKey = DEFAULT_MAXIMUM_SEQUENCE_KEY;
    /**
     * 序列号配置表列“序列号标示”
     */
    private String idKey = DEFAULT_ID_SEQUENCE_KEY;
    /**
     * 序列号配置表名称
     */
    private String tableName = DEFAULT_TABLE_NAME;

    /**
     * 序列号配置表列“上次重置时间”
     */
    private String lastRestTimeSequenceKey = DEFAULT_LASTRESET_TIME_KEY;
    /**
     * 首次启动时，检查数据库是否有指定的序列号记录，如果没有则使用该语句进行创建
     */
    private String sequenceCreateSql = null;
    /**
     * 获取SequenceConfig查询语句
     * 根据序列号唯一标识查询配置，返回2列，第1列为当前序列号，第2列为最大可用序列号
     * 查询语句必须加锁以保证两个执行本语句的事务串行执行。
     * 示例（DB2数据库）：SELECT CURRENT,MAXIMUM FROM SEQUENCE_CONFIG WHERE ID=? FOR UPDATE WITH RS
     */
    private String sequenceConfigQuery = null;

    /**
     * 更新重置时间点
     * 示例（DB2数据库）：UPDATE SEQUENCE_CONFIG SET DEFAULT_LASTRESET_TIME_KEY=? WHERE ID=?
     */
    private String lastResetTimeUpdate = null;

    /**
     * 获取上次重置时间，用于与当前服务器时间进行比对，当超过指定重置间隔后，将对序列号进行重置
     * 查询语句必须加锁以保证两个执行本语句的事务串行执行。
     * 示例（DB2数据库）：SELECT DEFAULT_LASTRESET_TIME_KEY FROM SEQUENCE_CONFIG WHERE ID=? FOR UPDATE WITH RS
     */
    private String lastResetTimeQuery = null;

    /**
     * 获取SequenceConfig更新语句
     * 根据序列号配置唯一标示更新当前序列号
     * 示例（DB2数据库）：UPDATE SEQUENCE_CONFIG SET CURRENT=? WHERE ID=?
     */
    private String sequenceConfigUpdate = null;

    /**
     * 循环重置周期
     */
    private String resetCronExp;

    /**
     * 起始值
     */
    private long minSequenceValue;

    /**
     * 最大值
     */
    private long maxSequenceValue;

    /**
     * 记录本服务器实例重置时间点
     */
    private Date lastUsedTime;

    /**
     * 重置起始值
     */
    private CronExpression exp;

    /**
     * 查询模板
     */
    private String SEQUENCE_QUERY_SQL_TEMPLATE = "SELECT %s,%s FROM %s WHERE %s=?";

    private String sequenceQuerySql;

    /**
     * 获取具体的sequence
     * @return
     */
    @Override
    public String getSequence() {
        String numberSequence = "";
        //对象头的锁，是为了防止多线程访问的问题
        synchronized (lock) {
            boolean getFromServer = true;
            if (StringUtils.hasText(resetCronExp)) {
                //通过表达式获取获取下次需要重置的时间
                Date nextComparedTime = exp.getNextValidTimeAfter(lastUsedTime);
                //判断当前是否满足满足重置时间。
                if (new Date().compareTo(nextComparedTime) >= 0) {
                    getFromServer = false;
                }
            }
            //如果不满足重置时间并且当前的可sequence就进行当前“计数”累加
            if (getFromServer && (currentSequenceInServer < maxSequenceInServer)) {
                currentSequenceInServer = currentSequenceInServer + 1L;
                numberSequence = currentSequenceInServer + "";
            } else {
                getSequenceBatchFromDb();
                numberSequence = getSequence();
            }
        }
        return numberSequence;
    }

    /**
     * 获取数据库中序列号；在获取一个可用值后，会增加一个步长到数据库值。当序列号即将超过配置的最大值时,重置循环使用。
     * 本方法使用数据库事务，且扩散行为为强制开启一个新的事务，否则使用已有事务时多线程情况下会出现序列号重复的问题。
     */
    private void getSequenceBatchFromDb() {

        long currentSequenceInServerBefore = currentSequenceInServer;
        long maxSequenceInServerBefore = maxSequenceInServer;

        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus txStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        DataSource dataSource = dataSourceTransactionManager.getDataSource();

        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);

            if (StringUtils.hasText(resetCronExp)) {
                Date lastResetTime = getLastResetTime(connection);
                if (lastResetTime == null || new Date().compareTo(exp.getNextValidTimeAfter(lastResetTime)) >= 0) {
                    updateResetTime(connection);// 同时更新时间和初始值
                    lastUsedTime = new Date();
                }
            }

            DbSequenceConfig config = getSequenceConfig(connection);
            long currentInDb = config.getCurrent();
            long maximumInDb = config.getMaximum();
            //按照对应的step的步长进行自增
            long nextMaxInServer = currentInDb + step;

            if (nextMaxInServer <= maximumInDb) {
                // nextMaxInServer available
                currentSequenceInServer = currentInDb;
                maxSequenceInServer = nextMaxInServer;
            } else {
                // nextMaxInServer not available
                // 序列号即将用完,重置循环使用
                // 这种处理方式也不是很好 FIXME
                // 这里step
                currentSequenceInServer = minSequenceValue;
                maxSequenceInServer = step;
            }

            boolean success = updateSequenceConfig(connection, id, maxSequenceInServer);
            if (!success) {
                throw new RuntimeException("Fail to update DbSequenceConfig row for ID [" + id + "], CURRENT ["
                        + maxSequenceInServer + "].");
            }

            dataSourceTransactionManager.commit(txStatus);
        } catch (RuntimeException tfe) {
            // reset local variables
            currentSequenceInServer = currentSequenceInServerBefore;
            maxSequenceInServer = maxSequenceInServerBefore;

            LOG.error(tfe.getMessage(), tfe);

            dataSourceTransactionManager.rollback(txStatus);
            throw tfe;
        } catch (Exception e) {
            currentSequenceInServer = currentSequenceInServerBefore;
            maxSequenceInServer = maxSequenceInServerBefore;

            LOG.error(e.getMessage(), e);

            dataSourceTransactionManager.rollback(txStatus);
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     * 检查db是否有指定的序列号记录，如果没有则进行创建
     *
     * @throws SQLException
     */
    protected void checkDbSequence() throws SQLException {
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus txStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        DataSource dataSource = dataSourceTransactionManager.getDataSource();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        PreparedStatement ps = null;
        ResultSet rs = null;
        Statement statement = null;
        boolean foundResult = false;
        try {
            ps = connection.prepareStatement(sequenceQuerySql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (foundResult) {
                    throw new RuntimeException("More than one DbSequenceConfig row found for id [" + id + "]. DbSequenceConfig id must be unique.");
                }
                long current = rs.getLong(1);
                long maximum = rs.getLong(2);
                if (current > maximum) {
                    throw new RuntimeException("Illegal DbSequenceConfig row found for id [" + id + "]. Current value must less than maximum.");
                }
                foundResult = true;
            }
            if (!foundResult) {
                statement = connection.createStatement();
                int result = statement.executeUpdate(sequenceCreateSql);
                if (result == 0) {
                    throw new RuntimeException("cannot create sequence record <" + id + "> in table " + tableName);
                }
            }
            dataSourceTransactionManager.commit(txStatus);
        } catch (RuntimeException cause) {
            // 避免后续的数据库异常淹没此异常
            LOG.error(cause.getMessage(), cause);

            dataSourceTransactionManager.rollback(txStatus);

            throw cause;
        } catch (SQLException cause) {
            // 避免后续的数据库异常淹没此异常
            LOG.error(cause.getMessage(), cause);

            dataSourceTransactionManager.rollback(txStatus);

            throw cause;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } finally {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }
    }

    /**
     * 查询序列号配置
     *
     * @return
     * @throws SQLException
     */
    protected DbSequenceConfig getSequenceConfig(Connection connection) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        // Loop over results - although we are only expecting one result, since id should be unique
        boolean foundResult = false;
        DbSequenceConfig config = new DbSequenceConfig(id);
        try {
            ps = connection.prepareStatement(sequenceConfigQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (foundResult) {
                    throw new RuntimeException("More than one DbSequenceConfig row found for id [" + id
                            + "]. DbSequenceConfig id must be unique.");
                }
                long current = rs.getLong(1);
                long maximum = rs.getLong(2);
                if (current > maximum) {
                    throw new RuntimeException("Illegal DbSequenceConfig row found for id [" + id
                            + "]. Current value must less than maximum.");
                }
                config.setCurrent(current);
                config.setMaximum(maximum);
                foundResult = true;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        if (!foundResult) {
            throw new RuntimeException("No DbSequenceConfig row found for id [" + id + "].");
        }
        return config;
    }

    /**
     * 更新当前可用序列号到服务器
     *
     * @throws SQLException
     */
    protected boolean updateSequenceConfig(Connection connection, String id, long current) throws SQLException {

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sequenceConfigUpdate);
            ps.setLong(1, current);
            ps.setString(2, id);
            int affected = ps.executeUpdate();
            return affected == 1 ? true : false;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * 更新序列号重置时间到服务器
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    protected boolean updateResetTime(Connection connection) throws SQLException {

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(lastResetTimeUpdate);
            ps.setTimestamp(1, new Timestamp(new Date().getTime()));
            ps.setLong(2, minSequenceValue);
            ps.setString(3, id);
            int affected = ps.executeUpdate();
            return affected == 1 ? true : false;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * 查询上次重置时间
     *
     * @return
     * @throws SQLException
     */
    protected Date getLastResetTime(Connection connection) throws SQLException {
        Timestamp lastResetTime = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean foundResult = false;
        try {
            ps = connection.prepareStatement(lastResetTimeQuery);
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (foundResult) {
                    throw new RuntimeException("More than one DbSequenceConfig row found for id [" + id
                            + "]. DbSequenceConfig id must be unique.");
                }
                lastResetTime = rs.getTimestamp(1);
                foundResult = true;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        if (!foundResult) {
            throw new RuntimeException("No DbSequenceConfig row found for id [" + id + "].");
        }
        return lastResetTime;
    }


    /**
     * 根据DatabaseMetaData确定数据库类型
     *
     * @return
     * @throws SQLException
     */
    private String resolveDatabaseName() throws SQLException {

        Connection connection = null;
        String databaseName = "";
        DataSource dataSource = dataSourceTransactionManager.getDataSource();
        try {
            connection = DataSourceUtils.getConnection(dataSource);
            String databaseProductName = connection.getMetaData().getDatabaseProductName();
            String upperDatabaseProductName = databaseProductName.toUpperCase();
            if (upperDatabaseProductName.contains(DATABASE_DB2)) {
                databaseName = DATABASE_DB2;
            } else if (upperDatabaseProductName.contains(DATABASE_ORACLE)) {
                databaseName = DATABASE_ORACLE;
            } else if (upperDatabaseProductName.contains(DATABASE_MYSQL)) {
                databaseName = DATABASE_MYSQL;
            } else {
                throw new RuntimeException("Unsupported database: " + databaseProductName);
            }
        } finally {
            if (connection != null) {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }
        return databaseName;
    }

    /**
     * 根据数据库类型和配置准备查询语句和更新语句
     *
     * @param databaseName 数据库名称
     * @param currentSequenceKey 当前序列
     * @param maximumSequenceKey 最大序列
     * @param idKey 配置主键
     * @param tableName 表明你
     */
    private void prepareSql(String databaseName, String currentSequenceKey, String maximumSequenceKey, String idKey,
                            String tableName) {

        sequenceCreateSql = String.format(SEQUENCE_CREATE_TEMPLATE, tableName,
                idKey, currentSequenceKey, maximumSequenceKey, "'" + id + "'", minSequenceValue, maxSequenceValue);

        if (DATABASE_DB2.equals(databaseName)) {
            sequenceConfigQuery = String.format(SEQUENCE_CONFIG_QUERY_TEMPLATE_DB2, currentSequenceKey,
                    maximumSequenceKey, tableName, idKey);
            sequenceConfigUpdate = String.format(SEQUENCE_CONFIG_UPDATE_TEMPLATE_DB2, tableName, currentSequenceKey,
                    idKey);
            lastResetTimeQuery = String.format(SEQUENCE_RESETTIME_QUERY_TEMPLATE_DB2, lastRestTimeSequenceKey,
                    tableName, idKey);
            lastResetTimeUpdate = String.format(SEQUENCE_RESETTIME_UPDATE_TEMPLATE_DB2, tableName, lastRestTimeSequenceKey,
                    currentSequenceKey, idKey);
        } else if (DATABASE_ORACLE.equals(databaseName)) {
            sequenceConfigQuery = String.format(SEQUENCE_CONFIG_QUERY_TEMPLATE_ORACLE, currentSequenceKey,
                    maximumSequenceKey, tableName, idKey);
            sequenceConfigUpdate = String.format(SEQUENCE_CONFIG_UPDATE_TEMPLATE_ORACLE, tableName, currentSequenceKey,
                    idKey);
            lastResetTimeQuery = String.format(SEQUENCE_RESETTIME_QUERY_TEMPLATE_ORACLE, lastRestTimeSequenceKey,
                    tableName, idKey);
            lastResetTimeUpdate = String.format(SEQUENCE_RESETTIME_UPDATE_TEMPLATE_ORACLE, tableName, lastRestTimeSequenceKey,
                    currentSequenceKey, idKey);
        } else if (DATABASE_MYSQL.equals(databaseName)) {
            sequenceConfigQuery = String.format(SEQUENCE_CONFIG_QUERY_TEMPLATE_MYSQL, currentSequenceKey,
                    maximumSequenceKey, tableName, idKey);
            sequenceConfigUpdate = String.format(SEQUENCE_CONFIG_UPDATE_TEMPLATE_MYSQL, tableName, currentSequenceKey,
                    idKey);
            lastResetTimeQuery = String.format(SEQUENCE_RESETTIME_QUERY_TEMPLATE_MYSQL, lastRestTimeSequenceKey,
                    tableName, idKey);
            lastResetTimeUpdate = String.format(SEQUENCE_RESETTIME_UPDATE_TEMPLATE_MYSQL, tableName, lastRestTimeSequenceKey,
                    currentSequenceKey, idKey);
        }
        if (!StringUtils.hasText(sequenceConfigQuery)) {
            throw new RuntimeException("sequenceConfigQuery is empty");
        }
        if (!StringUtils.hasText(sequenceConfigUpdate)) {
            throw new RuntimeException("sequenceConfigUpdate is empty");
        }
        if (!StringUtils.hasText(lastResetTimeQuery)) {
            throw new RuntimeException("lastResetTimeQuery is empty");
        }
        if (!StringUtils.hasText(lastResetTimeUpdate)) {
            throw new RuntimeException("lastResetTimeUpdate is empty");
        }

        sequenceQuerySql = String.format(SEQUENCE_QUERY_SQL_TEMPLATE, currentSequenceKey,
                maximumSequenceKey, tableName, idKey);
    }

    public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
        Assert.notNull(dataSourceTransactionManager, "dataSourceTransactionManager should not be null");
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    public String getId() {

        return id;
    }

    public void setStep(int step) {
        Assert.isTrue(step > 0, "step should greater than 0");
        this.step = step;
    }

    public void setCurrentSequenceKey(String currentSequenceKey) {

        this.currentSequenceKey = currentSequenceKey;
    }

    public void setMaximumSequenceKey(String maximumSequenceKey) {

        this.maximumSequenceKey = maximumSequenceKey;
    }

    public void setIdKey(String idKey) {

        this.idKey = idKey;
    }

    public void setTableName(String tableName) {

        this.tableName = tableName;
    }

    public void setLastRestTimeSequenceKey(String lastRestTimeSequenceKey) {
        this.lastRestTimeSequenceKey = lastRestTimeSequenceKey;
    }

    public void setResetCronExp(String resetCronExp) {
        this.resetCronExp = resetCronExp;
    }


    public void setMinSequenceValue(long minSequenceValue) {
        this.minSequenceValue = minSequenceValue;
    }

    public void setMaxSequenceValue(long maxSequenceValue) {
        this.maxSequenceValue = maxSequenceValue;
    }


    @Override
    public void init(SegmentConfig segmentConfig) throws Exception {
        Map<String, Object> args = segmentConfig.getArgs();
        if (args == null || !args.containsKey("id")) {
            throw new RuntimeException("sequence number need a id arg!");
        }
        this.id = (String) args.get("id");
        if (args.containsKey("idKey")) {
            this.idKey = (String) args.get("idKey");
        }
        if (args.containsKey("tableName")) {
            this.tableName = (String) args.get("tableName");
        }
        if (args.containsKey("step")) {
            this.step = Integer.parseInt((String) args.get("step"));
        }
        if (args.containsKey("length")) {
            this.length = Integer.parseInt((String) args.get("length"));
        }
        if (args.containsKey("currentSequenceKey")) {
            this.currentSequenceKey = (String) args.get("currentSequenceKey");
        }
        if (args.containsKey("maximumSequenceKey")) {
            this.maximumSequenceKey = (String) args.get("maximumSequenceKey");
        }
        if (args.containsKey("maxSequenceValue")) {
            this.maxSequenceValue = Long.parseLong(args.get("maxSequenceValue").toString());
        } else {
            if (args.containsKey("length")) {
                int l = Integer.parseInt((String) args.get("length"));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < l; i++) {
                    sb.append("9");
                }
                this.maxSequenceValue = Long.parseLong(sb.toString());
            }
        }
        if (args.containsKey("resetCronExp")) {
            this.resetCronExp = (String) args.get("resetCronExp");
        }
        if (args.containsKey("lastRestTimeSequenceKey")) {
            this.lastRestTimeSequenceKey = (String) args.get("lastRestTimeSequenceKey");
        }
        String transactionManager = "transactionManager";
        if (!args.containsKey("transactionManager")) {
            transactionManager = (String) args.get("transactionManager");
        }
        //这里是在
        this.dataSourceTransactionManager = (DataSourceTransactionManager) segmentConfig.getInitArg("transactionManager");

        String databaseName = resolveDatabaseName();
        prepareSql(databaseName, currentSequenceKey, maximumSequenceKey, idKey, tableName);
        checkDbSequence();
        //如果有重置的Cron表达式，就设置为自定义cron表达式
        if (StringUtils.hasText(resetCronExp)) {
            //构造出一个指定的时间表达式
            exp = new CronExpression(resetCronExp);
            //默认的重置时间的也就是当前时间
            lastUsedTime = new Date();
        }
        getSequenceBatchFromDb();
        if (maxSequenceInServer <= 0L) {
            throw new RuntimeException("maxium sequence number should be greater then zero!");
        }


    }

    @Override
    public String getType() {
        return "dbNumberSequence";
    }
}
