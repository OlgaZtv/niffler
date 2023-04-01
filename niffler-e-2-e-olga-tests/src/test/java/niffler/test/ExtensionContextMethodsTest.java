package niffler.test;

import niffler.jupiter.FullCallbackExtension;
import niffler.test.web.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FullCallbackExtension.class)
public class ExtensionContextMethodsTest extends BaseTest {

    /*
    Посмотреть эксперементально – написав код, – на каком этапе жизненного цикла Extension работают методы
    ExtensionContext.getRequiredTestMethod(),
    ExtensionContext.getRequiredTestClass(),
    ExtensionContext.getRequiredTestInstance()
     */

    @Test
    void contextMethods() {
        System.out.println("                        ### Test");

        /*
        На этапе BeforeAllCallback, AfterAllCallback доступен только getRequiredTestClass
        На остальных уровнях доступны все три метода
         */
    }
}
