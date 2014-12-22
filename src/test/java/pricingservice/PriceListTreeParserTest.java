package pricingservice;

import org.assertj.core.data.MapEntry;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static pricingservice.PromoOption.BOLD;
import static pricingservice.PromoOption.HIGHLIGHT;
import static pricingservice.PromoOption.PHOTO;

public class PriceListTreeParserTest {

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
}
