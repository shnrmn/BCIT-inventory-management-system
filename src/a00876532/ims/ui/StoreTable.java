package a00876532.ims.ui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import a00876532.ims.ApplicationException;
import a00876532.ims.Ims;
import a00876532.ims.data.Store;
import a00876532.ims.db.dao.StoreDao;

import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class StoreTable extends JFrame {

	public static final int STORE_NUMBER = 0;
	public static final int DESCRIPTION = 1;
	public static final int STREET = 2;
	public static final int CITY = 3;
	public static final int PROVINCE = 4;
	public static final int POSTAL_CODE = 5;
	public static final int STORE_PH = 6;
	public static final int SERVICE_PH = 7;

	static Logger logger = Logger.getLogger(Ims.class);

	private JPanel _contentPane;
	private final StoreDao _storeDao;
	private JTable _table;
	private final StoreDataModel _tableModel;
	private final StoreDataColumnModel _storeDataColumnModel;

	/**
	 * Create the frame.
	 * 
	 * @throws ApplicationException
	 */
	public StoreTable() throws ApplicationException {

		logger.debug("StoreTable()");

		setTitle("Stores");
		_storeDao = new StoreDao();
		try {
			_tableModel = new StoreDataModel();
		} catch (ApplicationException e1) {
			throw e1;
		}
		_storeDataColumnModel = new StoreDataColumnModel();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(960, 160);
		_contentPane = new JPanel();
		_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(_contentPane);

		_table = new JTable(_tableModel, _storeDataColumnModel);
		_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_table.getColumnModel().getColumn(DESCRIPTION).setPreferredWidth(130);
		_table.getColumnModel().getColumn(STREET).setPreferredWidth(130);
		_contentPane.setLayout(new MigLayout("", "[934px,grow]", "[115px][][grow]"));
		_contentPane.add(new JScrollPane(_table), "cell 0 0,grow");

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StoreTable.this.dispose();
			}
		});
		_contentPane.add(btnClose, "flowx,cell 0 1");

		JPanel panel = new JPanel();
		_contentPane.add(panel, "cell 0 2,grow");
		panel.setLayout(new GridLayout(1, 0, 0, 0));

	}

	/**
	 * An inner class to handle the Table Column Model.
	 * 
	 * @author Shawn Norman, A00876532
	 * 
	 */
	class StoreDataColumnModel extends DefaultTableColumnModel {

		/**
		 * Constructor.
		 */
		StoreDataColumnModel() {
			int i = 0;
			String[] NAMES = { "Store Number", "Description", "Street", "City", "Province", "Postal Code",
					"Store Phone", "Service Phone" };

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
	class StoreDataModel extends AbstractTableModel {

		Map<Integer, Store> _stores;

		/**
		 * Constructor.
		 * 
		 * @throws ApplicationException
		 */
		StoreDataModel() throws ApplicationException {
			_stores = new HashMap<Integer, Store>();
			int i = 0;
			try {
				Map<String, Store> dbStores = _storeDao.getAll();
				for (Store store : dbStores.values()) {
					_stores.put(i++, store);
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
			return _storeDataColumnModel.getColumnCount();
		}

		/**
		 * @return the row count.
		 */
		@Override
		public int getRowCount() {
			return _stores.size();
		}

		/**
		 * @return the column class.
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Class getColumnClass(int columnIndex) {
			return String.class;
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
			Store store = _stores.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return store.getStoreNumber();
			case 1:
				return store.getDescription();
			case 2:
				return store.getStreet();
			case 3:
				return store.getCity();
			case 4:
				return store.getProvince();
			case 5:
				return store.getPostalCode();
			case 6:
				return store.getStorePhone();
			case 7:
				return store.getServicePhone();
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
			Store store = _stores.get(rowIndex);
			switch (columnIndex) {
			case 0:
				store.setStoreNumber(value.toString());
				break;
			case 1:
				store.setDescription(value.toString());
				break;
			case 2:
				store.setStreet(value.toString());
				break;
			case 3:
				store.setCity(value.toString());
				break;
			case 4:
				store.setProvince(value.toString());
				break;
			case 5:
				store.setPostalCode(value.toString());
				break;
			case 6:
				store.setStorePhone(value.toString());
				break;
			case 7:
				store.setServicePhone(value.toString());
				break;
			default:
			}
		}

	}

}
