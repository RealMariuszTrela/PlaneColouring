package uk.ac.cam.mjt203.PlaneColouring;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlaneShufflerPanel extends JPanel implements KeyListener {
    private ColouredPlane plane;
    private GUI parent;

    public PlaneShufflerPanel(GUI parent, ColouredPlane plane) {
        super();

        this.parent = parent;
        this.plane = plane;
        setLayout(new GridLayout(2, 1));
        JPanel topButtons = new JPanel();
        topButtons.setLayout(new GridLayout(2, 1));

        JButton top = new JButton("^ (W)");
        top.addActionListener(e -> {slide(1, -1);});
        topButtons.add(top);
        JButton bottom = new JButton("v (S)");
        bottom.addActionListener(e -> {slide(1, 1);});
        topButtons.add(bottom);
        add(topButtons);

        JPanel bottomButtons = new JPanel();
        bottomButtons.setLayout(new GridLayout(1, 2));

        JButton left = new JButton("< (A)");
        left.addActionListener(e -> {slide(2, -1);});
        bottomButtons.add(left);
        JButton right = new JButton("> (D)");
        right.addActionListener(e -> {slide(2, 1);});
        bottomButtons.add(right);
        add(bottomButtons);

        Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border tb = BorderFactory.createTitledBorder(etch,LanguageTranslator.getTranslation(parent.getLanguage(),"Slide"));
        setBorder(tb);

    }

    public void slide(int dir, int amount) {
        if(plane==null) return;
        if(dir==1) plane.slide1(amount);
        else plane.slide2(amount);
        parent.update();
    }

    public void setPlane(ColouredPlane plane) {
        this.plane = plane;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_W) {
            slide(1, -1);
        }
        if(e.getKeyCode()== KeyEvent.VK_S) {
            slide(1, 1);
        }
        if(e.getKeyCode()== KeyEvent.VK_A) {
            slide(2, -1);
        }
        if(e.getKeyCode()== KeyEvent.VK_D) {
            slide(2, 1);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
