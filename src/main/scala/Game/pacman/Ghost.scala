package Game.pacman

import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.image.Image
import scalafx.scene.paint.Color

import scala.util.Random

case class Ghost(initialX: Double, initialY: Double, var x: Double, var y: Double, var direction: Int,var isVulnerable: Boolean = false)
{

}
object Ghost {
  val size: Double = 20.0
  val normalSpeed: Double = 1.0 // Adjust this value to control the ghost speed
  val vulnerableSpeed: Double = 0.5
  val GhostImage = new Image("images/ghost.gif")
  val blueGhost = new Image("images/blueghost.gif")
  //Draw the ghosts in the game
  def drawGhosts(ghosts: List[Ghost], graphicsContext: GraphicsContext): Unit = {

    ghosts.foreach { ghost =>
      val ghostImage = if (ghost.isVulnerable) blueGhost else GhostImage
      graphicsContext.drawImage(ghostImage, ghost.x, ghost.y, size, size)
    }
  }
  //Ghosts movement
  def moveGhosts(ghosts: List[Ghost], pacman: Pacman, canvas: Canvas, walls: List[(Double, Double)]): List[Ghost] = {
    ghosts.map { ghost =>
      val speed = if (ghost.isVulnerable) vulnerableSpeed else normalSpeed
      val (newX, newY, newDirection) = if (ghost.isVulnerable) {
        moveAwayFromPacman(ghost.x, ghost.y, pacman, walls, speed)
      } else if (sameRowOrColumn(ghost.x, ghost.y, pacman)) {
        chasePacman(ghost.x, ghost.y, pacman, walls, speed)
      } else {
        moveRandomly(ghost.x, ghost.y, ghost.direction, walls, speed)
      }
      ghost.x = newX
      ghost.y = newY
      ghost.direction = newDirection
      ghost
    }
  }
  /***************************************************************************************
   *    Title: ChatGPT source code
   *    Author: ChatGPT 4.o
   *    Date: 8/20/2024
   *    Code version: Scala 2.12.19
   *    Availability: https://chatgpt.com/
   *    Parts : line 50 - line 124
   ***************************************************************************************/
  //Function for when ghosts vulnerable
  private def moveAwayFromPacman(x: Double, y: Double, pacman: Pacman, walls: List[(Double, Double)], speed: Double): (Double, Double, Int) = {
    val dxOptions = if (pacman.x < x) List(speed, 0.0) else List(-speed, 0.0)
    val dyOptions = if (pacman.y < y) List(0.0, speed) else List(0.0, -speed)

    val moves = for {
      dx <- dxOptions
      dy <- dyOptions
      if !Wall.handleWallBlock(x + dx, y + dy, walls)
    } yield (dx, dy)

    if (moves.nonEmpty) {
      val (dx, dy) = moves(Random.nextInt(moves.length))
      moveSmoothly(x, y, dx, dy, walls, speed)
    } else {
      moveRandomly(x, y, Random.nextInt(4), walls, speed)
    }
  }

  //Functions for Ghosts and Pacman at same row or column will chase
  private def sameRowOrColumn(x: Double, y: Double, pacman: Pacman): Boolean = {
    (x / Wall.width).toInt == (pacman.x / Wall.width).toInt || (y / Wall.height).toInt == (pacman.y / Wall.height).toInt
  }
  //Functions for Ghosts chase Pacman
  private def chasePacman(x: Double, y: Double, pacman: Pacman, walls: List[(Double, Double)], speed: Double): (Double, Double, Int) = {
    val path = Pathfinding.aStar((x, y), (pacman.x, pacman.y), walls)
    path match {
      case Some(p :: _) =>
        val dx = p._1 - x
        val dy = p._2 - y
        moveSmoothly(x, y, dx, dy, walls, speed)
      case _ =>
        moveRandomly(x, y, Random.nextInt(4), walls, speed)
    }
  }
  //Ghosts can move Random
  private def moveRandomly(x: Double, y: Double, direction: Int, walls: List[(Double, Double)], speed: Double): (Double, Double, Int) = {
    val (dx, dy) = direction match {
      case 0 => (speed, 0.0)
      case 1 => (-speed, 0.0)
      case 2 => (0.0, speed)
      case 3 => (0.0, -speed)
    }
    val (newX, newY, newDirection) = moveIfPossible(x, y, dx, dy, walls)
    if (newX == x && newY == y) {
      // If stuck, try all directions to find a valid move
      val possibleMoves = List((speed, 0.0), (-speed, 0.0), (0.0, speed), (0.0, -speed))
      val validMoves = possibleMoves.filter { case (dx, dy) => !Wall.handleWallBlock(x + dx, y + dy, walls) }
      if (validMoves.nonEmpty) {
        val (dx, dy) = validMoves(Random.nextInt(validMoves.length))
        (x + dx, y + dy, directionFromDxDy(dx, dy))
      } else {
        (x, y, Random.nextInt(4))
      }
    } else {
      (newX, newY, newDirection)
    }
  }
  //Ghosts move smoothly not stuck at the wall or Shaking there
  private def moveSmoothly(x: Double, y: Double, dx: Double, dy: Double, walls: List[(Double, Double)],speed: Double): (Double, Double, Int) = {
    val stepX = if (dx > 0) math.min(speed, dx) else math.max(-speed, dx)
    val stepY = if (dy > 0) math.min(speed, dy) else math.max(-speed, dy)
    moveIfPossible(x, y, stepX, stepY, walls)
  }
  //Ghosts when wall collision will move other way
  private def moveIfPossible(x: Double, y: Double, dx: Double, dy: Double, walls: List[(Double, Double)]): (Double, Double, Int) = {
    val newX = x + dx
    val newY = y + dy
    if (Wall.handleWallBlock(newX, newY, walls)) {
      (x, y, Random.nextInt(4))
    } else {
      (newX, newY, directionFromDxDy(dx, dy))
    }
  }

  private def directionFromDxDy(dx: Double, dy: Double): Int = {
    if (dx > 0) 0 else if (dx < 0) 1 else if (dy > 0) 2 else 3
  }
  //Reset the ghost position when consumed by pacman
  def resetGhostPositions(ghosts: List[Ghost]): Unit = {
    ghosts.foreach { ghost =>
      ghost.x = ghost.initialX
      ghost.y = ghost.initialY
    }
  }
}


