package JavaProcFram;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * Implements the data model for the GUI's alerts table.
 *
 */
public class AlertDataModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 201308241356L;
	
	private Vector<Alert> alerts;

	public AlertDataModel() {
		super();
		setAlerts(new Vector<Alert>());
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return alerts.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Alert alert = alerts.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return alert.getId();
		case 1:
			return alert.getDate();
		case 2:
			return alert.getDisplay();
		case 3:
			return alert.getMessage();
		}
		return null;
	}

	public Vector<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(Vector<Alert> alerts) {
		this.alerts = alerts;
	}

}
