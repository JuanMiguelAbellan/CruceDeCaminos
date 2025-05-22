import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.Date;
import java.util.List;


public class VerCalendario extends JFrame{
    private final JFrame ventanaAnterior;

    private final JLabel lblProfesor=new JLabel();

    private final int idPofesor;
    private String nombreProfesor;

    private JPanel panelCalendario;
    private JLabel lblMes;
    private Calendar calendar = Calendar.getInstance();

    public VerCalendario(JFrame ventanaAnterior, int idPofesor){
        this.ventanaAnterior = ventanaAnterior;
        this.idPofesor = idPofesor;
        setTitle("Calendario");
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

        cargarCalendarioProfesorElegido();

        lblProfesor.setFont(new Font("Arial", Font.BOLD, 20));
        lblProfesor.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(lblProfesor, BorderLayout.WEST);
        add(panelSuperior, BorderLayout.NORTH);

        setVisible(true);
    }


    public void cargarCalendarioProfesorElegido() {
        lblProfesor.setText(nombreProfesor);

        try (Connection conn = ConexionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT Nombre FROM Profesor WHERE ID_Profesor = ?");
            stmt.setInt(1, idPofesor);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                this.nombreProfesor = rs.getString(1);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ID_Profesor: " + e.getMessage());
            return;
        }

        if (panelCalendario != null) remove(panelCalendario);

        panelCalendario = new JPanel(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        JButton btnAnterior = new JButton("<<");
        JButton btnSiguiente = new JButton(">>");
        lblMes = new JLabel("", SwingConstants.CENTER);
        lblMes.setFont(new Font("Arial", Font.BOLD, 20));

        actualizarEtiquetaMes();

        btnAnterior.addActionListener(e -> {
            calendar.add(Calendar.MONTH, -1);
            cargarCalendarioProfesorElegido();
        });

        btnSiguiente.addActionListener(e -> {
            calendar.add(Calendar.MONTH, 1);
            cargarCalendarioProfesorElegido();
        });

        panelSuperior.add(btnAnterior, BorderLayout.WEST);
        panelSuperior.add(lblMes, BorderLayout.CENTER);
        panelSuperior.add(btnSiguiente, BorderLayout.EAST);

        panelCalendario.add(panelSuperior, BorderLayout.NORTH);

        // Panel con los días
        JPanel gridDias = new JPanel(new GridLayout(0, 7));
        String[] diasSemana = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
        for (String dia : diasSemana) {
            JLabel lblDia = new JLabel(dia, SwingConstants.CENTER);
            lblDia.setFont(new Font("Arial", Font.BOLD, 16));
            gridDias.add(lblDia);
        }

        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int primerDiaSemana = cal.get(Calendar.DAY_OF_WEEK);
        int diasEnMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i < primerDiaSemana; i++) {
            gridDias.add(new JLabel(""));
        }

        java.util.Map<Integer, java.util.List<String>> mapaHoras = new java.util.HashMap<>();
        List<Date> reservas = cargarReservasDelProfesor(idPofesor);

        Calendar c = Calendar.getInstance();

        for (Date d : reservas) {
            c.setTime(d);
            int dia = c.get(Calendar.DAY_OF_MONTH);
            int mes = c.get(Calendar.MONTH);
            int año = c.get(Calendar.YEAR);

            // Importante: compara con el mes y año del calendario actual
            if (mes == calendar.get(Calendar.MONTH) && año == calendar.get(Calendar.YEAR)) {
                String hora = String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
                mapaHoras.computeIfAbsent(dia, k -> new ArrayList<>()).add(hora);
            }
        }

        for (int dia = 1; dia <= diasEnMes; dia++) {
            JPanel panelDia = new JPanel(new BorderLayout());
            panelDia.setPreferredSize(new Dimension(100, 100));
            panelDia.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panelDia.setBackground(new Color(240, 248, 255));

            JLabel labelDia = new JLabel(String.valueOf(dia));
            labelDia.setHorizontalAlignment(SwingConstants.RIGHT);
            labelDia.setFont(new Font("Arial", Font.BOLD, 12));
            labelDia.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            panelDia.add(labelDia, BorderLayout.NORTH);

            JPanel panelCitas = new JPanel();
            panelCitas.setLayout(new BoxLayout(panelCitas, BoxLayout.Y_AXIS));
            panelCitas.setOpaque(false);

            List<String> citas = mapaHoras.getOrDefault(dia, new ArrayList<>());
            if (citas.isEmpty()) {
                JLabel sinCitas = new JLabel("Sin citas");
                sinCitas.setFont(new Font("Arial", Font.ITALIC, 11));
                panelCitas.add(sinCitas);
            } else {
                for (String hora : citas) {
                    JLabel l = new JLabel("• " + hora);
                    l.setFont(new Font("Arial", Font.PLAIN, 11));
                    panelCitas.add(l);
                }
            }

            panelDia.add(panelCitas, BorderLayout.CENTER);
            gridDias.add(panelDia);
        }


        panelCalendario.add(gridDias, BorderLayout.CENTER);
        add(panelCalendario, BorderLayout.CENTER);
        revalidate();
        repaint();
    }


    private void actualizarEtiquetaMes() {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        lblMes.setText(meses[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));
    }


    public List<Date> cargarReservasDelProfesor(int idPofesor){
        List<Date> listaFechasReservadas=new ArrayList<>();

        try (Connection conn = ConexionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT Fecha_Hora FROM Reservas WHERE ID_Profesor=?");
            stmt.setInt(1, idPofesor);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Timestamp ts = rs.getTimestamp("Fecha_Hora");
                if (ts != null) {
                    listaFechasReservadas.add(new Date(ts.getTime()));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar reservas: " + e.getMessage());
        }
        return listaFechasReservadas;
    }
}
