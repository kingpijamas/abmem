package jamel.markets.labor;

import jamel.markets.Demanding;
import jamel.markets.Offering;
import jamel.markets.goods.Goods;
import jamel.spheres.monetary.BankAccount;
import jamel.spheres.productive.Factory;
import jamel.spheres.productive.Producing;
import jamel.utils.JamelRandom;
import jamel.utils.Recordable;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import economicCycle.Cycle;
import economicCycle.RegularUseCycleElement;

public class Employer extends RegularUseCycleElement implements
		Demanding<Labor> {

	private double ACCEPTABLE_VACANCIES_RATE = 0.3;// TODO: fruta number
													// courtesy
													// of JP

	private long totalWage;
	private Recordable<Double> vacancyRate;
	private int laborUnitsObtainedThisIteration;

	private BankAccount financing;
	private Producing<Goods> factory;

	private Wage offeredWage;
	private PriorityQueue<EmploymentContract> contracts;

	public Employer(Cycle circuit, Factory factory, BankAccount financing, Wage wage) {
		super(circuit);
		this.contracts = new PriorityQueue<EmploymentContract>();
		this.offeredWage = wage;
		this.financing = financing;
		this.factory = factory;
		this.vacancyRate = new Recordable<Double>(circuit, 1.0);// TODO:
																// Original
																// vacancy rate:
																// 100%
	}

	public void hire(Worker worker) {
		EmploymentContract contract = new EmploymentContract(getCycle(), this,
				worker, offeredWage.getValue());
		contracts.offer(contract);
		worker.notifyHiring(contract);
		totalWage += offeredWage.getValue();
	}

	public void fire(int quantity) {
		while (quantity > 0) {
			contracts.peek().expire();
			quantity--;
		}
	}

	private void endExpiredContracts() {
		if (contracts.isEmpty()) {
			return;
		}
		while (contracts.peek().isExpirationTime()) {
			contracts.poll().expire();
		}
	}

	public long getTotalWage() {
		return totalWage;
	}

	private void updateOfferedWage() {
		double currentVacancyRate = vacancyRate.getCurrentValue();
		if (currentVacancyRate == ACCEPTABLE_VACANCIES_RATE) {
			return;
		}
		float alpha1 = new JamelRandom().nextFloat();
		float alpha2 = new JamelRandom().nextFloat();
		if (currentVacancyRate < ACCEPTABLE_VACANCIES_RATE) {
			if (alpha1 * alpha2 < 1 - currentVacancyRate
					/ ACCEPTABLE_VACANCIES_RATE) {
				offeredWage.reduce(alpha1);
			}
		} else {
			if (alpha1 * alpha2 < currentVacancyRate
					/ ACCEPTABLE_VACANCIES_RATE - 1) {
				offeredWage.raise(alpha1);
			}
		}
	}

	public void endActivities() {
		fire(contracts.size());
	}

	public void getSupplied(Offering<Labor> offerer) {
		laborUnitsObtainedThisIteration++;
		Labor l = offerer.supply(financing, offerer.getOffer().getWage());
		factory.addLabor(l);
	}

	public int getLaborNeeds() {// TODO: check the rate at which workers are
								// being fired... seems too fast.
		int workforceRequirement = factory.getWorkforceRequirement();
		if (isUseable()) {
			endExpiredContracts();
			updateOfferedWage();
			int vacancies = workforceRequirement - contracts.size();
			if (vacancies < 0) {
				fire(Math.abs(vacancies));
			}
			vacancyRate.setValue(vacancies / (double) workforceRequirement);
			laborUnitsObtainedThisIteration = 0;
			use();
		}
		return workforceRequirement - laborUnitsObtainedThisIteration;
	}

	public List<Offering<Labor>> getWorkers() {
		List<Offering<Labor>> workers = new ArrayList<Offering<Labor>>(
				contracts.size());
		for (EmploymentContract contract : contracts) {
			workers.add(contract.getEmployee());
		}
		return workers;
	}

	public long getOfferedWage() {
		return offeredWage.getValue();
	}

	void removeContract(EmploymentContract employmentContract) {
		contracts.remove(employmentContract);
		totalWage -= employmentContract.getWage();
	}

}
