import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.Calendar;


public class InputPanel extends JPanel {
	//global variables 
	JFrame priceFrame;
	JFrame adminFrame;
	JComboBox fromBox;
	JComboBox toBox;
	JComboBox fromBoxAdmin;
	JComboBox toBoxAdmin;
	JComboBox journeyTypeBox;
	JComboBox sortBox;
	JourneyList allJourneys;
	JButton priceMenuButton;
	JButton timeMenuButton;
	JButton calculatePriceButton;
	JButton adminButton;
	JButton routeButton;
	JButton btnExit;
	JButton backToMainButton;
	JPanel resultsPanel;
	JPanel adminPanel;
	JPanel routePanel;
	JLabel resultsLabel;
	JLabel adminLabel;
	JLabel routeLabel;
	JTextField dateBox;
	JTextArea routeTextBox;
	DateFormat dayMonthYear;
	JLabel discountLabel;
	JButton adminSaveButton;


	//Constructor 
	public InputPanel() {
		
		//setLayout(new BorderLayout());
		//initialise variables
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
        routeButton = new JButton("Get Route");
        btnExit = new JButton("Quit");
        backToMainButton = new JButton("Back");

        
		add(fromBox);
        add(toBox);
        add(priceMenuButton);
        add(timeMenuButton);
        add(routeButton);
        add(adminButton);
        add(btnExit);
        //add main menu GUI elements to panel
       /* add(fromBox,BorderLayout.NORTH);
        add(toBox,BorderLayout.NORTH);
        add(priceMenuButton, BorderLayout.CENTER);
        add(timeMenuButton, BorderLayout.EAST);
        add(routeButton, BorderLayout.WEST);
        add(adminButton, BorderLayout.SOUTH);
        add(btnExit, BorderLayout.SOUTH);*/



        setBorder( new TitledBorder( new EtchedBorder(), "Main Menu"));

		//set up action listners on the main menu GUI elements        
        priceMenuButton.addActionListener(new PriceClickAction());
        btnExit.addActionListener(new ExitButtonAction());
        timeMenuButton.addActionListener(new TimeClickAction());
        adminButton.addActionListener(new AdminClickAction());
        routeButton.addActionListener(new RouteClickAction());
        toBoxAdmin.addActionListener(new LoadRouteAction());
        fromBoxAdmin.addActionListener(new LoadRouteAction());

        setVisible(true);
	}

	//convert double to money format for displaying price, for example this turns 2.5 into 2.50
	public String getCostInMoneyFormat(Double cost){
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(cost);
	}

	//convert a numer of minutes into an hours and minutes format
	public String getMinutesToHoursAndMinutes(int durationInMinutes) {
		int hour = durationInMinutes / 60;
		int minute = durationInMinutes % 60;
		return hour + " hour(s) " + minute + " minutes";
	}

	//Take an Array list of strings (stops), sort them using bubble sort then return ordered array list
	public ArrayList<String> sortRouteAlphabetically(ArrayList<String> stops) {

		 ArrayList<String> sortedStops = new ArrayList<String>();
		 String [] stopsArray = stops.toArray(new String[stops.size()]);
		 String temp;
		 for (int i=0; i< stopsArray.length; i++){
			 for(int j= i; j< stopsArray.length-1; j++){
			 	char a = stopsArray[i].charAt(0);
			 	char b = stopsArray[j+1].charAt(0);
				if (a > b)  {
					 temp = stopsArray[j+1];
					 stopsArray[j+1] = stopsArray[i];        
					 stopsArray[i] = temp;
				}
	   		 }
        	sortedStops.add(stopsArray[i]);
    	 }

    	return sortedStops;
	}

	//when "Get Price" is clicked
	public class PriceClickAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			//set up price information window
			priceFrame = new JFrame("Payment Information");
          	priceFrame.setSize(300,200);
          	priceFrame.setLocationRelativeTo(null);	
          	priceFrame.setVisible(true);
			String[] journeyType = {"[Select Journey Type]", "Single", "Return"};
        	journeyTypeBox = new JComboBox(journeyType);
    		dateBox = new JTextField();

    		calculatePriceButton = new JButton("Calculate Price");
        	calculatePriceButton.addActionListener(new CalculatePriceAction());
			
        	//get from station and to station from the relevant boxes
          	String fromValue = fromBox.getSelectedItem().toString();
          	String toValue = toBox.getSelectedItem().toString();
          
          	//initialise a new panel for the price window
          	resultsPanel = new JPanel();
          	resultsLabel = new JLabel();

          	// if the from station or the to station has not been set, set the label text to an error
          	// else if the from station and the to station are the same, set the label text to an error
          	// otherwise add the rest of the elements needed to calculate the price to the 
          	// result panel (date, journeyType, calculate price button)

