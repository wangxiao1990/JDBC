package jdbc;

public class Product {
	private int id;
	private double weight;
	private double volume;
	
	public Product() {}
	public Product(int id, double weight, double volume) {
		super();
		this.id = id;
		this.weight = weight;
		this.volume = volume;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public double getVolume() {
		return volume;
	}
	
	public void setVolume(double volume) {
		this.volume = volume;
	}
}
