package ro.teamnet.audit.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.teamnet.audit.annotation.Auditable;
import ro.teamnet.audit.constants.AuditStrategy;
import ro.teamnet.audit.strategy.MethodAuditingStrategy;
import ro.teamnet.audit.strategy.MethodAuditingStrategyFactory;
import ro.teamnet.audit.util.AuditInfo;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * An abstract aspect used for auditing.
 */
public abstract class AbstractAuditingAspect {
    private Logger log = LoggerFactory.getLogger(AbstractAuditingAspect.class);

    /**
     * A pointcut for specifying methods bearing the {@link ro.teamnet.audit.annotation.Auditable} annotation.
     */
    @Pointcut("execution(@ro.teamnet.audit.annotation.Auditable * *(..))")
    public void auditableMethod() {
    }

    /**
     * Retrieves the proper MethodAuditingStrategyFactory implementation based on the {@link ro.teamnet.audit.annotation.Auditable#strategy} value
     * from the audited method annotation.
     *
     * @param auditStrategy the auditing strategy, used to deretmine the MethodAuditingStrategyFactory
     * @return an instance of MethodAuditingStrategyFactory
     */
    public abstract MethodAuditingStrategyFactory getMethodAuditingStrategyFactory(String auditStrategy);

    /**
     * Perform auditing for all methods with the {@link ro.teamnet.audit.annotation.Auditable} annotation.
     *
     * @param joinPoint a proceeding join point, exposes the proceed(..) method in order to support around advice
     * @param auditable the annotation found on the method
     * @return the value returned by the audited method.
     */
    @Around("auditableMethod() && @annotation(auditable))")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Auditable auditable) {
        String auditStrategy = auditable.strategy();

        if (Objects.equals(auditStrategy, AuditStrategy.IGNORE)) {
            return null;
        }

        Method auditedMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String auditableType = auditable.type() == null ? auditedMethod.getName() : auditable.type();

        log.info("Started auditing : " + auditableType + ", using strategy : " + auditStrategy);

        MethodAuditingStrategyFactory strategyFactory = getMethodAuditingStrategyFactory(auditStrategy);
        if (strategyFactory == null) {
            log.warn("No MethodAuditingStrategyFactory implementation was found for the provided audit strategy: {}", auditStrategy);
            return null;
        }

        log.info("__________________________________________________________________________________________________");
        log.info("__________________________________________________________________________________________________");

        AuditInfo auditInfo = new AuditInfo(auditableType, auditedMethod, joinPoint.getThis(), joinPoint.getArgs());
        Object auditedMethodReturnValue = null;

        MethodAuditingStrategy auditingStrategy = strategyFactory.getStrategy(auditableType);

        auditingStrategy.setAuditInfo(auditInfo);
        auditingStrategy.auditMethodBeforeInvocation();
        try {
            log.info("Invoking audited method: {}", auditedMethod.getName());
            auditedMethodReturnValue = joinPoint.proceed();
            log.info("Returned value from audited method: {}", auditedMethodReturnValue);
        } catch (Throwable throwable) {
            log.warn("Could not proceed: ", throwable);
            auditingStrategy.auditMethodInvocationError(throwable);
        } finally {
            if (auditedMethodReturnValue != null) {
                auditingStrategy.auditMethodAfterInvocation(auditedMethodReturnValue);
            }
            log.info("Finished auditing : " + auditableType + ", using strategy : " + auditStrategy);
            log.info("__________________________________________________________________________________________________");
            log.info("__________________________________________________________________________________________________");

            return auditedMethodReturnValue;
        }
    }
}