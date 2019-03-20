package uk.ac.cam.mjt203.PlaneColouring;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ColourPickingPanel extends JPanel implements KeyListener {

    private int size = 10;

    private int currentColour = 0;

    private GUI parent;

    public ColourPickingPanel(GUI parent) {
        this.parent = parent;
        setLayout(new GridLayout(1, size));
        for (int i = 0; i < 10; ++i) {
            JButton button = new JButton(Integer.toString(i));
            button.setBackground(ColourEncoder.getColour(i));
            button.setMinimumSize(new Dimension(50, 50));
            int k = i;
            button.addActionListener(e -> {currentColour = k; parent.update();});
            add(button);
        }
    }

    public int getCurrentColourId() {
        return currentColour;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        char c = keyEvent.getKeyChar();
        if(c>='0' && c<= '9')
        {
            currentColour = c-'0';
            parent.update();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
