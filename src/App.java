import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.border.*;

public class App {
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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        JLabel userLabel = new JLabel("USERNAME:");
        userLabel.setForeground(Color.CYAN);
        userLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        JTextField userField = new CustomTextField();
        styleTextField(userField);

        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(userField, gbc);

        // Password
        JLabel passLabel = new JLabel("PASSWORD:");
        passLabel.setForeground(Color.CYAN);
        passLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        JPasswordField passField = new JPasswordField(20);
        styleTextField(passField);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(passField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        // Login Button
        JButton loginButton = new JButton("LOGIN") {
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

        // Signup Button
        JButton signupButton = new JButton("SIGN UP") {
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
        styleButton(signupButton);

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        mainPanel.add(buttonPanel, gbc);

        // Action Listeners
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
        
            if (DatabaseManager.validateUser(username, password)) {
                frame.dispose();
                SwingUtilities.invokeLater(() -> new TravelPlanner(username).setVisible(true));
            } else {
                JOptionPane.showMessageDialog(frame, "Access Denied", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        signupButton.addActionListener(e -> {
            SignUpDialog dialog = new SignUpDialog(frame);
            dialog.setVisible(true);
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
            g2.setColor(new Color(0, 30, 60));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 
                getHeight(), getHeight()));
            super.paintComponent(g);
        }
    }

    private static void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 30));
        field.setForeground(Color.CYAN);
        field.setCaretColor(Color.CYAN);
        field.setFont(new Font("Consolas", Font.PLAIN, 14));
        if (!(field instanceof CustomTextField)) {
            field.setOpaque(false);
            field.setBorder(new RoundedCornerBorder());
        }
    }

    private static void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(120, 35));
        button.setForeground(Color.CYAN);
        button.setFont(new Font("Consolas", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
}