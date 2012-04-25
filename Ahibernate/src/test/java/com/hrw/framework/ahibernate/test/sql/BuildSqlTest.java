
package com.hrw.framework.ahibernate.test.sql;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.hrw.framework.ahibernate.builder.DataBuilder;

public class BuildSqlTest {
    private static String EXPECTED_QUERY_BY_FIELD_SQL = "SELECT * FROM testtable WHERE test = 'testvalue'";

    private static String EXPECTED_UPDATE_SQL1 = "UPDATE testtable SET test = 1";

    private static String EXPECTED_UPDATE_SQL2 = "UPDATE testtable SET test = 1, test2 = 2";

    private static String EXPECTED_UPDATE_SQL3 = "UPDATE testtable SET test = 1, test2 = 2 where id = 1";

    private static String EXPECTED_UPDATE_SQL4 = "UPDATE testtable SET test = 1, test2 = 2 where id = 1 and name = 2";

    @Test
    public void testQueryByFieldSql() throws IllegalArgumentException, IllegalAccessException {
        String sql = DataBuilder.buildQueryByFieldSql("testtable", "test", "testvalue");
        Assert.assertEquals(EXPECTED_QUERY_BY_FIELD_SQL, sql);
    }

    @Test
    public void testUpdateSql1() {
        HashMap<String, String> needUpdate = new HashMap<String, String>();
        needUpdate.put("test", "1");

        String sql = DataBuilder.buildUpdateSql("testtable", needUpdate, null);
        Assert.assertEquals(EXPECTED_UPDATE_SQL1, sql);
    }

    @Test
    public void testUpdateSql2() {
        HashMap<String, String> needUpdate = new HashMap<String, String>();
        needUpdate.put("test", "1");
        needUpdate.put("test2", "2");

        String sql = DataBuilder.buildUpdateSql("testtable", needUpdate, null);
        Assert.assertEquals(EXPECTED_UPDATE_SQL2, sql);
    }

    @Test
    public void testUpdateSql3() {
        HashMap<String, String> needUpdate = new HashMap<String, String>();
        needUpdate.put("test", "1");
        needUpdate.put("test2", "2");

        HashMap<String, String> where = new HashMap<String, String>();
        where.put("id", "1");

        String sql = DataBuilder.buildUpdateSql("testtable", needUpdate, where);
        Assert.assertEquals(EXPECTED_UPDATE_SQL3, sql);
    }

    @Test
    public void testUpdateSql4() {
        HashMap<String, String> needUpdate = new HashMap<String, String>();
        needUpdate.put("test", "1");
        needUpdate.put("test2", "2");

        HashMap<String, String> where = new HashMap<String, String>();
        where.put("id", "1");
        where.put("name", "2");

        String sql = DataBuilder.buildUpdateSql("testtable", needUpdate, where);
        Assert.assertEquals(EXPECTED_UPDATE_SQL4, sql);
    }

}
