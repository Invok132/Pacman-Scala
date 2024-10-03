package Game.pacman

import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image
import scalafx.scene.input.KeyCode
import scalafx.scene.canvas.Canvas

class Pacman(var x: Double, var y: Double) {
  val size: Double = 20.0
  var isPoweredUp: Boolean = false

  //Show the Pacman
  val images: Map[KeyCode, Image] = Map(
    KeyCode.Up -> new Image(getClass.getResource("/images/pacmanUp.gif").toString, size, size, true, true),
    KeyCode.Down -> new Image(getClass.getResource("/images/pacmanDown.gif").toString, size, size, true, true),
    KeyCode.Left -> new Image(getClass.getResource("/images/pacmanLeft.gif").toString, size, size, true, true),
    KeyCode.Right -> new Image(getClass.getResource("/images/pacmanRight.gif").toString, size, size, true, true)
  )
  var currentDirection: Option[KeyCode] = None
  var nextDirection: Option[KeyCode] = None

  private val startX = x
  private val startY = y
  // Move Pacman based on the current and next direction
  def move(canvas: Canvas, walls: List[(Double, Double)]): Unit = {
    val stepSize = 1.0
    // Update current direction if next direction is set and valid
    nextDirection match {
      case Some(dir) if canMove(dir, walls) => currentDirection = nextDirection
      case _ =>
    }
    // Move in the current direction if possible
    currentDirection match {
      case Some(KeyCode.Up) if !Wall.handleWallBlock(x, y - stepSize, walls) => y -= stepSize
      case Some(KeyCode.Down) if !Wall.handleWallBlock(x, y + stepSize, walls) => y += stepSize
      case Some(KeyCode.Left) if !Wall.handleWallBlock(x - stepSize, y, walls) => x -= stepSize
      case Some(KeyCode.Right) if !Wall.handleWallBlock(x + stepSize, y, walls) => x += stepSize
      case _ =>
    }

    // Ensure Pacman stays within the canvas boundaries
    if (x < 0) x = 0
    if (x > canvas.width.value - size) x = canvas.width.value - size
    if (y < 0) y = 0
    if (y > canvas.height.value - size) y = canvas.height.value - size
  }
  // Check if Pacman can move in the given direction without hitting a wall
  def canMove(direction: KeyCode, walls: List[(Double, Double)]): Boolean = {
    val stepSize = 1.0
    direction match {
      case KeyCode.Up => !Wall.handleWallBlock(x, y - stepSize, walls)
      case KeyCode.Down => !Wall.handleWallBlock(x, y + stepSize, walls)
      case KeyCode.Left => !Wall.handleWallBlock(x - stepSize, y, walls)
      case KeyCode.Right => !Wall.handleWallBlock(x + stepSize, y, walls)
      case _ => false
    }
  }
  // Draw Pacman on the canvas
    def draw(graphicsContext: GraphicsContext): Unit = {
      // Use the image corresponding to the current direction, default to Right if no direction is set
      val imageToDraw = currentDirection.flatMap(images.get).getOrElse(images(KeyCode.Right))
      graphicsContext.drawImage(imageToDraw, x, y)
    }
// Reset Pacman's position when ate by ghosts
    def resetPosition(): Unit = {
      x = startX
      y = startY
      currentDirection = None
      nextDirection = None
    }
  // Set the next direction Pacman should move in
  def setDirection(direction: KeyCode): Unit = {
    nextDirection = Some(direction)
  }

  // Stop Pacman's movement
    def stop(): Unit = {
      nextDirection = None
    }


}