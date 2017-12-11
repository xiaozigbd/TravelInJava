package proxytest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by Knight on 2017/8/14.
 */
public class TestProxyMain {

    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        ProxyInterface target = new ProxyInterfaceImpl();
        InvocationHandler invocationHandler = new TestProxyInvcationHandler(target);
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                target.getClass().getInterfaces(),invocationHandler);
        ((ProxyInterface)proxy).test();

    }
}
