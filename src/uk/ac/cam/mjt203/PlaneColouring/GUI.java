package uk.ac.cam.mjt203.PlaneColouring;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GUI extends JFrame implements ListSelectionListener {
    private PlanePanel mainPanel;
    private ColouredPlaneStore store;
    private ColourPickingPanel picker;
    private ColouredPlane workingPlane;
    private JPanel sidePanel;
    private PlaneShufflerPanel shuffler;
    private Map<String, ColouredPlane> editedPlanes = new HashMap<>();
    private Map<String, ColouredPlane> originalPlanes = new HashMap<>();


    public GUI(ColouredPlaneStore store) {
        super("Plane colouring");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024,768);
        this.store = store;

        add(createColourPickingPanel(), BorderLayout.SOUTH);
        add(createColouringsPanel(), BorderLayout.WEST);
        add(createMainPanel(picker), BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.EAST);
    }

    private void addBorder(JComponent component, String title) {
        Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border tb = BorderFactory.createTitledBorder(etch,title);
        component.setBorder(tb);
    }
    private PlanePanel createMainPanel(ColourPickingPanel picker) {
        mainPanel = new PlanePanel(picker);
        addBorder(mainPanel, "Plane");
        return mainPanel;
    }
    private JPanel createColouringsPanel() {
        JPanel colouringsPanel = new JPanel();
        addBorder(colouringsPanel, "Patterns");
        colouringsPanel.setLayout(new BorderLayout());
        JList<ColouredPlane> patterns = new JList<>(store.getPatternsNameSorted().toArray(new ColouredPlane[0]));
        patterns.addListSelectionListener(this);

        JScrollPane scroll = new JScrollPane(patterns);
        colouringsPanel.add(scroll, BorderLayout.CENTER);
        return colouringsPanel;
    }

    private JPanel createColourPickingPanel() {
        picker = new ColourPickingPanel(this);
        picker.setMinimumSize(new Dimension(100, 100));
        return picker;
    }

    private JPanel createSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(3, 1));
        JButton button = new JButton("Restore original");
        button.addActionListener(e -> restore());
        sidePanel.add(button);

        shuffler = new PlaneShufflerPanel(this, workingPlane);
        sidePanel.add(shuffler);

        return sidePanel;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList<ColouredPlane> list = (JList<ColouredPlane>) e.getSource();
        workingPlane = list.getSelectedValue().clone();
        if(editedPlanes.containsKey(workingPlane.toString())) {
            workingPlane = editedPlanes.get(workingPlane.toString());
        }
        else {
            editedPlanes.put(workingPlane.toString(), workingPlane);
            originalPlanes.put(workingPlane.toString(), list.getSelectedValue());
        }
        mainPanel.display(workingPlane);
        shuffler.setPlane(workingPlane);
    }

    public void update() {
        mainPanel.repaint();
        shuffler.setPlane(workingPlane);
    }

    private void restore() {
        if(workingPlane == null) return;
        workingPlane = originalPlanes.get(workingPlane.toString()).clone();
        editedPlanes.put(workingPlane.toString(), workingPlane);
        mainPanel.display(workingPlane);
    }

    public static void main(String[] args) throws IOException {
        ColouredPlaneStore store = new ColouredPlaneStore("/sampleplanes");
        GUI gui = new GUI(store);
        gui.setVisible(true);
    }


}
