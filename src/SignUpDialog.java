import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.border.*;

public class SignUpDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private boolean signupSuccessful = false;

    public SignUpDialog(JFrame parent) {
        super(parent, "Sign Up", true);
        setupUI();
    }

    private void setupUI() {
        // Main panel with geeky background
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 0, 50),
                        0, getHeight(), new Color(0, 30, 60));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Tech circles
                g2d.setColor(new Color(0, 100, 255, 50));
                for (int i = 0; i < 5; i++) {
                    int size = 40 + i * 20;
                    g2d.drawOval(getWidth()/2 - size/2, getHeight()/2 - size/2, size, size);
                }

                // Data streams
                g2d.setColor(new Color(0, 255, 0, 100));
                for (int i = 0; i < 20; i++) {
                    int x1 = (int)(Math.random() * getWidth());
                    int y1 = (int)(Math.random() * getHeight());
                    int x2 = (int)(Math.random() * getWidth());
                    int y2 = (int)(Math.random() * getHeight());
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username field
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = createStyledLabel("USERNAME:");
        mainPanel.add(userLabel, gbc);
        
        gbc.gridx = 1;
        usernameField = new CustomTextField();
        styleTextField(usernameField);
        mainPanel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passLabel = createStyledLabel("PASSWORD:");
        mainPanel.add(passLabel, gbc);
        
        gbc.gridx = 1;
        passwordField = new CustomPasswordField();
        styleTextField(passwordField);
        mainPanel.add(passwordField, gbc);

        // Confirm password field
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel confirmLabel = createStyledLabel("CONFIRM:");
        mainPanel.add(confirmLabel, gbc);
        
        gbc.gridx = 1;
        confirmPasswordField = new CustomPasswordField();
        styleTextField(confirmPasswordField);
        mainPanel.add(confirmPasswordField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        JButton signupButton = new CustomButton("SIGN UP");
        JButton cancelButton = new CustomButton("CANCEL");

        signupButton.addActionListener(e -> handleSignup());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(signupButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(buttonPanel, gbc);

        setContentPane(mainPanel);
        setSize(400, 300);
        setLocationRelativeTo(getParent());
    }

    private void handleSignup() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DatabaseManager.addUser(username, password)) {
            signupSuccessful = true;
            JOptionPane.showMessageDialog(this, 
                "Sign up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Consolas", Font.BOLD, 12));
        label.setForeground(Color.CYAN);
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 30));
        field.setForeground(Color.CYAN);
        field.setCaretColor(Color.CYAN);
        field.setFont(new Font("Consolas", Font.PLAIN, 14));
    }

    // Custom text field with rounded corners
    static class CustomTextField extends JTextField {
        public CustomTextField() {
            setOpaque(false);
            setBorder(new RoundedCornerBorder());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 30, 60));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 
                getHeight(), getHeight()));
            super.paintComponent(g);
        }
    }

    // Custom password field with rounded corners
    static class CustomPasswordField extends JPasswordField {
        public CustomPasswordField() {
            setOpaque(false);
            setBorder(new RoundedCornerBorder());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 30, 60));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 
                getHeight(), getHeight()));
            super.paintComponent(g);
        }
    }

    // Custom button with glowing effect
    static class CustomButton extends JButton {
        public CustomButton(String text) {
            super(text);
            setForeground(Color.CYAN);
            setFont(new Font("Consolas", Font.BOLD, 14));
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(120, 35));
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getModel().isArmed()) {
                g.setColor(new Color(0, 100, 200));
            } else {
                g.setColor(new Color(0, 50, 100));
            }
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            super.paintComponent(g);
        }
    }

    static class RoundedCornerBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.CYAN);
            g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, height, height));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }
    }

    public boolean isSignupSuccessful() {
        return signupSuccessful;
    }
}