package mainPackage;

import java.awt.EventQueue;
import java.text.*;
import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.event.*;

import org.jdatepicker.impl.*;
import java.util.*;
import java.time.*;

/*
 *  Komponent górnego menu, zawieraj¹cy wszystkie mo¿liwe do dodania pola
 *  Jest podzielony na zak³adki tematyczne
 */
public class ComponentToolbar extends JTabbedPane 
{
	private PickComp mainComp;
	private UtilDateModel model;
	private UtilDateModel model2;
	private JButton birthDateRange;
	private JSpinner rangeStart;
	private JSpinner rangeEnd;
	
	public ComponentToolbar(PickComp mainComp)
	{
		this.mainComp = mainComp;
		addBasicCard();
		addNameCard();
		addAdressCard();
		addDateCard();
	}
	
	private void addBasicCard()
	{
		JPanel topPanel = new JPanel();
		
		JButton liczbaPo = new JButton("Liczba porz¹dkowa");
		liczbaPo.addActionListener(event->{try{mainComp.addComp("Liczba porz¹dkowa ",Generator.class.getMethod("getOrdinalNumber"));}
		catch(NoSuchMethodException e) {e.printStackTrace();}});
		
		SpinnerModel min = new SpinnerNumberModel(0, 0, 1000, 1);
		SpinnerModel max = new SpinnerNumberModel(100, 0, 1000, 1);
		
		rangeStart = new JSpinner(min);
		rangeEnd = new JSpinner(max); 
		JPanel range = new JPanel(); range.add(rangeStart); range.add(rangeEnd);
		range.setBorder(BorderFactory.createTitledBorder("Przedzia³ liczby losowej"));
		
		JButton liczbaLos = new JButton("Liczba losowa");
		liczbaLos.addActionListener(event->{try{mainComp.addComp("Liczba losowa ",Generator.class.getMethod("getRandomInteger", int.class, int.class),getRandomStart(),getRandomEnd());}
		catch(NoSuchMethodException e) {e.printStackTrace();}});
		
		topPanel.add(liczbaPo);
		topPanel.add(liczbaLos);
		topPanel.add(range);
		addTab("Podstawowe", topPanel);
	}
	
	private void addNameCard()
	{
		JPanel topPanel = new JPanel();
		
		JComboBox<String> names = new JComboBox<>(new String[]{"Imie:",">G³ówne",">Dodatkowe"}); names.setSelectedItem("Imie:");
		names.addPopupMenuListener(new PopupMenuListener(){public void popupMenuCanceled(PopupMenuEvent e){}public void popupMenuWillBecomeVisible(PopupMenuEvent e){}
															public void popupMenuWillBecomeInvisible(PopupMenuEvent e){names.setSelectedItem("Imie:");}});
		names.addActionListener(event->{
			if(names.getSelectedItem().equals(">G³ówne")) 
			{
				try{mainComp.addComp("Imiê g³ówne",Generator.class.getMethod("getName"));}
				catch(NoSuchMethodException e) {e.printStackTrace();}
			}else if(names.getSelectedItem().equals(">Dodatkowe")) 
			{
				try{mainComp.addComp("Imiê dodatkowe",Generator.class.getMethod("getNameB"));}
				catch(NoSuchMethodException e) {e.printStackTrace();}
			}});
		
		JComboBox<String> surnames = new JComboBox<>(new String[]{"Nazwisko:",">G³ówne",">Dodatkowe"}); surnames.setSelectedItem("Nazwisko:");
		surnames.addPopupMenuListener(new PopupMenuListener(){public void popupMenuCanceled(PopupMenuEvent e){}public void popupMenuWillBecomeVisible(PopupMenuEvent e){}
															public void popupMenuWillBecomeInvisible(PopupMenuEvent e){surnames.setSelectedItem("Nazwisko:");}});
		surnames.addActionListener(event->{
			if(surnames.getSelectedItem().equals(">G³ówne")) 
			{
				try{mainComp.addComp("Nazwisko g³ówne",Generator.class.getMethod("getSurname"));}
				catch(NoSuchMethodException e) {e.printStackTrace();}
			}else if(surnames.getSelectedItem().equals(">Dodatkowe")) 
			{
				try{mainComp.addComp("Nazwisko dodatkowe",Generator.class.getMethod("getSurnameB"));}
				catch(NoSuchMethodException e) {e.printStackTrace();}
			}});
		
		
		JButton genderButt = new JButton("P³eæ");
		genderButt.addActionListener(event->{try{mainComp.addComp("P³eæ ",Generator.class.getMethod("getGender", String.class));}
		catch(NoSuchMethodException e) {e.printStackTrace();}});
		
		JButton phoneNumberButt = new JButton("Numer telefonu");
		phoneNumberButt.addActionListener(event->{try{mainComp.addComp("Numer telefonu ",Generator.class.getMethod("getPhoneNumber"));}
		catch(NoSuchMethodException e) {e.printStackTrace();}});
		
		JButton emailButt = new JButton("E-mail");
		emailButt.addActionListener(event->{try{mainComp.addComp("E-mail ",Generator.class.getMethod("getEmail", String.class, String.class,int.class));}
		catch(NoSuchMethodException e) {e.printStackTrace();}});
		
		JButton peselButt = new JButton("PESEL");
		peselButt.addActionListener(event->{try{mainComp.addComp("PESEL ",Generator.class.getMethod("getPESEL", String.class));}
		catch(NoSuchMethodException e) {e.printStackTrace();}});
		
		topPanel.add(names);
		topPanel.add(surnames);
		topPanel.add(genderButt);
		topPanel.add(phoneNumberButt);
		topPanel.add(emailButt);
		topPanel.add(peselButt);
		
		addTab("Dane osobowe", topPanel);
	}
	
