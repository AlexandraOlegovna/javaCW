package ru.ifmo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ServerTask implements Callable<Object> {

    private String clName;
    private String metName;
    private List<Object> params;

    public ServerTask(String cl, String m, List<Object> p) {
        clName = cl;
        metName = m;
        params = new ArrayList<>();
        params.addAll(p);
    }

    public Object call() {
        try {
            Class<?> clazz = Class.forName(clName);
            List<Class<?>> types = new ArrayList<>();
            for (Object p : params)
                types.add(p.getClass());
            Method method = clazz.getMethod(metName, types.toArray(new Class<?>[0]));
            Object result = method.invoke(null, params.toArray(new Object[0]));
            return result;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
