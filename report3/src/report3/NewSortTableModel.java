package report3;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

/**
 * Interface is needed so that TableRowTransferHandler can access the Model
 * 
 */
interface Reorderable {
	public void reorder(int fromIndex, int toIndex);
}

/**
 * TableModel for ChannelSort TabbedPane saves a intern List which fits in
 * Profile and a List for Display use (Dispays only Position, Programm and empty
 * Coll for Editing) Accepts List<Channel> with attributes from ChannelSearch
 * Supports Drag&Drop and Reordering by input new position in coll 2
 */
public class NewSortTableModel extends AbstractTableModel implements
		Reorderable {

	private Vector<Vector<String>> rowsDisplay;
	private List<Channel> rowsIntern;
	private Vector<String> header;

	/**
	 * Constructor with parameters
	 * 
	 * @param data
	 *            List<String> for Cells
	 * @param header
	 *            Vector<String> for Headers
	 */
	NewSortTableModel(List<Channel> data, Vector<String> header) {
		rowsDisplay = new Vector<Vector<String>>();
		rowsIntern = new ArrayList<Channel>();
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
	 * Returns true for Column 2 and false for all others, so all colls except 2
	 * are not editable
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == 2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes row given by param
	 * 
	 * @param rowAt
	 */
	public void removeRow(int rowAt) {
		rowsDisplay.remove(rowAt);
		rowsIntern.remove(rowAt);
	}

	/**
	 * returns Datamodell which is List<Channel>
	 * 
	 * @return
	 */
	public List<Channel> getChannelList() {
		return rowsIntern;
	}

	/**
	 * moves selected row to row which is specified by Value in input and
	 * switches the rows
	 */
	public void setValueAt(Object input, int row, int col) {

		try {
			if (Integer.parseInt((String) input) >= 0
					&& Integer.parseInt((String) input) < rowsDisplay.size()) {
				reorder(row, Integer.parseInt(input.toString()));
			}
		} catch (Exception e) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		}
	}

	/**
	 * Clears TableModel and inserts new Data given by
	 * 
	 * @param data
	 *            List<Channel>
	 */
	public void resetTableModel(List<Channel> data) {
		if (rowsIntern.size() > 0) {
			rowsIntern.clear();
		}
		if (rowsDisplay.size() > 0) {
			rowsDisplay.clear();
		}
		rowsIntern = new ArrayList<Channel>(data);
		rowsDisplay = new Vector<Vector<String>>();

		for (int i = 0; i < data.size(); i++) {
			Vector<String> actRow = new Vector<String>();
			actRow.addElement(new Integer(i).toString());
			actRow.addElement(data.get(i).getProgram());
			actRow.addElement("");
			rowsDisplay.add(actRow);
		}
	}

	/**
	 * switches to rows. Indexes given by param fromIndex and toIndex
	 */
	@Override
	public void reorder(int fromIndex, int toIndex) {
		Vector<String> temp = new Vector<String>(rowsDisplay.get(fromIndex));
		rowsDisplay.remove(fromIndex);
		rowsDisplay.insertElementAt(temp, toIndex);
		Channel tempChannel = new Channel(rowsIntern.get(fromIndex));
		rowsIntern.remove(fromIndex);
		rowsIntern.add(toIndex, tempChannel);
		for (int i = 0; i < rowsDisplay.size(); i++) {
			rowsDisplay.get(i).set(0, new Integer(i).toString());
		}
	}
}

