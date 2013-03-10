package economicCycle.scheduling.events;

import jamel.agents.firms.Firm;
import jamel.agents.firms.ProductiveSector;
import economicCycle.scheduling.SimulationEvent;

public class FirmComeback extends SimulationEvent {

	private ProductiveSector sector;
	private Firm firm;

	public FirmComeback(ProductiveSector sector, Firm firm) {
		this.sector = sector;
		this.firm = firm;
	}

	@Override
	public void execute() {// TODO: check!
		firm.enterMarkets();
		sector.addFirm(firm);
	}

}
