package pricingservice.stories;

import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import pricingservice.AcceptanceTest;
import pricingservice.PriceCalculationReference;
import pricingservice.PricingApiClient;
import pricingservice.steps.PriceAssertionSteps;
import pricingservice.steps.PriceInRootCategorySteps;

public class PriceInRootCategoryTest extends AcceptanceTest {

  @Override
  public InjectableStepsFactory stepsFactory() {
    PricingApiClient pricingApiClient = new PricingApiClient();
    PriceCalculationReference priceCalculationReference = new PriceCalculationReference();
    return new InstanceStepsFactory(configuration(),
            new PriceInRootCategorySteps(pricingApiClient.getPricingApi(),
                    priceCalculationReference),
            new PriceAssertionSteps(pricingApiClient.getPricingApi(),
                    priceCalculationReference));
  }
}
