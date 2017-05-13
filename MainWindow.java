import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
	
	//Contructor
	public MainWindow() {
        String[] placeNames = {"Loughborough", "Leicester", "Derby", "York", "Nottingham"};
        JComboBox fromBox = new JComboBox(placeNames);
        JComboBox toBox = new JComboBox(placeNames);
        InputPanel inputPanel = new InputPanel();
        add(inputPanel);
        setSize(400,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

        revalidate();
	}
}