package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

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

	public boolean registerUser(String userId, String password, String firstname, String lastname, JSONArray interests) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return false;
		}

		try {
			String sql = "INSERT IGNORE INTO users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, password);
			ps.setString(3, firstname);
			ps.setString(4, lastname);
			for (int i = 0; i < interests.length(); i++) {
				String currInterest = interests.getJSONObject(i).toString();
				ps.setString(i+5, currInterest);
			}

			return ps.executeUpdate() == 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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

	public void setSavedJob(String userId, Item item) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}
		try {
			String sql = "INSERT IGNORE INTO saved VALUES (?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, item.getId());
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}