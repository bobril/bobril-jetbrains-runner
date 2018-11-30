package tools;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import configuration.connection.data.AgentData;
import configuration.connection.data.CompilationFinishedData;
import services.BbService;
import javax.swing.*;
import java.util.Arrays;

public class BbTools {
    private JPanel mainContent;
    private JPanel buildPanel;
    private JLabel errors;
    private JLabel warnings;
    private JLabel buildTime;
    private JPanel testsPanel;
    private JList errorsList;
    private JScrollPane errorListScrollPane;
    private JLabel failed;
    private JLabel skipped;
    private JLabel total;
    private JLabel testTime;
    private JList testsList;
    private JScrollPane testsListScrollPane;
    private JLabel buildState;
    private JLabel testState;
    private ErrorListModel errorsListModel = new ErrorListModel();
    private TestsListModel testsListModel = new TestsListModel();


    public BbTools() {
        BbService bbService = ServiceManager.getService(BbService.class);
        bbService.setBbTools(this);
        errorsList.setModel(errorsListModel);
        errorsList.setCellRenderer(new ErrorsCellRenderer());
        errorListScrollPane.setVisible(false);
        testsPanel.setVisible(false);
        testsList.setModel(testsListModel);

    }

    public void setProject(Project project) {
        Arrays.stream(errorsList.getListSelectionListeners()).forEach( l -> errorsList.removeListSelectionListener(l));
        errorsList.addMouseListener(new ErrorListClickListener(project, errorsListModel, errorsList));
        testsList.addMouseListener(new TestsListSelectionListener(project, testsListModel, testsList));
    }

    public void setCompileStarted() {
        buildState.setText("Build started");
        buildState.setForeground(JBColor.foreground());
    }

    public void setCompileStopped() {
        buildState.setText("Not Running");
        buildState.setForeground(JBColor.foreground());
    }

    public void setCompilationFinished(CompilationFinishedData finishedData) {
        errors.setText(Integer.toString(finishedData.getErrors()));
        warnings.setText(Integer.toString(finishedData.getWarnings()));
        buildTime.setText((finishedData.getTime() / 1000) + " s");

        boolean errorListVisible = finishedData.getMessages() != null && finishedData.getMessages().size() > 0;
        errorsListModel.setErrors(finishedData.getMessages());
        errorListScrollPane.setVisible(errorListVisible);
        testsPanel.setVisible(!errorListVisible);

        if (finishedData.getErrors() != 0) {
            buildState.setText("Failed");
            buildState.setForeground(JBColor.RED);
        } else if (finishedData.getErrors() != 0) {
            buildState.setText("With warnings");
            buildState.setForeground(JBColor.ORANGE);
        } else {
            buildState.setText("Success");
            buildState.setForeground(JBColor.GREEN);
        }

        testState.setText("Running");
        testState.setForeground(JBColor.foreground());
    }

    public void setTestUpdatedFinished(AgentData agentData) {
        failed.setText(Integer.toString(agentData.getTestsFailed()));
        skipped.setText(Integer.toString(agentData.getTestsSkipped()));
        total.setText(Integer.toString(agentData.getTotalTests()));
        testTime.setText((agentData.getDuration() / 1000) + " s");

        if (agentData.getTestsFailed() > 0) {
            testState.setText("Failed");
            testState.setForeground(JBColor.RED);
        } else {
            testState.setText("Success");
            testState.setForeground(JBColor.GREEN);
        }

        testsListModel.setFailureTests(agentData);
        errorListScrollPane.setVisible(false);
        testsPanel.setVisible(true);
    }

    public JPanel getContent() {
        return mainContent;
    }
}