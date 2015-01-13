package pricingservice.stories;

import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import pricingservice.AcceptanceTest;
import pricingservice.PriceCalculationReference;
import pricingservice.PriceListRepository;
import pricingservice.PricingApi;
import pricingservice.steps.PriceAssertionSteps;
import pricingservice.steps.PriceInRootCategorySteps;

public class PriceInRootCategoryTest extends AcceptanceTest {

    @Override
    public InjectableStepsFactory stepsFactory() {
        PricingApi pricingApi = new PricingApi(new PriceListRepository());
        PriceCalculationReference priceCalculationReference = new PriceCalculationReference();
        return new InstanceStepsFactory(configuration(),
                new PriceInRootCategorySteps(pricingApi, priceCalculationReference),
                new PriceAssertionSteps(pricingApi, priceCalculationReference));
    }
}
