/**
 * Project: a00876532Ims
 * File: StockDao.java
 * Date: 2013-04-25
 * Time: 6:35:38 PM
 */

package a00876532.ims.db.dao;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import a00876532.ims.ApplicationException;
import a00876532.ims.Ims;
import a00876532.ims.data.Stock;

/**
 * Used to access Item entries in database.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class StockDao extends Dao {

	static Logger logger = Logger.getLogger(Ims.class);

	public static final String TABLE_NAME = "Stock";
	public static final String STORENUMBER = "STORENUMBER";
	public static final String ITEMNUMBER = "ITEMNUMBER";
	public static final String STOCK = "STOCK";
	public static final String VALUE = "VALUE";

	/**
	 * Constructor for the StockDAO.
	 */
	public StockDao() {
		super(TABLE_NAME);
	}

	/**
	 * Create the table.
	 */
	@Override
	public void create() throws ApplicationException {
		String createStatement = String
				.format("create table %s(%s CHAR(5), %s VARCHAR(10), %s INT, %s INT, PRIMARY KEY (%s, %s), FOREIGN KEY (%s) REFERENCES Stores, FOREIGN KEY (%s) REFERENCES Items)",
						_tableName, //
						STORENUMBER, ITEMNUMBER, STOCK, VALUE, STORENUMBER, ITEMNUMBER, STORENUMBER, ITEMNUMBER);
		try {
			super.create(createStatement);
		} catch (ApplicationException e) {
			throw e;
		}
	}

	/**
	 * Add a stock to the table.
	 * 
	 * @param stock
	 *            The stock to add.
	 * @throws ApplicationException
	 */
	public void add(Stock stock) throws ApplicationException {
		logger.info("Adding stock: " + stock + " to database.");
		Statement statement = null;
		try {
			Connection connection = _database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values('%s', '%s', %d, %d)", _tableName, //
					stock.getStoreNumber(), stock.getItemNumber(), stock.getItemCount(), stock.getValue());
			statement.executeUpdate(insertString);
			logger.info(insertString);
		} catch (ApplicationException e) {
			throw e;
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Could not add item to database.");
		} finally {
			close(statement);
		}
	}

	/**
	 * @param storeNo
	 *            The store number to search for.
	 * @param itemNo
	 *            The item number to search for.
	 * @return A stock from the table.
	 * @throws ApplicationException
	 */
	public Stock getStock(String storeNo, String itemNo) throws ApplicationException {
		Connection connection;
		Statement statement = null;
		Stock stock = null;
		try {
			connection = _database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s'", _tableName, STORENUMBER,
					storeNo, ITEMNUMBER, itemNo);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// get the Item
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new ApplicationException(String.format("Expected one result, got %d", count));
				}
				String storeNumber = resultSet.getString(STORENUMBER);
				String itemNumber = resultSet.getString(ITEMNUMBER);
				int amount = resultSet.getInt(STOCK);
				stock = new Stock(storeNumber, itemNumber, amount);
			}
		} catch (ApplicationException e) {
			throw e;
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Could not get stock from database.");
		} finally {
			close(statement);
		}

		return stock;
	}

	/**
	 * Update a stock entry.
	 * 
	 * @param stock
	 *            The stock to update.
	 * @throws ApplicationException
	 */
	public void update(Stock stock) throws ApplicationException {
		Connection connection;
		Statement statement = null;
		try {
			connection = _database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format(
					"UPDATE %s set %s='%s', %s='%s', %s=%d, %s=%d, WHERE %s='%s' AND %s= '%s'", _tableName, //
					STORENUMBER, stock.getStoreNumber(), //
					ITEMNUMBER, stock.getItemNumber(), //
					STOCK, stock.getItemCount(), //
					VALUE, stock.getValue(), //
					STORENUMBER, stock.getStoreNumber(), //
					ITEMNUMBER, stock.getItemNumber());
			System.out.println(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			logger.info(String.format("Updated %d rows", rowcount));
		} catch (ApplicationException e) {
			throw e;
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Could not update stock database.");
		} finally {
			close(statement);
		}
	}

	/**
	 * Delete a stock entry.
	 * 
	 * @param stock
	 *            The stock to delete.
	 * @throws ApplicationException
	 */
	public void delete(Stock stock) throws ApplicationException {
		Connection connection;
		Statement statement = null;
		try {
			connection = _database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s='%s' AND %s='%s'", _tableName, STORENUMBER,
					stock.getStoreNumber(), ITEMNUMBER, stock.getItemNumber());
			System.out.println(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			System.out.println(String.format("Deleted %d rows", rowcount));
		} catch (ApplicationException e) {
			throw e;
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Could not delete stock from database.");
		} finally {
			close(statement);
		}
	}

	/**
	 * @return A collection of all the stock entries.
	 * @throws ApplicationException
	 */
	public ArrayList<Stock> getAll() throws ApplicationException {
		ArrayList<Stock> results = new ArrayList<Stock>();
		Statement statement = null;
		try {
			Connection connection = _database.getConnection();
			statement = connection.createStatement();
			String query = String.format("SELECT * FROM %s", _tableName);
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				String storeNumber = rs.getString(STORENUMBER);
				String itemNumber = rs.getString(ITEMNUMBER);
				Stock stock = getStock(storeNumber, itemNumber);
				results.add(stock);
			}
		} catch (ApplicationException e) {
			throw e;
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Could not get all stock from database.");
		} finally {
			close(statement);
		}
		return results;
	}

}
