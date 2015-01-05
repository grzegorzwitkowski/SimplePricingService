package pricingservice;

import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

public class PriceInRootCategoryTest extends AcceptanceTest {

    @Override
    public InjectableStepsFactory stepsFactory() {
        PricingApi pricingApi = new PricingApi();
        PriceCalculationReference priceCalculationReference = new PriceCalculationReference();
        return new InstanceStepsFactory(configuration(),
                new PriceInRootCategorySteps(pricingApi, priceCalculationReference),
                new PriceAssertionSteps(pricingApi, priceCalculationReference));
    }
}
