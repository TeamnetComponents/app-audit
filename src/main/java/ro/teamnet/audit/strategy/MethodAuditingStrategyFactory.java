package ro.teamnet.audit.strategy;

/**
 * A factory for creating MethodAuditingStrategy instances.
 */
public interface MethodAuditingStrategyFactory {

    /**
     * Retrieves an instance of {@link MethodAuditingStrategy}.
     *
     * @return the method auditing strategy
     */
    MethodAuditingStrategy getStrategy();
}
