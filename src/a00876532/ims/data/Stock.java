/**
 * Project: a00876532Ims
 * File: Stock.java
 * Date: 2013-05-22
 * Time: 10:41:24 PM
 */

package a00876532.ims.data;

import org.apache.log4j.Logger;

import a00876532.ims.Ims;
import a00876532.ims.db.dao.ItemDao;

/**
 * Represents an stock entry in an inventory.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class Stock {

	static Logger logger = Logger.getLogger(Ims.class);

	private String itemNumber;
	private String storeNumber;
	private int itemCount;
	private int value;

	/**
	 * @param itemNumber
	 * @param storeNumber
	 * @param itemCount
	 */
	public Stock(String storeNumber, String itemNumber, int itemCount) {
		this.itemNumber = itemNumber;
		this.storeNumber = storeNumber;
		this.itemCount = itemCount;
		setValue();
	}

	/**
	 * @return the itemNumber
	 */
	public String getItemNumber() {
		return itemNumber;
	}

	/**
	 * @return the storeNumber
	 */
	public String getStoreNumber() {
		return storeNumber;
	}

	/**
	 * @return the itemCount
	 */
	public int getItemCount() {
		return itemCount;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return the price of the Item referenced by the Stock entry
	 */
	public int getPrice() {
		ItemDao dao = new ItemDao();
		Item item = null;
		try {
			item = dao.getItem(itemNumber);
		} catch (Exception e) {
			logger.debug("Could not access Items database.");
		}
		return item.getPrice();

	}

	/**
	 * @param itemNumber
	 *            the itemNumber to set
	 */
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	/**
	 * @param storeNumber
	 *            the storeNumber to set
	 */
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	/**
	 * @param itemCount
	 *            the itemCount to set
	 */
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	/**
	 * Sets the value of an stock entry.
	 */
	public void setValue() {
		try {
			value = getPrice() * itemCount;
		} catch (Exception e) {
			logger.debug("Could not access Items database.");
		}
	}

	/**
	 * Sets the value with an outside value.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Stock [storeNumber=" + storeNumber + ", itemNumber=" + itemNumber + ", itemCount=" + itemCount + "]";
	}

}