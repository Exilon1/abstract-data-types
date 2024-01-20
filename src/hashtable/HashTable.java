package hashtable;

import java.util.Random;

public class HashTable {

  public static final int NIL = 0; // команда ещё не вызывалась
  public static final int OK = 1; // последняя команда отработала нормально
  public static final int ERR_NULL = 2; // искомое значение null
  public static final int ERR_FULL = 3; // массив заполнен

  private int hashFunStatus;
  private int findStatus;
  private int seekSlotStatus;
  private int putStatus;

  public int size;
  public int step;
  public String[] slots;

  // конструктор
  // предусловие: значение sz больше 0
  // создаёт пустой массив заданного размера
  public HashTable(int sz) {
    size = sz;
    if (sz/3 > 0) {
      step = sz/3;
    } else {
      step = 1;
    }
    slots = new String[size];

    for (int i = 0; i < size; i++) {
      slots[i] = null;
    }

    hashFunStatus = NIL;
    findStatus = NIL;
    seekSlotStatus = NIL;
    putStatus = NIL;
  }

  // запросы
  // предусловие: искомое значение не null
  // постусловие: возвращает значение, не превышающее размер массива минус 1
  public int hashFun(String value) {
    if (value == null) {
      hashFunStatus = ERR_NULL;
      return -1;
    }
    hashFunStatus = OK;
    return Math.abs(value.hashCode()) % size;
  }

  // предусловие: искомое значение не null
  // постусловие: возвращает индекс слота для вставки значения или -1. если массив заполнен
  public int seekSlot(String value) {
    int index = find(value);

    if (index > -1) {
      seekSlotStatus = OK;
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
        seekSlotStatus = OK;
        return index;
      } else {
        seekSlotStatus = ERR_FULL;
        return -1;
      }
    } catch (IndexOutOfBoundsException e) {
      seekSlotStatus = ERR_NULL;
      return -1;
    }
  }

  // предусловие: искомое значение не null
  // постусловие: возвращает индекс найденного значения или -1
  public int find(String value) {
    int index = hashFun(value);

    try {
      if (slots[index] == null) {
        findStatus = OK;
        return -1;
      }

      if (slots[index].equals(value)) {
        findStatus = OK;
        return index;
      }

      for (int i = 0; i < size; i++) {
        if (i == index) {
          continue;
        }

        if (value.equals(slots[i])) {
          findStatus = OK;
          return i;
        }
      }
      findStatus = OK;
      return -1;
    } catch (IndexOutOfBoundsException e) {
      findStatus = ERR_NULL;
      return -1;
    }
  }

  // команды
  // предусловие: искомое значение не null
  // постусловие: если массив не заполнен, помещает значение в свободную ячейку и возвращает её индекс
  public int put(String value) {
    if (value == null) {
      putStatus = ERR_NULL;
      return -1;
    }

    int index = seekSlot(value);

    if (index > -1) {
      slots[index] = value;
      putStatus = OK;
      return index;
    }

    putStatus = ERR_FULL;
    return index;
  }

  // постусловие: удаляет все значения из массива
  public void clear() {
    for (int i = 0; i < size; i++) {
      slots[i] = null;
    }
  }

  // запросы статусов
  // успешно, значение null
  public int getHashFunStatus() {
    return hashFunStatus;
  }

  // успешно, значение null
  public int getFindStatus() {
    return findStatus;
  }

  // успешно, значение null, массив заполнен
  public int getSeekSlotStatus() {
    return seekSlotStatus;
  }

  // успешно, значение null, массив заполнен
  public int getPutStatus() {
    return putStatus;
  }
}
