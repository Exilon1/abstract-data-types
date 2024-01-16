package deque;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Deque<T> {

  public static final int NIL = 0; // команда ещё не вызывалась
  public static final int OK = 1; // последняя команда отработала нормально
  public static final int ERR = 2; // очередь пуста

  private final LinkedList<T> linkedList;

  private int addFrontStatus;
  private int addTailStatus;
  private int removeFrontStatus;
  private int removeTailStatus;
  private int clearStatus;

  // конструктор
  // создана пустая очередь
  public Deque() {
    linkedList = new LinkedList<>();

    addFrontStatus = NIL;
    addTailStatus = NIL;
    removeFrontStatus = NIL;
    removeTailStatus = NIL;
    clearStatus = NIL;
  }

  // конструктор
  // создана очередь на основе списка
  public Deque(List<T> list) {
    this.linkedList = new LinkedList<>(list);

    addFrontStatus = NIL;
    addTailStatus = NIL;
    removeFrontStatus = NIL;
    removeTailStatus = NIL;
    clearStatus = NIL;
  }

  // команды
  // постусловие: в голову добавлен новый элемент
  public void addFront(T item) {
    linkedList.addFirst(item);

    addFrontStatus = OK;
  }

  // постусловие: в хвост добавлен новый элемент
  public void addTail(T item) {
    linkedList.addLast(item);

    addTailStatus = OK;
  }

  // предусловие: очередь не пуста;
  // постусловие: из головы очереди удалён элемент
  public T removeFront() {
    try {
      removeFrontStatus = OK;
      return linkedList.removeFirst();
    } catch (NoSuchElementException e) {
      removeFrontStatus = ERR;
      return null;
    }
  }

  // предусловие: очередь не пуста;
  // постусловие: из хвоста очереди удалён элемент
  public T removeTail() {
    try {
      removeTailStatus = OK;
      return linkedList.removeLast();
    } catch (NoSuchElementException e) {
      removeTailStatus = ERR;
      return null;
    }
  }

  // постусловие: удаляет все элементы их очереди
  public void clear() {
    linkedList.clear();
    clearStatus = OK;
  }

  // запросы
  // возвращает размер
  public int size() {
    return linkedList.size();
  }

  // запросы статусов
  // успешно; очередь пуста
  public int getAddFrontStatus() {
    return addFrontStatus;
  }

  // успешно; очередь пуста
  public int getAddTailStatus() {
    return addTailStatus;
  }

  // успешно; очередь пуста
  public int getRemoveFrontStatus() {
    return removeFrontStatus;
  }

  // успешно; очередь пуста
  public int getRemoveTailStatus() {
    return removeTailStatus;
  }

  // успешно; очередь пуста
  public int getClearStatus() {
    return clearStatus;
  }
}
