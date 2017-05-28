package report3;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

/**
 * Diese Klasse kapselt und simuliert die Audio- und Video-Elektronik des
 * Fernsehers. Steuerbefehle werden simuliert durch Ausgabe auf die Konsole.
 * 
 * @author Bernhard Kreling
 * @version 1.0
 * @version 1.1 public now()
 */
public class TvElectronics {

	protected JPanel mainDisplay;
	protected JPanel pipDisplay;
	private boolean isRecording; // der TimeShift-Recorder nimmt momentan auf
	private long recordingStartTime; // zu diesem Zeitpunkt hat die
										// TimeShift-Aufnahme begonnen (in
										// Sekunden seit 1.1.1970)

	/**
	 * Der Konstruktur uebernimmt Referenzen auf die beiden JPanel-Objekte, die
	 * die Displays repraesentieren.
	 * 
	 * @param mainDisplay
	 *            dieses Panel repraesentiert das Haupt-Display
	 * @param pipDisplay
	 *            dieses Panel repraesentiert das PictureInPicture-Display
	 */
	TvElectronics(JPanel mainDisplay, JPanel pipDisplay) {
		this.mainDisplay = mainDisplay;
		this.pipDisplay = pipDisplay;
		this.isRecording = false;
		this.recordingStartTime = 0;
	}

	/**
	 * Liefert den aktuellen Zeitpunkt.
	 * 
	 * @return die aktuelle Zeit in Sekunden seit 1.1.1970
	 */
	public long now() {
		return Calendar.getInstance().getTimeInMillis() / 1000;
	}

