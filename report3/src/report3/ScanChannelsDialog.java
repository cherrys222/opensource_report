package report3;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

/**
 * Dialog showing Progress in ScanningChannels Process
 * 
 * @author moritz
 * 
 */
public class ScanChannelsDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel(), buttonPane;
	private JButton scanButton, cancelButton;
	private JProgressBar progressBar;
	private JLabel startScanLabel;
	private RcController rcController;
	private Task myTask;

	/**
	 * Constructor of Dialog showing Progress in ScanningChannels Process
	 */
	public ScanChannelsDialog(RcController control) {
		this.rcController = control;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 454, 189);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0 };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0 };
		contentPanel.setLayout(gbl_contentPanel);
		{
			startScanLabel = new JLabel("검색 시작");
			GridBagConstraints gbc_lblStartScan = new GridBagConstraints();
			gbc_lblStartScan.insets = new Insets(0, 0, 5, 0);
			gbc_lblStartScan.gridx = 0;
			gbc_lblStartScan.gridy = 0;
			contentPanel.add(startScanLabel, gbc_lblStartScan);
		}
		{
			progressBar = new JProgressBar();
			progressBar.setStringPainted(true);
			GridBagConstraints gbc_progressBar = new GridBagConstraints();
			gbc_progressBar.weighty = 1.0;
			gbc_progressBar.weightx = 0.8;
			gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
			gbc_progressBar.gridx = 0;
			gbc_progressBar.gridy = 1;
			contentPanel.add(progressBar, gbc_progressBar);
		}
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				cancelButton = new JButton("취소");
				cancelButton.setActionCommand("취소");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(this);
			}
			{
				scanButton = new JButton("검색");
				scanButton.setActionCommand("검색");
				buttonPane.add(scanButton);
				getRootPane().setDefaultButton(scanButton);
				scanButton.addActionListener(this);
			}
		}
	}

	/**
	 * SwingWorker for long-term Task - doesnt block GUI. First Type (here
	 * String): return of get (called in done) and doInBackground second Type
	 * (here Integer): Parameter for publish and process(List<Integer>)
	 */
	class Task extends SwingWorker<String, Integer> {
		@Override
		protected String doInBackground() throws Exception {
			for (int i = 0; i <= 25; ++i) {
				Thread.sleep(50);
				startScanLabel.setText("검색중......");
				publish(new Integer(i));
			}
			for (int i = 26; i <= 50; ++i) {
				Thread.sleep(50);
				startScanLabel.setText("검색중............");
				publish(new Integer(i));
			}
			for (int i = 51; i <= 75; ++i) {
				Thread.sleep(50);
				startScanLabel.setText("검색중..................");
				publish(new Integer(i));
			}
			for (int i = 76; i <= 100; ++i) {
				Thread.sleep(50);
				startScanLabel.setText("검색중........................");
				publish(new Integer(i));
			}
			return "검색완료";
		}

		/**
		 * invoked from the event dispatch thread
		 */
		@Override
		protected void done() {
			try {
				startScanLabel.setText(get());
				scanButton.setEnabled(true);
				if (rcController.getTvMemory().getProfilesSize() == 0) {
					rcController.getTvMemory().addProfile();
				} else {
					rcController
							.getTvMemory()
							.getProfileAt(
									rcController.getTvMemory()
											.getCurrentProfile())
							.setChannels(rcController.scanChannels());
				}
				rcController.getRcMainFrame().initChannelTable();
				rcController.getRcMainFrame().playSelectedChannel();
				setVisible(false);
				dispose();
			} catch (InterruptedException | ExecutionException e) {
				Logger.getLogger(RcMainFrame.class.getName()).log(
						Level.WARNING, null, e);
			}
		}

		/**
		 * invoked from the event dispatch thread process call is asynchron.
		 */
		@Override
		protected void process(List<Integer> l) {
			progressBar.setValue(l.get(l.size() - 1));
			scanButton.setEnabled(false);
		}
	}

	/**
	 * ActionHandling for ScanButton and cancelButton
	 * 
	 * @Bug Cancel doesnt terminate Thread
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(scanButton)) {
			myTask = new Task();
			myTask.execute();
		}
		if (e.getSource().equals(cancelButton)) {
			this.setVisible(false);
			this.dispose();
		}
	}

}
