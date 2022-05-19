package mainPackage;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.*;

public class CustomsMenu extends JFrame
{

	private String[] nazwy;
	private JList<String> fList;
	private JPanel panel;
	
	public CustomsMenu(JPanel panel)
	{
		this.panel = panel;
		setTitle("Usuwanie plików");
		aktualizujMape();
		
		setVisible(true);
		pack();
		setSize(getWidth()+50, getHeight());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}

	private void aktualizujMape()
	{
		File root = new File("sources/customs");
		File[] files = root.listFiles();
		nazwy = new String[files.length];
		
		for(int i=0;i<files.length;i++)
			nazwy[i] = files[i].getName();
		
		wyswietlNazwy();
	}
	
	private void wyswietlNazwy()
	{
		fList = new JList<String>(nazwy);
		fList.addListSelectionListener(new ListSelectionListener() {public void valueChanged(ListSelectionEvent e) {
			if(fList.getSelectedValue()!=null && JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz usun¹æ pole "+fList.getSelectedValue(), "Czy na pewno", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				Generator.deleteFromCustoms(fList.getSelectedValue());
				JButton por; int i=-1;
				do
				{
					i++;
					por = (JButton)panel.getComponent(i);
				}while(! por.getText().equals(fList.getSelectedValue().substring(0, fList.getSelectedValue().length()-4)));
				panel.remove(i); 
				EventQueue.invokeLater(()->{panel.updateUI();panel.repaint();});
				dispose();
			}
		}});
				
		add(fList);
	}
	
}
