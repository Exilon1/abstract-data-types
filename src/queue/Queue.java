package queue;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Queue<T> {

  public static final int NIL = 0; // команда ещё не вызывалась
  public static final int OK = 1; // последняя команда отработала нормально
  public static final int ERR = 2; // очередь пуста

  private int enqueueStatus;
  private int dequeueStatus;

  private final LinkedList<T> linkedList;

  // конструктор
  // постусловие: создана новая пустая очередь
  public Queue() {
    linkedList = new LinkedList<>();

    enqueueStatus = NIL;
    dequeueStatus = NIL;
  }

  // команды
  // постусловие: в конец очереди добавлен элемент
  public void enqueue(T item) {
    linkedList.addLast(item);

    enqueueStatus = OK;
  }


  public int size() {
    return linkedList.size();
  }

  // запросы
  // предусловие: очередь не пуста
  // постусловие: первый элемент удалён из очереди. Первым элементом стал второй
  public T dequeue() {
    try {
      T val = linkedList.pop();
      enqueueStatus = OK;
      return val;
    } catch (NoSuchElementException e) {
      enqueueStatus = ERR;
      return null;
    }
  }

  // успешно
  public int getEnqueueStatus() {
    return enqueueStatus;
  }

  // успешно; очередь пуста
  public int getDequeueStatus() {
    return dequeueStatus;
  }
}
