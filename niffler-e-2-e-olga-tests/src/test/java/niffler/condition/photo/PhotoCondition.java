package niffler.condition.photo;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class PhotoCondition extends Condition {
    private final String expectedPhotoClasspath;

    private PhotoCondition(String expectedPhotoClasspath) {
        super("photo");
        this.expectedPhotoClasspath = Objects.requireNonNull(expectedPhotoClasspath, "Argument must not be null or empty string.");
    }

    public static Condition photo(String expectedPhotoClasspath) {
        return new PhotoCondition(expectedPhotoClasspath);
    }

    @Nonnull
    @Override
    public CheckResult check(Driver driver, WebElement element) {
        String actualPhoto = element.getAttribute("src");
        byte[] actualBase64Photo = StringUtils.substringAfter(actualPhoto, "base64,").getBytes(StandardCharsets.UTF_8);
        byte[] expectedBase64Photo = getPhotoAsBase64StringFromClasspath();

        boolean valueMatches = Arrays.equals(expectedBase64Photo, actualBase64Photo);
        return new CheckResult(valueMatches, actualPhoto);
    }

    private byte[] getPhotoAsBase64StringFromClasspath() {
        ClassLoader classLoader = PhotoCondition.class.getClassLoader();
        try (InputStream is = Objects.requireNonNull(classLoader.getResourceAsStream(expectedPhotoClasspath), "Photo not found in classpath: " + expectedPhotoClasspath)) {
            return Base64.getEncoder().encode(is.readAllBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

