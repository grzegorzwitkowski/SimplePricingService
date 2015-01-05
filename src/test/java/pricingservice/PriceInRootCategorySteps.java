package pricingservice;

import com.google.common.collect.ImmutableSet;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class PriceInRootCategorySteps {

    private static final int ROOT_CATEGORY = 0;

    private PricingApi pricingApi;
    private PriceCalculationReference priceCalculationReference;

    public PriceInRootCategorySteps(PricingApi pricingApi, PriceCalculationReference priceCalculationReference) {
        this.pricingApi = pricingApi;
        this.priceCalculationReference = priceCalculationReference;
    }

    @Given("price list for root category exists with: $fees")
    public void priceListForRootCategoryExistsWithFees(ExamplesTable fees) {
        PriceList priceList = toPriceList(fees);
        pricingApi.addPriceList(priceList, ROOT_CATEGORY);
    }

    @When("creating offer in root category with promo options $selectedPromoOptions")
    public void creatingOfferInRootCategoryWithPromoOptions(List<String> selectedPromoOptions) {
        PriceCalculation priceCalculation = pricingApi.calculatePrice(toPromoOptions(selectedPromoOptions), ImmutableSet.of(ROOT_CATEGORY));
        this.priceCalculationReference.setCalculationId(priceCalculation.getCalculationId());
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
