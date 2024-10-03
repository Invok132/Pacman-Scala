package Game.Starter

import Game.MyApp
import scalafxml.core.macros.sfxml


@sfxml
class HowToPlayController(){
  def handleMainMenu(): Unit = {
    MyApp.showMainMenu()
  }
}
