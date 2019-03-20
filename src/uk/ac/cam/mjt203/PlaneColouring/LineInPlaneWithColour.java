package uk.ac.cam.mjt203.PlaneColouring;

public class LineInPlaneWithColour extends  LineInPlane{
    public int colourId;
    public LineInPlaneWithColour(ColouredPlane.PPoint start, ColouredPlane.PPoint step, int colourId) {
        super(start, step);
        this.colourId = colourId;
    }

    public static LineInPlaneWithColour lineFromString(ColouredPlane plane, String format)
    {
        String[] split = format.split(" ");
        ColouredPlane.PPoint start = plane.new PPoint(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        ColouredPlane.PPoint step = plane.new PPoint(Integer.parseInt(split[2]), Integer.parseInt(split[3]));
        int colourId = Integer.parseInt(split[4]);
        return new LineInPlaneWithColour(start, step, colourId);

    }
}
