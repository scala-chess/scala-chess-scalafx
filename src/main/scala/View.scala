import chess.api.Position

import scalafx.geometry.{HPos, Insets}
import scalafx.scene.Scene
import scalafx.scene.layout._

class View(val model: Model) {

  val centerPane = new GridPane {
    hgap = 10
    vgap = 10
    padding = Insets(10)
    columnConstraints = List(new ColumnConstraints {
        halignment = HPos.Right
        minWidth = 65
      },
      new ColumnConstraints {
        halignment = HPos.Left
        minWidth = 200
      }
    )
  }

  lazy val tiles: Seq[Tile] = {
    val positions = for {
      x <- 0 until model.dimX()
      y <- 0 until model.dimY()
    } yield (x, y)
    positions.map { position =>
      val x = position._1
      val y = position._2
      val square = SquareRegion(position)
      val actionOverlay = ActionOverlayRegion(model, position)
      val actions = MoveAndReplaceChoice(model, position)
      val chessPiece = PieceRegion()
      chessPiece.maybePiece <== model.posPieces(x)(y)
      Tile(position, square, actionOverlay, chessPiece, actions)
    }
  }

  def board(): GridPane = {
    val board = new GridPane()
    tiles.foreach(tile => {
      board.add(new StackPane() {
        children = tile.regions
      }, tile.x, tile.y)
    })
    board
  }

  val game = new BorderPane() {
    center = new StackPane() {
      children = board()
    }
  }

  val scene = new Scene(640, 640) {
    root = new AnchorPane() {
      children = List(
        game
      )
    }
  }

  case class Tile(position: Position, regions: Region*) {
    def x = position._1
    def y = position._2
  }
}
