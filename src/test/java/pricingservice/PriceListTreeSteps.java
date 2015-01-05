package pricingservice;

import com.google.common.collect.ImmutableSet;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;

import java.util.List;
import java.util.Map;

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
    public void creatingOfferInCategoryWithPromoOptions(List<Integer> categoryPath, List<PromoOption> promoOptions) {
        PriceCalculation priceCalculation = pricingApi.calculatePrice(ImmutableSet.copyOf(promoOptions), ImmutableSet.copyOf(categoryPath));
        this.priceCalculationReference.setCalculationId(priceCalculation.getCalculationId());
    }
}
