package bekah;
public class Distribution {
private int[] pictures;
private double[] locationPrice;
private double totalSpent; 
private double budget;

public Distribution(int[] p, double[] prices, double ts, double b) {
	pictures = p;
	locationPrice = prices;
	totalSpent = ts; 
	budget = b;
}

public int[] getPictures() {
	return pictures;
}

public void setPictures(int[] p) {
	pictures = p;
}

public double[] getLocationPrice() {
	return locationPrice;
}

public void setLocationPrice(double prices[]) {
	locationPrice = prices;
}

public double getTotalSpent() {
	return totalSpent;
}

public void setTotalSpent(double ts) {
	totalSpent = ts;
}

public double getBudget() {
	return budget; 
}

public void setBudget(double b) {
	budget = b;
}

public int totalNumPics() {
	int total = 0; 
	for(int i = 0; i < pictures.length; i++) {
		total+=pictures[i];
	}
	return total;
}
}