package org.hr.sso.client.retryer;

import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Collections;
import java.util.Map;



/**
 * @author huangr
 * @version $Id: RetryerUtils.java, v0.1 2018/10/24 10:34 huangr Exp $$
 */
public class RetryerHelper {

    private static final RetryTemplate RETRY_TEMPLATE =  new RetryTemplate();
    static {
        RETRY_TEMPLATE.setBackOffPolicy(BackOffPolicyStrategy.newExponentialBackOffPolicy(1000, 15000, 2));
        RETRY_TEMPLATE.setRetryPolicy(RetryPolicyStrategy.newSimpleRetryPolicy(2, Collections.<Class<? extends Throwable>, Boolean> singletonMap(Exception.class, true)));
    }

    public static  <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E {
        return RETRY_TEMPLATE.execute(retryCallback);
    }

    public static <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RecoveryCallback<T> recoveryCallback) throws E {
        return RETRY_TEMPLATE.execute(retryCallback, recoveryCallback);
    }

    private  static class BackOffPolicyStrategy {

        public static BackOffPolicy newExponentialBackOffPolicy(long initialInterval, long maxInterval, double multiplier) {
            ExponentialBackOffPolicy backOff = new ExponentialBackOffPolicy();
            backOff.setInitialInterval(initialInterval);
            backOff.setMultiplier(multiplier);
            backOff.setMaxInterval(maxInterval);
            return backOff;
        }
    }

    private static class RetryPolicyStrategy {

        public static RetryPolicy newSimpleRetryPolicy(int maxAttempts, Map<Class<? extends Throwable>, Boolean> retryableExceptions) {
            return new SimpleRetryPolicy(maxAttempts, retryableExceptions);
        }
    }
}
