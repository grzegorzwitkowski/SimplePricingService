package pricingservice;

import com.google.common.collect.ImmutableSet;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

@UsingSteps(instances = {PriceInRootCategoryTest.Steps.class})
public class PriceInRootCategoryTest extends AcceptanceTest {

    public static class Steps {

        private static final int ROOT_CATEGORY = 0;

        private PricingApi pricingApi = new PricingApi();
        private Set<PromoOption> selectedPromoOptions;

        @Given("price list for root category exists with: $fees")
        public void priceListForRootCategoryExistsWithFees(ExamplesTable fees) {
            PriceList priceList = toPriceList(fees);
            pricingApi.addPriceList(priceList, ROOT_CATEGORY);
        }

        @When("creating offer in root category with promo options $fees")
        public void creatingOfferInRootCategoryWithPromoOptions(List<String> fees) {
            selectedPromoOptions = toPromoOptions(fees);
        }

        @Then("price should equal $expPrice")
        public void priceShouldEqual(BigDecimal expPrice) {
            BigDecimal price = pricingApi.calculatePrice(selectedPromoOptions, ImmutableSet.of(ROOT_CATEGORY));
            assertThat(price).isEqualTo(expPrice);
        }

        private PriceList toPriceList(ExamplesTable fees) {
            Map<PromoOption, BigDecimal> feesForPromoOptions = fees.getRows().stream()
                    .collect(toMap(row -> PromoOption.valueOf(row.get("promoOption")), row -> new BigDecimal(row.get("fee"))));
            return new PriceList(feesForPromoOptions);
        }

        private Set<PromoOption> toPromoOptions(List<String> fees) {
            return fees.stream().map(PromoOption::valueOf).collect(toSet());
        }
    }
}
