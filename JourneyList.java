import java.util.ArrayList;

public class JourneyList {

	private ArrayList<Journey> journeyList;

	//Constructor
	public JourneyList () {
		journeyList = new ArrayList<Journey>();
		journeyList.add(new Journey("Loughborough", "Leicester", 2.50, 4.00, 10));
		journeyList.add(new Journey("Loughborough", "Nottingham", 1.50, 2.50, 15));
		journeyList.add(new Journey("Loughborough", "Derby", 1.25, 2.50, 23));
		journeyList.add(new Journey("Loughborough", "York", 11.50, 20.00, 60));
		journeyList.add(new Journey("Leicester", "Loughborough", 2.50, 4.00, 10));
		journeyList.add(new Journey("Leicester", "Nottingham", 3.50, 6.20, 30));
		journeyList.add(new Journey("Leicester", "Derby", 3.70, 7.00, 48));
		journeyList.add(new Journey("Leicester", "York", 23.50, 25.00, 65));
		journeyList.add(new Journey("Nottingham", "Leicester", 3.50, 6.20, 30));
		journeyList.add(new Journey("Nottingham", "Loughborough", 1.50, 2.50, 15));
		journeyList.add(new Journey("Nottingham", "Derby", 2.50, 3.00, 12));
		journeyList.add(new Journey("Nottingham", "York", 11.50, 16.00, 40));
		journeyList.add(new Journey("Derby", "Loughborough", 2.00, 2.50, 25));
		journeyList.add(new Journey("Derby", "Nottingham", 1.50, 3.00, 10));
		journeyList.add(new Journey("Derby", "Leicester", 3.70, 7.00, 48));
		journeyList.add(new Journey("Derby", "York", 7.20, 16.00, 85));		
		journeyList.add(new Journey("York", "Loughborough", 12.00, 20.00, 60));
		journeyList.add(new Journey("York", "Nottingham", 8.20, 16.00, 40));
		journeyList.add(new Journey("York", "Leicester", 12.20, 25.00, 70));
		journeyList.add(new Journey("York", "Derby", 11.20, 16.00, 75));		
	}

	//search Journies list fora specific journey
	public Journey getSpecificJourney(String from, String to){
		for(int i = 0; i < journeyList.size(); i++) {
			if (journeyList.get(i).getFrom() == from && journeyList.get(i).getTo() == to) {
				return journeyList.get(i);
			}
		}
		return null;
	}
		
}
