package a00876532.ims.ui;

import java.util.ArrayList;
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

import a00876532.ims.Ims;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ReportTable extends JFrame {

	public static final int NO = 0;
	public static final int SKU = 1;
	public static final int ITEM = 2;
	public static final int PRICE = 3;
	public static final int DISCOUNT = 4;
	public static final int SALE_PRICE = 5;
	public static final int STORE_NO = 6;
	public static final int STORE = 7;
	public static final int ITEM_COUNT = 8;
	public static final int VALUE = 9;

	static Logger logger = Logger.getLogger(Ims.class);

	private JPanel _contentPane;
	private JTable _table;
	private final ReportDataModel _tableModel;
	private final ReportDataColumnModel _reportDataColumnModel;
	private ArrayList<String[]> report;

	/**
	 * Create the frame.
	 * 
	 * @param report
	 *            The report to show.
	 */
	public ReportTable(ArrayList<String[]> report) {

		logger.debug("ReportTable()");

		setTitle("Inventory Report");
		this.report = report;
		_tableModel = new ReportDataModel();
		_reportDataColumnModel = new ReportDataColumnModel();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1050, 200);
		_contentPane = new JPanel();
		_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(_contentPane);

		_table = new JTable(_tableModel, _reportDataColumnModel);
		_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_table.getColumnModel().getColumn(ITEM).setPreferredWidth(300);
		_table.getColumnModel().getColumn(STORE).setPreferredWidth(180);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		_table.getColumnModel().getColumn(PRICE).setCellRenderer(rightRenderer);
		_table.getColumnModel().getColumn(DISCOUNT).setCellRenderer(rightRenderer);
		_table.getColumnModel().getColumn(SALE_PRICE).setCellRenderer(rightRenderer);
		_table.getColumnModel().getColumn(ITEM_COUNT).setCellRenderer(rightRenderer);
		_table.getColumnModel().getColumn(VALUE).setCellRenderer(rightRenderer);
		_contentPane.setLayout(new MigLayout("", "[934px,grow]", "[115px][][grow]"));
		_contentPane.add(new JScrollPane(_table), "cell 0 0,grow");

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReportTable.this.dispose();
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
	class ReportDataColumnModel extends DefaultTableColumnModel {

		/**
		 * Constructor.
		 */
		ReportDataColumnModel() {
			int i = 0;
			String[] NAMES = { "No.", "SKU", "Item", "Price", "Discount", "Sale Price", "Store No.", "Store",
					"Item Count", "Value" };

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
	class ReportDataModel extends AbstractTableModel {

		/**
		 * Constructor.
		 */
		ReportDataModel() {

		}

		/**
		 * @return the column count.
		 */
		public int getColumnCount() {
			return _reportDataColumnModel.getColumnCount();
		}

		/**
		 * @return the row count.
		 */
		public int getRowCount() {
			return report.size();
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
			String[] entry = report.get(rowIndex);
			return entry[columnIndex];
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
			String[] entry = report.get(rowIndex);
			entry[columnIndex] = value.toString();
		}

	}

}
