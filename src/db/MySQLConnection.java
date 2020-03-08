package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;

import entity.Item;

public class MySQLConnection {
	// make a connection, utilize JDBC to implement the connection between
	// MYSQLClient and MySQL
	private Connection conn;

	// Constructor
	public MySQLConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			conn = DriverManager.getConnection(MySQLDBUtil.URL);
			// catch a parent exception
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// during read/write operation, in the end need to close the stream.
	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getFullname(String userId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return "";
		}
		String name = "";
		try {
			String sql = "SELECT first_name, last_name FROM users WHERE user_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				name = rs.getString("first_name") + " " + rs.getString("last_name");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return name;
	}

	public boolean verifyLogin(String userId, String password) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return false;
		}
		try {
			String sql = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public boolean registerUser(String userId, String password, String firstname, String lastname,
			JSONArray interests) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return false;
		}

		try {

			// step 1: add user into users table
			String sql1 = "INSERT IGNORE INTO users (user_id, password, first_name, last_name) "
					+ "VALUES (?, ?, ?, ?)";

			PreparedStatement ps1 = conn.prepareStatement(sql1);
			ps1.setString(1, userId);
			ps1.setString(2, password);
			ps1.setString(3, firstname);
			ps1.setString(4, lastname);
			if (ps1.executeUpdate() != 1)
				return false;

			// step 2: add interest into interests table
			for (int i = 0; i < interests.length(); i++) {
				String sql2 = "INSERT IGNORE INTO interests (user_id, interest) VALUES (?, ?)";
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ps2.setString(1, userId);
				ps2.setString(2, interests.getString(i));
				if (ps2.executeUpdate() != 1) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public Set<String> getSavedJobs(String userId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return null;
		}

		Set<String> savedJobs = new HashSet<>();

		try {
			String sql = "SELECT item_id FROM saved WHERE user_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String savedJobId = rs.getString("item_id");
				savedJobs.add(savedJobId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return savedJobs;
	}

	public boolean setSavedJob(String userId, String jobId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return false;
		}
		try {
			String sql = "INSERT IGNORE INTO saved (user_id, item_id) VALUES (?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, jobId);
			return ps.executeUpdate() == 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean unSetSavedJob(String userId, String jobId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return false;
		}
		try {
			String sql = "DELETE FROM saved WHERE user_id = ? and item_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, jobId);
			return ps.executeUpdate() == 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public List<String> getInterests(String userId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new ArrayList<>();
		}
		List<String> interests = new ArrayList<>();
		try {
			String sql = "SELECT interest FROM interests WHERE user_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String i = rs.getString("interest");
				interests.add(i);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return interests;
	}
}
