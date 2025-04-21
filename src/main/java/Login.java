import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Login extends JFrame {

    JLabel icono, user, password;
    JTextField campoUser;
    JPasswordField campoPassword;
    JButton botonEnviar, botonSalir;

    public Login() {
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 350);
        setLocationRelativeTo(null);

        // Fondo personalizado
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
        setContentPane(panelConFondo); // usar el panel con fondo como contenido

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Cargar icono
        URL iconoURL = getClass().getClassLoader().getResource("usuario gradient.png");
        if (iconoURL != null) {
            Image img = new ImageIcon(iconoURL).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            icono = new JLabel(new ImageIcon(img));
        } else {
            icono = new JLabel("ICONO");
        }

        icono.setHorizontalAlignment(JLabel.CENTER);

        // Icono
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        panelConFondo.add(icono, gbc);

        // Usuario
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        user = new JLabel("Usuario:");
        panelConFondo.add(user, gbc);

        gbc.gridx = 1;
        campoUser = new JTextField();
        panelConFondo.add(campoUser, gbc);

        // Contraseña
        gbc.gridy++;
        gbc.gridx = 0;
        password = new JLabel("Contraseña:");
        panelConFondo.add(password, gbc);

        gbc.gridx = 1;
        campoPassword = new JPasswordField();
        panelConFondo.add(campoPassword, gbc);

        // Botones
        gbc.gridy++;
        gbc.gridx = 0;
        botonEnviar = new JButton("Enviar");
        panelConFondo.add(botonEnviar, gbc);

        gbc.gridx = 1;
        botonSalir = new JButton("Salir");
        panelConFondo.add(botonSalir, gbc);

        setVisible(true);
    }
}
