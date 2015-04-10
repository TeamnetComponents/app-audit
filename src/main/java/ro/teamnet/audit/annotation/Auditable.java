package ro.teamnet.audit.annotation;


import ro.teamnet.audit.constants.AuditStrategy;
import ro.teamnet.audit.constants.AuditedMethodType;

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
     * <p>Defaults to {@link AuditStrategy#IGNORE}, indicating that no auditing should be performed on the method.</p>
     *
     * @return the audit strategy to be applied to the annotated method
     * @see AuditStrategy
     */
    String strategy() default AuditStrategy.IGNORE;

    /**
     * Specifies the type of the audited method. May be used by the auditing service in order to determine how to
     * specifically handle the annotated method.
     * <p>Defaults to {@link AuditedMethodType#DEFAULT}.</p>
     *
     * @return the audited method type
     * @see AuditedMethodType
     */
    String type() default AuditedMethodType.DEFAULT;
}