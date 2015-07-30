import java.util.Random;

import org.apache.commons.math3.distribution.ZipfDistribution;

/**
 * The second strategy is to balance the rewards allocated to tasks based on the
 * number of collected pictures per task. For example, if there were many
 * pictures collected for a particular task, we would reduce its reward while
 * increasing rewards for the other tasks with fewer pictures. Toward that end,
 * we want to propose a reward function that decays with the number of collected
 * pictures. Our hypothesis is that this reward function would result in maximum
 * coverage of the tasks.
 *
 */
public class PricingStrategy1FixedPrice {

	static int[] priceSet = { 10, 20, 30, 40, 50, 60, 70, 80 };
	static double W = 20 * 300 * 30; // total budget
	static int k = 300; // max number of pics per location that we are willing
						// to
	// pay for.
	static int L = 20; // number of locations

	// factor f: even when the number of pics per location exceeds k, we
	// still want to pay
	// users a small amount to keep them engaged with the app. However, if
	// the number of pics
	// exceeds f*k, then we want to stop paying completely.
	static double f = 3.0;

	// Flat rate per pic, aka delta in the word file.
	static double flatRate = ((double) W) / (k * L);

	public static void main(String[] args) throws Exception {
		PricingStrategy1FixedPrice strat = new PricingStrategy1FixedPrice();

		System.out.println("Budget: " + W);
		System.out.println("Flat rate: " + flatRate);

//		strat.testPricing();

		/**
		 * Run multiple rounds and change f from 1.0 to 10
		 */
		for (int round = 0; round <= 100; round++) {
			double overBudget = 0;
			int rounds = 10;
			for (int i = 0; i < rounds; i++) {
				double spending = strat.estimateSpending(W, L, k, f);
				// System.out.println(spending); //printing out the spending
				overBudget += spending - W;
			}
			System.out.println("f: " + f + ", Percentage of over-budget: \t"
					+ Math.round(100 * overBudget / (rounds * W)) + "\t%");
			f = f + 0.1;
		}
	}

	/**
	 * Estimate the total of spending for an event by simulating the action of a
	 * user taking a pic at a location as a random var.
	 * 
	 * @param W
	 *            : budget
	 * @param L
	 *            : number of locations
	 * @param k
	 *            : the max number of well-paid pics for each location
	 * @param f
	 *            : the factor - after (f*k) pics has been collected in a
	 *            location, any more pics will not get paid for the location.
	 * @param priceSet
	 *            : the range of prices we are willing to pay
	 * @return the total spending.
	 */
	public double estimateSpending(double W, int L, int k, double f)
			throws Exception {
		Random r = new Random();
		int limit = (int) (((double) k) * ((double) L) * f);// max number of
															// paid pics in all
															// locations
		int count = 0;
		double spending = 0;
		int[] picDistribution = new int[L];
		while (count < limit) {
			int loc = r.nextInt(L);
			picDistribution[loc] += 1;
			spending += pricing(loc, picDistribution, W, k, f);

			count++;
		}
//		 printArray(picDistribution);
		// System.out.println(spending);
		return spending;
	}

