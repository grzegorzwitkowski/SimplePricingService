package pricingservice.steps;

import com.google.common.collect.ImmutableSet;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import pricingservice.PriceCalculation;
import pricingservice.PriceCalculationReference;
import pricingservice.PriceList;
import pricingservice.PricingApi;
import pricingservice.PromoOption;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class PriceInRootCategorySteps {

  private static final int ROOT_CATEGORY = 0;

  private PricingApi pricingApi;
  private PriceCalculationReference priceCalculationRef;

  public PriceInRootCategorySteps(PricingApi pricingApi,
                                  PriceCalculationReference priceCalculationRef) {
    this.pricingApi = pricingApi;
    this.priceCalculationRef = priceCalculationRef;
  }

  @Given("price list for root category exists with: $fees")
  public void priceListForRootCategoryExistsWithFees(ExamplesTable fees) {
    PriceList priceList = toPriceList(fees);
    pricingApi.addPriceList(priceList, ROOT_CATEGORY);
  }

  @When("creating offer in root category with promo options $selectedPromoOptions")
  public void creatingOfferInRootCategoryWithPromoOptions(
          List<PromoOption> selectedPromoOptions) {
    PriceCalculation priceCalculation = pricingApi.calculatePrice(
            ImmutableSet.copyOf(selectedPromoOptions),
            ImmutableSet.of(ROOT_CATEGORY));
    this.priceCalculationRef
            .setCalculationId(priceCalculation.getCalculationId());
  }

  private PriceList toPriceList(ExamplesTable fees) {
    Map<PromoOption, BigDecimal> feesForPromoOptions = fees.getRows().stream()
            .collect(toMap(
                    row -> PromoOption.valueOf(row.get("promoOption")),
                    row -> new BigDecimal(row.get("fee"))
            ));
    return new PriceList(feesForPromoOptions);
  }
}
