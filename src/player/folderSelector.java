/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.Dimension;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.File;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author sn0w
 */
public class folderSelector extends JFrame implements Runnable {

    private settings s;
    public boolean inited = false;
    private JFileChooser chooser;
    private final String title = "Select a folder full of gifs :)";
    private final int panelW = 700;
    private final int panelH = 50;
    private JLabel status;

    public folderSelector() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(content());
        pack();
        setVisible(true);
    }

    private JPanel content() {
        JPanel content = new JPanel();
        content.setBackground(consts.whiteColor);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(buttonPanel());
        content.add(statusPanel());
        return content;
    }

    private JPanel buttonPanel() {
        JPanel content = new JPanel();
        content.setBackground(consts.whiteColor);
        content.setPreferredSize(new Dimension(panelW, panelH));
        button b = new button(this, "Open folder", "open", 200, 40);
        button b1 = new button(this, "Get GIFs", "get", 200, 40);
        button b2 = new button(this, "Play", "play", 200, 40);
        content.add(b1);
        content.add(b);

        content.add(b2);
        return content;

    }

    private JPanel statusPanel() {
        JPanel content = new JPanel();
        content.setBackground(consts.whiteColor);
        content.setPreferredSize(new Dimension(panelW, panelH));

        JPanel pane = new JPanel();
        pane.setPreferredSize(new Dimension(panelW / 2, panelH));
        pane.setBackground(consts.redMuchDarkerColor);
        JLabel dro = new JLabel("Drag Files and Drop here ;)");
        dro.setForeground(consts.white);
        dro.setFont(consts.small);
        pane.add(dro);
        // Add a drop target to the JPanel
        TLTHandler target = new TLTHandler(pane, this);

        content.add(pane);
        status = new JLabel(consts.version);
        status.setForeground(consts.redDarkerColor);
        status.setFont(consts.normal);
        content.add(status);
        return content;

    }

    public void selectPath() {
        chooser = new JFileChooser();
        //title
        chooser.setDialogTitle(title);
        //only dirs
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //disable all files
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            File currentDirectory = chooser.getSelectedFile();
            init(currentDirectory);
            redrawUI();
        } else {
            //no file selected

        }
    }

    public void setPath(File file) {
        init(file);
    }

    private void init(File file) {
        s = new settings(file.getAbsolutePath());
        inited = true;
        redrawUI();
    }

    public void initDrop(List<File> f) {
        // System.out.println("called");
        s = new settings(f);
        inited = true;
        redrawUI();
        setLoading();
    }

    public void setShuffle() {

        if (consts.shuffle) {
            consts.shuffle = false;
        } else {
            consts.shuffle = true;
        }
        redrawUI();

    }

    public void redrawUI() {
        //status.setText("Ready: " + s.getPath() + " Files: " + s.getNumFiles() + " Shuffle:" + consts.shuffle);
        revalidate();
    }

    public void setLoading() {
        status.setText("Loading...");

        revalidate();
        play();
    }

    public void play() {

        imagePlayer player = new imagePlayer(s, this);

    }

    public void showNoFileError() {
        status.setText("No folder Selected!");
        revalidate();
    }

    @Override
    public void run() {

    }

    private class button extends JPanel implements MouseListener {

        private folderSelector f;
        private final String action;

        public button(folderSelector f, String label, String action, int w, int h) {
            this.f = f;
            this.action = action;
            this.setPreferredSize(new Dimension(w, h));
            setBackground(consts.redColor);
            JLabel l = new JLabel(label);
            l.setFont(consts.normal);
            l.setForeground(consts.whiteColor);
            add(l);
            addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            if (action.equals("open")) {
                f.selectPath();
            }
            if (action.equals("get")) {
                gifDownloader downloader = new gifDownloader(f);
                tumblrRipper ripper = new tumblrRipper(f);
            }
            if (action.equals("play")) {
                if (f.inited) {
                    f.setLoading();

                } else {
                    f.showNoFileError();
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent me) {
            setBackground(consts.redMuchDarkerColor);
            revalidate();
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            setBackground(consts.redColor);
            revalidate();
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            setBackground(consts.redLighterColor);
            revalidate();
        }

        @Override
        public void mouseExited(MouseEvent me) {
            setBackground(consts.redColor);
            revalidate();
        }

    }

}
