import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

public class VentanaAlumno extends JFrame {
    private int finalIdAlumno=1;

    public VentanaAlumno(int idUsuario) {
        finalIdAlumno = idUsuario;
        setTitle("Panel del Alumno - " + finalIdAlumno);
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

        JButton menuBtn = new JButton(cargarIcono("menu.png", 50, 50));
        menuBtn.setBackground(barraSuperior.getBackground());
        menuBtn.setBorderPainted(false);
        menuBtn.setFocusPainted(false);

        JPopupMenu menu = new JPopupMenu();
        int anchoIconosMenus = 30;
        menu.add(new JMenuItem("Realizar test práctica", cargarIcono("prueba.png", anchoIconosMenus, anchoIconosMenus))).addActionListener(e-> new TestPractica(this, finalIdAlumno));
        menu.add(new JMenuItem("Realizar test examen", cargarIcono("examen.png", anchoIconosMenus, anchoIconosMenus))).addActionListener(e-> new TestExamen(this, finalIdAlumno));
        menu.add(new JMenuItem("Realizar test de fallos", cargarIcono("examen.png", anchoIconosMenus, anchoIconosMenus))).addActionListener(e->new TestFallos(this, finalIdAlumno));
        menu.add(new JMenuItem("Consultar porcentaje", cargarIcono("porcentaje.png", anchoIconosMenus, anchoIconosMenus))).addActionListener(e->mostrarPorcentaje());
        menu.addSeparator();
        menu.add(new JMenuItem("Solicitar cita práctica", cargarIcono("calendario.png", anchoIconosMenus, anchoIconosMenus))).addActionListener(e->new CitaPractica(this, finalIdAlumno));
        menu.add(new JMenuItem("Consultar maniobras superadas", cargarIcono("formar.png", anchoIconosMenus, anchoIconosMenus))).addActionListener(e-> mostrarManiobras());


        menuBtn.addActionListener(e -> menu.show(menuBtn, 0, menuBtn.getHeight()));

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


        barraSuperior.add(menuBtn, BorderLayout.WEST);
        barraSuperior.add(userButton, BorderLayout.EAST);

        fondo.add(barraSuperior, BorderLayout.NORTH);

        // Panel de botones centrales
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);


        gbc.gridx = 0;
        JButton b1 = crearBoton("Test práctica", "prueba.png");

        b1.addActionListener(e -> new TestPractica(this, finalIdAlumno));
        panelCentral.add(b1, gbc);

        gbc.gridx = 1;
        JButton b2 = crearBoton("Test examen", "examen.png");
        b2.addActionListener(e -> new TestExamen(this, finalIdAlumno));
        panelCentral.add(b2, gbc);

        gbc.gridx = 2;
        JButton b3 = crearBoton("Examen fallos", "examen.png");
        b3.addActionListener(e -> new TestFallos(this, finalIdAlumno));
        panelCentral.add(b3, gbc);

        gbc.gridx = 3;
        JButton b4 = crearBoton("Pedir cita", "calendario.png");
        b4.addActionListener(e -> new CitaPractica(this, finalIdAlumno));
        panelCentral.add(b4, gbc);

        fondo.add(panelCentral, BorderLayout.CENTER);

        JButton chatBoton = new JButton(cargarIcono("justificar.png", 50, 50));
        chatBoton.setBorderPainted(false);
        chatBoton.setContentAreaFilled(false);
        chatBoton.setFocusPainted(false);

        JPanel chatPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        chatPanel.setOpaque(false);
        chatPanel.add(chatBoton);
        fondo.add(chatPanel, BorderLayout.SOUTH);

        // Chat
        JDialog chat = new JDialog(this, "Chat", false);
        chat.setSize(300, 400);
        chat.setLayout(new BorderLayout());

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chat.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JTextField campo = new JTextField();
        JButton enviar = new JButton("Enviar");
        JPanel input = new JPanel(new BorderLayout());
        input.add(campo, BorderLayout.CENTER);
        input.add(enviar, BorderLayout.EAST);
        chat.add(input, BorderLayout.SOUTH);

        enviar.addActionListener(e -> {
            String texto = campo.getText();
            if (!texto.isEmpty()) {
                chatArea.append("Tú: " + texto + "\nIA: (respuesta generada)\n\n");
                campo.setText("");
            }
        });

        chatBoton.addActionListener(e -> {
            Point p = this.getLocationOnScreen();
            chat.setLocation(p.x + getWidth() - 320, p.y + getHeight() - 480);
            chat.setVisible(!chat.isVisible());
        });

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
    private void mostrarPorcentaje(){
        double porcentaje =0;
        try (Connection conn = ConexionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT Porcentaje FROM ResultadosTeoricos WHERE ID_Alumno = ?");
            stmt.setInt(1, finalIdAlumno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                porcentaje+=rs.getInt("Porcentaje");
            }


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el porcentaje: " + e.getMessage());
        }
        JOptionPane.showMessageDialog(this,"Tu porcentaje de aciertos es:"+porcentaje);
    }
    private void mostrarManiobras(){
        ArrayList<String> maniobras= new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT Maniobra_Superada FROM ResultadosPracticos WHERE ID_Alumno = ?");
            stmt.setInt(1, finalIdAlumno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
               maniobras.add(rs.getString("Porcentaje"));
            }


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el porcentaje: " + e.getMessage());
        }
        if(maniobras.isEmpty()){
            JOptionPane.showMessageDialog(this,"No has realizado ninguna clase practica todavia");
        }else{
            JOptionPane.showMessageDialog(this,"Tus maniobras superadas son: "+maniobras);
        }
    }
}
