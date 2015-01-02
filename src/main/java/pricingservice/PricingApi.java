package pricingservice;

import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class PricingApi {

    private Map<Integer, PriceList> priceListsInCategories = new HashMap<>();

    public void addPriceList(PriceList priceList, int category) {
        priceListsInCategories.put(category, priceList);
    }

    public BigDecimal calculatePrice(Set<PromoOption> selectedPromoOptions, Set<Integer> categoryPath) {
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
        return feesForSelectedPromoOptions.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
