package pricingservice;

public class PricingApiClient {

    private final PriceListRepository priceListRepository = new PriceListRepository();
    private final PriceCalculationRepository priceCalculationRepository = new PriceCalculationRepository();
    private final FeeCalculator feeCalculator = new FeeCalculator(priceListRepository);
    private final PricingApi pricingApi = new PricingApi(feeCalculator, priceListRepository, priceCalculationRepository);

    public PricingApi getPricingApi() {
        return pricingApi;
    }
}
