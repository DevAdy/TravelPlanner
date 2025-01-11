import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.border.*;
import java.util.*;
import java.util.List;

public class TravelPlanner extends JFrame {
    private final String[] INTERESTS = {"Culture", "Food", "Shopping", "Nature", "Adventure"};
    private JTextField destinationField;
    private JTextField startDate;
    private JTextField endDate;
    private JSlider budget;
    private JPanel interestsPanel;
    
    public TravelPlanner() {
        setTitle("Travel Itinerary Planner");
        setSize(550, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main panel with geeky background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
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

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(450, 60));
        JLabel titleLabel = new JLabel("TRAVEL PLANNER");
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 24));
        titleLabel.setForeground(Color.CYAN);
        headerPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Initialize components with custom style
        destinationField = new CustomTextField();
        startDate = new CustomTextField();
        endDate = new CustomTextField();
        budget = createStyledSlider();
        interestsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        interestsPanel.setOpaque(false);

        // Add form fields
        addFormField(formPanel, "DESTINATION:", destinationField, gbc, 0);
        addFormField(formPanel, "START DATE:", startDate, gbc, 1);
        addFormField(formPanel, "END DATE:", endDate, gbc, 2);

        // Budget Slider
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createStyledLabel("BUDGET ($):"), gbc);
        gbc.gridx = 1;
        formPanel.add(budget, gbc);

        // Interests
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createStyledLabel("INTERESTS:"), gbc);
        for (String interest : INTERESTS) {
            JCheckBox cb = new JCheckBox(interest);
            styleCheckBox(cb);
            interestsPanel.add(cb);
        }
        gbc.gridx = 1;
        formPanel.add(interestsPanel, gbc);

        // Generate Button
        JButton generateButton = new JButton("GENERATE ITINERARY") {
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
        styleButton(generateButton);
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        formPanel.add(generateButton, gbc);

        generateButton.addActionListener(e -> {
            if (destinationField.getText().trim().isEmpty() || 
                startDate.getText().trim().isEmpty() || 
                endDate.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please fill all required fields", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            String destination = destinationField.getText().trim();
            String start = startDate.getText().trim();
            String end = endDate.getText().trim();
            int budgetValue = budget.getValue();
            
            List<String> selectedInterests = new ArrayList<>();
            for (Component c : interestsPanel.getComponents()) {
                if (c instanceof JCheckBox) {
                    JCheckBox cb = (JCheckBox) c;
                    if (cb.isSelected()) {
                        selectedInterests.add(cb.getText());
                    }
                }
            }

            String prompt = String.format(
                "Create a travel itinerary for %s from %s to %s with a budget of $%d. " +
                "Interests include: %s", 
                destination, start, end, budgetValue, 
                String.join(", ", selectedInterests)
            );

            JDialog loadingDialog = new JDialog(this, "Processing", true);
            JLabel loadingLabel = new JLabel("Generating itinerary...");
            loadingLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
            loadingLabel.setForeground(new Color(173, 181, 189));
            loadingDialog.add(loadingLabel);
            loadingDialog.pack();
            loadingDialog.setLocationRelativeTo(this);

            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() {
                    return PythonConnector.getAIResponse(prompt);
                }

                @Override
                protected void done() {
                    loadingDialog.dispose();
                    try {
                        String itinerary = get();
                        
                        // Create custom styled HTML content
                        String styledContent = String.format("""
                            <html>
                            <body style='width: 500px; font-family: Consolas; background-color: rgb(0,30,60); color: rgb(0,255,255);'>
                                <div style='padding: 20px;'>
                                    %s
                                </div>
                            </body>
                            </html>""",
                            itinerary.replace("**", "</span>")
                                    .replaceFirst("TRAVEL ITINERARY FOR", 
                                        "<span style='font-size: 28px; color: rgb(0,255,255); display: block; text-align: center; margin-bottom: 30px;'>TRAVEL ITINERARY FOR")
                                    .replace("Day", "<span style='font-size: 22px; color: rgb(0,200,255); display: block; margin-top: 25px; margin-bottom: 15px;'>Day")
                                    .replace("Duration:", "<span style='font-size: 18px; color: rgb(0,255,200); display: block; margin-top: 15px;'>⏱ Duration:")
                                    .replace("Budget:", "<span style='font-size: 18px; color: rgb(0,255,200); display: block; margin-top: 10px;'>💰 Budget:")
                                    .replace("Recommended Hotels:", "<span style='font-size: 20px; color: rgb(0,200,255); display: block; margin-top: 30px;'>🏨 Recommended Hotels:")
                                    .replace("Tips:", "<span style='font-size: 20px; color: rgb(0,200,255); display: block; margin-top: 30px;'>💡 Tips:")
                                    .replace("* Morning:", "<div style='margin: 25px 0 15px 0;'><span style='font-size: 20px; color: rgb(255,200,0); font-weight: bold;'>🌅 Morning:</span></div>")
                                    .replace("* Afternoon:", "<div style='margin: 25px 0 15px 0;'><span style='font-size: 20px; color: rgb(255,200,0); font-weight: bold;'>☀️ Afternoon:</span></div>")
                                    .replace("* Evening:", "<div style='margin: 25px 0 15px 0;'><span style='font-size: 20px; color: rgb(255,200,0); font-weight: bold;'>🌙 Evening:</span></div>")
                                    .replaceAll("\\n\\* ([^\\n]+)", "<div style='margin: 12px 0 12px 20px; font-size: 15px;'>• $1</div>")
                                    .replace("\n", "<br>"));
                
                        JEditorPane editorPane = new JEditorPane();
                        editorPane.setContentType("text/html");
                        editorPane.setText(styledContent);
                        editorPane.setEditable(false);
                        editorPane.setBackground(new Color(0, 30, 60));
                
                        JScrollPane scrollPane = new JScrollPane(editorPane);
                        scrollPane.setPreferredSize(new Dimension(550, 500));
                        scrollPane.setBorder(BorderFactory.createEmptyBorder());
                
                        // Custom dialog
                        JDialog resultDialog = new JDialog(TravelPlanner.this, "Your AI Generated Itinerary", true);
                        resultDialog.setLayout(new BorderLayout());
                        resultDialog.add(scrollPane, BorderLayout.CENTER);
                        
                        // Add close button at bottom
                        JButton closeButton = new JButton("CLOSE");
                        styleButton(closeButton);
                        closeButton.addActionListener(e -> resultDialog.dispose());
                        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                        buttonPanel.setBackground(new Color(0, 30, 60));
                        buttonPanel.add(closeButton);
                        resultDialog.add(buttonPanel, BorderLayout.SOUTH);
                
                        resultDialog.setSize(600, 600);
                        resultDialog.setLocationRelativeTo(TravelPlanner.this);
                        resultDialog.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(TravelPlanner.this,
                            "Error generating itinerary: " + ex.getMessage(),
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
            loadingDialog.setVisible(true);
        });

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
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

    private JSlider createStyledSlider() {
        JSlider slider = new JSlider(500, 5000, 1000);
        slider.setOpaque(false);
        slider.setForeground(Color.CYAN);
        slider.setMajorTickSpacing(1000);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    private void addFormField(JPanel panel, String labelText, JTextField field, 
                            GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel(labelText), gbc);
        gbc.gridx = 1;
        styleTextField(field);
        panel.add(field, gbc);
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

    private void styleCheckBox(JCheckBox checkBox) {
        checkBox.setFont(new Font("Consolas", Font.PLAIN, 12));
        checkBox.setForeground(Color.CYAN);
        checkBox.setBackground(null);
        checkBox.setFocusPainted(false);
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(200, 35));
        button.setForeground(Color.CYAN);
        button.setFont(new Font("Consolas", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
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