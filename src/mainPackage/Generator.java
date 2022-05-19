package mainPackage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

import javax.swing.*;

/*
 * Klasa wczytuj¹ca zawartoœæ plików tekstowych z danymi
 * na ich podstawie generuje 'Losowe' rezultaty dla odpowiednich pól
 */
public class Generator 
{
	private ArrayList<String> imiona;
	private ArrayList<String> nazwiska;
	private ArrayList<String> ulice;
	private ArrayList<String> miasta;
	private static ArrayList<ArrayList<String>> customs = new ArrayList<>();
	private Random rand;
	private static int LP = 0;
	private static int customIterator = 0;
	private boolean quotes;
	
	/*
	 * Kostruktor Tworz¹cy listy i wczytuj¹cy do nich zawartoœæ z plików przy pomocy metody poni¿ej
	 */
	public Generator()
	{
		quotes = false;
		imiona = new ArrayList<String>(fillArrays("sources/imiona.txt"));
		nazwiska = new ArrayList<String>(fillArrays("sources/nazwiska.txt"));
		ulice = new ArrayList<String>(fillArrays("sources/ulice.txt"));
		miasta = new ArrayList<String>(fillArrays("sources/miasta.txt"));
		rand = new Random();
	}
	
	public static int addToCustoms(String src, String name, boolean kontrola)
	{
		File addFile = new File("sources/customs/"+name+".txt");
		if(addFile.exists() && !kontrola)
		{
			MainFrame.errorMessage("Pole o tej nazwie ju¿ istnieje");
			return -1;
		}
		try {
			Files.write(Paths.get("sources/customs/"+name+".txt"), Files.readAllBytes(Paths.get(src)), StandardOpenOption.CREATE);
		}catch(IOException e) {e.printStackTrace(); return -1;}
		
		customs.add(new ArrayList<String>(fillArrays("sources/customs/"+name+".txt")));
		
		return customIterator++;
	}
	
	public static void deleteFromCustoms(String name)
	{
		File deleted = new File("sources/customs/"+name);
		if(deleted.exists())
		{
			deleted.delete();
		}
	}
	
	public static void setOrdinalNumber(int x)
	{
		LP = x;
	}
	
	/*
	 * Wpisuje dane z pliku do podajen listy numerowanej
	 * @param fileName œcie¿ka do pliku
	 * @param list nazwa listy do której wpisane s¹ dane
	 */
	public static List<String> fillArrays(String fileName)
	{
		List<String> list;
		try
		{
			String contents = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
			Stream<String> variables = Stream.of(contents.split("(\\PL+[\\s])"));
			list = variables.collect(Collectors.toList());
			return list;
		}
		catch(IOException iex)
		{
			errorFile(fileName);
		}
		return null;
	}
	
	/*
	 * Metoda do sutalania cudzys³owiów okalaj¹cych dane stestowe
	 */
	public void setQuotes(boolean a)
	{
		quotes = a;
	}
	
	//Nadanie liczby porz¹dkowej dla krotki
	public String getOrdinalNumber()
	{
		Integer in = ++LP;
		return in.toString();
	}
	
	//reset liczby porz¹dkowej
	public void resetOrdinalNumber()
	{
		LP=0;
	}
	
	/*
	 * zwraca losow¹ liczbê z zakresu od 0 do podanej liczby
	 * @param górna granica losowej liczby
	 */
	
	public String getRandomInteger(int y, int x)
	{
		Integer in = 0;
		if(y<x)
			{in = rand.nextInt(x-y)+y;}
		else
			{in = rand.nextInt(y-x)+x;}
		return in.toString();
	}
	
	//zwraca imiê
	public String getName()
	{
		int index = rand.nextInt(imiona.size());
		return imiona.get(index);
	}
	
	//zwraca drugie i kolejne imiê
	public String getNameB()
	{
		return getName();
	}
	
	//zwraca nazwisko
	public String getSurname()
	{
		int index = rand.nextInt(nazwiska.size());
		return nazwiska.get(index);
	}
	
	//zwraca drugie i kolejne nazwiska
	public String getSurnameB()
	{
		return getSurname();
	}
	
	//ulica
	public String getStreet()
	{
		int index = rand.nextInt(ulice.size());
		if(quotes)return "'"+ulice.get(index)+"'";
		return ulice.get(index);
	}
	
	//numer domu
	public String getStreetWNumber()
	{
		int index = rand.nextInt(ulice.size());
		int number = rand.nextInt(200)+1;
		if(quotes)return "'"+ulice.get(index)+" "+number+"'";
		return ulice.get(index)+" "+number;
	}
	
	//zwraca miasto
	public String getCity()
	{
		int index = rand.nextInt(miasta.size());
		if(quotes)return "'"+miasta.get(index)+"'";
		return miasta.get(index);
	}
	
	//zwraca numer telefonu
	public String getPhoneNumber()
	{
		Integer in = rand.nextInt(888888888)+111111111;
		return in.toString();
	}
	
	/*
	 * Zwraca email na podstawie imienia i nazwiska g³ównego
	 * @param name imiê osoby
	 * @param surname nazwisko osoby
	 */
	public String getEmail(String name, String surname, int mailsNumber)
	{
		String[] mail = {"@gmail.com","@wp.pl","@onet.pl","@yahoo.com"};
		if(quotes)return "'"+new StringBuilder(name.substring(0, 3) + surname + mail[rand.nextInt(4)]).toString()+"'";
		return new StringBuilder(name.substring(0, 3) + surname + mail[rand.nextInt(4)]).toString();
	}
	
