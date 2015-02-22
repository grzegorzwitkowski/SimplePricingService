package pricingservice.stories;

import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import pricingservice.AcceptanceTest;
import pricingservice.PriceCalculationReference;
import pricingservice.PricingApi;
import pricingservice.PricingApiClient;
import pricingservice.steps.PriceAssertionSteps;
import pricingservice.steps.PriceInRootCategorySteps;

public class PriceInRootCategoryTest extends AcceptanceTest {

  @Override
  public InjectableStepsFactory stepsFactory() {
    PricingApiClient pricingApiClient = new PricingApiClient();
    PriceCalculationReference priceCalculationRef = new PriceCalculationReference();
    PricingApi pricingApi = pricingApiClient.getPricingApi();
    return new InstanceStepsFactory(configuration(),
            new PriceInRootCategorySteps(pricingApi, priceCalculationRef),
            new PriceAssertionSteps(pricingApi, priceCalculationRef));
  }
}
