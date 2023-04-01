package niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import niffler.model.CurrencyValues;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static niffler.condition.photo.PhotoCondition.photo;

public class ProfilePage extends BasePage{

    public static final String URL = CFG.frontUrl() + "profile";

    private final SelenideElement firstNameInput = $("input[name='firstname']");
    private final SelenideElement surnameInput = $("input[name='surname']");
    private final SelenideElement categoryInput = $("input[name='category']");
    private final SelenideElement currencySelector = $("div .select-wrapper");
    private final SelenideElement submitButton = $(byText("Submit"));
    private final SelenideElement createCategoryButton = $(byText("Create"));

    @Step("Fill profile page with name: {0}, surname: {1}, currency: {2}")
    public ProfilePage fillProfile(String name, String surname, CurrencyValues currencyValues) {
        setFirstName(name);
        setSurname(surname);
        setCurrency(currencyValues);
        submitProfile();
        return this;
    }

    @Step("Set first name: {0}")
    public ProfilePage setFirstName(String name) {
        firstNameInput.setValue(name);
        return this;
    }

    @Step("Set surname: {0}")
    public ProfilePage setSurname(String surname) {
        surnameInput.setValue(surname);
        return this;
    }

    @Step("Set currency: {0}")
    public ProfilePage setCurrency(CurrencyValues currency) {
        currencySelector.click();
        $$("div[id^='react-select']").find(text(currency.name())).click();
        return this;
    }

    @Step("Check first name: {0}")
    public ProfilePage checkFirstName(String name) {
        firstNameInput.shouldHave(value(name));
        return this;
    }

    @Step("Check surname: {0}")
    public ProfilePage checkSurname(String surname) {
        surnameInput.shouldHave(value(surname));
        return this;
    }

    @Step("Check currency: {0}")
    public ProfilePage checkCurrency(CurrencyValues currency) {
        currencySelector.shouldHave(text(currency.name()));
        return this;
    }

    @Step("Save profile")
    public ProfilePage submitProfile() {
        submitButton.click();
        return this;
    }

    @Step("Check that page is loaded")
    @Override
    public ProfilePage waitForPageLoaded() {
        firstNameInput.should(visible);
        return this;
    }

    @Step("Update avatar with image: {avatarPath}")
    public ProfilePage updateAvatar(String avatarPath) {
        $(".profile__avatar").click();
        $(".edit-avatar__input[type=file]").uploadFromClasspath(avatarPath);
        return this;
    }

    @Step("Check avatar image: {avatarPath}")
    public void checkAvatar(String avatarPath) {
        $(".profile__avatar").shouldHave(photo(avatarPath));
    }
}
