package Game

import javafx.{scene => jfxs}
import scalafx.application.{JFXApp, Platform}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.{AnchorPane, BorderPane}
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import Game.pacman.{PacmanGame, ScoreManager}
import javafx.scene.layout

object MyApp extends JFXApp {

  var playerName: String = ""

  // Load the FXML file
  val rootResource = getClass.getResource("/viewEditor/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load()

  // Get the root node from the FXML file
  val rootPane = new BorderPane(loader.getRoot[layout.BorderPane])

  // Set up the scene
  stage = new PrimaryStage {
    title = "Pacman"
    scene = new Scene {
      stylesheets = List(getClass.getResource("/viewEditor/background.css").toString)
      root = rootPane
    }
  }

  // Function to show welcome screen
  def showWelcome(): Unit = {
    val resource = getClass.getResource("/viewEditor/StartActivity.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val newRootPane = loader.getRoot[jfxs.layout.AnchorPane]
    rootPane.setCenter(newRootPane)
  }

  //Function to show main menu
  def showMainMenu(): Unit = {
    val resource = getClass.getResource("/viewEditor/MainMenu.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val mainMenuRoot = new AnchorPane(loader.getRoot[layout.AnchorPane])
    val mainMenuScene = new Scene(mainMenuRoot)
    stage.scene = mainMenuScene
    stage.show()
  }



  // Function to launch game
  def launchGame(playerName: String): Unit = {
    this.playerName = playerName
    val gamePane = new PacmanGame(playerName)
    val gameScene = new Scene(gamePane)
    stage.scene = gameScene
    // Request focus for the game pane to capture keyboard events
    gamePane.requestFocus()
  }

  def showLeaderBoard(): Unit = {
    val resource = getClass.getResource("/viewEditor/LeaderBoard.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val newRootPane = new AnchorPane(loader.getRoot[jfxs.layout.AnchorPane])

    // Create a new scene for the leaderboard
    val leaderboardScene = new Scene(newRootPane)

    // Set the new scene to the primary stage
    stage.scene = leaderboardScene
    stage.show()
  }


  // Function to quit
    def quitGame(): Unit = {
    Platform.exit()
    System.exit(0)
  }

    def tutorial(): Unit = {
      val resource = getClass.getResource("/viewEditor/HowToPlay.fxml")
      val loader = new FXMLLoader(resource, NoDependencyResolver)
      loader.load()
      val newRootPane = loader.getRoot[jfxs.layout.AnchorPane]
      rootPane.setCenter(newRootPane)
    }


  showWelcome()
}
/***************************************************************************************
 *    Title: AddressApp
 *    Author: Dr Chin Teck Min
 *    Date: 8/21/2024
 *    Code version: Scala 2.12.19
 *    Availability: Practical Lab Work
 ***************************************************************************************/