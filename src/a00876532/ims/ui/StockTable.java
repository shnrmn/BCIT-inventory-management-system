package a00876532.ims.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
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
import a00876532.ims.data.Stock;
import a00876532.ims.db.dao.StockDao;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class StockTable extends JFrame {

	public static final int STORE_NUMBER = 0;
	public static final int ITEM_NUMBER = 1;
	public static final int ITEM_COUNT = 2;
	public static final int VALUE = 3;

	static Logger logger = Logger.getLogger(Ims.class);

	private JPanel _contentPane;
	private final StockDao _stockDao;
	private JTable _table;
	private final StockDataModel _tableModel;
	private final StockDataColumnModel _stockDataColumnModel;

	/**
	 * Create the frame.
	 * 
	 * @throws ApplicationException
	 */
	public StockTable() throws ApplicationException {

		logger.debug("StockTable()");

		setTitle("Stock");
		_stockDao = new StockDao();
		try {
			_tableModel = new StockDataModel();
		} catch (ApplicationException e1) {
			throw e1;
		}
		_stockDataColumnModel = new StockDataColumnModel();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(960, 200);
		_contentPane = new JPanel();
		_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(_contentPane);

		_table = new JTable(_tableModel, _stockDataColumnModel);
		_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_table.getColumnModel().getColumn(ITEM_NUMBER).setPreferredWidth(90);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		_table.getColumnModel().getColumn(ITEM_COUNT).setCellRenderer(rightRenderer);
		_table.getColumnModel().getColumn(VALUE).setCellRenderer(rightRenderer);
		_contentPane.setLayout(new MigLayout("", "[934px,grow]", "[115px][][grow]"));
		_contentPane.add(new JScrollPane(_table), "cell 0 0,grow");

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StockTable.this.dispose();
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
	class StockDataColumnModel extends DefaultTableColumnModel {

		/**
		 * Constructor.
		 */
		StockDataColumnModel() {
			int i = 0;
			String[] NAMES = { "Store Number", "Item Number", "Item Count", "Value" };

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
	class StockDataModel extends AbstractTableModel {

		Map<Integer, Stock> _stock;

		/**
		 * Constructor.
		 * 
		 * @throws ApplicationException
		 */
		StockDataModel() throws ApplicationException {
			_stock = new HashMap<Integer, Stock>();
			int i = 0;
			try {
				ArrayList<Stock> dbStock = _stockDao.getAll();
				for (Stock stock : dbStock) {
					_stock.put(i++, stock);
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
			return _stockDataColumnModel.getColumnCount();
		}

		/**
		 * @return the row count.
		 */
		@Override
		public int getRowCount() {
			return _stock.size();
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
			Stock stock = _stock.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return stock.getStoreNumber();
			case 1:
				return stock.getItemNumber();
			case 2:
				return stock.getItemCount();
			case 3:
				return String.format("%8.2f", stock.getValue() / 100.0);
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
			Stock stock = _stock.get(rowIndex);
			switch (columnIndex) {
			case 0:
				stock.setStoreNumber(value.toString());
				break;
			case 1:
				stock.setItemNumber(value.toString());
				break;
			case 2:
				stock.setItemCount((Integer) value);
				break;
			case 3:
				stock.setValue((Integer) value);
				break;
			default:
			}
		}

	}

}
