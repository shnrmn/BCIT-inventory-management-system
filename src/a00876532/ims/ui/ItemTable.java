/**
 * Project: a00876532Ims
 * File: ItemTable.java
 * Date: 2013-06-29
 * Time: 6:35:38 PM
 */

package a00876532.ims.ui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import a00876532.ims.ApplicationException;
import a00876532.ims.Ims;
import a00876532.ims.data.Item;
import a00876532.ims.db.dao.ItemDao;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ItemTable extends JFrame {

	public static final int PRODUCT_NUMBER = 0;
	public static final int DESCRIPTION = 1;
	public static final int PRICE = 2;
	public static final int DISCOUNT = 3;
	public static final int IMAGE = 4;

	static Logger logger = Logger.getLogger(Ims.class);

	private JPanel _contentPane;
	private final ItemDao _itemDao;
	private JTable _table;
	private final ItemDataModel _tableModel;
	private final ItemDataColumnModel _itemDataColumnModel;

	/**
	 * Create the frame.
	 * 
	 * @throws ApplicationException
	 */
	public ItemTable() throws ApplicationException {

		logger.debug("ItemTable()");

		setTitle("Items");
		_itemDao = new ItemDao();
		try {
			_tableModel = new ItemDataModel();
		} catch (ApplicationException e1) {
			throw e1;
		}
		_itemDataColumnModel = new ItemDataColumnModel();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(960, 190);
		_contentPane = new JPanel();
		_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(_contentPane);

		_table = new JTable(_tableModel, _itemDataColumnModel);
		_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_table.getColumnModel().getColumn(PRODUCT_NUMBER).setPreferredWidth(300);
		_table.getColumnModel().getColumn(DISCOUNT).setPreferredWidth(40);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		_table.getColumnModel().getColumn(PRICE).setCellRenderer(rightRenderer);
		_table.getColumnModel().getColumn(DISCOUNT).setCellRenderer(rightRenderer);
		_contentPane.setLayout(new MigLayout("", "[934px,grow]", "[115px][][grow]"));
		_contentPane.add(new JScrollPane(_table), "cell 0 0,grow");

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ItemTable.this.dispose();
			}
		});
		_contentPane.add(btnCancel, "flowx,cell 0 1");

		JPanel panel = new JPanel();
		_contentPane.add(panel, "cell 0 2,grow");
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int rowIndex = _table.getSelectedRow();
				if (rowIndex >= 0) {
					String sku = (String) _table.getModel().getValueAt(rowIndex, 1);
					ItemTable.this.dispose();
					try {
						showItemDetails(rowIndex, sku);
					} catch (ApplicationException e1) {
						logger.error(e1.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(ItemTable.this, "Please select a row.", "Choose an item",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		_contentPane.add(btnOK, "cell 0 1,alignx right");

	}

	/**
	 * Create an Item Dialog from the selected row.
	 * 
	 * @param rowIndex
	 *            The selected row.
	 * @param sku
	 *            The item number.
	 * @throws ApplicationException
	 */
	protected void showItemDetails(int rowIndex, String sku) throws ApplicationException {
		Item item;
		try {
			item = _itemDao.getItem(sku);
			ItemDialog dialog = new ItemDialog(this, item);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			item = dialog.getItem();
			if (item != null) {
				_itemDao.update(item);
				updateTable(item, rowIndex);
			}

			_table.clearSelection();
		} catch (ApplicationException e) {
			throw e;
		}
	}

	/**
	 * Update the table.
	 * 
	 * @param item
	 *            The item to update.
	 * @param rowIndex
	 *            The rox to update.
	 */
	private void updateTable(Item item, int rowIndex) {
		_tableModel.setValueAt(item.getDescription(), rowIndex, 0);
		_tableModel.setValueAt(item.getProductNumber(), rowIndex, 1);
		_tableModel.setValueAt(item.getPrice(), rowIndex, 2);
		_tableModel.setValueAt(item.getDiscount(), rowIndex, 3);
		_tableModel.setValueAt(item.get_imageName(), rowIndex, 4);
		_tableModel.fireTableRowsUpdated(rowIndex, rowIndex);
	}

	/**
	 * An inner class to handle the Table Column Model.
	 * 
	 * @author Shawn Norman, A00876532
	 * 
	 */
	class ItemDataColumnModel extends DefaultTableColumnModel {

		/**
		 * Constructor.
		 */
		ItemDataColumnModel() {
			int i = 0;
			String[] NAMES = { "SKU", "Description", "Price", "Discount", "Image Name" };

			for (String name : NAMES) {
				TableColumn column = new TableColumn(i++);
				column.setHeaderValue(name);
				addColumn(column);
			}
		}
	}

	/**
	 * An inner class to handle the Table Model.
	 * 
	 * @author Shawn Norman, A00876532
	 * 
	 */
	class ItemDataModel extends AbstractTableModel {

		Map<Integer, Item> _items;

		/**
		 * Constructor.
		 * 
		 * @throws ApplicationException
		 */
		ItemDataModel() throws ApplicationException {
			_items = new HashMap<Integer, Item>();
			int i = 0;
			try {
				Map<String, Item> dbItems = _itemDao.getAll();
				for (Item item : dbItems.values()) {
					_items.put(i++, item);
				}
			} catch (ApplicationException e) {
				throw e;
			}
		}

		/**
		 * @return the column count.
		 */
		@Override
		public int getColumnCount() {
			return _itemDataColumnModel.getColumnCount();
		}

		/**
		 * @return the row count.
		 */
		@Override
		public int getRowCount() {
			return _items.size();
		}

		/**
		 * @return the column class.
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Class getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return String.class;
			case 1:
				return String.class;
			case 2:
				return String.class;
			case 3:
				return String.class;
			case 4:
				return String.class;
			default:
				return null;
			}
		}

		/**
		 * @return the value of a cell.
		 * @param rowIndex
		 *            The row to get.
		 * @param columnIndex
		 *            The column to get.
		 */
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Item item = _items.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return item.getDescription();
			case 1:
				return item.getProductNumber();
			case 2:
				return String.format("%5.2f", item.getPrice() / 100.0);
			case 3:
				return String.format("%5.1f %%", item.getDiscount() * 100);
			case 4:
				return item.get_imageName();
			default:
				return null;
			}
		}

		/**
		 * Set the value of a cell.
		 * 
		 * @param value
		 *            The value to set.
		 * @param rowIndex
		 *            The row to set.
		 * @param columnIndex
		 *            The column to set.
		 */
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			Item item = _items.get(rowIndex);
			switch (columnIndex) {
			case 0:
				item.setDescription(value.toString());
				break;
			case 1:
				item.setProductNumber(value.toString());
				break;
			case 2:
				item.setPrice((Integer) value);
				break;
			case 3:
				item.setDiscount((Double) value);
				break;
			case 4:
				item.set_imageName(value.toString());
				break;
			default:
			}
		}

	}

}
