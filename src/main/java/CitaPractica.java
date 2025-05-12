import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.List;


public class CitaPractica extends JFrame{
    private final JFrame ventanaAnterior;

    private final JLabel lblProfesor= new JLabel("Selecciona el profesor: ", SwingConstants.CENTER);
    private final JComboBox listaProfesores= new JComboBox();

    private final int idAlumno;

    private JPanel panelCalendario;
    private JLabel lblMes;
    private Calendar calendar = Calendar.getInstance();

    public CitaPractica(JFrame ventanaAnterior, int idAlumno){
        this.ventanaAnterior = ventanaAnterior;
        this.idAlumno = idAlumno;
        setTitle("Test Práctica");
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

        cargarProfesoresPracticos();

        lblProfesor.setFont(new Font("Arial", Font.BOLD, 20));
        lblProfesor.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        listaProfesores.setFont(new Font("Arial", Font.BOLD, 20));
        listaProfesores.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        listaProfesores.addActionListener(e-> cargarCalendarioProfesorElegido((String) listaProfesores.getSelectedItem()));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(lblProfesor, BorderLayout.WEST);
        panelSuperior.add(listaProfesores, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        setVisible(true);
    }

    public void cargarProfesoresPracticos(){
        try (Connection conn = ConexionDB.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Profesor where Tipo='practico'");



            while (rs.next()) {
                listaProfesores.addItem(rs.getString(3));
            }


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar profesores: " + e.getMessage());
        }
    }

    public void cargarCalendarioProfesorElegido(String profesor) {
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
            cargarCalendarioProfesorElegido(profesor);
        });

        btnSiguiente.addActionListener(e -> {
            calendar.add(Calendar.MONTH, 1);
            cargarCalendarioProfesorElegido(profesor);
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

        // Cargar reservas
        List<Date> reservas = cargarReservasDelProfesor(profesor);
        java.util.Map<Integer, java.util.List<String>> mapaHoras = new java.util.HashMap<>();

        for (Date d : reservas) {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int dia = c.get(Calendar.DAY_OF_MONTH);
            int mes = c.get(Calendar.MONTH);
            int año = c.get(Calendar.YEAR);

            if (mes == calendar.get(Calendar.MONTH) && año == calendar.get(Calendar.YEAR)) {
                String hora = String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
                mapaHoras.computeIfAbsent(dia, k -> new ArrayList<>()).add(hora);
            }
        }

        // Espacios en blanco hasta el primer día
        for (int i = 1; i < primerDiaSemana; i++) {
            gridDias.add(new JLabel(""));
        }

        for (int dia = 1; dia <= diasEnMes; dia++) {
            JButton btnDia = new JButton(String.valueOf(dia));
            btnDia.setMargin(new Insets(10, 10, 10, 10));
            btnDia.setFont(new Font("Arial", Font.BOLD, 14));
            btnDia.setHorizontalAlignment(SwingConstants.CENTER);


            btnDia.setBackground(new Color(173, 216, 230)); // azul claro si hay reservas
            btnDia.setOpaque(true);
            btnDia.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            int diaFinal = dia; // necesario para la lambda
            btnDia.addActionListener(e -> {
                ArrayList<String> horasDisponibles = new ArrayList<>();
                for (int h = 9; h < 15; h++) {
                    horasDisponibles.add(String.format("%02d:00", h));
                }
                for (int h = 17; h < 22; h++) {
                    horasDisponibles.add(String.format("%02d:00", h));
                }

                List<String> horasNoDisponibles = mapaHoras.getOrDefault(diaFinal, new ArrayList<>());
                horasDisponibles.removeAll(horasNoDisponibles);

                JComboBox<String> comboHorasFiltradas = new JComboBox<>(horasDisponibles.toArray(new String[0]));

                int opcion = JOptionPane.showConfirmDialog(
                        null,
                        comboHorasFiltradas,
                        "Selecciona una hora",
                        JOptionPane.OK_CANCEL_OPTION
                );

                if (opcion == JOptionPane.OK_OPTION) {
                    String horaSeleccionada = (String) comboHorasFiltradas.getSelectedItem();
                    System.out.println("Hora seleccionada para el día " + diaFinal + ": " + horaSeleccionada);

                    int mesMostrado = calendar.get(Calendar.MONTH) + 1;
                    int anioMostrado = calendar.get(Calendar.YEAR);

                    LocalDate fechaSeleccionada= LocalDate.of(anioMostrado, mesMostrado, diaFinal);

                    LocalTime hora = LocalTime.parse(horaSeleccionada);
                    LocalDateTime fechaHoraCompleta = LocalDateTime.of(fechaSeleccionada, hora);

                    Timestamp fechaHoraSeleccionada=Timestamp.valueOf(fechaHoraCompleta);

                    insertarReserva(profesor, fechaHoraSeleccionada);
                }
            });


            gridDias.add(btnDia);
        }

        panelCalendario.add(gridDias, BorderLayout.CENTER);
        add(panelCalendario, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void insertarReserva(String nombreProfesor, Timestamp fechaHora){
        int idProfesor = 0;
        try (Connection conn = ConexionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT ID_Profesor FROM Profesor WHERE Nombre = ?");
            stmt.setString(1, nombreProfesor);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idProfesor = rs.getInt(1);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ID_Profesor: " + e.getMessage());
            return;
        }


        try (Connection conn = ConexionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Reservas (ID_Profesor, ID_Alumno, Fecha_Hora) VALUES (?, ?, ?)");
            stmt.setInt(1, idProfesor);
            stmt.setInt(2, idAlumno);
            stmt.setTimestamp(3, fechaHora);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Reserva realizada con éxito.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la reserva: " + e.getMessage());
        }
    }



    private void actualizarEtiquetaMes() {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        lblMes.setText(meses[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));
    }


    public List<Date> cargarReservasDelProfesor(String nombreProfesor){
        List<Date> listaFechasReservadas=new ArrayList<>();

        try (Connection conn = ConexionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT Fecha_Hora FROM Reservas WHERE ID_Profesor=(SELECT ID_Profesor FROM Profesor WHERE Nombre = ?)");
            stmt.setString(1, nombreProfesor);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Timestamp ts = rs.getTimestamp("Fecha_Hora");
                if (ts != null) {
                    listaFechasReservadas.add(new Date(ts.getTime()));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar profesores: " + e.getMessage());
        }
        return listaFechasReservadas;
    }
}
