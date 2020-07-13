package com.github.rbaul.spring.boot.activity_log.config;

import com.github.rbaul.spring.boot.activity_log.ActivityLogReceiver;
import com.github.rbaul.spring.boot.activity_log.objects.ActivityLogObject;
import com.github.rbaul.spring.boot.activity_log.objects.ActivityLogStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aspect to {@link ActivityLog} parameters.
 *
 * @author Roman Baul
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ActivityLogAspect {

    private static final String ANONYMOUS_USERNAME = "anonymous";
    private final List<ActivityLogReceiver> activityLogReceivers;
    private final ApplicationContext applicationContext;

    /**
     * Pointcut of parameters with {@link ActivityLog}
     */
    @Pointcut("@annotation(activityLog)")
    protected void activityLogMethod(ActivityLog activityLog) {
    }

    @Around(value = "activityLogMethod(activityLog)", argNames = "joinPoint,activityLog")
    public Object activityLogExecutionMethod(ProceedingJoinPoint joinPoint, ActivityLog activityLog) throws Throwable {
        String templateAction = activityLog.value();

        String actionMessage = getMessage(joinPoint, templateAction);

        try {
            if (activityLog.onPre()) {
                notifyActivityLog(createActivityLogObject(activityLog.onPrePrefix() + actionMessage,
                        ActivityLogStatus.SUCCESS));
            }

            Object returnVal = joinPoint.proceed();
            notifyActivityLog(createActivityLogObject(actionMessage, ActivityLogStatus.SUCCESS));
            return returnVal;
        } catch (Throwable ex) {
            notifyActivityLog(createActivityLogObject(actionMessage, ActivityLogStatus.FAILED));
            throw ex;
        }
    }

    /**
     * Get Activity log object
     */
    private ActivityLogObject createActivityLogObject(String actionMessage, ActivityLogStatus activityLogStatus) {
        return ActivityLogObject.builder()
                .username(getUserName())
                .action(actionMessage)
                .status(activityLogStatus).build();
    }

    /**
     * Create template with argument values instead of templates
     *
     * @param joinPoint ProceedingJoinPoint
     * @param template  template message
     * @return message
     */
    private String getMessage(ProceedingJoinPoint joinPoint, String template) {
        Set<String> messageArgumentNames = getMessageArgumentNames(template);

        Map<String, Object> parametersValue = getArgumentsValue(messageArgumentNames, joinPoint);

        // Replace all template arguments {{...}}
        if (!StringUtils.isEmpty(template)) {
            for (String parameterName : parametersValue.keySet()) {
                Object parameterValue = parametersValue.getOrDefault(parameterName, null);
                template = template.replace(parameterName,
                        (parameterValue == null ? "null" : parameterValue.toString()));
            }
        }

        return template;
    }

    /**
     * Get username from security context
     *
     * @return username
     */
    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ANONYMOUS_USERNAME;
        }

        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    /**
     * Get arguments name from template
     *
     * @param template template message
     * @return arguments name
     */
    private Set<String> getMessageArgumentNames(String template) {
        Pattern pattern = Pattern.compile("\\{{2}(\\S)*}{2}");
        Matcher matcher = pattern.matcher(template);
        Set<String> parameters = new HashSet<>();
        while (matcher.find()) {
            String result = matcher.group(0);
            parameters.add(result);
        }

        return parameters;
    }

    /**
     * Get relevant parameters values
     *
     * @param messageArgumentNames arguments name from message
     * @param joinPoint            ProceedingJoinPoint
     * @return map of argument values
     */
    private Map<String, Object> getArgumentsValue(Set<String> messageArgumentNames, ProceedingJoinPoint joinPoint) {
        Map<String, Object> parametersValue = new HashMap<>();
        if (!CollectionUtils.isEmpty(messageArgumentNames)) {
            messageArgumentNames.forEach(parameter -> {
                String parameterWithoutPrefixAndPostfix = parameter.substring(2).replace("}}", "");
                if (parameterWithoutPrefixAndPostfix.startsWith("@")) {
                    parametersValue.put(parameter, getBeanMethodResponse(joinPoint, parameterWithoutPrefixAndPostfix));
                } else if (parameterWithoutPrefixAndPostfix.startsWith("#")) {
                    parametersValue.put(parameter, getInnerMethodResponse(joinPoint, parameterWithoutPrefixAndPostfix));
                } else if (parameter.startsWith("$")) {
                    parametersValue.put(parameter, getResponseValue(joinPoint, parameterWithoutPrefixAndPostfix));
                } else {
                    parametersValue.put(parameter, getArgumentValues(new HashSet<>(Collections.singletonList(parameterWithoutPrefixAndPostfix)), joinPoint).get(parameterWithoutPrefixAndPostfix));
                }
            });
        }
        return parametersValue;
    }

    /**
     * Get relevant parameters values
     *
     * @param messageArgumentNames arguments name from message
     * @param joinPoint            ProceedingJoinPoint
     * @return map of argument values
     */
    private Map<String, Object> getArgumentValues(Set<String> messageArgumentNames, ProceedingJoinPoint joinPoint) {
        Map<String, Object> parametersValue = new HashMap<>();
        if (!CollectionUtils.isEmpty(messageArgumentNames)) {
            List<String> paramNames = Arrays.asList(((CodeSignature) joinPoint.getSignature()).getParameterNames());
            Object[] paramValues = joinPoint.getArgs();

            messageArgumentNames.forEach(parameter -> {
                List<String> split = Arrays.asList(parameter.split("\\."));
                Optional<String> stringOptional = split.stream().findFirst();
                if (stringOptional.isPresent()) {
                    String parmName = stringOptional.get();
                    int indexOf = paramNames.indexOf(parmName);
                    if (indexOf > -1) {
                        try {
                            Object paramValue = paramValues[indexOf];

                            // recursive get field
                            for (int i = 1; i < split.size(); i++) {
                                String fieldName = split.get(i);
                                Field declaredField = paramValue.getClass().getDeclaredField(fieldName);
                                String getMethodOfField = getGetMethodFromFieldName(fieldName, declaredField.getType());
                                Method declaredMethod = paramValue.getClass().getDeclaredMethod(getMethodOfField);
                                paramValue = declaredMethod.invoke(paramValue);

//                                declaredField.setAccessible(true);
//                                paramValue = declaredField.get(paramValue);
                            }
                            parametersValue.put(parameter, paramValue);
                        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        return parametersValue;
    }

    /**
     * Get method of get field
     */
    private String getGetMethodFromFieldName(String fieldName, Class<?> fieldType) {
        if (fieldType.isPrimitive() && fieldType == boolean.class) {
            return "is" + getCapitalizeString(fieldName);
        } else {
            return "get" + getCapitalizeString(fieldName);
        }
    }

    private String getCapitalizeString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private void notifyActivityLog(ActivityLogObject activityLogObject) {
        if (!CollectionUtils.isEmpty(activityLogReceivers)) {
            activityLogReceivers.forEach(activityLogReceiver ->
                    activityLogReceiver.receive(activityLogObject));
        }
    }

    /**
     * Get string from
     *
     * @param beanMethod - example @beanName.methodName(arguments)
     * @return
     */
    private String getBeanMethodResponse(ProceedingJoinPoint joinPoint, String beanMethod) {
        if (beanMethod.startsWith("@")) {
            int firstDotIndex = beanMethod.indexOf(".");
            String beanName = beanMethod.substring(1, firstDotIndex);
            Object bean = applicationContext.getBean(beanName);

            String methodSignature = beanMethod.substring(firstDotIndex + 1);
            return getMethodResponse(joinPoint, bean, methodSignature);
        }
        return null;
    }

    /**
     * Get string from method
     *
     * @param method - example #methodName(arguments)
     * @return
     */
    private String getInnerMethodResponse(ProceedingJoinPoint joinPoint, String method) {
        if (method.contains("#")) {
            Object target = joinPoint.getTarget();
            return getMethodResponse(joinPoint, target, method.substring(1));
        }
        return null;
    }

    /**
     * Get Method response
     *
     * @return
     */
    private String getMethodResponse(ProceedingJoinPoint joinPoint, Object target, String methodSignature) {
        String methodName = methodSignature.substring(0, methodSignature.indexOf("("));
        String argumentsString = methodSignature.substring(methodSignature.indexOf("(") + 1, methodSignature.indexOf(")"));
        Map<String, Object> argumentsValue;
        String[] argumentNames;
        if (argumentsString.isEmpty()) {
            argumentsValue = new HashMap<>();
            argumentNames = new String[]{};
        } else {
            argumentNames = argumentsString.split(",");
            Set<String> args = new HashSet<>(Arrays.asList(argumentNames));
            argumentsValue = getArgumentValues(args, joinPoint);
        }

        Object[] arguments = Arrays.stream(argumentNames).map(argumentsValue::get).toArray(Object[]::new);
        Optional<Method> methodToExecute = Arrays.stream(target.getClass().getMethods())
                .filter(method -> Objects.equals(method.getName(), methodName) && method.getParameterCount() == argumentsValue.size()).findFirst();

        if (methodToExecute.isPresent()) {
            Object result = ReflectionUtils.invokeMethod(methodToExecute.get(), target, arguments);
            return result.toString();
        }
        return null;
    }

    private Object getResponseValue(ProceedingJoinPoint joinPoint, String responseValue) {
        if (responseValue.contains("$")) {
            Object target = joinPoint.getTarget();
            return getMethodResponse(joinPoint, target, responseValue.substring(1));
        }
        return null;
    }
}