	private void addAdressCard()
	{
		JPanel topPanel = new JPanel();
		//topPanel.setLayout(new GridLayout(1,2));
		JButton streetButt = new JButton("Ulica");
		streetButt.addActionListener(event->{try{mainComp.addComp("Ulica ",Generator.class.getMethod("getStreet"));}
		catch(NoSuchMethodException e) {e.printStackTrace();}});
		
		JButton streetWnButt = new JButton("Ulica i numer");
		streetWnButt.addActionListener(event->{try{mainComp.addComp("Ulica i numer",Generator.class.getMethod("getStreetWNumber"));}
		catch(NoSuchMethodException e) {e.printStackTrace();}});
		
		JButton cityButt = new JButton("Miasto");
		cityButt.addActionListener(event->{try{mainComp.addComp("Miasto",Generator.class.getMethod("getCity"));}
		catch(NoSuchMethodException e) {e.printStackTrace();}});
		
		topPanel.add(streetButt);
		topPanel.add(streetWnButt);
		topPanel.add(cityButt);
		addTab("Adress",topPanel);
	}
	
	private void addDateCard()
	{
		JPanel topPanel = new JPanel();
		topPanel.add(dateComp());
		
		addTab("Daty",topPanel);
	}
	
	public LocalDate getStartDate()
	{
		return LocalDate.of(model.getYear(), model.getMonth()+1, model.getDay());
	}
	
	public LocalDate getStopDate()
	{
		return LocalDate.of(model2.getYear(), model2.getMonth()+1, model2.getDay());
	}
	
	public int getRandomStart()
	{
		return (int) rangeStart.getValue();
	}
	
	public int getRandomEnd()
	{
		return (int) rangeEnd.getValue();
	}
	
	private JPanel dateComp()
	{
		birthDateRange = new JButton("Data urodzenia");
		birthDateRange.addActionListener(event->{birthDateRange.setEnabled(false); try{mainComp.addComp("Data urodzenia ",Generator.class.getMethod("getDate", LocalDate.class, LocalDate.class),getStartDate(),getStopDate());}
		catch(NoSuchMethodException e) {e.printStackTrace();}});
		
		JButton dateRange = new JButton("Data zwyk³a");
		dateRange.addActionListener(event->{try{mainComp.addComp("Data zwyk³a ",Generator.class.getMethod("getDate", LocalDate.class, LocalDate.class),getStartDate(),getStopDate());}
		catch(NoSuchMethodException e) {e.printStackTrace();}});
		
		Properties p = new Properties();
		p.put("text.today", "Dzisiaj");p.put("text.month", "Miesi¹c");p.put("text.year", "Rok");
		
		model = new UtilDateModel();
		model.setDate(LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1, LocalDate.now().getDayOfMonth());model.addDay(-1);
		model.setSelected(true);
		
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.setTextEditable(false);
		
		model2 = new UtilDateModel();
		model2.setDate(LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1, LocalDate.now().getDayOfMonth());
		model2.setSelected(true);
		
		JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p);
		JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
		datePicker2.setTextEditable(false);
		
		JPanel topPanel = new JPanel();
		topPanel.add(datePicker);
		topPanel.add(datePicker2);
		topPanel.add(dateRange);
		topPanel.add(birthDateRange);
		
		return topPanel;
	}
	
	private class DateLabelFormatter extends AbstractFormatter {

	    private String datePattern = "dd-MM-yyyy";
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	    @Override
	    public Object stringToValue(String text) throws ParseException {
	        return dateFormatter.parseObject(text);
	    }

	    @Override
	    public String valueToString(Object value) throws ParseException {
	        if (value != null) {
	            Calendar cal = (Calendar) value;
	            return dateFormatter.format(cal.getTime());
	        }

	        return "";
	    }

	}
	
	public void buttonEnable()
	{
		EventQueue.invokeLater(()->{
			if(birthDateRange != null) {birthDateRange.setEnabled(!mainComp.getBDateState());}
		});
	}
}
