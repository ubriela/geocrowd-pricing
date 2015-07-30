package bekah;
public class DiversityMetric {
	public static double shannonEntropy(double[] arr) {
		int sum = 0;
		double[] ln = new double[arr.length];
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i]; // sum of array values
		}
		for (int i = 0; i < ln.length; i++) {
			if (arr[i] != 0) {
				double pi = arr[i] / sum; // sample/sum
				ln[i] = Math.log(pi) * pi;
			}

		}
		double shannon_sum = 0;
		for (int i = 0; i < ln.length; i++) {
			shannon_sum += ln[i];
		}
		return -1 * shannon_sum;
		/*
		 * I used this as a reference for the entropy calculation
		 * https://www.easycalculation
		 * .com/statistics/learn-shannon-wiener-diversity.php
		 */
	}

	public static void main(String[] args) {

	}
}
