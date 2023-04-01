package niffler.test.web;

import niffler.data.entity.UsersEntity;
import niffler.jupiter.DAOResolver;
import niffler.jupiter.extension.UserEntity;
import niffler.model.CurrencyValues;
import niffler.page.HomePage;
import niffler.page.LoginPage;
import niffler.page.ProfilePage;
import niffler.test.web.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static niffler.model.CurrencyValues.USD;


@ExtendWith({DAOResolver.class})
public class UserDataTest extends BaseTest {

    @UserEntity(username = "dima", currency = USD)
    UsersEntity dima;
    LoginPage loginPage = new LoginPage();
    HomePage homePage = new HomePage();
    ProfilePage profilePage = new ProfilePage();

    @BeforeEach
    void addUserDataBeforeTest() {
        loginPage.fillLoginPage("dima", "12345");
    }

    @Test
    void checkCurrencyTest() {
        homePage.usernameLink.click();
        profilePage.checkCurrency(CurrencyValues.valueOf(dima.getCurrency()));
    }
}
