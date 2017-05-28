package report3;


import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel for ChannelTable in RcMainFrame
 * Dispays only Position, Channel, Programm, Supplier
 * Accepts List<Channel> with attributes from ChannelSearch
 */
public class MyTableModel extends AbstractTableModel {
	private Vector<Vector<String>> rows;
	private Vector<String> header;

	/**
	 * Constructor with parameters
	 * @param data List<String> for Cells
	 * @param header Vector<String> for Headers
	 */
	MyTableModel(List<Channel> data, Vector<String> header) {
		rows = new Vector<Vector<String>>();
		this.header = header;
		
		for (int i = 0; i < data.size(); i++) {
			Vector<String> actRow = new Vector<String>();
			actRow.addElement(new Integer(i).toString());
			actRow.addElement(data.get(i).getChannel());
			actRow.addElement(data.get(i).getProgram());
			actRow.addElement(data.get(i).getSupplier());
			rows.add(actRow);
		}
		
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
		return rows.size();
	}

	/**
	 * Getter for Value in cells x=arg0, y=arg1
	 */
	@Override
	public Object getValueAt(int arg0, int arg1) {
		return rows.get(arg0).get(arg1);
	}

	/**
	 * Returns false, so all cells are not editable
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
        return false;
    }
}