	/**
	 * Similar to estimateSpending. However, instead of simple random function,
	 * we use zipf.
	 * 
	 * @param W
	 * @param L
	 * @param k
	 * @param f
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public double estimateSpending_skew(double W, int L, int k, double f) throws Exception {
		ZipfDistribution r = new ZipfDistribution(100, 1);
		int limit = (int) (((double) k) * ((double) L) * f);// max number of
															// paid pics in all
															// locations
		int count = 0;
		double spending = 0;
		int[] picDistribution = new int[L];
		while (count < limit) {
			int loc = r.sample();
			loc = Math.min(L - 1, loc);
			picDistribution[loc] += 1;
			spending += pricing(loc, picDistribution, W, k, f);

			count++;
		}
		// printArray(picDistribution);
		// System.out.println(spending);
		return spending;
	}

	/**
	 * Get the price of the next pic for a give location, given the current
	 * distribution of pics in all locations.
	 * 
	 * @param location
	 * @param picDistribution
	 *            - the current numbers of pics in the locations
	 * @param W
	 *            - budget
	 * @param k
	 *            - the maximum number of *wanted* pictures in each location,
	 *            which is needed to avoid over-spending
	 * @param f
	 *            - after f*k pics have been collected at a location, no more
	 *            payment for pics in that location
	 * @param priceSet
	 *            - the range of prices we are willing to pay
	 * @return the price for an additional pic at location "loc".
	 */
	private int pricing(int loc, int[] picDistribution, double W, int k,
			double f) throws Exception {
		int len = picDistribution.length;
		int curr = picDistribution[loc];
		int flatRate = (int) Math.round(W / (k * len));
		int total = getTotalPics(picDistribution);

		// corner case
		if (total <= k) // flat price for the first k pictures
			return flatRate;
		if (curr > k) {
			if (curr > k * f) // when obtain enough number of picture at current
								// location, set price to 0
				return 0;
			else
				return priceSet[0]; // otherwise, set to minimum payment
		}

		double[] probabilities1 = new double[len];
		double[] probabilities2 = new double[len];

		for (int i = 0; i < len; i++) {
			probabilities1[i] = ((double) picDistribution[i]) / total;
		}

		for (int i = 0; i < len; i++) {
			if (i == loc) {
				probabilities2[i] = ((double) (picDistribution[i] + 1))
						/ (total + 1);
			} else
				probabilities2[i] = ((double) picDistribution[i]) / (total + 1);
		}

		double H1 = 0, H2 = 0, D1 = 0, D2 = 0; // entropies and diversities
												// before and after adding a
												// pic.
		for (int i = 0; i < len; i++) {
			if (probabilities1[i] != 0)
				H1 += probabilities1[i] * Math.log(probabilities1[i]);
			if (probabilities2[i] != 0)
				H2 += probabilities2[i] * Math.log(probabilities2[i]);
		}
		H1 = (-1) * H1;
		H2 = (-1) * H2;
		D1 = Math.exp(H1);
		D2 = Math.exp(H2);

		double diff = D2 - D1;
		
//		System.out.println("diff=" + diff);

		if (diff < 0)	//	min price
			return priceSet[0];
		if (diff > 0.1)	// max price
			return priceSet[priceSet.length - 1];
		else {
			if (diff > 0.005)
				return setPrice((int) Math.round(200 * diff * priceSet[0]),
						priceSet[0], priceSet[priceSet.length - 1]);
			else
				// make sure the user gets at least flat rate
				return setPrice(
						flatRate + (int) Math.round(200 * diff * priceSet[0]),
						priceSet[0], priceSet[priceSet.length - 1]);
		}
	}

	/**
	 * Make sure the price is within the range
	 * @param price
	 * @param minPrice
	 * @param maxPrice
	 * @return
	 */
	private int setPrice(int price, int minPrice, int maxPrice) {
		// TODO Auto-generated method stub
		if (price < minPrice)
			return minPrice;
		if (price > maxPrice)
			return maxPrice;

		int idx = binarySearchFloor(priceSet, price);
//		System.out.println(priceSet[idx]);
		return priceSet[idx];
	}

	/**
	 * find the lower bound index of a value in a sorted list
	 * 
	 * @param search
	 * @param find
	 * @return
	 */
	public static final int binarySearchFloor(int[] search, int find) {
		int start, end, midPt;
		start = 0;
		end = search.length - 1;
		while (start <= end) {
			midPt = (start + end) / 2;
			if (search[midPt] == find) {
				while (midPt - 1 >= 0 && search[midPt - 1] == find)
					midPt--;
				return midPt;
			} else if (search[midPt] < find) {
				start = midPt + 1;
			} else {
				end = midPt - 1;
			}
		}

		return end;
	}
	
	/**
	 * find upper index of rec query
	 * 
	 * @param search
	 * @param find
	 * @return
	 */
	public static final int binarySearchCeil(int[] search, int find) {
		int start, end, midPt;
		start = 0;
		end = search.length - 1;
		while (start <= end) {
			midPt = (start + end) / 2;
			if (search[midPt] == find) {
				while (midPt + 1 <= search.length - 1
						&& search[midPt + 1] == find)
					midPt++;
				return midPt;
			} else if (search[midPt] < find) {
				start = midPt + 1;
			} else {
				end = midPt - 1;
			}
		}

		return start;
	}

	private void printArray(int[] pics) {
		for (int x : pics) {
			System.out.print(x + "  ");
		}
		System.out.println();
	}

	private int getTotalPics(int[] picDistribution) {
		int count = 0;
		for (int num : picDistribution) {
			count += num;
		}
		return count;
	}

	/**
	 * Test the pricing() function
	 * 
	 * @throws Exception
	 */
	public static void testPricing() throws Exception {
		PricingStrategy1FixedPrice strat = new PricingStrategy1FixedPrice();

		int[] pics = { 13, 10, 19, 17, 110, 14, 1, 2, 10, 3, 15, 12, 8, 88, 12,
				25, 1, 32, 12, 10 };
		int flatRate = (int) Math.round(W / (k * pics.length));

		System.out.println("flat rate: " + flatRate);

		for (int i = 0; i < pics.length; i++) {
			double price = strat.pricing(i, pics, W, k, f);
			System.out.println(price);
		}
	}
}
