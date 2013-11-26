/**
 * Project: a00876532Ims
 * File: StoreReader.java
 * Date: 2013-05-02
 * Time: 1:15:15 PM
 */

package a00876532.ims.io;

import java.io.File;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import a00876532.ims.ApplicationException;
import a00876532.ims.Ims;
import a00876532.ims.data.Store;

/**
 * Reads stores.txt and creates Store objects from the data.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class StoreReader extends Reader {

	static Logger logger = Logger.getLogger(Ims.class);

	public static final int STORE_NO = 0;
	public static final int STORE_NAME = 1;
	public static final int STREET = 2;
	public static final int CITY = 3;
	public static final int PROVINCE = 4;
	public static final int POSTAL_CODE = 5;
	public static final int STORE_PHONE = 6;
	public static final int SERVICE_PHONE = 7;
	public static final int STORE_ATTRIBUTES = 8;

	/**
	 * Constructor
	 */
	public StoreReader() {
		logger.debug("StoreReader()");
	}

	public ArrayList<Object> read(File itemsFile) throws ApplicationException {
		try {
			return super.read(itemsFile, STORE_ATTRIBUTES);
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
	public Store get(String[] attributes) throws ApplicationException {
		try {
			return new Store(attributes[STORE_NO].trim(), attributes[STORE_NAME].trim(), attributes[STREET].trim(),
					attributes[CITY].trim(), attributes[PROVINCE].trim(), attributes[POSTAL_CODE].trim(),
					attributes[STORE_PHONE].trim(), attributes[SERVICE_PHONE].trim());
		} catch (NumberFormatException e) {
			throw new ApplicationException(e.getMessage());
		}
	}

}
