package jamel.markets.labor;

import jamel.markets.Market;
import jamel.markets.Offering;

import java.util.List;

import economicCycle.Cycle;

public class LaborMarket extends Market<Labor, Employer> {

	public LaborMarket(Cycle circuit) {
		super(circuit);
	}

	@Override
	protected void supply(Employer e) {
		List<Offering<Labor>> workers = e.getWorkers();
		for (Offering<Labor> w : workers) {
			updateVolume(w.getOffer().getVolume());
			updateValue(w.getOffer().getValue());
			e.getSupplied(w);
		}
		if (e.getLaborNeeds() == 0) {
			return;
		}
		Worker jobSeeker;
		do {
			jobSeeker = getUnemployedJobSeeker(e.getOfferedWage());
			if (jobSeeker != null) {
				e.hire(jobSeeker);
				updateVolume(jobSeeker.getOffer().getVolume());
				updateValue(jobSeeker.getOffer().getValue());
				e.getSupplied(jobSeeker);
			}
		} while (e.getLaborNeeds() > 0 && jobSeeker != null);
	}

	private Worker getUnemployedJobSeeker(long offeredWage) {
		for (Offering<Labor> o : getOfferers()) {
			Worker w = (Worker) o;
			if (w.isUnemployed() && w.isUseable()) {
				return w;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		double unemp = 0;
		for (Offering<Labor> o : getOfferers()) {
			Worker w = (Worker) o;
			if (w.isUnemployed()) {
				unemp++;
			}
		}
		return super.toString() + "\nUnemployment rate: " + unemp * 100
				/ getOfferers().size();
	}
}