package JavaProcFram;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;

/**
 * Implements the log viewer GUI.
 *
 */
public class LogViewerApp {
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JTable table;
	private JScrollPane tableScrollPane;
	private JButton refreshButton;
	
	private int numberOfNewAlerts = 0;
	private int numberOfQueries = 0;
	
	public LogViewerApp() {
		
	}
	/**
	 * Prepares and displays the application's GUI.
	 */
	private void prepareAndShowGUI() {
		mainFrame = new JFrame("Log Viewer");
		mainFrame.setMinimumSize(new Dimension(400, 300));
		mainFrame.setPreferredSize(new Dimension(800, 500));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		
		String[] columnNames = {"ID", "Date", "Display", "Message"};
		int[] columnWidths = {40, 300, 60, 500};
		table = new JTable(new AlertDataModel()) {
			
			private static final long serialVersionUID = 201308241505L;
			private Color newRowColor = new Color(207, 230, 210);

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (numberOfQueries > 1) {
					if (row < numberOfNewAlerts) {
						c.setBackground(newRowColor);
					} else {
						c.setBackground(Color.WHITE);
					}
				}
				return c;
			}
		};
		
		for (int i=0; i<columnNames.length; i++) {
			table.getColumnModel().getColumn(i).setHeaderValue(columnNames[i]);
			table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
			table.getColumnModel().getColumn(i).setMinWidth(columnWidths[i]);
			if (i < columnNames.length-1) {
				table.getColumnModel().getColumn(i).setMaxWidth(columnWidths[i]);
			}
		}
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setAutoscrolls(true);
		tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(4,4,4,4);
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		mainPanel.add(tableScrollPane, c);
		
		refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refreshButton.setEnabled(false);
				Vector<Alert> alerts = Database.getSharedInstance().getAllNewAlerts();
				numberOfNewAlerts = alerts.size();
				System.out.println("Number of new alerts: " + numberOfNewAlerts);
				numberOfQueries++;
				AlertDataModel model = (AlertDataModel)table.getModel();
				if (numberOfNewAlerts > 0) {
					model.getAlerts().addAll(0, alerts);
					model.fireTableRowsInserted(0, numberOfNewAlerts);
				}
				
				refreshButton.setEnabled(true);
			}
		});
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(4,4,4,4);
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		mainPanel.add(refreshButton, c);
		
		mainFrame.getContentPane().add(mainPanel);
		mainFrame.setResizable(true);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final LogViewerApp app = new LogViewerApp(); 
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	app.prepareAndShowGUI();
            }
		});
	}

}
