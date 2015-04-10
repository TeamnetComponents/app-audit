package ro.teamnet.audit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides information concerning the annotated parameter to the auditing service.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AuditedParameter {
    /**
     * Describes a parameter of an audited method. May be used by the auditing service in order to determine how to
     * specifically handle the annotated parameter.
     *
     * @return the parameter description
     */
    String description();
}
