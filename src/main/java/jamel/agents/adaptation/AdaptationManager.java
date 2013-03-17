package jamel.agents.adaptation;


import java.util.ArrayList;
import java.util.List;

import utils.JamelRandom;

/**
 * Une classe pour le gestionnaire de l'évolution des agents.
 */
/**
 * @author pascal
 * 
 */
public class AdaptationManager<A extends AdaptiveObject> {
	private int valuableRate;
	private int frequency;
	private final int tournamentSize;
	private final double audacity;

	@Override
	public String toString() {
		String ans = "Valuable Rate: " + valuableRate;
		ans += "\nFrequency: " + frequency;
		ans += "\nTournament Size: " + tournamentSize;
		ans += "\nAudacity: " + audacity;
		return ans;
	}

	public AdaptationManager(double audacity, int valuableRate, int frequency,
			int tournamentSize) {
		if (frequency <= 0) {
			throw new IllegalArgumentException("Invalid frequency, must be >0");
		}
		this.valuableRate = valuableRate;
		this.frequency = frequency;
		this.tournamentSize = tournamentSize;
		this.audacity = audacity;
	}

	/**
	 * Adapte certains agents au sein de la population passée en paramètre.
	 * <p>
	 * Récupère la liste des agents adaptables. Sélectionne les participants au
	 * tournoi. Récupère les résultats du tournoi. Adapte l'agent perdant.
	 * 
	 * @param population
	 *            la liste des agents adaptatifs
	 */
	public List<A> adaptLosers(List<A> population) {
		List<A> matureAdaptablesSelection = getMatureSelection(population);
		if (!population.isEmpty()) {
			valuableRate = (int) ((100. * matureAdaptablesSelection.size()) / population
					.size());
		}
		List<A> tournamentSelection;
		List<A> adaptedLosers = new ArrayList<A>(frequency);
		for (int i = 0; i < frequency; i++) {
			tournamentSelection = getRandomSelection(tournamentSize,
					matureAdaptablesSelection);
			if (tournamentSelection == null) {
				return adaptedLosers;
			}
			Tournament<A> tournament = new Tournament<A>(tournamentSelection,
					audacity);
			tournament.adaptLoser();
			adaptedLosers.add(tournament.getLoser());
			matureAdaptablesSelection.remove(tournament.getParentA());
			matureAdaptablesSelection.remove(tournament.getParentB());
			matureAdaptablesSelection.remove(tournament.getLoser());
		}
		return adaptedLosers;
	}

	/**
	 * Adapte un agent donné à partir de la population passée en paramètre.
	 * <p>
	 * Cette méthode est normalement utilisée avec un agent qui vient d'être
	 * créé, et qui n'a pas d'expérience. Plusieurs différences importantes avec
	 * la méthode {@link #adapte(List)}:<br>
	 * - l'agent à adapter est désigné, son fitness n'est pas évalué,<br>
	 * - la position de l'agent à adapter n'est pas prise en compte pour le
	 * calcul de la nouvelle position. On est donc plus près de l'algorithme
	 * génétique classique.<br>
	 * 
	 * @param newcomer
	 *            l'agent à adapter
	 * @param population
	 *            la liste des agents adaptatifs
	 */
	public void adaptNewcomer(List<A> population, A newcomer) {
		List<A> tournamentSelection = getRandomSelection(tournamentSize,
				getMatureSelection(population));
		if (tournamentSelection == null) {
			return;
		}
		new Tournament<A>(tournamentSelection, audacity)
				.adaptNewcomer(newcomer);
	}

	private List<A> getMatureSelection(List<A> population) {
		List<A> matureSelection = new ArrayList<A>();
		for (A a : population) {
			if (a.isMature()) {
				matureSelection.add(a);
			}
		}
		return matureSelection;
	}

	/**
	 * Renvoie une liste d'agents adaptatifs sélectionnés au hasard dans la
	 * liste passée en paramètre.
	 * 
	 * @param i
	 *            le nombre d'agents à sélectionner.
	 * @param population
	 *            la liste des agents d'origine.
	 * @return la liste des agents sélectionnés (<code>null</code> si la liste
	 *         n'a pu être constituée)
	 */
	private List<A> getRandomSelection(int quantity, List<A> population) {
		if (population.size() < quantity) {
			return null;
		}
		List<A> ans = new ArrayList<A>(quantity);
		while (ans.size() < quantity) {
			A aux = population.get(Math.abs(new JamelRandom().nextInt()) % quantity);
			if (!ans.contains(aux)) {
				ans.add(aux);
			}
		}
		return ans;
	}

	public int getMeasurableRate() {
		return valuableRate;
	}
}