package niffler.test.web;

import niffler.config.Config;
import niffler.jupiter.extension.JpaExtension;
import niffler.page.HomePage;
import niffler.page.LandingPage;
import niffler.page.LoginPage;
import niffler.page.ProfilePage;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(JpaExtension.class)
public abstract class BaseTest {

    protected static final Config CFG = Config.getConfig();

    protected final LandingPage landingPage = new LandingPage();
    protected final LoginPage loginPage = new LoginPage();
    protected final HomePage homePage = new HomePage();

    protected final ProfilePage profilePage = new ProfilePage();
}
