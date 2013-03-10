package economicCycle.scheduling;

public abstract class SimulationEvent {
	public abstract void execute();

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
