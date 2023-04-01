package niffler.test.web;

import niffler.jupiter.extension.ScreenshotExtension;
import niffler.jupiter.user.User;
import niffler.model.UserModel;
import niffler.page.LoginPage;
import niffler.test.web.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static niffler.jupiter.user.User.UserType.ADMIN;
import static niffler.jupiter.user.User.UserType.COMMON;

@ExtendWith({ScreenshotExtension.class})
public class NifflerUsersTest extends BaseTest {

    @Test
    void test1(@User(userType = ADMIN) UserModel user) {
        System.out.println("#### Test 1. Username: " + user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.fillLoginPage("Kate", "pass");
    }

    @Test
    void test2(@User(userType = ADMIN) UserModel userFirst,
               @User(userType = COMMON) UserModel userSecond,
               @User(userType = COMMON) UserModel userThird) {
        System.out.println("#### Test 2. Username: " + userFirst.getUsername());
        System.out.println("#### Test 2. Username: " + userSecond.getUsername());
        System.out.println("#### Test 2. Username: " + userThird.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.fillLoginPage("Kate", "pass");
    }

    @Test
    void test3(@User UserModel user) {
        System.out.println("#### Test 3. Username: " + user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.fillLoginPage("Kate", "pass");
    }

    @Test
    void test4(@User UserModel user) {
        System.out.println("#### Test 4. Username: " + user.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.fillLoginPage("Kate", "pass");
    }

    @Test
    void test5(@User UserModel userFirst, @User UserModel userSecond) {
        System.out.println("#### Test 5. Username: " + userFirst.getUsername());
        System.out.println("#### Test 5. Username: " + userSecond.getUsername());
        LoginPage loginPage = new LoginPage();
        loginPage.fillLoginPage("Kate", "pass");
    }
}

