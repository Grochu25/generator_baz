package mainPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 *  @version 1.3
 *  @author Grochu
 */
public class MainFrame extends JFrame
{
	private int windowHeight;
	private int windowWidth;
	private PickComp mainPanel;
	private ComponentToolbar topBar;
	private DBaseConfigFrame DBConfig;
	private JPanel bottom;
	protected CustomMenuBar menu;
	
	/*
	 * Program g³ówny
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(()->{
			JFrame frame = new MainFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}
	
	/*
	 * Tworzenie okna g³ównego oraz rozmieszczenie elemnetów w nim obecnych
	 * dodaje te¿ mo¿liwoœæ scrollowania horyzontalnie w g³ównym menu
	 */
	public MainFrame() {
		halfSize();
		setTitle("Generator przyk³adowych baz danych");
		Image icon = new ImageIcon("sources/icon.png").getImage();
		setIconImage(icon);
		
		mainPanel = new PickComp(this);
		topBar = new ComponentToolbar(mainPanel);
		JScrollPane quickScroll = new JScrollPane(topBar);
		quickScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		quickScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		bottom = endComp();
		
		mainPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
		add(quickScroll, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
		
		DBConfig = new DBaseConfigFrame();
		menu = new CustomMenuBar(DBConfig, mainPanel);
		setJMenuBar(menu);
	}
	
	/*
	 * metoda potrzebna do aktualizacji równie¿ topBaru
	 */
	public void repaint()
	{
		EventQueue.invokeLater(()->{
		topBar.repaint();
		super.repaint();
		});
	}
	
	/*
	 * obliczanie wymierów okna
	 */
	private void halfSize()
	{
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		windowHeight = screenSize.height;
		windowWidth = screenSize.width;
		setSize(windowWidth/2,windowHeight/2);
	}
	
	/*
	 * tworzenie dolnej belki pogramu
	 */
	private JPanel endComp()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		SpinnerModel model = new SpinnerNumberModel(1,0,1000,1);
		JSpinner editor = new JSpinner(model);
		JButton submit = new JButton("Wykonaj");
		submit.addActionListener(event->{mainPanel.setOut((int) editor.getValue());
			
		});
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.ipadx = 40;
		gbc.insets = new Insets(0,10,0,10);
		gbc.gridx = 1;
		panel.add(editor, gbc);
		gbc.gridx = 2;
		panel.add(submit, gbc);
		return panel;
	}
	
	public static void sqlMessage(String message)
	{
		JOptionPane.showConfirmDialog(null, message, "B³¹d SQL", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void errorMessage(String message)
	{
		JOptionPane.showConfirmDialog(null, message, "B³¹d", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
	}
	
	/*private class MouseMotionHandler implements MouseMotionListener
	{
		public void mouseMoved(MouseEvent event)
		{
			
		}
		
		public void mouseDragged(MouseEvent event)
		{
			
		}
	}*/
}
