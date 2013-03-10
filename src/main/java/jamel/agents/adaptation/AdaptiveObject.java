package jamel.agents.adaptation;

public interface AdaptiveObject {
	public void adaptNewcomer(AdaptiveObject parentB, AdaptiveObject newcomer);

	public void changeLoser(AdaptiveObject parentB, AdaptiveObject loser,
			double audacity, double prefA);

	public boolean isMature();
	
	public double getFitness(int poolSize);
}
