package niffler.page;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import niffler.config.app.AppProperties;

import static com.codeborne.selenide.Selenide.$;

public class LandingPage extends BasePage {

    @Step("Open landing page")
    public LandingPage open() {
        Selenide.open(AppProperties.FRONTEND_APP_URL);
        return this;
    }

    @Step("Click login button")
    public void clickLoginButton() {
        $("a[href='/redirect']").click();
    }

    @Override
    public BasePage waitForPageLoaded() {
        return null;
    }
}
