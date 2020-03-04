package db;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;


public class MySQLTableCreation {
	// Run this as Java application to reset db schema.
		public static void main(String[] args) {
			try {
				// Step 1 Connect to MySQL.
				System.out.println("Connecting to " + MySQLDBUtil.URL);
				Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
				Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
				
				if (conn == null) {
					return;
				}
				
				// Step 2 Drop tables in case they exist.
				Statement statement = conn.createStatement();
				
				String sql = "DROP TABLE IF EXISTS users";
				statement.execute(sql);
			
				sql = "DROP TABLE IF EXISTS saved";
				statement.execute(sql);
				
				sql = "DROP TABLE IF EXISTS applied";
				statement.execute(sql);
				
				sql = "DROP TABLE IF EXISTS interests";
				statement.execute(sql);
				
				// Step 3 Create new tables
				
				sql = "CREATE TABLE users ("
						+ "user_id VARCHAR(255) NOT NULL,"
						+ "password VARCHAR(255) NOT NULL,"
						+ "first_name VARCHAR(255),"
						+ "last_name VARCHAR(255),"
						+ "PRIMARY KEY (user_id)"
						+ ")";
				statement.execute(sql);
				
				sql = "CREATE TABLE saved ("
						+ "user_id VARCHAR(255) NOT NULL,"
						+ "item_id VARCHAR(255) NOT NULL,"
						+ "last_favor_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
						+ "PRIMARY KEY (user_id, item_id),"
						+ "FOREIGN KEY (user_id) REFERENCES users(user_id)"
						+ ")";
				statement.execute(sql);
				
				sql = "CREATE TABLE applied ("
						+ "user_id VARCHAR(255) NOT NULL,"
						+ "item_id VARCHAR(255) NOT NULL,"
						+ "last_favor_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
						+ "PRIMARY KEY (user_id),"
						+ "FOREIGN KEY (user_id) REFERENCES users(user_id)"
						+ ")";
				statement.execute(sql);

				sql = "CREATE TABLE interests ("
						+ "user_id VARCHAR(255) NOT NULL,"
						+ "interest VARCHAR(255) NOT NULL,"
						+ "PRIMARY KEY (user_id, interest),"
						+ "FOREIGN KEY (user_id) REFERENCES users(user_id)"
						+ ")";
				statement.execute(sql);

				
				// Step 4: insert fake user 1111/3229c1097c00d497a0fd282d586be050
				sql = "INSERT INTO users VALUES('1111', '3229c1097c00d497a0fd282d586be050', 'John', 'Smith')";
				statement.execute(sql);

				
				conn.close();
				System.out.println("Import done successfully");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

}
