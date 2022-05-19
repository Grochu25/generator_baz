package mainPackage;

import java.io.*;
import java.util.Scanner;

/*
 * Klasa s≥uøπca wycinaniu polskich liter z tekstu
 */
public class Cutter 
{
	private File file;
	private String line;
	private StringBuilder sb;
	
	/*
	 * konstruktor
	 * @param file plik do obrÛbki
	 */
	public Cutter(File file)
	{
		sb = new StringBuilder();
		this.file = file;
	}
	
	//metoda g≥Ûwna klasy
	public void start()
	{
		try(Scanner in = new Scanner(file)){
			while(in.hasNext())
			{
				line = in.nextLine();
				sb.append(changer(line)+"\n");
			}
		}catch(FileNotFoundException e) {e.printStackTrace();}
		try {
			PrintWriter plik = new PrintWriter("wyjscie.txt");
			plik.write(sb.toString());
			plik.close();
		}catch(FileNotFoundException e) {e.printStackTrace();}
	}
	
	/*
	 * metoda zamieniajπca litery
	 * @param text fragment (linia) do przetworzenia
	 */
	private String changer(String text)
	{
		int start = 0;
		StringBuilder sb2 = new StringBuilder();
		for(int i=0;i<text.length();i++)
		{
			if(text.codePointAt(i)>240)
			{
			if(text.charAt(i)=='•') sb2.append('A');
			else if(text.charAt(i)=='π') sb2.append('a');
			else if(text.charAt(i)=='∆') sb2.append('C');
			else if(text.charAt(i)=='Ê') sb2.append('c');
			else if(text.charAt(i)==' ') sb2.append('E');
			else if(text.charAt(i)=='Í') sb2.append('e');
			else if(text.charAt(i)=='£') sb2.append('L');
			else if(text.charAt(i)=='≥') sb2.append('l');
			else if(text.charAt(i)=='—') sb2.append('N');
			else if(text.charAt(i)=='Ò') sb2.append('n');
			else if(text.charAt(i)=='”') sb2.append('O');
			else if(text.charAt(i)=='Û') sb2.append('o');
			else if(text.charAt(i)=='å') sb2.append('S');
			else if(text.charAt(i)=='ú') sb2.append('s');
			else if(text.charAt(i)=='è') sb2.append('Z');
			else if(text.charAt(i)=='ü') sb2.append('z');
			else if(text.charAt(i)=='Ø') sb2.append('Z');
			else if(text.charAt(i)=='ø') sb2.append('z');
			else sb2.append('?');
			start = i+1;
			}
			else
				sb2.append(text.charAt(i));
		}
		return sb2.toString();
	}
}