          	if (fromValue == "[Select Station]" || toValue == "[Select Station]" ) {
          		resultsLabel.setText("<html> Invalid input: <br>Please select stations and a valid journey type.");
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));
          	}
          	else if (fromValue == toValue) { 
				resultsLabel.setText("Destinations cannot be the same");
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));
          	} 
          	else {
				resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Price"));				
    			dateBox.setText(dayMonthYear.format(new Date()));
    			resultsPanel.add(dateBox);
				resultsPanel.add(journeyTypeBox);
				resultsPanel.add(calculatePriceButton);
			}

			//add the label to the panel and then the panel to the frame       
			resultsPanel.add(resultsLabel);
          	priceFrame.add(resultsPanel);  
		}
	}

	//ensure that a date is a valid date and in the right format 
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

	//this function figures out if a date is the last day of the month. It does this by 
	//taking the date and adding 1 day to it. If the new date is the 1st of the month,
	//it knows that the day before must have been the last day of the month so it returns
	//true
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

	
	//when "Get Duration" is clicked
	public class TimeClickAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			//initialise frame
			JFrame timeFrame = new JFrame("Travel Time Information");
			timeFrame.setLocationRelativeTo(null);
          	timeFrame.setVisible(true);
          	timeFrame.setSize(300,200);
		
			//get from station and to station
          	String fromValue = fromBox.getSelectedItem().toString();
          	String toValue = toBox.getSelectedItem().toString();

          	resultsPanel = new JPanel();
          	resultsLabel = new JLabel();

          	//if the from station or the to station has not been set, set the label text to an error,
			// else if the from station and the to station are the same, set the label text to an error,
			// otherwise, set the label text to the duration of the journey 
          	if (fromValue == "[Select Station]" || toValue == "[Select Station]") {

          		resultsLabel.setText("<html> Invalid input: <br>   Please select stations");
          		add(resultsLabel,SwingConstants.CENTER);
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));

          	}
		   	else if (fromValue == toValue) { 
				resultsLabel.setText("Destinations cannot be the same");
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));

          	}	
          	else {
          		Integer duration = allJourneys.getSpecificJourney(fromValue, toValue).getDuration();
          	 	resultsLabel = new JLabel("One way journey: " + getMinutesToHoursAndMinutes(duration));
          		resultsPanel.setBorder( new TitledBorder( new EtchedBorder(), "Duration"));


			}
			//add the label to the panel and then the panel to the frame       
          	timeFrame.add(resultsPanel);
          	resultsPanel.add(resultsLabel); 
         }

	}

	//when "calculate price" is clicked
	public class CalculatePriceAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			//get the from station, to staion, journey type and date
			String fromValue = fromBox.getSelectedItem().toString(); 
          	String toValue = toBox.getSelectedItem().toString();
			String typeValue = journeyTypeBox.getSelectedItem().toString();
			String dateValue = dateBox.getText().toString();


			Boolean dateIsLastDay = dateIsLastDayOfMonth(dateValue);
			Double discountMultiplier = 1.0;

			//if the date isn't valid, set the label text to "Please enter a valid date",
			// else if date is valid, run the code which displays the price
          	if (dateIsValid(dateValue) == false ) {
          		resultsLabel.setText("Please enter a valid date is dd/mm/yyyy format");
          	} 
          	else {
          		//if it is the last day of the month, set the discount multiplier and add the discount
          		//label to the panel, else hide the discount label
          		if (dateIsLastDayOfMonth(dateValue) == true) {
          			discountMultiplier = 0.9;
          			resultsPanel.add(discountLabel);
          			discountLabel.setVisible(true);

          		} 
          		else if (dateIsLastDayOfMonth(dateValue) == false) {
          			discountLabel.setVisible(false);
          		} 
          		//Calculate the right price depending on whether single or return is set
				if (typeValue == "Single") {
	          		Double cost = allJourneys.getSpecificJourney(fromValue, toValue).getSingleCost();
	          	 	resultsLabel.setText("Price of Single:  £" + getCostInMoneyFormat(cost * discountMultiplier));
				} else if (typeValue == "Return") {
					Double cost = allJourneys.getSpecificJourney(fromValue, toValue).getReturnCost();
		         	resultsLabel.setText("Price of Return:  £" + getCostInMoneyFormat(cost * discountMultiplier));
				}
				 else {
					resultsLabel.setText("Select single or return");
				}
			}

			//Refresh the panel with the updated label
			resultsPanel.revalidate(); 

		}
	} 

	//when the "admin" button is clicked
	public class AdminClickAction implements ActionListener {

			public void actionPerformed (ActionEvent e) {
				//initialise the admin frame
				adminFrame = new JFrame("Administrator");
				adminFrame.setSize(300,500);
				adminFrame.setLocationRelativeTo(null);
				adminFrame.setVisible(true);

				//set up the admin panel
				adminPanel = new JPanel();
				adminLabel = new JLabel();
				adminSaveButton = new JButton("Save");

				adminPanel.setBorder( new TitledBorder( new EtchedBorder(), "Make route"));
				adminPanel.add(fromBoxAdmin);
				adminPanel.add(toBoxAdmin);
				adminLabel.setText("Please select stations (above)");
				adminPanel.add(adminLabel);
				adminPanel.add(routeTextBox);
				adminPanel.add(adminSaveButton);
				adminPanel.add(backToMainButton);

				adminFrame.add(adminPanel);
				

				backToMainButton.addActionListener(new BackToMainAction());
				adminSaveButton.addActionListener(new SaveRouteAction());
			}
	}

	//when "Save" is clicked
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

          	//save the stops for that journey as the arraylist of the stops entered in the box
          	newjourney.setStops(newRouteList);
		}
	}

	//When a station is selected from the From and To JComboBoxes on the admin page
	public class LoadRouteAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			//get the stations
			String fromValue = fromBoxAdmin.getSelectedItem().toString(); 
          	String toValue = toBoxAdmin.getSelectedItem().toString();

          	//if the stations are not selected set the label text to an error, 
          	// else if the stations selected are the same set the label text to an error,
          	// else show the stops for the selected journey
			if (fromValue == "[Select Station]" || toValue == "[Select Station]") {
				adminLabel.setText("Please select stations (above)");
			} else if(fromValue == toValue){
				adminLabel.setText("Please select different stations (above)");
			} else {
				//get the ArrayList of stops for the specific journey selected
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

	//when the "Get Route" button is clicked
	public class RouteClickAction implements ActionListener {

		public void actionPerformed (ActionEvent e) {
			//initialise the frame
			JFrame routeFrame = new JFrame("Your Route");
			routeFrame.setLocationRelativeTo(null);
          	routeFrame.setVisible(true);
          	routeFrame.setSize(300,200);
		
			//get the from station and to station
          	String fromValue = fromBox.getSelectedItem().toString();
          	String toValue = toBox.getSelectedItem().toString();
          	String[] sortType = {"In Order", "Alphabetical Order"};
        	sortBox = new JComboBox(sortType);
        	sortBox.addActionListener(new SortBoxAction());


          	//intiialise the panel and label
          	routePanel = new JPanel();
          	routeLabel = new JLabel();
          	routePanel.setLayout(new BorderLayout());

          	//if the from or to stations haven't been selected, set the label to an error,
          	//else if the from station is the same as the to staiton, set the label to an error,
          	//else set the label as the route of the journey 
          	if (fromValue == "[Select Station]" || toValue == "[Select Station]") {
          		routeLabel.setText("<html> Invalid input: <br>   Please select stations");
          		add(routeLabel,SwingConstants.CENTER);
          		routePanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));

          	}   else if (fromValue == toValue) { 
				routeLabel.setText("Destinations cannot be the same");
          		routePanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));
          	}	else {
          		ArrayList<String> newRoute = allJourneys.getSpecificJourney(fromValue, toValue).getStops();
          	 	routeLabel.setText("<html>Intermediate Stops: <br>" + (newRoute));
          		routePanel.setBorder( new TitledBorder( new EtchedBorder(), " "));
			}
			
			//add the panel to the frame and the label to the panel
          	routeFrame.add(routePanel);
          	routePanel.add(routeLabel, BorderLayout.NORTH); 
          	routePanel.add(sortBox, BorderLayout.CENTER);



		}
	}

	public class SortBoxAction implements ActionListener {

        public void actionPerformed(ActionEvent e){
        	
        	//get the from station, to station and sort type
          	String fromValue = fromBox.getSelectedItem().toString();
          	String toValue = toBox.getSelectedItem().toString();
        	String sortValue = sortBox.getSelectedItem().toString();

			//if the from or to stations haven't been selected, set the label to an error,
          	//else if the from station is the same as the to staiton, set the label to an error,
          	//else set the label as the route of the journey 
          	if (fromValue == "[Select Station]" || toValue == "[Select Station]") {
          		routeLabel.setText("<html> Invalid input: <br>   Please select stations");
          		add(routeLabel,SwingConstants.CENTER);
          		routePanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));

          	}   else if (fromValue == toValue) { 
				routeLabel.setText("Destinations cannot be the same");
          		routePanel.setBorder( new TitledBorder( new EtchedBorder(), "Error"));
          	}	else {
          		ArrayList<String> newRoute = allJourneys.getSpecificJourney(fromValue, toValue).getStops();
          	 	if (sortValue == "Alphabetical Order") {
          	 		routeLabel.setText("<html>Intermediate Stops: <br>" + (sortRouteAlphabetically(newRoute)));
          	 	}
          	 	else { 

          	 		routeLabel.setText("<html>Intermediate Stops: <br>" + (newRoute));
          		}
				routePanel.setBorder( new TitledBorder( new EtchedBorder(), " "));
				routePanel.revalidate();
			}
		}
		
	}



	public class ExitButtonAction implements ActionListener {

        public void actionPerformed(ActionEvent e){
           //Close program
        	System.exit(0);

        }
	}

	public class BackToMainAction implements ActionListener {

		public void actionPerformed(ActionEvent e){
          adminFrame.dispose();
        }  

    }  
}







