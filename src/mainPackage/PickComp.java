package mainPackage;

import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.sql.*;
import java.time.*;
import java.io.*;

public class PickComp extends JPanel
{
	private ArrayList<Method> order;
	private ArrayList<JTextArea> comps;
	private ArrayList<dateRangeHolder> dInfos;
	private Generator gen;
	private PrintWriter plik;
	private StringBuilder dataString;
	private String nameCont;
	private String surnameCont;
	private String bDateCont;
	private GridBagConstraints con;
	private JPanel content;
	private JComboBox<String> separator;
	private ArrayList<Integer> customs;
	private JCheckBox brackets;
	private JCheckBox quotes;
	private File file;
	private int bDateIndex = -1;
	private int serial;
	
	/*
	 *  G³ówny konstruktor
	 *  @param main referencja do okna g³ównego w celu wywo³ania metod z nim zwi¹zanych
	 */
	public PickComp(MainFrame main)
	{
		file = new File("wyjscie.txt");	
		gen = new Generator();
		order = new ArrayList<>();
		comps = new ArrayList<>();
		dInfos = new ArrayList<>();
		customs = new ArrayList<>();
		content = new JPanel();
		con = new GridBagConstraints();
		setLayout(new GridBagLayout());
		setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel helpButtons = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		JButton erase = new JButton("usuñ ostatni"); 
		erase.addActionListener(event->{int x = comps.size();										//usuwa ostatni¹ pozycjê, je¿eli kolejka jest niezerowa
		if(x>0){
			if(x-1==bDateIndex){bDateIndex=-1;bDateCont=null;}										//usuwa datê urodzenia czyszcz¹c zmienn¹ pomocnicz¹
			if(order.get(x-1).getName()=="getDate"||order.get(x-1).getName()=="getRandomInteger")
				dInfos.remove(dInfos.get(dInfos.size()-1));   										//usuwa datê z listy danych skorelowanych
			content.remove(comps.get(x-1)); order.remove(order.size()-1); 							//usuwa pole z obszaru g³ównego i odœwie¿a ca³oœæ
			comps.remove(x-1); EventQueue.invokeLater(()->{repaint();updateUI(); main.repaint();});
		}});
		
		//dodaje pola pomocnicze odnoœcnie znaków ograniczaj¹cych sk³adniê SQL
		//blokowane w razie wyboru bazy dancych
		separator = new JComboBox<>(new String[]{", ","; "}); helpButtons.add(new JLabel("separator danych:"));helpButtons.add(separator);
		brackets = new JCheckBox("nawiasy brze¿ne"); brackets.setSelected(true); helpButtons.add(brackets);
		quotes = new JCheckBox("cudzys³owia"); quotes.setSelected(true); helpButtons.add(quotes);
		
		con.anchor = GridBagConstraints.CENTER; con.gridy = 0; con.gridwidth = GridBagConstraints.REMAINDER;
		add(content,con);
		con.gridy = 1;
		add(helpButtons,con);
		con.gridy = 2;
		add(erase,con);
	}
	
	public void blockAdditionals()
	{
		separator.setSelectedItem(separator.getItemAt(0));
		separator.setEnabled(false);
		brackets.setSelected(true);
		brackets.setEnabled(false);
		quotes.setSelected(true);
		quotes.setEnabled(false);
	}
	
	public void unlockAdditionals()
	{
		separator.setEnabled(true);
		brackets.setEnabled(true);
		quotes.setEnabled(true);
	}
	
	/*
	 * Dodaje kafelek do obszaru g³ównego dodaj¹c do kolejki wykonawczej odpowiedni¹ metodê 
	 * @param label etykieta kafelka
	 * @param m metoda generatora odpowiednia dla kafelka
	 */
	public void addComp(String label, Method m)
	{	//TODO: zmieniæ nazwy na powa¿niejsze
		JTextArea kachow = new JTextArea(" "+label); kachow.setBorder(BorderFactory.createRaisedSoftBevelBorder()); kachow.setEditable(false);
		comps.add(kachow);
		content.add(kachow);
		order.add(m);
		EventQueue.invokeLater(()->{
		updateUI();
		repaint();});
	}
	
