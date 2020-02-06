package configuration;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class BbSettingsEditor extends SettingsEditor<BbRunConfiguration> {
    private JPanel panel;
    private TextFieldWithBrowseButton projectRoot;
    private JCheckBox bundle;
    private JPanel bundleSettings;
    private JCheckBox fastCheckBox;
    private JCheckBox compressCheckBox;
    private JCheckBox mangleCheckBox;
    private JCheckBox beautifyCheckBox;
    private JCheckBox newBundlerCheckBox;

    public BbSettingsEditor() {
        projectRoot.addBrowseFolderListener(null,
                null,
                ProjectManager.getInstance().getOpenProjects()[0],
                FileChooserDescriptorFactory.createSingleFolderDescriptor());

        bundleSettings.setVisible(false);
        bundle.addChangeListener(e -> {
            boolean isBundle = bundle.isSelected();
            boolean bundleSettingsVisible = bundleSettings.isVisible();

            if (!isBundle && bundleSettingsVisible) {
                bundleSettings.setVisible(false);
            } else if (isBundle && !bundleSettingsVisible) {
                bundleSettings.setVisible(true);
            }
        });
    }

    @Override
    protected void resetEditorFrom(@NotNull BbRunConfiguration configuration) {
        projectRoot.setText(configuration.getCustomProjectRoot());
        bundle.setSelected(configuration.getBundle());
        fastCheckBox.setSelected(configuration.getFast());
        compressCheckBox.setSelected(configuration.getCompress());
        mangleCheckBox.setSelected(configuration.getMangle());
        beautifyCheckBox.setSelected(configuration.getBeautify());
        newBundlerCheckBox.setSelected(configuration.getNewBundler());

        bundleSettings.setVisible(configuration.getBundle());
    }

    @Override
    protected void applyEditorTo(@NotNull BbRunConfiguration configuration) throws ConfigurationException {
        if (projectRoot.getText().length() > 0) {
            configuration.setCustomProjectRoot(projectRoot.getText());
        } else {
            configuration.setCustomProjectRoot(null);
        }

        configuration.setBundle(bundle.isSelected());
        configuration.setFast(fastCheckBox.isSelected());
        configuration.setCompress(compressCheckBox.isSelected());
        configuration.setMangle(mangleCheckBox.isSelected());
        configuration.setBeautify(beautifyCheckBox.isSelected());
        configuration.setNewBundler(newBundlerCheckBox.isSelected());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return panel;
    }
}