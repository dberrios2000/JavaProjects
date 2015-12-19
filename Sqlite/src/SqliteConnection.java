
import java.sql.*;

public class SqliteConnection
{
  public static void main( String args[] )
  {
	  
	  Connection c = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:/Users/dberrios2000/Documents/db/test.db");
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Opened database successfully");
  }
}