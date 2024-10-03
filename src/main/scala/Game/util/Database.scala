package Game.util

import Game.pacman.{Score, ScoreManager}
import scalikejdbc._

trait Database{
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:pacmanDB;create=true;"

  Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "", "")

  implicit val session: AutoSession = AutoSession
  println("Database connection initialized")
}

object Database extends Database{
  def setupDB(): Unit = {
    if (!hasDBInitialized){
      println("Setting up database...")
      ScoreManager.initTable()
    }else{
      println("Database already initialized.")
    }
  }

  def hasDBInitialized: Boolean = {
    DB.getTable("scores") match{
      case Some(_) => true
      case None => false
    }
  }
}
/***************************************************************************************
 *    Title: AddressApp
 *    Author: Dr Chin Teck Min
 *    Date: 8/21/2024
 *    Code version: Scala 2.12.19
 *    Availability: Practical Lab Work
 ***************************************************************************************/