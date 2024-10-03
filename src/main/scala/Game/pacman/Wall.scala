package Game.pacman
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color

object Wall {
  val width: Double = 20.0
  val height: Double = 20.0
  //Draw the walls
  def drawWalls(walls: List[(Double, Double)], graphicsContext: GraphicsContext): Unit = {
    graphicsContext.fill = Color.Blue
    walls.foreach { case (x, y) =>
      graphicsContext.fillRect(x, y, width, height)
    }
  }
  //Ensure ghosts and pacman go through the walls
  def handleWallBlock(x: Double, y: Double, walls: List[(Double, Double)]): Boolean = {
    walls.exists { case (wallX, wallY) =>
      x < wallX + width &&
        x + width > wallX &&
        y < wallY + height &&
        y + height > wallY
    }
  }
}
