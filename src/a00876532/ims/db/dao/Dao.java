/**
 * Project: a00876532Ims
 * File: Dao.java
 * Date: 2013-04-25
 * Time: 6:35:38 PM
 */

package a00876532.ims.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import a00876532.ims.ApplicationException;
import a00876532.ims.db.Database;

public abstract class Dao {

	protected final Database _database;
	protected final String _tableName;

	/**
	 * Constructor for the DAO.
	 * 
	 * @param tableName
	 *            The name of the table.
	 */
	protected Dao(String tableName) {
		_database = Database.getTheInstance();
		_tableName = tableName;
	}

	/**
	 * Create the table.
	 * 
	 * @throws ApplicationException
	 */
	public abstract void create() throws ApplicationException;

	/**
	 * Create the table with a statement.
	 * 
	 * @param createStatement
	 *            The create statement.
	 * @throws ApplicationException
	 */
	protected void create(String createStatement) throws ApplicationException {
		Statement statement = null;
		try {
			Connection connection = _database.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(createStatement);
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Cannot create database.");
		} finally {
			close(statement);
		}
	}

	/**
	 * Add to the table.
	 * 
	 * @param updateStatement
	 *            The update statement.
	 * @throws ApplicationException
	 */
	protected void add(String updateStatement) throws ApplicationException {
		Statement statement = null;
		try {
			Connection connection = _database.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(updateStatement);
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Cannot add to database.");
		} finally {
			close(statement);
		}
	}

	/**
	 * Drop the table.
	 * 
	 * @throws ApplicationException
	 */
	public void drop() throws ApplicationException {
		Statement statement = null;
		try {
			Connection connection = _database.getConnection();
			statement = connection.createStatement();
			if (_database.tableExists(_tableName)) {
				statement.executeUpdate("drop table " + _tableName);
			}
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Cannot drop database.");
		} finally {
			close(statement);
		}
	}

	/**
	 * Close the statement.
	 * 
	 * @param statement
	 *            The statement too close.
	 * @throws ApplicationException
	 */
	protected void close(Statement statement) throws ApplicationException {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			throw new ApplicationException("SQLException. Cannot close connection.");
		}
	}

}
