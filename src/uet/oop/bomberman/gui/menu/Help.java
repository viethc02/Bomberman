package uet.oop.bomberman.gui.menu;

import uet.oop.bomberman.gui.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Help extends JMenu {
    public Help(Frame frame) {
        super("Help");

        JMenuItem play = new JMenuItem("How to play");
        play.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        play.addActionListener(new MenuActionListener(frame));
        add(play);

        JMenuItem about = new JMenuItem("About");
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        about.addActionListener(new MenuActionListener(frame));
        add(about);
    }

    class MenuActionListener implements ActionListener {
        public Frame frame;

        public MenuActionListener(Frame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("How to play")) {
                JOptionPane.showMessageDialog(frame, "Play with Computer: " +
                        "UP, DOWN, LEFT, RIGHT; SPACE\n" +
                        "PvP: \n" +
                        "\tPlayer_1: UP, DOWN, LEFT, RIGHT; SPACE\n" +
                        "\tPlayer_2: W, S, A, D; X", "How to play", JOptionPane.QUESTION_MESSAGE);
            }
            if (e.getActionCommand().equals("About")) {
                JOptionPane.showMessageDialog(frame, "Author:\n" +
                        "Nguyen Hoang Viet\n" +
                        "Hoang Ngoc Kieu Anh", "About", JOptionPane.QUESTION_MESSAGE);
            }
        }
    }
}
