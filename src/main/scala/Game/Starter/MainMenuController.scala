package Game.Starter

import Game.MyApp
import Game.pacman.{Score, ScoreManager}
import scalafx.event.ActionEvent
import scalafx.scene.control.TextField
import scalafxml.core.macros.sfxml


@sfxml
class MainMenuController(private val playerName: TextField){


  def StartGame(): Unit = {
    val name = playerName.text.value.trim
    if(name.nonEmpty){
      println(s"Starting game for player: $name ")
      ScoreManager.getTopScores(10)
      MyApp.launchGame(name)
    }else{
      println("Please enter a name. ")
    }

  }

  def leaderBoard(event: ActionEvent): Unit = {
   MyApp.showLeaderBoard()
  }

  def quit(event: ActionEvent): Unit = {
    MyApp.quitGame()
  }

}
