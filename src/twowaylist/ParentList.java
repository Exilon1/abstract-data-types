package twowaylist;

public class ParentList {

  public static final int NIL = 0; // команда ещё не вызывалась
  public static final int OK = 1; // последняя команда отработала нормально
  public static final int ERR = 2; // список пуст
  public static final int RIGHT_ERR = 3; // правее нет элемента
  public static final int NEXT_FOUND = 4; // следующий найден
  public static final int NEXT_NOT_FOUND = 5; // следующий не найден

  private int is_headStatus; // статус команды is_head()
  private int is_tailStatus; // статус команды is_tail()
  private int rightStatus; // статус запроса right()
  private int getStatus; // статус команды get()
  private int put_rightStatus; // статус команды put_right()
  private int put_leftStatus; // статус команды put_left()
  private int removeStatus; // статус команды remove()
  private int replaceStatus; // статус команды replace()
  private int findStatus; // статус команды find()


  public Node head;
  public Node tail;
  protected Node cursor;

  // конструктор
  // постусловие: создан новый пустой список
  public ParentList() {
    head = null;
    tail = null;
    cursor = null;

    rightStatus = NIL;
    getStatus = NIL;
    put_rightStatus = NIL;
    put_leftStatus = NIL;
    removeStatus = NIL;
    replaceStatus = NIL;
    findStatus = NIL;
    is_headStatus = NIL;
    is_tailStatus = NIL;
  }

  // команды
  // предусловие: список не пуст;
  // постусловие: курсор установлен на первый узел в списке
  public void head() {
    if (head == null) {
      is_headStatus = ERR;
      return;
    }
    cursor = head;
    is_headStatus = OK;
  }

  // предусловие: список не пуст;
  // постусловие: курсор установлен на последний узел в списке
  public void tail() {
    if (head == null) {
      is_headStatus = ERR;
      return;
    }
    cursor = tail;
    is_tailStatus = OK;
  }

  // предусловие: правее курсора есть элемент;
  // постусловие: курсор сдвинут на один узел вправо
  public void right() {
    if (cursor.next == null) {
      rightStatus = RIGHT_ERR;
      return;
    }
    cursor = cursor.next;
    rightStatus = OK;
  }

  // предусловие: список не пуст;
  // постусловие: следом за текущим узлом добавлен новый узел с заданным значением
  public void put_right(int value) {
    if (head == null) {
      put_rightStatus = ERR;
      return;
    }
    put_rightStatus = OK;

    if (cursor == tail) {
      tail = new Node(value);
      cursor.next = tail;
      return;
    }

    Node node = new Node(value);
    node.next = cursor.next;
    cursor.next = node;
  }

  // предусловие: список не пуст;
  // постусловие: перед текущим узлом добавлен новый узел с заданным значением
  public void put_left(int value) {
    if (head == null) {
      put_leftStatus = ERR;
      return;
    }
    put_leftStatus = OK;

    Node prevNode = findPrevious();
    if (prevNode == null) {
      head = new Node(value);
      head.next = cursor;
      return;
    }

    prevNode.next = new Node(value);
    prevNode.next.next = cursor;
  }

  // предусловие: список не пуст
  // постусловие: текущий узел удалён, курсор смещён к правому соседу, если он есть,
  // в противном случае курсор смещён к левому соседу, если он есть
  public void remove() {
    if (head == null) {
      removeStatus = ERR;
      return;
    }
    removeStatus = OK;

    Node prevNode = findPrevious();
    if (prevNode != null) {
      if (cursor == tail) {
        tail = prevNode;
        cursor = tail;
        return;
      }
      prevNode.next = cursor.next;
      cursor = cursor.next;
    } else if (head == tail) {
      clear();
      cursor = null;
    } else {
      head = head.next;
      cursor = head;
    }
  }

  // постусловие: из списка удалятся все значения
  public void clear() {
    head = null;
    tail = null;
    cursor = null;
  }

