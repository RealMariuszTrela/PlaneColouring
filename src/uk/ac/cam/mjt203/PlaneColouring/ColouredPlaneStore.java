package uk.ac.cam.mjt203.PlaneColouring;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class ColouredPlaneStore {

    private List<ColouredPlane> planes = new LinkedList<>();

    public ColouredPlaneStore(String source) throws IOException {
        if (source.startsWith("http://") || source.startsWith("https://")) {
            loadFromURL(source);
        }
        else {
            loadFromDisk(source);
        }
    }

    public ColouredPlaneStore(Reader source) throws IOException {
        load(source);
    }

    private void load(Reader r) throws IOException {
        BufferedReader b = new BufferedReader(r);
        String line;
        while ( (line = b.readLine()) != null) {
            ColouredPlane p;
            p = new ColouredPlane(line);
            planes.add(p);
        }
    }


    private void loadFromURL(String url) throws IOException {
        URL destination = new URL(url);
        URLConnection conn = destination.openConnection();
        Reader r = new InputStreamReader(conn.getInputStream());
        load(r);
    }

    private void loadFromDisk(String filename) throws IOException {
        Reader r = new InputStreamReader(getClass().getResourceAsStream(filename));
        load(r);
    }

    public List<ColouredPlane> getPatternsNameSorted() {
        List<ColouredPlane> result = new ArrayList<>(planes);
        Collections.sort(result);
        return result;
    }
}
