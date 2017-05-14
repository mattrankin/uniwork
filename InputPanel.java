import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.Calendar;


public class InputPanel extends JPanel {
		JFrame priceFrame;
		JFrame adminFrame;
		JComboBox fromBox;
		JComboBox toBox;
		JComboBox fromBoxAdmin;
		JComboBox toBoxAdmin;
		JComboBox journeyTypeBox;
		JourneyList allJourneys;
		JButton priceMenuButton;
		JButton timeMenuButton;
		JButton calculatePriceButton;
		JButton adminButton;
		JButton routeButton;
		JPanel resultsPanel;
		JPanel adminPanel;
		JLabel resultsLabel;
		JLabel adminLabel;
		JTextField dateBox;
		JTextArea routeTextBox;
		DateFormat dayMonthYear;
		JLabel discountLabel;
		JButton adminSaveButton;


	//Constructor 
	public InputPanel() {
		

       	String[]  placeNames = {"[Select Station]","Loughborough","Leicester", "Derby", "York", "Nottingham"};
        fromBox = new JComboBox(placeNames);
        toBox = new JComboBox(placeNames);
        fromBoxAdmin = new JComboBox(placeNames);
        toBoxAdmin = new JComboBox(placeNames);
        allJourneys = new JourneyList();
        priceMenuButton = new JButton("Get Price");
        timeMenuButton = new JButton("Get Duration");
        adminButton = new JButton("Admin");
        dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        discountLabel = new JLabel("<html>10% off! <br> Last day of the Month!!");
        routeTextBox = new JTextArea(10,20);
        adminSaveButton = new JButton("Save");
        routeButton = new JButton("Get Route");
        add(fromBox);
        add(toBox);
        add(priceMenuButton);
        add(timeMenuButton);
        add(adminButton);
        add(routeButton);
        setBorder( new TitledBorder( new EtchedBorder(), "Stations"));

      
        
        priceMenuButton.addActionListener(new PriceClickAction());
        timeMenuButton.addActionListener(new TimeClickAction());
        adminButton.addActionListener(new AdminClickAction());
        routeButton.addActionListener(new RouteClickAction());
        toBoxAdmin.addActionListener(new LoadRouteAction());
        fromBoxAdmin.addActionListener(new LoadRouteAction());

        setVisible(true);
	}

	public String getCostInMoneyFormat(Double cost){
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(cost);
	}

	public String getMinutesToHoursAndMinutes(int durationInMinutes) {
			int hour = durationInMinutes / 60;
			int minute = durationInMinutes % 60;
			return hour + " hour(s) " + minute + " minutes";
	}


