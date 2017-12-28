// Kartankatseluohjelman graafinen käyttöliittymä

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MapDialog extends JFrame {

    private final String SERVER_ADDRESS = "http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1";
    private final String SRS = "EPSG:4326";

    // Kuvan resoluutio ja formaatti

    private final int WIDTH = 960;
    private final int HEIGHT = 480;
    private final String IMAGE_FORMAT = "image/png";
    private final boolean TRANSPARENCY = true;

    // Karttakuvan sijainti

    private int x = 0;
    private int y = 0;
    private int zoom = 90;
    private int offset = 20;

    // Käyttöliittymän komponentit

    private JLabel imageLabel = new JLabel();
    private JPanel leftPanel = new JPanel();

    private List<LayerCheckBox> checkboxes = new ArrayList<>();

    private JButton refreshB = new JButton("Päivitä");
    private JButton resetB = new JButton("Reset");
    private JButton leftB = new JButton("<");
    private JButton rightB = new JButton(">");
    private JButton upB = new JButton("^");
    private JButton downB = new JButton("v");
    private JButton zoomInB = new JButton("+");
    private JButton zoomOutB = new JButton("-");
    

    public MapDialog() throws Exception {

        // Valmistele ikkuna ja lisää siihen komponentit

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // ALLA OLEVAN TESTIRIVIN VOI KORVATA JOLLAKIN MUULLA ERI ALOITUSNäKYMäN
        // LATAAVALLA RIVILLä
        //imageLabel.setIcon(new ImageIcon(new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&BBOX=-180,-90,180,90&SRS=EPSG:4326&WIDTH=953&HEIGHT=480&LAYERS=bluemarble,cities&STYLES=&FORMAT=image/png&TRANSPARENT=true")));

        // haetaan aloitusnäkymä vähemmän vammasesti
        new Konstan_Java_Luoka("bluemarble").run(); // piti laittaa jotain layereihin niin kovakoodasin bluemarble ":D"

        add(imageLabel, BorderLayout.EAST);

        ButtonListener bl = new ButtonListener();
        refreshB.addActionListener(bl);
        resetB.addActionListener(bl);
        leftB.addActionListener(bl);
        rightB.addActionListener(bl);
        upB.addActionListener(bl);
        downB.addActionListener(bl);
        zoomInB.addActionListener(bl);
        zoomOutB.addActionListener(bl);
        
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        leftPanel.setMaximumSize(new Dimension(100, 600));

        addCheckBoxes();

        leftPanel.add(refreshB);
        leftPanel.add(resetB);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(leftB);
        leftPanel.add(rightB);
        leftPanel.add(upB);
        leftPanel.add(downB);
        leftPanel.add(zoomInB);
        leftPanel.add(zoomOutB);

        add(leftPanel, BorderLayout.WEST);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new MapDialog();
    }

    private void addCheckBoxes() {
        String url = SERVER_ADDRESS + "&REQUEST=GetCapabilities";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new URL(url).openStream());

            for (String l : getLayers(doc)) {
                LayerCheckBox b = new LayerCheckBox(l, l, false);
                checkboxes.add(b);
                leftPanel.add(b);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getLayers(Document doc) {
        List<String> list = new ArrayList<>();

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        String query = "/WMT_MS_Capabilities/Capability/Layer/Layer/Name/text()";
        try { 
            XPathExpression expr = xpath.compile(query);
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            // koitin käyttää forEach mut NodeList ei suostunu :(
            for (int i=0; i<nodes.getLength(); i++) {
                list.add(nodes.item(i).getNodeValue());
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Tarkastetaan mitkä karttakerrokset on valittu,
    // tehdään uudesta karttakuvasta pyyntä palvelimelle ja päivitetään kuva
    public void updateImage()  {
        // Tutkitaan, mitkä valintalaatikot on valittu, ja
        // kerätään s:ään pilkulla erotettu lista valittujen kerrosten
        // nimistä (käytetään haettaessa uutta kuvaa)
        String s = String.join(",", checkboxes.stream()
            .filter(cb -> cb.isSelected())
            .map(cb -> cb.getName())
            .collect(Collectors.toList()));

        new Konstan_Java_Luoka(s).run();
    }


    // Kontrollinappien kuuntelija
    // KAIKKIEN NAPPIEN YHTEYDESSä VOINEE HYöDYNTää updateImage()-METODIA
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == refreshB) {
                // :DD
            } else if (e.getSource() == leftB) {
                // VASEMMALLE SIIRTYMINEN KARTALLA
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PäIVITä KUVA
                x = x - offset;     
            } else if (e.getSource() == rightB) {
                // OIKEALLE SIIRTYMINEN KARTALLA
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PäIVITä KUVA
                x = x + offset;
            } else if (e.getSource() == upB) {
                // YLäSPäIN SIIRTYMINEN KARTALLA
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PäIVITä KUVA
                y = y + offset;   
            } else if (e.getSource() == downB) {
                // ALASPäIN SIIRTYMINEN KARTALLA
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PäIVITä KUVA
                y = y - offset; 
            } else if (e.getSource() == zoomInB) {
                // ZOOM IN -TOIMINTO
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PäIVITä KUVA
                zoom = new Double(zoom*0.75).intValue();   
            } else if (e.getSource() == zoomOutB) {
                // ZOOM OUT -TOIMINTO
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PäIVITä KUVA
                zoom = new Double(zoom*1.25).intValue();
            } else if (e.getSource() == resetB) {
                x = 0;
                y = 0;
                zoom = 90;
            } 
            
            updateImage();
        }  
    }

    // Valintalaatikko, joka muistaa karttakerroksen nimen
    private class LayerCheckBox extends JCheckBox {
        private String name = "";

        public LayerCheckBox(String name, String title, boolean selected) {
            super(title, null, selected);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private class Konstan_Java_Luoka extends Thread {
        private String layers;

        public Konstan_Java_Luoka(String s) {
            this.layers = s;
        }

        public void run() {
            int x1 = x - 2 * zoom,
                y1 = y - zoom,
                x2 = x + 2 * zoom,
                y2 = y + zoom;

            String url = SERVER_ADDRESS
                    + "&REQUEST=GetMap"
                    + String.format("&BBOX=%d,%d,%d,%d", x1, y1, x2, y2)
                    + "&SRS=" + SRS
                    + "&WIDTH=" + WIDTH
                    + "&HEIGHT=" + HEIGHT
                    + "&LAYERS=" + layers
                    + "&STYLES="
                    + "&FORMAT=" + IMAGE_FORMAT
                    + "&TRANSPARENT=" + TRANSPARENCY;

            try {
                imageLabel.setIcon(new ImageIcon(new URL(url)));
            } catch (MalformedURLException m) {
                m.printStackTrace();
            }
        }
    }

} // MapDialog
