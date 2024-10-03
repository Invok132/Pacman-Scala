package Game.pacman

import Game.MyApp
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.layout.Pane
import scalafx.Includes._
import scalafx.animation.{AnimationTimer, PauseTransition}
import scalafx.scene.paint.Color
import scalafx.util.Duration

class PacmanGame(playerName: String)  extends Pane {
  val canvas = new Canvas(600, 400)
  val showContent = canvas.graphicsContext2D
  val map = GameMap.initMap("/Game/pacman/level1.txt")
  var score = 0
  var currentDirection: Option[KeyCode] = None
  var gameRunning = true
  var pacman = new Pacman(map.pacmanStart.x * Wall.width, map.pacmanStart.y * Wall.height)
  //  var dots = Dot.initializeDots(600, 400)
  var dots = map.grid.zipWithIndex.flatMap { case (row, y) =>
    row.zipWithIndex.collect {
      //dots position
      case (CellValue.SMALLDOT, x) =>
        SmallDot(x * Wall.width + (Wall.width - 10.0) / 2, y * Wall.height + (Wall.height - 10.0) / 2)
      case (CellValue.BIGDOT, x) =>
        BigDot(x * Wall.width + (Wall.width - 20.0) / 2, y * Wall.height + (Wall.height - 20.0) / 2)
    }
  }.toList
  var ghosts = List(
    //ghost spawn
    Ghost(map.ghost1Start.x * Wall.width, map.ghost1Start.y * Wall.height, map.ghost1Start.x * Wall.width, map.ghost1Start.y * Wall.height, 0),
    Ghost(map.ghost2Start.x * Wall.width, map.ghost2Start.y * Wall.height, map.ghost2Start.x * Wall.width, map.ghost2Start.y * Wall.height, 1)
  )
  //generate walls
  var walls = map.grid.zipWithIndex.flatMap { case (row, y) =>
    row.zipWithIndex.collect { case (CellValue.WALL, x) => (x * Wall.width, y * Wall.height) }
  }.toList
  //Control Key
  this.onKeyPressed = (event: KeyEvent) => {
    pacman.setDirection(event.code)
  }
  this.onKeyReleased = (event: KeyEvent) => {
    pacman.stop()
  }
  this.focusTraversable = true
  this.requestFocus()

  val timer = AnimationTimer(t => {
    if (gameRunning) {
      pacman.move(canvas, walls)
      ghosts = Ghost.moveGhosts(ghosts, pacman, canvas, walls)
      eatDot()
      handleGhostEatPac()
      draw()
      handleGameEnd()
    }
  })
  timer.start()

  children.add(canvas)
  draw()
  //Draw the game map
  def draw(): Unit = {
    showContent.fill = Color.Black
    showContent.fillRect(0, 0, canvas.width.value, canvas.height.value)
    dots.foreach(_.draw(showContent))
    pacman.draw(showContent)
    Ghost.drawGhosts(ghosts, showContent)
    Wall.drawWalls(walls, showContent)
    showContent.fill = Color.White
    showContent.fillText(s"Score: $score, Lives: $health, Player: $playerName", 10, 20)
  }
  //When Pacman eat Dots
  def eatDot(): Unit = {
    dots = dots.filterNot { dot =>
      if (collideDot(pacman, dot)) {
        dot match {
          case _: BigDot => powerUpMode(); ScoreManager.addScore(50)
          case _: SmallDot => ScoreManager.addScore(10)
        }
        score = ScoreManager.getScore
        true
      } else {
        false
      }
    }
  }
  //pacman collide the dots
  private def collideDot(pacman: Pacman, dot: Dot): Boolean = {
    val distDot = math.sqrt(math.pow(pacman.x + pacman.size / 2 - dot.x, 2) + math.pow(pacman.y + pacman.size / 2 - dot.y, 2))
    distDot < pacman.size / 2 + dot.size / 2
  }


  private def powerUpMode(): Unit = {
    //pacman poweredup mode
    pacman.isPoweredUp = true
    ghosts.foreach(_.isVulnerable = true)
    //powered mode end at 6seconds
    val pause = new PauseTransition(Duration(6000))
    pause.onFinished = _ => {
      pacman.isPoweredUp = false
      ghosts.foreach(_.isVulnerable = false)
    }
    pause.play()
  }

  /***************************************************************************************
   *    Title: ChatGPT source code
   *    Author: ChatGPT 4.o
   *    Date: 8/20/2024
   *    Code version: Scala 2.12.19
   *    Availability: https://chatgpt.com/
   *    Parts : line 115 - line 138
   ***************************************************************************************/
    //When the ghosts eat pacman
    var health = 3
  def handleGhostEatPac(): Unit = {
    ghosts.foreach { ghost =>
      val distance = Math.sqrt(Math.pow(pacman.x + pacman.size / 2 - ghost.x, 2) + Math.pow(pacman.y + pacman.size / 2 - ghost.y, 2))
      if (distance < pacman.size) {
        if (ghost.isVulnerable) {
          ghost.x = ghost.initialX
          ghost.y = ghost.initialY
          ghost.isVulnerable = false
          ScoreManager.addScore(200)
          score = ScoreManager.getScore
        } else {
          health -= 1
          if (health > 0) {
            pacman.resetPosition()
            Ghost.resetGhostPositions(ghosts)
            val pause = new PauseTransition()
            pause.duration = Duration(1000)
            pause.onFinished = _ => gameRunning = true
            pause.play()
          } else {
            gameRunning = false
            showContent.fill = Color.White
            showContent.fillText("Game Over!", canvas.width.value / 2 - 30, canvas.height.value / 2)
            ScoreManager.saveScore(playerName, score)
            val pause = new PauseTransition(Duration(2000))
            pause.onFinished = _ => {
              MyApp.showMainMenu()
          }
            pause.play()
        }
      }
    }
  }
}
  //When the game end
  def handleGameEnd(): Unit = {
    if (dots.isEmpty) {
      gameRunning = false
      showContent.fill = Color.White
      showContent.fillText("You Won!", canvas.width.value / 2 - 30, canvas.height.value / 2)
      ScoreManager.saveScore(playerName, score)
      val pause = new PauseTransition(Duration(2000))
      pause.onFinished = _ => {
        MyApp.showMainMenu()
      }
      pause.play()
    }
  }
}
