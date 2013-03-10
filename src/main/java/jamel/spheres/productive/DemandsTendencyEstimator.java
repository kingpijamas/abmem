package jamel.spheres.productive;

import jamel.utils.Tendency;

public interface DemandsTendencyEstimator {
	public Tendency getDemandsTendency(int currentStock, float alpha1,
			float alpha2);
}
