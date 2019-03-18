package uk.ac.cam.mjt203.PlaneColouring;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColourPickingPanel extends JPanel {

    private int size = 10;

    private int currentColour = -1;

    public ColourPickingPanel() {
        setLayout(new GridLayout(1, size));
        for (int i = 0; i < 10; ++i) {
            JButton button = new JButton(Integer.toString(i));
            button.setBackground(ColourEncoder.getColour(i));
            button.setMinimumSize(new Dimension(50, 50));
            int k = i;
            button.addActionListener(e -> {currentColour = k;});
            add(button);
        }
    }

    public int getCurrentColourId() {
        return currentColour;
    }

}
