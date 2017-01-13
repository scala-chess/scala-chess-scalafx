import chess.api.{Action, MoveAndReplace, Position}

import scalafx.application.Platform
import scalafx.beans.property.{DoubleProperty, ObjectProperty}
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.ComboBox
import scalafx.scene.layout.Pane
import scalafx.util.StringConverter
import scalafx.Includes._

case class MoveAndReplaceChoice(model: Model, position: Position) extends Pane {

  val actions = ObjectProperty[Seq[Action]](List.empty)
  val comboBox = ObjectProperty[ComboBox[MoveAndReplace]](new ComboBox[MoveAndReplace])
  val actionToExecute = ObjectProperty[Option[MoveAndReplace]](None)

  model.actions.onChange((a, b, c) => {
    actions() = List.empty
    val relevantActions = model.actions().filter(_.target == position).collect({case x: MoveAndReplace => x})
    if (relevantActions.size > 1) {
      comboBox() = new ComboBox[MoveAndReplace]() {
        items = ObservableBuffer(relevantActions)
        converter = StringConverter.toStringConverter((x: MoveAndReplace) => x.choice)
        onAction = (event: ActionEvent) => {
          actionToExecute() = Some(comboBox().value())
        }
      }
      Platform.runLater {
        children = comboBox()
      }
    } else {
      Platform.runLater {
        children = List()
      }
    }
  })


}
