package niffler.page.components;

import static com.codeborne.selenide.Selenide.$;

public class Footer extends BaseComponent<Footer> {

    public Footer() {
        super($(".footer"));
    }
}
