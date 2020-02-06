package configuration

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element
import org.jetbrains.annotations.NotNull

class BbRunConfiguration(project: Project, factory: ConfigurationFactory, name: String) : RunConfigurationBase<String>(project, factory, name) {
    var customProjectRoot: String? = null
    var bundle: Boolean = false
    var fast: Boolean = true
    var newBundler: Boolean = true
    var compress: Boolean = true
    var mangle: Boolean = false
    var beautify: Boolean = true

    @NotNull
    override fun getConfigurationEditor(): SettingsEditor<out BbRunConfiguration> {
        return BbSettingsEditor()
    }

    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment): RunProfileState? {
        return BbRunProfileState(executionEnvironment, project, this)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        customProjectRoot = JDOMExternalizerUtil.readField(element, KEY_CUSTOM_PROJECT_ROOT)
        bundle = JDOMExternalizerUtil.readField(element, KEY_BUNDLE)?.toBoolean() ?: false
        fast = JDOMExternalizerUtil.readField(element, KEY_FAST)?.toBoolean() ?: false
        compress = JDOMExternalizerUtil.readField(element, KEY_COMPRESS)?.toBoolean() ?: false
        mangle = JDOMExternalizerUtil.readField(element, KEY_MANGLE)?.toBoolean() ?: false
        beautify = JDOMExternalizerUtil.readField(element, KEY_BEAUTIFY)?.toBoolean() ?: false
        newBundler = JDOMExternalizerUtil.readField(element, KEY_NEW_BUNDLER)?.toBoolean() ?: false
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, KEY_CUSTOM_PROJECT_ROOT, customProjectRoot)
        JDOMExternalizerUtil.writeField(element, KEY_BUNDLE, bundle.toString())
        JDOMExternalizerUtil.writeField(element, KEY_NEW_BUNDLER, newBundler.toString())
        JDOMExternalizerUtil.writeField(element, KEY_FAST, fast.toString())
        JDOMExternalizerUtil.writeField(element, KEY_COMPRESS, compress.toString())
        JDOMExternalizerUtil.writeField(element, KEY_MANGLE, mangle.toString())
        JDOMExternalizerUtil.writeField(element, KEY_BEAUTIFY, beautify.toString())
    }

    companion object {
        val KEY_CUSTOM_PROJECT_ROOT = "CUSTOM_PROJECT_ROOT"
        val KEY_BUNDLE = "BUNDLE"
        val KEY_FAST = "BUNDLE"
        val KEY_NEW_BUNDLER = "NEW_BUNDLER"
        val KEY_COMPRESS = "COMPRESS"
        val KEY_MANGLE = "MANGLE"
        val KEY_BEAUTIFY = "BEAUTIFY"
    }
}