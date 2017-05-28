package report3;


import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

/**
 * Frame with TabbedPanes displayed for Setup of RemoteControl
 * Additional Adjustments for User
 */

public class RcSetupFrame extends JFrame implements ActionListener {

	private RcController rcController;
	private JPanel contentPane, adjustPanel, favoritesPanel, profilePanel, confirmPanel, sortPanel;
	private JTabbedPane tabbedPane;
	private JButton okButton, cancelButton;
	private JSlider brightnessSlider,contrastSlider;
	private JRadioButton format169RadioButton, format43RadioButton, format2351RadioButton;
	private ButtonGroup formatButtonGroup;
	private String screenFormat = "16:9";
	private JButton activateProfButton;
	private JButton deleteProfButton;
	private JButton editProfButton,searchChannelButton;
	private JButton newProfButton,refreshChannelButton, deleteChannelButton;
	private JButton resetAllButton, addFavButton, deleteFavButton;
	private JTable channelTable;
	private JScrollPane favScrollPane;
	private JTable favoritesTable;
	private JScrollPane profScrollPane, sortScrollPane;
	private JTable profTable;
	private JLabel resetLabel;
	private int[] SpaltenbreiteSortTable = { 30, 70, 100};
	private int[] SpaltenbreiteFavTable = { 30, 200};
	private Vector<String> columnNamesSort, columnNamesProf, columnNamesFav;


	/**
	 * Constructor RcSetupFrame. Initialization of Panels with values from TvMemory
	 */
	public RcSetupFrame(RcController control) {
		this.rcController = control;
		this.setTitle("옵션");
		
		columnNamesSort = new Vector<String>();
		columnNamesSort.add("순서");
		columnNamesSort.add("채널");
		columnNamesSort.add("새로운 위치");
		
		columnNamesProf = new Vector<String>();
		columnNamesProf.add("프로필");

		columnNamesFav = new Vector<String>();
		columnNamesFav.add("순서");
		columnNamesFav.add("채널");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 486, 503);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(15, 5, 455, 426);
		contentPane.add(tabbedPane);
		
		initConfirmPanel();

		initAdjustPanel();

		initSortPanel();

		initfavoritesPanel();

		initProfilesPanel();

		changeFormat(rcController.getTvMemory().getCurrentFormat());

		adjustBrightness(rcController.getTvMemory().getBrightnessValue());

