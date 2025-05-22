import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.sql.*;

public class AddUser extends JFrame {
    private final JLabel lblNombre= new JLabel("Nombre:");
    private final JTextField campoNombre = new JTextField();

    private final JLabel lblApellido= new JLabel("Apellido:");
    private final JTextField campoApellido = new JTextField();

    private final JLabel lblDni= new JLabel("DNI:");
    private final JTextField campoDni = new JTextField();

    private final JLabel lblRol = new JLabel("Rol: ");
    private final JComboBox<String> rol= new JComboBox<>();

    private final JButton botonEnviar= new JButton("Enviar");

    public AddUser(JFrame ventanaAnterior) {
        setTitle("Añadir usuario");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 350);
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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        panelConFondo.add(lblNombre, gbc);

        gbc.gridx = 1;
        panelConFondo.add(campoNombre, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panelConFondo.add(lblApellido, gbc);

        gbc.gridx = 1;
        panelConFondo.add(campoApellido, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panelConFondo.add(lblDni, gbc);

        gbc.gridx = 1;
        panelConFondo.add(campoDni, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panelConFondo.add(lblRol, gbc);

        rol.addItem("Administrador");
        rol.addItem("Profesor");
        rol.addItem("Alumno");

        gbc.gridx = 1;
        panelConFondo.add(rol, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panelConFondo.add(botonEnviar, gbc);

        setVisible(true);

        botonEnviar.addActionListener(e -> {
            String dni = campoDni.getText();
            String rolElegido = (String) rol.getSelectedItem();
            if (comprobarUsuario(rolElegido, dni)) {
                JOptionPane.showMessageDialog(null, "Este usuario ya existe.");
            }else {
                switch (rolElegido) {
                    case "Alumno":
                        registrarAlumno();
                        break;
                    case "Profesor":
                        registrarProfesor();
                        break;
                    case "Administrador":
                        registrarAdministrador();
                        break;
                }
            }
        });

    }
    public boolean comprobarUsuario(String rol, String dni) {

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

        try (Connection conn = ConexionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dni);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al comprobar usuario: " + e.getMessage());
        }
        return false;
    }

    private void registrarAlumno() {
        JTextField txtUsuario = new JTextField();
        JPasswordField txtContrasena = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre de usuario:"));
        panel.add(txtUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtContrasena);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Ingrese sus datos",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String usuario = txtUsuario.getText();
            String contrasena = new String(txtContrasena.getPassword());
            String rolElegido = (String) rol.getSelectedItem();

            if(comprobarNombreUsuario(usuario)){
                JOptionPane.showMessageDialog(null, "Este nombre de usuario ya esta en uso.");
            }else {
                insertarNuevoUsuario(usuario, contrasena, rolElegido);
            }
        } else {
            System.out.println("Operación cancelada");
        }
    }

    private void registrarProfesor() {
        JTextField txtUsuario = new JTextField();
        JPasswordField txtContrasena = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre de usuario:"));
        panel.add(txtUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtContrasena);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Ingrese sus datos",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String usuario = txtUsuario.getText();
            String contrasena = new String(txtContrasena.getPassword());
            String rolElegido = (String) rol.getSelectedItem();

            if(comprobarNombreUsuario(usuario)){
                JOptionPane.showMessageDialog(null, "Este nombre de usuario ya esta en uso.");
            }else {
                insertarNuevoUsuario(usuario, contrasena, rolElegido);
            }
        } else {
            System.out.println("Operación cancelada");
        }
    }

    private void registrarAdministrador() {
        JTextField txtUsuario = new JTextField();
        JPasswordField txtContrasena = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre de usuario:"));
        panel.add(txtUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtContrasena);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Ingrese sus datos",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String usuario = txtUsuario.getText();
            String contrasena = new String(txtContrasena.getPassword());
            String rolElegido = (String) rol.getSelectedItem();

            if(comprobarNombreUsuario(usuario)){
                JOptionPane.showMessageDialog(null, "Este nombre de usuario ya esta en uso.");
            }else {
                insertarNuevoUsuario(usuario, contrasena, rolElegido);
            }
        } else {
            System.out.println("Operación cancelada");
        }
    }

    private boolean comprobarNombreUsuario(String nombreUsuario){
        try (Connection conn = ConexionDB.getConnection()) {
            String sql = "SELECT COUNT(*) FROM Usuarios WHERE Username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombreUsuario);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if(count > 0){
                    return count > 0;
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al comprobar el nombre de usuario: " + e.getMessage());
        }
        return false;
    }

    private void insertarNuevoUsuario(String userName, String password, String rol){
        try (Connection conn = ConexionDB.getConnection()) {
            String sql = "INSERT INTO Usuarios (Username, Password_Hash, Rol) VALUES" +
                    "(?, ?, ?)";
            String tabla;
            String sql2;
            switch (rol) {
                case "Alumno":
                    tabla = "Alumno";
                     sql2 = "INSERT INTO " + tabla + " (DNI, Nombre, Apellido) VALUES" +
                            "(?, ?, ?)";
                    PreparedStatement stmt2 = conn.prepareStatement(sql2);
                    stmt2.setString(1, campoDni.getText());
                    stmt2.setString(2, campoNombre.getText());
                    stmt2.setString(3, campoApellido.getText());

                    stmt2.executeUpdate();
                    break;
                case "Profesor":
                    tabla = "Profesor";
                    sql2 = "INSERT INTO " + tabla + " (DNI, Nombre, Apellido, Tipo) VALUES" +
                            "(?, ?, ?, ?)";
                    stmt2 = conn.prepareStatement(sql2);
                    stmt2.setString(1, campoDni.getText());
                    stmt2.setString(2, campoNombre.getText());
                    stmt2.setString(3, campoApellido.getText());
                    stmt2.setString(4, "practico");

                    stmt2.executeUpdate();
                    break;
                case "Administrador":
                    tabla = "Administrador";
                    sql2 = "INSERT INTO " + tabla + " (DNI, Nombre, Apellido) VALUES" +
                            "(?, ?, ?)";
                    stmt2 = conn.prepareStatement(sql2);
                    stmt2.setString(1, campoDni.getText());
                    stmt2.setString(2, campoNombre.getText());
                    stmt2.setString(3, campoApellido.getText());

                    stmt2.executeUpdate();
                    break;
                default:
                    tabla=" ";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt()); //Aplicar Hash al password
            stmt.setString(2, hashPassword);
            stmt.setString(3, rol.toLowerCase());


            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Usuario insertado correctamente");


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar usuario: " + e.getMessage());
        }

    }
}
