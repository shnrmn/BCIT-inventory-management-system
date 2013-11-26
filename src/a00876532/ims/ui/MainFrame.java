/**
 * Project: a00876532Lab10
 * File: MainFrame.java
 * Date: 2013-06-02
 * Time: 6:35:38 PM
 */

package a00876532.ims.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import a00876532.ims.ApplicationException;
import a00876532.ims.Ims;
import a00876532.ims.data.Stock;
import a00876532.ims.db.dao.StockDao;
import a00876532.ims.inventory.InventoryReport;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;

/**
 * The main frame of the application.
 * 
 * @author Shawn Norman, A00876532
 * 
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	static Logger logger = Logger.getLogger(Ims.class);

	private JPanel _contentPane;

	private StockDao dao;
	private ArrayList<Stock> stock;
	private MainFrame main;

	/**
	 * Create the frame.
	 * 
	 * @throws ApplicationException
	 */
	public MainFrame() throws ApplicationException {
		main = this;
		logger.debug("MainFrame()");
		dao = new StockDao();
		try {
			stock = dao.getAll();
		} catch (ApplicationException e1) {
			throw e1;
		}

		init();
	}

	/**
	 * Initialize the application.
	 */
	private void init() {
		setTitle("Inventory Management System");
		logger.debug("Initializing application.");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 150);
		setLocationRelativeTo(null);

		createMenu();

		_contentPane = new JPanel();
		_contentPane.setLayout(new BorderLayout());
		_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(_contentPane);
	}

	/**
	 * Create the menu bar.
	 */
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Quit");
		mntmExit.setMnemonic('Q');
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("Terminating application.");
				System.exit(0);
			}
		});

		mnFile.add(mntmExit);

		JMenu mnTables = new JMenu("Tables");
		mnTables.setMnemonic('T');
		menuBar.add(mnTables);

		JMenuItem mntmItems = new JMenuItem("Items");
		mntmItems.setMnemonic('I');
		mntmItems.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ItemTable items;
				try {
					items = new ItemTable();
					items.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					items.setLocationRelativeTo(main);
					items.setVisible(true);
				} catch (ApplicationException e1) {
					logger.error("Could not create Item Table.");
					JOptionPane.showMessageDialog(MainFrame.this, "Could not create Item Table.", "Error",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnTables.add(mntmItems);

		JMenuItem mntmStores = new JMenuItem("Stores");
		mntmStores.setMnemonic('S');
		mntmStores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StoreTable stores = null;
				try {
					stores = new StoreTable();
					stores.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					stores.setLocationRelativeTo(main);
					stores.setVisible(true);
				} catch (ApplicationException e1) {
					logger.error("Could not create Store Table.");
					JOptionPane.showMessageDialog(MainFrame.this, "Could not create Store Table.", "Error",
							JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
		mnTables.add(mntmStores);

		JMenuItem mntmStock = new JMenuItem("Stock");
		mntmStock.setMnemonic('K');
		mntmStock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StockTable stock;
				try {
					stock = new StockTable();
					stock.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					stock.setLocationRelativeTo(main);
					stock.setVisible(true);
				} catch (ApplicationException e1) {
					logger.error("Could not create Store Table.");
					JOptionPane.showMessageDialog(MainFrame.this, "Could not create Stock Table.", "Error",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnTables.add(mntmStock);

		JMenu mnReports = new JMenu("Reports");
		mnReports.setMnemonic('R');
		menuBar.add(mnReports);

		JMenu mnInventory = new JMenu("Inventory");
		mnInventory.setMnemonic('I');
		mnReports.add(mnInventory);

		JMenuItem mntmTotal = new JMenuItem("Total");
		mntmTotal.setMnemonic('O');
		mntmTotal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int total = 0;
				for (Stock toAdd : stock) {
					total += toAdd.getValue();
				}
				JOptionPane.showMessageDialog(MainFrame.this,
						"Total Inventory Value: $" + String.format("%8.2f", total / 100.0), "Total",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnInventory.add(mntmTotal);

		final JCheckBoxMenuItem descending = new JCheckBoxMenuItem("Descending");
		mnInventory.add(descending);

		JMenuItem mntmByPrice = new JMenuItem("By Price");
		mntmByPrice.setMnemonic('P');
		mntmByPrice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OpenReport("by_price", descending.getState());
			}
		});
		mnInventory.add(mntmByPrice);

		JMenuItem mntmByCount = new JMenuItem("By Count");
		mntmByCount.setMnemonic('C');
		mntmByCount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OpenReport("by_count", descending.getState());
			}
		});
		mnInventory.add(mntmByCount);

		JMenuItem mntmByValue = new JMenuItem("By Value");
		mntmByValue.setMnemonic('V');
		mntmByValue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OpenReport("by_value", descending.getState());
			}
		});
		mnInventory.add(mntmByValue);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('H');
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mntmAbout.setMnemonic('a');
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this,
						"Inventory Management System\nBy Shawn Norman, A00876532", "About IMS",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnHelp.add(mntmAbout);
	}

	/**
	 * Open report dialog.
	 * 
	 * @param arg
	 *            The argument for how the report is sorted.
	 * @param descending
	 *            Whether the order is descending or not.
	 */
	private void OpenReport(String arg, boolean descending) {
		String order = null;
		if (descending) {
			order = "desc";
		}
		String[] args = { arg, order };
		InventoryReport ir = new InventoryReport(stock, args);
		ArrayList<String[]> report = new ArrayList<String[]>();
		try {
			report = ir.getReport();
		} catch (ApplicationException e) {
			logger.error("Could not generate report.");
			JOptionPane.showMessageDialog(MainFrame.this, "Could not generate report.", "Error",
					JOptionPane.INFORMATION_MESSAGE);

		}
		ReportTable _report = new ReportTable(report);
		_report.setLocationRelativeTo(main);
		_report.setVisible(true);
	}
}
