package Game.pacman

import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image
trait Dot{
  val x: Double
  val y: Double
  val size: Double
  val image: Image
//draw the dots
  def draw(graphicsContext: GraphicsContext): Unit = {
    graphicsContext.drawImage(image, x, y)
  }
}

  case class SmallDot(x: Double, y: Double) extends Dot{
    val size = 10.0
    val image = new Image(getClass.getResource("/images/smalldot.png").toString, size, size, true, true)
  }

case class BigDot(x: Double, y: Double) extends Dot{
  val size = 20.0
  val image = new Image(getClass.getResource("/images/whitedot.png").toString, size, size, true, true)
}