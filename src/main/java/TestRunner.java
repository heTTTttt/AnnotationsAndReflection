import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Test {
    int priority() default 5;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface BeforeSuite {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface AfterSuite {}

public class TestRunner {
    public static void start(Class testClass) throws Exception {
        Method beforeSuiteMethod = null;
        Method afterSuiteMethod = null;
        List<Method> testMethods = new ArrayList<>();
        Object testInstance = testClass.getDeclaredConstructor().newInstance();

        Method[] methods = testClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuiteMethod != null) {
                    throw new RuntimeException("Multiple BeforeSuite methods are not allowed");
                }
                beforeSuiteMethod = method;
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuiteMethod != null) {
                    throw new RuntimeException("Multiple AfterSuite methods are not allowed");
                }
                afterSuiteMethod = method;
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }

        Collections.sort(testMethods, (m1, m2) -> {
            int priority1 = m1.getAnnotation(Test.class).priority();
            int priority2 = m2.getAnnotation(Test.class).priority();
            return Integer.compare(priority2, priority1);
        });

        if (beforeSuiteMethod != null) {
            beforeSuiteMethod.invoke(testInstance);
        }

        for (Method testMethod : testMethods) {
            testMethod.invoke(testInstance);
        }

        if (afterSuiteMethod != null) {
            afterSuiteMethod.invoke(testInstance);
        }
    }
}
