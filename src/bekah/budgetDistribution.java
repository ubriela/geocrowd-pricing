package bekah;
import java.util.Random;

public class budgetDistribution {
	
	/**
	 * 
	 * @param N : number of pictures
	 * @param budget
	 * @return
	 */
	public static Distribution simulate(int N, double budget) {
		int[] pictures = new int[N];
		double[] prices = new double[N];
		double moneySpent = 0;
		Random r = new Random();
		while (budget - moneySpent > 0) {
			r.setSeed(System.nanoTime());
			double p = r.nextDouble();
			if (budget - moneySpent > p) {
				r.setSeed(System.nanoTime());
				int location = r.nextInt(N);
				prices[location] = p;
				pictures[location]++;
				moneySpent += prices[location];
			} else {
				break;

			}
		}
		return new Distribution(pictures, prices, moneySpent, budget);
	}

	public static void print(Distribution dist) {
		System.out.print("Picture Distribution: {");
		for (int i = 0; i < dist.getPictures().length; i++) {
			if (i == dist.getPictures().length - 1) {
				System.out.print(dist.getPictures()[i] + " }");
			} else {
				System.out.print(dist.getPictures()[i] + ", ");
			}
		}
		System.out.println();
		System.out.print("Spending Distribution: {");
		for (int i = 0; i < dist.getLocationPrice().length; i++) {
			if (i == dist.getLocationPrice().length - 1) {
				System.out.print(dist.getLocationPrice()[i] + " }");
			} else {
				System.out.print(dist.getLocationPrice()[i] + ", ");
			}
		}
		System.out.println();
		System.out.println("Budget limit: " + dist.getBudget());
		System.out.println("Spending: " + dist.getTotalSpent());
		System.out.println("Remaining Budget: "
				+ (dist.getBudget() - dist.getTotalSpent()));
		System.out
				.println("Total number of pics taken: " + dist.totalNumPics());

	}

	public static void main(String[] args) {
		print(simulate(30, 10000));
	}
}
