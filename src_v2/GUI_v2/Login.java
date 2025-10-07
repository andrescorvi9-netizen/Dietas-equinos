package GUI;

import Persistencia.ConexionBD;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Login extends JFrame {

    private final JTextField txtUsuario;
    private final JPasswordField txtPassword;
    private final JButton btnLogin;
    private final JButton btnRegistro;
    // Componente para selección de rol
    private final JComboBox<String> cmbRol;
    private final Connection conexion;

    public Login() {
        // Config ventana
        setTitle("Login - Dietas Equinas 🐴");
        setSize(400, 350); // Aumentar tamaño para incluir el selector de rol
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        conexion = ConexionBD.getConexion();
        
        // Manejo básico de error de conexión
        if (conexion == null) {
             JOptionPane.showMessageDialog(this, "Error de conexión con la base de datos.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }

        // Colores
        Color fondoCafeClaro = new Color(210, 180, 140);
        Color panelCafeOscuro = new Color(139, 69, 19);
        Color botonVerdeOliva = new Color(85, 107, 47);
        Color textoClaro = Color.WHITE;

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(fondoCafeClaro);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10); // Margen ajustado

        JPanel panelLogin = new JPanel(new GridBagLayout());
        panelLogin.setBackground(panelCafeOscuro);
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Componente de selección de Rol
        String[] roles = {"Dueño", "Cuidador"};
        cmbRol = new JComboBox<>(roles);
        cmbRol.setBackground(textoClaro);
        
        JLabel lblTitulo = new JLabel("Software de Dietas Equinas 🐎");
        lblTitulo.setForeground(textoClaro);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 18));
        
        JLabel lblRol = new JLabel("Seleccionar Rol:");
        lblRol.setForeground(textoClaro);

        JLabel lblUsuario = new JLabel("Usuario (Nombre Dueño):");
        lblUsuario.setForeground(textoClaro);
        txtUsuario = new JTextField(15);

        JLabel lblPassword = new JLabel("Contraseña (Cédula):");
        lblPassword.setForeground(textoClaro);
        txtPassword = new JPasswordField(15);

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(botonVerdeOliva);
        btnLogin.setForeground(textoClaro);

        btnRegistro = new JButton("Registrar");
        btnRegistro.setBackground(botonVerdeOliva);
        btnRegistro.setForeground(textoClaro);

        // Ubicación elementos

        // 1. Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelLogin.add(lblTitulo, gbc);

        // 2. Selector de Rol
        gbc.gridwidth = 1; gbc.gridy++;
        gbc.gridx = 0; gbc.fill = GridBagConstraints.NONE;
        panelLogin.add(lblRol, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; 
        panelLogin.add(cmbRol, gbc);
        
        // 3. Usuario
        gbc.gridx = 0; gbc.gridy++; gbc.fill = GridBagConstraints.NONE;
        panelLogin.add(lblUsuario, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; 
        panelLogin.add(txtUsuario, gbc);

        // 4. Contraseña
        gbc.gridx = 0; gbc.gridy++; gbc.fill = GridBagConstraints.NONE;
        panelLogin.add(lblPassword, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; 
        panelLogin.add(txtPassword, gbc);

        // 5. Botones
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        panelLogin.add(btnLogin, gbc);

        gbc.gridy++;
        panelLogin.add(btnRegistro, gbc);

        panel.add(panelLogin);
        add(panel);

        // Eventos
        btnLogin.addActionListener(e -> loginUsuario());
        btnRegistro.addActionListener(e -> registrarUsuario());
    }

    private void loginUsuario() {
        String usuario = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        // Obtener el rol seleccionado
        String rolSeleccionado = (String) cmbRol.getSelectedItem();
        int idUsuario = -1;

        if (usuario.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Ingresa usuario y contraseña.");
            return;
        }
        
        if (conexion == null) {
             JOptionPane.showMessageDialog(this, "❌ No hay conexión a la base de datos.");
             return;
        }

        try {
            String sql;
            String idCol = "ID_Dueno";
            
            // ✅ Ambos roles (Cuidador y Administrador) se autentican contra la tabla Duenos
            if ("Cuidador".equals(rolSeleccionado) || "Administrador".equals(rolSeleccionado)) {
                sql = "SELECT ID_Dueno FROM Duenos WHERE Nombre_Dueno=? AND Cedula_Dueno=?";
            } else {
                JOptionPane.showMessageDialog(this, "❌ Rol no válido.");
                return;
            }

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idUsuario = rs.getInt(idCol);
                JOptionPane.showMessageDialog(this, "✅ Bienvenido " + rolSeleccionado + " " + usuario);

                this.dispose();

                // Redirección basada en el rol
                if ("Cuidador".equals(rolSeleccionado)) {
                    // Cuidadores van a la gestión de dietas
                    new Pantalla(conexion, idUsuario).setVisible(true);
                } else if ("Administrador".equals(rolSeleccionado)) {
                    // Administradores van a su propia pantalla (debe crear la clase AdministradorPantalla)
                    new AdministradorPantalla(conexion, idUsuario).setVisible(true);
                }
                
            } else {
                JOptionPane.showMessageDialog(this, "❌ Usuario, contraseña o rol incorrectos.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error BD al intentar logearse como " + rolSeleccionado + ": " + ex.getMessage());
        }
    }

    private void registrarUsuario() {
        String usuario = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        String rolSeleccionado = (String) cmbRol.getSelectedItem();

        // ⚠️ Restricción: Solo permitir el registro para el rol de Cuidador (tabla Duenos)
        if (!"Cuidador".equals(rolSeleccionado)) {
             JOptionPane.showMessageDialog(this, "La opción de registrar solo está disponible para el rol de Cuidador.");
             return;
        }
        
        if (usuario.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Ingresa usuario y contraseña.");
            return;
        }

        if (conexion == null) {
             JOptionPane.showMessageDialog(this, "❌ No hay conexión a la base de datos.");
             return;
        }
        
        try {
            // Registro en la tabla Duenos
            String sql = "INSERT INTO Duenos (Nombre_Dueno, Cedula_Dueno) VALUES (?, ?)";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, pass);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "✅ Usuario Cuidador registrado correctamente. ¡Ahora inicia sesión!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error BD al registrar: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}