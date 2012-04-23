
package com.hrw.framework.ahibernate.test.sql;

import org.junit.Assert;
import org.junit.Test;

import com.hrw.framework.ahibernate.builder.DataBuilder;

public class BuildSqlTest {

    @Test
    public void testQuerySql() throws IllegalArgumentException, IllegalAccessException {
    
        String sql = DataBuilder.buildQuerySql("testtable", "test", "testvalue");
        System.out.println(sql);
        Assert.assertNotNull(sql);
    }

}
