import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;


public class ManiobraAlumno extends JFrame{
    private final JFrame ventanaAnterior;

    private final JLabel lblAlumno = new JLabel("Selecciona el alumno: ", SwingConstants.CENTER);
    private final JComboBox listaAlumnos = new JComboBox();

    private final JLabel lblManiobra = new JLabel("Maniobra: ");
    private final JTextField campoManiobra= new JTextField();
    private final JLabel lblComentario = new JLabel("Comentario: ");
    private final JTextField campoComentario= new JTextField();

    private final JButton botonGuardar=new JButton("Guardar");
    private final JButton botonCancelar= new JButton("Cancelar");

    private final int idProfesor;

    public ManiobraAlumno(JFrame ventanaAnterior, int idProfesor){
        this.ventanaAnterior = ventanaAnterior;
        this.idProfesor = idProfesor;
        setTitle("Añadir maniobra");
        setMinimumSize(new Dimension(800, 600));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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

        cargarAlumnos();

        lblAlumno.setFont(new Font("Arial", Font.BOLD, 20));
        lblAlumno.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        listaAlumnos.setFont(new Font("Arial", Font.BOLD, 20));
        listaAlumnos.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(lblAlumno, BorderLayout.WEST);
        panelSuperior.add(listaAlumnos, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc= new GridBagConstraints();

        gbc.gridx=0;
        gbc.gridy=0;
        panelCentral.add(lblManiobra, gbc);

        campoManiobra.setPreferredSize(new Dimension(100, 30));
        gbc.gridx=1;
        gbc.gridy=0;
        panelCentral.add(campoManiobra, gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        panelCentral.add(lblComentario, gbc);

        campoComentario.setPreferredSize(new Dimension(100, 30));
        gbc.gridx=1;
        gbc.gridy=1;
        panelCentral.add(campoComentario, gbc);

        botonGuardar.addActionListener(e-> insertarManiobra((String) listaAlumnos.getSelectedItem()));
        gbc.gridx=0;
        gbc.gridy=2;
        panelCentral.add(botonGuardar, gbc);

        botonCancelar.addActionListener(e-> cancelar());
        gbc.gridx=0;
        gbc.gridy=2;
        panelCentral.add(botonCancelar, gbc);

        add(panelCentral, BorderLayout.NORTH);

        setVisible(true);
    }

    public void cargarAlumnos(){
        try (Connection conn = ConexionDB.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Alumno where Tipo='practico'");


            while (rs.next()) {
                listaAlumnos.addItem(rs.getString(3));
            }


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar profesores: " + e.getMessage());
        }
    }

    public void insertarManiobra(String nombreAlumno){
        int idAlumno = 0;
        try (Connection conn = ConexionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT ID_Alumno FROM Alumno WHERE Nombre = ?");
            stmt.setString(1, nombreAlumno);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idAlumno = rs.getInt(1);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ID del alumno: " + e.getMessage());
            return;
        }


        try (Connection conn = ConexionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Resultados_Practicos (ID_Alumno, ID_Profesor, Comentario, Maniobra) VALUES (?, ?, ?, ?)");
            stmt.setInt(1, idAlumno);
            stmt.setInt(2, this.idProfesor);
            stmt.setString(3, campoComentario.getText());
            stmt.setInt(4, Integer.parseInt(campoManiobra.getText()));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Maniobra insertada con éxito.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la maniobra: " + e.getMessage());
        }
    }
    public void cancelar(){
        campoManiobra.setText("");
        campoComentario.setText("");
    }
}
