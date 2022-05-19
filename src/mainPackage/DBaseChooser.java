package mainPackage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DBaseChooser extends JFrame
{
	private JButton accept;
	private static String name;
	private static String field;
	private JComboBox<String> cols;
	
	public DBaseChooser(PickComp parent)
	{
		GridBagConstraints gblCons = new GridBagConstraints();
		gblCons.insets = new Insets(20,3,5,20);
		gblCons.anchor = GridBagConstraints.EAST;
		
		JPanel options = new JPanel();
		options.setLayout(new GridBagLayout());
		JComboBox<String> tables = new JComboBox<>();
		tables.addActionListener(event->{name = (String)tables.getSelectedItem(); showFields();});
		cols = new JComboBox<>();
		cols.setEnabled(false);
		cols.addActionListener(event->{field = (String)cols.getSelectedItem();});
		
		options.add(new JLabel("Nazwa tabeli: "), gblCons); gblCons.gridy = 1; gblCons.insets = new Insets(5,3,20,20);
		options.add(new JLabel("Klucz g³ówny (liczba): "), gblCons); gblCons.gridx = 1; gblCons.gridy = 0; gblCons.insets = new Insets(20,20,5,3); gblCons.anchor = GridBagConstraints.WEST;
		options.add(tables, gblCons); gblCons.gridy = 1; gblCons.insets = new Insets(5,20,20,3);
		options.add(cols, gblCons);
		
		accept = new JButton("OK");
		accept.addActionListener(event->{parent.readyDB();dispose();});
		JButton cancel = new JButton("Anuluj");
		
		JPanel buttonP = new JPanel();
		buttonP.add(accept); 
		buttonP.add(cancel);
		buttonP.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
		
		JPanel topText = new JPanel();
		topText.setLayout(new GridBagLayout()); gblCons = new GridBagConstraints(); gblCons.anchor=GridBagConstraints.CENTER;
		topText.add(new JLabel("Bazê adnych nale¿y ustawiæ w zak³adce"),gblCons);gblCons.gridy=1;
		topText.add(new JLabel("\"Baza Danych\">\"Ustawienia Bazy Danych\""),gblCons);
		topText.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		
		add(topText, BorderLayout.NORTH);
		add(options);
		add(buttonP, BorderLayout.SOUTH);
		
		try(Connection conn = PickComp.getConnection(); Statement stat = conn.createStatement())
		{
			ResultSet rs = stat.executeQuery("SHOW TABLES;");
			while(rs.next())
			{
				tables.addItem(rs.getString(1));
			}
		}
		catch(SQLException ex)
		{
			MainFrame.sqlMessage(ex.getMessage());
		}catch(IOException e) {e.printStackTrace();}
		
		setVisible(true);
		pack();
	}
	
	private void showFields()
	{
		try(Connection conn = PickComp.getConnection();)
		{
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM "+name);
			ResultSetMetaData rsmd = rs.getMetaData();
			
			cols.removeAllItems();
			for(int i=1;i<=rsmd.getColumnCount();i++)
				cols.addItem(rsmd.getColumnName(i));
			EventQueue.invokeLater(()->repaint());
		}
		catch(SQLException ex)
		{
			MainFrame.sqlMessage(ex.getMessage());
		}catch(IOException e) {e.printStackTrace();}
		
		if(cols.getItemCount()>0 && !CustomMenuBar.isOverrideSet())
			cols.setEnabled(true);
	}
	
	public static String getBaseName()
	{
		return name;
	}
	
	public static String getID()
	{
		return field;
	}
}
