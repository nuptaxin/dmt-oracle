package org.renix.dmt.oracle.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.renix.dmt.oracle.DataMigrationTool;
import org.renix.dmt.oracle.bean.DataValue;
import org.sql2o.Sql2o;

public class DatabaseUtil {
    private static Logger LOGGER = Logger.getLogger(DatabaseUtil.class);
    private static Sql2o sql2o;

    public static final Sql2o getSql2o() {
        return sql2o;
    }

    public static final void setSql2o(Sql2o sql2o) {
        DatabaseUtil.sql2o = sql2o;
    }

    private static BasicDataSource dataSource = (BasicDataSource) DataMigrationTool.ctx
            .getBean("dataSource");

    /**
     * 空表设置自动分配空间
     * 
     * @return
     */
    public static void AllocateExtent() {
        String sql = "select 'alter table '||table_name||' allocate extent' "
                + "from user_tables t where t.PARTITIONED ='NO' and "
                + "t.num_rows=0 and t.TABLE_NAME not in "
                + "(select t1.segment_name from USER_SEGMENTS t1) and "
                + "t.TABLE_NAME not like '%test%'";
        List<String> strList = sql2o.createQuery(sql).executeScalarList();
        for (String string : strList) {
            sql2o.createQuery(string).executeUpdate();
        }
    }

    public static List<DataValue> fetchNormalTables() {
        String sql = "select t.TABLE_NAME as TABLENAME, t1.BYTES as TABLEBYTES from USER_TABLES t,USER_SEGMENTS t1 "
                + "where t.PARTITIONED ='NO' and t.TABLE_NAME=t1.SEGMENT_NAME "
                + "and t.TABLE_NAME not in (select t3.TABLE_NAME from USER_LOBS t3)";
        List<DataValue> dataValueList = sql2o.createQuery(sql).executeAndFetch(
                DataValue.class);
        return dataValueList;
    }

    public static List<DataValue> fetchLobTables() {
        String sql = "select t.TABLE_NAME as TABLENAME, t1.BYTES as TABLEBYTES from USER_LOBS t, USER_SEGMENTS t1 "
                + "where t.SEGMENT_NAME = t1.SEGMENT_NAME";
        List<DataValue> dataValueList = sql2o.createQuery(sql).executeAndFetch(
                DataValue.class);
        return dataValueList;
    }

    public static List<DataValue> fetchParTables() {
        String sql = "select t.SEGMENT_NAME as TABLENAME, t.PARTITION_NAME as PARNAME, t.BYTES as TABLEBYTES from USER_SEGMENTS t where t.SEGMENT_TYPE = 'TABLE PARTITION' order by t.SEGMENT_NAME,t.PARTITION_NAME";
        List<DataValue> dataValueList = sql2o.createQuery(sql).executeAndFetch(
                DataValue.class);
        return dataValueList;
    }

    public static boolean connTest() {
        try {
            sql2o.createQuery("select * from dual");
        } catch (Exception e) {
            LOGGER.error(e);
            return false;
        }
        return true;
    }

    public static List<DataValue> fetchAllTables() {
        List<DataValue> dataValueList = new ArrayList<DataValue>();
        dataValueList.addAll(fetchNormalTables());
        dataValueList.addAll(fetchParTables());
        return dataValueList;
    }

    public static void init() {
        sql2o = new Sql2o(dataSource);
    }

    public static void reConn(String url, String userId) {
        try {
            // 这里需要先关闭数据源，才可以使新的数据源设置生效
            dataSource.close();
        } catch (SQLException e) {
            LOGGER.warn("关闭从Spring获取的数据源时出现异常！", e);
        }
        String[] strArray = StringUtils.split(url, "/");
        dataSource.setUrl("jdbc:oracle:thin:@" + strArray[0] + ":"
                + strArray[1]);
        String[] strArray1 = StringUtils.split(userId, "/");
        dataSource.setUsername(strArray1[0]);
        dataSource.setPassword(strArray1[1]);
        sql2o = new Sql2o(dataSource);
    }
}
