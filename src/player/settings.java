/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.io.File;
import java.util.List;

/**
 *
 * @author sn0w
 */
public class settings {

    private String path;
    private File[] files;
    private final String ext=".gif";
    private final Integer maxCapacity = 2147483639;
    private final gifIndexer index;
    public settings(String path) {
        index = new gifIndexer();
        this.path=path;
        File f = new File(path);
        processFolder(f);
    }
    public void reinit(){
        File f = new File(path);
        processFolder(f);
    }
    public  settings(List<File> files){
        this.files=files.toArray(new File[files.size()]);
         index = new gifIndexer();
    }

    public void setPath(String s) {
        path = s;
    }

    public String getPath() {
        return path;
    }
    public boolean dublicate(int pointer){
        if(index.exists(files[pointer].getName())){
            return true;
        }else{
            index.add(files[pointer].getName());
            return false;
        }
    }
    public File getFileAtPointer(int pointer) {
        
        if (pointer > files.length - 1) {
            System.out.println("Out of bounds");
            return files[0];
        } else {
            return files[pointer];
        }

    }
    public int getNumFiles(){
        return files.length;
    }
    public File[] getFiles() {
        return files;
    }

    private void processFolder(File folder) {
        files = folder.listFiles();

    }
    private String getFileExtension(File file) {
    String name = file.getName();
    int lastIndexOf = name.lastIndexOf(".");
    if (lastIndexOf == -1) {
        return "null"; // empty extension
    }
    return name.substring(lastIndexOf);
}

}
