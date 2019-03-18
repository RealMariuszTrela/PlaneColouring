package uk.ac.cam.mjt203.PlaneColouring;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.IOException;

public class GUI extends JFrame implements ListSelectionListener {
    private PlanePanel mainPanel;
    private ColouredPlaneStore store;
    private ColourPickingPanel picker;

    public GUI(ColouredPlaneStore store) {
        super("Plane colouring");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024,768);
        this.store = store;

        add(createColourPickingPanel(), BorderLayout.SOUTH);
        add(createColouringsPanel(), BorderLayout.WEST);
        add(createMainPanel(picker), BorderLayout.CENTER);


        mainPanel.display(new ColouredPlane(5, ":P"));
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
        picker = new ColourPickingPanel();
        picker.setMinimumSize(new Dimension(100, 100));
        return picker;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList<ColouredPlane> list = (JList<ColouredPlane>) e.getSource();
        ColouredPlane p = list.getSelectedValue();
        mainPanel.display(p);
    }

    public static void main(String[] args) throws IOException {
        ColouredPlaneStore store = new ColouredPlaneStore("/sampleplanes");
        GUI gui = new GUI(store);
        gui.setVisible(true);
    }


}
