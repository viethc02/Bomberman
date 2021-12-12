package uet.oop.bomberman.gui.menu;

import uet.oop.bomberman.gui.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Game extends JMenu {
    //private Frame frame;

    public Game(Frame frame) {
        super("Game");
        //this.frame = frame;

        JMenu newGame = new JMenu("New Game");
        JMenuItem pvc = new JMenuItem("Play with Computer");
        pvc.addActionListener(new MenuActionListener(frame));
        newGame.add(pvc);

        JMenuItem pvp = new JMenuItem("PvP");
        pvp.addActionListener(new MenuActionListener(frame));
        newGame.add(pvp);

        JMenuItem ai = new JMenuItem("AI");
        ai.addActionListener(new MenuActionListener(frame));
        newGame.add(ai);
        newGame.addSeparator();
        add(newGame);

        JMenuItem pause = new JMenuItem("Pause");
        pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        pause.addActionListener(new MenuActionListener(frame));
        add(pause);

        JMenuItem resume = new JMenuItem("Resume");
        resume.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        resume.addActionListener(new MenuActionListener(frame));
        add(resume);
    }

    class MenuActionListener implements ActionListener {
        public Frame frame;

        public MenuActionListener(Frame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Play with Computer")) {
                frame.newGame();
            }
            if (e.getActionCommand().equals("PvP")) {
                frame.newPvP();
            }
            if (e.getActionCommand().equals("AI")) {
                frame.newAI();
            }
            if (e.getActionCommand().equals("Pause")) {
                frame.pauseGame();
            }
            if (e.getActionCommand().equals("Resume")) {
                frame.resumeGame();
            }
        }
    }
}
