import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.border.*;

public class App {
    private static final String CORRECT_USERNAME = "admin";
    private static final String CORRECT_PASSWORD = "password123";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("System Access Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Main panel with geeky background
        JPanel mainPanel = new JPanel() {
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
                    g2d.drawOval(getWidth() / 2 - size / 2, getHeight() / 2 - size / 2, size, size);
                }

                // Data streams
                g2d.setColor(new Color(0, 255, 0, 100));
                for (int i = 0; i < 20; i++) {
                    int x1 = (int) (Math.random() * getWidth());
                    int y1 = (int) (Math.random() * getHeight());
                    int x2 = (int) (Math.random() * getWidth());
                    int y2 = (int) (Math.random() * getHeight());
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        JLabel titleLabel = new JLabel("LOGIN TERMINAL");
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 24));
        titleLabel.setForeground(Color.CYAN);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);

        // Username field
        JTextField userField = new CustomTextField();
        styleTextField(userField);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(userField, gbc);

        // Password field
        JPasswordField passField = new CustomPasswordField();
        styleTextField(passField);
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(passField, gbc);

        // Login button
        JButton loginButton = new JButton("AUTHENTICATE") {
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
        };
        styleButton(loginButton);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        mainPanel.add(loginButton, gbc);

        // Action listener for login
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.equals(CORRECT_USERNAME) && password.equals(CORRECT_PASSWORD)) {
                frame.dispose();
                SwingUtilities.invokeLater(() -> new TravelPlanner().setVisible(true));
            } else {
                JOptionPane.showMessageDialog(frame, "Access Denied", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
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
            g2.setColor(getBackground());
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
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 
                getHeight(), getHeight()));
            super.paintComponent(g);
        }
    }

    private static void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 30));
        field.setBackground(new Color(0, 30, 60));
        field.setForeground(Color.CYAN);
        field.setCaretColor(Color.CYAN);
        field.setFont(new Font("Consolas", Font.PLAIN, 14));
    }

    private static void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(200, 35));
        button.setForeground(Color.CYAN);
        button.setFont(new Font("Consolas", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Rounded corner border
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
}