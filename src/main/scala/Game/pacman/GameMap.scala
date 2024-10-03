package Game.pacman
/***************************************************************************************
 *    Title: pacman source code
 *    Author: jbaskauf
 *    Date: 8/20/2024
 *    Code version: Scala 2.12.19
 *    Availability: https://github.com/jbaskauf/pacman
 ***************************************************************************************/
import scala.io.Source
import scalafx.geometry.Point2D

import java.io.FileNotFoundException

object CellValue extends Enumeration {
  type CellValue = Value
  val WALL, SMALLDOT, BIGDOT, GHOST1HOME, GHOST2HOME, PACMANHOME, EMPTY = Value
}

case class Level(grid: Array[Array[CellValue.Value]], pacmanStart: Point2D, ghost1Start: Point2D, ghost2Start: Point2D)
//represent objects in the map
object GameMap {
  def initMap(fileName: String): Level = {
    val resourcePath = getClass.getResourceAsStream(fileName)
    if (resourcePath == null) {
      throw new FileNotFoundException(s"Resource not found: $fileName")
    }

    val source = Source.fromInputStream(resourcePath)
    val lines = source.getLines().toList
    source.close()

    val rowCount = lines.length
    val columnCount = lines.head.length

    val grid = Array.ofDim[CellValue.Value](rowCount, columnCount)
    var pacmanStart = Point2D.Zero
    var ghost1Start = Point2D.Zero
    var ghost2Start = Point2D.Zero

    for ((line, row) <- lines.zipWithIndex) {
      for ((char, column) <- line.zipWithIndex) {
        val cellValue = char match {
          case 'W' => CellValue.WALL
          case 'S' => CellValue.SMALLDOT
          case 'B' => CellValue.BIGDOT
          case '1' =>
            ghost1Start = new Point2D(column, row)
            CellValue.GHOST1HOME
          case '2' =>
            ghost2Start = new Point2D(column, row)
            CellValue.GHOST2HOME
          case 'P' =>
            pacmanStart = new Point2D(column, row)
            CellValue.PACMANHOME
          case _ => CellValue.EMPTY
        }
        if (row >= grid.length || column >= grid(0).length) {
          println(s"Warning: Skipping out-of-bounds index ($row, $column)")
        } else {
          grid(row)(column) = cellValue
        }
      }
    }
    Level(grid, pacmanStart, ghost1Start, ghost2Start)
  }
}
