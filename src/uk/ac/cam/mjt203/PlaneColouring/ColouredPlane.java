package uk.ac.cam.mjt203.PlaneColouring;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ColouredPlane implements Comparable<ColouredPlane> {
    private final int p;
    private int[][] colours;
    private final int r;
    private final String name;

    @Override
    public int compareTo(ColouredPlane other) {
        return name.compareTo(other.name);
    }

    public class PPoint
    {
        public final int x, y;
        public PPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public PPoint add(PPoint other) {
            return new PPoint((x+other.x)%p, (y+other.y)%p);
        }

        public PPoint subtract(PPoint other) {
            return new PPoint((x-other.x+p)%p, (y-other.y+p)%p);
        }

        public PPoint multiply(int scalar) {
            return new PPoint((x*scalar)%p, (y*scalar)%p);
        }

        public int realDistanceSq() {
            return Math.min(x, p-x)*Math.min(x, p-x)+Math.min(y, p-y)*Math.min(y, p-y);
        }

        @Override
        public boolean equals(Object o){
            PPoint pt = (PPoint)o;
            return x == pt.x && y == pt.y;
        }
    }

    public class Line {
        final PPoint start, step;
        public Line(PPoint start, PPoint step) {
            this.start = start;
            this.step = step;
        }
        public int getColourCount() {
            Set<Integer> colourSet = new HashSet<>();
            for(PPoint pt: pointsOnLine()) colourSet.add(getColour(pt));
            colourSet.remove(-1);
            return colourSet.size();
        }

        public List<PPoint> pointsOnLine() {
            List<PPoint> res = new ArrayList<>();
            for(int i=0; i<p; ++i) res.add(start.add(step.multiply(i)));
            return res;
        }

        public Line getDisplayed() {
            return new Line(ColouredPlane.this.getDisplayed(start),
                    ColouredPlane.this.getDisplayed(start.add(step)).subtract(ColouredPlane.this.getDisplayed(start)));
        }

        public Line optimizeLineStep() {
            PPoint best = step;
            for(PPoint pt: pointsOnLine()) if(!pt.equals(start)) {
                if(pt.subtract(start).realDistanceSq()<best.realDistanceSq()) {
                    best = pt.subtract(start);
                }
            }
            return new Line(start, best);
        }

        public Set<Line2D.Float> toPrint() {
            Line l = getDisplayed().optimizeLineStep();
            Set<Line2D.Float> res = new HashSet<>();
            for(int i=0; i<p; ++i) {
                PPoint a = l.start.add(l.step.multiply(i));
                PPoint b = l.start.add(l.step.multiply(i+1));
                Point2D.Float fa = getCoord(a, true);
                Point2D.Float fb = getCoord(b, true);
                Point2D.Float dir = getCoord(l.step, true);
                if(dir.x>0.5) dir.x -= 1.0f;
                if(dir.y>0.5) dir.y -= 1.0f;
                float remTime = 1.0f;
                while(true)
                {
                    float minTime = 1.0f;
                    float tmp = (1.0f-0.5f/p-fa.x)/dir.x;
                    if(tmp<minTime && tmp>0) minTime = tmp;
                    tmp = (1.0f-0.5f/p-fa.y)/dir.y;
                    if(tmp<minTime && tmp>0) minTime = tmp;
                    tmp = (-0.5f/p-fa.x)/dir.x;
                    if(tmp<minTime && tmp>0) minTime = tmp;
                    tmp = (-0.5f/p-fa.y)/dir.y;
                    if(tmp<minTime && tmp>0) minTime = tmp;

                    if(minTime>remTime) break;
                    Point2D.Float intPoint = new Point2D.Float(fa.x+minTime*dir.x, fa.y+minTime*dir.y);
                    res.add(new Line2D.Float(fa, intPoint));
                    fa = new Point2D.Float(fb.x-(remTime-minTime+0.0001f)*dir.x, fb.y-(remTime-minTime+0.0001f)*dir.y);
                    remTime -= minTime+0.0001f;
                }
                res.add(new Line2D.Float(fa, fb));
            }
            return res;
        }


    }

    public ColouredPlane(int p, String name) {
        this.p = p;
        this.name = name;
        int rval = p-1;
        while(ModularArithmetic.power(rval, (p-1)/2, p)!=p-1) --rval;
        r = rval;

        colours = new int[p][p];
        for(int i=0; i<p; ++i) for(int j=0; j<p; ++j) {
            colours[i][j] = -1;
        }
    }

    public ColouredPlane(String format) {
        format = format.replaceAll("\n", "");
        String[] basicSplit = format.split(":");
        name = basicSplit[0];
        p = Integer.parseInt(basicSplit[1]);
        colours = new int[p][p];


        String[] cells = basicSplit[2].split(" ");
        for(int i=0; i<p; ++i) for(int j=0; j<p; ++j) {
            colours[i][j] = Integer.parseInt(cells[i*p+j]);
        }



        int rval = p-1;
        while(ModularArithmetic.power(rval, (p-1)/2, p)!=p-1) --rval;
        r = rval;
    }

    public int getColour(PPoint a) {
        if(a.x<0||a.x>=p || a.y<0||a.y>=p) return -1;
        return colours[a.x][a.y];
    }

    public int getColour(int x, int y) {
        if(x<0||x>=p || y<0||y>=p) return -1;
        return colours[x][y];
    }

    public void setColour(PPoint a, int col) {
        if(a.x<0||a.x>=p || a.y<0||a.y>=p) return;
        colours[a.x][a.y] = col;
    }

    public int getP() {
        return p;
    }

    public PPoint getDisplayed(PPoint pt) {
        return pt;
    }

    public Point2D.Float getCoord(int row, int col, boolean fromDisplayed) {
        return getCoord(new PPoint(row, col), fromDisplayed);
    }

    public Point2D.Float getCoord(PPoint pt, boolean fromDisplayed) {
        PPoint displayed = pt;
        if(!fromDisplayed) displayed = getDisplayed(pt);
        return new Point2D.Float(displayed.x*(1.0f/p), displayed.y*(1.0f/p));
    }


    public PPoint getPoint(float x, float y, double sensitivity) {


        PPoint res = new PPoint((int)(x*p+0.5), (int)(y*p+0.5));
        float dist_sq = (res.x-x*p)*(res.x-x*p)+(res.y-y*p)*(res.y-y*p);
        if(dist_sq>sensitivity*sensitivity) return new PPoint(-1, -1);
        return res;
    }

    public int getColourCount() {
        Set<Integer> colourSet = new HashSet<>();
        for(int i=0; i<p; ++i) for(int j=0; j<p; ++j) {
            colourSet.add(colours[i][j]);
        }
        colourSet.remove(-1);
        return colourSet.size();
    }



    public Set<Line> getAllLines() {
        Set<Line> lines = new HashSet<>();
        for(int j=0; j<p; ++j) for(int k=0; k<p; ++k) {
            lines.add(new Line(new PPoint(0, j), new PPoint(1, k)));
        }
        for(int i=0; i<p; ++i) {
            lines.add(new Line(new PPoint(i, 0), new PPoint(0, 1)));
        }
        return lines;
    }

    public Set<Line> getViolatingLines() {
        Set<Line> violatingLines = new HashSet<>();
        for(Line l: getAllLines()) {
            if(l.getColourCount()>3) {
                violatingLines.add(l);
            }
        }
        return violatingLines;
    }

    @Override
    public String toString() {
        return name;
    }

}
