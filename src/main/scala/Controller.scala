import java.util.concurrent.TimeUnit
import javafx.beans.Observable

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import chess.api.actors.{RegisterObserver, UnregisterObserver}
import chess.api.{Action, InvalidAction, QueryValidActions, Update}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.beans.Observable
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent}

class Controller(model: Model, val view: View) {

  val system = ActorSystem()
  val actorPath = chess.api.actors.Config.controllerActorPath
  val selection = system.actorSelection(actorPath)
  implicit val timeout = akka.util.Timeout(5, TimeUnit.SECONDS)
  val chessCore = Await.result(selection.resolveOne(), Duration.Inf)
  val messageHandler = system.actorOf(Props(new MessageHandler))

  chessCore.tell(RegisterObserver, messageHandler)

  class MessageHandler extends Actor {

    override def receive: Receive = {
      case update: Update => Platform.runLater(handleUpdate(update))
      case invalid@InvalidAction(message) => println(message)
    }

    def handleUpdate(update: Update): Unit = {
      model.dimX() = update.chessBoard.dimX
      model.dimY() = update.chessBoard.dimY
      model.winner() = update.winner
      model.posPieces.flatten.foreach(_() = None)
      model.actions() = List()
      update.chessBoard.pieces.foreach(posPiece => {
        val pos = posPiece._1
        val piece = posPiece._2
        model.posPieces(pos._1)(pos._2)() = Some(piece)
      })
    }
  }

  def start(): Unit = {
    chessCore ! RegisterObserver
  }

  def exit(): Unit = {
    chessCore ! UnregisterObserver
  }

  def executeAction(action: Action) = {
    chessCore.tell(action, messageHandler)
  }

  view.tiles.foreach(tile => {
    // install on click listener
    tile.regions.last.onMouseClicked = (event: MouseEvent) => {
      val pos = (tile.x, tile.y)
      val actions = model.actions().filter(_.target == pos)
      if (actions.size == 1) {
        clearSelection()
        chessCore.tell(actions.head, messageHandler)
      } else if (model.posPieces(tile.x)(tile.y)().isDefined) {
        (chessCore ? QueryValidActions(pos)).map {
          case validActions: Seq[Action] if validActions.nonEmpty =>
            model.actions() = validActions
            model.currentField() = Some(pos)
        }
      } else {
        clearSelection()
      }
    }

    val actionsRegion = tile.regions.collect { case x: MoveAndReplaceChoice => x } .head
    actionsRegion.actionToExecute.onChange((_,_,_) => {
      if (actionsRegion.actionToExecute().isDefined) {
        chessCore.tell(actionsRegion.actionToExecute().get, messageHandler)
        actionsRegion.actionToExecute() = None
        clearSelection()
      }
    })
  })

  view.scene.onKeyPressed = (v: KeyEvent) => v.code match {
    case KeyCode.Escape => clearSelection()
  }

  private def clearSelection(): Unit = {
    model.currentField() = None
    model.actions() = List.empty
  }
}