	public void addComp(String label, int customNumber, Method m)
	{
		JTextArea kachow = new JTextArea(" "+label); kachow.setBorder(BorderFactory.createRaisedSoftBevelBorder()); kachow.setEditable(false);
		comps.add(kachow);
		content.add(kachow);
		order.add(m);
		customs.add(customNumber);
		EventQueue.invokeLater(()->{
		updateUI();
		repaint();});
	}
	
	/*
	 * Dodaje liczbê losow¹ z podango przedzia³u
	 * @param label etykieta kafelka
	 * @param m metoda generatora odpowiednia dla kafelka
	 * @param start dolny zakres liczby losowej
	 * @param end górny przedzia³ liczby losowej
	 */
	public void addComp(String label, Method m, int start, int end)
	{
		JTextArea kachow = new JTextArea(" "+label+"\n od: "+start+"\n do"+end); kachow.setBorder(BorderFactory.createRaisedSoftBevelBorder()); kachow.setEditable(false);
		comps.add(kachow);
		content.add(kachow);
		order.add(m);
		dateRangeHolder drh = new dateRangeHolder(); drh.firstDate = start; drh.secondDate = end; drh.index = order.lastIndexOf(m);
		dInfos.add(drh);
		EventQueue.invokeLater(()->{
		updateUI();
		repaint();
		});
	}
	
	/*
	 * Dodaje kafelek DATY do obszaru g³ównego dodaj¹c do kolejki wykonawczej odpowiedni¹ metodê
	 * do przechowania przedzia³u generowanej daty wykorzystuje klasê wewnêtrzn¹ dateRangeHolder
	 * @param label etykieta kafelka
	 * @param m metoda generatora odpowiednia dla kafelka
	 * @param l1 dolny zakres generowanej daty
	 * @param l2 górny zakres generowanej daty
	 */
	public void addComp(String label, Method m, LocalDate l1, LocalDate l2)
	{
		JTextArea kachow = new JTextArea(" "+label+"\n od"+l1+"\n do"+l2); kachow.setBorder(BorderFactory.createRaisedSoftBevelBorder()); kachow.setEditable(false);
		comps.add(kachow);
		content.add(kachow);
		order.add(m);
		if(label == "Data urodzenia ") bDateIndex = order.size()-1;
		dateRangeHolder drh = new dateRangeHolder(); drh.firstDate = l1; drh.secondDate = l2; drh.index = order.lastIndexOf(m);
		dInfos.add(drh);
		EventQueue.invokeLater(()->{
		updateUI();
		repaint();
		});
	}
	
