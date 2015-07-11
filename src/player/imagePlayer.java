/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author sn0w
 */
public class imagePlayer extends JFrame implements Runnable {

    private Thread t;
    private final String name = "gifThread";
    private final settings sets;
    private gif[] gifs;
    private JLabel display;
    private int pointer = 0;
    private final int max;
    private volatile boolean isRunning = true;
    private boolean threadDone = false;
    private int threadNum = 0;
    private final controls c;
    private final folderSelector select;
    private Graphics g;
    private boolean customPointer = false;
    private final playlistEditor editor;

    public imagePlayer(settings s, folderSelector select) {

        gifs = new gif[consts.buffer];
        sets = s;
        this.select = select;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pointer = s.getNumFiles() - 1;
        sGif();
        add(content());
        pack();
        setVisible(true);
        max = 1;
        c = new controls(select, s);
        editor = new playlistEditor(this);
        if (t == null) {
            t = new Thread(this, name);
            t.start();

        }

    }

    private JPanel content() {
        JPanel content = new JPanel();
        content.setBackground(consts.black);
        content.setPreferredSize(new Dimension(720, 480));
        display = new JLabel(new ImageIcon(gifs[0].getFrameAtPointer(0)));

        content.add(display);
        return content;
    }

    private int getScreenW() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return (int) screenSize.getWidth();

    }

    public void setPointer(int i) {
        pointer = i;
        customPointer = true;
    }

    private int getScreenH() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return (int) screenSize.getHeight();
    }

    @Override
    public void run() {
//System.out.println("Entered loop:"+gifs.length);

        loader l;
        int shufflePointer = 1 + (int) (Math.random() * sets.getNumFiles() - consts.buffer - 1);
        if (consts.shuffle) {
            l = new loader("t_" + threadNum, sets, consts.buffer, shufflePointer, c);
        } else {
            l = new loader("t_" + threadNum, sets, consts.buffer, pointer, c);

        }
        while (isRunning) {
            if (threadDone) {
                threadNum++;
                if (consts.shuffle) {
                    shufflePointer = 1 + (int) (Math.random() * sets.getNumFiles() - consts.buffer - 1);
                    l = new loader("t_" + threadNum, sets, consts.buffer, shufflePointer, c);
                } else {

                    l = new loader("t_" + threadNum, sets, consts.buffer, pointer, c);
                }
                threadDone = false;
            }
            mainLoop:
            for (int i = 0; i < gifs.length; i++) {
                //System.out.println(gifs[i].getFrameCount());
                editor.panels[gifs[i].getPointer()].setBorder(BorderFactory.createLineBorder(consts.redDarkerColor));;
                for (int j = 0; j < gifs[i].getFrameCount(); j++) {
                    if (customPointer) {
                        editor.clearPanel(gifs[i].getPointer());
                     
                        break mainLoop;

                    }
                    if (consts.skip) {
                        if (i < gifs.length - 1) {
                            j = gifs[i].getFrameCount() - 1;
                            consts.skip = false;
                        } else {
                            break;
                        }
                    }
                    try {
                        ImageIcon icon = new ImageIcon(gifs[i].getFrameAtPointer(j).getScaledInstance(this.getWidth(), this.getHeight(), BufferedImage.SCALE_SMOOTH));
                        c.updateStatus("[file:" + gifs[i].getPointer() + "/" + sets.getNumFiles() + "]" + gifs[i].getSource().getName() + "@" + consts.fps + "fps");

                        c.updateProgress(j, gifs[i].getFrameCount());
                        display.setIcon(icon);
                        display.repaint();
                        //notify display display.notify();

                    } catch (NullPointerException e) {

                    }
                    //  System.out.println("working..."+j);
                    // revalidate();
                    try {
                        Thread.sleep(1000 / consts.fps);
                    } catch (InterruptedException ex) {

                    }
                }
                editor.panels[gifs[i].getPointer()].setBorder(BorderFactory.createEmptyBorder());
                if (consts.repeat) {
                    i--;
                }
            }

            if (l.isDone()) {
                if (customPointer) {
                    customPointer = false;
                } else {
                    pointer = l.returnPointer();
                }
                // System.out.println("Updating pointer at main Thread:" + pointer);
                c.updatePointer(pointer, sets.getNumFiles());
                threadDone = true;
                gifs = l.getGifs();

            }

        }
    }

    private void sGif() {
        //deal with shuffle
        pointer = sets.getNumFiles() - 1;
        System.out.println(pointer);
        for (int i = 0; i < consts.buffer; i++) {
            gifs[i] = new gif(sets.getFileAtPointer(pointer), pointer);
            if (pointer < 1) {
                pointer = sets.getNumFiles() - 1;
            } else {
                pointer--;
            }
            //System.out.println("Pointer=" + pointer);
        }
        select.redrawUI();

    }

    public void setGifs(gif[] gifs) {
        this.gifs = gifs;
    }

    public settings getSettings() {
        return sets;
    }

}
