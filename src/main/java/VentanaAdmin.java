import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

public class VentanaAdmin extends JFrame {

    public VentanaAdmin() {
        setTitle("Panel del Administrador ");
        setMinimumSize(new Dimension(800, 600));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Color colorMenu = new Color(90, 78, 141);

        // Panel principal con fondo
        JPanel fondo = new JPanel() {
            Image bg = new ImageIcon(getClass().getClassLoader().getResource("fondo.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) {
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // Barra superior
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(90, 78, 141));
        barraSuperior.setPreferredSize(new Dimension(100, 80));

        int anchoIconosMenus = 30;

        JButton userButton = new JButton(cargarIcono("usuario.png", 50, 50));
        userButton.setBackground(colorMenu);
        userButton.setBorderPainted(false);

        JPopupMenu menuUsuario = new JPopupMenu();
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar sesión", cargarIcono("boton-de-encendido.png", anchoIconosMenus, anchoIconosMenus));
        itemCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new Login();
            }
        });
        //menuUsuario.add(new JMenuItem("Consultar mensajes", cargarIcono("correo-electronico.png", anchoIconosMenus, anchoIconosMenus)));
        menuUsuario.add(itemCerrarSesion);

        userButton.addActionListener(e -> menuUsuario.show(userButton, userButton.getWidth() - 100, userButton.getHeight()));

        barraSuperior.add(userButton, BorderLayout.EAST);

        fondo.add(barraSuperior, BorderLayout.NORTH);

        // Panel de botones centrales
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);


        gbc.gridx = 0;
        JButton b1 = crearBoton("Añadir usuario", "agregar-usuario.png");

        b1.addActionListener(e -> new AddUser(this));
        panelCentral.add(b1, gbc);

        gbc.gridx = 1;
        JButton b2 = crearBoton("Elminiar usuario", "usuariox.png");
        b2.addActionListener(e -> new RemoveUser(this));
        panelCentral.add(b2, gbc);

        gbc.gridx = 2;
        JButton b3 = crearBoton("Anadir ficheros", "justificar.png");
        b3.addActionListener(e ->  new AddDoc(this));
        panelCentral.add(b3, gbc);


        fondo.add(panelCentral, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton crearBoton(String texto, String icono) {
        JButton b = new JButton(texto, cargarIcono(icono, 80, 80));
        b.setVerticalTextPosition(SwingConstants.BOTTOM);
        b.setHorizontalTextPosition(SwingConstants.CENTER);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        return b;
    }

    private ImageIcon cargarIcono(String nombre, int w, int h) {
        URL url = getClass().getClassLoader().getResource(nombre);
        if (url != null) {
            Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            System.err.println("No se encontró el icono: " + nombre);
            return null;
        }
    }
}


