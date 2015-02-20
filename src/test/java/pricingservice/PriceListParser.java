package pricingservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceListParser {

	private static final Pattern CATEGORY_LINE_PATTERN = Pattern
					.compile("#\\s(\\d+)\\s*:\\s*(.*)");
	private static final Pattern PROMO_OPTIONS_PATTERN = Pattern
					.compile("(\\w+)\\s*:\\s*(\\d+.?\\d*)");

	public Map<Integer, PriceList> parse(String priceListsTree)
					throws IOException {
		Map<Integer, PriceList> priceListsInCategories = new HashMap<>();
		new BufferedReader(new StringReader(priceListsTree)).lines().forEach(
						line -> {
							if (isLineWithCategory(line)) {
								parseLineForCategory(priceListsInCategories, line);
							}
						});
		return priceListsInCategories;
	}

	private boolean isLineWithCategory(String line) {
		return CATEGORY_LINE_PATTERN.matcher(line).find();
	}

	private void parseLineForCategory(
					Map<Integer, PriceList> priceListsInCategories, String line) {
		Matcher matcher = CATEGORY_LINE_PATTERN.matcher(line);
		if (matcher.find()) {
			int category = Integer.valueOf(matcher.group(1));
			String promoOptionsPart = matcher.group(2);
			Map<PromoOption, BigDecimal> promoOptionFees = parsePromoOptionFees(promoOptionsPart);
			priceListsInCategories.put(category, new PriceList(promoOptionFees));
		}
	}

	private Map<PromoOption, BigDecimal> parsePromoOptionFees(
					String promoOptionsPart) {
		Matcher matcher = PROMO_OPTIONS_PATTERN.matcher(promoOptionsPart);
		Map<PromoOption, BigDecimal> feesForPromoOptions = new HashMap<>();
		while (matcher.find()) {
			PromoOption promoOption = PromoOption.valueOf(matcher.group(1));
			BigDecimal fee = new BigDecimal(matcher.group(2));
			feesForPromoOptions.put(promoOption, fee);
		}
		return feesForPromoOptions;
	}
}
