package pricingservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PricingApi {

    private Map<Integer, PriceList> priceListsInCategories = new HashMap<>();

    public void addPriceList(PriceList priceList) {
        priceListsInCategories.put(priceList.getCategory(), priceList);
    }

    public BigDecimal calculatePrice(Set<PromoOption> selectedPromoOptions, int category) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        PriceList priceList = priceListsInCategories.get(category);
        for (PromoOption promoOption : selectedPromoOptions) {
            BigDecimal priceForPromoOption = priceList.getFeesForPromoOptions().get(promoOption);
            totalPrice = totalPrice.add(priceForPromoOption);
        }
        return totalPrice;
    }
}
