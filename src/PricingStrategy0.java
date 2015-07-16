import java.util.Random;

/***
 * The first strategy is to give a fixed reward for each task. For example,
 * giving a set of reward values (e.g., 0, .1, .5, 1), we randomly set a random
 * reward value for each task. The reward adjustment can be executed after a
 * certain time period (e.g., 5 minutes). All the reward values will be set to
 * zero when running out of budget.
 *
 */
public class PricingStrategy0 {

	public static void main(String[] args) throws Exception {
		PricingStrategy0 strat = new PricingStrategy0();
		double W = 1000;// total budget
		int k = 200; // max number of pics per location that we are willing to
						// pay for.
		int L = 20; // number of locations

		// Flat rate per pic, aka delta in the word file.
		double flatRate = ((double) W) / (k * L);

		// the maximum factor m, which determines the maximum price per pic that
		// we are willing to pay
		// a user for the pic in order to increase the coverage. This applies
		// when some locations do not have
		// any pics, or have very few pics.
		double m = 5.0;

		System.out.println("Budget: " + W);
		System.out.println("flat rate: " + flatRate);

		double[] prices = { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7 };

		/**
		 * Test the pricing() function
		 */

		int rounds = 1000;
		double usedBudget = 0;
		for (int i = 0; i < rounds; i++) {
			double roundBudget = 0.0;
			for (int pic = 0; pic < L; pic++) {
				double price = strat.pricing(W, usedBudget, prices);
				usedBudget += price;
				roundBudget += price;
			}

			System.out.println("Round " + i + " uses " + roundBudget
					+ "; total used " + usedBudget);
		}
	}

	private double pricing(double W, double usedBudget, double[] prices) {
		// reward is set to zero when running out of budget
		if (usedBudget > W)
			return 0.0;
		Random r = new Random();
		int idx = r.nextInt(prices.length);
		return prices[idx];
	}
}
