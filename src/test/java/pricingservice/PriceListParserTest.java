package pricingservice;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static pricingservice.PriceListParserTest.PriceListAssert.assertThatPriceList;
import static pricingservice.PromoOption.BOLD;
import static pricingservice.PromoOption.HIGHLIGHT;
import static pricingservice.PromoOption.PHOTO;

public class PriceListParserTest {

    private Map<Integer, PriceList> categoriesToPriceLists;

    @Test
    public void shouldParsePriceListTree() throws Exception {
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

        assertThatPriceList(forCategory(0)).hasFee(BOLD, "0.70").hasFee(HIGHLIGHT, "1.00").hasFee(PHOTO, "0.50");
        assertThatPriceList(forCategory(1)).hasFee(BOLD, "0.60").hasFee(PHOTO, "0.40");
        assertThatPriceList(forCategory(2)).hasFee(BOLD, "0.50");
        assertThatPriceList(forCategory(3)).hasFee(BOLD, "0.40").hasFee(PHOTO, "0.30");
        assertThatPriceList(forCategory(4)).hasFee(HIGHLIGHT, "0.90");
    }

    private PriceList forCategory(int category) {
        return categoriesToPriceLists.get(category);
    }

    static class PriceListAssert {

        private final PriceList priceList;

        private PriceListAssert(PriceList priceList) {
            this.priceList = priceList;
        }

        public static PriceListAssert assertThatPriceList(PriceList priceList) {
            return new PriceListAssert(priceList);
        }

        public PriceListAssert hasFee(PromoOption promoOption, String fee) {
            Assertions.assertThat(priceList.getFee(promoOption)).isEqualTo(new BigDecimal(fee));
            return this;
        }
    }
}