		adjustContrast(rcController.getTvMemory().getContrastValue());
	}

	/**
	 * confirm panel with ok and cancel buttons in it
	 */
	private void initConfirmPanel() {
		confirmPanel = new JPanel();
		confirmPanel.setBounds(15, 431, 455, 33);
		contentPane.add(confirmPanel);
		confirmPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 5));
		
		okButton = new JButton("확인");
		okButton.setPreferredSize(new Dimension(81, 25));
		confirmPanel.add(okButton);
		okButton.addActionListener(this);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		confirmPanel.add(horizontalStrut);
		
		cancelButton = new JButton("취소");
		okButton.setPreferredSize(new Dimension(81, 25));
		confirmPanel.add(cancelButton);
		cancelButton.addActionListener(this);
	}
	
	/**
	 * adjust panel with brightness,contrast and screen size 
	 */
	private void initAdjustPanel(){
		adjustPanel = new JPanel();
		adjustPanel.setLayout(null);
		adjustPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		tabbedPane.addTab("화면조절", null, adjustPanel, null);

		format169RadioButton = new JRadioButton("16:9");
		format169RadioButton.setBounds(174, 49, 54, 23);
		format169RadioButton.addActionListener(this);
		format169RadioButton.setActionCommand("16:9");

		format43RadioButton = new JRadioButton("4:3");
		format43RadioButton.setBounds(64, 49, 46, 23);
		format43RadioButton.addActionListener(this);
		format43RadioButton.setActionCommand("4:3");
		
		format2351RadioButton = new JRadioButton("2,35:1");
		format2351RadioButton.setBounds(292, 49, 67, 23);
		format2351RadioButton.addActionListener(this);
		format2351RadioButton.setActionCommand("2,35:1");

		formatButtonGroup = new ButtonGroup();
		formatButtonGroup.add(format169RadioButton);
		formatButtonGroup.add(format43RadioButton);
		formatButtonGroup.add(format2351RadioButton);
		format169RadioButton.setSelected(true);

		brightnessSlider = new JSlider();
		brightnessSlider.setBounds(264, 17, 150, 62);
		brightnessSlider.setValue(30);

		contrastSlider = new JSlider();
		contrastSlider.setBounds(53, 17, 150, 62);
		contrastSlider.setValue(30);

		JPanel compFormatPanel = new JPanel(false);
		compFormatPanel.setBounds(12, 115, 426, 104);
		compFormatPanel.setLayout(null);
		compFormatPanel.add(format43RadioButton);
		compFormatPanel.add(format169RadioButton);
		compFormatPanel.add(format2351RadioButton);
		compFormatPanel.setBorder(BorderFactory.createTitledBorder("화면비율"));
		adjustPanel.add(compFormatPanel);
		
		JPanel compColorPanel = new JPanel(false);
		compColorPanel.setBounds(12, 12, 426, 91);
		ImageIcon birghtnessImage = new ImageIcon(RcSetupFrame.class.getResource("/icons/brightness_Icon.png"));
		JLabel brightnessLabel = new JLabel(birghtnessImage);
		brightnessLabel.setBounds(218, 17, 41, 62);
		ImageIcon contrastImage = new ImageIcon(RcSetupFrame.class.getResource("/icons/contrast_Icon.png"));
		JLabel contrastLabel = new JLabel(contrastImage);
		contrastLabel.setBounds(12, 17, 41, 62);
		compColorPanel.setLayout(null);
		compColorPanel.add(brightnessSlider);
		compColorPanel.add(brightnessLabel);
		compColorPanel.add(contrastSlider);
		compColorPanel.add(contrastLabel);
		compColorPanel.setBorder(BorderFactory.createTitledBorder("화면밝기"));
		adjustPanel.add(compColorPanel);
	}
	
	/**
	 * panel for the sort/change channel table
	 */
	private void initSortPanel() {
		sortPanel = new JPanel();
		tabbedPane.addTab("채널 순서변경", null, sortPanel, null);
		sortPanel.setLayout(null);
		
		sortScrollPane = new JScrollPane();
		sortScrollPane.setBounds(12, 12, 297, 375);
		sortPanel.add(sortScrollPane);
		
		channelTable = new JTable();
		sortScrollPane.setViewportView(channelTable);
		
		refreshChannelButton = new JButton("저장");
		refreshChannelButton.setBounds(321, 362, 117, 25);
		sortPanel.add(refreshChannelButton);
		refreshChannelButton.addActionListener(this);
		
		deleteChannelButton = new JButton("삭제");
		deleteChannelButton.setBounds(321, 325, 117, 25);
		sortPanel.add(deleteChannelButton);
		deleteChannelButton.addActionListener(this);
		
		searchChannelButton = new JButton("재검색");
		searchChannelButton.setBounds(321, 288, 117, 25);
		sortPanel.add(searchChannelButton);
		searchChannelButton.addActionListener(this);
		
		refreshSortTable();
	}
	
	/**
	 * panel for the favorite channels with buttons
	 */
	private void initfavoritesPanel() {
		favoritesPanel = new JPanel();
		tabbedPane.addTab("즐겨찾기", null, favoritesPanel, null);
		favoritesPanel.setLayout(null);
		
		favScrollPane = new JScrollPane();
		favScrollPane.setBounds(12, 12, 426, 184);
		favoritesPanel.add(favScrollPane);
		
		favoritesTable = new JTable();
		favScrollPane.setViewportView(favoritesTable);
		
		addFavButton = new JButton("추가");
		addFavButton.setBounds(192, 208, 117, 25);
		favoritesPanel.add(addFavButton);
		addFavButton.addActionListener(this);
		
		deleteFavButton = new JButton("삭제");
		deleteFavButton.setBounds(321, 208, 117, 25);
		favoritesPanel.add(deleteFavButton);
		deleteFavButton.addActionListener(this);
		
		refreshFavTable();
	}

	/**
	 * profile panel with buttons
	 */
	private void initProfilesPanel() {
		profilePanel = new JPanel();
		tabbedPane.addTab("프로필", null, profilePanel, null);
		profilePanel.setLayout(null);
		
		activateProfButton = new JButton("적용");
		activateProfButton.setBounds(341, 208, 91, 25);
		profilePanel.add(activateProfButton);
		activateProfButton.addActionListener(this);
		
		deleteProfButton = new JButton("삭제");
		deleteProfButton.setBounds(233, 208, 91, 25);
		profilePanel.add(deleteProfButton);
		deleteProfButton.addActionListener(this);
		
		editProfButton = new JButton("수정");
		editProfButton.setBounds(125, 208, 91, 25);
		profilePanel.add(editProfButton);
		editProfButton.addActionListener(this);
		
		newProfButton = new JButton("신규");
		newProfButton.setBounds(17, 208, 91, 25);
		profilePanel.add(newProfButton);
		newProfButton.addActionListener(this);
		
		resetAllButton = new JButton("기본값");
		resetAllButton.setBounds(12, 314, 117, 25);
		profilePanel.add(resetAllButton);
		resetAllButton.addActionListener(this);
		
		resetLabel = new JLabel("주의:기본값으로 변경됩니다.");
		resetLabel.setBounds(161, 319, 218, 15);
		profilePanel.add(resetLabel);
		
		profScrollPane = new JScrollPane();
		profScrollPane.setBounds(12, 12, 426, 184);
		profilePanel.add(profScrollPane);
		
		profTable = new JTable();
		profScrollPane.setViewportView(profTable);
		
		refreshProfTable();
	}

	private void refreshProfTable() {
		List<Profile> actProfiles = new ArrayList<Profile>(rcController.getTvMemory().getProfiles()); 
		List<String> showProfilesList = new ArrayList<String>();
		for (int i = 0; i < actProfiles.size(); i++) {
			showProfilesList.add(actProfiles.get(i).getName());
		}
		setProfTable(new JTable(new ProfilesTableModel(showProfilesList, columnNamesProf)));
	}
	
	/**
	 * sets new JTable to ProfilesScrollPane
	 */
	public void setProfTable(JTable profileList) {
		this.profTable = profileList;
		profScrollPane.setViewportView(profTable);
		profTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		profTable.setRowSelectionInterval(rcController.getTvMemory().getCurrentProfile(), rcController.getTvMemory().getCurrentProfile());
		profTable.getTableHeader().setReorderingAllowed(false);
	}
	
	private void refreshFavTable() {
		List<Channel> actFavorites = new ArrayList<Channel>(rcController.getTvMemory().getProfileAt(rcController.getTvMemory().getCurrentProfile()).getFavorites()); 	
		setFavTable(new JTable(new FavoritesTableModel(actFavorites, columnNamesFav)));
	}
	
	/**
	 * sets new JTable in Favorites ScrollPane
	 * @param table
	 */
	public void setFavTable(JTable table) {
		this.favoritesTable = table;
		for (int s = 0; s < SpaltenbreiteFavTable.length; s++) {
			favoritesTable.getColumnModel().getColumn(s)
					.setPreferredWidth(SpaltenbreiteFavTable[s]);
		}
		favScrollPane.setViewportView(favoritesTable);
		favoritesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		favoritesTable.getTableHeader().setReorderingAllowed(false);
	}

	private void refreshSortTable() {
		List<Channel> actProfileChannels = new ArrayList<Channel>(rcController.getTvMemory().getProfileAt(rcController.getTvMemory().getCurrentProfile()).getChannels()); 
		setChannelTable(new JTable(new NewSortTableModel(actProfileChannels, columnNamesSort)));
	}
	
	/**
	 * sets new JTable to ChannelSortPane
	 */
	public void setChannelTable(JTable channelTable) {
		this.channelTable = channelTable;
		for (int s = 0; s < SpaltenbreiteSortTable.length; s++) {
			channelTable.getColumnModel().getColumn(s)
					.setPreferredWidth(SpaltenbreiteSortTable[s]);
		}
		sortScrollPane.setViewportView(channelTable);
		channelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		channelTable.setRowSelectionInterval(0, 0);
		channelTable.getTableHeader().setReorderingAllowed(false);
		channelTable.setDragEnabled(true);
		channelTable.setDropMode(DropMode.INSERT_ROWS);
		channelTable.setTransferHandler(new TableRowTransferHandler(channelTable)); 
	}

	/**
	 * sets private attribute screenFormat and selects RadioButton according to param 
	 * @param value
	 * String 4:3, 16:9, 2,35:1
	 */
	public void changeFormat(String value){
		this.screenFormat = value;
		switch(screenFormat){
		case "4:3" :
			format43RadioButton.setSelected(true);
			break;
		case "16:9" :
			format169RadioButton.setSelected(true);
			break;
		case "2,35:1" :
			format2351RadioButton.setSelected(true);
			break;
		default:
			format169RadioButton.setSelected(true);

		}
	}
	
	/**
	 * sets BrightnessSlider to value
	 * @param value
	 */
	public void adjustBrightness(int value){
		this.brightnessSlider.setValue(value);
	}
	
	/**
	 * Sets ContrastSlider to value
	 * @param value
	 */
	public void adjustContrast(int value){
		this.contrastSlider.setValue(value);
	}

	/**
	 * JOptionPane for Confirmation of ResetDialog
	 * @return -1 if user chooses no as answer
	 */
	public int questionPane(){
		int answer =  JOptionPane.showConfirmDialog(
				this,
				"기본값으로 변경하시겠습니까?",
				"종료",
				JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION == answer) {
			return answer;
		}
		return -1;
	}

	/**
	 * ActionListener from RcSetupFrame
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addFavButton)) {
			addFavoriteChannel();
		}
		if (e.getSource().equals(deleteFavButton)) {
			deleteFavoriteChannel();	
		}
		if (e.getSource().equals(newProfButton)) {
			createProfile(); 
		}
		if (e.getSource().equals(activateProfButton)) {
			selectProfile();
		}
		if (e.getSource().equals(deleteProfButton)) {
			removeProfile();
		}
		if (e.getSource().equals(editProfButton)) {		
			editProfile();
		}
		if (e.getSource().equals(deleteChannelButton)) {
			deleteChannel();
		}
		if (e.getSource().equals(searchChannelButton)) {
			startChannelSearch();
		}
		if(e.getSource().equals(refreshChannelButton)) {
			refreshChannelList();
		}
		if(e.getSource().equals(resetAllButton)){
			resetSettings();
		}
		if(e.getSource().equals(cancelButton)){
			exitWoSaving();
		}
		if (e.getActionCommand().equals("16:9")) {
			screenFormat = "16:9";
		}
		if (e.getActionCommand().equals("4:3")) {
			screenFormat = "4:3";
		}
		if (e.getActionCommand().equals("2,35:1")) {
			screenFormat = "2,35:1";
		}
		if(e.getSource().equals(okButton)){
			exitWSaving();
		}
	}

	private void exitWSaving() {
		switch(screenFormat){
		case "4:3" :
			rcController.getTvScreenFrame().getMainPanel().setLocation(100, 0);
			rcController.getTvScreenFrame().getMainPanel().setPreferredSize(new Dimension(800, 600));
			rcController.getTvScreenFrame().getMainPanel().setSize(new Dimension(800, 600));
			break;
		case "16:9":
			rcController.getTvScreenFrame().getMainPanel().setLocation(0, 19);
			rcController.getTvScreenFrame().getMainPanel().setPreferredSize(new Dimension(1000, 562));
			rcController.getTvScreenFrame().getMainPanel().setSize(new Dimension(1000, 562));
			break;
		case "2,35:1":
			rcController.getTvScreenFrame().getMainPanel().setLocation(30, 100);
			rcController.getTvScreenFrame().getMainPanel().setPreferredSize(new Dimension(940, 400));
			rcController.getTvScreenFrame().getMainPanel().setSize(new Dimension(940, 400));
			break;
		default:
			rcController.getTvScreenFrame().getMainPanel().setLocation(0, 19);
			rcController.getTvScreenFrame().getMainPanel().setPreferredSize(new Dimension(1000, 562));
			rcController.getTvScreenFrame().getMainPanel().setSize(new Dimension(1000, 562));
		}
		
		rcController.getTvMemory().setBrightnessValue(brightnessSlider.getValue());
		rcController.getTvMemory().setContrastValue(contrastSlider.getValue());
		rcController.getTvMemory().setCurrentFormat(screenFormat);

		List<Channel> favoritesList = ((FavoritesTableModel)favoritesTable.getModel()).getChannelList();
		rcController.getTvMemory().getProfileAt(rcController.getTvMemory().getCurrentProfile()).setFavorites(favoritesList);

		List<Channel> channelList = ((NewSortTableModel)channelTable.getModel()).getChannelList();
		rcController.getTvMemory().getProfileAt(rcController.getTvMemory().getCurrentProfile()).setChannels(channelList);
		rcController.getRcMainFrame().initChannelTable();

		refreshProfTable();

		setVisible(false);
		dispose();
	}

	private void exitWoSaving() {
		brightnessSlider.setValue(30);
		contrastSlider.setValue(30);
		format169RadioButton.setSelected(true);
		
		refreshSortTable();
		refreshFavTable();
		refreshProfTable();
		setVisible(false);
		dispose();
	}

	/**
	 * resets all brightness,contrast and screen format 
	 * it also refills the channel list in sort channels table,
	 * also all of the favorite channels of the user get deleted
	 */
	private void resetSettings() {
		int userChoice = questionPane();
		if(userChoice != -1){
			
			brightnessSlider.setValue(50);
			contrastSlider.setValue(50);
			format169RadioButton.setSelected(true);

			screenFormat = "16:9";

			int curentProf =  rcController.getTvMemory().getCurrentProfile();

			rcController.getTvMemory().getProfileAt(curentProf).setChannels(rcController.scanChannels());
			rcController.getRcMainFrame().initChannelTable();

			((NewSortTableModel) channelTable.getModel()).resetTableModel(rcController.getTvMemory().getProfileAt(curentProf).getChannels());

			((FavoritesTableModel)favoritesTable.getModel()).clearFavorites();

			List<Channel> clearedList = ((FavoritesTableModel)favoritesTable.getModel()).getChannelList();

			((FavoritesTableModel)favoritesTable.getModel()).resetTableModel(clearedList);

			refreshProfTable();
		}
	}

	private void refreshChannelList() {
		List<Channel> channelList = ((NewSortTableModel)channelTable.getModel()).getChannelList();
		rcController.getTvMemory().getProfileAt(rcController.getTvMemory().getCurrentProfile()).setChannels(channelList);
		rcController.getRcMainFrame().initChannelTable();
		setChannelTable(channelTable);
	}

	private void startChannelSearch() {
		ScanChannelsDialog scan = new ScanChannelsDialog(rcController);
		scan.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		scan.setLocationRelativeTo(this);
		scan.setVisible(true);
		((NewSortTableModel) channelTable.getModel()).resetTableModel(rcController.getTvMemory().getProfileAt(rcController.getTvMemory().getCurrentProfile()).getChannels());
		channelTable.updateUI();
	}

	/**
	 * Deletes Channel from ChannelList
	 * @Bug
	 * the problem here is that if we delete a channel form sort channel table
	 * and the channel was/is saved as a favorite channel it stays in the favorites table
	 * although it was deleted from the current channels list
	 */
	private void deleteChannel() {
		Object channelName = null;

		if (channelTable.getModel().getRowCount() > 0) {
			channelName = channelTable.getValueAt(channelTable.getSelectedRow(), 1);
			((NewSortTableModel) channelTable.getModel()).removeRow(channelTable.getSelectedRow());
			channelTable.updateUI();
		}
		List<Channel> favoriteChannels = ((FavoritesTableModel)favoritesTable.getModel()).getChannelList();	

		if(channelName != null){
			for(int i  = 0;i<favoriteChannels.size();i++ ){
				if(favoriteChannels.get(i).getProgram() == channelName.toString()){
					((FavoritesTableModel)favoritesTable.getModel()).removeRow(i);

				}
			}
		}
	}

	private void editProfile() {
		String newName = JOptionPane.showInputDialog(this,
				"새로운 프로필이름을 적으세요:");
		if (newName != null) {
			if (!newName.equals("")) {
				boolean isUsed = false;
				for (int i = 0; i < profTable.getRowCount(); i++) {
					if (profTable.getValueAt(i, 0).equals(newName)) {
						isUsed = true;
					}
				}
				if (!isUsed) {
					rcController.getTvMemory().getProfileAt(profTable.getSelectedRow()).setName(newName);
					refreshProfTable();
				}
			}
		}
	}

	/**
	 * Removes Profile from ProfileList
	 * if the profile that we want to delete is activated and is not the only profile,		
	 * than it will activate the next profile and delete the current one	
	 */
	private void removeProfile() {
		if (profTable.getRowCount() > 1) {	
			if (rcController.getTvMemory().getProfileAt(rcController.getTvMemory().getCurrentProfile()).getName().equals(profTable.getValueAt(profTable.getSelectedRow(), 0))) {
				rcController.getTvMemory().removeProfile(profTable.getSelectedRow());
				rcController.getTvMemory().setCurrentProfile(0);
				refreshProfTable();
				profTable.setRowSelectionInterval(0, 0);
				activateProfButton.doClick();
			} else {
				rcController.getTvMemory().removeProfile(profTable.getSelectedRow());
				refreshProfTable();
				rcController.getTvMemory().setCurrentProfile(profTable.getSelectedRow());
			}
		}
		else {
			System.out.println("마지막 프로필은 삭제가 불가능합니다.");
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.INFO, "마지막 프로필은 삭제가 불가능합니다.");
			JOptionPane.showMessageDialog(this,"마지막 프로필은 삭제가 불가능합니다.");
		}
	}

	private void selectProfile() {
		rcController.getTvMemory().setCurrentProfile(profTable.getSelectedRow());
		
		refreshSortTable();

		refreshFavTable();
		
		JOptionPane.showMessageDialog(this, "메인 프로필을 '"+rcController.getTvMemory().getProfileAt(rcController.getTvMemory().getCurrentProfile()).getName()+"'로 적용");
	}

	private void createProfile() {
		rcController.getTvMemory().addProfile();
		refreshProfTable();
	}

	private void deleteFavoriteChannel() {
		if (favoritesTable.getRowCount() > 0 && favoritesTable.getSelectedRow() >= 0) {
			((FavoritesTableModel)favoritesTable.getModel()).removeRow(favoritesTable.getSelectedRow());
		favoritesTable.updateUI();
		}
	}

	/**
	 * create a JOptionPane window with a list of all the channels a profile has
	 * saves the user's choice in newFavorite
	 */
	private void addFavoriteChannel() {
		List<Channel> channelTempList = new ArrayList<Channel>(((NewSortTableModel)channelTable.getModel()).getChannelList());
		Vector<String> favTempList = new Vector<String>();
		for (int i = 0; i < channelTempList.size(); i++) {
			favTempList.add(channelTempList.get(i).getProgram());
		}
		String newFavorite = (String) JOptionPane.showInputDialog(this, "즐겨찾기에 추가할 채널을 선택하세요", "즐겨찾기"
				,JOptionPane.QUESTION_MESSAGE,null, favTempList.toArray(), favTempList.get(0));
		if (newFavorite != null) {
			for (int i = 0; i < channelTempList.size(); i++) {
				if (channelTempList.get(i).getProgram().equals(newFavorite)) {
					((FavoritesTableModel)favoritesTable.getModel()).addRow(channelTempList.get(i));
					favoritesTable.updateUI();
					break;
				}
			}
		}
	}
}

