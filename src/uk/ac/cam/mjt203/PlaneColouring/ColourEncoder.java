package uk.ac.cam.mjt203.PlaneColouring;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColourEncoder {
    private static Map<Integer, Color> colourEncoding = new HashMap<>();
    static {
        colourEncoding.put(-1, Color.GRAY);
        colourEncoding.put(0, Color.DARK_GRAY);
    }
    private static Color getColourOfId(int id) {
        float hue = ((133*id)%231)/231.0f;
        float saturation = 0.5f+((81*id)%245)/(2*245.0f);
        float brightness = 0.5f+(183*id%239)/(2*239.0f);
        return Color.getHSBColor(hue, saturation, brightness);
    }

    private static float distance_sq(Color a, Color b) {
        float[] c = Color.RGBtoHSB(a.getRed(), a.getGreen(), a.getBlue(), null);
        float[] d = Color.RGBtoHSB(b.getRed(), b.getGreen(), b.getBlue(), null);
        return 9*(c[0]-d[0])*(c[0]-d[0])+(c[1]-d[1])*(c[1]-d[1])+(c[2]-d[2])*(c[2]-d[2]);
    }

    public static Color getColour(int id) {
        if(!colourEncoding.containsKey(id)) {
            float bestDist = 0;
            int bestId = id;
            for(int i=id; i<=id+100*colourEncoding.size(); ++i) {
                float minDist = Float.MAX_VALUE;
                for(Color c: colourEncoding.values()) {
                    minDist = Math.min(minDist, distance_sq(c, getColourOfId(i)));
                }
                if(minDist > bestDist) {
                    bestDist = minDist;
                    bestId = i;
                }
            }
            colourEncoding.put(id, getColourOfId(bestId));
        }
        return colourEncoding.get(id);
    }
}
