package Game.Starter

import Game.MyApp
import Game.pacman.{Score, ScoreManager}
import javafx.fxml.FXML
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.beans.value.ObservableValue
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}
import scalafxml.core.macros.sfxml
@sfxml
class LeaderBoardController(@FXML private val RankView: TableView[Score],
                            @FXML private val tableNo: TableColumn[Score, Int],
                            @FXML private val tableName: TableColumn[Score, String],
                            @FXML private val tableScore: TableColumn[Score, Int]){


    // Load top 10 scores from database
    val topScores = ScoreManager.getTopScores(10)
    val scoreData = ObservableBuffer(topScores: _*)

  tableNo.cellValueFactory = { cellData =>
    IntegerProperty(scoreData.indexOf(cellData.value) + 1).asInstanceOf[ObservableValue[Int, Int]]
  }

    tableName.cellValueFactory = { cellData: TableColumn.CellDataFeatures[Score, String] =>
      StringProperty(cellData.value.playerName)
    }

    tableScore.cellValueFactory = { cellData: TableColumn.CellDataFeatures[Score, Int] =>
      IntegerProperty(cellData.value.score).asInstanceOf[ObservableValue[Int, Int]]
    }


    // Set items to TableView
    RankView.items = scoreData


  def backToMenu(): Unit = {
    // Navigate back to the main menu
    MyApp.showMainMenu()
  }
}
