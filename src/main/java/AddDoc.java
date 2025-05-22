import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddDoc extends JFrame {
    private final JLabel lblArchivos= new JLabel("XML o CSV");
    private final JFileChooser fileChooser= new JFileChooser();

    public AddDoc(JFrame ventanaAnterior) {
        setTitle("Añadir archivo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (ventanaAnterior != null) {
                    ventanaAnterior.setVisible(true);
                }
            }
        });

        // Fondo con imagen
        JPanel panelConFondo = new JPanel() {
            Image fondo = new ImageIcon(getClass().getClassLoader().getResource("fondo.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (fondo != null) {
                    g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        panelConFondo.setLayout(new GridBagLayout());
        setContentPane(panelConFondo);


        fileChooser.addActionListener(e -> {
            if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                File archivo = fileChooser.getSelectedFile();
                String tipo = detectarTipoArchivo(archivo);

                switch (tipo) {
                    case "CSV":
                        insertarCSV(archivo);
                        break;
                    case "XML":
                        insertarXML(archivo);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Tipo de archivo no soportado.");
                }
            } else if (e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
                ventanaAnterior.setVisible(true);
                dispose();
            }
        });


        add(fileChooser);

        setVisible(true);
    }
    private String detectarTipoArchivo(File archivo) {
        String nombre = archivo.getName().toLowerCase();
        if (nombre.endsWith(".csv")) {
            return "CSV";
        } else if (nombre.endsWith(".xml")) {
            return "XML";
        } else {
            return "DESCONOCIDO";
        }
    }
    private void insertarCSV(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");

                try (Connection conn = ConexionDB.getConnection()) {
                    String sql = "INSERT INTO PreguntasTest(Pregunta, Correcta, Opcion1, Opcion2, Opcion3) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, datos[0]);
                    stmt.setString(2, datos[1]);
                    stmt.setString(3, datos[2]);
                    stmt.setString(4, datos[3]);
                    stmt.setString(5, datos[4]);
                    stmt.executeUpdate();
                }
            }

            JOptionPane.showMessageDialog(this, "Archivo CSV importado con éxito.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar CSV: " + e.getMessage());
        }
    }
    private void insertarXML(File archivo) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivo);
            doc.getDocumentElement().normalize();

            NodeList lista = doc.getElementsByTagName("Preguntas"); // Cambia "registro" por tu etiqueta raíz

            for (int i = 0; i < lista.getLength(); i++) {
                Node nodo = lista.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;

                    String campo1 = elemento.getElementsByTagName("pregunta").item(0).getTextContent();
                    String campo2 = elemento.getElementsByTagName("correcta").item(0).getTextContent();
                    String campo3 = elemento.getElementsByTagName("opcion1").item(0).getTextContent();
                    String campo4 = elemento.getElementsByTagName("opcion2").item(0).getTextContent();
                    String campo5 = elemento.getElementsByTagName("opcion3").item(0).getTextContent();

                    try (Connection conn = ConexionDB.getConnection()) {
                        String sql = "INSERT INTO PreguntasTest(Pregunta, Correcta, Opcion1, Opcion2, Opcion3) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, campo1);
                        stmt.setString(2, campo2);
                        stmt.setString(3, campo3);
                        stmt.setString(4, campo4);
                        stmt.setString(5, campo5);
                        stmt.executeUpdate();
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Archivo XML importado con éxito.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar XML: " + e.getMessage());
        }
    }

}
