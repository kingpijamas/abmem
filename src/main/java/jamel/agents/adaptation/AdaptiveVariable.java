package jamel.agents.adaptation;

public class AdaptiveVariable {
	private double value;

	public AdaptiveVariable(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	/**
	 * Calcule la nouvelle valeur du paramètre.
	 * <p>
	 * Equivalent de l'opérateur de crossover dans un algorithme génétique
	 * classique. On commence par calculer la moyenne pondérée des paramètres
	 * imités.
	 * 
	 * @param vA
	 *            la valeur du paramètre imité A
	 * @param vB
	 *            la valeur du paramètre imité B
	 * @param vL
	 *            la valeur du paramètre à modifier
	 * @param audacity
	 *            l'audace
	 * @param prefA
	 *            la préférence pour le paramètre A
	 * @return la valeur calculée.
	 */
	public void adapt(AdaptiveVariable vA, AdaptiveVariable vB,
			double audacity, double prefA) {
		double prefB = 1 - prefA;
		double vG = vA.getValue() * prefA + vB.getValue() * prefB;
		double vN = vG + audacity * (vG - this.value);
		this.value = vN;
	}

	public void adapt(AdaptiveVariable valueA, AdaptiveVariable valueB,
			double prefA) {
		double prefB = 1 - prefA;
		this.value = valueA.getValue() * prefA + valueB.getValue() * prefB;
	}
}
