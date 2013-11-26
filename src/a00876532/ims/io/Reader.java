/**
 * Project: a00876532Ims
 * File: Reader.java
 * Date: 2013-06-29
 * Time: 7:11:50 PM
 */

package a00876532.ims.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import a00876532.ims.ApplicationException;

/**
 * @author Shawn Norman, A00876532
 * 
 */
public abstract class Reader {

	/**
	 * Parses the information from the inventory string.
	 * 
	 * @param inventory
	 *            The inventory string.
	 * @return An array of Item objects created from the inventory data.
	 * @throws ApplicationException
	 */
	public ArrayList<Object> read(File file, int total) throws ApplicationException {
		ArrayList<Object> items = new ArrayList<Object>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			@SuppressWarnings("unused")
			String contents = scanner.nextLine();
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				String[] attributes = line.split("\\|");
				try {
					checkAttributes(attributes, total);
					items.add(get(attributes));
				} catch (ApplicationException e) {
					throw e;
				}
			}

		} catch (FileNotFoundException e) {
			throw new ApplicationException("Inventory text file cannot be found.");
		} finally {
			scanner.close();
		}

		return items;
	}

	/**
	 * Creates items from the inventory reading.
	 * 
	 * @param itemStrings
	 *            the Strings representing the items.
	 * @return An array of item objects created from the Strings.
	 * @throws ApplicationException
	 */
	public abstract Object get(String[] attributes) throws ApplicationException;

	/**
	 * Checks to make sure there are enough attributes provided to create an item.
	 * 
	 * @param attributes
	 *            An array of Strings representing the attributes.
	 * @throws ApplicationException
	 */
	private void checkAttributes(String[] attributes, int total) throws ApplicationException {
		if (attributes.length < total) {
			throw new ApplicationException("Expected " + total + " elements but got " + attributes.length);
		}
	}

}