package powerset;

public class PowerSet extends HashTable {

  public static final int ERR_NOT_DUB = 3; // попытка вставить дубликат

  private int putToSetStatus;

  public PowerSet(int sz) {
    super(sz);

    putToSetStatus = NIL;
  }

  // предусловие: в множестве не содержится добавляемое значение
  // постусловие: добавлено новое уникальное значение
  @Override
  public int put(String value) {
    int index = super.put(value);

    if (index > -1 && slots[index] == null) {
      size++;
    }

    if (index > -1) {
      slots[index] = value;
      putToSetStatus = OK;
      return index;
    }

    putToSetStatus = ERR_NOT_DUB;
    return -1;
  }

  public int getPutToSetStatus() {
    return putToSetStatus;
  }
}

class HashTable {

  public static final int NIL = 0; // команда ещё не вызывалась
  public static final int OK = 1; // последняя команда отработала нормально
  public static final int ERR_NOT_FOUND = 2; // значение не найдено


  private int getStatus;
  private int putStatus;
  private int removeStatus;

  public int size;
  public int step;
  public String[] slots;

  // конструктор
  // предусловие: значение sz больше 0
  // создаёт пустой массив заданного размера
  public HashTable(int sz) {
    size = sz;
    if (sz / 3 > 0) {
      step = sz / 3;
    } else {
      step = 1;
    }
    slots = new String[size];

    for (int i = 0; i < size; i++) {
      slots[i] = null;
    }

    getStatus = NIL;
    putStatus = NIL;
    removeStatus = NIL;
  }

  // команды
  // постусловие: значение добавлено в пустой слот
  protected int put(String value) {
    int index = seekSlot(value);

    if (index > -1) {
      slots[index] = value;
      putStatus = OK;
      return index;
    }

    putStatus = ERR_NOT_FOUND;
    return index;
  }

  // предусловие: массив не пуст
  public String get(String value) {
    int index = find(value);

    if (index > -1) {
      getStatus = OK;
      return slots[index];
    }

    getStatus = ERR_NOT_FOUND;
    return null;
  }

  // постусловие: удалён элемент, если существовал в таблице
  public void remove(String value) {
    int index = find(value);

    if (index > -1) {
      removeStatus = OK;
      slots[index] = null;
      return;
    }

    removeStatus = ERR_NOT_FOUND;
  }

  private int hashFun(String value) {
    if (value == null) {
      return -1;
    }
    return Math.abs(value.hashCode()) % size;
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

      if (slots[index] == null) {
        return index;
      }

      return -1;
    } catch (IndexOutOfBoundsException e) {
      return -1;
    }
  }

  // предусловие: искомое значение не null
  // постусловие: возвращает индекс найденного значения или -1
  private int find(String value) {
    int index = hashFun(value);

    try {
      if (slots[index] == null) {
        return -1;
      }

      if (slots[index].equals(value)) {
        return index;
      }

      for (int i = 0; i < size; i++) {
        if (i == index) {
          continue;
        }

        if (value.equals(slots[i])) {
          return i;
        }
      }
      return -1;
    } catch (IndexOutOfBoundsException e) {
      return -1;
    }
  }

  // постусловие: удаляет все значения из массива
  public void clear() {
    for (int i = 0; i < size; i++) {
      slots[i] = null;
    }
  }

  public int getPutStatus() {
    return putStatus;
  }

  public int getGetStatus() {
    return getStatus;
  }

  public int getRemoveStatus() {
    return removeStatus;
  }
}