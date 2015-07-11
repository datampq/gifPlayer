/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author sn0w
 */
public class playlistEditor extends JFrame {

    private final imagePlayer player;
    public JPanel[] panels;
    public playlistEditor(imagePlayer player) {
        this.player = player;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panels = new JPanel[player.getSettings().getNumFiles()];
        add(content(), BorderLayout.CENTER);
        pack();
        setVisible(true);

    }

    private JScrollPane content() {
        JPanel content = new JPanel();
       content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        
        int initialDimH = 0;
        for (int i = 0; i < player.getSettings().getNumFiles(); i++) {
           // System.out.println("Adding data");
            JPanel filePanel = new JPanel();
            filePanel.setBackground(consts.redLighterColor);
            filePanel.setPreferredSize(new Dimension(200,25));
            filePanel.addMouseListener(new MouseListenerImpl(filePanel, i, player));
            JLabel l = new JLabel(i+1+". "+player.getSettings().getFileAtPointer(i).getName());
            l.setForeground(consts.white);
            l.setFont(consts.tiny);
            panels[i]=filePanel;
            initialDimH+=25;
            filePanel.add(l);
           
            content.add(filePanel);
            
        }
        
        JScrollPane pane = new JScrollPane(content);
        
       
        pane.setPreferredSize(new Dimension(300, 700));
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.getVerticalScrollBar().setUnitIncrement(20);
        return pane;
    }
    public void clearPanels(){
        for (JPanel panel : panels) {
            panel.setBorder(BorderFactory.createEmptyBorder());
        }
    }
    public void clearPanel(int i){
        panels[i].setBorder(BorderFactory.createEmptyBorder());
    }

    private static class MouseListenerImpl implements MouseListener {
        
        private final JPanel panel;
        private final int num;
        private final imagePlayer player;

        public MouseListenerImpl(JPanel p, int pointer, imagePlayer player) {
            this.player=player;
            num=pointer;
            panel=p;
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            player.setPointer(num);
        }

        @Override
        public void mousePressed(MouseEvent me) {

        }

        @Override
        public void mouseReleased(MouseEvent me) {

        }

        @Override
        public void mouseEntered(MouseEvent me) {
            panel.setBackground(consts.tumblrBlue);
        }   

        @Override
        public void mouseExited(MouseEvent me) {
            panel.setBackground(consts.redLighterColor);
        }
    }

}
