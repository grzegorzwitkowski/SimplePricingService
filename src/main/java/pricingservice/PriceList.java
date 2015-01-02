package pricingservice;

import com.google.common.collect.ImmutableMap;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PriceList {

    private Map<PromoOption, BigDecimal> feesForPromoOptions = new HashMap<>();

    public PriceList(Map<PromoOption, BigDecimal> feesForPromoOptions) {
        this.feesForPromoOptions = ImmutableMap.copyOf(feesForPromoOptions);
    }

    public Map<PromoOption, BigDecimal> getFeesForPromoOptions() {
        return feesForPromoOptions;
    }

    public BigDecimal getFee(PromoOption promoOption) {
        return feesForPromoOptions.get(promoOption);
    }
}
