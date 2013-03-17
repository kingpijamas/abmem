package jamel.spheres.productive;

import utils.Tendency;

public interface DemandsTendencyEstimator {
	public Tendency getDemandsTendency(int currentStock, float alpha1,
			float alpha2);
}
