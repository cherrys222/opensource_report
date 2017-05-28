package report3;


import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

/**
 * Handles drag & drop row reordering Used in ChannelList in RcSetupFrame
 */
public class TableRowTransferHandler extends TransferHandler {
	// provides the meta information for the drag and drop
	private final DataFlavor localObjectFlavor = new ActivationDataFlavor(
			Integer.class, DataFlavor.javaJVMLocalObjectMimeType,
			"Integer Row Index");
	private JTable table = null;

	/**
	 * Constructor tableRowTransferHandler. Needs JTable for which it is used
	 * 
	 * @param table
	 */
	public TableRowTransferHandler(JTable table) {
		this.table = table;
	}

	/**
	 * Creates a Transferable to use as the source for a data transfer
	 */
	@Override
	protected Transferable createTransferable(JComponent c) {
		assert (c == table);
		return new DataHandler(new Integer(table.getSelectedRow()),
				localObjectFlavor.getMimeType());
	}

	/**
	 * Indicates whether a component will accept an import of the given set of
	 * data flavors prior to actually attempting to import it.
	 */
	@Override
	public boolean canImport(TransferHandler.TransferSupport info) {
		boolean b = info.getComponent() == table && info.isDrop()
				&& info.isDataFlavorSupported(localObjectFlavor);
		table.setCursor(b ? DragSource.DefaultMoveDrop
				: DragSource.DefaultMoveNoDrop);
		return b;
	}

	/**
	 * Returns the type of transfer actions supported by the source
	 */
	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}

	/**
	 * Causes a transfer to occur from a clipboard or a drag and drop operation.
	 * The Transferable to be imported and the component to transfer to are
	 * contained within the TransferSupport.
	 */
	@Override
	public boolean importData(TransferHandler.TransferSupport info) {
		JTable target = (JTable) info.getComponent();
		JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
		int index = dl.getRow();
		int max = table.getModel().getRowCount();
		if (index < 0 || index > max)
			index = max;
		target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		try {
			Integer rowFrom = (Integer) info.getTransferable().getTransferData(
					localObjectFlavor);
			if (rowFrom != -1 && rowFrom != index) {
				((Reorderable) table.getModel()).reorder(rowFrom, index);
				if (index > rowFrom)
					index--;
				target.getSelectionModel().addSelectionInterval(index, index);
				return true;
			}
		} catch (Exception e) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		}
		return false;
	}

	/**
	 * Invoked after data has been exported. This method should remove the data
	 * that was transferred if the action was MOVE.
	 */
	@Override
	protected void exportDone(JComponent c, Transferable t, int act) {
		if (act == TransferHandler.MOVE) {
			table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}

