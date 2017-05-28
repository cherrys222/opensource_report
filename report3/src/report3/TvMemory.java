package report3;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Datastructure Class of RemoteControl
 * Entityclass, Class which contains persitent Information
 */

public class TvMemory implements Serializable {
	transient private RcController rcController;
	transient private Deque<String> lastChannels;
	private int volumeLevel, brightnessValue, contrastValue;
	private int currentProfile;
	private List<Profile> profiles;
	private String currentChannelMain, currentFormat;
	
	/**
	 * Constructor TvMemory
	 * @param control is reference to RcController Class
	 * must exist before init TvMemory 
	 */
	public TvMemory(RcController control) {
		lastChannels = new ArrayDeque<String>();
		rcController = control;
		currentChannelMain = "8a";
		profiles = new ArrayList<Profile>();
		brightnessValue = 50;
		contrastValue = 50;
		currentFormat = "16:9";
	}

	/**
	 * Setter CurrentChannelMain. Currently Playing
	 * @param currentChannelMain
	 */
	public void setCurrentChannelMain(String currentChannelMain) {
		this.addLastChannel(this.currentChannelMain);
		this.currentChannelMain = currentChannelMain;
	}
	
	/**
	 * if TvMemory has been deserialized from Saved Mode
	 * this default Attributes are Missing
	 * @param control
	 */
	public void setMissingAttributes(RcController control) {
		this.rcController = control;
		lastChannels = new ArrayDeque<String>();
	}

	/**
	 * Getter for ProfilesList
	 * @return List<Profile>
	 */
	public List<Profile> getProfiles() {
		return profiles;
	}

	/**
	 * Getter for CurrentProfile
	 * @return int
	 */
	public int getCurrentProfile() {
		return currentProfile;
	}

	/**
	 * Setter for CurrentProfile
	 * @param currentProfile
	 */
	public void setCurrentProfile(int currentProfile) {
		this.currentProfile = currentProfile;
	}

	/**
	 * Getter for BrightnessLevel
	 * @return int
	 */
	public int getBrightnessValue() {
		return brightnessValue;
	}

	/**
	 * Setter for BrightnessLevel
	 * @param brightnessValue
	 */
	public void setBrightnessValue(int brightnessValue) {
		this.brightnessValue = brightnessValue;
	}

	/**
	 * Getter for ContrastValue
	 * @return int
	 */
	public int getContrastValue() {
		return contrastValue;
	}

	/**
	 * Setter for ContrastValue
	 * @param contrastValue
	 */
	public void setContrastValue(int contrastValue) {
		this.contrastValue = contrastValue;
	}

	/**
	 * Getter for Currently selected Format
	 * @return String
	 */
	public String getCurrentFormat() {
		return currentFormat;
	}

	/**
	 * Setter for CurrentFormat
	 * @param currentFormat
	 */
	public void setCurrentFormat(String currentFormat) {
		this.currentFormat = currentFormat;
	}

	/**
	 * saves max 5 last channels. adds String to List of saved Channels
	 * @param channel
	 */
	public void addLastChannel(String channel) {
		while (lastChannels.size() >= 3) {
			lastChannels.removeLast();
		}
		lastChannels.addFirst(channel);
	}
	
	/**
	 * return but does not delete lastAdded Channel
	 * @return String
	 */
	public String peekLastChannel() {
		return lastChannels.getFirst();
	}
	
	/**
	 * returns last added channel and puts it to end of queue
	 * @return String
	 */
	public String pullLastChannel() {
		lastChannels.addLast(lastChannels.removeFirst());
		return lastChannels.getLast();
	}
	
	/**
	 * Getter for VolumeLevel
	 * @return int
	 */
	public int getVolumeLevel() {
		return volumeLevel;
	}

	/**
	 * Setter for VolumeLevel
	 * @param volumeLevel
	 */
	public void setVolumeLevel(int volumeLevel) {
		this.volumeLevel = volumeLevel;
	}
	
