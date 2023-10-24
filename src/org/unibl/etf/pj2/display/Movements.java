package org.unibl.etf.pj2.display;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.pj2.Config;
import org.unibl.etf.pj2.composition.Trajectory;

public class Movements extends JFrame {

	private JPanel contentPane;
	
	public Movements() throws Exception {
		super();
		setTitle("Movements");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 465, 404);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTextPane textPane = new JTextPane();
		contentPane.add(textPane, BorderLayout.CENTER);
		
		JScrollPane scrollBar = new JScrollPane(textPane);
		contentPane.add(scrollBar);
		try {
			textPane.setText(getText());
		} catch (Exception e) {
			Logger.getLogger(Movements.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
private  String getText() throws Exception{
		
		String text = "";
		File f = new File(Config.get(Config.MOVEMENTS));
		for(String s : f.list()) {
			if(!s.endsWith(".ser")) {
				continue;
			}
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(f, s))))
	        {   
				Trajectory trajectory = (Trajectory)in.readObject();
				text += "\n\n" + s.substring(0, s.indexOf(".")) + ":\n\n";
				text += trajectory.toString();
				
	        }
	        catch(Exception e)
	        {
	        	Logger.getLogger(Movements.class.getName()).log(Level.SEVERE,e.toString());
	        }
		}
		return text;
	}
}
