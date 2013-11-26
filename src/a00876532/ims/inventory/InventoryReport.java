/**
 * Project: a00876532Ims
 * File: InventoryReport.java
 * Date: 2013-05-02
 * Time: 1:15:00 PM
 */

package a00876532.ims.inventory;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.log4j.Logger;

import a00876532.ims.ApplicationException;
import a00876532.ims.Ims;
import a00876532.ims.data.Item;
import a00876532.ims.data.Stock;
import a00876532.ims.data.Store;
import a00876532.ims.db.dao.ItemDao;
import a00876532.ims.db.dao.StoreDao;

/**
 * Generates a print out of the items in a company's inventory.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class InventoryReport {

	static Logger logger = Logger.getLogger(Ims.class);

	public static final double DOLLAR = 0.01;
	private ArrayList<Stock> stock;
	String[] args;
	List<String> arguments;
	ItemDao iDao;
	StoreDao sDao;

	/**
	 * Constructor
	 * 
	 * @param stock
	 *            the stock entries in the inventory
	 * @param args
	 *            the run arguments
	 */
	public InventoryReport(ArrayList<Stock> stock, String[] args) {
		logger.debug("InventoryReport()");
		iDao = new ItemDao();
		sDao = new StoreDao();
		this.stock = stock;
		this.args = args;
		arguments = Arrays.asList(args);
	}

	/**
	 * Generates the report.
	 * 
	 * @throws ApplicationException
	 */
	public ArrayList<String[]> getReport() throws ApplicationException {
		logger.debug("printReport()");
		sort();
		ArrayList<String[]> report = new ArrayList<String[]>();
		Item item = null;
		Store store = null;
		for (int i = 0; i < stock.size(); i++) {
			try {
				item = iDao.getItem(stock.get(i).getItemNumber());
				store = sDao.getStore(stock.get(i).getStoreNumber());
			} catch (ApplicationException e) {
				throw e;
			} finally {
				String count = (i + 1) + ".";
				String sku = item.getProductNumber();
				String itemDesc = item.getDescription();
				String price = String.format("%5.2f", (item.getPrice() * DOLLAR));
				String discount = String.format("%5.2f %%", ((item.getDiscount() / DOLLAR)));
				String sale = String.format("%5.2f", (item.getSalePrice() * DOLLAR));
				String storeNo = store.getStoreNumber();
				String storeDesc = store.getDescription();
				String itemCount = Integer.toString(stock.get(i).getItemCount());
				String value = String.format("%8.2f", (stock.get(i).getValue() * DOLLAR));
				String[] entry = { count, sku, itemDesc, price, discount, sale, storeNo, storeDesc, itemCount, value };
				report.add(entry);
			}
		}
		return report;
	}

	/**
	 * Sort the entries based on the arguments.
	 */
	private void sort() {
		logger.debug("sort()");
		if (arguments.contains("by_price")) {
			logger.info("Sorting by price.");
			CompareByPrice comparator = new CompareByPrice();
			Collections.sort(stock, comparator);
		} else if (arguments.contains("by_count")) {
			logger.info("Sorting by item count.");
			CompareByCount comparator = new CompareByCount();
			Collections.sort(stock, comparator);
		} else if (arguments.contains("by_value")) {
			logger.info("Sorting by inventory value.");
			CompareByValue comparator = new CompareByValue();
			Collections.sort(stock, comparator);
		}
		if (arguments.contains("desc")) {
			logger.info("Sorting in descending order.");
			Collections.reverse(stock);
		}
	}
}
