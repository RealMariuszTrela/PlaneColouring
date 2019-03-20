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

    private GUI parent;
    protected ColouredPlane plane;
    private float squareSize;
    private ColourPickingPanel picker;
    private Point2D.Float lineStart;

    public PlanePanel(GUI parent, ColourPickingPanel picker) {
        super();
        this.addMouseListener(this);
        this.picker = picker;
        this.parent = parent;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g1) {

        Graphics2D g = (Graphics2D)g1;
        // Paint the background white
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());



        if(this.plane == null) return;

        squareSize = Math.min(this.getWidth(), this.getHeight())/(plane.getP()+1.0f);


        for(LineInPlaneWithColour l: plane.getMarkedLines()) {

            g.setColor(ColourEncoder.getColour(l.colourId));
            Set<Line2D.Float> segments = l.toPrint(plane, false);
            for(Line2D.Float segment: segments) {
                int x1 = (int)(squareSize+plane.getP()*squareSize*segment.x1);
                int y1 = (int)(squareSize+plane.getP()*squareSize*segment.y1);
                int x2 = (int)(squareSize+plane.getP()*squareSize*segment.x2);
                int y2 = (int)(squareSize+plane.getP()*squareSize*segment.y2);
                g.drawLine(x1, y1, x2, y2);
            }
        }

        g.setStroke(new BasicStroke(5));

        for(LineInPlane l: plane.getViolatingLines()) {
            for(ColouredPlane.PPoint pt: l.pointsOnLine(plane)) {
                Point2D.Float fp = plane.getCoord(pt, false);
                int x = (int)(squareSize+plane.getP()*squareSize*fp.x);
                int y = (int)(squareSize+plane.getP()*squareSize*fp.y);
                g.setColor(Color.RED);
                g.drawOval(x-(int)squareSize/6, y-(int)squareSize/6, (int)squareSize/3, (int)squareSize/3);
            }
            Set<Line2D.Float> segments = l.toPrint(plane, true);
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
        final int distanceFromCorner = 20;
        g.drawString(LanguageTranslator.getTranslation(parent.getLanguage(),"Number of colours: ")+plane.getColourCount(),
                distanceFromCorner, this.getHeight()-2*distanceFromCorner);

        g.drawString(LanguageTranslator.getTranslation(parent.getLanguage(),"Selected colour"),
                2*distanceFromCorner, this.getHeight()-distanceFromCorner);

        g.setColor(ColourEncoder.getColour(picker.getCurrentColourId()));
        g.fillRect(distanceFromCorner/2, this.getHeight()-3*distanceFromCorner/2,
                distanceFromCorner, distanceFromCorner);

        g.setStroke(new BasicStroke(7));
        g.setColor(Color.BLACK);
        g.drawRect((int)squareSize/2, (int)squareSize/2, (int)(plane.getP()*squareSize), (int)(plane.getP()*squareSize));
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
    public void mousePressed(MouseEvent e) {
        lineStart = new Point2D.Float(e.getPoint().x, e.getPoint().y);
        parent.requestFocusInWindow();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(lineStart==null) return;
        Point2D.Float lineEnd = new Point2D.Float(e.getPoint().x, e.getPoint().y);

        float sx = (lineStart.x-squareSize)/(plane.getP()*squareSize);
        float sy = (lineStart.y-squareSize)/(plane.getP()*squareSize);

        float ex = (lineEnd.x-squareSize)/(plane.getP()*squareSize);
        float ey = (lineEnd.y-squareSize)/(plane.getP()*squareSize);

        ColouredPlane.PPoint start = plane.getPoint(sx, sy, 1.0/8);
        ColouredPlane.PPoint end = plane.getPoint(ex, ey, 1.0/8);
        plane.addLine(start, end, picker.getCurrentColourId());

        lineStart = null;

        repaint();
        parent.requestFocusInWindow();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
