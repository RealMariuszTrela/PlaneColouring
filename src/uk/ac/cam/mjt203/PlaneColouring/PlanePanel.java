package uk.ac.cam.mjt203.PlaneColouring;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class PlanePanel extends JPanel implements MouseListener {

    protected ColouredPlane plane;
    private float squareSize;
    private ColourPickingPanel picker;

    public PlanePanel(ColourPickingPanel picker) {
        super();
        this.addMouseListener(this);
        this.picker = picker;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g1) {

        Graphics2D g = (Graphics2D)g1;
        // Paint the background white
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());



        if(this.plane == null) return;

        squareSize = Math.min(this.getWidth(), this.getHeight())/(plane.getP()+1.0f);

        g.setStroke(new BasicStroke(5));

        for(ColouredPlane.Line l: plane.getViolatingLines()) {
            for(ColouredPlane.PPoint pt: l.pointsOnLine()) {
                Point2D.Float fp = plane.getCoord(pt, false);
                int x = (int)(squareSize+plane.getP()*squareSize*fp.x);
                int y = (int)(squareSize+plane.getP()*squareSize*fp.y);
                g.setColor(Color.RED);
                g.drawOval(x-(int)squareSize/6, y-(int)squareSize/6, (int)squareSize/3, (int)squareSize/3);
            }
            Set<Line2D.Float> segments = l.toPrint();
            for(Line2D.Float segment: segments) {
                int x1 = (int)(squareSize+plane.getP()*squareSize*segment.x1);
                int y1 = (int)(squareSize+plane.getP()*squareSize*segment.y1);
                int x2 = (int)(squareSize+plane.getP()*squareSize*segment.x2);
                int y2 = (int)(squareSize+plane.getP()*squareSize*segment.y2);
                g.drawLine(x1, y1, x2, y2);
            }
        }

        for(int row=0; row<plane.getP(); ++row) {
            for(int col=0; col<plane.getP(); ++col) {
                Point2D.Float fp = plane.getCoord(row, col, false);
                int x = (int)(squareSize+plane.getP()*squareSize*fp.x);
                int y = (int)(squareSize+plane.getP()*squareSize*fp.y);
                g.setColor(ColourEncoder.getColour(plane.getColour(row, col)));
                g.fillOval(x-(int)squareSize/8, y-(int)squareSize/8, (int)squareSize/4, (int)squareSize/4);
                if(plane.getColour(row, col)==-1) {
                    g.setColor(Color.WHITE);
                    g.drawString("?", x, y);
                }
            }
        }




        g.setColor(Color.BLACK);
        final int distanceFromCorner = 10;
        g.drawString("Number of colours: "+plane.getColourCount(), distanceFromCorner, this.getHeight()-distanceFromCorner);
    }

    public void display(ColouredPlane plane) {
        this.plane = plane;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(plane == null) return;
        Point p = e.getPoint();
        float fx = (p.x-squareSize)/(plane.getP()*squareSize);
        float fy = (p.y-squareSize)/(plane.getP()*squareSize);

        if(e.getButton() == MouseEvent.BUTTON1) {
            plane.setColour(plane.getPoint(fx, fy, 1.0 / 8), picker.getCurrentColourId());
        }
        else if(e.getButton() == MouseEvent.BUTTON3) {
            plane.setColour(plane.getPoint(fx, fy, 1.0 / 8), -1);
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
