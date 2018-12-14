package ru.ifmo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class RmiServerServiceImpl implements RmiServerService {

    // запускает обработку подключений на указанном порту
    // каждую команду на исполнение запускает в отдельном потоке (см пул потоков)
    public void launch(String port) {
        int portInt;
        try {
            portInt = Integer.parseInt(port);
        }
        catch (Exception ignore) {
            portInt = 3000;
        }

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portInt);
            System.out.println("waiting connections....");

            ExecutorService pool = Executors.newSingleThreadExecutor();


            while (true) {
                Socket socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                String clName = ois.readUTF();
                String metName = ois.readUTF();
                int parLength = ois.readInt();

                List<Object> params = new ArrayList<>();
                for (int i = 0; i < parLength; ++i)
                    params.add(ois.readObject());

                Future<Object> result = pool.submit(new ServerTask(clName, metName, params));

                OutputStream os = socket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(result.get());
                oos.flush();
            }


        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }

    }
}