import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsView extends JFrame {
    private JPanel profilePanel;
    private JPanel settingsPanel;
    private JComboBox<String> themeComboBox;

    public SettingsView() {
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(800, 600);
        setResizable(false);

        profilePanel = createProfilePanel();
        settingsPanel = createSettingsPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Adding the profile panel (30%)
        gbc.weightx = 0.3;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(profilePanel, gbc);

        // Adding the settings panel (70%)
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        add(settingsPanel, gbc);

        // Theme selection
        JPanel themePanel = new JPanel();
        themeComboBox = new JComboBox<>(new String[]{"Light", "Dark"});
        themeComboBox.addActionListener(new ThemeChangeListener());
        themePanel.add(new JLabel("Select Theme:"));
        themePanel.add(themeComboBox);
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(themePanel, gbc);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Profile Info"));
        // Add other profile-related components here
        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Settings"));
        panel.add(createCard("Contact Info"));
        panel.add(createCard("Security"));
        panel.add(createCard("Appearance"));
        return panel;
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setBorder(BorderFactory.createTitledBorder(title));
        card.setBackground(Color.WHITE);
        return card;
    }

    private class ThemeChangeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedTheme = (String) themeComboBox.getSelectedItem();
            try {
                if ("Dark".equals(selectedTheme)) {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                } else {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                }
                SwingUtilities.updateComponentTreeUI(SettingsView.this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SettingsView().setVisible(true);
        });
    }
}