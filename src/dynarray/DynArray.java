package dynarray;

import java.lang.reflect.Array;

public class DynArray<T> {
  public static final int NIL = 0; // команда ещё не вызывалась
  public static final int OK = 1; // последняя команда отработала нормально
  public static final int ERR = 2; // индекс за пределами границ массива

  private int makeArrayStatus;
  private int getItemStatus;
  private int appendStatus;
  private int insertStatus;
  private int removeStatus;

  public T[] array;
  public int count;
  public int capacity;
  Class clazz;

  // конструктор
  // постусловие: создан новый пустой массив заданного размера
  public DynArray(Class clz) {
    clazz = clz;

    count = 0;
    makeArray(16);

    makeArrayStatus = NIL;
    getItemStatus = NIL;
    appendStatus = NIL;
    insertStatus = NIL;
    removeStatus = NIL;
  }

  // команды
  // постусловие: создан массив с новой ёмкостью
  // если ёмкость больше текущей, то оставшиеся ячейки инициализируются null
  public void makeArray(int new_capacity) {
    if (new_capacity < 0) {
      makeArrayStatus = ERR;
      return;
    }

    T[] newArray = (T[]) Array.newInstance(clazz, new_capacity);

    if (array != null) {
      System.arraycopy(array, 0, newArray, 0, count);
    }

    array = newArray;
    capacity = new_capacity;

    makeArrayStatus = OK;
  }

  // постусловие: в конец массива добавлен элемент. Если количество элементов равно размеру массива,
  // массив расширяется
  public void append(T itm) {
    if (count == capacity) {
      expandArray();
    }
    array[count] = itm;
    count++;

    appendStatus = OK;
  }

  // постусловие: элемент либо в конец очереди, либо в середину. Во втором случае все элементы
  // правее смещаются на одну ячейку вправо. Если количество элементов равно размеру массива,
  // массив расширяется. Если индекс за пределами границ массива, то ни чего не происходит
  public void insert(T itm, int index) {
    if (index == count) {
      append(itm);
      return;
    }

    if (index >= count || index < 0) {
      insertStatus = ERR;
      return;
    }

    if (count == capacity) {
      expandArray();
    }

    for (int i = count; i > index; i--) {
      array[i] = array[i - 1];
    }

    array[index] = itm;
    count++;

    insertStatus = OK;
  }

  // постусловие: по индексу удалён элемент, если есть. Если индекс за пределами границ массива,
  // то ни чего не происходит. Массив может уменьшиться при условиях сокращения размера
  public void remove(int index) {
    if (index >= count || index < 0) {
      removeStatus = ERR;
      return;
    }

    for (int i = index; i < count; i++) {
      array[i] = array[i + 1];
    }

    count--;

    if (checkConditionToReduce()) {
      reduceArray();
    }

    removeStatus = OK;
  }

  // постусловие: массив пуст. Создан новый массив стандартного размера
  public void clear() {
    array = null;
    count = 0;
    makeArray(16);
  }

  // запросы
  // постусловие: если индекс за пределами границ массива, то ничего не найдено, иначе возвращает
  // значение по индексу
  public T getItem(int index) {
    if (index >= count || index < 0) {
      getItemStatus = ERR;
      return null;
    }

    getItemStatus = OK;
    return array[index];
  }

  private void expandArray() {
    makeArray(capacity * 2);
  }

  private void reduceArray() {
    int newCapacity = (int) (capacity / 1.5);

    if (newCapacity < 16) {
      makeArray(16);
      return;
    }

    makeArray(newCapacity);
  }

  private boolean checkConditionToReduce() {
    return (capacity % 2 == 0 && count < capacity / 2) ||
        (capacity % 2 != 0 && count <= capacity / 2);
  }

  public int getMakeArrayStatus() {
    return makeArrayStatus;
  }

  public int getGetItemStatus() {
    return getItemStatus;
  }

  public int getAppendStatus() {
    return appendStatus;
  }

  public int getInsertStatus() {
    return insertStatus;
  }

  public int getRemoveStatus() {
    return removeStatus;
  }
}
