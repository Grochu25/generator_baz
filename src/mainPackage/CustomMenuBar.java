package mainPackage;

import javax.swing.*;

public class CustomMenuBar extends JMenuBar
{
	private static Output outputState = Output.TFILE;
	private static JCheckBoxMenuItem override;
	private static JCheckBoxMenuItem znaki;
	
	private PickComp refer;
	
	public CustomMenuBar(DBaseConfigFrame conf, PickComp comp)
	{
		refer = comp;
		
		JMenu outputs = new JMenu("wyjscia");
		ButtonGroup outputsOpt = new ButtonGroup();
		JRadioButtonMenuItem tFile = new JRadioButtonMenuItem("plik tekstowy");
		tFile.setSelected(true);
		tFile.addActionListener(event->{outputState = Output.TFILE; refer.unlockAdditionals();});
		JRadioButtonMenuItem dBase = new JRadioButtonMenuItem("baza danych");
		dBase.addActionListener(event->{outputState = Output.DBASE; refer.blockAdditionals();});
		outputsOpt.add(tFile);
		outputsOpt.add(dBase);
		outputs.add(tFile);
		outputs.add(dBase);
		outputs.addSeparator();
		znaki = new JCheckBoxMenuItem("usuwanie polskich znaków");
		znaki.setSelected(false);
		add(outputs);
		
		JMenu DBase = new JMenu("baza danych");
		JMenuItem DBaseSettings = new JMenuItem("Ustawienia Bazy Danych");
		DBaseSettings.addActionListener(event->{conf.setVisible(true);});
		DBase.add(DBaseSettings);
		override = new JCheckBoxMenuItem("Nadpisuj istniej¹ce dane");
		DBase.add(override);
		add(DBase);
	}
	
	public static boolean isOverrideSet() 
	{
		return override.isSelected();
	}
	
	public static boolean isCuttingSet()
	{
		return znaki.isSelected();
	}
	
	public static Output getOutput()
	{
		return outputState;
	}
	
	public static enum Output
	{
		DBASE, TFILE;
	}
}
