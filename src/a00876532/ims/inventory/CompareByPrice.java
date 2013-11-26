/**
 * Project: a00876532Ims
 * File: CompareByPrice.java
 * Date: 2013-05-11
 * Time: 3:54:38 PM
 */

package a00876532.ims.inventory;

import java.util.Comparator;

import a00876532.ims.data.Stock;

/**
 * Provides the code to allow items to be sorted by price.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class CompareByPrice implements Comparator<Stock> {

	@Override
	/**
	 * Compare two items by price.
	 * @param price1 The first price
	 * @param price2 The second price
	 * @return The difference.
	 */
	public int compare(Stock stock1, Stock stock2) {
		return stock1.getPrice() - stock2.getPrice();
	}

}
