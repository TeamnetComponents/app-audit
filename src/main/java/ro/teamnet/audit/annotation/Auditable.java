package ro.teamnet.audit.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method is to be audited.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auditable {

    /**
     * Specifies the audit strategy to be be applied to the annotated method.
     *
     * @return the audit strategy to be applied to the annotated method
     */
    Strategy strategy();
}