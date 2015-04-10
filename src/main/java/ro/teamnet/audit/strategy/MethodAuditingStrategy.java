package ro.teamnet.audit.strategy;

import ro.teamnet.audit.util.AuditInfo;

/**
 * A strategy for auditing methods.
 */
public interface MethodAuditingStrategy {

    /**
     * Sets the information gathered from the audited method.
     * @param auditInfo the information gathered from the audited method
     */
    void setAuditInfo(AuditInfo auditInfo);

    /**
     * Save audited method information before invoking it.
     */
    void auditMethodBeforeInvocation();

    /**
     * Update audited method information after invoking it.
     *
     * @param auditedMethodReturnValue the value returned by the audited method
     */
    void auditMethodAfterInvocation(Object auditedMethodReturnValue);

    /**
     * Save information about errors encountered during method invocation.
     *
     * @param throwable an error encountered during method invocation
     */
    void auditMethodInvocationError(Throwable throwable);
}
