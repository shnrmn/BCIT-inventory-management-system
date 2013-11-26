/**
 * Project: a00876532Ims
 * File: ItemDialog.java
 * Date: 2013-06-02
 * Time: 6:35:38 PM
 */

package a00876532.ims.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import a00876532.ims.ApplicationException;
import a00876532.ims.Ims;
import a00876532.ims.data.Item;
import net.miginfocom.swing.MigLayout;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * The frame that displays the item information.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
@SuppressWarnings("serial")
public class ItemDialog extends JDialog {

	static Logger logger = Logger.getLogger(Ims.class);

	private final JPanel _contentPanel = new JPanel();
	private JTextField _productNumberField;
	private JTextArea _descriptionField;
	private JTextField _priceField;
	private JTextField _discountField;
	private JTextField _discountedPriceField;
	private JLabel _imageField;
	private Item _item;

	/**
	 * Create the dialog.
	 * 
	 * @throws ApplicationException
	 */
	public ItemDialog(ItemTable itemTable, Item item) throws ApplicationException {
		super(itemTable, true);

		_item = item;

		setSize(450, 500);
		setLocationRelativeTo(itemTable);
		getContentPane().setLayout(new BorderLayout());
		_contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(_contentPanel, BorderLayout.CENTER);
		_contentPanel.setLayout(new MigLayout("", "[][grow]", "[][grow][][][][][]"));
		{
			JLabel lblProductNumber = new JLabel("Product Number");
			_contentPanel.add(lblProductNumber, "cell 0 0,alignx trailing");
		}
		{
			_productNumberField = new JTextField();
			_productNumberField.setEditable(false);
			_contentPanel.add(_productNumberField, "cell 1 0,growx");
			_productNumberField.setColumns(10);
		}
		{
			JLabel lblDescription = new JLabel("Description");
			_contentPanel.add(lblDescription, "cell 0 1,alignx right,aligny top");
		}
		{
			_descriptionField = new JTextArea();
			_contentPanel.add(new JScrollPane(_descriptionField), "cell 1 1 1 2,grow");
		}
		{
			JLabel lblPrice = new JLabel("Price");
			_contentPanel.add(lblPrice, "cell 0 3,alignx trailing");
		}
		{
			_priceField = new JTextField();
			_contentPanel.add(_priceField, "cell 1 3,growx");
			_priceField.setColumns(10);
		}
		{
			JLabel lblDiscount = new JLabel("Discount");
			_contentPanel.add(lblDiscount, "cell 0 4,alignx trailing");
		}
		{
			_discountField = new JTextField();
			_contentPanel.add(_discountField, "cell 1 4,growx");
			_discountField.setColumns(10);
		}
		{
			JLabel lblDiscountedPrice = new JLabel("Discounted Price");
			_contentPanel.add(lblDiscountedPrice, "cell 0 5,alignx trailing");
		}
		{
			_discountedPriceField = new JTextField();
			_discountedPriceField.setEditable(false);
			_contentPanel.add(_discountedPriceField, "cell 1 5,growx");
			_discountedPriceField.setColumns(10);
		}
		{
			JLabel lblImage = new JLabel("Image");
			_contentPanel.add(lblImage, "cell 0 6");
		}
		{
			_imageField = new JLabel();
			_imageField.setPreferredSize(new Dimension(66, 146));
			_contentPanel.add(_imageField, "cell 1 6");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							setItemData();
						} catch (ApplicationException e1) {
							JOptionPane.showMessageDialog(ItemDialog.this, e1.getMessage(), "Error",
									JOptionPane.INFORMATION_MESSAGE);
						}
						ItemDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						_item = null;
						ItemDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		try {
			setDialogData(item);
		} catch (ApplicationException e) {
			throw e;
		}
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return _item;
	}

	/**
	 * Set the text in the dialog.
	 * 
	 * @param item
	 *            The item to set in the dialog.
	 * @throws ApplicationException
	 */
	private void setDialogData(Item item) throws ApplicationException {
		_productNumberField.setText(item.getProductNumber());
		_descriptionField.setText(item.getDescription());
		_priceField.setText(String.format("%5.2f", item.getPrice() / 100.0));
		_discountField.setText(String.format("%5.2f %%", item.getDiscount() * 100));
		_discountedPriceField.setText(String.format("%5.2f", item.getSalePrice() / 100.0));
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(String.format("images/%s", item.get_imageName())));
			_imageField.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			throw new ApplicationException("Could not read image file.");
		}
	}

	private void setItemData() throws ApplicationException {
		try {
			_item.setProductNumber(_productNumberField.getText());
			_item.setDescription(_descriptionField.getText());
			String price = _priceField.getText();
			price = price.replaceAll("[^0-9]", "");
			_item.setPrice(Integer.parseInt(price));
			String discount = _discountField.getText();
			discount = discount.replaceAll("[^0-9.]", "");
			float value = Float.parseFloat(discount) / 100;
			_item.setDiscount(value);
		} catch (NumberFormatException e) {
			throw new ApplicationException("Must enter a numerical value.");
		}
	}
}
