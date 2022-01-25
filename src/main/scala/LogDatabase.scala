import java.sql.Connection
import scala.io.StdIn.readLine
import util.control.Breaks.{break, _}

//Log DB query related
class LogDatabase {
  var attemptedName: String = _
  val closeMySQL = new ConnectMySQL
  var connectionHolder : Connection = _
  var crewName, logDate, crewLog = " "


  def main(args: Array[String]): Unit = {

  }

  def showLogDatabase(connection: Connection): Unit = {

  }

  def searchNameLogDatabase(connection: Connection): Unit = {
    //save connection to local variable for future use
    connectionHolder = connection
    //search crew name from database
    if (attemptedName != null) {
      try {
        val searchDBStatement = connection.createStatement
        val searchDBrs = searchDBStatement.executeQuery("SELECT crew_name, text FROM crew_logs")

        breakable {
          while (searchDBrs.next) {
            crewName = searchDBrs.getString("crew_name")
            crewLog = searchDBrs.getString("text")
            if (attemptedName == crewName) {
              println(s"$crewLog")
              println(" ")
              CrewRecords.getUserChoiceLogDB()
            }
          }
        }
      }
      catch {
        case e: Exception => println("LogDatabase caught: " + e.toString)
      }
      closeMySQL.closeConnection()
    }
  }

  def diyReadQuery(connection: Connection): Unit = {
    val diyReadRequest = readLine("Please enter your query in the order of SELECT, FROM, WHERE, GROUP BY, HAVING, ORDER BY: ")
    try {
      val diyStatement = connection.createStatement
      val diyDBrs = diyStatement.executeQuery(diyReadRequest)

      breakable {
        while (diyDBrs.next) {
          crewName = diyDBrs.getString("crew_name")
          logDate = diyDBrs.getString("date_saved")
          crewLog = diyDBrs.getString("text")

          println(s"crew_name is: $crewName, date_saved is: $logDate, personal log is: $crewLog")
        }
        println(" ")
        CrewRecords.getUserChoiceCR(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
      }
    }
    catch {
      case e: Exception => println("You have entered: " + diyReadRequest + ". Please check your syntax and try again: ")
        diyReadQuery(connectionHolder)
    }

    closeMySQL.closeConnection()
  }

  def diyModifyQuery(connection: Connection): Unit = {
    val diyModifyRequest = readLine("Please enter your query to create, update, or delete: ").toLowerCase()

    try {
      val diyStatement = connection.createStatement
      val diyDBrs = diyStatement.executeUpdate(diyModifyRequest)

      //this is to modify database - update, delete, create.

      breakable {
        println("Successfully modified crew information. StandBy for update...")
        Thread.sleep(1000)
        searchNameLogDatabase(connection)
        println(" ")
        CrewRecords.getUserChoiceCR(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
      }
    }
    catch {
      case e: Exception => println("You have entered: " + diyModifyRequest + ". Please check your query and try again: ")
        diyReadQuery(connectionHolder)
    }
    closeMySQL.closeConnection()
  }
}
