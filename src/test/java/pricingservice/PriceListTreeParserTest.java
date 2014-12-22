package pricingservice;

import org.assertj.core.data.MapEntry;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static pricingservice.PromoOption.BOLD;
import static pricingservice.PromoOption.HIGHLIGHT;
import static pricingservice.PromoOption.PHOTO;

public class PriceListTreeParserTest {

    private Map<Integer, PriceList> categoriesToPriceLists;

    @Test
    public void shouldParse() throws Exception {
        String priceListsTree =
                "--0: BOLD: 0.70, HIGHLIGHT: 1.00, PHOTO: 0.50\n" +
                        "  |\n" +
                        "  |--1: BOLD: 0.60, PHOTO: 0.40\n" +
                        "  |  |\n" +
                        "  |  |--2: BOLD: 0.50\n" +
                        "  |  |\n" +
                        "  |  |--3: BOLD: 0.40, PHOTO: 0.30\n" +
                        "  |\n" +
                        "  |--4: HIGHLIGHT: 0.90";

        categoriesToPriceLists = new PriceListParser().parse(priceListsTree);

        assertThat(priceListForCategory(0)).containsOnly(entry(BOLD, "0.70"), entry(HIGHLIGHT, "1.00"), entry(PHOTO, "0.50"));
        assertThat(priceListForCategory(1)).containsOnly(entry(BOLD, "0.60"), entry(PHOTO, "0.40"));
        assertThat(priceListForCategory(2)).containsOnly(entry(BOLD, "0.50"));
        assertThat(priceListForCategory(3)).containsOnly(entry(BOLD, "0.40"), entry(PHOTO, "0.30"));
        assertThat(priceListForCategory(4)).containsOnly(entry(HIGHLIGHT, "0.90"));
    }

    private Map<PromoOption, BigDecimal> priceListForCategory(int category) {
        return categoriesToPriceLists.get(category).getFeesForPromoOptions();
    }

    private MapEntry entry(PromoOption promoOption, String price) {
        return MapEntry.entry(promoOption, new BigDecimal(price));
    }

    private static class PriceListParser {

        public Map<Integer, PriceList> parse(String priceListsTree) throws IOException {
            Map<Integer, Integer> indexesToCategories = new HashMap<>();
            Map<Integer, PriceList> priceLists = new HashMap<>();
            new BufferedReader(new StringReader(priceListsTree)).lines().forEach(line -> {
                int categoryIndicatorIndex = line.indexOf("--");
                if (categoryIndicatorIndex != -1) {
                    int parentCategoryIndex = categoryIndicatorIndex - 1;
                    int categoryIndex = categoryIndicatorIndex + 2;
                    int categoryEndIndex = line.indexOf(":");
                    int category = Integer.valueOf(line.substring(categoryIndex, categoryEndIndex));
                    indexesToCategories.put(categoryIndex, category);

                    String promoOptionFeesLine = line.substring(categoryEndIndex + 1).trim();
                    Map<PromoOption, BigDecimal> promoOptionFees = parsePromoFees(promoOptionFeesLine);

                    if (parentCategoryIndex < 0) {
                        priceLists.put(category, new PriceList(promoOptionFees));
                    } else {
                        priceLists.put(category, new PriceList(promoOptionFees, category, indexesToCategories.get(parentCategoryIndex)));
                    }
                }
            });
            return priceLists;
        }

        private Map<PromoOption, BigDecimal> parsePromoFees(String promoFeesLine) {
            String[] splittedFees = promoFeesLine.split(",");

            Map<PromoOption, BigDecimal> promoOptionFees = new HashMap<>();
            for (String splittedFee : splittedFees) {
                String[] feeNameAndValue = splittedFee.split(":");
                PromoOption promoOption = PromoOption.valueOf(feeNameAndValue[0].trim());
                BigDecimal fee = new BigDecimal(feeNameAndValue[1].trim());
                promoOptionFees.put(promoOption, fee);
            }
            return promoOptionFees;
        }
    }
}
