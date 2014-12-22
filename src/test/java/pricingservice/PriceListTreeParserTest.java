package pricingservice;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static pricingservice.PromoOption.BOLD;
import static pricingservice.PromoOption.HIGHLIGHT;
import static pricingservice.PromoOption.PHOTO;

public class PriceListTreeParserTest {

    private String tree =
            "--0: BOLD: 0.70, HIGHLIGHT: 1.00, PHOTO: 0.50\n" +
            "  |\n" +
            "  |--1: BOLD: 0.60, PHOTO: 0.40\n" +
            "  |  |\n" +
            "  |  |--2: BOLD: 0.50\n" +
            "  |  |\n" +
            "  |  |--3: BOLD: 0.40, PHOTO: 0.30\n" +
            "  |\n" +
            "  |--4: HIGHLIGHT: 0.90";

    @Test
    public void shouldParse() throws Exception {
        Map<Integer, PriceList> priceLists = new PriceListParser().parse(tree);

        assertThat(priceLists.get(0).getPromoOptionFees())
                .containsOnly(entry(BOLD, price("0.70")), entry(HIGHLIGHT, price("1.00")), entry(PHOTO, price("0.50")));
        assertThat(priceLists.get(1).getPromoOptionFees())
                .containsOnly(entry(BOLD, price("0.60")), entry(PHOTO, price("0.40")));
        assertThat(priceLists.get(2).getPromoOptionFees())
                .containsOnly(entry(BOLD, price("0.50")));
        assertThat(priceLists.get(3).getPromoOptionFees())
                .containsOnly(entry(BOLD, price("0.40")), entry(PHOTO, price("0.30")));
        assertThat(priceLists.get(4).getPromoOptionFees())
                .containsOnly(entry(HIGHLIGHT, price("0.90")));
    }

    private BigDecimal price(String price) {
        return new BigDecimal(price);
    }

    private static class PriceListParser {

        public Map<Integer, PriceList> parse(String priceListsAsString) throws IOException {
            Map<Integer, Integer> indexToCategory = new HashMap<>();
            indexToCategory.put(-1, Categories.NO_PARENT);
            HashMap<Integer, PriceList> priceLists = new HashMap<>();
            BufferedReader reader = new BufferedReader(new StringReader(priceListsAsString));
            String line = reader.readLine();
            while (line != null) {
                if (line.contains("--")) {
                    int parentCategoryIndex = line.indexOf("--") - 1;
                    int categoryIndex = line.indexOf("--") + 2;
                    int categoryEndIndex = line.indexOf(":");
                    int category = Integer.valueOf(line.substring(categoryIndex, categoryEndIndex));
                    indexToCategory.put(categoryIndex, category);

                    Map<PromoOption, BigDecimal> promoOptionFees = parsePromoFees(line.substring(categoryEndIndex + 1).trim());
                    PriceList p = new PriceList(promoOptionFees, category, indexToCategory.get(parentCategoryIndex));
                    priceLists.put(category, p);
                }
                line = reader.readLine();
            }
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
