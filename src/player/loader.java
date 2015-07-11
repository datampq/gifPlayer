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
public class loader implements Runnable {

    Thread t;
    String t_name;
    private final settings s;

    private final int numToLoad;
    private int pointer;
    private volatile boolean done = false;
    private final controls co;
    private gif[] gifs;

    public loader(String name, settings s, int numToLoad, int pointer, controls c) {
        this.s = s;
        this.numToLoad = numToLoad;
        co = c;
        this.pointer = pointer;
        t_name = name;

        if (t == null) {
            t = new Thread(this, t_name);
           // System.out.println("Thread: " + t_name + " started!");
            t.start();
        }
    }

    @Override
    public void run() {

        if (!done) {
            gifs = new gif[numToLoad];
            for (int i = 0; i < gifs.length; i++) {
                
              
                //co.updateProgressTop(i, gifs.length - 1);
                gifs[i] = new gif(s.getFileAtPointer(pointer), pointer);
               // System.out.println("-----------------------------"+i+"----"+pointer);
                if (pointer < 1) {
                    pointer = s.getNumFiles() - 1;
                } else {
                    pointer--;
                }
                //System.out.println("Pointer=" + pointer);
            }
            done = true;
           // System.out.println("Finished loading chunk!");
        }
        t=null;
       // System.out.println("Finished loading chunk! Pointer end point="+pointer);
    }

    public int returnPointer() {
       // System.out.println("-----------------------------<<<End of loader");
        return pointer;
        
    }

    public boolean isDone() {
        return done;

    }

    public gif[] getGifs() {
        return gifs;
    }

}
