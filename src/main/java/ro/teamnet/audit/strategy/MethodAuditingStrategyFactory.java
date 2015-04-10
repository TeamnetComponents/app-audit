package ro.teamnet.audit.strategy;

/**
 * A factory for creating MethodAuditingStrategy instances.
 */
public interface MethodAuditingStrategyFactory {

    /**
     * Retrieves the proper instance of {@link MethodAuditingStrategy} based on the audited method name.
     * @param methodName the audited method name
     * @return the method auditing strategy
     */
    MethodAuditingStrategy getStrategy(String methodName);
}
