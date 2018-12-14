# RMI

Легенда: вы хотите исполнять некий код локально, но на самом деле возможно только его удаленное исполнение.

Как это устроено: предположим, есть интерфейс, с экземпляром которого вы хотите работать. Данный интерфейс унаследован классом-прокси, который все вызовы пересылает на сервер и возвращает их результат.

Необходимо: реализовать интерфейсы RmiClientService и RmiServerService, с помощью которых класс-прокси будет выполнять свою работу.

---

# Этапы

* Реализовать вызов методов у классов, которые не имеют состояния (invokeStatic)
    * Т.е. можно свободно пересоздавать их экземпляры
    
* Реализовать вызов методов у классов, которые имеют состояние (invokeOn)
    * Сами экземпляры передавать нельзя
    
* Реализовать поддержку исключений при исполнении кода на сервере и уведомление клиента об этом
    * Уведомлять нужно только об исключениях произошедших непосредственно во время исполнения вызываемого кода

---

# RmiClientService

```java
import java.lang.reflect.Method;

public interface RmiClientService {

    // на удаленном экземпляре класса clazz вызывается метод method с аргументами params
    Object invokeStatic(Class<?> clazz, Method method, Object... params);
    
    // инициализирует удаленного двойника для localReceiverReference
    // с помощью конструктора constructor и аргументов params
    void init(Object localReceiverReference, Constructor constructor, Object... params);
    
    // якобы на localReceiverReference,
    // но на самом деле на удаленном его двойнике
    // вызывается метод method с аргументами params
    Object invokeOn(Object localReceiverReference, Method method, Object... params);
}
```

---

# RmiServerService

```java
public interface RmiServerService {
    
    // запускает обработку подключений на указанном порту
    // каждую команду на исполнение запускает в отдельном потоке (см пул потоков)
    void launch(String port);
}
```

---

# Гарантии и замечания

* Считайте, что и серверу, и клиенту доступны одни и те же классы
* Передаваемые аргументы реализуют интерфейс Serializable
* Помните про перегрузки методов