	public class PriceClickAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			priceFrame = new JFrame("Payment Information");
          	priceFrame.setSize(300,200);
          	priceFrame.setLocationRelativeTo(null);	
          	priceFrame.setVisible(true);
			String[] journeyType = {"[Select Journey Type]", "Single", "Return"};
        	journeyTypeBox = new JComboBox(journeyType);
    		dateBox = new JTextField();
    		calculatePriceButton = new JButton("Calculate Price");
        	calculatePriceButton.addActionListener(new CalculatePriceAction());
			

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
    			dateBox.setText(dayMonthYear.format(new Date()));
    			resultsPanel.add(dateBox);
				resultsPanel.add(journeyTypeBox);
				resultsPanel.add(calculatePriceButton);
			}       
			resultsPanel.add(resultsLabel);
          	priceFrame.add(resultsPanel);  
		}
	}

	public Boolean dateIsValid(String dateValue) {
		dayMonthYear.setLenient(false);
		try {
			Date date = dayMonthYear.parse(dateValue);
			//if the provided date is an a valid date format, but not our date format, this check will fail
			if (!dayMonthYear.format(date).equals(dateValue)) {
				return(false);

			} else return(true);
		} 
		//if provided date is not a valid date, it will break the code above when it tries
		//to parse it as a date
		catch (Exception e) {
			return false;
		}
	}

	public Boolean dateIsLastDayOfMonth(String dateValue) {
		try{
			Date today = dayMonthYear.parse(dateValue);
			Calendar cal = Calendar.getInstance();
			cal.setTime(today);
			cal.add(Calendar.DATE, 1); //add 1 day to today's date to get tomorrows date
			int tomorrow = cal.get(Calendar.DAY_OF_MONTH);
			if (tomorrow == 1) {
				return true;
			} else return false;
		
		} catch (Exception e){
			return false;
		}
	}

	

	public class TimeClickAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			JFrame timeFrame = new JFrame("Travel Time Information");
			timeFrame.setLocationRelativeTo(null);
          	timeFrame.setVisible(true);
          	timeFrame.setSize(300,200);
		
          	String fromValue = fromBox.getSelectedItem().toString();
          	String toValue = toBox.getSelectedItem().toString();

          	resultsPanel = new JPanel();
          	resultsLabel = new JLabel();

          	if (fromValue == "[Select Station]" || toValue == "[Select Station]") {

          		resultsLabel.setText("<html> Invalid input: <br>   Please select stations");
          		add(resultsLabel,SwingConstants.CENTER);
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));

          	}   else if (fromValue == toValue) { 
				resultsLabel.setText("Destinations cannot be the same");
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));



          	}	else {
          		Integer duration = allJourneys.getSpecificJourney(fromValue, toValue).getDuration();
          	 	resultsLabel = new JLabel("One way journey: " + getMinutesToHoursAndMinutes(duration));
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Duration"));


			}
			
          	timeFrame.add(resultsPanel);
          	resultsPanel.add(resultsLabel); 
         }

	}

	public class CalculatePriceAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			String fromValue = fromBox.getSelectedItem().toString(); 
          	String toValue = toBox.getSelectedItem().toString();
			String typeValue = journeyTypeBox.getSelectedItem().toString();
			String dateValue = dateBox.getText().toString(); //The String value of the text in the dateBox
			Boolean dateIsLastDay = dateIsLastDayOfMonth(dateValue);
			Double discountMultiplier = 1.0;

          	if (dateIsValid(dateValue) == false ) {
          		resultsLabel.setText("Please enter a valid date");
          	} else {
          		//If date is valid, run all this code which displays the price
          		if (dateIsLastDayOfMonth(dateValue) == true) {
          			discountMultiplier = 0.9;
          			resultsPanel.add(discountLabel);
          			discountLabel.setVisible(true);

          		} else if (dateIsLastDayOfMonth(dateValue) == false) {
          			discountLabel.setVisible(false);
          		} 

				if (typeValue == "Single") {
	          		Double cost = allJourneys.getSpecificJourney(fromValue, toValue).getSingleCost();
	          	 	resultsLabel.setText("Price of Single:  £" + getCostInMoneyFormat(cost * discountMultiplier));
				} else if (typeValue == "Return") {
					Double cost = allJourneys.getSpecificJourney(fromValue, toValue).getReturnCost();
		         	resultsLabel.setText("Price of Return:  £" + getCostInMoneyFormat(cost * discountMultiplier));
				} else {
					resultsLabel.setText("Select single or return");
				}
			}


			

			//Refresh the panel with the updated label
			resultsPanel.revalidate(); 

		}
	} 

	public class AdminClickAction implements ActionListener {

			public void actionPerformed (ActionEvent e) {
				adminFrame = new JFrame("Administrator");
				adminFrame.setSize(300,500);
				adminFrame.setLocationRelativeTo(null);
				adminFrame.setVisible(true);

				adminPanel = new JPanel();
				adminLabel = new JLabel();
				adminPanel.setBorder( new TitledBorder( new EtchedBorder(), "Make route"));
				adminPanel.add(fromBoxAdmin);
				adminPanel.add(toBoxAdmin);
				adminLabel.setText("Please select stations (above)");
				adminPanel.add(adminLabel);
				adminPanel.add(routeTextBox);
				adminPanel.add(adminSaveButton);
				adminFrame.add(adminPanel);
				
				adminSaveButton.addActionListener(new SaveRouteAction());
			}
	}

   public class SaveRouteAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			String fromValue = fromBoxAdmin.getSelectedItem().toString(); 
          	String toValue = toBoxAdmin.getSelectedItem().toString();
          	//get the specific jouney
          	Journey newjourney = allJourneys.getSpecificJourney(fromValue, toValue);



          	//get the text from the textArea and put it in an ArrayList<String>
          	String txt = routeTextBox.getText();
          	String[] arrayOfLines = txt.split("\\r?\\n");
          	ArrayList<String> newRouteList = new ArrayList<>(Arrays.asList(arrayOfLines));

          	//set the stops for that journey as the arraylist
          	newjourney.setStops(newRouteList);

          	//the line below should print out the stops
          	System.out.println(newjourney.getStops());

		}
	}

	public class LoadRouteAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			String fromValue = fromBoxAdmin.getSelectedItem().toString(); 
          	String toValue = toBoxAdmin.getSelectedItem().toString();
			if (fromValue == "[Select Station]" || toValue == "[Select Station]") {
				adminLabel.setText("Please select stations (above)");
			} else if(fromValue == toValue){
				adminLabel.setText("Please select different stations (above)");
			} else {
				ArrayList<String> stops = allJourneys.getSpecificJourney(fromValue, toValue).getStops();
				//set route box to the journies stops, by converting the array list into a string and putting a new line 
				//in between each stop.
				routeTextBox.setText(String.join("\n", stops));
				adminLabel.setText("Enter stops below, one stop per line");

			}			

			//Refresh the panel with the updated label
			adminPanel.revalidate(); 

		}
	}    

  public class RouteClickAction implements ActionListener {

	public void actionPerformed (ActionEvent e) {
			JFrame routeFrame = new JFrame("Your Route");
			routeFrame.setLocationRelativeTo(null);
          	routeFrame.setVisible(true);
          	routeFrame.setSize(300,200);
		
          	String fromValue = fromBox.getSelectedItem().toString();
          	String toValue = toBox.getSelectedItem().toString();

          	resultsPanel = new JPanel();
          	resultsLabel = new JLabel();

          	if (fromValue == "[Select Station]" || toValue == "[Select Station]") {

          		resultsLabel.setText("<html> Invalid input: <br>   Please select stations");
          		add(resultsLabel,SwingConstants.CENTER);
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));

          	}   else if (fromValue == toValue) { 
				resultsLabel.setText("Destinations cannot be the same");
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));



          	}	else {
          		ArrayList<String> newRoute = allJourneys.getSpecificJourney(fromValue, toValue).getStops();

          	 	resultsLabel = new JLabel("Route: " + (newRoute));
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), " "));


			}
			
          	routeFrame.add(resultsPanel);
          	resultsPanel.add(resultsLabel); 

		}
	}

}







