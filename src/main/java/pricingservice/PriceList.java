package pricingservice;

import java.math.BigDecimal;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

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
