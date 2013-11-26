/**
 * Project: a00876532Ims
 * File: Item.java
 * Date: 2013-04-25
 * Time: 9:55:24 PM
 */

package a00876532.ims.data;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import a00876532.ims.Ims;

/**
 * Represents an item at a store.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class Item {

	static Logger logger = Logger.getLogger(Ims.class);

	public static final int DOLLAR = 100;
	private String description;
	private String productNumber;
	private int price;
	private double discount;
	private double salePrice;
	private String _imageName;

	/**
	 * Constructor
	 * 
	 * @param description
	 *            The description of the item.
	 * @param productNumber
	 *            The item's product number.
	 * @param price
	 *            The price of the item in dollars.
	 * @param discount
	 *            The percent of the price to discount.
	 * @param _imageName
	 *            The name of the image file.
	 */
	public Item(String description, String productNumber, int price, double discount, String _imageName) {
		logger.debug("Item(" + description + ")");
		this.description = description;
		this.productNumber = productNumber;
		this.price = price;
		setDiscount(discount);
		this.salePrice = setSalePrice();
		this._imageName = _imageName;
	}

	public Item() {

	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the productNumber
	 */
	public String getProductNumber() {
		return productNumber;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @return the discount
	 */
	public double getDiscount() {
		return discount;
	}

	/**
	 * @return the salePrice
	 */
	public double getSalePrice() {
		return salePrice;
	}

	/**
	 * @return the _imageName
	 */
	public String get_imageName() {
		return _imageName;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param productNumber
	 *            the productNumber to set
	 */
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(double discount) {
		DecimalFormat df = new DecimalFormat("###.##");
		this.discount = Double.valueOf(df.format(discount));
	}

	/**
	 * Set the sale price.
	 */
	public double setSalePrice() {
		double salePrice = price - (price * discount);
		return salePrice;
	}

	/**
	 * @param _imageName
	 *            the _imageName to set
	 */
	public void set_imageName(String _imageName) {
		this._imageName = _imageName;
	}

	@Override
	public String toString() {
		return "Item [description=" + description + ", productNumber=" + productNumber + ", price=" + price
				+ ", discount =" + discount + ", salePrice =" + salePrice + ", _imageName=" + _imageName + "]";
	}

}
