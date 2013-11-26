/**
 * Project: a00876532Ims
 * File: StockReader.java
 * Date: 2013-05-02
 * Time: 1:15:15 PM
 */

package a00876532.ims.io;

import java.io.File;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import a00876532.ims.ApplicationException;
import a00876532.ims.Ims;
import a00876532.ims.data.Stock;

/**
 * Reads stock.txt and creates stock entries.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class StockReader extends Reader {

	static Logger logger = Logger.getLogger(Ims.class);

	public static final int STORE_NO = 0;
	public static final int ITEM_NO = 1;
	public static final int STOCK = 2;
	public static final int STOCK_ATTRIBUTES = 3;

	/**
	 * Constructor
	 */
	public StockReader() {
		logger.debug("StockReader()");
	}

	/**
	 * Parses the information from the inventory string.
	 * 
	 * @param inventory
	 *            The inventory string.
	 * @return An array of Item objects created from the inventory data.
	 * @throws ApplicationException
	 */
	public ArrayList<Object> read(File stockFile) throws ApplicationException {
		try {
			return super.read(stockFile, STOCK_ATTRIBUTES);
		} catch (ApplicationException e) {
			throw e;
		}
	}

	/**
	 * Creates items from the inventory reading.
	 * 
	 * @param itemStrings
	 *            the Strings representing the items.
	 * @return An array of item objects created from the Strings.
	 * @throws ApplicationException
	 */
	@Override
	public Stock get(String[] attributes) throws ApplicationException {
		try {
			return new Stock(attributes[STORE_NO].trim(), attributes[ITEM_NO].trim(), Integer.valueOf(attributes[STOCK]
					.trim()));
		} catch (NumberFormatException e) {
			throw new ApplicationException(e.getMessage());
		}
	}

}