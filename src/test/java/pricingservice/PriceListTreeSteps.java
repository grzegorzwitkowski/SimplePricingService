package pricingservice;

import com.google.common.collect.ImmutableSet;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class PriceListTreeSteps {

    private PricingApi pricingApi = new PricingApi();
    private Set<PromoOption> selectedPromoOptions;
    private Set<Integer> selectedCategory;

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
        selectedPromoOptions = toPromoOptions(promoOptions);
        selectedCategory = ImmutableSet.copyOf(categoryPath);
    }

    @Then("price should equal $expPrice")
    public void priceShouldEqual(BigDecimal expPrice) {
        BigDecimal price = pricingApi.calculatePrice(selectedPromoOptions, selectedCategory);
        assertThat(price).isEqualTo(expPrice);
    }

    private Set<PromoOption> toPromoOptions(List<String> fees) {
        return fees.stream().map(PromoOption::valueOf).collect(toSet());
    }
}
