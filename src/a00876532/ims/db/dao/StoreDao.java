/**
 * Project: a00876532Ims
 * File: StoreDao.java
 * Date: 2013-04-25
 * Time: 6:35:38 PM
 */

package a00876532.ims.db.dao;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import a00876532.ims.ApplicationException;
import a00876532.ims.Ims;
import a00876532.ims.data.Store;

/**
 * Used to access Item entries in database.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class StoreDao extends Dao {

	static Logger logger = Logger.getLogger(Ims.class);

	public static final String TABLE_NAME = "Stores";
	public static final String STORENUMBER = "STORENUMBER";
	public static final String NAME = "NAME";
	public static final String STREET = "STREET";
	public static final String CITY = "CITY";
	public static final String PROVINCE = "PROVINCE";
	public static final String POSTALCODE = "POSTALCODE";
	public static final String STOREPHONE = "STOREPHONE";
	public static final String SERVICEPHONE = "SERVICEPHONE";

	/**
	 * Constructor for the StoreDAO.
	 */
	public StoreDao() {
		super(TABLE_NAME);
	}

	/**
	 * Create the table.
	 */
	@Override
	public void create() throws ApplicationException {
		String createStatement = String
				.format("create table %s(%s CHAR(5), %s VARCHAR(20), %s VARCHAR(25), %s VARCHAR(15), %s CHAR(2), %s CHAR(7), %s CHAR(12), %s CHAR(12), primary key (%s) )",
						_tableName, //
						STORENUMBER, NAME, STREET, CITY, PROVINCE, POSTALCODE, STOREPHONE, SERVICEPHONE, STORENUMBER);
		try {
			super.create(createStatement);
		} catch (ApplicationException e) {
			throw e;
		}
	}

	/**
	 * Add a store to the table.
	 * 
	 * @param store
	 *            The store to add.
	 * @throws ApplicationException
	 */
	public void add(Store store) throws ApplicationException {
		logger.info("Adding store: " + store + " to database.");
		Statement statement = null;
		try {
			Connection connection = _database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format(
					"insert into %s values('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
					_tableName, //
					store.getStoreNumber(), store.getDescription(), store.getStreet(), store.getCity(),
					store.getProvince(), store.getPostalCode(), store.getStorePhone(), store.getServicePhone());
			statement.executeUpdate(insertString);
			logger.info(insertString);
		} catch (ApplicationException e) {
			throw e;
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Could not add store to database.");
		} finally {
			close(statement);
		}
	}

	/**
	 * @param storeNumber
	 *            The store number to search for.
	 * @return A store from the table.
	 * @throws ApplicationException
	 */
	public Store getStore(String storeNumber) throws ApplicationException {
		Connection connection;
		Statement statement = null;
		Store store = null;
		try {
			connection = _database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s = '%s'", _tableName, STORENUMBER, storeNumber);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// get the Item
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new ApplicationException(String.format("Expected one result, got %d", count));
				}
				String number = resultSet.getString(STORENUMBER);
				String desc = resultSet.getString(NAME);
				String street = resultSet.getString(STREET);
				String city = resultSet.getString(CITY);
				String province = resultSet.getString(PROVINCE);
				String postal = resultSet.getString(POSTALCODE);
				String storePhone = resultSet.getString(STOREPHONE);
				String servicePhone = resultSet.getString(SERVICEPHONE);
				store = new Store(number, desc, street, city, province, postal, storePhone, servicePhone);
			}
		} catch (ApplicationException e) {
			throw e;
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Could not get store from database.");
		} finally {
			close(statement);
		}

		return store;
	}

	/**
	 * Update a store on the table.
	 * 
	 * @param store
	 *            The store to update.
	 * @throws ApplicationException
	 */
	public void update(Store store) throws ApplicationException {
		Connection connection;
		Statement statement = null;
		try {
			connection = _database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String
					.format("UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'",
							_tableName, //
							STORENUMBER, store.getStoreNumber(), //
							NAME, store.getDescription(), //
							STREET, store.getStreet(), //
							CITY, store.getCity(), //
							PROVINCE, store.getProvince(), //
							POSTALCODE, store.getPostalCode(), //
							STOREPHONE, store.getStorePhone(), //
							SERVICEPHONE, store.getServicePhone(), //
							STORENUMBER, store.getStoreNumber());
			System.out.println(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			logger.info(String.format("Updated %d rows", rowcount));
		} catch (ApplicationException e) {
			throw e;
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Could not update store database.");
		} finally {
			close(statement);
		}
	}

	/**
	 * Delete a store from the table.
	 * 
	 * @param store
	 *            The store to delete.
	 * @throws ApplicationException
	 */
	public void delete(Store store) throws ApplicationException {
		Connection connection;
		Statement statement = null;
		try {
			connection = _database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s='%s'", _tableName, STORENUMBER,
					store.getStoreNumber());
			System.out.println(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			System.out.println(String.format("Deleted %d rows", rowcount));
		} catch (ApplicationException e) {
			throw e;
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Could not delete store from database.");
		} finally {
			close(statement);
		}
	}

	/**
	 * @return A collection of all the stores on the table.
	 * @throws ApplicationException
	 */
	public HashMap<String, Store> getAll() throws ApplicationException {
		HashMap<String, Store> results = new HashMap<String, Store>();
		Statement statement = null;
		try {
			Connection connection = _database.getConnection();
			statement = connection.createStatement();
			String query = String.format("SELECT * FROM %s", _tableName);
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				String number = rs.getString(STORENUMBER);
				Store store = getStore(number);
				results.put(number, store);
			}
		} catch (ApplicationException e) {
			throw e;
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Could not get all stores from database.");
		} finally {
			close(statement);
		}
		return results;
	}

}
