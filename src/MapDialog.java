// Kartankatseluohjelman graafinen käyttöliittymä

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

public class MapDialog extends JFrame {

    private String SERVER_ADDRESS = "http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1";
    private String SRS = "EPSG:4326";

    // Kuvan resoluutio ja formaatti

    private int WIDTH = 953;
    private int HEIGHT = 480;
    private String IMAGE_FORMAT = "image/png";
    private boolean TRANSPARENCY = true;

    // Karttakuvan sijainti

    private int x = 0;
    private int y = 0;
    private int zoom = 90;
    private int offset = 20;

    // Käyttöliittymän komponentit

    private JLabel imageLabel = new JLabel();
    private JPanel leftPanel = new JPanel();

    private JButton refreshB = new JButton("Päivitä");
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
        leftB.addActionListener(bl);
        rightB.addActionListener(bl);
        upB.addActionListener(bl);
        downB.addActionListener(bl);
        zoomInB.addActionListener(bl);
        zoomOutB.addActionListener(bl);

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        leftPanel.setMaximumSize(new Dimension(100, 600));

        // TODO:
        // ALLA OLEVIEN KOLMEN TESTIRIVIN TILALLE SILMUKKA JOKA LISää KäYTTöLIITTYMääN
        // KAIKKIEN XML-DATASTA HAETTUJEN KERROSTEN VALINTALAATIKOT MALLIN MUKAAN
        leftPanel.add(new LayerCheckBox("bluemarble", "Maapallo", true));
        leftPanel.add(new LayerCheckBox("cities", "Kaupungit", false));

        leftPanel.add(refreshB);
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


    /**
     * lähetä getCapabilities pyyntö palvelimelle ja parsi XML:stä layerit
     * public String[] getCapabilities(server){}
     */

    // Tarkastetaan mitkä karttakerrokset on valittu,
    // tehdään uudesta karttakuvasta pyyntä palvelimelle ja päivitetään kuva
    public void updateImage()  {
        String s = "";
        
        // Tutkitaan, mitkä valintalaatikot on valittu, ja
        // kerätään s:ään pilkulla erotettu lista valittujen kerrosten
        // nimistä (käytetään haettaessa uutta kuvaa)
        Component[] components = leftPanel.getComponents();
        for (Component com : components) {
            if (com instanceof LayerCheckBox)
                if (((LayerCheckBox) com).isSelected()) s = s + com.getName() + ",";
        }
        if (s.endsWith(",")) s = s.substring(0, s.length() - 1);
       
            new Konstan_Java_Luoka(s).run();
           
        }
        

    // Kontrollinappien kuuntelija
    // KAIKKIEN NAPPIEN YHTEYDESSä VOINEE HYöDYNTää updateImage()-METODIA
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == refreshB) {
                updateImage();
            }
            else if (e.getSource() == leftB) {
                // TODO:
                // VASEMMALLE SIIRTYMINEN KARTALLA
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PäIVITä KUVA
                x = x - offset;
                updateImage();
            }
            else if (e.getSource() == rightB) {
                // TODO:
                // OIKEALLE SIIRTYMINEN KARTALLA
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PäIVITä KUVA
                x = x + offset;
                updateImage();

            }
            else if (e.getSource() == upB) {
                // TODO:
                // YLäSPäIN SIIRTYMINEN KARTALLA
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PäIVITä KUVA
                y = y + offset;
                updateImage();
            }
            else if (e.getSource() == downB) {
                // TODO:
                // ALASPäIN SIIRTYMINEN KARTALLA
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PäIVITä KUVA
                y = y - offset;
                updateImage();
            }
            else if (e.getSource() == zoomInB) {
                // TODO:
                // ZOOM IN -TOIMINTO
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PäIVITä KUVA
                zoom = new Double(zoom*0.75).intValue();
                updateImage();
            }
            else if (e.getSource() == zoomOutB) {
                // TODO:
                // ZOOM OUT -TOIMINTO
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PäIVITä KUVA
                zoom = new Double(zoom*1.25).intValue();
                updateImage();
            }
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
