import java.util.LinkedList;

public class BoundedStack<T> {

  // интерфейс класса, реализующий АТД BoundedStack
  public static final int POP_NIL = 0; // push() ещё не вызывалась
  public static final int POP_OK = 1; // последняя pop() отработала нормально
  public static final int POP_ERR = 2; // стек пуст
  public static final int PEEK_NIL = 0; // push() ещё не вызывалась
  public static final int PEEK_OK = 1; // последняя peek() вернула корректное значение
  public static final int PEEK_ERR = 2; // стек пуст

  // скрытые поля
  private final LinkedList<T> linkedList = new LinkedList<>(); // основное хранилище стека
  private final int maxSize; // максимальный размер хранилища
  private int peekStatus; // статус запроса peek()
  private int popStatus; // статус команды pop()

  // конструктор
  // постусловие: создан новый пустой стек
  public BoundedStack(int maxSize) {
    this.maxSize = maxSize;
    clear();
  }

  // команды
  // постусловие: из стека удалятся все значения
  public void clear() {
    linkedList.clear(); // очистка стека
    // начальные статусы для предусловий peek() и pop()
    peekStatus = PEEK_NIL;
    popStatus = POP_NIL;
  }

  // предусловие: число элементов стека не превышает maxSize
  // постусловие: в стек добавлено новое значение
  public void push(T val) {
    if (size() < maxSize) {
      linkedList.addLast(val);
      peekStatus = PEEK_OK;
    } else {
      peekStatus = PEEK_ERR;
    }
  }

  // предусловие: стек не пустой
  // постусловие: из стека удалён верхний элемент
  public void pop() {
    if (size() > 0) {
      linkedList.removeLast();
      popStatus = POP_OK;
    } else {
      popStatus = POP_ERR;
    }
  }

  // запросы
  // предусловие: стек не пустой
  // постусловие: состояние стека не меняется
  // возвращает верхний элемент стека
  public T peek() {
    T result;
    if (size() > 0) {
      result = linkedList.getLast();
      peekStatus = PEEK_OK;
    } else {
      result = null;
      peekStatus = PEEK_ERR;
    }
    return result;
  }

  // постусловие: состояние стека не меняется
  // возвращает текущее количество элементов
  public int size() {
    return linkedList.size();
  }

  // запросы статусов
  // возвращает значение POP_*
  public int getPopStatus() {
    return popStatus;
  }

  // возвращает значение PEEK_*
  public int getPeekStatus() {
    return peekStatus;
  }

}
