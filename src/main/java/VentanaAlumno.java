import javafx.scene.layout.Background;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class VentanaAlumno extends JFrame {

    public VentanaAlumno(String nombreUsuario) {
        setTitle("Panel del Alumno - " + nombreUsuario);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Color colorMenu = new Color(103, 86, 157);

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
        setContentPane(panelConFondo);

        JPanel barraSuperior = new JPanel(new BorderLayout());
//        barraSuperior.setOpaque(false);
        barraSuperior.setBackground(colorMenu);
        barraSuperior.setPreferredSize(new Dimension(getWidth(), 60));

        // Botón del menú
        JButton menuButton = new JButton(cargarIcono("menu.png", 30, 30));
//        menuButton.setOpaque(false);
        menuButton.setBackground(colorMenu);
        menuButton.setBorderPainted(false);

        // Menú emergente para opciones del alumno
        JPopupMenu menuOpciones = new JPopupMenu();

        int anchoIconosMenus = 20;
        JMenuItem itemHome = new JMenuItem("Home", cargarIcono("hogar.png", anchoIconosMenus, anchoIconosMenus));
        JMenuItem itemTestPractica = new JMenuItem("Realizar test práctica", cargarIcono("prueba.png", anchoIconosMenus, anchoIconosMenus));
        JMenuItem itemTestExamen = new JMenuItem("Realizar test examen", cargarIcono("examen.png", anchoIconosMenus, anchoIconosMenus));
        JMenuItem itemPorcentaje = new JMenuItem("Consultar porcentaje", cargarIcono("porcentaje.png", anchoIconosMenus, anchoIconosMenus));
        JMenuItem itemCita = new JMenuItem("Solicitar cita práctica", cargarIcono("calendario.png", anchoIconosMenus, anchoIconosMenus));
        JMenuItem itemManiobras = new JMenuItem("Consultar maniobras superadas", cargarIcono("formar.png", anchoIconosMenus, anchoIconosMenus));

        menuOpciones.add(itemHome);
        menuOpciones.add(itemTestPractica);
        menuOpciones.add(itemTestExamen);
        menuOpciones.add(itemPorcentaje);
        menuOpciones.addSeparator(); // separación
        menuOpciones.add(itemCita);
        menuOpciones.add(itemManiobras);

        menuButton.addActionListener(e -> {
            menuOpciones.show(menuButton, 0, menuButton.getHeight());
        });

        // Botón del usuario
        JButton userButton = new JButton(cargarIcono("usuario.png", 24, 24));
//        userButton.setOpaque(false);
        userButton.setBackground(colorMenu);
        userButton.setBorderPainted(false);

        // Menú emergente del usuario
        JPopupMenu menuUsuario = new JPopupMenu();
        JMenuItem itemMensajes = new JMenuItem("Consultar mensajes", cargarIcono("correo-electronico.png", anchoIconosMenus, anchoIconosMenus));
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar sesión", cargarIcono("boton-de-encendido.png", anchoIconosMenus, anchoIconosMenus));

        itemCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                Login login = new Login();
            }
        });

        menuUsuario.add(itemMensajes);
        menuUsuario.add(itemCerrarSesion);

        userButton.addActionListener(e -> {
            menuUsuario.show(userButton, userButton.getWidth() - 100, userButton.getHeight());
        });

        // Añadir botones a la barra superior
        barraSuperior.add(menuButton, BorderLayout.WEST);
        barraSuperior.add(userButton, BorderLayout.EAST);

        add(barraSuperior, BorderLayout.NORTH);

        // Panel principal para //TODO agregar panel copn las opciones
//        JPanel panelPrincipal = new JPanel();
//        add(panelPrincipal, BorderLayout.CENTER);

        //Chat de texto con IA

        JLayeredPane layeredPane = getLayeredPane();
        JButton chatBoton = new JButton(cargarIcono("justificar.png", 40, 40));
        chatBoton.setBorderPainted(false);
        chatBoton.setContentAreaFilled(false);
        chatBoton.setFocusPainted(false);
        chatBoton.setBounds(getWidth() - 80, getHeight() - 140, 50, 50); // posición flotante

        layeredPane.add(chatBoton, JLayeredPane.POPUP_LAYER);

        // Mini ventana de chat
        JDialog chatDialog = new JDialog(this, "Asistente Virtual", false);
        chatDialog.setSize(300, 400);
        chatDialog.setLayout(new BorderLayout());
        chatDialog.setLocation(getX() + getWidth() - 320, getY() + getHeight() - 480);

        JTextArea areaChat = new JTextArea();
        areaChat.setEditable(false);
        areaChat.setLineWrap(true);
        areaChat.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(areaChat);
        chatDialog.add(scroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField campoTexto = new JTextField();
        JButton enviar = new JButton("Enviar");

        inputPanel.add(campoTexto, BorderLayout.CENTER);
        inputPanel.add(enviar, BorderLayout.EAST);
        chatDialog.add(inputPanel, BorderLayout.SOUTH);

        enviar.addActionListener(e -> {
            String mensaje = campoTexto.getText().trim();
            if (!mensaje.isEmpty()) {
                areaChat.append("Tú: " + mensaje + "\n");
                // TODO conectar con api de IA para el chat
                areaChat.append("IA: (respuesta generada)\n\n");
                campoTexto.setText("");
            }
        });

        chatBoton.addActionListener(e -> {
            chatDialog.setVisible(!chatDialog.isVisible());
        });

        // Dejar fijada la ventana de texto al aumentar ventana
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                chatBoton.setBounds(getWidth() - 80, getHeight() - 140, 50, 50);
                chatDialog.setLocation(getX() + getWidth() - 320, getY() + getHeight() - 480);
            }

            public void componentMoved(java.awt.event.ComponentEvent e) {
                chatDialog.setLocation(getX() + getWidth() - 320, getY() + getHeight() - 480);
            }
        });

        setVisible(true);
    }

    private ImageIcon cargarIcono(String nombreArchivo, int ancho, int alto) {
        URL url = getClass().getClassLoader().getResource(nombreArchivo);
        if (url != null) {
            Image img = new ImageIcon(url).getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            System.err.println("No se encontró el icono: " + nombreArchivo);
            return null;
        }
    }
}
