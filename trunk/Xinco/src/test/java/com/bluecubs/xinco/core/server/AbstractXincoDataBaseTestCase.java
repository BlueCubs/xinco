package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.db.DBState;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import junit.framework.TestCase;
import org.h2.jdbcx.JdbcDataSource;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public abstract class AbstractXincoDataBaseTestCase extends TestCase {

    public static boolean deleteDatabase = true;

    public AbstractXincoDataBaseTestCase(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        XincoDBManager.setPU("XincoTest");
        assertTrue(XincoDBManager.getState().equals(DBState.VALID));
    }
    
    @Override
    protected void tearDown() throws Exception {
        if (deleteDatabase) {
            Connection conn = null;
            Statement stmt = null;
            try {
                DataSource ds = new JdbcDataSource();
                ((JdbcDataSource) ds).setPassword("xinco");
                ((JdbcDataSource) ds).setUser("root");
                ((JdbcDataSource) ds).setURL(
                        "jdbc:h2:file:./target/data/xinco-test;AUTO_SERVER=TRUE");
                //Load the H2 driver
                Class.forName("org.h2.Driver");
                conn = ds.getConnection();
                stmt = conn.createStatement();
                stmt.executeUpdate("DROP ALL OBJECTS DELETE FILES");
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException ex) {
                    fail();
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    fail();
                }
            }
        }
        XincoDBManager.close();
        //Restore to default to make it atomic.
        deleteDatabase=true;
    }
}
