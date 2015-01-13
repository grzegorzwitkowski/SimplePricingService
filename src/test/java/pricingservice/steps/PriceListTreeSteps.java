package pricingservice.steps;

import com.google.common.collect.ImmutableSet;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import pricingservice.PriceCalculation;
import pricingservice.PriceCalculationReference;
import pricingservice.PriceList;
import pricingservice.PriceListParser;
import pricingservice.PriceListRepository;
import pricingservice.PricingApi;
import pricingservice.PromoOption;

import java.util.List;
import java.util.Map;

public class PriceListTreeSteps {

    private PricingApi pricingApi;
    private PriceListRepository priceListRepository;
    private PriceCalculationReference priceCalculationReference;

    public PriceListTreeSteps(PricingApi pricingApi, PriceListRepository priceListRepository, PriceCalculationReference priceCalculationReference) {
        this.priceListRepository = priceListRepository;
        this.priceCalculationReference = priceCalculationReference;
        this.pricingApi = pricingApi;
    }

    @Given("no price lists are defined")
    public void noPriceListsAreDefined() {
        priceListRepository.clear();
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
