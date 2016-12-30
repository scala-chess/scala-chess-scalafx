
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage

object App extends JFXApp {

  val model = new Model()
  val view = new View(model)
  val controller = new Controller(model, view)

  controller.start()

  override def stopApp(): Unit = controller.exit()

  stage = new PrimaryStage {
    scene = view.scene
  }

}
