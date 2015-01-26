package pricingservice;

import java.util.UUID;

public class PriceCalculationReference {

    private UUID calculationId;

    public UUID getCalculationId() {
        return calculationId;
    }

    public void setCalculationId(UUID calculationId) {
        this.calculationId = calculationId;
    }
}
