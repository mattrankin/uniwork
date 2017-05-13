import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class InputPanel extends JPanel {

	//Constructor 
	public InputPanel() {
		

        String[] placeNames = {"Loughborough","Leicester", "Derby", "York", "Nottingham"};
        JComboBox fromBox = new JComboBox(placeNames);
        JComboBox toBox = new JComboBox(placeNames);
        JButton priceButton = new JButton("Get Price");
        JButton routeButton = new JButton("View Route");
        add(fromBox);
        add(toBox);
        add(priceButton);
        System.out.println("here");
        //add(routeButton);
        priceButton.addActionListener(new PriceClickAction());
        setVisible(true);

	}

	public static class PriceClickAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			System.out.println("here2");
			JFrame frame2 = new JFrame("Your Route");
          	frame2.setVisible(true);
          	frame2.setSize(200,200);

          
          
          	JLabel label = new JLabel("you clicked me");
          	JPanel panel = new JPanel();
          	frame2.add(panel);
          	panel.add(label); 
		}
	}


}