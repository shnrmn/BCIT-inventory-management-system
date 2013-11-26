/**
 * Project: a00876532Ims
 * File: ItemReader.java
 * Date: 2013-05-02
 * Time: 1:15:15 PM
 */

package a00876532.ims.io;

import java.io.File;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import a00876532.ims.ApplicationException;
import a00876532.ims.Ims;
import a00876532.ims.data.Item;

/**
 * Reads an items.txt file and creates Item objects from the data.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class ItemReader extends Reader {

	static Logger logger = Logger.getLogger(Ims.class);

	public static final int NAME = 0;
	public static final int PRODUCT_NUMBER = 1;
	public static final int PRICE = 2;
	public static final int DISCOUNT = 3;
	public static final int IMAGE = 4;
	public static final int ITEM_ATTRIBUTES = 5;

	/**
	 * Constructor
	 */
	public ItemReader() {
		logger.debug("ItemReader()");
	}

	/**
	 * Parses the information from the inventory string.
	 * 
	 * @param inventory
	 *            The inventory string.
	 * @return An array of Item objects created from the inventory data.
	 * @throws ApplicationException
	 */
	public ArrayList<Object> read(File itemsFile) throws ApplicationException {
		try {
			return super.read(itemsFile, ITEM_ATTRIBUTES);
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
	public Item get(String[] attributes) throws ApplicationException {
		try {
			return new Item(attributes[NAME].trim(), attributes[PRODUCT_NUMBER].trim(),
					Integer.valueOf(attributes[PRICE].trim()), Float.valueOf(attributes[DISCOUNT].trim()),
					attributes[IMAGE].trim());
		} catch (NumberFormatException e) {
			throw new ApplicationException(e.getMessage());
		}
	}

}
