/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 *
 * @author sn0w
 */
public class error extends JFrame implements Runnable {

    private final String label;
    public tumblrRipper r;


    public error(String label) {

        this.label = label;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(content());
        pack();
        setVisible(true);
    }

    public error(String label, tumblrRipper r) {
        this.r = r;
        this.label = label;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(content());
        pack();
        setVisible(true);
    }

    private JPanel content() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        //content.setPreferredSize(new Dimension(400, 400));
        // content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JLabel l = new JLabel(label);
        l.setForeground(consts.white);
        l.setFont(consts.small);
        content.add(l);
        but ok = new but(this,true);
        but no = new but(this,false);
        content.add(ok);
        content.add(no);
        return content;
    }

    @Override
    public void run() {

    }

    private class but extends JPanel implements MouseListener {

        private final error r;
        private final boolean b;

        public but(error r, boolean b) {
            this.b = b;
            this.r = r;
            setPreferredSize(new Dimension(100, 50));
            setBackground(consts.white);
            JLabel l;
            if (b) {
                l = new JLabel("Continue");
            } else {
                l = new JLabel("Interupt");
            }
            l.setForeground(consts.tumblrBlue);
            l.setFont(consts.small);
            add(l);
            addMouseListener(this);

        }

        @Override
        public void mouseClicked(MouseEvent me) {
            r.r.cont(b);
            r.dispose();
        }

        @Override
        public void mousePressed(MouseEvent me) {

        }

        @Override
        public void mouseReleased(MouseEvent me) {

        }

        @Override
        public void mouseEntered(MouseEvent me) {
            setBackground(consts.redLighterColor);
            revalidate();
        }

        @Override
        public void mouseExited(MouseEvent me) {
            setBackground(consts.white);
            revalidate();
        }
    }

}
