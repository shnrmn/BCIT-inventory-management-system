/**
 * Project: a00876532Lab3
 * File: ApplicationException.java
 * Date: 2013-05-02
 * Time: 1:02:58 PM
 */

package a00876532.ims;

/**
 * @author Shawn Norman, A00876532
 * 
 */
@SuppressWarnings("serial")
public class ApplicationException extends Exception {

	/**
	 * Construct the exception.
	 * 
	 * @param message
	 */
	public ApplicationException(String message) {
		super(message);
	}
}
