package report3;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * 
 * TableModel for Favorites TabbedPane in RCSetupFrame Displays Position and
 * Programm Accepts List<Channel> with attributes from ChannelSearch
 */
public class FavoritesTableModel extends AbstractTableModel {

	/**
	 * The Serial Version ID is used when serializing and deserializing an
	 * object. Java recognizes if the bytes you want to deserialize match the
	 * local class version. If not it will throw an exception.
	 */
	private static final long serialVersionUID = 1L;
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
	FavoritesTableModel(List<Channel> data, Vector<String> header) {
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
		rowsIntern.remove(rowAt);
	}

	/**
	 * adds new Row with Channel given by
	 * 
	 * @param newChannel
	 */
	public void addRow(Channel newChannel) {
		if (getRowCount() < 4) {
			Vector<String> newRow = new Vector<String>();
			newRow.add(new Integer(rowsDisplay.size()).toString());
			newRow.add(newChannel.getProgram());
			rowsDisplay.add(newRow);
			rowsIntern.add(newChannel);
		}
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
	 * Clears internal Data
	 */
	public void clearFavorites() {
		rowsIntern.clear();
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
			rowsDisplay.add(actRow);
		}
	}

}