	/*
	 * Okreœla p³eæ na podstawie imienia
	 * @param name imiê osoby
	 */
	public String getGender(String name)
	{
		if(name.endsWith("a"))
		{
			if(quotes)return "'Kobieta'";
			return "Kobieta";
		}
		else 
		{
			if(quotes)return "'Mê¿czyzna'";
			return "Mê¿czyzna";
		}
	}
	
	/*
	 * Generuje pesel na podstawie daty urodzenia
	 * @param birth data urodzenia jako tekst
	 */
	public String getPESEL(String birth, String gender)
	{
		int shift = (quotes)?1:0;
		StringBuilder pesel = new StringBuilder();
		pesel.append(birth.substring(2+shift, 4+shift));
		int month = Integer.parseInt(birth.substring(5+shift, 7+shift)); if(birth.charAt(0+shift) == '2') month+=20;
		if(month<10)pesel.append("0");
		pesel.append(month);
		pesel.append(birth.substring(8+shift,10+shift));
		pesel.append(rand.nextInt(888)+111);
		if(gender.substring(0+shift,1+shift).equals("M"))
			pesel.append((rand.nextInt(5)*2)+1);
		else
			pesel.append((rand.nextInt(5)*2));
		pesel.append(controlSum(pesel.toString()));
		return pesel.toString();
	}
	
	public String getCustom(int customNumber)
	{
		int index = rand.nextInt(customs.get(customNumber).size());
		if(quotes)return "'"+customs.get(customNumber).get(index)+"'";
		return customs.get(customNumber).get(index);
	}
	
	/*
	 * Funkcja pomocnicza wyliczaj¹ca sumê kontroln¹ z PESELu
	 * @param pesel 10-cyfrowy pocz¹tek pseslu
	 */
	private int controlSum(String pesel)
	{
		int suma = 0;
		for(int i=0;i<10;i++)
		{
			if(i==0 || i==4 || i==8) suma += Integer.parseInt(pesel.substring(i,i+1))*1 %  10;
			if(i==1 || i==5 || i==9) suma += Integer.parseInt(pesel.substring(i,i+1))*3 %  10;
			if(i==2 || i==6) suma += Integer.parseInt(pesel.substring(i,i+1))*7 %  10;
			if(i==3 || i==7) suma += Integer.parseInt(pesel.substring(i,i+1))*9 %  10;
		}
		return 10 - suma % 10;
	}
	
	/*
	 * generuje datê z zakresu
	 * @param start dolny zakres
	 * @param stop górny zakres
	 */
	public String getDate(LocalDate startGet, LocalDate stopGet)
	{
		LocalDate start = LocalDate.MIN;
		LocalDate stop = LocalDate.MIN;
		
		if(startGet.compareTo(stopGet)>0) {
			start = stopGet;
			stop = startGet;}
		else{
			start = startGet;
			stop = stopGet;}
		
		if(start.equals(stop)) { if(quotes)return "'"+start.toString()+"'"; else return start.toString();}
		
		int day;
		int month;
		int year = (stop.getYear()-start.getYear() == 0)? stop.getYear() : rand.nextInt(stop.getYear()-start.getYear()+1)+start.getYear();
		
		if((stop.getYear()-start.getYear() == 0) && (stop.getMonthValue()-start.getMonthValue() == 0))
			month = start.getMonthValue();
		else if(stop.getYear()-start.getYear() == 0)
			month = rand.nextInt(stop.getMonthValue()-start.getMonthValue()+1)+start.getMonthValue();
		else if(year==start.getYear()) 
			month = rand.nextInt(12-start.getMonthValue())+start.getMonthValue();
		else if(year==stop.getYear())
			month = rand.nextInt(stop.getMonthValue())+1;
		else month = rand.nextInt(12)+1;
		
		Month mm = Month.of(month);
		if((stop.getYear()-start.getYear() == 0) && (stop.getMonthValue()-start.getMonthValue() == 0))
			day = rand.nextInt(stop.getDayOfMonth()-start.getDayOfMonth()+1)+start.getDayOfMonth();
		else if(month==start.getMonthValue() && year==start.getYear())
			day = (start.lengthOfMonth()-start.getDayOfMonth()==0)?start.getDayOfMonth():
				rand.nextInt(start.lengthOfMonth()-start.getDayOfMonth())+start.getDayOfMonth();
		else if(month==stop.getMonthValue() && year==stop.getYear())
			day = rand.nextInt(stop.getDayOfMonth())+1;
		else day = rand.nextInt(mm.length(false))+1;
		
		if(quotes)return "'"+LocalDate.of(year, mm, day).toString()+"'";
		return LocalDate.of(year, mm, day).toString();
	}
	
	//informacja zwrotna o b³êdzie odczytu któregoœ z plików
	private static void errorFile(String name)
	{
		JOptionPane.showMessageDialog(null, "Generator napotka³ b³¹d przy wczytywaniu pliku "+name, "B³¹d odczytu",  JOptionPane.WARNING_MESSAGE);
	}
}

