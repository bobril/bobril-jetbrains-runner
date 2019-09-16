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

    public BbSettingsEditor() {
        projectRoot.addBrowseFolderListener(null,
                null,
                ProjectManager.getInstance().getOpenProjects()[0],
                FileChooserDescriptorFactory.createSingleFolderDescriptor());
    }

    @Override
    protected void resetEditorFrom(@NotNull BbRunConfiguration configuration) {
        projectRoot.setText(configuration.getCustomProjectRoot());
    }

    @Override
    protected void applyEditorTo(@NotNull BbRunConfiguration configuration) throws ConfigurationException {
        if (projectRoot.getText().length() > 0) {
            configuration.setCustomProjectRoot(projectRoot.getText());
        } else {
            configuration.setCustomProjectRoot(null);
        }
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return panel;
    }
}