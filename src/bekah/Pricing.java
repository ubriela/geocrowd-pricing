package bekah;
public class Pricing {
	public final static double minEntropy = Math.random() * 10 + 1;

	public static void updatePricing(double[] locations, double[] prices,
			double budget) {
		if (shannonEntropy(locations) < minEntropy) {
			double max = locations[0];
			double min = locations[0];
			for (int i = 1; i < locations.length; i++) {
				if (locations[i] > max) {
					max = locations[i];
				}
				if (locations[i] < min) {
					min = locations[i];
				}
			}
			int locsAboveMedian = 0, locsBelowQuarter = 0, locsBelowMedian = 0;
			for (int j = 0; j < locations.length; j++) {
				if (locations[j] > (max - min) / 2) {
					locsAboveMedian++;
				} else if (locations[j] < (max - min) / 4) {
					locsBelowQuarter++;
				} else if (locations[j] < (max - min) / 2) {
					locsBelowMedian++;
				}
			}

			for (int i = 0; i < locations.length; i++) {
				if (locations[i] > (max - min) / 2) {
					prices[i] = (budget / 6) / locsAboveMedian;
				} else if (locations[i] < (max - min) / 4) {
					prices[i] = (budget / 2) / locsBelowQuarter;
				} else if (locations[i] < (max - min) / 2) {
					prices[i] = (budget / 3) / locsBelowQuarter;
				}
			}
		} else if (shannonEntropy(locations) >= minEntropy) {
			for (int i = 0; i < locations.length; i++) {
				prices[i] = budget / locations.length; // changes pricing so
														// pricing is equal
			}
		}
	}
}