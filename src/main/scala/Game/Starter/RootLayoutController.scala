package Game.Starter

import Game.MyApp
import scalafxml.core.macros.sfxml

@sfxml
class RootLayoutController {
  def handleExit(): Unit = {
    MyApp.quitGame()
  }

  def tutorial(): Unit = {
    MyApp.tutorial()
  }


}
