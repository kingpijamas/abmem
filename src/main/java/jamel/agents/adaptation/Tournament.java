package jamel.agents.adaptation;


import java.util.List;

import utils.JamelRandom;

class Tournament<A extends AdaptiveObject> {
	private static final int SELECTION_SIZE = 3;
	private final double audacity;
	private A parentA;
	private A parentB;
	private A loser;

	public Tournament(List<A> contestants, double baseAudacity) {
		if (contestants.size() < SELECTION_SIZE) {
			throw new IllegalArgumentException("Cannot get " + SELECTION_SIZE
					+ " candidates to generate adaptations");
		}
		this.audacity = baseAudacity * new JamelRandom().nextExp();
		for (A contestant : contestants) {
			parentB = contestant;
			if (parentA == null
					|| contestant.getFitness(contestants.size()) > parentA
							.getFitness(contestants.size())) {
				parentB = parentA;
				parentA = contestant;
			} else if (loser == null
					|| contestant.getFitness(contestants.size()) < loser
							.getFitness(contestants.size())) {
				loser = contestant;
			}
		}
	}

	/**
	 * Adapte l'agent passé en paramètre à partir des caractéristiques des
	 * gagnants du tournoi
	 * <p>
	 * Le passé de l'agent (sa position courante, son fitness) ne sont pas
	 * considérés. Cette méthode est conçu pour doter un agent nouveau (donc
	 * sans expérience) de caractéristiques héritées de la population courante.
	 * 
	 * @param newcomer
	 *            l'agent à adapter.
	 * @param tournament
	 *            les résultats du tournoi.
	 */
	public void adaptNewcomer(A newcomer) {
		A parentA = getParentA();
		A parentB = getParentB();
		parentA.adaptNewcomer(parentB, newcomer);
	}

	public A getParentA() {
		return parentA;
	}

	public A getParentB() {
		return parentB;
	}

	public A getLoser() {
		return loser;
	}

	/**
	 * Adapte l'agent perdant du tournoi à partir des caractères des gagnants du
	 * tournoi.
	 * <p>
	 * Récupère les caractères des gagnants et du perdant du tournoi. Passe en
	 * revue chacun de ces caractères. Pour chacun, crée un nouveau caractère
	 * calculé à partir des caractères gagnants et perdant. Affecte au perdant
	 * les caractères nouvellement calculés.
	 * 
	 * @param tournament
	 *            les résultats du tournoi.
	 */
	public void adaptLoser() {
		parentA.changeLoser(parentB, loser,
				audacity * new JamelRandom().nextExp(),
				new JamelRandom().nextDouble());
	}
}
