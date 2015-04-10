package ro.teamnet.audit.constants;

/**
 * Lists supported auditing strategies.
 */
public final class AuditStrategy {
    private AuditStrategy() {
    }

    /**
     * Ignore the {@link ro.teamnet.audit.annotation.Auditable} annotation and perform no auditing.
     */
    public static final String IGNORE = "ignore";

}
