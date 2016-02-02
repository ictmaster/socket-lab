import java.sql.*;

public class DbHelper {
    private static String c_name = "org.sqlite.JDBC";
    private static String db_con = "jdbc:sqlite:socketlab.db";
    public static void createDb() throws ClassNotFoundException, SQLException {
        Connection c = null;

        Class.forName(c_name);
        c = DriverManager.getConnection(db_con);

        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE data " +
                "(DEPNUM INT PRIMARY KEY    NOT NULL), " +
                "FNAME   TEXT    NOT NULL, " +
                "LNAME   TEXT    NOT NULL, " +
                "PNUMBER TEXT    NOT NULL, " +
                "EMAIL   TEXT    NOT NULL";

        stmt.executeUpdate(sql);
        stmt.close();
        c.close();
    }

    public static void addDummyData() throws SQLException, ClassNotFoundException {
        Connection c = null;

        Class.forName(c_name);
        c = DriverManager.getConnection(db_con);

        Statement stmt = c.createStatement();
        stmt.executeUpdate("INSERT INTO data (FNAME, LNAME, PNUMBER, EMAIL) VALUES('Jonas', 'Natten', '12345', 'jonas@jonas.it');");
        stmt.executeUpdate("INSERT INTO data (FNAME, LNAME, PNUMBER, EMAIL) VALUES('Per', 'Andersen', '54321', 'per@per.no');");
        stmt.executeUpdate("INSERT INTO data (FNAME, LNAME, PNUMBER, EMAIL) VALUES('Christian', 'Kråkevik', '67890', 'christian@christian.no');");
        stmt.executeUpdate("INSERT INTO data (FNAME, LNAME, PNUMBER, EMAIL) VALUES('Jahn', 'Fidje', '09876', 'jahn@jahn.no');");
        stmt.executeUpdate("INSERT INTO data (FNAME, LNAME, PNUMBER, EMAIL) VALUES('Martin', 'Hansen', '66666', 'martin@marin.no');");

        stmt.close();
        c.close();
    }

    public static String getEmailsByName(String[] args) throws ClassNotFoundException, SQLException {
        if (args.length != 2){
            return "Error! expected exactly 2 arguments, "+args.length+" was given...";
        }
        String firstName = args[0];
        String lastName = args[1];

        Connection c = null;

        Class.forName(c_name);
        c = DriverManager.getConnection(db_con);

        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM data WHERE FNAME = ? AND LNAME = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        //TODO: get mail from db
        stmt.executeUpdate(sql);
        stmt.close();
        c.close();


        return "getmail not yet implemented with args "+String.join(", ",args);
    }
}
