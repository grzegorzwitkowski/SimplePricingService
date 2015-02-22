package pricingservice;

import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class FeeCalculator {

  private final PriceListRepository priceListRepository;

  public FeeCalculator(PriceListRepository priceListRepository) {
    this.priceListRepository = priceListRepository;
  }

  public BigDecimal calculateTotalPrice(Set<PromoOption> selectedPromoOptions,
                                        Set<Integer> categoryPath) {
    Map<PromoOption, BigDecimal> calculatedFees = calculateFees(
            selectedPromoOptions, categoryPath);
    return totalFees(calculatedFees);
  }

  private Map<PromoOption, BigDecimal> calculateFees(
          Set<PromoOption> selectedPromoOptions, Set<Integer> categoryPath) {
    ListIterator<Integer> it = ImmutableList.copyOf(categoryPath).listIterator(
            categoryPath.size());
    Map<PromoOption, BigDecimal> feesForSelectedPromoOptions = new HashMap<>();
    while (it.hasPrevious()) {
      int category = it.previous();
      PriceList priceListForCategory = priceListRepository.get(category);
      if (priceListForCategory != null) {
        addFeesForPromoOptionsDefinedInPriceList(selectedPromoOptions,
                priceListForCategory, feesForSelectedPromoOptions);
      }
    }
    return feesForSelectedPromoOptions;
  }

  private void addFeesForPromoOptionsDefinedInPriceList(
          Set<PromoOption> selectedPromoOptions,
          PriceList priceListForCategory,
          Map<PromoOption, BigDecimal> feesForSelectedPromoOptions) {
    selectedPromoOptions.forEach(selectedPromoOption -> {
      BigDecimal promoOptionFee = priceListForCategory.getFeesForPromoOptions()
              .get(selectedPromoOption);
      if (promoOptionFee != null) {
        feesForSelectedPromoOptions.putIfAbsent(selectedPromoOption,
                promoOptionFee);
      }
    });
  }

  private BigDecimal totalFees(
          Map<PromoOption, BigDecimal> calculatedFeesForPromoOptions) {
    return calculatedFeesForPromoOptions.values().stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
