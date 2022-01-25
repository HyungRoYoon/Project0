import java.sql.Connection
import scala.io.StdIn.readLine
import util.control.Breaks.{break, _}

class LogDatabase {
  var attemptedName: String = _
  val closeMySQL = new ConnectMySQL
  var connectionHolder : Connection = _

  def main(args: Array[String]): Unit = {

  }

  def searchNameDatabase(connection: Connection): Unit = {
    //save connection to local variable for future use
    connectionHolder = connection
    //search crew name from database
    if (attemptedName != null) {
      try {
        val searchDBStatement = connection.createStatement
        val searchDBrs = searchDBStatement.executeQuery("SELECT crew_name, text FROM crew_logs")

        breakable {
          while (searchDBrs.next) {
            val searchedName = searchDBrs.getString("crew_name")
            val crewLog = searchDBrs.getString("text")
            if (attemptedName == searchedName) {
              println(s"$crewLog")
              println(" ")
              getUserChoice()
            }
          }
        }
      }
      catch {
        case e: Exception => e.printStackTrace
      }
      val closeMySQL = new ConnectMySQL
      closeMySQL.closeConnection()
    }
  }

  def getUserChoice(): Unit = {
    userChoices(readLine("Please enter 1 to customize crew search, 2 to modify crew's log database, 9 to go back, or 0 to exit the program: ").toInt)
    println(" ")
  }

  def userChoices(x: Int): Unit = x match {
    case 0 => CrewRecords.quitProgram()
    case 1 => diyReadQuery(connectionHolder)
    case 2 => diyModifyQuery(connectionHolder)
    case 9 => goBack()
    case _ => reEnterNumber(x)
  }

  def reEnterNumber(x: Int) : Unit = {
    if (x != 0 && x != 9) println("Please select shown numbers.")
    getUserChoice()
  }

  def goBack(): Unit = {
    CrewRecords.getUserChoice(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
  }

  def diyReadQuery(connection: Connection): Unit = {
    val diyRequest = readLine("Please enter your query in the order of SELECT, FROM, WHERE, GROUP BY, HAVING, ORDER BY: ")
    try {
      val diyStatement = connection.createStatement
      val diyDBrs = diyStatement.executeQuery(diyRequest)

      breakable {
        while (diyDBrs.next) {
          val crewName = diyDBrs.getString("crew_name")
          val logDate = diyDBrs.getString("date_saved")
          val crewLog = diyDBrs.getString("text")

          println(s"crew_name is: $crewName, date_saved is: $logDate, personal log is: $crewLog")
        }
        println(" ")
        CrewRecords.getUserChoice(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
      }
    }
    catch {
      case e: Exception => e.printStackTrace
    }

    closeMySQL.closeConnection()
  }

  def diyModifyQuery(connection: Connection): Unit = {
    val diyRequest = readLine("Please enter your query to create, update, or delete: ").toLowerCase()

    try {
      val diyStatement = connection.createStatement
      val diyDBrs = diyStatement.executeUpdate(diyRequest)

      //this is to modify database - update, delete, create.

      breakable {

        println(" ")

      }
    }
    catch {
      case e: Exception => e.printStackTrace
    }
    closeMySQL.closeConnection()
  }

}
