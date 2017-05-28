package report3;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.util.GregorianCalendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.text.SimpleDateFormat;
/**
 * 
 * @author moritz
 * 
 *         Mainframe of RemoteControl Entrypoint for UserInteraction. Appears
 *         first on screen
 */
public class RcMainFrame extends JFrame implements ActionListener,
		ChangeListener {
	private Calendar calendar1 = Calendar.getInstance(); 
	private static Logger myLogger = Logger.getLogger(RcMainFrame.class
			.getName());
	private static FileHandler myFilehandler;
	private JPanel contentPane, middlePanel, rightPanel;
	private JScrollPane leftScrollPane;
	private JTable channelTable;
	private JButton plusButton, minusButton, okButton, setupButton, pipButton,
			lastChannelButton, recButton, playButton, stopRecButton,
			muteButton;
	private JSlider volumeSlider, offsetSlider;
	private JLabel volumeLabel, timeRecordedLabel, volumeLabel2, timeLabel;
    private int hour = calendar1.get(Calendar.HOUR_OF_DAY);
    private int min = calendar1.get(Calendar.MINUTE);
    private int sec = calendar1.get(Calendar.SECOND); 
	private RcController rcController;
	private Timer myTimer, timer;
	private int[] Spaltenbreite = { 50, 100, 150, 150 };
	private Font f121 = new Font("Times New Roman", 1, 14);
	private URL muteIconURL, pipIconURL, lastChannelIconURL, recordIconURL,
			stopRecIconURL, playIconURL;
	private URL volumeIconURL = RcMainFrame.class
			.getResource("/icons/volume_Icon.png");
	/**
	 * main Method
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RcMainFrame frame = new RcMainFrame();
					frame.pack();
					frame.setVisible(true);
				} catch (Exception e) {
					Logger.getLogger(RcMainFrame.class.getName()).log(
							Level.WARNING, null, e);
				}				
			}
		});
	}
	/**
	 * Constructor RcMainFrame inits Panels and creates RcController and
	 * BackgroundLogik
	 */
	public RcMainFrame() {
		initLogger();
		this.setTitle("리모컨 컨트롤러");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				closeOperation();
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(938, 301);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbLayout = new GridBagLayout();
		gbLayout.columnWidths = new int[] { 808, 81, 340 };
		gbLayout.rowHeights = new int[] { 250 };
		gbLayout.columnWeights = new double[] { 1.0, 0.1, 1.0 };
		gbLayout.rowWeights = new double[] { 1.0 };
		contentPane.setLayout(gbLayout);

		initLeftScrollPane();

		initMiddlePanel();

		initRightPanel();

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				- this.getSize().height);
		initBackgroundLogic();
	}

	/**
	 * initializes RcController class with persistence class TvMemory and
	 * dependent classes. Also activates ChannelScan, if no Serialized File is
	 * available
	 */
	private void initBackgroundLogic() {
		rcController = new RcController(this, new TvScreenFrame());

		if (rcController.getTvMemory() == null) {
			rcController.setTvMemory(new TvMemory(rcController));
			rcController.getTvMemory().setCurrentProfile(0);
			rcController.getTvMemory().setVolumeLevel(30);
			volumeLabel2.setText(String.format("%s",volumeSlider.getValue()));
			ScanChannelsDialog scan = new ScanChannelsDialog(rcController);
			scan.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
			scan.setLocationRelativeTo(this);
			scan.setVisible(true);
		} else {
			rcController.getTvMemory().setMissingAttributes(rcController);
			this.initChannelTable();
			okButton.doClick();
			setVolumeSlider(rcController.getTvMemory().getVolumeLevel());
			volumeLabel2.setText(String.format("%s",volumeSlider.getValue()));
			setFormat(rcController.getTvMemory().getCurrentFormat());
		}
	}

	/**
	 * initializes Logger for Exceptionhandling
	 */
	private void initLogger() {
		myLogger.setUseParentHandlers(false);
		Handler[] handlers = myLogger.getHandlers();
		for (Handler handler : handlers) {
			if (handler.getClass() == ConsoleHandler.class)
				myLogger.removeHandler(handler);
		}
		try {
			myFilehandler = new FileHandler("%t/RcControl_LogFile.log", true);
		} catch (SecurityException | IOException e) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		}
		myLogger.addHandler(myFilehandler);
		myLogger.setLevel(Level.ALL);
		SimpleFormatter formatter = new SimpleFormatter();
		myFilehandler.setFormatter(formatter);
	}

	private void initRightPanel() {
		rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(340, 250));
		rightPanel.setMinimumSize(new Dimension(340, 250));
		GridBagLayout gbLayout = new GridBagLayout();
		gbLayout.columnWidths = new int[] { 85, 85, 85, 85 };
		gbLayout.rowHeights = new int[] { 25, 45, 80, 100 };
		gbLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		gbLayout.rowWeights = new double[] { 1.0, 0.5, 1.0, 1.0 };
		rightPanel.setLayout(gbLayout);
		GridBagConstraints gbConstraint = new GridBagConstraints();
		gbConstraint.fill = GridBagConstraints.BOTH;
		gbConstraint.gridx = 2;
		gbConstraint.gridy = 0;
		gbConstraint.insets = new Insets(0, 12, 0, 0);
		contentPane.add(rightPanel, gbConstraint);
		
		timer = new Timer(1000,this);
		gbConstraint = new GridBagConstraints();
		gbConstraint.anchor = GridBagConstraints.NORTHEAST;
		gbConstraint.gridx = 0;
		gbConstraint.gridy = 0;
		gbConstraint.gridwidth = 2;
		gbConstraint.insets = new Insets(0, 0, 0, 0);
		timer.setInitialDelay(0);
		timer.start();
		timeLabel = new JLabel("현재 : " + hour + "시" + min + "분 " + sec + "초", 
                Label.RIGHT);
		rightPanel.add(timeLabel,gbConstraint);

		setupButton = new JButton("옵션");
		setupButton.setPreferredSize(new Dimension(117, 25));
		gbConstraint = new GridBagConstraints();
		gbConstraint.anchor = GridBagConstraints.NORTHEAST;
		gbConstraint.gridx = 2;
		gbConstraint.gridy = 0;
		gbConstraint.gridwidth = 2;
		gbConstraint.insets = new Insets(0, 0, 0, 1);
		rightPanel.add(setupButton, gbConstraint);
		setupButton.addActionListener(this);

		pipButton = new JButton("");
		pipButton.setPreferredSize(new Dimension(60, 60));
		pipButton.setMinimumSize(new Dimension(60, 60));
		pipIconURL = RcMainFrame.class.getResource("/icons/pip_Icon.png");
		if (pipIconURL != null) {
			pipButton.setIcon(new ImageIcon(pipIconURL));
		}
		gbConstraint = new GridBagConstraints();
		gbConstraint.anchor = GridBagConstraints.EAST;
		gbConstraint.gridx = 3;
		gbConstraint.gridy = 2;
		gbConstraint.fill = GridBagConstraints.BOTH;
		gbConstraint.insets = new Insets(10, 24, 10, 1);
		rightPanel.add(pipButton, gbConstraint);
		pipButton.addActionListener(this);

		lastChannelButton = new JButton("");
		lastChannelButton.setPreferredSize(new Dimension(60, 60));
		lastChannelButton.setMinimumSize(new Dimension(60, 60));
		lastChannelIconURL = RcMainFrame.class
				.getResource("/icons/lastChannel_Icon.png");
		if (lastChannelIconURL != null) {
			lastChannelButton.setIcon(new ImageIcon(lastChannelIconURL));
		}
		gbConstraint = new GridBagConstraints();
		gbConstraint.anchor = GridBagConstraints.EAST;
		gbConstraint.gridx = 2;
		gbConstraint.gridy = 2;
		gbConstraint.fill = GridBagConstraints.BOTH;
		gbConstraint.insets = new Insets(10, 25, 10, 0);
		rightPanel.add(lastChannelButton, gbConstraint);
		lastChannelButton.addActionListener(this);

		recButton = new JButton("");
		recButton.setPreferredSize(new Dimension(60, 60));
		recButton.setMinimumSize(new Dimension(60, 60));
		recordIconURL = RcMainFrame.class.getResource("/icons/record_Icon.png");
		if (recordIconURL != null) {
			recButton.setIcon(new ImageIcon(recordIconURL));
		}
		gbConstraint = new GridBagConstraints();
		gbConstraint.anchor = GridBagConstraints.WEST;
		gbConstraint.gridx = 0;
		gbConstraint.gridy = 2;
		gbConstraint.fill = GridBagConstraints.BOTH;
		gbConstraint.insets = new Insets(10, 0, 10, 13);
		rightPanel.add(recButton, gbConstraint);
		recButton.addActionListener(this);

		stopRecButton = new JButton();
		stopRecButton.setPreferredSize(new Dimension(60, 60));
		stopRecButton.setMinimumSize(new Dimension(60, 60));
		stopRecIconURL = this.getClass().getResource("/icons/stop_Icon.png");
		if (stopRecIconURL != null) {
			stopRecButton.setIcon(new ImageIcon(stopRecIconURL));
		}
		gbConstraint = new GridBagConstraints();
		gbConstraint.anchor = GridBagConstraints.WEST;
		gbConstraint.gridx = 1;
		gbConstraint.gridy = 2;
		gbConstraint.fill = GridBagConstraints.BOTH;
		gbConstraint.insets = new Insets(10, 0, 10, 25);
		rightPanel.add(stopRecButton, gbConstraint);
		stopRecButton.setVisible(false);
		stopRecButton.addActionListener(this);

		playButton = new JButton("");
		playButton.setPreferredSize(new Dimension(60, 60));
		playButton.setMinimumSize(new Dimension(60, 60));
		playIconURL = this.getClass().getResource("/icons/play_Icon.png");
		if (playIconURL != null) {
			playButton.setIcon(new ImageIcon(playIconURL));
		}
		gbConstraint = new GridBagConstraints();
		gbConstraint.anchor = GridBagConstraints.WEST;
		gbConstraint.gridx = 0;
		gbConstraint.gridy = 2;
		gbConstraint.fill = GridBagConstraints.BOTH;
		gbConstraint.insets = new Insets(10, 0, 10, 13);
		rightPanel.add(playButton, gbConstraint);
		playButton.addActionListener(this);
		playButton.setVisible(false);

		offsetSlider = new JSlider();
		offsetSlider.setMinimum(1);
		offsetSlider.setPreferredSize(new Dimension(269, 16));
		offsetSlider.setMinimumSize(new Dimension(269, 16));
		gbConstraint = new GridBagConstraints();
		gbConstraint.fill = GridBagConstraints.HORIZONTAL;
		gbConstraint.anchor = GridBagConstraints.SOUTHWEST;
		gbConstraint.gridx = 0;
		gbConstraint.gridy = 1;
		gbConstraint.gridwidth = 3;
		rightPanel.add(offsetSlider, gbConstraint);
		offsetSlider.setVisible(false);
		offsetSlider.setValue(1);

		timeRecordedLabel = new JLabel("00:00");
		timeRecordedLabel.setPreferredSize(new Dimension(48, 15));
		timeRecordedLabel.setMinimumSize(new Dimension(48, 15));
		gbConstraint = new GridBagConstraints();
		gbConstraint.anchor = GridBagConstraints.SOUTHWEST;
		gbConstraint.gridx = 3;
		gbConstraint.gridy = 1;
		rightPanel.add(timeRecordedLabel, gbConstraint);
		timeRecordedLabel.setVisible(false);

		myTimer = new Timer(1000, this);

		muteButton = new JButton("");
		muteButton.setPreferredSize(new Dimension(40, 40));
		muteButton.setMinimumSize(new Dimension(40, 40));
		muteIconURL = RcMainFrame.class.getResource("/icons/mute_Icon.png");
		if (muteIconURL != null) {
			muteButton.setIcon(new ImageIcon(muteIconURL));
		}
		gbConstraint = new GridBagConstraints();
		gbConstraint.anchor = GridBagConstraints.SOUTHWEST;
		gbConstraint.gridx = 0;
		gbConstraint.gridy = 3;
		rightPanel.add(muteButton, gbConstraint);
		muteButton.addActionListener(this);

		volumeSlider = new JSlider();
		volumeSlider.setPreferredSize(new Dimension(184, 16));
		volumeSlider.setMinimumSize(new Dimension(184, 16));
		volumeSlider.addChangeListener(this);
		gbConstraint = new GridBagConstraints();
		gbConstraint.fill = GridBagConstraints.HORIZONTAL;
		gbConstraint.anchor = GridBagConstraints.SOUTH;
		gbConstraint.ipady = 24;
		gbConstraint.gridx = 1;
		gbConstraint.gridy = 3;
		gbConstraint.gridwidth = 2;
		rightPanel.add(volumeSlider, gbConstraint);

		volumeLabel = new JLabel("볼륨");
		volumeLabel.setPreferredSize(new Dimension(70, 15));
		volumeLabel.setMinimumSize(new Dimension(70, 15));
		gbConstraint = new GridBagConstraints();
		gbConstraint.ipady = 25;
		gbConstraint.fill = GridBagConstraints.HORIZONTAL;
		gbConstraint.anchor = GridBagConstraints.SOUTHEAST;
		gbConstraint.gridx = 3;
		gbConstraint.gridy = 3;
		rightPanel.add(volumeLabel, gbConstraint);
		
		volumeLabel2 = new JLabel(String.format("%s",volumeSlider.getValue()));
		volumeLabel2.setPreferredSize(new Dimension(70, 15));
		volumeLabel2.setMinimumSize(new Dimension(70, 15));	
		gbConstraint = new GridBagConstraints();
		gbConstraint.ipady = 26;
		gbConstraint.fill = GridBagConstraints.HORIZONTAL;
		gbConstraint.anchor = GridBagConstraints.SOUTHEAST;
		gbConstraint.gridx = 4;
		gbConstraint.gridy = 3;	
		rightPanel.add(volumeLabel2,gbConstraint);
		
	}
	
	

	private void initMiddlePanel() {
		plusButton = new JButton("+");
		plusButton.setPreferredSize(new Dimension(61, 62));
		plusButton.setMinimumSize(new Dimension(61, 62));
		plusButton.addActionListener(this);

		minusButton = new JButton("-");
		minusButton.setPreferredSize(new Dimension(61, 62));
		minusButton.setMinimumSize(new Dimension(61, 62));
		minusButton.addActionListener(this);

		okButton = new JButton("OK");
		okButton.setPreferredSize(new Dimension(61, 126));
		okButton.setMinimumSize(new Dimension(61, 126));
		okButton.setActionCommand("okButton");
		okButton.addActionListener(this);

		middlePanel = new JPanel();
		middlePanel.setPreferredSize(new Dimension(61, 250));
		middlePanel.setMinimumSize(new Dimension(61, 250));
		GridBagLayout gbLayout = new GridBagLayout();
		gbLayout.columnWidths = new int[] { 61 };
		gbLayout.rowHeights = new int[] { 62, 62, 126 };
		gbLayout.columnWeights = new double[] { 1.0 };
		gbLayout.rowWeights = new double[] { 1.0, 1.0, 1.0 };
		middlePanel.setLayout(gbLayout);
		GridBagConstraints gbConstraint = new GridBagConstraints();
		gbConstraint.fill = GridBagConstraints.VERTICAL;
		gbConstraint.gridx = 0;
		gbConstraint.gridy = 0;
		middlePanel.add(plusButton, gbConstraint);
		gbConstraint.fill = GridBagConstraints.VERTICAL;
		gbConstraint.gridx = 0;
		gbConstraint.gridy = 1;
		middlePanel.add(minusButton, gbConstraint);
		gbConstraint.fill = GridBagConstraints.VERTICAL;
		gbConstraint.gridx = 0;
		gbConstraint.gridy = 2;
		gbConstraint.gridheight = 2;
		middlePanel.add(okButton, gbConstraint);
		gbConstraint.fill = GridBagConstraints.VERTICAL;
		gbConstraint.gridx = 1;
		gbConstraint.gridy = 0;
		gbConstraint.gridheight = 1;
		gbConstraint.anchor = GridBagConstraints.WEST;
		contentPane.add(middlePanel, gbConstraint);
	}

	private void initLeftScrollPane() {
		leftScrollPane = new JScrollPane();
		leftScrollPane.setPreferredSize(new Dimension(508, 250));
		leftScrollPane.setMinimumSize(new Dimension(508, 250));
		GridBagConstraints gbConstraint = new GridBagConstraints();
		gbConstraint.fill = GridBagConstraints.BOTH;
		gbConstraint.gridx = 0;
		gbConstraint.gridy = 0;
		contentPane.add(leftScrollPane, gbConstraint);
	}

	/**
	 * sets new JTable in leftscrollPane from RcMainFrame. Takes activated
	 * Profile Channels and Favorites
	 */
	public void initChannelTable() {
		List<Channel> channels;
		List<Channel> favChannels;
		if (rcController.getTvMemory().getProfilesSize() >= 0) {
			Profile userProfile = new Profile(rcController.getTvMemory()
					.getProfileAt(
							rcController.getTvMemory().getCurrentProfile()));
			channels = new ArrayList<Channel>(userProfile.getChannels());
			favChannels = new ArrayList<Channel>(userProfile.getFavorites());

			for (int i = 0; i < favChannels.size(); i++) {
				channels.add(0, favChannels.get(i));
			}

			Vector<String> columnNames = new Vector<String>();
			columnNames.addElement("순서");
			columnNames.addElement("채널");
			columnNames.addElement("프로그램");
			columnNames.addElement("회사");
			channelTable = new JTable(new MyTableModel(channels, columnNames));
			channelTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						ActionEvent event = new ActionEvent(okButton, 0,
								"okButton");
						actionPerformed(event);
					}
				}
			});
			channelTable.setFont(f121);
			channelTable.setRowHeight(30);
			for (int s = 0; s < Spaltenbreite.length; s++) {
				channelTable.getColumnModel().getColumn(s)
						.setPreferredWidth(Spaltenbreite[s]);
				if (s <= 1) {
					channelTable.getColumnModel().getColumn(s)
							.setMaxWidth(Spaltenbreite[s]);
				}
			}
			leftScrollPane.setViewportView(channelTable);
			channelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			channelTable.setRowSelectionInterval(0, 0);
			channelTable.getTableHeader().setReorderingAllowed(false);
			setVolumeSlider(rcController.getTvMemory().getVolumeLevel());
		}
	}

	/**
	 * sets volumeSlider to value
	 */
	public void setVolumeSlider(int value) {
		volumeSlider.setValue(value);
	}

	/**
	 * sets Format for TvScreenFrame corresponding to value
	 * 
	 * @param value
	 *            String 4:3, 16:9, 2,35:1
	 */
	public void setFormat(String value) {
		switch (value) {
		case "4:3":
			rcController.getTvScreenFrame().getMainPanel().setLocation(100, 0);
			rcController.getTvScreenFrame().getMainPanel()
					.setPreferredSize(new Dimension(800, 600));
			rcController.getTvScreenFrame().getMainPanel()
					.setSize(new Dimension(800, 600));
			break;
		case "16:9":
			rcController.getTvScreenFrame().getMainPanel().setLocation(0, 19);
			rcController.getTvScreenFrame().getMainPanel()
					.setPreferredSize(new Dimension(1000, 562));
			rcController.getTvScreenFrame().getMainPanel()
					.setSize(new Dimension(1000, 562));
			break;
		case "2,35:1":
			rcController.getTvScreenFrame().getMainPanel().setLocation(30, 100);
			rcController.getTvScreenFrame().getMainPanel()
					.setPreferredSize(new Dimension(940, 400));
			rcController.getTvScreenFrame().getMainPanel()
					.setSize(new Dimension(940, 400));
			break;
		default:
			rcController.getTvScreenFrame().getMainPanel().setLocation(0, 19);
			rcController.getTvScreenFrame().getMainPanel()
					.setPreferredSize(new Dimension(1000, 562));
			rcController.getTvScreenFrame().getMainPanel()
					.setSize(new Dimension(1000, 562));
		}
	}

	/**
	 * Default Close Operation with ConfirmDialog Save or not
	 * 
	 * @return is SystemExit(0)
	 */
	public int closeOperation() {
		int n = JOptionPane.showConfirmDialog(this,
				"현재 설정들을 저장하시겠습니까?", "종료",
				JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION == n) {
			rcController.serialize(rcController.getTvMemory(), "tvMemory.ser");
		}
		System.exit(0);
		return 0;
	}

	/**
	 * ActionListener from RcMainFrame
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer){
			++sec; 
            Calendar calendar2 = Calendar.getInstance(); 
            hour = calendar2.get(Calendar.HOUR_OF_DAY); 
            min = calendar2.get(Calendar.MINUTE); 
            sec = calendar2.get(Calendar.SECOND); 
            timeLabel.setText("현재 : " + hour + "시" + min + "분 " + sec + "초"); 
		}
		if (e.getSource() == muteButton) {
			mute();
		}
		if (e.getSource() == setupButton) {
			changeSettings();
		}
		if (e.getSource() == recButton) {
			startTimeshift(e);
		}
		if (e.getSource() == stopRecButton) {
			stopTimeshift(e);
		}
		if (e.getSource() == playButton) {
			playTimeshift(e);
		}
		if (e.getSource() == pipButton) {
			togglePip(e);
		}
		if (e.getSource() == myTimer) {
			long secondsPlaying = rcController.getRecordedSeconds();
			long seconds = secondsPlaying % 60;
			long minutesPlaying = secondsPlaying / 60;
			long minutes = minutesPlaying % 60;
			long hours = minutesPlaying / 60;
			String time;
			if (hours > 0) {
				time = String.format("%d:%02d:%02d", hours, minutes, seconds);
			} else {
				time = String.format("%d:%02d", minutes, seconds);
			}
			timeRecordedLabel.setText(time);
			offsetSlider.setMaximum((int) secondsPlaying);
		}
		if (e.getSource() == okButton) {
			playSelectedChannel(e);
		}
		if (e.getSource() == plusButton) {
			channelUp();
		}
		if (e.getSource() == minusButton) {
			channelDown();
		}
		if (e.getSource() == lastChannelButton) {
			switchToLastChannel();
		}
	}

	private void switchToLastChannel() {
		String lastChannel = rcController.getTvMemory().pullLastChannel();
		int i;
		for (i = 0; i < channelTable.getModel().getRowCount(); i++) {
			if (channelTable.getModel().getValueAt(i, 1).equals(lastChannel)) {
				break;
			}
		}
		channelTable.setRowSelectionInterval(i, i);
		ActionEvent event = new ActionEvent(okButton, 0, "lastChannel");
		actionPerformed(event);
	}

	private void channelDown() {
		if (channelTable.getSelectedRow() - 1 < 0) {
			channelTable.setRowSelectionInterval(channelTable.getModel()
					.getRowCount() - 1,
					channelTable.getModel().getRowCount() - 1);
		} else {
			channelTable.setRowSelectionInterval(
					channelTable.getSelectedRow() - 1,
					channelTable.getSelectedRow() - 1);
		}
		rcController.getTvMemory().setCurrentChannelMain(
				(String) channelTable.getModel().getValueAt(
						channelTable.getSelectedRow(), 1));
		ActionEvent event = new ActionEvent(okButton, 0, "minusButton");
		actionPerformed(event);
	}

	private void channelUp() {
		if (channelTable.getSelectedRow() + 1 >= channelTable.getModel()
				.getRowCount()) {
			channelTable.setRowSelectionInterval(0, 0);
		} else {
			channelTable.setRowSelectionInterval(
					channelTable.getSelectedRow() + 1,
					channelTable.getSelectedRow() + 1);
		}
		rcController.getTvMemory().setCurrentChannelMain(
				(String) channelTable.getModel().getValueAt(
						channelTable.getSelectedRow(), 1));
		ActionEvent event = new ActionEvent(okButton, 0, "plusButton");
		actionPerformed(event);
	}

	/**
	 * Activates Program given by selected row in ChannelTable
	 */
	public void playSelectedChannel() {
		ActionEvent event = new ActionEvent(okButton, 0, "okButton");
		actionPerformed(event);
	}

	private void playSelectedChannel(ActionEvent e) {
		String channel = (String) channelTable.getModel().getValueAt(
				channelTable.getSelectedRow(), 1);
		try {
			rcController.setChannel(channel, false);
		} catch (Exception e1) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		}
		if (e.getActionCommand().equals("okButton")) {
			rcController.getTvMemory().setCurrentChannelMain(channel);
		}
		channelTable.scrollRectToVisible(channelTable.getCellRect(
				channelTable.getSelectedRow(), 1, true));
	}

	private void togglePip(ActionEvent e) {
		if (rcController.getTvScreenFrame().getPipPanel().isVisible()) {
			rcController.setPictureInPicture(false);
		} else {
			try {
				rcController.setChannel((String) channelTable.getModel()
						.getValueAt(channelTable.getSelectedRow(), 1), true);
			} catch (Exception e1) {
				Logger.getLogger(RcMainFrame.class.getName()).log(
						Level.WARNING, null, e);
			}
		}
	}

	private void stopTimeshift(ActionEvent e) {
		try {
			rcController.recordTimeShift(false);
		} catch (Exception e1) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		}
		stopRecButton.setVisible(false);
		playButton.setVisible(false);
		offsetSlider.setVisible(false);
		recButton.setVisible(true);
		myTimer.stop();
		timeRecordedLabel.setVisible(false);
	}

	private void startTimeshift(ActionEvent e) {
		try {
			rcController.recordTimeShift(true);
		} catch (Exception e1) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		}
		rcController.setRecordingStartTime(rcController.now());
		recButton.setVisible(false);
		stopRecButton.setVisible(true);
		playButton.setVisible(true);
		offsetSlider.setVisible(true);
		timeRecordedLabel.setVisible(true);
		myTimer.start();
	}

	private void playTimeshift(ActionEvent e) {
		try {
			rcController.playTimeShift(true, offsetSlider.getValue());
		} catch (Exception e1) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		}
	}

	private void changeSettings() {
		if (rcController.getRcSetupFrame() == null) {
			rcController.setRcSetupFrame(new RcSetupFrame(rcController));
			rcController.getRcSetupFrame().setLocationRelativeTo(this);
		}
		rcController.getRcSetupFrame().setVisible(true);
	}

	private void mute() {
		if (volumeSlider.getValue() > 0) {
			rcController.getTvMemory().setVolumeLevel(volumeSlider.getValue());
			volumeSlider.setValue(0);
			volumeLabel2.setText(String.format("%s",volumeSlider.getValue()));
			if (volumeIconURL != null) {
				muteButton.setIcon(new ImageIcon(volumeIconURL));
			}
		} else {
			volumeSlider.setValue(rcController.getTvMemory().getVolumeLevel());
			if (muteIconURL != null) {
				muteButton.setIcon(new ImageIcon(muteIconURL));
			}
		}
	}

	/**
	 * ChangeListener from RcMainFrame for VolumeSlider
	 * 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == volumeSlider) {
			adjustVolume(e);
			volumeLabel2.setText(String.format("%s",volumeSlider.getValue()));
		}
	}

	private void adjustVolume(ChangeEvent e) {
		try {
			rcController.setVolume(volumeSlider.getValue());
			volumeLabel2.setText(String.format("%s",volumeSlider.getValue()));

		} catch (Exception e1) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		}
		if (volumeSlider.getValue() == 0) {
			if (volumeIconURL != null) {
				muteButton.setIcon(new ImageIcon(volumeIconURL));
			}
		} else {
			muteButton.setIcon(new ImageIcon(muteIconURL));
		}
	}
}
