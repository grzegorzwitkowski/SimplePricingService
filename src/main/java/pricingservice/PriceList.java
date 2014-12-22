package pricingservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PriceList {

    private Map<PromoOption, BigDecimal> promoOptionFees = new HashMap<>();

    public PriceList(Map<PromoOption, BigDecimal> promoOptionFees) {
        this.promoOptionFees = promoOptionFees;
    }

    public Map<PromoOption, BigDecimal> getPromoOptionFees() {
        return promoOptionFees;
    }
}
