package mainPackage;

import javax.swing.*;
import java.awt.*;

/*
 *  @version 1.2 
 *  @author Grochu
 */
public class MainFrame extends JFrame
{
	private int windowHeight;
	private int windowWidth;
	private PickComp mainPanel;
	private ComponentToolbar topBar;
	
	/*
	 * Program g��wny
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
	 * Tworzenie okna g��wnego oraz rozmieszczenie elemnet�w w nim obecnych
	 * dodaje te� mo�liwo�� scrollowania horyzontalnie w g��wnym menu
	 */
	public MainFrame() {
		halfSize();
		setTitle("Generator przyk�adowych baz danych");
		Image icon = new ImageIcon("sources/icon.png").getImage();
		setIconImage(icon);
		
		mainPanel = new PickComp(this);
		topBar = new ComponentToolbar(mainPanel);
		JScrollPane quickScroll = new JScrollPane(topBar);
		quickScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		quickScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		mainPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
		add(quickScroll, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(endComp(), BorderLayout.SOUTH);
	}
	
	/*
	 * metoda potrzebna do aktualizacji r�wnie� topBaru
	 */
	//TODO: sprawdzi� sp�jno�� rysowania i od�wie�ania z w�tkiem dystrybucji zdarze�
	//to jest ten od wy�wietlania interfacu (meody EventQueue)
	public void repaint()
	{
		topBar.repaint();
		super.repaint();
	}
	
	/*
	 * obliczanie wymier�w okna
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
		GridLayout gl = new GridLayout(1,5,2,0);
		panel.setLayout(gl);
		panel.setBorder(BorderFactory.createEmptyBorder(0, windowWidth/6, 0, windowWidth/6));
		
		JSpinner editor = new JSpinner();
		editor.setValue(1);
		JButton submit = new JButton("Wykonaj");
		submit.addActionListener(event->{mainPanel.gener((int) editor.getValue());
			
		});
		
		panel.add(editor);
		gl.setHgap(10);
		panel.add(submit);
		return panel;
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
