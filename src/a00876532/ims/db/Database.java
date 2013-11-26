/**
 * Project: A00876532Ims
 * File: Database.java
 * Date: 2012-10-28
 * Time: 12:26:04 PM
 */

package a00876532.ims.db;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import a00876532.ims.ApplicationException;

/**
 * @author scirka
 * 
 */
public class Database {

	public static final String DB_PROPERTIES_FILENAME = "db.properties";
	public static final String DB_DRIVER_KEY = "db.driver";
	public static final String DB_URL_KEY = "db.url";
	public static final String DB_USER_KEY = "db.user";
	public static final String DB_PASSWORD_KEY = "db.password";

	private static Logger LOG = Logger.getLogger(Database.class.getName());

	private static Connection connection;
	private final Properties _properties;
	private static Database theInstance = new Database();

	/**
	 * Singleton constructor for the database.
	 */
	private Database() {
		LOG.debug("Loading database properties from db.properties");

		File dbPropertiesFile = new File(DB_PROPERTIES_FILENAME);
		if (!dbPropertiesFile.exists()) {
			LOG.error("Database properties file not found.");
			System.exit(-1);
		}
		_properties = new Properties();
		try {
			_properties.load(new FileReader(dbPropertiesFile));
		} catch (Exception e) {
			LOG.error("Could not read database properties file.");
		}

	}

	/**
	 * @return The Database instance.
	 */
	public static Database getTheInstance() {
		return theInstance;
	}

	/**
	 * @return the database connection.
	 * @throws ApplicationException
	 */
	public Connection getConnection() throws ApplicationException {
		if (connection != null) {
			return connection;
		}

		try {
			connect();
		} catch (Exception e) {
			throw new ApplicationException("Could not get connection.");
		}

		return connection;
	}

	/**
	 * Connect to the database.
	 * 
	 * @throws ApplicationException
	 */
	private void connect() throws ApplicationException {

		try {
			Class.forName(_properties.getProperty(DB_DRIVER_KEY));
		} catch (ClassNotFoundException e) {
			throw new ApplicationException("Could not load database properties.");
		}
		LOG.info("Driver loaded");
		try {
			connection = DriverManager.getConnection(_properties.getProperty(DB_URL_KEY),
					_properties.getProperty(DB_USER_KEY), _properties.getProperty(DB_PASSWORD_KEY));
		} catch (SQLException e) {
			throw new ApplicationException("Could not connect to database.");
		}
		LOG.info("Database connected");
	}

	/**
	 * Shutdown the database.
	 * 
	 * @throws ApplicationException
	 */
	public void shutdown() throws ApplicationException {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				throw new ApplicationException("Could not shutdown database.");
			}
		}
	}

	/**
	 * @param tableName
	 *            The table name to check.
	 * @return if the table exists of not.
	 * @throws ApplicationException
	 */
	public boolean tableExists(String tableName) throws ApplicationException {
		DatabaseMetaData databaseMetaData;
		try {
			databaseMetaData = connection.getMetaData();
		} catch (SQLException e1) {
			throw new ApplicationException("Could not get connection data.");
		}
		ResultSet resultSet = null;
		String rsTableName = null;

		try {
			resultSet = databaseMetaData.getTables(connection.getCatalog(), "%", "%", null);
			while (resultSet.next()) {
				rsTableName = resultSet.getString("TABLE_NAME");
				if (rsTableName.equalsIgnoreCase(tableName)) {
					return true;
				}
			}
		} catch (SQLException e) {
			throw new ApplicationException("Could not get table name.");
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new ApplicationException("Could not close result set.");
			}
		}

		return false;
	}

}
