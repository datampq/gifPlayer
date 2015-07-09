/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author sn0w
 */
public class console extends JFrame implements Runnable{
    Thread t;
    String t_name;
    private JScrollPane scrol;
    private boolean run;
    public console(){
        run=true;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        add(content());
        pack();
        setVisible(true);
    
    }
    private JPanel content(){
        JPanel content = new JPanel();
        content.setBackground(consts.black);
        return content;
    }
    @Override
    public void run() {
        while(run){
        
        }
    }
    public void increment(){
    
    }
    
}
