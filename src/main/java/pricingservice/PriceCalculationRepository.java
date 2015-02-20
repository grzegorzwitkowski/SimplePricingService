package pricingservice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PriceCalculationRepository {

	private final Map<UUID, PriceCalculation> priceCalculations = new HashMap<>();

	public void add(PriceCalculation priceCalculation, UUID calculationId) {
		priceCalculations.put(calculationId, priceCalculation);
	}

	public PriceCalculation get(UUID calculationId) {
		return priceCalculations.get(calculationId);
	}
}
