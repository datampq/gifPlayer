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
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author sn0w
 */
public class gifDownloader extends JFrame implements Runnable {

    private final folderSelector f;
    private JTextArea input;
    private JLabel status;
    private File path;
    private boolean inited = false;

    public gifDownloader(folderSelector f) {
        this.f = f;
        f.setVisible(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        add(content());
        pack();
        setVisible(true);
    }

    private JPanel content() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(700, 400));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(controlPanel());
        JLabel l = new JLabel("Type keywords separated by comma, no spaces!");

        l.setForeground(consts.white);
        l.setFont(consts.tiny);
        content.add(l);
        content.add(inputPanel());
        content.add(statusPanel());
        return content;
    }

    private JPanel controlPanel() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(700, 100));
        //int w,int h, String title,String action,gifDownloader d
        button b = new button(200, 50, "Select folder", "create", this);
        JLabel l1 = new JLabel("1.");
        l1.setForeground(consts.white);
        l1.setFont(consts.big);
        button b2 = new button(200, 50, "Download", "down", this);
        JLabel l2 = new JLabel("2.");
        l2.setForeground(consts.white);
        l2.setFont(consts.big);
        button b3 = new button(200, 50, "Done", "close", this);
        JLabel l3 = new JLabel("3.");
        l3.setForeground(consts.white);
        l3.setFont(consts.big);
        content.add(l1);
        content.add(b);
        content.add(l2);
        content.add(b2);
        content.add(l3);
        content.add(b3);
        return content;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File f) {
        path = f;
        inited = true;

    }

    public boolean isInited() {
        return inited;
    }

    private JPanel inputPanel() {
        JPanel content = new JPanel();
        content.setPreferredSize(new Dimension(700, 250));
        content.setBackground(consts.tumblrBlue);
        input = new JTextArea(10, 40);
        input.setWrapStyleWord(true);
        input.setLineWrap(true);
        content.add(input);
        return content;
    }

    public String[] getInput() {
        String text = input.getText();
        String[] split = text.split(",");
        return split;
    }

    private JPanel statusPanel() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(700, 50));
        status = new JLabel("GIF downloader powered by Tumblr");
        status.setForeground(consts.white);
        status.setFont(consts.normal);
        content.add(status);
        return content;
    }

    public void updateStatus(String s) {
        status.setText(s);
        revalidate();
    }

    @Override
    public void run() {

    }

    public folderSelector getMainWindow() {
        return f;
    }

    private static class button extends JPanel implements MouseListener {

        private JFileChooser chooser;
        private final JLabel label;
        private final String action;
        private final gifDownloader d;

        public button(int w, int h, String title, String action, gifDownloader d) {
            this.setBackground(consts.white);
            this.setPreferredSize(new Dimension(w, h));
            this.d = d;
            this.action = action;
            label = new JLabel(title);
            label.setForeground(consts.tumblrBlue);
            label.setFont(consts.normal);
            add(label);
            addMouseListener(this);

        }

        @Override
        public void mouseClicked(MouseEvent me) {
            if (action.equals("create")) {

                chooser = new JFileChooser();
        //title

                //only dirs
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                //disable all files
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                    File currentDirectory = chooser.getSelectedFile();
                    d.updateStatus("Folder inited!Add keywords and Download.");
                    d.setPath(currentDirectory);

                } else {
                    //no file selected

                }
            }
            if (action.equals("down")) {
                if (d.inited) {
                    if (d.getInput().length > 0) {
                        downloader down = new downloader(d);
                        d.setEnabled(false);
                    } else {
                        d.updateStatus("ERROR: Please insert keywords!");
                    }
                } else {
                    d.updateStatus("ERROR: Please select a folder!");
                }
            }
            if (action.equals("close")) {
                if (d.inited) {
                    d.getMainWindow().setPath(d.path);
                }
                d.getMainWindow().setVisible(true);
                d.dispose();
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
            label.setForeground(consts.white);
            setBackground(consts.tumblrBlue);
            revalidate();
        }

        @Override
        public void mouseExited(MouseEvent me) {
            label.setForeground(consts.tumblrBlue);
            setBackground(consts.white);
            revalidate();
        }
    }

}
