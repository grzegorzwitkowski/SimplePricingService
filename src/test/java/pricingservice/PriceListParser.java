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

    private static final Pattern LINE_PATTERN = Pattern.compile("(--)(\\d+)\\s*:\\s*(.*)");
    private static final Pattern PROMO_OPTIONS_PATTERN = Pattern.compile("(\\w+)\\s*:\\s*(\\d+.?\\d*)");

    public Map<Integer, PriceList> parse(String priceListsTree) throws IOException {
        Map<Integer, Integer> indexesToCategories = new HashMap<>();
        Map<Integer, PriceList> priceLists = new HashMap<>();
        new BufferedReader(new StringReader(priceListsTree)).lines().forEach(line -> {
            Matcher matcher = LINE_PATTERN.matcher(line);
            if (matcher.find()) {
                int parentCategoryIndex = matcher.start(1);
                int parentCategory = indexesToCategories.containsKey(parentCategoryIndex) ? indexesToCategories.get(parentCategoryIndex) : Categories.NO_CATEGORY;
                int category = Integer.valueOf(matcher.group(2));
                int categoryIndex = matcher.start(2);
                String promoOptionsPart = matcher.group(3);
                indexesToCategories.put(categoryIndex, category);
                Map<PromoOption, BigDecimal> feesForPromoOptions = parseFeesForPromoOptions(promoOptionsPart);
                priceLists.put(category, new PriceList(feesForPromoOptions, category, parentCategory));
            }
        });
        return priceLists;
    }

    private Map<PromoOption, BigDecimal> parseFeesForPromoOptions(String promoOptionsPart) {
        Matcher matcher = PROMO_OPTIONS_PATTERN.matcher(promoOptionsPart);
        Map<PromoOption, BigDecimal> feesForPromoOptions = new HashMap<>();
        while (matcher.find()) {
            PromoOption promoOption = PromoOption.valueOf(matcher.group(1));
            BigDecimal value = new BigDecimal(matcher.group(2));
            feesForPromoOptions.put(promoOption, value);
        }
        return feesForPromoOptions;
    }
}
