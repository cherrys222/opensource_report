package report3;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RcController class is the Central class in the RemoteControl application. It
 * Delegates References to all other classes and is extended from TvElectronics
 * which encapsulates the TvLogic
 * 
 * @author moritz
 * 
 */
public class RcController extends TvElectronics {
	private RcMainFrame rcMainFrame;
	private TvScreenFrame tvScreenFrame;
	private RcSetupFrame rcSetupFrame;
	private TvMemory tvMemory;
	private long recordingStartTime;

	/**
	 * Constructor. Reads TvMemory from File
	 */
	public RcController(RcMainFrame mainframe, TvScreenFrame screen) {
		super(screen.getMainPanel(), screen.getPipPanel());
		rcMainFrame = mainframe;
		tvScreenFrame = screen;
		tvScreenFrame.setRcController(this);
		tvScreenFrame.setLocation(0, 0);
		tvScreenFrame.setVisible(true);
		tvScreenFrame.getPipPanel().setVisible(false);
		try {
			tvMemory = deserialize("tvMemory.ser");
		} catch (ClassNotFoundException | IOException e) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		}
	}

	/**
	 * Getter for TvScreenFrame
	 */
	public TvScreenFrame getTvScreenFrame() {
		return tvScreenFrame;
	}

	/**
	 * returns rcMainFrame
	 */
	public RcMainFrame getRcMainFrame() {
		return rcMainFrame;
	}

	/**
	 * returns tvMemory
	 */
	public TvMemory getTvMemory() {
		return tvMemory;
	}

	/**
	 * Setter for TvMemory
	 * 
	 * @param tvMemory
	 */
	public void setTvMemory(TvMemory tvMemory) {
		this.tvMemory = tvMemory;
	}

	/**
	 * returns rcSetupFrame. If Null creates new instance
	 */
	public RcSetupFrame getRcSetupFrame() {
		return rcSetupFrame;
	}

	/**
	 * Setter for RcSetupFrame
	 * 
	 * @param rcSetupFrame
	 */
	public void setRcSetupFrame(RcSetupFrame rcSetupFrame) {
		this.rcSetupFrame = rcSetupFrame;
	}

	/**
	 * sets recordingStartTime
	 */
	public void setRecordingStartTime(long recordingStartTime) {
		this.recordingStartTime = recordingStartTime;
	}

	/**
	 * returns recorded Time in Seconds
	 */
	public long getRecordedSeconds() {
		return this.now() - recordingStartTime;
	}

	/**
	 * Serializes Object obj into destination given by filename
	 * 
	 * @param obj
	 * @param fileName
	 */
	public void serialize(Object obj, String fileName) {
		try {
			FileOutputStream f = new FileOutputStream(fileName);
			ObjectOutputStream s = new ObjectOutputStream(f);
			try {
				s.writeObject(obj);
			} finally {
				s.close();
			}
		} catch (FileNotFoundException e) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		} catch (IOException e) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		}
	}

	/**
	 * Deserializes Object TvMemory from fileName given by source
	 * 
	 * @param fileName
	 * @return is TvMemory object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public TvMemory deserialize(String fileName) throws IOException,
			ClassNotFoundException {
		TvMemory temp = null;
		File newFile = new File(fileName);
		if (newFile.exists()) {
			FileInputStream f = new FileInputStream(fileName);
			ObjectInputStream s = new ObjectInputStream(f);
			try {
				temp = (TvMemory) s.readObject();
			} finally {
				s.close();
			}
		}
		return temp;
	}
}

