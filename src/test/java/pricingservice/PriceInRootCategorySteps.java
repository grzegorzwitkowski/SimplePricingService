package pricingservice;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class PriceInRootCategorySteps {

    private PricingApi pricingApi = new PricingApi();
    private Set<PromoOption> selectedPromoOptions;
    private int selectedCategory;

    @Given("price list for root category exists with: $fees")
    public void priceListForRootCategoryExistsWithFees(ExamplesTable fees) {
        PriceList priceList = toPriceList(fees);
        pricingApi.addPriceList(priceList);
    }

    @When("creating offer in root category with promo options $fees")
    public void creatingOfferInRootCategoryWithPromoOptions(List<String> fees) {
        selectedPromoOptions = toPromoOptions(fees);
        selectedCategory = Categories.ROOT;
    }

    @Then("price should equal $expPrice")
    public void priceShouldEqual(BigDecimal expPrice) {
        BigDecimal price = pricingApi.calculatePrice(selectedPromoOptions, selectedCategory);
        assertThat(price).isEqualTo(expPrice);
    }

    private PriceList toPriceList(ExamplesTable fees) {
        Map<PromoOption, BigDecimal> data = new HashMap<>();
        for (Map<String, String> row : fees.getRows()) {
            PromoOption promoOption = PromoOption.valueOf(row.get("fee"));
            BigDecimal value = new BigDecimal(row.get("value"));
            data.put(promoOption, value);
        }
        return new PriceList(data, Categories.ROOT);
    }

    private Set<PromoOption> toPromoOptions(List<String> fees) {
        return fees.stream().map(PromoOption::valueOf).collect(toSet());
    }
}
