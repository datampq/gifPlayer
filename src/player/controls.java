/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author sn0w
 */
public class controls extends JFrame implements Runnable {

    Thread t;
    String t_name;

    private JLabel status;
    private Graphics overall;
    private JLabel oProgress;
    private JTextField input;
    private final folderSelector f;
    private final settings sets;

    public controls(folderSelector f, settings s) {
        sets = s;
        this.f = f;
        t_name = "controlsThread";
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() / 2;
        add(content((int) width));
        pack();
        setVisible(true);
        if (t == null) {
            t = new Thread(this, t_name);
            t.start();

        }

    }

    @Override
    public void run() {

    }

    private JPanel content(int w) {
        JPanel content = new JPanel();
        content.setBackground(consts.whiteColor);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.setPreferredSize(new Dimension(w, 100));
        content.add(status(w));
        content.add(progress(w));
        content.add(controlPanel(w));
        //content.add(playlist());
        return content;

    }

    private JPanel progress(int w) {
        JPanel content = new JPanel();
        content.setBackground(consts.whiteColor);
        content.setPreferredSize(new Dimension(w, 30));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JPanel to = new JPanel();
        to.setBackground(consts.whiteColor);
        final BufferedImage image = new BufferedImage(w, 30, BufferedImage.TYPE_INT_RGB);
        oProgress = new JLabel(new ImageIcon(image));
        to.add(oProgress);
        overall = image.createGraphics();
        overall.setColor(consts.redLighterColor);
        overall.fillRect(0, 0, w, 20);
        overall.setColor(consts.whiteColor);
        overall.fillRect(0, 20, w, 10);
        content.add(to);

        return content;
    }

    private JPanel playlist() {
        JPanel content = new JPanel(new GridBagLayout());
        for (int i = 0; i < sets.getNumFiles(); i++) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            gbc.weighty = 1;
            JPanel filePanel = new JPanel();
            filePanel.setBackground(consts.white);
            File fileAtPointer = sets.getFileAtPointer(i);
            JLabel l = new JLabel(i + ". " + fileAtPointer.getName());
            l.setForeground(consts.redMuchDarkerColor);
            l.setFont(consts.tiny);
            filePanel.add(l);
            content.add(filePanel, gbc);

        }

        return content;
    }

    private JPanel status(int w) {
        JPanel content = new JPanel();
        content.setBackground(consts.whiteColor);
        content.setPreferredSize(new Dimension(w, 20));
        status = new JLabel("idle");
        status.setForeground(consts.redDarkerColor);
        status.setFont(consts.tiny);
        content.add(status);

        return content;
    }

    private JPanel controlPanel(int w) {
        JPanel content = new JPanel();
        content.setBackground(consts.whiteColor);
        content.setPreferredSize(new Dimension(w, 40));
        btn b = new btn("<<", "slower", 70, 40);
        btn b2 = new btn(">>", "faster", 70, 40);
        btn b5 = new btn(">|", "next", 70, 40);
        btn b3 = new btn("Repeat", "repeat", 120, 40);
        btn b4 = new btn("Shuffle", "shuffle", 120, 40);
        btn b0 = new btn("Menu", "menu", 120, 40);
        //input = new JTextField(5);
        // content.add(input);
        //  btn b1 = new btn("Go", "go", 120, 40);
        //content.add(b1);
        content.add(b);
        content.add(b2);
        content.add(b5);
        content.add(b3);
        content.add(b4);
        content.add(b0);
        return content;

    }

    public void updateStatus(String s) {
        status.setText(s);
        revalidate();
    }

    public void updateProgress(int i, int max) {
        int factor = ((i * this.getWidth()) / max);
        if (factor == 0) {
            overall.setColor(consts.redLighterColor);
            overall.fillRect(0, 0, this.getWidth(), 10);
        } else {
            overall.setColor(consts.redDarkerColor);
            overall.fillRect(0, 0, factor, 10);
        }
        oProgress.repaint();
        revalidate();
    }

    public void updateProgressTop(int i, int max) {
        int factor = ((i * this.getWidth()) / max);
        if (factor == 0) {
            overall.setColor(consts.redLighterColor);
            overall.fillRect(0, 10, this.getWidth(), 10);

        } else {
            overall.setColor(consts.redDarkerColor);
            overall.fillRect(0, 10, factor, 10);
        }
        oProgress.repaint();
        revalidate();
    }

    public void updatePointer(int i, int max) {
        int factor = ((i * this.getWidth()) / max);
        if (factor == 0) {
            overall.setColor(consts.whiteColor);
            overall.fillRect(0, 20, this.getWidth(), 10);
        } else {
            overall.setColor(consts.redColor);
            overall.fillRect(0, 20, factor, 10);
        }
        oProgress.repaint();
        revalidate();
    }

    public int getValue() {
        return Integer.parseInt(input.getText());
    }

    private class btn extends JPanel implements MouseListener {

        private final String action;
        private boolean repeat = false;
        private boolean sh = false;
        private imagePlayer player;
        private controls c;

        public btn(String label, String action, int w, int h) {
            this.action = action;
            setBackground(consts.redDarkerColor);
            JLabel l = new JLabel(label);
            l.setForeground(consts.whiteColor);
            l.setFont(consts.small);
            add(l);
            addMouseListener(this);
        }

        public btn(String label, String action, int w, int h, imagePlayer player, controls c) {
            this.action = action;
            setBackground(consts.redDarkerColor);
            JLabel l = new JLabel(label);
            l.setForeground(consts.whiteColor);
            l.setFont(consts.small);
            add(l);
            this.player = player;
            this.c = c;
            addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            if (action.equals("faster")) {
                consts.fps++;
            }
            if (action.equals("slower")) {
                consts.fps--;
            }
            if (action.equals("go")) {
                player.setPointer(c.getValue());
            }
            if (action.equals("next")) {
                consts.skip = true;
            }
            if (action.equals("repeat")) {
                if (repeat) {

                    consts.repeat = false;
                    repeat = false;
                } else {
                    consts.repeat = true;
                    repeat = true;
                }

            }
            if (action.equals("menu")) {

            }
            if (action.equals("shuffle")) {
                if (sh) {

                    consts.shuffle = false;
                    sh = false;
                } else {
                    consts.shuffle = true;
                    sh = true;
                }

            }
        }

        @Override
        public void mousePressed(MouseEvent me) {

        }

        @Override
        public void mouseReleased(MouseEvent me) {

        }

        @Override
        public void mouseEntered(MouseEvent me) {
            if (sh || repeat) {
                setBackground(consts.redLighterColor);
                revalidate();
            } else {
                setBackground(consts.redColor);
                revalidate();
            }
        }

        @Override
        public void mouseExited(MouseEvent me) {
            if (sh || repeat) {
                setBackground(consts.redLighterColor);
                revalidate();
            } else {
                setBackground(consts.redDarkerColor);
                revalidate();
            }
        }

    }

}
