package niffler.jupiter.annotation;

import niffler.model.CurrencyValues;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface GenerateSpend {

    String date() default "";
    String category();
    CurrencyValues currency() default CurrencyValues.RUB;
    double amount();
    String description() default "";

}