  // постусловие: новый узел добавлен в хвост списка
  public void add_tail(int value) {
    Node node = new Node(value);
    if (head == null) {
      head = node;
    } else {
      tail.next = new Node(value);
    }
    tail = node;
  }

  // постусловие: в списке удалены все узлы с заданным значением
  public void removeAll(int value) {
    if (head == null) {
      return;
    }

    head();
    while (cursor != null) {
      if (cursor.value == value) {
        remove();
      }
      cursor = cursor.next;
    }
  }

  // предусловие: список не пуст;
  // постусловие: значение текущего узла заменено на новое
  public void replace(int value) {
    if (cursor != null) {
      replaceStatus = OK;
      cursor.value = value;
      return;
    }

    replaceStatus = ERR;
  }

  // предусловие: список не пустой
  // постусловие: курсор установлен на следующий узел с искомым значением, если такой узел найден
  public void find(int value) {
    if (head == null) {
      findStatus = ERR;
      return;
    }

    head();
    while (cursor != null) {
      if (cursor.value == value) {
        findStatus = NEXT_FOUND;
        return;
      }
      cursor = cursor.next;
    }
    findStatus = NEXT_NOT_FOUND;
  }



  // запросы
  // предусловие: список не пуст
  // возвращает значение курсора
  public Node get() {
    if (head == null) {
      getStatus = ERR;
    } else {
      getStatus = OK;
    }
    return cursor;
  }

  // предусловие: список не пустой
  // возвращает true, если курсор указывает на голову
  public boolean is_head() {
    if (head == null) {
      is_headStatus = ERR;
      return false;
    }
    is_headStatus = OK;
    return head == cursor;
  }

  // предусловие: список не пустой
  // возвращает true, если курсор указывает на хвост
  public boolean is_tail() {
    if (head == null) {
      is_tailStatus = ERR;
      return false;
    }
    is_tailStatus = OK;

    return head == tail;
  }

  // возвращает true, если курсор, если курсор не null
  public boolean is_value() {
    return cursor != null;
  }

  // возвращает текущее количество элементов
  public int size() {
    int n = 0;
    Node node = head;
    while (node != null) {
      n++;
      node = node.next;
    }
    return n;
  }







  // запросы статусов
  // успешно; список пуст
  public int get_head_status() {
    return is_headStatus;
  }
  // успешно; список пуст
  public int get_tail_status() {
    return is_tailStatus;
  }
  // успешно; правее нету элемента
  public int get_right_status() {
    return rightStatus;
  }
  // успешно; список пуст
  public int get_put_right_status() {
    return put_rightStatus;
  }
  // успешно; список пуст
  public int get_put_left_status() {
    return put_leftStatus;
  }
  // успешно; список пуст
  public int get_remove_status() {
    return removeStatus;
  }
  // успешно; список пуст
  public int get_replace_status() {
    return replaceStatus;
  }
  // следующий найден; следующий не найден; список пуст
  public int get_find_status() {
    return findStatus;
  }
  // успешно; список пуст
  public int get_get_status() {
    return getStatus;
  }

  protected Node findPrevious() {
    Node node = head;
    while (node != null) {
      if (node.next == cursor) {
        return node;
      }
      node = node.next;
    }
    return null;
  }
}

class LinkedList extends ParentList {}

class TwoWayList extends ParentList {

  public static final int LEFT_ERR = 6; // левее нет элемента

  private int leftStatus; // статус запроса left()

  public TwoWayList() {
    super();
    leftStatus = NIL;
  }

  // предусловие: левее курсора есть элемент;
  // постусловие: курсор сдвинут на один узел влево
  public void left() {
    if (head == null) {
      leftStatus = LEFT_ERR;
      return;
    }

    Node prevNode = findPrevious();
    if (prevNode == null) {
      leftStatus = LEFT_ERR;
      return;
    }

    cursor = prevNode;
    leftStatus = OK;
  }
}

class Node {

  public int value;
  public Node next;

  public Node(int _value) {
    value = _value;
    next = null;
  }
}
