package ro.teamnet.audit.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.teamnet.audit.annotation.Auditable;
import ro.teamnet.audit.annotation.Option;
import ro.teamnet.audit.annotation.Strategy;
import ro.teamnet.audit.constants.AuditStrategy;
import ro.teamnet.audit.strategy.MethodAuditingStrategy;
import ro.teamnet.audit.strategy.MethodAuditingStrategyFactory;
import ro.teamnet.audit.util.AuditInfo;

import java.lang.reflect.Method;
import java.util.Arrays;
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
     * @param auditStrategy the auditing strategy, used to determine the MethodAuditingStrategyFactory
     * @return an instance of MethodAuditingStrategyFactory
     */
    public abstract MethodAuditingStrategyFactory getMethodAuditingStrategyFactory(Strategy auditStrategy);

    /**
     * Perform auditing for all methods with the {@link ro.teamnet.audit.annotation.Auditable} annotation.
     *
     * @param joinPoint a proceeding join point, exposes the proceed(..) method in order to support around advice
     * @param auditable the annotation found on the method
     * @return the value returned by the audited method.
     */
    @Around("auditableMethod() && @annotation(auditable))")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Auditable auditable) {
        Strategy auditStrategy = auditable.strategy();

        if (Objects.equals(auditStrategy.value(), AuditStrategy.IGNORE)) {
            return null;
        }

        Option[] options = auditStrategy.options();

        Method auditedMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();

        log.debug("Started auditing : " + auditedMethod.getName() + ", using strategy : " + auditStrategy.value()
                + ", and options : " + Arrays.toString(options));

        MethodAuditingStrategyFactory strategyFactory = getMethodAuditingStrategyFactory(auditStrategy);
        if (strategyFactory == null) {
            log.warn("No MethodAuditingStrategyFactory implementation was found for the provided audit strategy: {}", auditStrategy);
            return null;
        }

        log.debug("__________________________________________________________________________________________________");
        log.debug("__________________________________________________________________________________________________");

        AuditInfo auditInfo = new AuditInfo(options, auditedMethod, joinPoint.getThis(), joinPoint.getArgs());
        Object auditedMethodReturnValue = null;

        MethodAuditingStrategy auditingStrategy = strategyFactory.getStrategy();

        auditingStrategy.setAuditInfo(auditInfo);
        auditingStrategy.auditMethodBeforeInvocation();
        try {
            log.debug("Invoking audited method: {}", auditedMethod.getName());
            auditedMethodReturnValue = joinPoint.proceed();
            log.debug("Returned value from audited method: {}", auditedMethodReturnValue);
        } catch (Throwable throwable) {
            log.error("Could not proceed: ", throwable);
            auditingStrategy.auditMethodInvocationError(throwable);
        } finally {
            if (auditedMethodReturnValue != null) {
                auditingStrategy.auditMethodAfterInvocation(auditedMethodReturnValue);
            }
            log.debug("Finished auditing : " + auditedMethod.getName() + ", using strategy : " + auditStrategy.value()
                    + ", and options : " + Arrays.toString(options));
            log.debug("__________________________________________________________________________________________________");
            log.debug("__________________________________________________________________________________________________");

        }
        return auditedMethodReturnValue;
    }
}
