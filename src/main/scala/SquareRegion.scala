import chess.api.Position

import scalafx.scene.layout.Region

case class SquareRegion(position: Position) extends Region {

  def blackOrWhite = {
    val isBlack = if (position._1 % 2 == 0) (position._2 % 2) == 1 else (position._2 % 2) == 0
    if (isBlack) "-fx-background-color: rgba(117, 117, 117, 0.7);" else "-fx-background-color: rgb(230,230,230);"
  }

  style = blackOrWhite

  prefHeight = 80
  prefWidth = 80

}
