/**
 * Project: a00876532Lab9
 * File: Ims.java
 * Date: 2013-04-25
 * Time: 6:35:38 PM
 */

package a00876532.ims;

import a00876532.ims.data.Item;
import a00876532.ims.data.Stock;
import a00876532.ims.data.Store;
import a00876532.ims.db.Database;
import a00876532.ims.db.dao.ItemDao;
import a00876532.ims.db.dao.StockDao;
import a00876532.ims.db.dao.StoreDao;
import a00876532.ims.io.ItemReader;
import a00876532.ims.io.StockReader;
import a00876532.ims.io.StoreReader;
import a00876532.ims.ui.MainFrame;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Main class of Ims. Takes a string that represents a stores inventory and uses other classes to parse and display the
 * items in the inventory.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class Ims {

	static Logger logger = Logger.getLogger(Ims.class);

	private static Database database;
	private static ItemDao itemDao;
	private static StockDao stockDao;
	private static StoreDao storeDao;
	private static ArrayList<Object> items;
	private static ArrayList<Object> stores;
	private static ArrayList<Object> stock;
	private ItemReader itemReader;
	private StoreReader storeReader;
	private StockReader stockReader;

	/**
	 * Constructor for Ims
	 * 
	 * @param itemCount
	 *            the number of elements in the items array
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Ims(File itemsFile, File storesFile, File stockFile) throws FileNotFoundException, IOException {
		logger.debug("Ims()");

		items = new ArrayList<Object>();
		stores = new ArrayList<Object>();
		stock = new ArrayList<Object>();

		itemReader = new ItemReader();
		storeReader = new StoreReader();
		stockReader = new StockReader();

		database = Database.getTheInstance();

		itemDao = new ItemDao();
		storeDao = new StoreDao();
		stockDao = new StockDao();

		try {
			database.getConnection();
		} catch (ApplicationException e) {
			displayError(e);
		}
		try {
			items = itemReader.read(itemsFile);
			stores = storeReader.read(storesFile);
		} catch (ApplicationException e) {
			displayError(e);
			return;
		}

		populateDatabase();

		try {
			stock = stockReader.read(stockFile);
		} catch (ApplicationException e) {
			displayError(e);
		}

		populateStockDb();

	}

	private void displayError(ApplicationException e) {
		logger.error(e.getMessage());
		try {
			JOptionPane.showMessageDialog(new MainFrame(), e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e1) {

		}
	}

	/**
	 * @param args
	 * @throws ApplicationException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ApplicationException, IOException {
		PropertyConfigurator.configure("log.properties");
		logger.debug("main()");
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getInstance();
		logger.info(dateFormat.format(date));

		File itemsFile = new File("items.txt");
		if (!itemsFile.exists()) {
			logger.error("items.txt not found");
		}
		File storesFile = new File("stores.txt");
		if (!storesFile.exists()) {
			logger.error("stores.txt not found");
		}
		File stockFile = new File("stock.txt");
		if (!stockFile.exists()) {
			logger.error("stock.txt not found");
		}

		new Ims(itemsFile, storesFile, stockFile);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (Exception e) {
					// If Nimbus is not available, use the default.
				}
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					logger.debug("Could not launch application.");
					e.printStackTrace();
				}
			}
		});

	}

	private void populateDatabase() {
		try {
			if (!database.tableExists("Items")) {
				itemDao.create();
				for (Object i : items) {
					itemDao.add((Item) i);
				}
			}
			if (!database.tableExists("Stores")) {
				storeDao.create();
				for (Object i : stores) {
					storeDao.add((Store) i);
				}
			}
		} catch (ApplicationException e) {
			displayError(e);
		}

	}

	private void populateStockDb() {
		try {
			if (!database.tableExists("Stock")) {
				stockDao.create();
				for (Object i : stock) {
					stockDao.add((Stock) i);
				}
			}
		} catch (ApplicationException e) {
			displayError(e);
		}
	}

}
