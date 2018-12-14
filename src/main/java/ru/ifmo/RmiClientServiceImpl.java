package ru.ifmo;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RmiClientServiceImpl implements RmiClientService {

    // на удаленном экземпляре класса clazz вызывается метод method с аргументами params
    public Object invokeStatic(Class<?> clazz, Method method, Object... params) {
        try {
            Socket socket = new Socket("localhost", 3000);
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            String clName = clazz.getName();
            String metName = method.getName();
            oos.writeUTF(clName);
            oos.writeUTF(metName);

            oos.writeInt(params.length);
            for (Object p : params) {
                oos.writeObject(p);
            }

            os.flush();

            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            Object result = ois.readObject();
            return result;

        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    // инициализирует удаленного двойника для localReceiverReference
    // с помощью конструктора constructor и аргументов params
    public void init(Object localReceiverReference, Constructor constructor, Object... params) {
    }

    // якобы на localReceiverReference,
    // но на самом деле на удаленном его двойнике
    // вызывается метод method с аргументами params
    public Object invokeOn(Object localReceiverReference, Method method, Object... params) {
        return null; // =(
    }
}
