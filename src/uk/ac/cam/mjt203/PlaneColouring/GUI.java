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
    private PlaneTranslatePanel translator;
    private Map<String, ColouredPlane> editedPlanes = new HashMap<>();
    private Map<String, ColouredPlane> originalPlanes = new HashMap<>();
    private String lang;
    private JLabel instruction;


    public GUI(ColouredPlaneStore store, String lang) {
        super(LanguageTranslator.getTranslation(lang,"Plane colouring"));
        this.lang = lang;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920,1080);
        this.store = store;

        add(createColourPickingPanel(), BorderLayout.SOUTH);
        add(createColouringsPanel(), BorderLayout.WEST);
        add(createMainPanel(picker), BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.EAST);
        add(createInstruction(), BorderLayout.NORTH);

        addKeyListener(picker);
        addKeyListener(shuffler);
        addKeyListener(translator);
        setFocusable(true);
        requestFocusInWindow();
    }

    private void addBorder(JComponent component, String title) {
        Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border tb = BorderFactory.createTitledBorder(etch,title);
        component.setBorder(tb);
    }
    private PlanePanel createMainPanel(ColourPickingPanel picker) {
        mainPanel = new PlanePanel(this, picker);
        addBorder(mainPanel, LanguageTranslator.getTranslation(lang, "Plane"));
        return mainPanel;
    }
    private JPanel createColouringsPanel() {
        JPanel colouringsPanel = new JPanel();
        addBorder(colouringsPanel, LanguageTranslator.getTranslation(lang,"Patterns"));
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
        sidePanel.setLayout(new GridLayout(4, 1));
        JButton button1 = new JButton(LanguageTranslator.getTranslation(lang,"Restore original"));
        button1.addActionListener(e -> restore());
        button1.setFocusable(false);
        sidePanel.add(button1);

        shuffler = new PlaneShufflerPanel(this, workingPlane);
        sidePanel.add(shuffler);

        translator = new PlaneTranslatePanel(this, workingPlane);
        sidePanel.add(translator);

        JButton button2 = new JButton(LanguageTranslator.getTranslation(lang,"Clear all added lines"));

        button2.addActionListener(e -> {workingPlane.clearMarkedLines(); update();});
        button2.setFocusable(false);
        sidePanel.add(button2);

        return sidePanel;
    }

    private JLabel createInstruction() {
        instruction = new JLabel("<html> Program do wizualizowania płaszczyzny F_p^2 i kolorowań na niej. Kolorowanie do wyświetlenia można wybrać na liście po lewej stronie. " +
                "Kliknięcie na punkt lewym przyciskiem myszki nadaje mu kolor. Kliknięcie prawym przyciskiem myszki usuwa kolor. " +
                "Kolor można wybrać naciskając na przyciski na spodzie, lub naciskając klawisze 0-9. "+
                "Przeciągnięcie myszki z jednego punktu na drugi dodaje prostą. Przyciski na panelu \"Przesuń\" przesuwają kolorowanie. " +
                "Przyciski na panelu \"Pochyl\" \"pochylają\". Zamiast naciskania przycisków na panelach \"Przesuń\" i \"Pochyl\" można używać odpowiednio strzałek " +
                "i klawiszy WASD. Prosta zawierająca więcej niż 3 kolory jest podkreślana na czerwono. </html>");
        return instruction;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList<ColouredPlane> list = (JList<ColouredPlane>) e.getSource();
        workingPlane = new ColouredPlane(list.getSelectedValue());
        if(editedPlanes.containsKey(workingPlane.toString())) {
            workingPlane = editedPlanes.get(workingPlane.toString());
        }
        else {
            editedPlanes.put(workingPlane.toString(), workingPlane);
            originalPlanes.put(workingPlane.toString(), list.getSelectedValue());
        }
       update();
        requestFocusInWindow();
    }

    public void update() {
        mainPanel.display(workingPlane);
        shuffler.setPlane(workingPlane);
        translator.setPlane(workingPlane);
        requestFocusInWindow();
    }

    private void restore() {
        if(workingPlane == null) return;
        workingPlane = new ColouredPlane(originalPlanes.get(workingPlane.toString()));
        editedPlanes.put(workingPlane.toString(), workingPlane);
        mainPanel.display(workingPlane);
    }

    public String getLanguage() {
        return lang;
    }

    public static void main(String[] args) throws IOException {
        ColouredPlaneStore store = new ColouredPlaneStore("/sampleplanes");
        GUI gui = new GUI(store, "pl");
        gui.setVisible(true);
    }


}
