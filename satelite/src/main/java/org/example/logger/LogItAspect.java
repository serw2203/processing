package org.example.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
@Aspect
public class LogItAspect {

    @Pointcut("@annotation(org.example.logger.LogIt)")
    public void logIt() {
    }

    @Around("logIt()")
    public Object aroundMethodExecuted(ProceedingJoinPoint point) throws Throwable {

        Logger logger = LoggerFactory.getLogger(point.getSignature().getDeclaringType());

        printMethodRun(logger, point);
        printMethodArguments(logger, point);

        long start = System.currentTimeMillis();
        Object result = point.proceed();
        long stop = System.currentTimeMillis();

        printMethodResult(logger, result);
        printMethodEnd(logger, start, stop);

        return result;
    }

    private void printMethodEnd(Logger logger, long start, long stop) {
        logger.info("End. Running time (millis) = " + (stop - start));
    }

    private void printMethodResult(Logger logger, Object result) {
        logger.info(
                "return value: {}: {}",
                result != null ? result.getClass().getSimpleName() : "null",
                toString(result)
        );
    }

    private void printMethodRun(Logger logger, ProceedingJoinPoint point) {
        logger.info("Run: {}", point.getSignature());
    }

    private void printMethodArguments(Logger logger, ProceedingJoinPoint point) {
        Object[] arguments = point.getArgs();

        StringBuilder sb = new StringBuilder("args: \n");
        for (int i = 0; i < arguments.length; i++) {
            if (i != 0) {
                sb.append("\n");
            }
            Object argument = arguments[i];

            if (argument != null) {
                sb.append(format("arg[%s] %s: %s", i, argument.getClass().getSimpleName(), toString(argument)));
            } else {
                sb.append(format("arg[%s]: %s", i, "null"));
            }

        }
        logger.info(sb.toString());
    }

    private String toString (Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "!!UNDEFINED!!";
        }
    }
}
