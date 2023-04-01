package niffler.jupiter.spend;

import niffler.model.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(WithSpendExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WithSpend {
    String category() default "Книги";
    CurrencyValues currency() default CurrencyValues.RUB;
    double amount();
    String description() default "Что-то купила";
    String username();
}
