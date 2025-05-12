import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TestPractica extends JFrame {
    private final JFrame ventanaAnterior;



    private final ArrayList<Pregunta> preguntas = new ArrayList<>();
    private int preguntaActual = 0;

    private final JLabel lblEnunciado = new JLabel("", SwingConstants.CENTER);
    private final JRadioButton[] opciones = new JRadioButton[4];
    private final ButtonGroup grupoOpciones = new ButtonGroup();
    private final JPanel panelOpciones = new JPanel(new GridLayout(4, 1, 10, 10));

    private final JButton volver = new JButton("Volver");
    private final JButton anterior = new JButton("Anterior");
    private final JButton siguiente = new JButton("Siguiente");
    private final JButton corregir = new JButton("Corregir");

    public TestPractica(JFrame ventanaAnterior, int idAlumno) {
        this.ventanaAnterior = ventanaAnterior;
        setTitle("Test Práctica");
        setMinimumSize(new Dimension(800, 600));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Para que al cerrar vueva a ventana alumno
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (ventanaAnterior != null) {
                    ventanaAnterior.setVisible(true);
                }
            }
        });

        setLayout(new BorderLayout());

        cargarPreguntasDesdeBD();

        lblEnunciado.setFont(new Font("Arial", Font.BOLD, 20));
        lblEnunciado.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(lblEnunciado, BorderLayout.NORTH);

        // Centramos opciones usando un panel de apoyo
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        for (int i = 0; i < 4; i++) {
            opciones[i] = new JRadioButton();
            opciones[i].setFont(new Font("Arial", Font.PLAIN, 18));
            grupoOpciones.add(opciones[i]);
            panelOpciones.add(opciones[i]);
        }
        panelCentro.add(panelOpciones);
        add(panelCentro, BorderLayout.CENTER);

        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        anterior.setPreferredSize(new Dimension(120, 40));
        siguiente.setPreferredSize(new Dimension(120, 40));
        corregir.setPreferredSize(new Dimension(120, 40));
        anterior.addActionListener(e -> mostrarPregunta(preguntaActual - 1));
        siguiente.addActionListener(e -> mostrarPregunta(preguntaActual + 1));
        corregir.addActionListener(e -> corregirTest(idAlumno));
        panelNavegacion.add(anterior);
        panelNavegacion.add(siguiente);
        panelNavegacion.add(corregir);

        JPanel panelNumeros = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        for (int i = 0; i < preguntas.size(); i++) {
            int num = i;
            JButton btn = new JButton(String.valueOf(i + 1));
            btn.addActionListener(e -> mostrarPregunta(num));
            panelNumeros.add(btn);
        }

        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.add(panelNavegacion, BorderLayout.NORTH);
        panelBotones.add(panelNumeros, BorderLayout.SOUTH);

        add(panelBotones, BorderLayout.SOUTH);

        if (!preguntas.isEmpty()) {
            mostrarPregunta(0);
        }

        setVisible(true);
    }

    private void mostrarPregunta(int indice) {
        if (indice >= 0 && indice < preguntas.size()) {

            // Antes de cambiar, guardamos la respuesta actual
            guardarRespuestaSeleccionada();

            preguntaActual = indice;
            Pregunta p = preguntas.get(preguntaActual);
            lblEnunciado.setText("Pregunta " + (preguntaActual + 1) + ": " + p.enunciado);

            List<String> opcionesMezcladas = new ArrayList<>();
            opcionesMezcladas.add(p.correcta);
            opcionesMezcladas.add(p.opcion1);
            opcionesMezcladas.add(p.opcion2);
            opcionesMezcladas.add(p.opcion3);
            Collections.shuffle(opcionesMezcladas);

            for (int i = 0; i < 4; i++) {
                opciones[i].setText(opcionesMezcladas.get(i));
            }

            grupoOpciones.clearSelection();

            if (p.respuestaSeleccionada != null) {
                for (JRadioButton opcion : opciones) {
                    if (opcion.getText().equals(p.respuestaSeleccionada)) {
                        opcion.setSelected(true);
                        break;
                    }
                }
            }
        }
    }

    private void guardarRespuestaSeleccionada() {
        if (preguntaActual >= 0 && preguntaActual < preguntas.size()) {
            Pregunta pregunta = preguntas.get(preguntaActual);
            String seleccion = null;

            for (JRadioButton opcion : opciones) {
                if (opcion.isSelected()) {
                    seleccion = opcion.getText();
                    break;
                }
            }

            pregunta.respuestaSeleccionada = seleccion;

            if (seleccion != null) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(".respuestas_usuario.txt", true))) {
                    bw.write(pregunta.id + ":" + seleccion);
                    bw.newLine();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al guardar respuesta del usuario");
                }
            }
        }
    }

    private void cargarPreguntasDesdeBD() {
        try (Connection conn = ConexionDB.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PreguntasTest ORDER BY RAND() LIMIT 30");

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(".respuestas_correctas.txt"))) {
                while (rs.next()) {
                    Pregunta pregunta = new Pregunta(
                            rs.getInt("ID_PreguntaTest"),
                            rs.getString("Pregunta"),
                            rs.getString("Opcion1"),
                            rs.getString("Opcion2"),
                            rs.getString("Opcion3"),
                            rs.getString("Correcta")
                    );
                    preguntas.add(pregunta);
                    bw.write(pregunta.id + ":" + pregunta.correcta);
                    bw.newLine();
                }
            }

        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar preguntas: " + e.getMessage());
        }
    }

    private void corregirTest(int idAlumno) {
        guardarRespuestaSeleccionada(); // guardar la última

        Map<Integer, String> mapaCorrectas = new HashMap<>();
        Map<Integer, String> mapaUsuario = new HashMap<>();

        try {
            // Leer respuestas correctas
            try (BufferedReader br = new BufferedReader(new FileReader(".respuestas_correctas.txt"))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(":", 2);
                    mapaCorrectas.put(Integer.parseInt(partes[0]), partes[1]);
                }
            }

            // Leer respuestas del usuario
            try (BufferedReader br = new BufferedReader(new FileReader(".respuestas_usuario.txt"))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(":", 2);
                    mapaUsuario.put(Integer.parseInt(partes[0]), partes[1]);
                }
            }

            int correctas = 0;
            try (Connection conn = ConexionDB.getConnection()) {
                for (Map.Entry<Integer, String> entrada : mapaCorrectas.entrySet()) {
                    int id = entrada.getKey();
                    String correcta = entrada.getValue();
                    String usuario = mapaUsuario.getOrDefault(id, "");

                    if (usuario.equals(correcta)) {
                        correctas++;
                    } else {
                        try (PreparedStatement stmt = conn.prepareStatement(
                                "INSERT INTO Fallos (ID_Pregunta, ID_Alumno, Fecha) VALUES (?, ?, CURRENT_DATE())")) {
                            stmt.setInt(1, id);
                            stmt.setInt(2, idAlumno);
                            stmt.executeUpdate();
                        }
                    }
                }
            }

            int enBlanco = mapaCorrectas.size() - mapaUsuario.size();

            JOptionPane.showMessageDialog(this,
                    "Test finalizado.\nRespuestas correctas: " + correctas + " de " + preguntas.size()
                            + "\nPreguntas sin responder: " + enBlanco,
                    "Resultado",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al corregir: " + e.getMessage());
        }

        //opcional
        new File(".respuestas_correctas.txt").delete();
        new File(".respuestas_usuario.txt").delete();

        dispose();
    }



}
