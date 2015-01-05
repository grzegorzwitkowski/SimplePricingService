package pricingservice;

import org.jbehave.core.annotations.Then;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceAssertionSteps {

    private PricingApi pricingApi;
    private PriceCalculationReference priceCalculationReference;

    public PriceAssertionSteps(PricingApi pricingApi, PriceCalculationReference priceCalculationReference) {
        this.pricingApi = pricingApi;
        this.priceCalculationReference = priceCalculationReference;
    }

    @Then("price should equal $expPrice")
    public void priceShouldEqual(BigDecimal expPrice) {
        PriceCalculation priceCalculation = pricingApi.getPriceCalculation(priceCalculationReference.getCalculationId());
        assertThat(priceCalculation.getPrice()).isEqualTo(expPrice);
    }
}
