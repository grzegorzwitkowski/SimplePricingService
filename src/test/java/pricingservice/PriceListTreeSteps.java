package pricingservice;

import com.google.common.collect.ImmutableSet;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class PriceListTreeSteps {

    private PricingApi pricingApi;
    private PriceCalculationReference priceCalculationReference;

    public PriceListTreeSteps(PricingApi pricingApi, PriceCalculationReference priceCalculationReference) {
        this.priceCalculationReference = priceCalculationReference;
        this.pricingApi = pricingApi;
    }

    @Given("price list configuration exists: $priceListTree")
    public void priceListConfigurationExists(String priceListTree) throws Exception {
        Map<Integer, PriceList> priceListsInCategories = new PriceListParser().parse(priceListTree);
        priceListsInCategories.entrySet().stream().forEach(entry -> {
            int category = entry.getKey();
            PriceList priceListForCategory = entry.getValue();
            pricingApi.addPriceList(priceListForCategory, category);
        });
    }

    @When("creating offer in category $categoryPath with promo options $promoOptions")
    public void creatingOfferInCategoryWithPromoOptions(List<Integer> categoryPath, List<String> promoOptions) {
        PriceCalculation priceCalculation = pricingApi.calculatePrice(toPromoOptions(promoOptions), ImmutableSet.copyOf(categoryPath));
        this.priceCalculationReference.setCalculationId(priceCalculation.getCalculationId());
    }

    private Set<PromoOption> toPromoOptions(List<String> fees) {
        return fees.stream().map(PromoOption::valueOf).collect(toSet());
    }
}
