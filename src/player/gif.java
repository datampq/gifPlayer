/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author sn0w
 */
public class gif {

    private final File source;
    private BufferedImage[] frames;
    private int numFrames;
    private final int filePointer;

    public gif(File f, int p) {
        source = f;
        filePointer = p;
        //System.out.println("-------->Stating at:"+p);
//        try {
//            
//            frames = safeRead(source);
//            numFrames = frames.length;
//            System.out.println("Sucessfully read safely");
//            for(int i = 0;i<frames.length;i++){
//            frames[i] = fix(frames[i], i);
//        }
//            //readFrames();
//        } catch (IOException ex) {
        readFrames();
//        }

    }

    public int getFrameCount() {
        return numFrames;

    }

    public int getPointer() {
        // System.out.println("-------->Ending at:"+filePointer);
        return filePointer;

    }

    public File getSource() {
        return source;
    }

    public BufferedImage[] getAllFrames() {
        return frames;
    }

    public BufferedImage getFrameAtPointer(int pointer) {
        if (pointer > frames.length - 1) {
            return frames[frames.length - 1];
        } else {
            return frames[pointer];
        }

    }

    private void readFrames() {
        try {
            ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
            ImageInputStream stream = ImageIO.createImageInputStream(source);
            reader.setInput(stream);
            try {
                int noi = reader.getNumImages(true);
                numFrames = noi;
                //System.out.println(source + "loaded; frame count: " + noi);
                frames = new BufferedImage[noi];
                for (int i = 0; i < noi; i++) {
                    try {
                        frames[i] = reader.read(i);
                        frames[i] = fix(frames[i], i);
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }
            } catch (IllegalStateException e) {

            }

        } catch (IOException ex) {

        }

    }

    private BufferedImage[] safeRead(File fileToLoad) throws IOException {
        if (fileToLoad == null) {
            throw new NullPointerException("null file!");
        }

        //obtain the appropriate image reader 
        ImageInputStream stream = ImageIO.createImageInputStream(fileToLoad);
        ImageReader reader = ImageIO.getImageReaders(stream).next();

        reader.setInput(stream, true, false);
        List<BufferedImage> framesTmp = new ArrayList<>();
        //read the images 

        int index = 0;
        while (true) {
            try {
                framesTmp.add(reader.read(index++));

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        BufferedImage[] toReturn = framesTmp.toArray(new BufferedImage[]{});

        //clean up 
        reader.dispose();
        stream.close();

        return toReturn;

    }

    private BufferedImage fix(BufferedImage image, int f) {

        BufferedImage toReturn = image;
        for (int i = image.getMinX(); i < image.getWidth(); i++) {
            for (int j = image.getMinY(); j < image.getHeight(); j++) {
                int rgb = image.getRGB(i, j);
                if ((rgb >> 24) == 0x00) {
                    boolean easyFix = false;
                    int newRGB = rgb;
                    if (f > 0 && frames[f - 1] != null) {
                        if ((frames[f - 1].getRGB(i, j) >> 24) != 0x00) {
                            easyFix = true;
                            rgb = frames[f - 1].getRGB(i, j);
                        }
                    }
                    if (!easyFix) {

                    }
                }
                image.setRGB(i, j, rgb);
            }
        }
        return toReturn;

    }
}
