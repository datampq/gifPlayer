/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author sn0w
 */
public class downloader extends JFrame implements Runnable {

    private final gifDownloader d;
    private JLabel display;
    private Graphics g;
    private JLabel cFile;
    private JLabel status;
    private int numFiles = 0;
    Thread t;
    String t_name;
    private gifIndexer index;

    public downloader(gifDownloader d) {
        this.d = d;
        index = new gifIndexer();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        add(content());
        pack();
        setVisible(true);
        t_name = "thread_" + (int) (Math.random() * 99999);
        if (t == null) {
            t = new Thread(this, t_name);
            t.start();
        }
    }

    private JPanel content() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(700, 100));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(textPanel());
        content.add(graphicsPanel());
        return content;
    }

    private JPanel textPanel() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        content.setPreferredSize(new Dimension(700, 50));

        status = new JLabel("Loading...");
        status.setForeground(consts.white);
        status.setFont(consts.normal);
        cFile = new JLabel();
        cFile.setForeground(consts.white);
        cFile.setFont(consts.tiny);
        content.add(status);
        content.add(cFile);
        return content;
    }

    private JPanel graphicsPanel() {
        JPanel content = new JPanel();
        content.setBackground(consts.tumblrBlue);
        final BufferedImage image = new BufferedImage(700, 50, BufferedImage.TYPE_INT_RGB);
        display = new JLabel(new ImageIcon(image));
        content.add(display);
        g = image.createGraphics();
        g.setColor(consts.tumblrBlue);
        g.fillRect(0, 0, 700, 50);
        return content;
    }

    @Override
    public void run() {
        String[] q = d.getInput();
        for (int i = 0; i < q.length; i++) {
            status.setText("Working...");

            updateOverallProgress(i, q.length);

            try {
                URL url = new URL("https://www.tumblr.com/search/gif+" + q[i]);
                URLConnection connection = url.openConnection();
                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {

                    builder.append(line);
                }
                String toString = builder.toString();
                // System.out.println(toString);
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
                    if (a == 'h' && w == 'i' && e == 'g' && q1 == 'h' && w1 == '_' && e1 == 'r' && w2 == 'e' && e2 == 's') {

                        String toDown = "";
                        int pointer = z + 11;
                        char r;
                        while ((r = toString.charAt(pointer)) != '"') {
                            toDown = toDown + r;
                            pointer++;
                        }
                        //implement .png, .jpg, .jpeg, .gif image recognition alorithm
                        String to = "\\\\";

                        toDown = toDown.replaceAll(to, "");

                        if (toDown.length() > 0) {
                            if (toDown.charAt(toDown.length() - 3) == 'g' && toDown.charAt(toDown.length() - 2) == 'i' && toDown.charAt(toDown.length() - 1) == 'f') {
                                //System.out.println("Is a GIF!!!!");
                                String[] split = toDown.split("/");
                                if (index.exists(split[split.length - 1])) {
                                    break;
                                } else {
                                    int task = i + 1;
                                    writeImage(d.getPath().getAbsolutePath(), "mpq_result_" + (int) (Math.random() * 999999) + "_" + z, toDown);
                                    status.setText("[Files: " + numFiles + ";Task: " + q[i] + " (" + task + "/" + q.length + ")]: ");
                                    revalidate();
                                    index.add(split[split.length - 1]);
                                    cFile.setText(split[split.length - 1]);
                                }
                            }
                        }

                        // System.out.println(toDown);
                        z = pointer;
                    }

                }

            } catch (MalformedURLException ex) {
                System.out.println("MalformedURLException ex");
                error r = new error("[MalformedURLException ex] Please verify your input");
                i++;
            } catch (IOException ex) {
                System.out.println("IOException ex");
                error r = new error("[Exception ex]");
                i++;
            }
            updateOverallProgress(i, q.length);
        }
        d.setEnabled(true);
        this.dispose();
    }

    private void writeImage(String folder, String filename, String url) throws IOException {
        URL urlIMG = new URL(url);
        InputStream in = new BufferedInputStream(urlIMG.openStream());
        OutputStream out = new BufferedOutputStream(new FileOutputStream(folder + "/" + filename + ".gif"));
        for (int i; (i = in.read()) != -1;) {
            out.write(i);
        }
        in.close();
        out.close();
        numFiles++;
    }

    private void writeImagev2(String folder, String filename, String u) throws IOException {
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

    public void updateProgress(int i, int max) {
        int factor = ((i * 700) / max);
        if (factor == 0) {
            g.setColor(consts.tumblrBlue);
            g.fillRect(0, 0, 700, 25);
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
        int factor = ((i * 700) / max);
        if (factor == 0) {
            g.setColor(consts.tumblrBlue);
            g.fillRect(0, 25, 700, 25);
        } else {
            g.setColor(consts.white);
            g.fillRect(0, 25, factor, 25);
            g.setColor(consts.tumblrBlue);
            g.drawString((int) ((i * 100) / max) + "%", factor - 30, 40);

        }
        display.repaint();
        revalidate();
    }

}
