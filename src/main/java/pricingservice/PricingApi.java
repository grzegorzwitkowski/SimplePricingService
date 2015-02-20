package pricingservice;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public class PricingApi {

	private final FeeCalculator feeCalculator;
	private final PriceListRepository priceListRepository;
	private final PriceCalculationRepository priceCalculationRepository;

	public PricingApi(FeeCalculator feeCalculator,
					PriceListRepository priceListRepository,
					PriceCalculationRepository priceCalculationRepository) {
		this.feeCalculator = feeCalculator;
		this.priceListRepository = priceListRepository;
		this.priceCalculationRepository = priceCalculationRepository;
	}

	public void addPriceList(PriceList priceList, int category) {
		priceListRepository.add(priceList, category);
	}

	public void removeAllPriceLists() {
		priceListRepository.removeAll();
	}

	public PriceCalculation calculatePrice(Set<PromoOption> selectedPromoOptions,
					Set<Integer> categoryPath) {
		BigDecimal totalPrice = feeCalculator.calculateTotalPrice(
						selectedPromoOptions, categoryPath);
		PriceCalculation priceCalculation = new PriceCalculation(UUID.randomUUID(),
						totalPrice);
		priceCalculationRepository.add(priceCalculation,
						priceCalculation.getCalculationId());
		return priceCalculation;
	}

	public PriceCalculation getPriceCalculation(UUID calculationId) {
		return priceCalculationRepository.get(calculationId);
	}
}
