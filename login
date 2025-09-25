package loginEquinos;

import java.awt.*;
import javax.swing.*;

public class LoginEquinos extends JFrame {

    private final JTextField txtUsuario;
    private final JPasswordField txtPassword;
    private final JButton btnLogin;
    private final JButton btnRegistro;

    public LoginEquinos() {
        // Configuración de la ventana
        setTitle("Login - Dietas Equinas 🐴");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Colores personalizados
        Color fondoCafeClaro = new Color(210, 180, 140); // #D2B48C
        Color panelCafeOscuro = new Color(139, 69, 19);  // #8B4513
        Color botonVerdeOliva = new Color(85, 107, 47);  // #556B2F
        Color textoClaro = Color.WHITE;

        // Panel principal
        JPanel panel = new JPanel();
        panel.setBackground(fondoCafeClaro);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Panel de login
        JPanel panelLogin = new JPanel(new GridBagLayout());
        panelLogin.setBackground(panelCafeOscuro);
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Software de Dietas Equinas 🐎");
        lblTitulo.setForeground(textoClaro);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 18));

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setForeground(textoClaro);
        txtUsuario = new JTextField(15);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setForeground(textoClaro);
        txtPassword = new JPasswordField(15);

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(botonVerdeOliva);
        btnLogin.setForeground(textoClaro);

        btnRegistro = new JButton("Registrar");
        btnRegistro.setBackground(botonVerdeOliva);
        btnRegistro.setForeground(textoClaro);

        // Colocar elementos dentro del panelLogin
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelLogin.add(lblTitulo, gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        panelLogin.add(lblUsuario, gbc);
        gbc.gridx = 1;
        panelLogin.add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelLogin.add(lblPassword, gbc);
        gbc.gridx = 1;
        panelLogin.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        panelLogin.add(btnLogin, gbc);

        gbc.gridy++;
        panelLogin.add(btnRegistro, gbc);

        // Añadir el panelLogin al panel principal
        panel.add(panelLogin);

        // Agregar a la ventana
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginEquinos().setVisible(true);
        });
    }
}

