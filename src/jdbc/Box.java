package jdbc;

public class Box {
	private int id;
	private double maximumWeight;
	private double volume;
	private int fulfills;
	
	public Box() {}
	public Box(int id, double maximumWeight, double volume, int fulfills) {
		super();
		this.id = id;
		this.maximumWeight = maximumWeight;
		this.volume = volume;
		this.fulfills = fulfills;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getMaximumWeight() {
		return maximumWeight;
	}
	public void setMaximumWeight(double maximumWeight) {
		this.maximumWeight = maximumWeight;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public int getFulfills() {
		return fulfills;
	}
	public void setFulfills(int fulfills) {
		this.fulfills = fulfills;
	}
}
