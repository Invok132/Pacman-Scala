package Game.pacman

import scala.collection.mutable
/***************************************************************************************
 *    Title: ChatGPT source code
 *    Author: ChatGPT 4.o
 *    Date: 8/20/2024
 *    Code version: Scala 2.12.19
 *    Availability: https://chatgpt.com/
 ***************************************************************************************/
case class Node(x: Int, y: Int, g: Int, h: Int, f: Int, parent: Option[Node])

object Pathfinding {

  def heuristic(start: Node, end: Node): Int = {
    Math.abs(start.x - end.x) + Math.abs(start.y - end.y)
  }

  def aStar(start: (Double, Double), goal: (Double, Double), walls: List[(Double, Double)]): Option[List[(Double, Double)]] = {
    val startNode = Node((start._1 / Wall.width).toInt, (start._2 / Wall.height).toInt, 0, 0, 0, None)
    val goalNode = Node((goal._1 / Wall.width).toInt, (goal._2 / Wall.height).toInt, 0, 0, 0, None)

    val openList = mutable.PriorityQueue[Node]()(Ordering.by(-_.f))
    val closedList = mutable.Set[Node]()

    openList.enqueue(startNode)

    while (openList.nonEmpty) {
      val currentNode = openList.dequeue()

      if (currentNode.x == goalNode.x && currentNode.y == goalNode.y) {
        return Some(reconstructPath(currentNode))
      }

      closedList.add(currentNode)

      for (neighbor <- getNeighbors(currentNode, walls)) {
        if (closedList.exists(n => n.x == neighbor.x && n.y == neighbor.y)) {
          // Skip already evaluated neighbors
        } else {
          val tentativeG = currentNode.g + 1

          if (!openList.exists(n => n.x == neighbor.x && n.y == neighbor.y) || tentativeG < neighbor.g) {
            val h = heuristic(neighbor, goalNode)
            val f = tentativeG + h
            val updatedNeighbor = neighbor.copy(g = tentativeG, h = h, f = f, parent = Some(currentNode))

            openList.enqueue(updatedNeighbor)
          }
        }
      }
    }

    None
  }

  private def getNeighbors(node: Node, walls: List[(Double, Double)]): List[Node] = {
    val neighbors = List(
      Node(node.x + 1, node.y, 0, 0, 0, None),
      Node(node.x - 1, node.y, 0, 0, 0, None),
      Node(node.x, node.y + 1, 0, 0, 0, None),
      Node(node.x, node.y - 1, 0, 0, 0, None)
    )

    neighbors.filterNot(n => walls.contains((n.x * Wall.width, n.y * Wall.height)))
  }

  private def reconstructPath(node: Node): List[(Double, Double)] = {
    val path = mutable.ListBuffer[(Double, Double)]()
    var currentNode = node

    while (currentNode.parent.isDefined) {
      path.prepend((currentNode.x * Wall.width, currentNode.y * Wall.height))
      currentNode = currentNode.parent.get
    }

    path.toList
  }
}
//need to cite chatgpt