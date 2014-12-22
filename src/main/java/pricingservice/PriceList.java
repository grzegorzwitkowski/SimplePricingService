package pricingservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PriceList {

    private Map<PromoOption, BigDecimal> feesForPromoOptions = new HashMap<>();
    private int category;
    private int parentCategory;

    public PriceList(Map<PromoOption, BigDecimal> feesForPromoOptions) {
        this.feesForPromoOptions = feesForPromoOptions;
        this.category = Categories.ROOT;
        this.parentCategory = Categories.NO_CATEGORY;
    }

    public PriceList(Map<PromoOption, BigDecimal> feesForPromoOptions, int category, int parentCategory) {
        this.feesForPromoOptions = feesForPromoOptions;
        this.category = category;
        this.parentCategory = parentCategory;
    }

    public Map<PromoOption, BigDecimal> getFeesForPromoOptions() {
        return feesForPromoOptions;
    }

    public int getCategory() {
        return category;
    }

    public int getParentCategory() {
        return parentCategory;
    }
}
