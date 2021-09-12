package mainPackage;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.lang.reflect.*;
import java.time.*;
import java.io.*;

public class PickComp extends JPanel
{
	private ArrayList<Method> order;
	private ArrayList<JTextArea> comps;
	private ArrayList<dateRangeHolder> dInfos;
	private Generator gen;
	private PrintWriter plik;
	private String nameCont;
	private String surnameCont;
	private String bDateCont;
	private GridBagConstraints con;
	private JPanel content;
	private JComboBox<String> separator;
	private JCheckBox brackets;
	private JCheckBox quotes;
	private File file;
	private int bDateIndex = -1;
	
	/*
	 *  G³ówny konstruktor
	 *  @param main referencja do okna g³ównego w celu wywo³ania metod z nim zwi¹zanych
	 */
	public PickComp(MainFrame main)
	{
		file = new File("wyjscie.txt");						//TODO:dodaæ mo¿liwoœæ wyboru pliku wyjœciowego albo bezpoœredniego wyjœcia do wybranej bazy
		gen = new Generator();
		order = new ArrayList<>();
		comps = new ArrayList<>();
		dInfos = new ArrayList<>();
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
			comps.remove(x-1); repaint(); updateUI(); main.repaint();
		}});
		
		//dodaje pola pomocnicze odnoœcnie znaków ograniczaj¹cych sk³adniê SQL
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
		updateUI();
		repaint();
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
		updateUI();
		repaint();
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
		updateUI();
		repaint();
	}
	
	/*
	 * G³ówna metoda wykonuj¹ca wszystkie operacje zawarte w kolejce wykonawczej
	 * decyduje te¿ o sk³adni ograniczaj¹cej SQL na podstawie wyborów z comboBox
	 * dla odpowiedzniej funkcji z kolejki wykonuje odpowiedni¹ metodê invoke
	 */	
	public void gener(int serial)
	{
		if(quotes.isSelected())gen.setQuotes(true);
		else gen.setQuotes(false);
		try {
			plik = new PrintWriter(file);
			try {
		for(int i=0; i<serial; i++)
		{if(brackets.isSelected())plik.print("(");
			int helper = 0;
			
			for(int j=0;j<order.size();j++){
				Method m = order.get(j);
				
				if(m.getName() == "getRandomInteger")
				{
					int index = 0;
					while(dInfos.get(index).index != j)
						index++;
					plik.print((String)m.invoke(gen,(int)dInfos.get(index).firstDate,(int) dInfos.get(index).secondDate));
				}
				
				else if(m.getName() == "getName")
				{
					if(quotes.isSelected())plik.print("'");
					if(nameCont != null)
					{	
						plik.print(nameCont);
					}
					else {nameCont = (String) m.invoke(gen); plik.print(nameCont);}
					if(quotes.isSelected())plik.print("'");
				}
				
				else if(m.getName() == "getNameB")
				{	
					if(quotes.isSelected())plik.print("'");
					plik.print((String) m.invoke(gen));
					if(quotes.isSelected())plik.print("'");
				}
				
				else if(m.getName() == "getSurname")
				{
					if(quotes.isSelected())plik.print("'");
					if(surnameCont != null)
					{
						plik.print(surnameCont);
					}
					else {surnameCont = (String) m.invoke(gen); plik.print(surnameCont);}
					if(quotes.isSelected())plik.print("'");
				}
				
				else if(m.getName() == "getSurnameB")
				{
					if(quotes.isSelected())plik.print("'");
					plik.print((String) m.invoke(gen));
					if(quotes.isSelected())plik.print("'");
				}
				
				else if(m.getName() == "getDate")
				{
					int index = 0;
					while(dInfos.get(index).index != j)
						index++;
					System.out.println(index);
					if(index == bDateIndex)
					{
						if(bDateCont!=null)
							plik.print(bDateCont);
						else {
						bDateCont = (String) m.invoke(gen, dInfos.get(index).firstDate, dInfos.get(index).secondDate);
						plik.print(bDateCont);}
					}
					else
						plik.print((String) m.invoke(gen, dInfos.get(index).firstDate, dInfos.get(index).secondDate));
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
					
					plik.print((String) m.invoke(gen,imie,nazwisko,4));
					}catch(NoSuchMethodException e) {e.printStackTrace();}
				}
				
				else if(m.getName() == "getGender")
				{
					try {
					int imie;
					if(order.contains(Generator.class.getMethod("getName")))
					{
						imie = order.indexOf(Generator.class.getMethod("getName"));
						if(imie>j && nameCont == null)
							nameCont = gen.getName();
							plik.print((String) m.invoke(gen, nameCont));
					}else {plik.print((String) m.invoke(gen, gen.getName()));}
					}catch(NoSuchMethodException e) {e.printStackTrace();}
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
						plik.print((String)m.invoke(gen, bDateCont));
					}else {plik.print((String)m.invoke(gen, gen.getDate(LocalDate.of(1970, 1, 1),LocalDate.now())));}
				}
				
				else
					plik.print((String) m.invoke(gen));
				if(helper!=order.size()-1)plik.print((String) separator.getSelectedItem());
				helper++;
			}
			
			if(brackets.isSelected())plik.print(")");
			if(brackets.isSelected())plik.print((i==serial-1)?";":",");
			plik.print("\n"); 
		nameCont = null; surnameCont = null;}
			plik.close(); gen.resetOrdinalNumber();
			}catch(InvocationTargetException | IllegalAccessException e) {e.printStackTrace();}
		}catch(FileNotFoundException e) {e.printStackTrace();}
		
		Cutter bruh = new Cutter(file);
		bruh.start();
	}
	
	public boolean getBDateState()
	{
		return bDateIndex > -1;
	}
	
	//TODO: sprawdziæ czy nie da siê zastosowaæ typu generycznego
	private class dateRangeHolder
	{
		public Object firstDate;
		public Object secondDate;
		public int index;
	}
}

