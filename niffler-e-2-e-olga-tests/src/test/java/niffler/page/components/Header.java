package niffler.page.components;

import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    public Header() {
        super($(".header"));
    }
}
