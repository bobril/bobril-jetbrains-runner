package configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class BbSettingsEditor extends SettingsEditor<BbRunConfiguration> {
    private JPanel panel;

    @Override
    protected void resetEditorFrom(BbRunConfiguration runConfiguration) {

    }

    @Override
    protected void applyEditorTo(BbRunConfiguration rConfiguration) throws ConfigurationException {

    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return panel;
    }
}