package report3;


import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * 
 * TableModel for Profiles TabbedPane in RCSetupFrame Displays Profilename
 * Accepts List<Channel> with attributes from ChannelSearch
 */

public class ProfilesTableModel extends AbstractTableModel {

	private Vector<Vector<String>> rowsDisplay;
	private Vector<String> header;
	private String[] possibleChannels = { "8a", "8b", "8c", "22a", "22b",
			"22c", "22d", "30a", "30b", "30c", "30d", "34a", "34b", "34c",
			"34d", "37a", "37b", "37c", "44a", "44b", "44c", "44d", "54a",
			"54b", "54c", "54d", "57a", "57b", "57c", "57d", "64a", "64b",
			"64c" };

	/**
	 * Constructor with parameters
	 * 
	 * @param data
	 *            List<String> for Cells
	 * @param header
	 *            Vector<String> for Headers
	 */
	public ProfilesTableModel(List<String> data, Vector<String> header) {
		rowsDisplay = new Vector<Vector<String>>();
		this.header = header;
		resetTableModel(data);
	}

	/**
	 * Getter for ColumnName
	 */
	public String getColumnName(int col) {
		return header.get(col);
	}

	/**
	 * Getter for ColumnCount
	 */
	@Override
	public int getColumnCount() {
		return header.size();
	}

	/**
	 * Getter for RowCount
	 */
	@Override
	public int getRowCount() {
		return rowsDisplay.size();
	}

	/**
	 * Getter for Value in cells x=arg0, y=arg1
	 */
	@Override
	public Object getValueAt(int arg0, int arg1) {
		return rowsDisplay.get(arg0).get(arg1);
	}

	/**
	 * Returns false, so all cells are not editable
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/**
	 * Removes row given by param
	 * 
	 * @param rowAt
	 */
	public void removeRow(int rowAt) {
		rowsDisplay.remove(rowAt);
	}

	/**
	 * returns Datamodell which is Vector< Vector<String>>
	 * 
	 * @return
	 */
	public Vector<Vector<String>> getChannelList() {
		return rowsDisplay;
	}

	/**
	 * Clears TableModell and inserts new Data given by param
	 * 
	 * @param data
	 */
	public void resetTableModel(List<String> data) {
		if (rowsDisplay.size() > 0) {
			rowsDisplay.clear();
		}
		rowsDisplay = new Vector<Vector<String>>();

		for (int i = 0; i < data.size(); i++) {
			Vector<String> actRow = new Vector<String>();
			actRow.addElement(data.get(i));
			rowsDisplay.add(actRow);
		}
	}

	/**
	 * Changes value in selected row and switches its values with the one row it
	 * came from Informs TableModelListeners
	 * 
	 * @param input
	 *            Value
	 * @param row
	 *            int
	 * @param col
	 *            int (is ignored)
	 */
	public void setValueAt(String input, int row, int col) {
		boolean testChannels = false;
		int theRow = 0;
		int indexOfObjectList = 0;
		Vector<String> rowsVector = rowsDisplay.get(row);
		String copyFrom = null;
		Vector<String> copyListTo = null;
		Vector<String> copyListFrom = null;

		for (String s : possibleChannels) {
			if (input.toString().equals(s)) {
				testChannels = true;
			}
		}
		if (!testChannels) {
			System.out.println("Input not in channel range!");
			return;
		}

		for (theRow = 0; theRow < rowsDisplay.size(); theRow++) {
			if (theRow == row) {
				copyListFrom = rowsDisplay.get(row);
				copyFrom = rowsDisplay.get(row).get(0);
			}
		}

		for (Vector<String> s : rowsDisplay) {
			if (s.contains(input)) {
				copyListTo = s;
				indexOfObjectList = rowsDisplay.indexOf(s);
				if (copyFrom != null) {
					copyListTo.set(0, copyFrom);
				}
			}
		}
		fireTableCellUpdated(row, col);
		if (copyListTo != null && copyListFrom != null) {
			rowsVector.set(0, input.toString());
			rowsDisplay.set(row, rowsVector);
			rowsDisplay.set(row, copyListTo);
			rowsDisplay.set(indexOfObjectList, copyListFrom);
		}
	}

}

