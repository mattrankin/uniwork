import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;


public class InputPanel extends JPanel {
		JComboBox fromBox;
		JComboBox toBox;
		JComboBox journeyTypeBox;
		JourneyList allJourneys;
		JButton priceButton;
		JButton timeButton;
		JPanel resultsPanel;
		JLabel resultsLabel;

	//Constructor 
	public InputPanel() {
		

        String[] placeNames = {"[Select Station]","Loughborough","Leicester", "Derby", "York", "Nottingham"};
        fromBox = new JComboBox(placeNames);
        toBox = new JComboBox(placeNames);
        allJourneys = new JourneyList();
        priceButton = new JButton("Get Price");
        timeButton = new JButton("Get Time");
        add(fromBox);
        add(toBox);
        add(priceButton);
        add(timeButton);
        setBorder( new TitledBorder( new EtchedBorder(), "Stations"));

      
        
        priceButton.addActionListener(new PriceClickAction());
        timeButton.addActionListener(new TimeClickAction());
        setVisible(true);
	}

	public String getCostInMoneyFormat(Double c){
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(c);
	}



	public class PriceClickAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
		
			String[] journeyType = {"[Select Journey Type]", "Single", "Return"};
        	journeyTypeBox = new JComboBox(journeyType);
        	journeyTypeBox.addActionListener(new ComboBoxAction());
			JFrame priceFrame = new JFrame("Payment Information");
          	priceFrame.setVisible(true);
          	priceFrame.setSize(300,200);

			String typeValue = journeyTypeBox.getSelectedItem().toString();
          	String fromValue = fromBox.getSelectedItem().toString();
          	String toValue = toBox.getSelectedItem().toString();
          	resultsPanel = new JPanel();
          	resultsLabel = new JLabel();
          	if (fromValue == "[Select Station]" || toValue == "[Select Station]" ) {

          		resultsLabel.setText("<html> Invalid input: <br>Please select stations and a valid journey type.");
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));


          	}   else if (fromValue == toValue) { 
				resultsLabel.setText("Destinations cannot be the same");
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));


          	} else {
				resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Price"));
				resultsPanel.add(journeyTypeBox);
			}       
			resultsPanel.add(resultsLabel);
          	priceFrame.add(resultsPanel);  
		}
	}

	public class TimeClickAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			JFrame timeFrame = new JFrame("Travel Time Information");
          	timeFrame.setVisible(true);
          	timeFrame.setSize(300,200);

			
          	String fromValue = fromBox.getSelectedItem().toString();
          	String toValue = toBox.getSelectedItem().toString();
          	JLabel label;
          	resultsPanel = new JPanel();
          	if (fromValue == "[Select Station]" || toValue == "[Select Station]") {

          		resultsLabel = new JLabel("<html> Invalid input: <br>   Please select stations");
          		add(resultsLabel,SwingConstants.CENTER);
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));

          	}   else if (fromValue == toValue) { 
				resultsLabel = new JLabel("Destinations cannot be the same");
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));



          	}	else {
          		Integer duration = allJourneys.getSpecificJourney(fromValue, toValue).getDuration();
          	 	resultsLabel = new JLabel("Duration of journey one way: " + duration);
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Duration"));


			}
				

          	//TODO: if the to vale or from value is "To Selected" , or if they are the same, don't run the next bit of code 

          	timeFrame.add(resultsPanel);
          	resultsPanel.add(resultsLabel); 
         }

	}

	public class ComboBoxAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			
			String fromValue = fromBox.getSelectedItem().toString();
          	String toValue = toBox.getSelectedItem().toString();
			String typeValue = journeyTypeBox.getSelectedItem().toString();

			if (typeValue == "Single") {
          		Double cost = allJourneys.getSpecificJourney(fromValue, toValue).getSingleCost();
          	 	resultsLabel.setText("Price of Single:  £" + getCostInMoneyFormat(cost));
                resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Price"));

			}   else if (typeValue == "Return") {
				Double cost = allJourneys.getSpecificJourney(fromValue, toValue).getReturnCost();
	         	resultsLabel.setText("Price of Return:  £" + getCostInMoneyFormat(cost));
	    		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Price"));
			} else {
				resultsLabel.setText("Select single or return");
				resultsPanel.add(resultsLabel); 
			}
			resultsPanel.revalidate();

		}
	}
}