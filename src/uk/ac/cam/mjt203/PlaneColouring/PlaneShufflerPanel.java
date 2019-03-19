package uk.ac.cam.mjt203.PlaneColouring;

import javax.swing.*;
import java.awt.*;

public class PlaneShufflerPanel extends JPanel {
    private ColouredPlane plane;
    private GUI parent;

    public PlaneShufflerPanel(GUI parent, ColouredPlane plane) {
        super();

        this.parent = parent;
        this.plane = plane;
        setLayout(new GridLayout(2, 1));
        JPanel topButtons = new JPanel();
        topButtons.setLayout(new GridLayout(2, 1));

        JButton top = new JButton("^");
        top.addActionListener(e -> {if(this.plane==null) return; this.plane.slide1(-1); this.parent.update();});
        topButtons.add(top);
        JButton bottom = new JButton("v");
        bottom.addActionListener(e -> {if(this.plane==null) return; this.plane.slide1(1); this.parent.update();});
        topButtons.add(bottom);
        add(topButtons);

        JPanel bottomButtons = new JPanel();
        bottomButtons.setLayout(new GridLayout(1, 2));

        JButton left = new JButton("<");
        left.addActionListener(e -> {if(this.plane==null) return; this.plane.slide2(-1); this.parent.update();});
        bottomButtons.add(left);
        JButton right = new JButton(">");
        right.addActionListener(e -> {if(this.plane==null) return; this.plane.slide2(1); this.parent.update();});
        bottomButtons.add(right);
        add(bottomButtons);

    }

    public void setPlane(ColouredPlane plane) {
        this.plane = plane;
    }
}
