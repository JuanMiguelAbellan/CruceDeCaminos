import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveUser extends JFrame {
    private final JLabel lblDni = new JLabel("DNI:");
    private final JTextField campoDni = new JTextField();

    private final JLabel lblRol = new JLabel("Rol:");
    private final JComboBox<String> rol = new JComboBox<>();

    private final JButton botonEliminar = new JButton("Eliminar");

    public RemoveUser(JFrame ventanaAnterior) {
        setTitle("Eliminar usuario");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 200);
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

        rol.addItem("Administrador");
        rol.addItem("Profesor");
        rol.addItem("Alumno");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelConFondo.add(lblDni, gbc);

        gbc.gridx = 1;
        panelConFondo.add(campoDni, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panelConFondo.add(lblRol, gbc);

        gbc.gridx = 1;
        panelConFondo.add(rol, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panelConFondo.add(botonEliminar, gbc);

        botonEliminar.addActionListener(e -> {
            String dni = campoDni.getText();
            String rolSeleccionado = (String) rol.getSelectedItem();

            if (!usuarioExiste(dni, rolSeleccionado)) {
                JOptionPane.showMessageDialog(null, "El usuario no existe.");
            } else {
                eliminarUsuario(dni, rolSeleccionado);
            }
        });

        setVisible(true);
    }

    private boolean usuarioExiste(String dni, String rol) {
        String tabla;
        switch (rol) {
            case "Alumno":
                tabla = "Alumno";
                break;
            case "Profesor":
                tabla = "Profesor";
                break;
            case "Administrador":
                tabla = "Administrador";
                break;
            default:
                return false;
        }

        String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE DNI = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al comprobar usuario: " + e.getMessage());
        }

        return false;
    }

    private void eliminarUsuario(String dni, String rol) {
        String tabla;
        switch (rol) {
            case "Alumno":
                tabla = "Alumno";
                break;
            case "Profesor":
                tabla = "Profesor";
                break;
            case "Administrador":
                tabla = "Administrador";
                break;
            default:
                JOptionPane.showMessageDialog(null, "Rol no válido.");
                return;
        }

        try (Connection conn = ConexionDB.getConnection()) {
            // Obtener el nombre de usuario para borrarlo de la tabla Usuarios
            String obtenerUsuario = "SELECT Username FROM Usuarios WHERE Rol = ? AND DNI = ?";
            String username = null;

            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT Username FROM Usuarios u JOIN " + tabla + " t ON u.Rol = ? AND t.DNI = ?")) {
                stmt.setString(1, rol.toLowerCase());
                stmt.setString(2, dni);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    username = rs.getString("Username");
                }
            }

            if (username != null) {
                // Eliminar de tabla específica
                PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM " + tabla + " WHERE DNI = ?");
                stmt1.setString(1, dni);
                stmt1.executeUpdate();

                // Eliminar de tabla Usuarios
                PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM Usuarios WHERE Username = ?");
                stmt2.setString(1, username);
                stmt2.executeUpdate();

                JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo encontrar el usuario en la tabla Usuarios.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage());
        }
    }
}

