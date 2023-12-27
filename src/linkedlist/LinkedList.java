package linkedlist;

import java.util.*;

public class LinkedList {

  public static final int NIL = 0; // команда ещё не вызывалась
  public static final int OK = 1; // последняя команда отработала нормально
  public static final int ERR = 2; // лист пуст
  public static final int RIGHT_ERR = 3; // лист пуст


  private int rightStatus; // статус запроса right()
  private int getStatus; // статус команды get()
  private int put_rightStatus; // статус команды put_right()
  private int put_leftStatus; // статус команды put_left()
  private int removeStatus; // статус команды remove()
  private int add_tailStatus; // статус команды add_tail()
  private int replaceStatus; // статус команды replace()
  private int findStatus; // статус команды find()
  private int remove_allStatus; // статус команды remove_all()
  private int is_headStatus; // статус команды is_head()
  private int is_tailStatus; // статус команды is_tail()

  public Node head;
  public Node tail;

  private Node cursor;

  // конструктор
  // постусловие: создан новый пустой список
  public LinkedList() {
    head = null;
    tail = null;
    cursor = null;

    rightStatus = NIL;
    getStatus = NIL;
    put_rightStatus = NIL;
    put_leftStatus = NIL;
    removeStatus = NIL;
    add_tailStatus = NIL;
    replaceStatus = NIL;
    findStatus = NIL;
    remove_allStatus = NIL;
    is_headStatus = NIL;
    is_tailStatus = NIL;
  }

  // постусловие: курсор имеет значение головы
  public void head() {
    cursor = head;
  }

  // постусловие: курсор имеет значение хвоста
  public void tail() {
    cursor = tail;
  }

  // предусловие: следующий элемент не null
  // постусловие: курсор содержит следующий элемент
  public void right() {
    if (cursor.next == null) {
      rightStatus = RIGHT_ERR;
      return;
    }
    cursor = cursor.next;
    rightStatus = OK;
  }

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

  // предусловие: список не пуст
  public void put_right(int value) {
    if (head == null) {
      put_rightStatus = ERR;
      return;
    }
    put_rightStatus = OK;

    if (cursor.next == null) {
      tail = new Node(value);
      cursor.next = tail;
      return;
    }

    Node node = new Node(value);
    node.next = cursor.next;
    cursor.next = node;
  }

  // предусловие: список не пуст
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
  // постусловие: список стал меньше на 1 элемент
  public boolean remove() {
    if (head == null) {
      removeStatus = ERR;
      return false;
    }
    removeStatus = OK;

    Node prevNode = findPrevious();
    if (prevNode != null) {
      prevNode.next = cursor.next;
      if (prevNode.next == null) {
        tail = prevNode;
        cursor = tail;
        return true;
      }
      cursor = cursor.next;
      return true;

    } else if (head == tail) {
      clear();
      cursor = null;
    } else if (head == cursor) {
      head = head.next;
      if (head == null) {
        tail = null;
      }
      cursor = head;
      return true;
    }
    return false;
  }

  // постусловие: из списка удалятся все значения
  public void clear() {
    head = null;
    tail = null;
    cursor = null;

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

  // постусловие: список стал больше на 1 элемент
  public void add_tail(int value) {
    Node node = new Node(value);
    if (head == null) {
      head = node;
    } else {
      tail.next = new Node(value);
    }
    tail = node;

    add_tailStatus = OK;
  }

  // предусловие: список не пуст
  // постусловие: список поменял значение в ноде курсор
  public void replace(int value) {
    if (cursor != null) {
      replaceStatus = OK;
      cursor.value = value;
      return;
    }

    replaceStatus = ERR;
  }

  // предусловие: список не пустой
  // возвращает верхний элемент списка, если найден, и null в противном случае
  public Node find(int value) {
    if (head == null) {
      findStatus = ERR;
      return null;
    }

    findStatus = OK;

    Node node = head;
    while (node != null) {
      if (node.value == value) {
        return node;
      }
      node = node.next;
    }
    return null;
  }

  // предусловие: список не пустой
  // постусловие: список стал меньше на количество элементов, содержащих value. курсор мог поменяться
  public void removeAll(int _value) {
    if (head == null) {
      remove_allStatus = ERR;
      return;
    }

    remove_allStatus = OK;

    Node node = head;
    Node prevNode = null;
    while (node != null) {
      if (node.value == _value) {
        if (prevNode != null) {
          prevNode.next = node.next;
          if (prevNode.next == null) {
            tail = prevNode;
          }
        } else {
          head = node.next;
          if (head == null) {
            tail = null;
          }
        }

        if (node == cursor && node.next != null) {
          cursor = node.next;
        } else if (node == cursor) {
          cursor = prevNode;
        }

        node = node.next;
        continue;
      }

      prevNode = node;
      node = node.next;
    }
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



  private Node findPrevious() {
    if (head == cursor) {
      return null;
    }

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

class Node {

  public int value;
  public Node next;

  public Node(int _value) {
    value = _value;
    next = null;
  }


}
