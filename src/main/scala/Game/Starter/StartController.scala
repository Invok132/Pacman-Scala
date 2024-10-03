package Game.Starter

import Game.MyApp
import scalafxml.core.macros.sfxml


@sfxml
class StartController(){
  def handleGetStarted(): Unit = {
    MyApp.showMainMenu()
  }
}
