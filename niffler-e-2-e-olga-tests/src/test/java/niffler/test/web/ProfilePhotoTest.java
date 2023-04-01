package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.ApiLogin;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.user.User;
import niffler.model.UserJson;
import niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

public class ProfilePhotoTest {

    @Test
    @AllureId("2")
    @ApiLogin(nifflerUser = @GenerateUser)
    void userNameShouldBeUpdatedAfterChangingInProfile(@User UserJson user) {
        ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
                .waitForPageLoaded()
                .setFirstName("Pizzly")
                .submitProfile();

        Selenide.refresh();

        profilePage.checkFirstName("Pizzly");
    }

    @Test
    @AllureId("4")
    @ApiLogin(nifflerUser = @GenerateUser)
    void photoShouldBeUDisplayedAfterAddingToProfile(@User UserJson user) {
        String avatarPath = "img/avatar_lama.jpg";
        ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
                .waitForPageLoaded()
                .updateAvatar(avatarPath)
                .submitProfile();

        Selenide.refresh();

        profilePage.checkAvatar(avatarPath);
    }
}
