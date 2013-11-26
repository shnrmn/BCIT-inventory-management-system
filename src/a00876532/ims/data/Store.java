/**
 * Project: a00876532Ims
 * File: Store.java
 * Date: 2013-05-21
 * Time: 9:01:24 PM
 */

package a00876532.ims.data;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import a00876532.ims.Ims;

/**
 * Represents a store.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
public class Store {

	static Logger logger = Logger.getLogger(Ims.class);

	private String storeNumber;
	private String description;
	private String street;
	private String city;
	private String province;
	private String postalCode;
	private String storePhone;
	private String servicePhone;

	/**
	 * @param storeNumber
	 * @param description
	 * @param street
	 * @param province
	 * @param city
	 * @param postalCode
	 * @param storePhone
	 * @param servicePhone
	 */
	public Store(String storeNumber, String description, String street, String city, String province,
			String postalCode, String storePhone, String servicePhone) {
		PropertyConfigurator.configure("log.properties");
		logger.debug("Store(" + description + ")");
		this.storeNumber = storeNumber;
		this.description = description;
		this.street = street;
		this.city = city;
		this.province = province;
		this.postalCode = postalCode;
		this.storePhone = storePhone;
		this.servicePhone = servicePhone;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the storeNumber
	 */
	public String getStoreNumber() {
		return storeNumber;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @return the storePhone
	 */
	public String getStorePhone() {
		return storePhone;
	}

	/**
	 * @return the servicePhone
	 */
	public String getServicePhone() {
		return servicePhone;
	}

	/**
	 * @param storeNumber
	 *            the storeNumber to set
	 */
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @param postalCode
	 *            the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @param storePhone
	 *            the storePhone to set
	 */
	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}

	/**
	 * @param servicePhone
	 *            the servicePhone to set
	 */
	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Store [storeNumber=" + storeNumber + ", description=" + description + ", street=" + street + ", city="
				+ city + ", province=" + province + ", postalCode=" + postalCode + ", storePhone=" + storePhone
				+ ", servicePhone=" + servicePhone + "]";
	}

}
