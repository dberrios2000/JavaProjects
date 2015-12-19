package JavaProcFram;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Implements the interface to the application's database.
 *
 */
public class Database {
	
	/**
	 * The JDBC driver class name.
	 */
	private String jdbcDriverClassName = "com.mysql.jdbc.Driver";
	
	/**
	 * The JDBC connection URL.
	 */
	private String connectionURL = "jdbc:mysql://localhost/test?user=root&password=";
	
	/**
	 * The JDBC connection.
	 */
	private Connection connection;
	
	/**
	 * Singleton instance.
	 */
	private static Database sharedInstance;
	
	/**
	 * Time of last query (in milliseconds).
	 */
	private long lastQueryTime = 0;

	/**
	 * Constructor.
	 */
	public Database() {
		sharedInstance = this;
	}
	
	/**
	 * Returns the shared instance, initializing it if needed.
	 * 
	 * @return
	 */
	public static Database getSharedInstance() {
		if (sharedInstance == null) {
			sharedInstance = new Database();
			sharedInstance.connect();
		}
		return sharedInstance;
	}
	
	/**
	 * Opens the connection to the database.
	 */
	public void connect() {
		if (connection != null) {
			return;
		}
		try {
			Class.forName(jdbcDriverClassName);
			connection = DriverManager.getConnection(connectionURL);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the connection to the database.
	 */
	public void disconnect() {
		if (connection == null) {
			return;
		}
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Inserts the specified lines to the ALERTS table.
	 * 
	 * @param lines
	 */
	public void insertLines(List<String> lines) {
		String sql = "INSERT INTO ALERTS (`date`,`display`,`message`) VALUES (?,?,?)";
		PreparedStatement stmt = null;
		try {
			connection.setAutoCommit(false);
			stmt = connection.prepareStatement(sql);
			for (String line : lines) {
				stmt.setTimestamp(1, new Timestamp(new Date(System.currentTimeMillis()).getTime()));
				stmt.setInt(2, 1);
				stmt.setString(3, line);
				stmt.addBatch();
			}
			int rowCount = stmt.executeBatch().length;
			System.out.println("Inserted " + rowCount + " row(s) in database");
		} catch (SQLException e) {
			System.out.println("skipping message"); //added but not test 20141221
		} finally {
			try {
				connection.setAutoCommit(true);
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				System.out.println("skipping message");  //added but not test 20141221
			}
		}
	}

	/**
	 * Retrieves all new alerts from the database, i.e. all entries that were 
	 * inserted between the last query time and the current one.
	 * 
	 * @return
	 */
	public Vector<Alert> getAllNewAlerts() {
		Vector<Alert> result = new Vector<Alert>();
		String sql = "SELECT * FROM ALERTS WHERE date_format(`date`,'%r') > date_format(?,'%r') ORDER BY `id` DESC";
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setTimestamp(1, new Timestamp(new Date(lastQueryTime).getTime()));
			rset = stmt.executeQuery();
			while (rset.next()) {
				Alert alert = new Alert();
				alert.setId(rset.getLong("id"));
				alert.setDate(new Date(rset.getTimestamp("date").getTime()));
				alert.setDisplay(rset.getInt("display"));
				alert.setMessage(rset.getString("message"));
				result.add(alert);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		} finally {
			try {
				if (rset != null) {
					rset.close();
					rset = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				lastQueryTime = System.currentTimeMillis();
			} catch (SQLException e) {}
		}
	}
	
	/**
	 * Deletes everything from the ALERTS table.
	 */
	public void clear() {
		String sql = "DELETE FROM ALERTS";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sql);
			int rowCount = stmt.executeUpdate();
			System.out.println("Deleted " + rowCount + " row(s) from database");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {}
		}
	}

}
