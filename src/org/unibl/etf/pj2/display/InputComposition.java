package org.unibl.etf.pj2.display;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.pj2.Config;
import org.unibl.etf.pj2.Simulation;
import org.unibl.etf.pj2.composition.*;

public class InputComposition extends JFrame {
	private JPanel contentPane;
	private JTextField speed;
	private JTextField tfNumberOfWagons;
	private JTextField tfNumberOfLocomotives;
	private static Composition composition;
	
	public static void addLocomotive(Locomotive l) {
		composition.addLocomotive(l);
	}
	
	public static void addWagon(Wagon w) {
		composition.addWagon(w);
	}
	
	public static Composition getComposition() {
		return composition;
	}
	
	public static int getNumberOfWagons() {
		return composition.getNumberOfWagons();
	}
	
	public static int getNumberOfLocomotives() {
		return composition.getNumberOfLocomotives();
	}
	
	public static void removeWagon() {
		composition.removeWagon();
	}
	
	public static void removeLocomotive() {
		composition.removeLocomotive();
	}
	

	public static void writeInFile() {

		try {
		try {
			File f = new File(Config.get(Config.TRAINS));
			PrintWriter pw = new PrintWriter(new FileOutputStream(
					new File(f.getPath() + File.separator + f.list().length + ".txt")));
			pw.print(String.valueOf(composition.getNumberOfLocomotives() + ","));
			int i = 0;
			for(Locomotive l : composition.getLocomotives()) {
				pw.print(String.valueOf(l.getPower()) + ";" + l.getmark() + ";" +
						String.valueOf(l.getType()) + ";" + String.valueOf(l.getEngine()));
				if(i == (composition.getLocomotives().size() - 1)) {
					pw.print(",");
				} else {
					pw.print("-");
				}
				i++;
			}
			pw.print(String.valueOf(composition.getNumberOfWagons()) + ",");
			if(composition.getNumberOfWagons() == 0) {
				pw.print("/,");
			}
			i = 0;
			for(Wagon v : composition.getWagons()) {
				pw.print(String.valueOf(v.getLength()) + ";" + v.getMark() + ";");
				if(v instanceof PassengerWagon) {
					pw.print("PW;" + String.valueOf(((PassengerWagon)v).getType()) + ";"
							+ String.valueOf(((PassengerWagon)v).getGadget().getGadget()));
				} else if(v instanceof FreightWagon) {
					pw.print("FW;" + String.valueOf(((FreightWagon)v).getMaxCapacity()));
				} else if(v instanceof ShuntingWagon) {
					pw.print("SW;");
				}
				if(i == (composition.getWagons().size() - 1)) {
					pw.print(",");
				} else {
					pw.print("-");
				}
				i++;
			}
			pw.print(String.valueOf(composition.getSpeed()) + ",");
			pw.print(String.valueOf(composition.getStartStation().getStation()) + ",");
			pw.print(String.valueOf(composition.getDestinationStation().getStation()));
			pw.close();
			
			} catch (Exception e) {
				throw new Exception("Writing composition to file failed.");
			}
		} catch (Exception e) {
			Logger.getLogger(InputComposition.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
	public InputComposition() {
		this.composition = new Composition();
		setTitle("Composition configuration");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Number of locomotives: ");
		lblNewLabel_1.setBounds(10, 36, 142, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Number of wagons:");
		lblNewLabel_2.setBounds(10, 60, 142, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Speed[ms]:");
		lblNewLabel_3.setBounds(10, 85, 133, 14);
		contentPane.add(lblNewLabel_3);
		
		speed = new JTextField();
		speed.setBounds(153, 82, 72, 20);
		contentPane.add(speed);
		speed.setColumns(10);
		
		
		JLabel lblNewLabel_4 = new JLabel("Start:");
		lblNewLabel_4.setBounds(10, 110, 124, 14);
		contentPane.add(lblNewLabel_4);
		
		JComboBox startingPosition = new JComboBox();
		startingPosition.addItem("A");
		startingPosition.addItem("B");
		startingPosition.addItem("C");
		startingPosition.addItem("D");
		startingPosition.addItem("E");
		startingPosition.addItem("EXIT_E");
		startingPosition.addItem("EXIT_A");
		startingPosition.setBounds(153, 110, 72, 22);
		contentPane.add(startingPosition);
		
		JLabel lblNewLabel_7 = new JLabel("Destination:");
		lblNewLabel_7.setBounds(10, 135, 109, 14);
		contentPane.add(lblNewLabel_7);
		
		JComboBox destinationStation = new JComboBox();
		destinationStation.addItem("A");
		destinationStation.addItem("B");
		destinationStation.addItem("C");
		destinationStation.addItem("D");
		destinationStation.addItem("E");
		destinationStation.setBounds(153, 135, 72, 22);
		contentPane.add(destinationStation);
		
		tfNumberOfWagons = new JTextField();
		tfNumberOfWagons.setBounds(153, 57, 72, 20);
		contentPane.add(tfNumberOfWagons);
		tfNumberOfWagons.setColumns(10);
		
		tfNumberOfLocomotives = new JTextField();
		tfNumberOfLocomotives.setBounds(153, 33, 72, 20);
		contentPane.add(tfNumberOfLocomotives);
		tfNumberOfLocomotives.setColumns(10);
		
		JButton btnOkButton = new JButton("OK");
		btnOkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(speed.getText().length() == 0 || 
						(Integer.valueOf(speed.getText()) < 500 || 
						Integer.valueOf(speed.getText()) > 5000)) {
					
					
					JOptionPane.showMessageDialog(contentPane, "The speed entered is not valid.");
					return;
				} else {
					composition.setSpeed(Integer.valueOf(speed.getText()));
				}
				
				if(startingPosition.getSelectedItem().equals("A")) {
					composition.setStartStation(Simulation.RailwayStations.get("A"));
				} else if(startingPosition.getSelectedItem().equals("B")) {
					composition.setStartStation(Simulation.RailwayStations.get("B"));
				} else if(startingPosition.getSelectedItem().equals("C")) {
					composition.setStartStation(Simulation.RailwayStations.get("C"));
				} else if(startingPosition.getSelectedItem().equals("D")) {
					composition.setStartStation(Simulation.RailwayStations.get("D"));
				} else if(startingPosition.getSelectedItem().equals("E")) {
					composition.setStartStation(Simulation.RailwayStations.get("E"));
				} else if(startingPosition.getSelectedItem().equals("EXIT_A")) {
					composition.setStartStation(Simulation.RailwayStations.get("F"));
				} else if(startingPosition.getSelectedItem().equals("EXIT_E")) {
					composition.setStartStation(Simulation.RailwayStations.get("G"));
				} else {
					JOptionPane.showMessageDialog(contentPane, "The starting position is not selected.");
					return;
				}
				
				if(destinationStation.getSelectedItem().equals(startingPosition.getSelectedItem())) {
					JOptionPane.showMessageDialog(contentPane, "The destination station cannot be the same as the starting station.");
					return;
				}
				
				if(destinationStation.getSelectedItem().equals("A")) {
					composition.setDestinationStation(Simulation.RailwayStations.get("A"));
				} else if(destinationStation.getSelectedItem().equals("B")) {
					composition.setDestinationStation(Simulation.RailwayStations.get("B"));
				} else if(destinationStation.getSelectedItem().equals("C")) {
					composition.setDestinationStation(Simulation.RailwayStations.get("C"));
				} else if(destinationStation.getSelectedItem().equals("D")) {
					composition.setDestinationStation(Simulation.RailwayStations.get("D"));
				} else if(destinationStation.getSelectedItem().equals("E")) {
					composition.setDestinationStation(Simulation.RailwayStations.get("E"));
				} else {
					JOptionPane.showMessageDialog(contentPane, "The destination station is not selected.");
					return;
				}
				
	
				
				
				if(tfNumberOfWagons.getText().length() == 0) {
					 JOptionPane.showMessageDialog(contentPane, "You must enter the wagon number.");
					 return;
				} else {
					composition.setNumberOfWagons(Integer.valueOf(tfNumberOfWagons.getText()));
				}
				
				
				if(tfNumberOfLocomotives.getText().length() == 0) {
					 JOptionPane.showMessageDialog(contentPane, "You must enter the locomotive number.");
					 return;
				} else {
					composition.setNumberOfLocomotives(Integer.valueOf(tfNumberOfLocomotives.getText()));
				}
				
				if(Integer.valueOf(tfNumberOfWagons.getText()) > 0 && Integer.valueOf(tfNumberOfWagons.getText()) < 50) {
					
					composition.setNumberOfWagons(Integer.valueOf(tfNumberOfWagons.getText()));
				}  else if(composition.getNumberOfWagons() == 0) {
					
				}  else {
					JOptionPane.showMessageDialog(contentPane, "Wrong number of wagons.");
					return;
				}
				if(composition.getNumberOfLocomotives() > 0 && composition.getNumberOfLocomotives() < 20) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							InputLocomotive fr = new InputLocomotive(composition.getNumberOfLocomotives());
							fr.setVisible(true);
						} catch (Exception e) {
							Logger.getLogger(InputComposition.class.getName()).log(Level.SEVERE,e.toString());
						}
					}
				});
				} else {
					JOptionPane.showMessageDialog(contentPane, "Wrong number of locomotives.");
					return;
				}
				
				/*Get out of the current window*/
				setVisible(false);
				dispose();
			}
		});
		btnOkButton.setBounds(335, 227, 89, 23);
		contentPane.add(btnOkButton);
	}
}
