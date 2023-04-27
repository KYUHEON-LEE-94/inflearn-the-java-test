package me.khlee.inflearnthejavatest;

import jdk.jfr.Threshold;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.event.annotation.AfterTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Slow한 테스트일 경우 @SlowTEST를 붙이라고 알려주는 확장 class
 */
public class FindSlowTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private long THRESHOLD = 1000L;

    public FindSlowTestExtension(long THRESHOLD) {
        this.THRESHOLD = THRESHOLD;
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = getStore(context);
        store.put("START TIME", System.currentTimeMillis());
    }



    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        /*리플렉션*/
        //현재 실행중인 테스트 메소드를 나타내는 Method 객체를 가져온다.
        Method requiredTestMethod = context.getRequiredTestMethod();
        //현재 실행중인 테스트 메소드에 @SlowTest가 있는가?
        SlowTest annotation = requiredTestMethod.getAnnotation(SlowTest.class);
        //-> @SlowTest가 없으면 null을 반환한다.

        String testMethodName = requiredTestMethod.getName();
        ExtensionContext.Store store = getStore(context);
        //key를 value타입으로 가져옴
        Long startTime = store.remove("START TIME", long.class);
        System.out.println(startTime);
        long duration = System.currentTimeMillis() - startTime;

        if(duration > THRESHOLD && annotation == null){
            System.out.printf("Please consider mark metho [%s] with @slowTest. \n", testMethodName);
        }
    }
    private static ExtensionContext.Store getStore(ExtensionContext context) {
        String testClassName = context.getRequiredTestClass().getName();
        String testMethodName = context.getRequiredTestMethod().getName();
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));
        System.out.println(testClassName);
        System.out.println(testMethodName);
        return store;
    }


}