	/*
	 * G³ówna metoda wykonuj¹ca wszystkie operacje zawarte w kolejce wykonawczej
	 * decyduje te¿ o sk³adni ograniczaj¹cej SQL na podstawie wyborów z comboBox
	 * dla odpowiedzniej funkcji z kolejki wykonuje odpowiedni¹ metodê invoke
	 */	
	public void gener()
	{
		if(quotes.isSelected())gen.setQuotes(true); 
		else gen.setQuotes(false); 
		
		dataString = new StringBuilder();
		try {
		for(int i=0; i<serial; i++)
		{
			if(brackets.isSelected())dataString.append("(");
			int helper = 0;
			int customsIndex = 0;
			
			for(int j=0;j<order.size();j++){
				Method m = order.get(j);
				
				if(m.getName() == "getRandomInteger")
				{
					int index = 0;
					while(dInfos.get(index).index != j)
						index++;
					dataString.append((String)m.invoke(gen,(int)dInfos.get(index).firstDate,(int) dInfos.get(index).secondDate));
				}
				
				else if(m.getName() == "getName")
				{
					if(quotes.isSelected())dataString.append("'");
					if(nameCont != null)
					{	
						dataString.append(nameCont);
					}
					else {nameCont = (String) m.invoke(gen); dataString.append(nameCont);}
					if(quotes.isSelected())dataString.append("'");
				}
				
				else if(m.getName() == "getNameB")
				{	
					if(quotes.isSelected())dataString.append("'");
					dataString.append((String) m.invoke(gen));
					if(quotes.isSelected())dataString.append("'");
				}
				
				else if(m.getName() == "getSurname")
				{
					if(quotes.isSelected())dataString.append("'");
					if(surnameCont != null)
					{
						dataString.append(surnameCont);
					}
					else {surnameCont = (String) m.invoke(gen); dataString.append(surnameCont);}
					if(quotes.isSelected())dataString.append("'");
				}
				
				else if(m.getName() == "getSurnameB")
				{
					if(quotes.isSelected())dataString.append("'");
					dataString.append((String) m.invoke(gen));
					if(quotes.isSelected())dataString.append("'");
				}
				
				else if(m.getName() == "getDate")
				{
					int index = 0;
					while(dInfos.get(index).index != j)
						index++;
					if(index == bDateIndex)
					{
						if(bDateCont!=null)
							dataString.append(bDateCont);
						else {
						bDateCont = (String) m.invoke(gen, dInfos.get(index).firstDate, dInfos.get(index).secondDate);
						dataString.append(bDateCont);}
					}
					else
						dataString.append((String) m.invoke(gen, dInfos.get(index).firstDate, dInfos.get(index).secondDate));
				}
				
				else if(m.getName() == "getEmail")
				{
					try {
					int i_imie, i_nazwisko;
					String imie, nazwisko;
					if(order.contains(Generator.class.getMethod("getName")))
					{
						i_imie = order.indexOf(Generator.class.getMethod("getName"));
						if(i_imie>j && nameCont == null)
							nameCont = gen.getName();
						imie = nameCont;
					}else {imie = gen.getName();}
					
					if(order.contains(Generator.class.getMethod("getSurname")))
					{
						i_nazwisko = order.indexOf(Generator.class.getMethod("getSurname"));
						if(i_nazwisko>j && surnameCont == null)
							surnameCont = gen.getSurname();
							nazwisko = surnameCont;
					}else {nazwisko = gen.getSurname();}
					
					dataString.append((String) m.invoke(gen,imie,nazwisko,4));
					}catch(NoSuchMethodException e) {e.printStackTrace();}
				}
				
				else if(m.getName() == "getGender")
				{
					dataString.append((String) m.invoke(gen, ifNameExists()));
				}
				
				else if(m.getName()=="getPESEL")
				{
					if(bDateIndex > -1)
					{
						if(bDateCont == null)
							{int index=0;
							while(dInfos.get(index).index != bDateIndex)
								index++;
							bDateCont = gen.getDate((LocalDate)dInfos.get(index).firstDate, (LocalDate)dInfos.get(index).secondDate);}
						
						dataString.append((String)m.invoke(gen, bDateCont, gen.getGender(ifNameExists())));
					}else {dataString.append((String)m.invoke(gen, gen.getDate(LocalDate.of(1970, 1, 1),LocalDate.now()), gen.getGender(ifNameExists())));}
				}
				
				else if(m.getName()=="getCustom")
				{
					dataString.append((String) m.invoke(gen, customs.get(customsIndex++)));
				}
				
				else
					dataString.append((String) m.invoke(gen));
				if(helper!=order.size()-1)dataString.append((String) separator.getSelectedItem());
				helper++;
			}
			
			if(brackets.isSelected())dataString.append(")");
			if(brackets.isSelected())dataString.append((i==serial-1)?";":",");
			dataString.append("\n"); 
			nameCont = null; surnameCont = null;
			}
			gen.resetOrdinalNumber();
			}catch(InvocationTargetException | IllegalAccessException e) {e.printStackTrace();}
		
		if(CustomMenuBar.getOutput() == CustomMenuBar.Output.TFILE)
		{
			writeToFile();
			
			if(CustomMenuBar.isCuttingSet())
			{Cutter bruh = new Cutter(file);
			bruh.start();}
		}
		else
			writeToDataBase();
	}
	
