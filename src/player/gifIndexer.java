/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

/**
 *
 * @author sn0w
 */
public class gifIndexer {

    private final gifUrl head;

    private final gifUrl tail;

    public gifIndexer() {
        head = new gifUrl("head");
        tail = new gifUrl("end");
        head.setNext(tail);
        tail.setNext(tail);
    }

    public void add(String url) {
        gifUrl n = new gifUrl(url);
        n.setNext(head.getNext());
        head.setNext(n);
    }

    public boolean exists(String url) {
        gifUrl tempPointer = head;
        boolean exists = false;
        while (tempPointer.hasNext()) {
            if (tempPointer.getData().equals(url)) {
                exists = true;
                break;
            }
            tempPointer = tempPointer.getNext();
        }
        return exists;
    }

    private class gifUrl {

        private final String data;
        private gifUrl next;

        public gifUrl(String url) {
            data = url;

        }

        public gifUrl getNext() {
            return next;
        }

        public String getData() {
            return data;
        }

        public void setNext(gifUrl g) {
            next = g;
        }

        public boolean hasNext() {
            if (next.getData().equals("end")) {
                return false;
            } else {
                return true;
            }
        }
    }

}
