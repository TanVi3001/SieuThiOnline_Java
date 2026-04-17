
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsView extends JFrame {

    private JComboBox<String> themeComboBox;
    private JCheckBox rememberLoginCheck;
    private JCheckBox autoLogoutCheck;
    private JSpinner autoLogoutMinutesSpinner;
    private JLabel accountLabel;
    private JLabel roleLabel;

    public SettingsView() {
        initUI();
    }

    private void initUI() {
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // không thoát cả app
        setSize(960, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        setContentPane(root);

        // LEFT: profile panel
        root.add(createProfilePanel(), BorderLayout.WEST);

        // CENTER: settings content
        root.add(createSettingsPanel(), BorderLayout.CENTER);

        // SOUTH: action buttons
        root.add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Profile"));

        panel.add(Box.createVerticalStrut(12));

        JLabel avatar = new JLabel("👤", SwingConstants.CENTER);
        avatar.setFont(new Font("SansSerif", Font.PLAIN, 64));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(avatar);

        panel.add(Box.createVerticalStrut(12));

        accountLabel = new JLabel("Account: ACC251447");
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(accountLabel);

        panel.add(Box.createVerticalStrut(8));

        roleLabel = new JLabel("Role: ADMIN");
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(roleLabel);

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createAppearanceCard());
        panel.add(Box.createVerticalStrut(12));
        panel.add(createSecurityCard());
        panel.add(Box.createVerticalStrut(12));
        panel.add(createSystemCard());

        return panel;
    }

    private JPanel createAppearanceCard() {
        JPanel card = createCard("Appearance");
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = baseGbc();
        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(new JLabel("Theme:"), gbc);

        themeComboBox = new JComboBox<>(new String[]{"Light", "Dark"});
        themeComboBox.addActionListener(e -> applyTheme((String) themeComboBox.getSelectedItem()));

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        card.add(themeComboBox, gbc);

        return card;
    }

    private JPanel createSecurityCard() {
        JPanel card = createCard("Security");
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = baseGbc();

        rememberLoginCheck = new JCheckBox("Remember login on this device");
        rememberLoginCheck.setSelected(true);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(rememberLoginCheck, gbc);

        autoLogoutCheck = new JCheckBox("Auto logout after idle time");
        autoLogoutCheck.setSelected(true);
        gbc.gridy = 1;
        card.add(autoLogoutCheck, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        card.add(new JLabel("Idle minutes:"), gbc);

        autoLogoutMinutesSpinner = new JSpinner(new SpinnerNumberModel(30, 5, 240, 5));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        card.add(autoLogoutMinutesSpinner, gbc);

        autoLogoutCheck.addActionListener(e -> autoLogoutMinutesSpinner.setEnabled(autoLogoutCheck.isSelected()));
        autoLogoutMinutesSpinner.setEnabled(autoLogoutCheck.isSelected());

        return card;
    }

    private JPanel createSystemCard() {
        JPanel card = createCard("System");
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = baseGbc();
        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(new JLabel("Token cleanup interval:"), gbc);

        JComboBox<String> cleanupCombo = new JComboBox<>(new String[]{"5 minutes", "10 minutes", "15 minutes", "30 minutes"});
        cleanupCombo.setSelectedItem("15 minutes");
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        card.add(cleanupCombo, gbc);

        return card;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());

        panel.add(btnCancel);
        panel.add(btnSave);
        return panel;
    }

    private void onSave() {
        // TODO: lưu DB/config file nếu cần
        JOptionPane.showMessageDialog(this, "Settings saved successfully.");
    }

    private void applyTheme(String selectedTheme) {
        try {
            if ("Dark".equalsIgnoreCase(selectedTheme)) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            SwingUtilities.updateComponentTreeUI(this);
            this.pack();
            this.setSize(960, 620);
            this.setLocationRelativeTo(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to apply theme: " + ex.getMessage());
        }
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setBorder(BorderFactory.createTitledBorder(title));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        return card;
    }

    private GridBagConstraints baseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
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
