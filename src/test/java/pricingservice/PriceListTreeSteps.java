package pricingservice;

import com.google.common.collect.ImmutableSet;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class PriceListTreeSteps {

    private PricingApi pricingApi = new PricingApi();
    private UUID calculationId;

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
        this.calculationId = priceCalculation.getCalculationId();
    }

    @Then("price2 should equal $expPrice")
    public void priceShouldEqual(BigDecimal expPrice) {
        PriceCalculation priceCalculation = pricingApi.getPriceCalculation(calculationId);
        assertThat(priceCalculation.getPrice()).isEqualTo(expPrice);
    }

    private Set<PromoOption> toPromoOptions(List<String> fees) {
        return fees.stream().map(PromoOption::valueOf).collect(toSet());
    }
}
