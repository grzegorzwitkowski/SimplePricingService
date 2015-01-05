package pricingservice;

import java.math.BigDecimal;
import java.util.UUID;

public class PriceCalculation {

    private UUID calculationId;
    private BigDecimal price;

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
