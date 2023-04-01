package niffler.api.logging;

import io.qameta.allure.attachment.AttachmentData;

public final class ReceivedCookieAttachment implements AttachmentData {

    public static ReceivedCookieAttachment newInstance(String name, String value) {
        return new ReceivedCookieAttachment(name, value);
    }

    private final String name, value;

    private ReceivedCookieAttachment(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
