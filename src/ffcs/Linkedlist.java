package ffcs;

/**
 * Linked List Program stores the students under each teacher. It performs a
 * insertion from the end. and deletion form anywhere and displays the linked
 * list. Programmed By Sanat Rohatgi
 */
class node {

    String data;
    node next;

    node(String d) {
        data = d;
        next = null;
    }
}

public class Linkedlist {

    void insertlast(node root, String roll) throws Exception {
        node first = root;
        String s;
        int i = 0;
        node second = null;
        node temp = new node(roll);

        while (first != null) {
            second = first;
            first = first.next;
            i++;
            if (i > 6) {
                System.out.println("slots are full");
                return;
            }
        }
        second.next = temp;

    }

    static String display(node root) {
        String t = "";
        node temp3 = root;

        if (temp3.next != null) {
            while (temp3.next != null) {
                t += "<td>" + temp3.data + "</td>";
                temp3 = temp3.next;
            }
            t += "<td>" + temp3.data + "</td>";
            return (t);
        } else {
            t = "<td>" + temp3.data + "</td>";
        }

        return (t);
    }

    void delany(node root, String roll) throws Exception {
        int c = 0;
        node first = root;
        node second = root;
        while (first.next != null) {

            if (first.data.equals(roll)) {
                second.next = first.next;
                c = 1;
                break;
            }
            second = first;
            first = first.next;
        }
        if (first.data.equals(roll) && c == 0) {
            second.next = null;
        }

    }
}
