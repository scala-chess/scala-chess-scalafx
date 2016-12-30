import chess.api.Position

import scalafx.beans.property.BooleanProperty
import scalafx.scene.layout.Region
import scalafx.Includes._

case class NumberOfActionsRegion(model: Model, position: Position) extends Region {

  val showNumberOfActions = BooleanProperty(false)
  model.actions.onChange((a, b, c) => {
    showNumberOfActions() = model.actions().count(a => a.target == position) > 1
  })

  def color = "-fx-background-color: darkgrey;"
  def somePadding = "-fx-padding: 30px; -fx-border-insets: 30px; -fx-background-insets: 30px;"
  def star = "-fx-shape: \"M 160.000 180.000 L 180.000 194.641 L 177.321 170.000 L 200.000 160.000 L 177.321 150.000 L 180.000 125.359 L 160.000 140.000 L 140.000 125.359 L 142.679 150.000 L 120.000 160.000 L 142.679 170.000 L 140.000 194.641 z\";"

  style <== (
    when(showNumberOfActions) choose color + somePadding + star
      otherwise ""
  )

}
