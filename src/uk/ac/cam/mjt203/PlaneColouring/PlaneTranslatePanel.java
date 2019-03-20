package uk.ac.cam.mjt203.PlaneColouring;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlaneTranslatePanel extends JPanel implements KeyListener {
    private ColouredPlane plane;
    private GUI parent;

    public PlaneTranslatePanel(GUI parent, ColouredPlane plane) {
        super();

        this.parent = parent;
        this.plane = plane;
        setLayout(new GridLayout(2, 1));
        JPanel topButtons = new JPanel();
        topButtons.setLayout(new GridLayout(2, 1));

        JButton top = new JButton("^");
        top.addActionListener(e -> {translate(0, -1);});
        topButtons.add(top);
        JButton bottom = new JButton("v");
        bottom.addActionListener(e -> {translate(0, 1);});
        topButtons.add(bottom);
        add(topButtons);

        JPanel bottomButtons = new JPanel();
        bottomButtons.setLayout(new GridLayout(1, 2));

        JButton left = new JButton("<");
        left.addActionListener(e -> {translate(-1, 0);});
        bottomButtons.add(left);
        JButton right = new JButton(">");
        right.addActionListener(e -> {translate(1, 0);});
        bottomButtons.add(right);
        add(bottomButtons);

        Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border tb = BorderFactory.createTitledBorder(etch,LanguageTranslator.getTranslation(parent.getLanguage(),"Translate"));
        setBorder(tb);

    }

    public void setPlane(ColouredPlane plane) {
        this.plane = plane;
    }

    public void translate(int x, int y) {
        if(plane==null) return;
        plane.offsetBy(x, y);
        parent.update();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode()== KeyEvent.VK_UP) {
            translate(0, -1);
        }
        if(e.getKeyCode()== KeyEvent.VK_DOWN) {
            translate(0, 1);
        }
        if(e.getKeyCode()== KeyEvent.VK_LEFT) {
            translate(-1, 0);
        }
        if(e.getKeyCode()== KeyEvent.VK_RIGHT) {
            translate(1, 0);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
