package mainPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class DBaseConfigFrame extends JFrame
{
	private JButton accept;
	private JButton cancel;
	private JTextField address;
	private JTextField port;
	private JTextField user;
	private JPasswordField password;
	private JTextField baseName;
	private Properties props;
	
	public static void main(String[] args)
	{
		JFrame ramka = new DBaseConfigFrame();
		ramka.setVisible(true);
	}
	
	public DBaseConfigFrame()
	{
		address = new JTextField();
		port = new JTextField();
		user = new JTextField();
		password = new JPasswordField();
		baseName = new JTextField();
		
		JPanel inputs = new JPanel();
		inputs.setLayout(new GridLayout(5,2,2,2));
		inputs.add(new JLabel("adres bazy: "));
		inputs.add(address);
		inputs.add(new JLabel("potr: "));
		inputs.add(port);
		inputs.add(new JLabel("nazwa u¿ytkownika: "));
		inputs.add(user);
		inputs.add(new JLabel("has³o: "));
		inputs.add(password);
		inputs.add(new JLabel("nazwa bazy danych: "));
		inputs.add(baseName);
		add(inputs, BorderLayout.CENTER);
		JPanel buttons = new JPanel();
		accept = new JButton("Zatwierdz");
		accept.addActionListener(event->{saveChanges(); setVisible(false);});
		cancel = new JButton("Anuluj");
		cancel.addActionListener(event->{setVisible(false);});
		buttons.add(accept);
		buttons.add(cancel);
		add(buttons, BorderLayout.SOUTH);
		
		setTitle("Dane Logowania");
		pack();
	}
	
	private void fillFields()
	{
		props = new Properties();
		try(InputStream in = Files.newInputStream(Paths.get("db.properties")))
		{
			props.load(in);
		}catch(IOException e){e.printStackTrace();}
		
		address.setText(props.getProperty("jdbc.address", "localhost"));
		port.setText(props.getProperty("jdbc.port", "3306"));
		user.setText(props.getProperty("jdbc.user", "root"));
		password.setText(props.getProperty("jdbc.password", ""));
		baseName.setText(props.getProperty("jdbc.dbname","mysql"));
	}
	
	private void saveChanges()
	{
		props.setProperty("jdbc.address", address.getText());
		props.setProperty("jdbc.port", port.getText());
		props.setProperty("jdbc.user", user.getText());
		props.setProperty("jdbc.password", new String(password.getPassword()));
		props.setProperty("jdbc.dbname", baseName.getText());
		props.setProperty("jdbc.url", "jdbc:mysql://"+address.getText()+":"+port.getText()+"/"+baseName.getText());
		
		try {
			props.store(Files.newOutputStream(Paths.get("db.properties")),"Database configuration");
		}catch(IOException e) {e.printStackTrace();}
	}
	
	@Override
	public void setVisible(boolean b)
	{
		fillFields();
		super.setVisible(b);
	}
	
}
