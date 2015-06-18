package ro.teamnet.audit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the auditing strategy.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Strategy {
    /**
     * The auditing strategy name.
     *
     * @return the strategy name.
     */
    String value();

    /**
     * Options of the audited method. May be used by the auditing strategy in order to determine how to
     * specifically handle the annotated method.
     *
     * @return the options of the audited method
     */
    Option[] options() default {};

}
