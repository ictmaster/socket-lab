import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbHelper{
    public static void main(String[] args) {
        try {
            createDb();
            addDummyData();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String c_name = "org.sqlite.JDBC";
    private static String db_con = "jdbc:sqlite:socketlab.db";
    public static void createDb() throws ClassNotFoundException, SQLException {
        Connection c = null;

        Class.forName(c_name);
        c = DriverManager.getConnection(db_con);

        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS data (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FNAME TEXT NOT NULL, " +
                "LNAME TEXT NOT NULL, " +
                "PNUMBER TEXT NOT NULL, " +
                "DEPNUM TEXT NOT NULL," +
                "EMAIL TEXT NOT NULL);";

        stmt.executeUpdate(sql);
        stmt.close();
        c.close();
    }

    public static void addDummyData() throws SQLException, ClassNotFoundException {
        Connection c = null;

        Class.forName(c_name);
        c = DriverManager.getConnection(db_con);
        Statement stmt = c.createStatement();

        stmt.executeUpdate("INSERT INTO data (FNAME, LNAME, PNUMBER, DEPNUM, EMAIL) VALUES('Jonas', 'Natten', '12345','0005', 'jonas@jonas.no');");
        stmt.executeUpdate("INSERT INTO data (FNAME, LNAME, PNUMBER, DEPNUM, EMAIL) VALUES('Per', 'Andersen', '54321','0003', 'per@per.no');");
        stmt.executeUpdate("INSERT INTO data (FNAME, LNAME, PNUMBER, DEPNUM, EMAIL) VALUES('Christian', 'Kråkevik', '67890','0003', 'christian@christian.no');");
        stmt.executeUpdate("INSERT INTO data (FNAME, LNAME, PNUMBER, DEPNUM, EMAIL) VALUES('Jahn', 'Fidje', '09876','0005', 'jahn@jahn.no');");
        stmt.executeUpdate("INSERT INTO data (FNAME, LNAME, PNUMBER, DEPNUM, EMAIL) VALUES('Martin', 'Hansen', '66666','0002', 'martin@marin.no');");

        stmt.close();
        c.close();
    }

    public static String getEmailsByName(String[] args) throws ClassNotFoundException, SQLException {
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
        String sql;
        if (argList.contains("--byid") && argList.size() == 3){
            argList.remove("--byid");
            sql = "SELECT * FROM data WHERE DEPNUM = ? AND LNAME = ?";
        }else if (argList.size() == 2){
            sql = "SELECT * FROM data WHERE FNAME = ? AND LNAME = ?";
        }else{
            return "Invalid arguments...";
        }

        Connection c;
        Class.forName(c_name);
        c = DriverManager.getConnection(db_con);

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, argList.get(0));
        ps.setString(2, argList.get(1));

        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            return rs.getString("EMAIL");
        }
        c.close();

        return "No entry found...(Remember to use syntax 'getmail fname lname' or 'getmail --byid depnum lname')";
    }
}
