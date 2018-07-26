# Intelli-J Plug-in Development examples

[toc]

## Logging

Choose showing log level on idea.log tab. It may be `warnings` by default.

```scala
import com.intellij.openapi.diagnostic.Logger  
...
  val logger = Logger.getInstance(classOf[HogeAction])  
...
  logger.info("aiueo")
```

## Add tool menu

* plugin.xml

```xml
        <action id="HelloAction" class="com.yuchesc.intellij.ToolMenuHelloAction" text="Hello">
            <add-to-group group-id="ToolsMenu" anchor="first"/>
            <keyboard-shortcut keymap="$default" first-keystroke="ctrl F12"/>
        </action>
```

* code

```scala
class ToolMenuHelloAction extends AnAction {
  override def actionPerformed(e: AnActionEvent) {
    Notifications.Bus.notify(
      new Notification("sample", "Hello Plugin!", "Hello! This is Sample Plugin.", NotificationType.INFORMATION)
    )
  }
}
```

## Add MainMenu

* plugin.xml

```xml
        <group id="MainMenuHello" text="_Sample Menu" description="Sample menu">
            <add-to-group group-id="MainMenu" anchor="last"/>
            <action id="MainMenuHelloAction" class="com.yuchesc.intellij.MainMenuHelloAction" text="MainMenuHello"/>
            <action id="MainMenuHelloAction2" class="com.yuchesc.intellij.MainMenuHelloAction" text="MainMenuHello2"/>
        </group>
```

* code

```scala
class MainMenuHelloAction extends AnAction {
  override def actionPerformed(e: AnActionEvent) {
    val project = e.getData(PlatformDataKeys.PROJECT_CONTEXT)
    val txt = Option(Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon))
    txt.foreach(tx => {
      Messages.showMessageDialog(project, "Hello, " + tx + "!\n I am glad to see you.", "Information", Messages.getInformationIcon)
    })
  }
}
```

## Get selected text

```xml
    <actions>
        <action id="SelectedTextAction" class="com.yuchesc.intellij.SelectedTextAction" text="WordLookup">
            <add-to-group group-id="ToolsMenu" anchor="first"/>
            <keyboard-shortcut keymap="$default" first-keystroke="ctrl C" second-keystroke="ctrl L"/>
        </action>
    </actions>
```

* code

```scala
class SelectedTextAction extends AnAction {

  // enable this action while editing text
  override def update(e: AnActionEvent): Unit =
    e.getPresentation.setEnabled(e.getData(CommonDataKeys.EDITOR) != null)

  override def actionPerformed(e: AnActionEvent) {
    // Get selected text
    val editor = e.getData(CommonDataKeys.EDITOR)
    val selectionModel = editor.getSelectionModel
    val selectedWord = selectionModel.getSelectedText

    val project = e.getData(CommonDataKeys.PROJECT)
    Messages.showMessageDialog(project, s"selected:[$selectedWord]", "Information", Messages.getInformationIcon)
  }
}
```

## Persistent

```xml
    <extensions defaultExtensionNs="com.intellij">
        <projectService serviceInterface="com.yuchesc.intellij.SamplePersistent" serviceImplementation="com.yuchesc.intellij.SamplePersistent"/>
    </extensions>
```

* code (state)

```scala
// Specify file name.
// @State(name = "Samplep", storages = Array(new Storage("sample-project.xml")))
// Write data in misc.xml as project-level.
@State(name = "Samplep")
class SamplePersistent extends PersistentStateComponent[SamplePersistent] {
  private var _word = ""

  def word: String = _word

  def setWord(word: String): Unit = {
    this._word = word
  }

  override def loadState(t: SamplePersistent): Unit =
    XmlSerializerUtil.copyBean(t, this)

  override def getState: SamplePersistent = this
}

object SamplePersistent {
  def getInstance(project: Project): SamplePersistent = ServiceManager.getService(project, classOf[SamplePersistent])
}
```

* code

```scala
    // Get persistent data
    val project = e.getData(CommonDataKeys.PROJECT)
    val persistent = SamplePersistent.getInstance(project)
    // Get
    persistent.word
    // Set
    persistent.setWord(selectedWord)
```

## JBPopup

* plugin.xml

```xml
        <action id="com.yuchesc.intellij.PopupAction" class="com.yuchesc.intellij.PopupAction" text="PopupAction">
            <add-to-group group-id="ToolsMenu" anchor="first"/>
            <keyboard-shortcut keymap="$default" first-keystroke="ctrl C" second-keystroke="shift P"/>
        </action>
```

* code
```scala
class PopupAction extends AnAction {
  def createPopup(): JBPopup = {
    val panel = new JPanel(new FlowLayout())
    val label = new JLabel("Hello World!!")
    panel.add(label)
    JBPopupFactory.getInstance.createComponentPopupBuilder(panel, null)
      .setTitle("Popup Title")
      .setFocusable(false)
      .setRequestFocus(false)
      .setMayBeParent(false)
      .setMovable(true)
      .setMinSize(new Dimension(200, 200))
      .setCancelOnClickOutside(true)
      .setCancelOnOtherWindowOpen(false)
      .setCancelKeyEnabled(true)
      .createPopup
  }

  var currentPopup: Option[JBPopup] = None

  // enable this action while editing text
  override def update(e: AnActionEvent): Unit =
    e.getPresentation.setEnabled(e.getData(CommonDataKeys.EDITOR) != null)

  override def actionPerformed(e: AnActionEvent): Unit = {
    val editor = e.getData(CommonDataKeys.EDITOR)

    currentPopup.foreach { p =>
      p.setUiVisible(false)
      p.dispose()
    }

    currentPopup = Option(createPopup())
    currentPopup.foreach(_.showInBestPositionFor(editor))
  }
}
```

Next: [Intelli-J Plug-in Development highlighter examples using Scala](intellij-plugin-examples-highlighter.md) 