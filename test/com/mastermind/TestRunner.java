package com.mastermind;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A tiny, dependency-free test runner.
 *
 * Conventions (a minimal subset of JUnit's):
 *   - Test classes are plain classes with a public no-arg constructor.
 *   - Instance methods named "setUp" (public or package-private, no args) run before every test.
 *   - Instance methods named "test*" (public, no args, void) are treated as test cases.
 *
 * Usage: java com.mastermind.TestRunner com.mastermind.GameLogicTest [more test classes...]
 */
public class TestRunner {

    public static void main(String[] args) throws Exception {
        List<Class<?>> testClasses = new ArrayList<Class<?>>();
        if (args.length == 0) {
            testClasses.add(GameLogicTest.class);
        } else {
            for (String className : args) {
                testClasses.add(Class.forName(className));
            }
        }

        int totalTests = 0;
        int totalFailures = 0;

        for (Class<?> testClass : testClasses) {
            System.out.println("Running " + testClass.getName());

            List<Method> testMethods = new ArrayList<Method>();
            for (Method method : testClass.getMethods()) {
                if (method.getName().startsWith("test")
                        && method.getParameterTypes().length == 0
                        && method.getReturnType() == void.class) {
                    testMethods.add(method);
                }
            }
            Collections.sort(testMethods, new Comparator<Method>() {
                @Override
                public int compare(Method a, Method b) {
                    return a.getName().compareTo(b.getName());
                }
            });

            Method setUp = null;
            try {
                setUp = testClass.getDeclaredMethod("setUp");
                setUp.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // No setUp method; that's fine.
            }

            for (Method testMethod : testMethods) {
                totalTests++;
                try {
                    Object instance = testClass.newInstance();
                    if (setUp != null) {
                        setUp.invoke(instance);
                    }
                    testMethod.invoke(instance);
                    System.out.println("  [PASS] " + testMethod.getName());
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    totalFailures++;
                    System.out.println("  [FAIL] " + testMethod.getName() + " - " + cause.getMessage());
                }
            }
        }

        System.out.println();
        System.out.println("Tests run: " + totalTests + ", Failures: " + totalFailures);

        if (totalFailures > 0) {
            System.exit(1);
        }
    }
}
