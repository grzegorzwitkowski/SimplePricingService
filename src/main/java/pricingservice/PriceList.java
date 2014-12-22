package pricingservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PriceList {

    private Map<PromoOption, BigDecimal> promoOptionFees = new HashMap<>();
    private int category;
    private int parentCategory;

    public PriceList(Map<PromoOption, BigDecimal> promoOptionFees, int category) {
        this.category = category;
        this.promoOptionFees = promoOptionFees;
        this.parentCategory = -1;
    }

    public PriceList(Map<PromoOption, BigDecimal> promoOptionFees, int category, int parentCategory) {
        this.promoOptionFees = promoOptionFees;
        this.category = category;
        this.parentCategory = parentCategory;
    }

    public Map<PromoOption, BigDecimal> getPromoOptionFees() {
        return promoOptionFees;
    }

    public int getCategory() {
        return category;
    }

    public int getParentCategory() {
        return parentCategory;
    }
}
