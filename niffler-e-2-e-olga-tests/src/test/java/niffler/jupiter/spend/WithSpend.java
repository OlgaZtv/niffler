package niffler.jupiter.spend;

import niffler.model.Category;
import niffler.model.Currency;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(WithSpendExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WithSpend {
    Category category() default Category.BOOKS;
    Currency currency() default Currency.RUB;
    double amount();
    String description() default "Что-то купила";
    String username();
}
