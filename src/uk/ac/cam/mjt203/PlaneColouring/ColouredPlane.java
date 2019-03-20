package uk.ac.cam.mjt203.PlaneColouring;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static uk.ac.cam.mjt203.PlaneColouring.LineInPlaneWithColour.lineFromString;


public class ColouredPlane implements Comparable<ColouredPlane> {
    private final int p;
    private int[][] colours;
    private final int r;
    private final String name;

    private PPoint displayBase1 = new PPoint(0, 1), displayBase2 = new PPoint(1, 0);
    private PPoint origin = new PPoint(0, 0);


    private Set<LineInPlaneWithColour> markedLines = new HashSet<>();


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

        if(basicSplit.length>3) {
            String[] lines = basicSplit[3].split("-");
            for(String l: lines) {
                markedLines.add(lineFromString(this, l));
            }
        }
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
        return displayBase1.multiply(pt.x).add(displayBase2.multiply(pt.y)).add(origin);
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


        PPoint inDisplay = new PPoint((int)(x*p+0.5), (int)(y*p+0.5));

        float dist_sq = (inDisplay.x-x*p)*(inDisplay.x-x*p)+(inDisplay.y-y*p)*(inDisplay.y-y*p);
        if(dist_sq>sensitivity*sensitivity) return new PPoint(-1, -1);

        PPoint res = new PPoint(0, 0);
        for(int i=0; i<p; ++i) for(int j=0; j<p; ++j) {
            if(getDisplayed(new PPoint(i, j)).equals(inDisplay)) {
                res = new PPoint(i, j);
                break;
            }
        }


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



    public Set<LineInPlane> getAllLines() {
        Set<LineInPlane> lines = new HashSet<>();
        for(int j=0; j<p; ++j) for(int k=0; k<p; ++k) {
            lines.add(new LineInPlane(new PPoint(0, j), new PPoint(1, k)));
        }
        for(int i=0; i<p; ++i) {
            lines.add(new LineInPlane(new PPoint(i, 0), new PPoint(0, 1)));
        }
        return lines;
    }

    public Set<LineInPlane> getViolatingLines() {
        Set<LineInPlane> violatingLines = new HashSet<>();
        for(LineInPlane l: getAllLines()) {
            if(l.getColourCount(this)>3) {
                violatingLines.add(l);
            }
        }
        return violatingLines;
    }

    @Override
    public String toString() {
        return name;
    }


    public ColouredPlane(ColouredPlane other) {
        p = other.p;
        r = other.r;
        name = other.name;
        displayBase1 = other.displayBase1;
        displayBase2 = other.displayBase2;

        colours = new int[p][p];
        for(int i=0; i<p; ++i) for(int j=0; j<p; ++j) {
            colours[i][j] = other.colours[i][j];
        }
        markedLines = new HashSet<>();

        markedLines.addAll(other.markedLines);
    }

    public void slide1(int amount) {
        amount = (amount+p)%p;
        displayBase1 = new PPoint(displayBase1.x, (displayBase1.y+amount*displayBase1.x)%p);
        displayBase2 = new PPoint(displayBase2.x, (displayBase2.y+amount*displayBase2.x)%p);
    }

    public void slide2(int amount) {
        amount = (amount+p)%p;
        displayBase1 = new PPoint((displayBase1.x+amount*displayBase1.y)%p, displayBase1.y);
        displayBase2 = new PPoint((displayBase2.x+amount*displayBase2.y)%p, displayBase2.y);
    }

    public void offsetBy(PPoint pt) {
        origin = origin.add(pt);
    }

    public void offsetBy(int x, int y) {
        origin = origin.add(new PPoint((x+p)%p, (y+p)%p));
    }

    public void addLine(PPoint a, PPoint b, int colourId) {
        if(a.x<0||a.y<0||b.x<0||b.y<0) return;
        if(a.equals(b)) return;
        markedLines.add(new LineInPlaneWithColour(a, b.subtract(a), colourId));
    }

    public Set<LineInPlaneWithColour> getMarkedLines() {
        return markedLines;
    }

    public void clearMarkedLines() {
        markedLines.clear();
    }

}
