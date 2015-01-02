package pricingservice;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PriceList {

    private Map<PromoOption, BigDecimal> feesForPromoOptions = new HashMap<>();

    public PriceList(Map<PromoOption, BigDecimal> feesForPromoOptions) {
        this.feesForPromoOptions = Collections.unmodifiableMap(feesForPromoOptions);
    }

    public Map<PromoOption, BigDecimal> getFeesForPromoOptions() {
        return feesForPromoOptions;
    }

    public Set<PromoOption> getDefinedPromoOptions() {
        return feesForPromoOptions.keySet();
    }
}
