package nativedictionary;

import java.lang.reflect.Array;

class NativeDictionary<T> {

  public static final int NIL = 0; // команда ещё не вызывалась
  public static final int OK = 1; // последняя команда отработала нормально
  public static final int NOT_FOUND = 2; // значения нет в таблице
  public static final int ERR_FULL = 3; // система коллизий не смогла найти свободный слот для значения

  private int putStatus;
  private int removeStatus;

  public int size;
  public String[] slots;
  public T[] values;
  private int step;

  // конструктор
  // постусловие: создана пустая хэш-таблица заданного размера
  public NativeDictionary(int sz, Class clazz) {
    size = sz;
    slots = new String[size];
    values = (T[]) Array.newInstance(clazz, size);
    step = 3;

    putStatus = NIL;
    removeStatus = NIL;
  }

  // команды
  // предусловие: в словаре имеется свободный слот по ключу
  // постусловие: в словарь добавлены ключ и значение
  public void put(String key, T value) {
    int index = seekSlot(key);

    if (index > -1) {
      slots[index] = key;
      values[index] = value;
      putStatus = OK;
    }

    putStatus = ERR_FULL;
  }

  // предусловие: в словаре имеется значение по ключу;
  // постусловие: из таблицы удалено значение по ключу
  public void remove(String key) {
    int index = find(key);

    if (index == -1) {
      removeStatus = NOT_FOUND;
      return;
    }

    values[index] = null;
    removeStatus = OK;
  }

  // запросы
  // предусловие: в словаре имеется значение по ключу;
  public boolean isKey(String key) {
    int index = find(key);

    if (index == -1) {
      return false;
    }

    return true;
  }

  // предусловие: в словаре имеется значение по ключу;
  public T get(String key) {
    int index = find(key);

    if (index == -1) {
      return null;
    }

    return values[index];
  }

  public void clear() {
    for (int i = 0; i < size; i++) {
      slots[i] = null;
      values[i] = null;
    }
  }

  private int seekSlot(String value) {
    int index = find(value);

    if (index > -1) {
      return index;
    }

    index = hashFun(value);
    int count = 0;

    try {
      while (slots[index] != null && count < size) {
        count++;

        if (size - index - 1 < step) {
          index = step - size + index;

          if (size % step == 0) {
            index++;
          }

          continue;
        }

        index = index + step;
      }

      return slots[index] == null ? index : -1;
    } catch (IndexOutOfBoundsException e) {
      return -1;
    }
  }

  private int find(String key) {
    int index = hashFun(key);

    try {
      if (slots[index] == null) {
        return -1;
      }

      if (slots[index].equals(key)) {
        return index;
      }

      for (int i = 0; i < size; i++) {
        if (i == index) {
          continue;
        }

        if (key.equals(slots[i])) {
          return i;
        }
      }

      return -1;
    } catch (IndexOutOfBoundsException e) {
      return -1;
    }
  }

  private int hashFun(String key) {
    return Math.abs(key.hashCode()) % size;
  }

  // запросы статусов (возможные значения статусов)
  public int getPutStatus() {
    return putStatus;
  } // успешно;
  // система коллизий не смогла найти свободный слот для значения

  public int getRemoveStatus() {
    return removeStatus;
  } // успешно; значения нет в словаре
}
