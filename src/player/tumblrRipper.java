/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author sn0w
 */
public class tumblrRipper extends JFrame implements Runnable {

    private JTextField input;
    private JLabel display;
    private Graphics g;
    private JLabel status;
    private boolean gifs = true;
    private boolean jpegs = true;
    private boolean pngs = true;
    private final JFrame container;
    private int numFiles = 0;
    private String query;
    private int currentPage = 1;
    private JTextField pages;
    private String jobFolder;
    private final gifIndexer index;
    private JTextField startAt;
    private JLabel status2;
    public boolean canRun = true;
    private JLabel timer;
    private long started;
    private long elapsed;
    private long remaining;
    private long[] average;
    Thread t;
    String t_name;
    private JFileChooser chooser;
    boolean suspended = false;

    public tumblrRipper(JFrame container) {
        this.container = container;
        average = new long[3];
        // container.setVisible(false);
        index = new gifIndexer();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        add(content());
        pack();

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        final int WIDTH = screenSize.width;

        this.setLocation(WIDTH / 2, WIDTH / 2);
        setVisible(true);
    }

    private JPanel content() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(600, 210));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(buttonPanel());
        content.add(inputPanel());
        content.add(statusPanel());
        content.add(progressPanel());
        content.add(timePanel());
        return content;

    }

    private JPanel buttonPanel() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(600, 100));
        // public button(String a, String l, int w, int h, tumblrRipper ripper, boolean s)
        button b = new button("gif", "GIFs", 100, 40, this, true, true);
        button b2 = new button("jpeg", "JPEGs", 100, 40, this, true, true);
        button b3 = new button("png", "PNGs", 100, 40, this, true, true);
        button b4 = new button("start", "Start", 100, 40, this, false, false);
        button b5 = new button("close", "Close", 100, 40, this, false, false);
        content.add(b);
        content.add(b2);
        content.add(b3);
        content.add(b4);
        content.add(b5);
        return content;
    }

    private JPanel inputPanel() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(600, 50));
        JLabel l = new JLabel("URL:");
        JLabel l2 = new JLabel("End at:");
        JLabel l3 = new JLabel("Start at:");
        l3.setForeground(consts.white);
        l3.setFont(consts.small);
        startAt = new JTextField(4);
        l.setForeground(consts.white);
        l2.setForeground(consts.white);
        l.setFont(consts.small);
        l2.setFont(consts.small);
        input = new JTextField(20);
        pages = new JTextField(4);
        startAt.setText("1");
        pages.setText("31");
        input.setForeground(consts.tumblrBlue);
        input.setText("e.g http://www.freempq.tumblr.com");
        content.add(l);
        content.add(input);
        content.add(l3);
        content.add(startAt);
        content.add(l2);
        content.add(pages);
        return content;
    }

    private JPanel statusPanel() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(600, 40));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(upStatus());
        content.add(downStatus());
        return content;
    }

    private JPanel upStatus() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(600, 20));
        status = new JLabel("");
        status.setForeground(consts.white);
        status.setFont(consts.tiny);
        content.add(status);
        return content;
    }

    private JPanel downStatus() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(600, 20));
        status2 = new JLabel("");
        status2.setForeground(consts.white);
        status2.setFont(consts.tiny);
        content.add(status2);
        return content;
    }

    private JPanel progressPanel() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(600, 50));
        final BufferedImage image = new BufferedImage(600, 50, BufferedImage.TYPE_INT_RGB);
        display = new JLabel(new ImageIcon(image));
        content.add(display);
        g = image.createGraphics();
        g.setColor(consts.tumblrBlue);
        g.fillRect(0, 0, 600, 50);
        return content;
    }

    private JPanel timePanel() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(600, 20));
        timer = new JLabel("");
        timer.setForeground(consts.white);
        timer.setFont(consts.tiny);
        content.add(timer);
        return content;
    }

    void suspend() {
        suspended = true;
    }

    synchronized void resume() {
        suspended = false;
        notify();
    }

    public void cont(boolean c) {
        if (c) {
            resume();
        } else {
            t = null;
            canRun = false;
        }
    }

    @Override
    public void run() {

        timer.setText("Elapsed: " + getTime(elapsed) + "/ Left: Unknown");
        int i = Integer.parseInt(pages.getText());
        int counter = 0;
        int initCounter = 0;
        currentPage = Integer.parseInt(startAt.getText());
        String toDown = "";
        while (currentPage < i && canRun) {
            while (suspended) {
                try {
                    wait();
                } catch (InterruptedException ex) {

                }
            }
            long st = System.nanoTime();
            status2.setText("Loading...");
            // System.out.println(query + currentPage + "/");
            updateOverallProgress(currentPage, i);

            try {
                URL url = new URL(query + currentPage + "/");

                URLConnection connection = url.openConnection();
                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                status2.setText("[Page: " + currentPage + "; Files downloaded: " + numFiles + "]Parsing data...");
                while ((line = reader.readLine()) != null) {

                    builder.append(line);
                }
                String toString = builder.toString();
                System.out.println(toString);
                for (int z = 0; z < toString.length() - 7; z++) {

                    updateProgress(z, toString.length() - 7);
                    char a = toString.charAt(z);
                    char w = toString.charAt(z + 1);
                    char e = toString.charAt(z + 2);
                    char q1 = toString.charAt(z + 3);
                    char w1 = toString.charAt(z + 4);
                    char e1 = toString.charAt(z + 5);
                    char w2 = toString.charAt(z + 6);
                    char e2 = toString.charAt(z + 7);
                    //high_res
                    if (a == 'm' && w == 'e' && e == 'd' && q1 == 'i' && w1 == 'a' && e1 == '.' && w2 == 't' && e2 == 'u') {

                        toDown = "";
                        int pointer = z - 10;
                        char r;
                        while ((r = toString.charAt(pointer)) != '"') {
                            toDown = toDown + r;
                            pointer++;
                        }
                        //implement .png, .jpg, .jpeg, .gif image recognition alorithm
                        String to = "\\\\";
                        System.out.println(toDown);
                        toDown = toDown.replaceAll(to, "");
                        String[] split = toDown.split("/");
                        // System.out.println(split[split.length - 1]);
                        int ran = (int) (Math.random() * 999999);
                        if (toDown.length() > 4) {
                            if (index.exists(split[split.length - 1])) {

                            } else {
                                status2.setText("[Page: " + currentPage + "; Files downloaded: " + numFiles + "]Downloading...");
                                status.setText("Getting: " + split[split.length - 1]);
                                if (toDown.charAt(toDown.length() - 3) == 'g' && toDown.charAt(toDown.length() - 2) == 'i' && toDown.charAt(toDown.length() - 1) == 'f') {
                                    //System.out.println("Is a GIF!!!!");

                                    //writeGIF(String folder, String filename, String u)
                                    writeImage(jobFolder + "\\\\gif", "mpq_result_" + ran + "_" + z, toDown, "gif");

                                    revalidate();
                                    index.add(split[split.length - 1]);

                                } else if (toDown.charAt(toDown.length() - 3) == 'p' && toDown.charAt(toDown.length() - 2) == 'n' && toDown.charAt(toDown.length() - 1) == 'g') {

                                    //private void writeImage(String folder, String filename, String url, String ext)
                                    writeImage(jobFolder + "\\\\png", "mpq_result_" + ran + "_" + z, toDown, "png");

                                    revalidate();
                                    index.add(split[split.length - 1]);
                                } else if (toDown.charAt(toDown.length() - 3) == 'j' && toDown.charAt(toDown.length() - 2) == 'p' && toDown.charAt(toDown.length() - 1) == 'g') {

                                    //private void writeImage(String folder, String filename, String url, String ext)
                                    writeImage(jobFolder + "\\\\jpeg", "mpq_result_" + ran + "_" + z, toDown, "jpg");

                                    revalidate();
                                    index.add(split[split.length - 1]);
                                }

                            }
                        }
                        status.setText(split[split.length - 1] + " Saved.");
                        status2.setText("[Page: " + currentPage + "; Files downloaded: " + numFiles + "]Parsing data...");
                        // System.out.println(toDown);
                        z = pointer;
                    }

                }

            } catch (MalformedURLException ex) {
                System.out.println("MalformedURLException ex");
                error r = new error("[MalformedURLException ex] Please verify the URL you have entered.\n Input: " + query  + "\nPage: "+ currentPage+"\nFile: "+toDown);
                //suspend();
            } catch (IOException ex) {
                System.out.println("IOException ex");
                error r = new error("[IOException ex] I/O Error.\n File: " + toDown+"\nPage: "+ currentPage+"\nInput: "+toDown);
                //suspend();
            }
            updateOverallProgress(currentPage, i);
            currentPage++;
            long endTime = System.nanoTime();
            long duration = (endTime - st);

            if (counter == 0) {
                average[0] = duration;
                if (initCounter < 3) {
                    timer.setText("Tile Left: Unknown");
                    initCounter++;
                } else {
                    timer.setText("Time Left:" + getTime(getAvarage() * (i - currentPage)));
                }
                counter++;
            } else if (counter == 2) {
                average[2] = duration;
                if (initCounter < 3) {
                    timer.setText("Time Left: Unknown");
                    initCounter++;
                } else {
                    timer.setText("Time Left:" + getTime(getAvarage() * (i - currentPage)));
                }
                counter = 0;
            } else {
                average[1] = duration;
                if (initCounter < 3) {
                    timer.setText("Time Left: Unknown");
                    initCounter++;
                } else {
                    timer.setText("Time Left:" + getTime(getAvarage() * (i - currentPage)));
                }
                counter++;

            }

        }
        status.setText("Finished.");
        status2.setText("[Page: " + currentPage + "; Files downloaded: " + numFiles + "]Idle");
        t = null;

    }

    private long getAvarage() {
        return (long) ((average[0] + average[1] + average[2]) / 3);
    }

    public boolean gif() {
        return gifs;

    }

    public boolean jpeg() {
        return jpegs;
    }

    public boolean png() {
        return pngs;
    }

    public void setGifs(boolean state) {
        gifs = state;
    }

    public void setPngs(boolean state) {
        pngs = state;
    }

    public void setJpegs(boolean state) {
        jpegs = state;
    }

    public void init() {
        query = input.getText() + "/page/";
        chooser = new JFileChooser();
        //title

        //only dirs
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //disable all files
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            File curr = chooser.getSelectedFile();
            jobFolder = curr.getAbsolutePath();

        } else {
            //abort

        }
        if (gif()) {
            makeFolder(jobFolder + "\\\\gif");
        }
        if (png()) {
            makeFolder(jobFolder + "\\\\png");
        }
        if (jpeg()) {
            makeFolder(jobFolder + "\\\\jpeg");
        }
        canRun=true;
        if(suspended){
            suspended=false;
        }
        if (t == null) {
            t = new Thread(this, "ripper_" + (int) (Math.random() * 99999));
            t.start();
        }
    }

    public void makeFolder(String name) {
        File dir = new File(name);
        dir.mkdir();
    }

    public JFrame getContainer() {
        return container;
    }

    private void writeGIF(String folder, String filename, String u) throws IOException {
        byte[] b = new byte[1];
        URL url = new URL(u);
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        DataInputStream di = new DataInputStream(urlConnection.getInputStream());

        FileOutputStream fo = new FileOutputStream(folder + "/" + filename + ".gif");
        while (-1 != di.read(b, 0, 1)) {
            fo.write(b, 0, 1);
        }
        di.close();
        fo.close();
        numFiles++;

    }

    private void writeImage(String folder, String filename, String url, String ext) throws IOException {
        URL urlIMG = new URL(url);
        InputStream in = new BufferedInputStream(urlIMG.openStream());
        OutputStream out = new BufferedOutputStream(new FileOutputStream(folder + "/" + filename + "." + ext));
        for (int i; (i = in.read()) != -1;) {
            out.write(i);
        }
        in.close();
        out.close();
        numFiles++;
    }

    public void updateProgress(int i, int max) {
        int factor = ((i * 600) / max);
        if (factor == 0) {
            g.setColor(consts.tumblrBlue);
            g.fillRect(0, 0, 600, 25);
        } else {
            g.setColor(consts.white);
            g.fillRect(0, 0, factor, 25);
            g.setColor(consts.tumblrBlue);
            g.drawString((int) ((i * 100) / max) + "%", factor - 30, 15);
        }
        display.repaint();
        revalidate();
    }

    public void updateOverallProgress(int i, int max) {
        int factor = ((i * 600) / max);
        if (factor == 0) {
            g.setColor(consts.tumblrBlue);
            g.fillRect(0, 25, 600, 25);
        } else {
            g.setColor(consts.white);
            g.fillRect(0, 25, factor, 25);
            g.setColor(consts.tumblrBlue);
            g.drawString((int) ((i * 100) / max) + "%", factor - 30, 40);

        }
        display.repaint();
        revalidate();
    }

    private class button extends JPanel implements MouseListener {

        private final String action;
        private final JLabel label;
        private final tumblrRipper r;
        private boolean sw;
        private boolean state;
        private JLabel stateLabel;
        private JPanel statePanel;
        private String stateString;

        public button(String a, String l, int w, int h, tumblrRipper ripper, boolean s, boolean state) {

            label = new JLabel(l);
            label.setForeground(consts.white);
            label.setFont(consts.small);
            add(label);

            statePanel = new JPanel();

            if (s) {
                statePanel.setBackground(consts.tumblrBlue);
            } else {
                statePanel.setBackground(consts.redColor);
            }
            statePanel.setPreferredSize(new Dimension(w / 4, h / 2));
            add(statePanel);

            this.setBackground(consts.redColor);
            sw = s;
            this.state = state;
            r = ripper;
            action = a;
            this.setPreferredSize(new Dimension(w, h));
            addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            if (action.equals("gif")) {
                if (r.gif()) {
                    r.setGifs(false);
                    sw = false;
                } else {
                    r.setGifs(true);
                    sw = true;
                }
            }

            if (action.equals("jpeg")) {
                if (r.jpeg()) {
                    r.setJpegs(false);
                    sw = false;
                } else {
                    r.setJpegs(true);
                    sw = true;
                }
            }

            if (action.equals("png")) {
                if (r.png()) {
                    r.setPngs(false);
                    sw = false;
                } else {
                    r.setPngs(true);
                    sw = true;
                }
            }
            if (action.equals("start")) {
                r.init();
            }

            if (action.equals("close")) {
                r.canRun = false;
                r.t = null;
                r.getContainer().setVisible(true);
                r.dispose();
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
            if (sw) {
                this.setBackground(consts.redDarkerColor);
                revalidate();
            } else {
                this.setBackground(consts.redDarkerColor);
                revalidate();
            }
        }

        @Override
        public void mouseExited(MouseEvent me) {
            if (sw) {
                this.setBackground(consts.redDarkerColor);
                statePanel.setBackground(consts.tumblrBlue);
                revalidate();
            } else {
                this.setBackground(consts.redColor);
                statePanel.setBackground(consts.redColor);
                revalidate();
            }

        }
    }

    public static String getTime(long milliseconds) {
        String format = String.format("%%0%dd", 2);
        long elapsedTime = milliseconds / 1000;
        elapsedTime = elapsedTime / 1000000;
        String seconds = String.format(format, elapsedTime % 60);
        String minutes = String.format(format, (elapsedTime % 3600) / 60);
        String hours = String.format(format, elapsedTime / 3600);
        String time = hours + ":" + minutes + ":" + seconds;
        return time;
    }

}
