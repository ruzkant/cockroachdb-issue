package cockroachdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CockroachIssue {


    public static void main(String[] args) throws Exception {
        boolean delayAfterQuery = false;
        boolean delayAfterCreate = false;
        if(args.length > 0) {
            delayAfterQuery = Boolean.parseBoolean(args[0]);
        }
        if(args.length > 1) {
            delayAfterCreate = Boolean.parseBoolean(args[1]);
        }
        String tableName = "cockroach_issue_table";
        Connection conn = null;
        Statement st = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:26257/cockroach_issue", "cockroach_issue_user", null);
            conn.setAutoCommit(false);
            st = conn.createStatement();
            ResultSet tables = st.executeQuery("show tables");
            tables.close();

            if(delayAfterQuery) {
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            st = conn.createStatement();
            st.execute("create table " + tableName + " (name varchar)");
            st.close();

            if(delayAfterCreate) {
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Before Insert");
            st = conn.createStatement();
            st.execute("insert into " + tableName + " values ('Bilbo')");
            st.close();
            System.out.println("After Insert");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}
