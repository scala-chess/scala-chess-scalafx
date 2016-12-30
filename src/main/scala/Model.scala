
import chess.api.{Action, Color, Piece, Position}

import scalafx.beans.property.{IntegerProperty, ObjectProperty}

class Model {
  val size = 8
  val dimX = IntegerProperty(8)
  val dimY = IntegerProperty(8)
  val posPieces = Array.tabulate(size, size)((_,_) => ObjectProperty[Option[Piece]](None))
  val currentField = ObjectProperty[Option[Position]](None)
  val actions = ObjectProperty[Seq[Action]](List.empty)
  val winner = ObjectProperty[Option[Color.Value]](None)
}
