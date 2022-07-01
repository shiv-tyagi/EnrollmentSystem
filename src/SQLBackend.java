import java.sql.*;
import java.util.*;
import java.io.*;

public class SQLBackend {
	Connection connection;
	private String database;
	public SQLBackend() {
		this.tryConnection(null);
	}
	
	public SQLBackend(String customDBName) {
		this.tryConnection(customDBName);
	}
	
	public boolean tryConnection(String dbname) {
		ServerCredentials server = new ServerCredentials(); 
		String username = server.settings.get("username");
		String password = server.settings.get("password");
		String host = server.settings.get("host");
		String port = server.settings.get("port");
		if(dbname==null)
			dbname = server.settings.get("database");
		String url = "jdbc:mysql://"+host+":"+port;
		try {
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Successfully connected to SQL server!!");
		} catch (SQLException e) {
			System.out.println("Error connecting to db. Check your credentials and make sure a mysql server is running on your system.");
			return false;
		}
		return true;	
	}
	
	public void closeConnection() {
		try {
			connection.close();
			System.out.println("Closed!");
			
		} catch (SQLException e) {
			System.out.println("Error closing the connection!");
		}
		
	}
	
	public boolean checkDB(String databaseName) {
		ResultSet results;
		try {
			results = connection.getMetaData().getCatalogs();
			while(results.next()) {
				if(results.getString(1).equals(databaseName)) {
					System.out.println("Great! "+databaseName+" db alreay exists. No need to create one.");
					return true;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error searching the database");
			return false;
		}
		System.out.println("DB not found!");
		return false;
	}
	
	public boolean createDatabase(String databaseName) {
		try {
			connection.createStatement().executeUpdate(
					"CREATE database "+databaseName
					);
		} catch (SQLException e) {
			System.out.println("Could not create database named "+databaseName);
			return false;
		}
		System.out.println("Successfully created the database named "+databaseName);
		return this.setDatabase(databaseName);
	}
	
	public boolean checkTable(String tableName, String database) {
		if(database==null)
			database = this.getDatabase();
		String tableQuery = "SELECT count(*) as tableCount FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = '"+this.getDatabase()+"' and TABLE_NAME='"+tableName+"'";
		try {
			ResultSet results = connection.createStatement().executeQuery(tableQuery);
			if(results.next() && results.getInt("tableCount")!=0) {
				System.out.println("'"+tableName+"' table exists!");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error searching the table");
			return false;
		}
		System.out.println("'"+tableName+"' Table not found!");
		return false;
	}
	
	public boolean checkTable(String tableName) {
		return checkTable(tableName, null);
	}
	
	public boolean createTable(Table table) {
		try {
			System.out.println("Creating table...");
			String query = "CREATE table "+table.name+" (";
			if(!table.attributes.isBlank()) query = query.concat(table.attributes);
			else 
				return false;
			if(!table.constraints.isBlank()) query = query.concat(", "+table.constraints);
			if(!table.foreignKeys.isBlank()) query = query.concat(", "+table.foreignKeys);
			query = query.concat(")");
			connection.createStatement().executeUpdate(query);
			
		} catch (SQLException e) {
			System.out.println("Failed to create table. Check raw definition carefully.");
			return false;
		}
		
		System.out.println("Successfully created table '"+table.name+"'!");
		return true;
	}

	String getDatabase() {
		return database;
	}

	public boolean setDatabase(String database) {
		try {
			connection.createStatement().executeUpdate("use "+database);
			this.database=database;
		} catch (SQLException e) {
			System.out.println("PANIC!! Could not set database. ");
			System.exit(1);
		}
		return true;
	}
	
	public boolean checkTables() {
		boolean success=true;
		// 	This  array contains the definitions of tables needed to run this program successfully
		// 	Format of definitions
		//	tableName-columns-constraints-foreign_keys
		String defs[] = {
				"students-rollNo varchar(10) primary key, name varchar(50) not null, year_of_admission DECIMAL(4,0) not null, programme varchar(50) not null, branch varchar(50) not null, dob date not null, email varchar(150), phone varchar(15),totalCredits int DEFAULT 0 - constraint check_branch check(branch in('CSE','CCE','MECH','ECE')), constraint check_programme check(programme in('BTECH','MTECH', 'MTECHDUAL')) - -",
				"departments-deptID int primary key auto_increment, name varchar(50), building varchar(50)- - -",
				"courses-courseID int primary key auto_increment, name varchar(50), credits int, deptID int- -foreign key (deptID) references departments(deptID) ON DELETE CASCADE ON UPDATE CASCADE -",
				"instructors-instructorID int primary key auto_increment, name varchar(20), phone varchar(50) not null, deptID int not null- -foreign key (deptID) references departments(deptID) ON DELETE CASCADE ON UPDATE CASCADE -",
				"enrollments-rollNo varchar(10) not null, courseID int not null- primary key (rollNo, courseID) -foreign key (rollNo) references students(rollNo) ON DELETE CASCADE ON UPDATE CASCADE, foreign key (courseID) references courses(courseID) ON DELETE CASCADE ON UPDATE CASCADE -",
				"duties-instructorID int not null, courseID int not null- primary key (instructorID, courseID) -foreign key (instructorID) references instructors(instructorID) ON DELETE CASCADE ON UPDATE CASCADE, foreign key (courseID) references courses(courseID) ON DELETE CASCADE ON UPDATE CASCADE -"
		};

		for(String line: defs) {
			if(line.contains("eof"))
				break;
			Table table = new Table(line);
			if(!this.checkTable(table.name)) {
				success=this.createTable(table);
			}
		}
		
		if(!success)
			System.exit(1);
		
		System.out.println("Hooray! Table setup done!!");
		return false;
	}

	// Method to execute queries that dont return rows
	public boolean executeSimpleQuery(String query) {
		if(database==null)
			database = this.getDatabase();

		try {
			connection.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
