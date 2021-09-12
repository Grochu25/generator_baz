package mainPackage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

import javax.swing.JOptionPane;

/*
 * Klasa wczytuj�ca zawarto�� plik�w tekstowych z danymi
 * na ich podstawie generuje 'Losowe' rezultaty dla odpowiednich p�l
 */
public class Generator 
{
	private ArrayList<String> imiona;
	private ArrayList<String> nazwiska;
	private ArrayList<String> ulice;
	private ArrayList<String> miasta;
	private Random rand;
	private static int LP = 0;
	private boolean quotes;
	
	/*
	 * Kostruktor Tworz�cy listy i wczytuj�cy do nich zawarto�� z plik�w przy pomocy metody poni�ej
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
	
	public static void main(String[] args)
	{
		Generator test = new Generator();
	}
	
	/*
	 * Wpisuje dane z pliku do podajen listy numerowanej
	 * @param fileName �cie�ka do pliku
	 * @param list nazwa listy do kt�rej wpisane s� dane
	 */
	public List<String> fillArrays(String fileName)
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
	 * Metoda do sutalania cudzys�owi�w okalaj�cych dane stestowe
	 */
	public void setQuotes(boolean a)
	{
		quotes = a;
	}
	
	//Nadanie liczby porz�dkowej dla krotki
	public String getOrdinalNumber()
	{
		Integer in = ++LP;
		return in.toString();
	}
	
	//reset liczby porz�dkowej
	public void resetOrdinalNumber()
	{
		LP=0;
	}
	
	/*
	 * zwraca losow� liczb� z zakresu od 0 do podanej liczby
	 * @param g�rna granica losowej liczby
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
	
	//zwraca imi�
	public String getName()
	{
		int index = rand.nextInt(imiona.size());
		return imiona.get(index);
	}
	
	//zwraca drugie i kolejne imi�
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
	 * Zwraca email na podstawie imienia i nazwiska g��wnego
	 * @param name imi� osoby
	 * @param surname nazwisko osoby
	 */
	public String getEmail(String name, String surname, int mailsNumber)
	{
		String[] mail = {"@gmail.com","@wp.pl","@onet.pl","@yahoo.com"};
		if(quotes)return "'"+new StringBuilder(name.substring(0, 3) + surname + mail[rand.nextInt(4)]).toString()+"'";
		return new StringBuilder(name.substring(0, 3) + surname + mail[rand.nextInt(4)]).toString();
	}
	
	/*
	 * Okre�la p�e� na podstawie imienia
	 * @param name imi� osoby
	 */
	//TODO: doda� wersj� metody bez imienia
	public String getGender(String name)
	{
		if(name.endsWith("a"))
		{
			if(quotes)return "'Kobieta'";
			return "Kobieta";
		}
		else 
		{
			if(quotes)return "'M�czyzna'";
			return "M�czyzna";
		}
	}
	
	/*
	 * Generuje pesel na podstawie daty urodzenia
	 * @param birth data urodzenia jako tekst
	 */
	//TODO: przerobi� metod� na lepsz� obsu�ug� dat i doda� wariant pustej daty
	public String getPESEL(String birth)
	{
		int shift = (quotes)?1:0;
		StringBuilder pesel = new StringBuilder();
		pesel.append(birth.substring(2+shift, 4+shift));
		int month = Integer.parseInt(birth.substring(5+shift, 7+shift)); if(birth.charAt(0+shift) == '2') month+=20;
		if(month<10)pesel.append("0");
		pesel.append(month);
		pesel.append(birth.substring(8+shift,10+shift));
		pesel.append(rand.nextInt(88888)+11111);
		return pesel.toString();
	}
	
	/*
	 * generuje dat� z zakresu
	 * @param start dolny zakres
	 * @param stop g�rny zakres
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
	
	//informacja zwrotna o b��dzie odczytu kt�rego� z plik�w
	private void errorFile(String name)
	{
		JOptionPane.showMessageDialog(null, "Generator napotka� b��d przy wczytywaniu pliku "+name, "B��d odczytu",  JOptionPane.WARNING_MESSAGE);
	}
}

