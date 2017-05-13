import java.util.ArrayList;

public class Journey {
	//Member variables
	private String from;
	private String to;
	private Double singleCost;
	private Double returnCost;
	private Integer duration;
	private ArrayList<String> stops;

	//Constructor
	public Journey (String from, String to, Double singleCost, Double returnCost, Integer duration) {
		this.from = from;
		this.to = to;
		this.singleCost = singleCost;
		this.returnCost = returnCost;
		this.duration = duration;
		this.stops = new ArrayList<String>();
	}

	//getters
	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public Double getSingleCost() {
		return singleCost;
	}

	public Double getReturnCost() {
		return returnCost;
	}

	public Integer getDuration() {
		return duration;
	}

	public ArrayList<String> getStops() {
		return stops;
	}

}