	private void writeToFile()
	{
		try
		{
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("pliki tekstowe","txt");
			chooser.setFileFilter(filter);
			chooser.setCurrentDirectory(file);
			chooser.setSelectedFile(file);
			chooser.setMultiSelectionEnabled(false);
			if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				file = chooser.getSelectedFile();
			}
			
			plik = new PrintWriter(file);
			plik.print(dataString.toString());
			plik.close();
		}catch(FileNotFoundException e) {e.printStackTrace(); MainFrame.errorMessage("B³¹d zapisu, nie znaleziono pliku lub brak uprawnieñ");}
	}
	
	private void writeToDataBase()
	{
		try(Connection conn = getConnection(); Statement stat = conn.createStatement())
		{
			stat.executeUpdate("INSERT INTO "+DBaseChooser.getBaseName()+" VALUES "+dataString.toString());

			try(ResultSet result = stat.executeQuery("SELECT * FROM "+DBaseChooser.getBaseName()))
			{
				while(result.next())
					System.out.println(result.getString(1));
			}
		}
		catch(SQLException ex)
		{
			MainFrame.sqlMessage(ex.getMessage());
		}
		catch(IOException e) {e.printStackTrace();}
	}
	
	public void readyDB()
	{
			try(Connection conn = getConnection();Statement stat = conn.createStatement();)
			{
				if(!CustomMenuBar.isOverrideSet())
				{
					ResultSet rs = stat.executeQuery("SELECT "+DBaseChooser.getID()+" FROM "+DBaseChooser.getBaseName()+" ORDER BY "+DBaseChooser.getID()+" DESC LIMIT 1");
					rs.next();
					Generator.setOrdinalNumber(Integer.parseInt(rs.getString(1)));
				}
				else
				{
					boolean erase = stat.execute("TRUNCATE TABLE "+DBaseChooser.getBaseName());
					if(erase) Generator.setOrdinalNumber(0);
				}
			}catch(SQLException ex)
			{
				MainFrame.sqlMessage(ex.getMessage());
			}
			catch(IOException e) {e.printStackTrace();}
		
		gener();
	}
	
	public void setOut(int serial)
	{
		this.serial = serial;
		if(CustomMenuBar.getOutput() == CustomMenuBar.Output.DBASE)
		{
			DBaseChooser chooser = new DBaseChooser(this);
		}
		else
			gener();
	}
	
	public static Connection getConnection() throws IOException, SQLException
	{
		Properties props = new Properties();
		try(InputStream in = Files.newInputStream(Paths.get("db.properties")))
		{
			props.load(in);
		}
		
		String drivers = props.getProperty("jdbc.drivers");
		String url = props.getProperty("jdbc.url");
		String user = props.getProperty("jdbc.user");
		String password = props.getProperty("jdbc.password");
		
		return DriverManager.getConnection(url, user, password);
	}
	
	public boolean getBDateState()
	{
		return bDateIndex > -1;
	}
	
	private String ifNameExists()
	{
		try {
		if(nameCont != null)
			return nameCont;
		else if(order.contains(Generator.class.getMethod("getName")))
		{
			nameCont =  gen.getName();
			return nameCont;
		}
		else
			return gen.getName();
		}catch(NoSuchMethodException e)
		{ e.printStackTrace(); }
		return "Bob";
	}
	

	private class dateRangeHolder
	{
		public Object firstDate;
		public Object secondDate;
		public int index;
	}
}

