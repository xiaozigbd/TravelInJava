package proxytest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Knight on 2017/8/14.
 */
public class TestProxyInvcationHandler implements InvocationHandler {

    public TestProxyInvcationHandler(Object target){
        super();
        this.target = target;
    }

    private Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before invocation");
        Object result = method.invoke(target,args);
        System.out.println("after invocation");
        return result;
    }
}
