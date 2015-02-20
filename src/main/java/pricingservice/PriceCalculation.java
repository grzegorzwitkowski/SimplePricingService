package pricingservice;

import java.math.BigDecimal;
import java.util.UUID;

public final class PriceCalculation {

	private final UUID calculationId;
	private final BigDecimal price;

	public PriceCalculation(UUID calculationId, BigDecimal price) {
		this.calculationId = calculationId;
		this.price = price;
	}

	public UUID getCalculationId() {
		return calculationId;
	}

	public BigDecimal getPrice() {
		return price;
	}
}
