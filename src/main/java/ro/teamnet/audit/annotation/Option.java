package ro.teamnet.audit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An auditing option.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Option {
    /**
     * The option key.
     *
     * @return the option key.
     */
    String key() default "";

    /**
     * The option value.
     *
     * @return the option value.
     */
    String value();
}
