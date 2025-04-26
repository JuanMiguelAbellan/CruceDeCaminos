import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestPractica extends JFrame {

    private final ArrayList<PreguntaPractica> preguntas = new ArrayList<>();
    private int preguntaActual = 0;

    private final JLabel lblEnunciado = new JLabel("", SwingConstants.CENTER);
    private final JRadioButton[] opciones = new JRadioButton[4];
    private final ButtonGroup grupoOpciones = new ButtonGroup();
    private final JPanel panelOpciones = new JPanel(new GridLayout(4, 1, 10, 10));

    private final JButton anterior = new JButton("Anterior");
    private final JButton siguiente = new JButton("Siguiente");
    private final JButton corregir = new JButton("Corregir");

    public TestPractica(int idAlumno) {
        setTitle("Test Práctica");
        setMinimumSize(new Dimension(800, 600));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
            preguntaActual = indice;
            PreguntaPractica p = preguntas.get(preguntaActual);
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
        }
    }

    private void cargarPreguntasDesdeBD() {
        try (Connection conn = ConexionDB.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PreguntasTest ORDER BY RAND() LIMIT 30");

            while (rs.next()) {
                preguntas.add(new PreguntaPractica(
                        rs.getInt("ID_PreguntaTest"),
                        rs.getString("Pregunta"),
                        rs.getString("Opcion1"),
                        rs.getString("Opcion2"),
                        rs.getString("Opcion3"),
                        rs.getString("Correcta")
                ));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar preguntas: " + e.getMessage());
        }
    }

    private void corregirTest(int idAlumno) {
        int correctas = 0;

        try (Connection conn = ConexionDB.getConnection()) {

            for (int i = 0; i < preguntas.size(); i++) {
                PreguntaPractica pregunta = preguntas.get(i);

                String respuestaSeleccionada = null;
                for (JRadioButton opcion : opciones) {
                    if (opcion.isSelected()) {
                        respuestaSeleccionada = opcion.getText();
                        break;
                    }
                }

                if (respuestaSeleccionada != null && respuestaSeleccionada.equals(pregunta.correcta)) {
                    correctas++;
                } else {
                    // Insertar fallo en la base de datos
                    String sql = "INSERT INTO Fallos (ID_Pregunta, ID_Alumno, Fecha) VALUES (?, ?, CURRENT_DATE())";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, pregunta.id);
                        stmt.setInt(2, idAlumno);
                        stmt.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar fallos: " + e.getMessage());
        }

        JOptionPane.showMessageDialog(this,
                "Test finalizado.\nRespuestas correctas: " + correctas + " de " + preguntas.size(),
                "Resultado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
