package pricingservice;

import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

public class PriceListTreeTest extends AcceptanceTest {

    @Override
    public InjectableStepsFactory stepsFactory() {
        PriceCalculationReference priceCalculationReference = new PriceCalculationReference();
        PricingApi pricingApi = new PricingApi();
        return new InstanceStepsFactory(configuration(),
                new PriceListTreeSteps(pricingApi, priceCalculationReference),
                new PriceAssertionSteps(pricingApi, priceCalculationReference));
    }
}
