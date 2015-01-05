package pricingservice;

import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PricingApi {

    private static Map<UUID, PriceCalculation> priceCalculations = new HashMap<>();

    private Map<Integer, PriceList> priceListsInCategories = new HashMap<>();

    public void addPriceList(PriceList priceList, int category) {
        priceListsInCategories.put(category, priceList);
    }

    public PriceCalculation calculatePrice(Set<PromoOption> selectedPromoOptions, Set<Integer> categoryPath) {
        Map<PromoOption, BigDecimal> feesForSelectedPromoOptions = calculateFeesForPromoOptions(selectedPromoOptions, categoryPath);
        BigDecimal totalPrice = calculateTotalPrice(feesForSelectedPromoOptions);
        PriceCalculation priceCalculation = new PriceCalculation(UUID.randomUUID(), totalPrice);
        priceCalculations.put(priceCalculation.getCalculationId(), priceCalculation);
        return priceCalculation;
    }

    private Map<PromoOption, BigDecimal> calculateFeesForPromoOptions(Set<PromoOption> selectedPromoOptions, Set<Integer> categoryPath) {
        ListIterator<Integer> it = ImmutableList.copyOf(categoryPath).listIterator(categoryPath.size());
        Map<PromoOption, BigDecimal> feesForSelectedPromoOptions = new HashMap<>();
        while (it.hasPrevious()) {
            int category = it.previous();
            PriceList priceListForCategory = priceListsInCategories.get(category);
            if (priceListForCategory != null) {
                for (PromoOption selectedPromoOption : selectedPromoOptions) {
                    BigDecimal promoOptionFee = priceListForCategory.getFeesForPromoOptions().get(selectedPromoOption);
                    if (promoOptionFee != null) {
                        feesForSelectedPromoOptions.putIfAbsent(selectedPromoOption, promoOptionFee);
                    }
                }
            }
        }
        return feesForSelectedPromoOptions;
    }

    private BigDecimal calculateTotalPrice(Map<PromoOption, BigDecimal> feesForSelectedPromoOptions) {
        return feesForSelectedPromoOptions.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public PriceCalculation getPriceCalculation(UUID calculationId) {
        return priceCalculations.get(calculationId);
    }
}
