package niffler.service;

import niffler.data.CategoryEntity;
import niffler.data.SpendEntity;
import niffler.data.repository.CategoryRepository;
import niffler.data.repository.SpendRepository;
import niffler.model.CurrencyValues;
import niffler.model.SpendJson;
import niffler.model.StatisticByCategoryJson;
import niffler.model.StatisticJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class SpendServiceTest {

    private SpendService spendService;

    private final CurrencyValues userCurrency = CurrencyValues.USD;

    private SpendEntity firstSpend, secondSpend, thirdSpend;
    private CategoryEntity firstCategory, secondCategory, thirdCategory;

    @BeforeEach
    void setUp(@Mock SpendRepository spendRepository,
               @Mock CategoryRepository categoryRepository,
               @Mock GrpcCurrencyClient grpcCurrencyClient) {
        firstCategory = new CategoryEntity();
        firstCategory.setCategory("Бар");
        secondCategory = new CategoryEntity();
        secondCategory.setCategory("Магазин");
        thirdCategory = new CategoryEntity();
        thirdCategory.setCategory("Рыбалка");

        firstSpend = new SpendEntity();
        firstSpend.setCategory(firstCategory);
        firstSpend.setAmount(750.0);
        firstSpend.setDescription("Коктейль двойной дайкири");
        firstSpend.setCurrency(CurrencyValues.RUB);
        firstSpend.setSpendDate(addDaysToDate(new Date(), Calendar.DAY_OF_WEEK, -3));

        secondSpend = new SpendEntity();
        secondSpend.setCategory(firstCategory);
        secondSpend.setAmount(600.0);
        secondSpend.setDescription("Бокал пива");
        secondSpend.setCurrency(CurrencyValues.RUB);
        secondSpend.setSpendDate(addDaysToDate(new Date(), Calendar.DAY_OF_WEEK, -1));

        thirdSpend = new SpendEntity();
        thirdSpend.setCategory(thirdCategory);
        thirdSpend.setAmount(12000.0);
        thirdSpend.setDescription("Спининг");
        thirdSpend.setCurrency(CurrencyValues.RUB);
        thirdSpend.setSpendDate(addDaysToDate(new Date(), Calendar.DAY_OF_WEEK, -2));

        lenient().when(spendRepository.findAllByUsernameAndSpendDateLessThanEqual(eq("dima"), any(Date.class)))
                .thenReturn(List.of(
                        firstSpend, secondSpend, thirdSpend
                ));

        lenient().when(spendRepository.findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(eq("dima"), any(Date.class), any(Date.class)))
                .thenReturn(List.of(
                        secondSpend, thirdSpend
                ));

        lenient().when(categoryRepository.findAllByUsername(eq("dima")))
                .thenReturn(List.of(
                        firstCategory, secondCategory, thirdCategory
                ));

        lenient().when(grpcCurrencyClient.calculate(any(Double.class), eq(CurrencyValues.RUB), eq(CurrencyValues.USD)))
                .thenAnswer(a -> BigDecimal.valueOf((double) a.getArguments()[0] / 75.0));

        spendService = new SpendService(spendRepository, categoryRepository, grpcCurrencyClient);
    }

    @Test
    void getStatisticTest() {
        List<StatisticJson> result = spendService.getStatistic("dima", userCurrency, null, null, null);
        assertEquals(4, result.size());
    }

    static Stream<Arguments> resolveDesiredCurrenciesInStatisticTest() {
        return Stream.of(
                Arguments.of(null, CurrencyValues.values()),
                Arguments.of(CurrencyValues.KZT, new CurrencyValues[]{CurrencyValues.KZT}),
                Arguments.of(CurrencyValues.USD, new CurrencyValues[]{CurrencyValues.USD})
        );
    }

    @MethodSource
    @ParameterizedTest
    void resolveDesiredCurrenciesInStatisticTest(CurrencyValues tested, CurrencyValues[] expected) {
        CurrencyValues[] currencyValues = spendService.resolveDesiredCurrenciesInStatistic(tested);
        assertArrayEquals(expected, currencyValues);
    }

    @Test
    void createDefaultStatisticJsonTest() {
        Date dateTo = new Date();
        StatisticJson defaultStatisticJson = spendService.createDefaultStatisticJson(CurrencyValues.KZT, userCurrency, dateTo);
        assertEquals(dateTo, defaultStatisticJson.getDateTo());
        assertEquals(CurrencyValues.KZT, defaultStatisticJson.getCurrency());
        assertEquals(userCurrency, defaultStatisticJson.getUserDefaultCurrency());
        assertEquals(0.0, defaultStatisticJson.getTotal());
        assertEquals(0.0, defaultStatisticJson.getTotalInUserDefaultCurrency());
        assertNull(defaultStatisticJson.getDateFrom());
        assertNull(defaultStatisticJson.getCategoryStatistics());
    }

    @Test
    void enrichStatisticDateFromByFirstStreamElementTest() {
        Date dateTo = new Date();
        StatisticJson defaultStatisticJson = spendService.createDefaultStatisticJson(CurrencyValues.KZT, userCurrency, dateTo);

        Stream.of(secondSpend, firstSpend, thirdSpend)
                .sorted(Comparator.comparing(SpendEntity::getSpendDate))
                .peek(spendService.enrichStatisticDateFromByFirstStreamElement(defaultStatisticJson))
                .collect(Collectors.toList());

        assertEquals(firstSpend.getSpendDate(), defaultStatisticJson.getDateFrom());
    }

    @Test
    void enrichStatisticTotalAmountByAllStreamElementsTest() {
        Date dateTo = new Date();
        StatisticJson defaultStatisticJson = spendService.createDefaultStatisticJson(CurrencyValues.KZT, userCurrency, dateTo);

        Stream.of(secondSpend, firstSpend, thirdSpend)
                .sorted(Comparator.comparing(SpendEntity::getSpendDate))
                .peek(spendService.enrichStatisticTotalAmountByAllStreamElements(defaultStatisticJson))
                .collect(Collectors.toList());

        assertEquals(13350.0, defaultStatisticJson.getTotal());
    }

    @Test
    void enrichStatisticTotalInUserCurrencyByAllStreamElementsSameCurrencyTest() {
        Date dateTo = new Date();
        CurrencyValues statisticCurrency = CurrencyValues.RUB;
        CurrencyValues userCurrency = CurrencyValues.RUB;
        StatisticJson defaultStatisticJson = spendService.createDefaultStatisticJson(statisticCurrency, userCurrency, dateTo);

        Stream.of(secondSpend, firstSpend, thirdSpend)
                .sorted(Comparator.comparing(SpendEntity::getSpendDate))
                .peek(spendService.enrichStatisticTotalAmountByAllStreamElements(defaultStatisticJson))
                .peek(spendService.enrichStatisticTotalInUserCurrencyByAllStreamElements(defaultStatisticJson, statisticCurrency, userCurrency))
                .collect(Collectors.toList());

        assertEquals(13350.0, defaultStatisticJson.getTotal());
        assertEquals(13350.0, defaultStatisticJson.getTotalInUserDefaultCurrency());
    }

    @Test
    void enrichStatisticTotalInUserCurrencyByAllStreamElementsDifferentCurrencyTest() {
        Date dateTo = new Date();
        CurrencyValues statisticCurrency = CurrencyValues.RUB;
        CurrencyValues userCurrency = CurrencyValues.USD;
        StatisticJson defaultStatisticJson = spendService.createDefaultStatisticJson(statisticCurrency, userCurrency, dateTo);

        Stream.of(secondSpend, firstSpend, thirdSpend)
                .sorted(Comparator.comparing(SpendEntity::getSpendDate))
                .peek(spendService.enrichStatisticTotalAmountByAllStreamElements(defaultStatisticJson))
                .peek(spendService.enrichStatisticTotalInUserCurrencyByAllStreamElements(defaultStatisticJson, statisticCurrency, userCurrency))
                .collect(Collectors.toList());

        assertEquals(13350.0, defaultStatisticJson.getTotal());
        assertEquals(178.0, defaultStatisticJson.getTotalInUserDefaultCurrency());
    }

    @Test
    void bindSpendsToCategoriesTest() {
        Date dateTo = new Date();
        CurrencyValues statisticCurrency = CurrencyValues.RUB;
        StatisticJson defaultStatisticJson = spendService.createDefaultStatisticJson(statisticCurrency, userCurrency, dateTo);

        Map<String, List<SpendJson>> map = spendService.bindSpendsToCategories(defaultStatisticJson, statisticCurrency, userCurrency, List.of(
                secondSpend, firstSpend, thirdSpend
        ));

        assertEquals(2, map.size());
        assertNotNull(map.get("Бар"));
        assertNotNull(map.get("Рыбалка"));
        List<SpendJson> barSpends = map.get("Бар");
        List<SpendJson> fishCatchSpends = map.get("Рыбалка");
        assertEquals(2, barSpends.size());
        assertEquals(1, fishCatchSpends.size());
    }

    private Date addDaysToDate(Date date, int selector, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(selector, days);
        return cal.getTime();
    }

    @Test
    void createStatisticByCategoryJsonListTest() {
        Date dateTo = new Date();
        CurrencyValues statisticCurrency = CurrencyValues.RUB;
        CurrencyValues userCurrency = CurrencyValues.USD;
        StatisticJson defaultStatisticJson = spendService.createDefaultStatisticJson(statisticCurrency, userCurrency, dateTo);
        Map<String, List<SpendJson>> spendsByCategory = spendService.bindSpendsToCategories(defaultStatisticJson, statisticCurrency, userCurrency, List.of(
                secondSpend, firstSpend, thirdSpend
        ));
        List<StatisticByCategoryJson> statisticByCategories = spendService.createStatisticByCategoryJsonList(statisticCurrency, userCurrency, spendsByCategory);

        Map<String, Double> expectedTotalByCategory = createExpectedTotalByCategory();
        Map<String, Double> expectedTotalInUserCurrencyByCategory = createExpectedTotalInUserCurrencyByCategory();

        assertExpectedTotals(expectedTotalByCategory, expectedTotalInUserCurrencyByCategory, statisticByCategories);
    }

    private Map<String, Double> createExpectedTotalByCategory() {
        Map<String, Double> expectedTotalByCategory = new HashMap<>();
        expectedTotalByCategory.put("Рыбалка", 12000.0);
        expectedTotalByCategory.put("Бар", 1350.0);
        return expectedTotalByCategory;
    }

    private Map<String, Double> createExpectedTotalInUserCurrencyByCategory() {
        Map<String, Double> expectedTotalInUserCurrencyByCategory = new HashMap<>();
        expectedTotalInUserCurrencyByCategory.put("Рыбалка", 160.0);
        expectedTotalInUserCurrencyByCategory.put("Бар", 18.0);
        return expectedTotalInUserCurrencyByCategory;
    }

    private void assertExpectedTotals(Map<String, Double> expectedTotalByCategory, Map<String, Double> expectedTotalInUserCurrencyByCategory, List<StatisticByCategoryJson> statisticByCategories) {
        assertEquals(expectedTotalByCategory.size(), statisticByCategories.size());
        for (StatisticByCategoryJson statisticByCategory : statisticByCategories) {
            String category = statisticByCategory.getCategory();
            assertEquals(expectedTotalByCategory.get(category), statisticByCategory.getTotal());
            assertEquals(expectedTotalInUserCurrencyByCategory.get(category), statisticByCategory.getTotalInUserDefaultCurrency());
        }
    }

    @Test
    void addRemainingEmptySpendCategoriesToStatisticTest() {
        Date dateTo = new Date();
        CurrencyValues statisticCurrency = CurrencyValues.RUB;
        CurrencyValues userCurrency = CurrencyValues.USD;
        StatisticJson defaultStatisticJson = spendService.createDefaultStatisticJson(statisticCurrency, userCurrency, dateTo);
        Map<String, List<SpendJson>> spendsByCategory = spendService.bindSpendsToCategories(defaultStatisticJson, statisticCurrency, userCurrency, List.of(
                secondSpend, firstSpend, thirdSpend
        ));
        List<StatisticByCategoryJson> statisticByCategories = spendService.createStatisticByCategoryJsonList(statisticCurrency, userCurrency, spendsByCategory);

        List<StatisticByCategoryJson> statisticWithEmptyCategories = new ArrayList<>();
        spendService.addRemainingEmptySpendCategoriesToStatistic("dima", spendsByCategory, statisticWithEmptyCategories);
        spendService.addRemainingEmptySpendCategoriesToStatistic("dima", spendsByCategory, statisticByCategories);

        assertEquals(1, statisticWithEmptyCategories.size());
        assertEquals("Магазин", statisticWithEmptyCategories.get(0).getCategory());
        assertEquals(0, statisticWithEmptyCategories.get(0).getSpends().size());
        assertEquals(0.0, statisticWithEmptyCategories.get(0).getTotal(), 0.0);
        assertEquals(0.0, statisticWithEmptyCategories.get(0).getTotalInUserDefaultCurrency(), 0.0);

        assertEquals(3, statisticByCategories.size());
    }

}