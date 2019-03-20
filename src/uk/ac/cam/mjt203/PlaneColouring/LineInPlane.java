package uk.ac.cam.mjt203.PlaneColouring;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LineInPlane {
    final ColouredPlane.PPoint start, step;

    public LineInPlane(ColouredPlane.PPoint start, ColouredPlane.PPoint step) {
        this.start = start;
        this.step = step;
    }
    public int getColourCount(ColouredPlane plane) {
        Set<Integer> colourSet = new HashSet<>();
        for(ColouredPlane.PPoint pt: pointsOnLine(plane)) colourSet.add(plane.getColour(pt));
        colourSet.remove(-1);
        return colourSet.size();
    }

    public List<ColouredPlane.PPoint> pointsOnLine(ColouredPlane plane) {
        List<ColouredPlane.PPoint> res = new ArrayList<>();
        for(int i=0; i<plane.getP(); ++i) res.add(start.add(step.multiply(i)));
        return res;
    }

    public LineInPlane getDisplayed(ColouredPlane plane) {
        return new LineInPlane(plane.getDisplayed(start),
                plane.getDisplayed(start.add(step)).subtract(plane.getDisplayed(start)));
    }

    public LineInPlane optimizeLineStep(ColouredPlane plane) {
        ColouredPlane.PPoint best = step;
        for(ColouredPlane.PPoint pt: pointsOnLine(plane)) if(!pt.equals(start)) {
            if(pt.subtract(start).realDistanceSq()<best.realDistanceSq()) {
                best = pt.subtract(start);
            }
        }
        return new LineInPlane(start, best);
    }

    public Set<Line2D.Float> toPrint(ColouredPlane plane, boolean optimize) {
        LineInPlane l = getDisplayed(plane);
        if(optimize) l = l.optimizeLineStep(plane);
        Point2D.Float dir = plane.getCoord(l.step, true);
        if(dir.x>0.5) dir.x -= 1.0f;
        if(dir.y>0.5) dir.y -= 1.0f;
        Set<Line2D.Float> res = new HashSet<>();
        int p = plane.getP();
        for(int i=0; i<p; ++i) {
            ColouredPlane.PPoint a = l.start.add(l.step.multiply(i));
            ColouredPlane.PPoint b = l.start.add(l.step.multiply(i+1));
            Point2D.Float fa = plane.getCoord(a, true);
            Point2D.Float fb = plane.getCoord(b, true);

            float remTime = 1.0f;
            while(true)
            {
                float minTime = 2.0f;
                float tmp = (1.0f-0.5f/p-fa.x)/dir.x;
                if(tmp<minTime && tmp>0.01) minTime = tmp;
                tmp = (1.0f-0.5f/p-fa.y)/dir.y;
                if(tmp<minTime && tmp>0.01) minTime = tmp;
                tmp = (-0.5f/p-fa.x)/dir.x;
                if(tmp<minTime && tmp>0.01) minTime = tmp;
                tmp = (-0.5f/p-fa.y)/dir.y;
                if(tmp<minTime && tmp>0.01) minTime = tmp;

                if(minTime>remTime) break;
                Point2D.Float intPoint = new Point2D.Float(fa.x+minTime*dir.x, fa.y+minTime*dir.y);
                res.add(new Line2D.Float(fa, intPoint));
                fa = intPoint;
                if(fa.x <=-0.5f/p+0.001) fa.x += 1;
                else if(fa.x >= 1-0.5f/p-0.001) fa.x -=1;

                if(fa.y <=-0.5f/p+0.001) fa.y += 1;
                else if(fa.y >= 1-0.5f/p-0.001) fa.y -=1;
                remTime -= minTime;
            }
            res.add(new Line2D.Float(fa, fb));
        }
        return res;
    }

}
