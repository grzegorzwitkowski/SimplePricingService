package pricingservice.stories;

import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import pricingservice.AcceptanceTest;
import pricingservice.PriceListRepository;
import pricingservice.steps.PriceAssertionSteps;
import pricingservice.PriceCalculationReference;
import pricingservice.steps.PriceListTreeSteps;
import pricingservice.PricingApi;

public class PriceListTreeTest extends AcceptanceTest {

    @Override
    public InjectableStepsFactory stepsFactory() {
        PriceCalculationReference priceCalculationReference = new PriceCalculationReference();
        PriceListRepository priceListRepository = new PriceListRepository();
        PricingApi pricingApi = new PricingApi(priceListRepository);
        return new InstanceStepsFactory(configuration(),
                new PriceListTreeSteps(pricingApi, priceListRepository, priceCalculationReference),
                new PriceAssertionSteps(pricingApi, priceCalculationReference));
    }
}
