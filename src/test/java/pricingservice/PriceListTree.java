package pricingservice;

import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

public class PriceListTree extends JBehaveStory {

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new PriceListTreeSteps(), new PriceInRootCategorySteps());
    }
}
