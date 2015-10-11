package jdbc;

public class Containment {
	private int contains;
	private int isContainedIn;
	private int count;
	
	public Containment() {}
	public Containment(int contains, int isContainedIn, int count) {
		super();
		this.contains = contains;
		this.isContainedIn = isContainedIn;
		this.count = count;
	}
	
	public int getContains() {
		return contains;
	}
	
	public void setContains(int contains) {
		this.contains = contains;
	}
	
	public int getIsContainedIn() {
		return isContainedIn;
	}
	
	public void setIsContainedIn(int isContainedIn) {
		this.isContainedIn = isContainedIn;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
}