	/**
	 * Fuehrt den Kanalscan aus und liefert die verfuegbaren Kanaele. Bei
	 * doppelt gelisteten Kanaelen wird der mit der besseren Qualitaet gewaehlt.
	 * 
	 * @return die Daten aus Kanalscan.csv
	 */
	public List<Channel> scanChannels() {
		List<Channel> channels = new ArrayList<Channel>();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				TvMemory.class.getResourceAsStream("/saves/Kanalscan.csv")));
		try {
			String line;
			Map<String, Channel> tempMap = new HashMap<String, Channel>();
			while ((line = in.readLine()) != null) {
				String[] splitted = line.split(";");
				if (splitted.length == 5 && !(splitted[0].equals("Frequenz"))) {
					if (tempMap.containsKey(splitted[3])) {
						if (Integer.parseInt(tempMap.get(splitted[3])
								.getQuality()) < Integer.parseInt(splitted[2])) {
							tempMap.put(splitted[3], new Channel(splitted[0],
									splitted[1], splitted[2], splitted[3],
									splitted[4]));
						}
					} else {
						tempMap.put(splitted[3], new Channel(splitted[0],
								splitted[1], splitted[2], splitted[3],
								splitted[4]));
					}
				}
			}
			in.close();
			for (Map.Entry<String, Channel> entry : tempMap.entrySet()) {
				channels.add(entry.getValue());
			}

		} catch (IOException e) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		}

		System.out.println("All channels scanned");
		Logger.getLogger(RcMainFrame.class.getName()).log(Level.INFO,
				"All channels scanned");
		return channels;
	}

	/**
	 * Waehlt einen Kanal fuer die Wiedergabe aus.
	 * 
	 * @param channel
	 *            Kanalnummer als Zahl im Bereich 1..99 gefolgt von einem
	 *            Buchstaben a..d (vgl. Kanalscan.csv)
	 * @param forPiP
	 *            true: Wiedergabe im PictureInPicture-Display; false:
	 *            Wiedergabe im Haupt-Display
	 * @throws Exception
	 *             wenn der Wert von "channel" nicht gueltig ist
	 */
	public void setChannel(String channel, boolean forPiP) throws Exception {
		String errmsg = "Illegal format for channel: " + channel;
		int channelNumber;
		try {
			channelNumber = Integer.parseInt(channel.substring(0,
					channel.length() - 1));
		} catch (NumberFormatException n) {
			throw new Exception(errmsg);
		}
		String subChannel = channel.substring(channel.length() - 1,
				channel.length());
		if (channelNumber < 1 || channelNumber > 99
				|| new String("abcd").indexOf(subChannel) < 0)
			throw new Exception(errmsg);
		System.out.println((forPiP ? "PiP" : "Main") + " channel = " + channel);
		Logger.getLogger(RcMainFrame.class.getName()).log(Level.INFO,
				(forPiP ? "PiP" : "Main") + " channel = " + channel);

		switch (channel) {
		case "8a":
			((PictPanel) mainDisplay).setPicture("/icons/screen_24.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_24.jpg");
				setPictureInPicture(true);
			}
			break;
		case "8b":
			((PictPanel) mainDisplay).setPicture("/icons/screen_1.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_1.jpg");
				setPictureInPicture(true);
			}
			break;
		case "8c":
			((PictPanel) mainDisplay).setPicture("/icons/screen_2.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_2.jpg");
				setPictureInPicture(true);
			}
			break;
		case "22a":
			((PictPanel) mainDisplay).setPicture("/icons/screen_3.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_3.jpg");
				setPictureInPicture(true);
			}
			break;
		case "22b":
			((PictPanel) mainDisplay).setPicture("/icons/screen_4.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_4.jpg");
				setPictureInPicture(true);
			}
			break;
		case "22c":
			((PictPanel) mainDisplay).setPicture("/icons/screen_5.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_5.jpg");
				setPictureInPicture(true);
			}
			break;
		case "22d":
			((PictPanel) mainDisplay).setPicture("/icons/screen_6.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_6.jpg");
				setPictureInPicture(true);
			}
			break;
		case "30a":
			((PictPanel) mainDisplay).setPicture("/icons/screen_7.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_7.jpg");
				setPictureInPicture(true);
			}
			break;
		case "30b":
			((PictPanel) mainDisplay).setPicture("/icons/screen_8.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_8.jpg");
				setPictureInPicture(true);
			}
			break;
		case "30c":
			((PictPanel) mainDisplay).setPicture("/icons/screen_9.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_9.jpg");
				setPictureInPicture(true);
			}
			break;
		case "30d":
			((PictPanel) mainDisplay).setPicture("/icons/screen_10.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_10.jpg");
				setPictureInPicture(true);
			}
			break;
		case "34a":
			((PictPanel) mainDisplay).setPicture("/icons/screen_11.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_11.jpg");
				setPictureInPicture(true);
			}
			break;
		case "34b":
			((PictPanel) mainDisplay).setPicture("/icons/screen_12.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_12.jpg");
				setPictureInPicture(true);
			}
			break;
		case "34c":
			((PictPanel) mainDisplay).setPicture("/icons/screen_13.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_13.jpg");
				setPictureInPicture(true);
			}
			break;
		case "34d":
			((PictPanel) mainDisplay).setPicture("/icons/screen_14.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_14.jpg");
				setPictureInPicture(true);
			}
			break;
		case "37a":
			((PictPanel) mainDisplay).setPicture("/icons/screen_15.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_15.jpg");
				setPictureInPicture(true);
			}
			break;
		case "37b":
			((PictPanel) mainDisplay).setPicture("/icons/screen_16.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_16.jpg");
				setPictureInPicture(true);
			}
			break;
		case "37c":
			((PictPanel) mainDisplay).setPicture("/icons/screen_17.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_17.jpg");
				setPictureInPicture(true);
			}
			break;
		case "44a":
			((PictPanel) mainDisplay).setPicture("/icons/screen_18.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_18.jpg");
				setPictureInPicture(true);
			}
			break;
		case "44b":
			((PictPanel) mainDisplay).setPicture("/icons/screen_19.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_19.jpg");
				setPictureInPicture(true);
			}
			break;
		case "44c":
			((PictPanel) mainDisplay).setPicture("/icons/screen_20.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_20.jpg");
				setPictureInPicture(true);
			}
			break;
		case "44d":
			((PictPanel) mainDisplay).setPicture("/icons/screen_21.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_21.jpg");
				setPictureInPicture(true);
			}
			break;
		case "54a":
			((PictPanel) mainDisplay).setPicture("/icons/screen_22.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_22.jpg");
				setPictureInPicture(true);
			}
			break;
		case "54b":
			((PictPanel) mainDisplay).setPicture("/icons/screen_23.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_23.jpg");
				setPictureInPicture(true);
			}
			break;
		case "54c":
			((PictPanel) mainDisplay).setPicture("/icons/screen_25.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/screen_25.jpg");
				setPictureInPicture(true);
			}
			break;

		default:
			((PictPanel) mainDisplay).setPicture("/icons/testbild.jpg");
			if (forPiP) {
				((PictPanel) pipDisplay).setPicture("/icons/testbild.jpg");
				setPictureInPicture(true);
			}
			break;
		}
	}

	/**
	 * Stellt die Lautstaerke des Fernsehers ein.
	 * 
	 * @param volume
	 *            Einstellwert fuer die Lautstaerke im Bereich 0..100 (0 = aus,
	 *            100 = volle Lautstaerke)
	 * @throws Exception
	 *             wenn der Wert von "volume" ausserhalb des zulaessigen
	 *             Bereichs ist
	 */
	public void setVolume(int volume) throws Exception {
		if (volume < 0 || volume > 100)
			throw new Exception("Volume out of range 0..100: " + volume);
		System.out.println("Volume = " + volume);
		Logger.getLogger(RcMainFrame.class.getName()).log(Level.INFO,
				"Volume = " + volume);
	}

	/**
	 * Vergroessert bei Aktivierung das aktuelle Bild des Main-Display auf 133%
	 * und stellt es zentriert dar, d.h. die Raender des vergroesserten Bildes
	 * werden abgeschnitten. Dadurch verschwinden die schwarzen Balken rechts
	 * und links bei 4:3 Sendungen, bzw. die schwarzen Balken oben und unten bei
	 * Cinemascope Filmen.
	 * 
	 * @param on
	 *            true: Vergroesserung auf 133%; false: Normalgroesse 100%
	 */
	public void setZoom(boolean on) {
		System.out.println("Zoom = " + (on ? "133%" : "100%"));
		Logger.getLogger(RcMainFrame.class.getName()).log(Level.INFO,
				"Zoom = " + (on ? "133%" : "100%"));

		if (on) {
			if (((double) mainDisplay.getWidth() / mainDisplay.getHeight() - 4.0 / 3.0) <= 0.1) {
				mainDisplay.setSize(1066, 800);
				mainDisplay.setLocation(-33, -100);
			} else if (((double) mainDisplay.getWidth()
					/ mainDisplay.getHeight() - 16.0 / 9.0) <= 0.1) {
				mainDisplay.setLocation(-166, -75);
				mainDisplay.setSize(1333, 750);
			} else {
				mainDisplay.setLocation(-125, 34);
				mainDisplay.setSize(1250, 532);
			}
		} else {
			if (((double) mainDisplay.getWidth() / mainDisplay.getHeight() - 4.0 / 3.0) <= 0.1) {
				mainDisplay.setSize(800, 600);
				mainDisplay.setLocation(100, 0);
			} else if (((double) mainDisplay.getWidth()
					/ mainDisplay.getHeight() - 16.0 / 9.0) <= 0.1) {
				mainDisplay.setLocation(0, 19);
				mainDisplay.setSize(1000, 562);
			} else {
				mainDisplay.setLocation(30, 100);
				mainDisplay.setSize(940, 400);
			}
		}
	}

	/**
	 * Aktiviert bzw. deaktiviert die PictureInPicture-Darstellung.
	 * 
	 * @param show
	 *            true: macht das kleine Bild sichtbar; false: macht das kleine
	 *            Bild unsichtbar
	 */
	public void setPictureInPicture(boolean show) {
		System.out.println("PiP = " + (show ? "visible" : "hidden"));
		Logger.getLogger(RcMainFrame.class.getName()).log(Level.INFO,
				"PiP = " + (show ? "visible" : "hidden"));

		this.pipDisplay.setVisible(show);
	}

	/**
	 * Startet die Aufnahme auf den TimeShift-Recorder bzw. beendet sie wieder.
	 * Das Beenden der Aufnahme beendet gleichzeitig eine eventuell laufende
	 * Wiedergabe.
	 * 
	 * @param start
	 *            true: Start; false: Stopp
	 * @throws Exception
	 *             wenn der Wert von "start" nicht zum aktuellen Zustand passt
	 */
	public void recordTimeShift(boolean start) throws Exception {
		if (this.isRecording == start)
			throw new Exception("TimeShift is already "
					+ (this.isRecording ? "recording" : "stopped"));
		if (!start)
			this.playTimeShift(false, 0);
		this.isRecording = start;
		this.recordingStartTime = now();
		System.out.println((start ? "Start" : "Stop") + " timeshift recording");
		Logger.getLogger(RcMainFrame.class.getName()).log(Level.INFO,
				(start ? "Start" : "Stop") + " timeshift recording");
	}

	/**
	 * Startet die Wiedergabe vom TimeShift-Recorder bzw. beendet sie wieder.
	 * 
	 * @param start
	 *            true: Start; false: Stopp
	 * @param offset
	 *            der Zeitversatz gegen�ber der Aufnahme in Sekunden (>0 und nur
	 *            relevant bei Start=true)
	 * @throws Exception
	 *             wenn keine Aufzeichnung l�uft oder noch nicht genug gepuffert
	 *             ist
	 */
	public void playTimeShift(boolean start, int offset) throws Exception {
		if (start && offset <= 0)
			throw new Exception("TimeShift offset shoud be greater than 0");
		if (start && !this.isRecording)
			throw new Exception("TimeShift is not recording");
		if (start && this.recordingStartTime + offset > now())
			throw new Exception("TimeShift has not yet buffered " + offset
					+ " seconds");
		System.out.println((start ? "Start" : "Stop") + " timeshift playing"
				+ (start ? " (offset " + offset + " seconds)" : ""));
		Logger.getLogger(RcMainFrame.class.getName()).log(
				Level.INFO,
				(start ? "Start" : "Stop") + " timeshift playing"
						+ (start ? " (offset " + offset + " seconds)" : ""));
	}
}
