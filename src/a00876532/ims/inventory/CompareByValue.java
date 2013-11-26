/**
 * Project: a00876532Ims
 * File: CompareByValue.java
 * Date: 2013-05-11
 * Time: 3:54:38 PM
 */

package a00876532.ims.inventory;

import java.util.Comparator;

import a00876532.ims.data.Stock;

/**
 * Provides the code to allow items to be sorted by inventory value.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class CompareByValue implements Comparator<Stock> {

	@Override
	/**
	 * Compare two stock entries by value.
	 * @param stock1 The first stock
	 * @param stock2 The second stock
	 * @return The difference.
	 */
	public int compare(Stock stock1, Stock stock2) {
		return stock1.getValue() - stock2.getValue();
	}

}
