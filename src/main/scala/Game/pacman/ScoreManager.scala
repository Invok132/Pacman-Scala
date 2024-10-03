package Game.pacman

import Game.util.Database
import scalikejdbc.{DB, SQLSyntaxSupport, WrappedResultSet, scalikejdbcSQLInterpolationImplicitDef}

import scala.util.Try

case class Score(id: Int, playerName: String, score: Int)

object ScoreManager extends Database {
  private var score: Int = 0

  def resetScore(): Unit = {
    score = 0
  }

  def addScore(points: Int): Unit = {
    score += points
  }

  def getScore: Int = score

  def saveScore(playerName: String, score: Int): Try[Int] = {
    println(s"Saving score: $score for player: $playerName")
    Try(DB.autoCommit { implicit session =>
      sql"""
           insert into scores (playerName, score) values ($playerName, $score)
           """.update.apply()
    })

  }

  def getTopScores(limit: Int): List[Score] = {
    DB.readOnly { implicit session =>
      sql"""
      SELECT * FROM scores ORDER BY score DESC FETCH FIRST $limit ROWS ONLY
    """.map(rs => Score(rs.int("id"), rs.string("playerName"), rs.int("score"))).list.apply()
    }
  }

  def initTable(): Unit = {
    if (!hasTableInitialized) {
      DB.autoCommit { implicit session =>
        sql"""
              create table if not exists scores (
              playerName varchar(64),
              score int
              )
             """.execute.apply()
        println("Scores table created")
      }
    }
  }

  private def hasTableInitialized(): Boolean = {
    DB.getTable("scores").isDefined
  }
}
/***************************************************************************************
 *    Title: AddressApp
 *    Author: Dr Chin Teck Min
 *    Date: 8/21/2024
 *    Code version: Scala 2.12.19
 *    Availability: Practical Lab Work
 ***************************************************************************************/