	/**
	 * Getter for Number of Profiles in List
	 * @return int
	 */
	public int getProfilesSize() {
		return profiles.size();
	}
	
	/**
	 * Get Profile at Position i
	 * @param i
	 * @return Profile
	 */
	public Profile getProfileAt(int i) {
		return profiles.get(i);
	}
	
	/**
	 * Adds new Profile to List of Profiles with default ChannelList
	 */
	public void addProfile() {
		profiles.add(new Profile(rcController.scanChannels()));
		profiles.get(profiles.size()-1).setName("프로필"+profiles.size());
	}
	
	/**
	 * removes Profile at position pos from List of Profiles
	 * @param pos
	 */
	public void removeProfile(int pos) {
		System.out.println(profiles.get(pos).getName()+" 삭제");
		profiles.remove(pos);
	}
}

/**
 * Class Profile to save Channels and Favorites
 * @author moritz
 *
 */
class Profile implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<Channel> channels;
	private List<Channel> favorites;
	private String name;
	
	/**
	 * Constructor with 
	 * @param channelsScanned is List<Channels>
	 */
	public Profile(List<Channel> channelsScanned) {
		channels = channelsScanned;
		name= "프로필";
		favorites = new ArrayList<Channel>();
	}
	
	/**
	 * Copy Constructor
	 * @param inputProfile is Profile
	 */
	public Profile(Profile inputProfile){
		this.setChannels(inputProfile.getChannels());
		this.setFavorites(inputProfile.getFavorites());
	}
	
	/**
	 * Adds Channel to ChannelList
	 * @param inputChannel
	 */
	public void addChannel(Channel inputChannel){
		channels.add(inputChannel);
	}
	
	/**
	 * Sets ChannelsList to List given by 
	 * @param channels
	 */
	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
	
	/**
	 * Getter for Name of Profile
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for Name of Profile
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets FavoritesList to List given by
	 * @param favorites
	 */
	public void setFavorites(List<Channel> favorites) {
		this.favorites = favorites;
	}
	
	/**
	 * Getter for ChannelList
	 * @return List<Channel>
	 */
	public List<Channel> getChannels() {
		return channels;
	}

	/**
	 * Getter for FavoritesList
	 * @return List<Favorites>
	 */
	public List<Channel> getFavorites() {
		return favorites;
	}
}


/**
 * Class Channel with attributes out of channelSearch File
 * @author moritz
 *
 */
class Channel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String Frequency, Channel, Quality, Program, Supplier;

	/**
	 * Constructor with attributes from ChannelSearchFile
	 * @param frequency
	 * @param channel
	 * @param quality
	 * @param program
	 * @param supplier
	 */
	public Channel(String frequency, String channel, String quality,
			String program, String supplier) {
		super();
		Frequency = frequency;
		Channel = channel;
		Quality = quality;
		Program = program;
		Supplier = supplier;
	}
	
	/**
	 * Copy Constructor
	 * @param aChannel
	 */
	public Channel(Channel aChannel) {
		this.Frequency = new String(aChannel.getFrequency());
		this.Channel = new String(aChannel.getChannel());
		this.Quality = new String(aChannel.getQuality());
		this.Program = new String(aChannel.getProgram());
		this.Supplier = new String(aChannel.getSupplier());
	}
	
	/**
	 * Getter for Quality
	 * @return String
	 */
	public String getQuality() {
		return Quality;
	}
	
	/**
	 * Getter for Frequency
	 * @return String
	 */
	public String getFrequency() {
		return Frequency;
	}

	/**
	 * Getter for Channel
	 * @return String
	 */
	public String getChannel() {
		return Channel;
	}

	/**
	 * Getter for Programm
	 * @return String
	 */
	public String getProgram() {
		return Program;
	}

	/**
	 * Getter for Supplier
	 * @return String
	 */
	public String getSupplier() {
		return Supplier;
	}
	
}
