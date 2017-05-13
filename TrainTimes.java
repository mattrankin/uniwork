import javax.swing.*;
import java.awt.*;

public class TrainTimes {

	public static void main(String[] args) {

		JourneyList allJourneys = new JourneyList();
		System.out.println(allJourneys.getSpecificJourney("Loughborough", "York").getSingleCost());
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainWindow mainWindow = new MainWindow();
			}
		});
	}
 
}