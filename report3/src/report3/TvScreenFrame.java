package report3;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * TV Screen Resolution 1000x600 Simple Frame with 2 JPanels which show jpgs
 * 
 * @author moritz
 * 
 */
public class TvScreenFrame extends JFrame {

	private JPanel contentPane;
	private PictPanel mainPanel, pipPanel;
	private RcController rcController;

	/**
	 * Constructor of TvScreenFrame with 2 Panels
	 */
	public TvScreenFrame() {
		this.setTitle("TV È­¸é");
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1004, 627);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		pipPanel = new PictPanel("/icons/testbild.jpg");
		pipPanel.setBorder(new LineBorder(Color.WHITE));
		pipPanel.setLocation(773, 12);
		pipPanel.setPreferredSize(new Dimension(200, 112));
		pipPanel.setSize(new Dimension(200, 112));
		pipPanel.setLayout(null);
		contentPane.add(pipPanel);

		mainPanel = new PictPanel("/icons/testbild.jpg");
		mainPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (mainPanel.getWidth() == 800 || mainPanel.getWidth() == 1000
						|| mainPanel.getWidth() == 940) {
					rcController.setZoom(true);
				} else {
					rcController.setZoom(false);
				}
			}
		});
		try {
			java.net.URL url = this.getClass().getResource(
					"/icons/lupe_Icon.gif");
			BufferedImage img = ImageIO.read(url);
			Point hotSpot = new Point(0, 0);
			if (url != null) {
				Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
						img, hotSpot, "red Cursor");
				mainPanel.setCursor(cursor);
			}
		} catch (IOException e) {
			Logger.getLogger(RcMainFrame.class.getName()).log(Level.WARNING,
					null, e);
		}
		mainPanel.setLocation(0, 19);
		mainPanel.setPreferredSize(new Dimension(1000, 562));
		mainPanel.setSize(new Dimension(1000, 562));
		mainPanel.setLayout(null);
		mainPanel.setOpaque(false);
		contentPane.add(mainPanel);
	}

	/**
	 * Setter for rcController Reference
	 * 
	 * @param rcController
	 */
	public void setRcController(RcController rcController) {
		this.rcController = rcController;
	}

	/**
	 * Getter for PipPanel
	 * 
	 * @return
	 */
	public JPanel getPipPanel() {
		return pipPanel;
	}

	/**
	 * Getter for MainPanel
	 * 
	 * @return is JPanel
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}

	/**
	 * Getter for MainPanel
	 * 
	 * @return is PictPanel
	 */
	public PictPanel getMainPictPanel() {
		return mainPanel;
	}
}

/**
 * Class PictPanel draws jpg on screen Default Constructor Testbild.jpg
 * 
 * @author moritz
 * 
 */
class PictPanel extends JPanel {
	private Image Pict = null;

	/**
	 * Constructor with default jpg
	 */
	public PictPanel() {
		super();
		this.setLayout(null);
		Pict = getToolkit().getImage(
				this.getClass().getResource("/icons/testbild.jpg"));
	}

	/**
	 * Constructor with jpg given in source
	 * 
	 * @param source
	 *            String of source. Path relative
	 */
	public PictPanel(String source) {
		super();
		this.setLayout(null);
		Pict = getToolkit().getImage(this.getClass().getResource(source));
	}

	/**
	 * PaintComponent. Invoked implizit
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(Pict, 0, 0, getWidth(), getHeight(), this);
	}

	/**
	 * Sets private attribute Pict and invokes repaint of Panel
	 * 
	 * @param source
	 */
	public void setPicture(String source) {
		Pict = getToolkit().getImage(this.getClass().getResource(source));
		this.repaint();
	}
}
