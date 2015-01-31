package pricingservice;

import com.google.common.collect.ImmutableMap;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class PriceList {

    private final Map<PromoOption, BigDecimal> feesForPromoOptions;

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
