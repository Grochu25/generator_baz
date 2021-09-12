package datePicker;

import java.time.*;
import java.util.*;
import java.time.format.*;
import java.time.temporal.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalendarWindow extends JComponent
{
	
	private JPanel CalendarGrid;
	private LocalDate actualDate;
	private JLabel monthName;
	private JLabel yearNumber;
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setSize(300,320);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new CalendarWindow());
		
		frame.setVisible(true);
	}
	
	public CalendarWindow()
	{
		setLayout(new BorderLayout());
		CalendarGrid = new JPanel();
		CalendarGrid.setLayout(new GridLayout(7,7));
		add(CalendarGrid, BorderLayout.CENTER);
		
		actualDate = LocalDate.now();
		fillMonth();
		
		JPanel months = new JPanel();
		add(months, BorderLayout.NORTH);
		
		monthName = new JLabel(actualDate.getMonth()+"");
		yearNumber = new JLabel(actualDate.getYear()+"");
		
		JButton nextMo = new JButton("\u2192");
		nextMo.addActionListener(event -> {actualDate = actualDate.plus(Period.ofMonths(1)); 
			monthName.setText(actualDate.getMonth()+"");
			yearNumber.setText(actualDate.getYear()+"");
			fillMonth();});
		
		JButton previousMo = new JButton("\u2190");
		previousMo.addActionListener(event -> {actualDate = actualDate.minus(Period.ofMonths(1));
		monthName.setText(actualDate.getMonth()+"");
		yearNumber.setText(actualDate.getYear()+"");
		fillMonth();});
		
		months.add(previousMo);
		months.add(monthName);
		months.add(yearNumber);
		months.add(nextMo);	
		
	}
	
	private void fillMonth()
	{
		CalendarGrid.removeAll();
		
		
		for(DayOfWeek dow : DayOfWeek.values())
		{
			JLabel day = new JLabel(dow.getDisplayName(TextStyle.SHORT, Locale.ROOT) + "", SwingConstants.CENTER);
			day.setBackground(Color.GRAY);
			day.setFont(new Font("SerafiSans",Font.BOLD,10));
			CalendarGrid.add(day);
		}
		
		int numberOfDays = 0;
		
		LocalDate firstDay = actualDate.with(TemporalAdjusters.firstDayOfMonth());
		for(int i = firstDay.getDayOfWeek().getValue()-2; i >= 0;i--)
		{
			JLabel day = new JLabel((firstDay.minus(Period.ofMonths(1)).lengthOfMonth() - i)+"", SwingConstants.CENTER);
			day.setForeground(Color.GRAY);
			day.setSize(50,48);
			day.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e)
				{
					actualDate = actualDate.minus(Period.ofMonths(1)).withDayOfMonth(Integer.parseInt(day.getText()));
					//EventQueue.invokeLater(()->{fillMonth();});
					monthName.setText(actualDate.getMonth()+""); yearNumber.setText(actualDate.getYear()+"");
					fillMonth();
					System.out.println(actualDate);
				}
			});
			//day.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			CalendarGrid.add(day);
			numberOfDays++;
		}
		
		for(int i = 1; i <= firstDay.lengthOfMonth(); i++)
		{
			JLabel day = new JLabel(i+"", SwingConstants.CENTER);
			day.setSize(50,48);
			//day.setAlignmentX(JLabel.CENTER);
			day.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e)
				{
					actualDate = actualDate.withDayOfMonth(Integer.parseInt(day.getText()));
					System.out.println(actualDate);
					fillMonth();
				}
			});
			if(i == actualDate.getDayOfMonth())day.setBackground(Color.BLUE);
			if(i == actualDate.getDayOfMonth())day.setBorder(BorderFactory.createLineBorder(Color.BLUE));
			if(i == LocalDate.now().getDayOfMonth() && actualDate.getMonth() == LocalDate.now().getMonth()) day.setForeground(Color.RED);
			CalendarGrid.add(day);
			numberOfDays++;
		}
		
		for(int i = 1; i <= 42-numberOfDays; i++)
		{
			JLabel day = new JLabel(i+"", SwingConstants.CENTER);
			day.setForeground(Color.GRAY);
			day.setSize(50,48);
			day.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e)
				{
					actualDate = actualDate.plus(Period.ofMonths(1)).withDayOfMonth(Integer.parseInt(day.getText()));
					//EventQueue.invokeLater(()->{fillMonth();});
					monthName.setText(actualDate.getMonth()+""); yearNumber.setText(actualDate.getYear()+"");
					fillMonth();
					System.out.println(actualDate);
				}
			});
			//day.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			CalendarGrid.add(day);
		}
		

		CalendarGrid.repaint();
		CalendarGrid.updateUI();
	}
}
