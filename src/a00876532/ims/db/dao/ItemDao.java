/**
 * Project: a00876532Ims
 * File: ItemDao.java
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
import a00876532.ims.data.Item;

/**
 * Used to access Item entries in database.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class ItemDao extends Dao {

	static Logger logger = Logger.getLogger(Ims.class);

	public static final String TABLE_NAME = "Items";
	public static final String ITEMNUMBER = "ITEMNUMBER";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String PRICE = "PRICE";
	public static final String DISCOUNT = "DISCOUNT";
	public static final String SALEPRICE = "SALEPRICE";
	public static final String IMAGENAME = "IMAGENAME";

	/**
	 * Constructor for the ItemDAO.
	 */
	public ItemDao() {
		super(TABLE_NAME);
	}

	/**
	 * Create the item table.
	 */
	@Override
	public void create() throws ApplicationException {
		String createStatement = String
				.format("create table %s(%s VARCHAR(10), %s VARCHAR(50), %s INT, %s DECIMAL(6,2), %s DECIMAL(8,2), %s VARCHAR(20), primary key (%s) )",
						_tableName, //
						ITEMNUMBER, DESCRIPTION, PRICE, DISCOUNT, SALEPRICE, IMAGENAME, ITEMNUMBER);
		super.create(createStatement);
	}

	/**
	 * Add an item to the database.
	 * 
	 * @param item
	 *            The item to add
	 * @throws ApplicationException
	 */
	public void add(Item item) throws ApplicationException {
		logger.info("Adding item: " + item + " to database.");
		Statement statement = null;
		try {
			Connection connection = _database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values('%s', '%s', %d, %f, %f, '%s')",
					_tableName, //
					(item.getProductNumber()), (item.getDescription()), (item.getPrice()), (item.getDiscount()),
					(item.getSalePrice()), (item.get_imageName()));
			statement.executeUpdate(insertString);
			logger.info(insertString);
		} catch (Exception e) {
			throw new ApplicationException("Could not add item to database.");
		} finally {
			close(statement);
		}
	}

	/**
	 * @param productNumber
	 *            The product number to search for.
	 * @return Item from the database.
	 * @throws ApplicationException
	 */
	public Item getItem(String productNumber) throws ApplicationException {
		Connection connection;
		Statement statement = null;
		Item item = null;
		try {
			connection = _database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s = '%s'", _tableName, ITEMNUMBER, productNumber);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// get the Item
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new ApplicationException(String.format("Expected one result, got %d", count));
				}
				String number = resultSet.getString(ITEMNUMBER);
				String desc = resultSet.getString(DESCRIPTION);
				int price = resultSet.getInt(PRICE);
				double discount = resultSet.getFloat(DISCOUNT);
				String image = resultSet.getString(IMAGENAME);
				item = new Item(desc, number, price, discount, image);

			}
		} catch (Exception e) {
			throw new ApplicationException("Could not get Item from database.");
		} finally {
			close(statement);
		}

		return item;
	}

	/**
	 * Update an item in the database.
	 * 
	 * @param item
	 *            The item to update.
	 * @throws ApplicationException
	 */
	public void update(Item item) throws ApplicationException {
		Connection connection;
		Statement statement = null;
		try {
			connection = _database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format(
					"UPDATE %s set %s='%s', %s='%s', %s=%d, %s=%f, %s=%f, %s='%s' WHERE %s='%s'", _tableName, //
					ITEMNUMBER, item.getProductNumber(), //
					DESCRIPTION, item.getDescription(), //
					PRICE, item.getPrice(), //
					DISCOUNT, item.getDiscount(), //
					SALEPRICE, item.getSalePrice(), //
					IMAGENAME, item.get_imageName(), //
					ITEMNUMBER, item.getProductNumber());
			System.out.println(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			logger.info(String.format("Updated %d rows", rowcount));
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Could not update database.");
		} finally {
			close(statement);
		}
	}

	/**
	 * Delete an item from the database.
	 * 
	 * @param item
	 *            The item to delete.
	 * @throws ApplicationException
	 */
	public void delete(Item item) throws ApplicationException {
		Connection connection;
		Statement statement = null;
		try {
			connection = _database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s='%s'", _tableName, ITEMNUMBER,
					item.getProductNumber());
			System.out.println(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			System.out.println(String.format("Deleted %d rows", rowcount));
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException("Could not delete item from database.");
		} finally {
			close(statement);
		}
	}

	/**
	 * @return A collection of all the items in the database.
	 * @throws ApplicationException
	 */
	public HashMap<String, Item> getAll() throws ApplicationException {
		HashMap<String, Item> results = new HashMap<String, Item>();
		Statement statement = null;
		try {
			Connection connection = _database.getConnection();
			statement = connection.createStatement();
			String query = String.format("SELECT * FROM %s", _tableName);
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				String number = rs.getString(ITEMNUMBER);
				Item item = getItem(number);
				results.put(number, item);
			}
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException("Could not get all items from database.");
		} finally {
			close(statement);
		}
		return results;
	}

}
