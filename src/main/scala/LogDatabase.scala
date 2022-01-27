import java.sql.{Connection, ResultSet, Statement}
import scala.io.StdIn.readLine
import util.control.Breaks.{break, _}

//Log DB query related
class LogDatabase {

  var attemptedName: String = _
  val connectMySQL = new ConnectMySQL
  var connectionHolder : Connection = _
  var crewName, logDate, crewLog = " "
  var comparisonResult = false
  var logCounter = 0;
  var backString = "back"

  def searchNameLogDatabase(connection: Connection): Unit = {
    logCounter = 0
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
            if (attemptedName.capitalize == crewName) {
              println(s"$crewLog")
              println(" ")
              CrewRecords.getUserChoiceLogDB()
            }
            else
              {
                logCounter += 1
              }
          }
          countRows(connection)
        }
      }
      catch {
        case e: Exception => println("LogDatabase caught: " + e.toString)
        println(" ")
      }
      connectMySQL.closeConnection()
    }
  }

  def countRows(connection: Connection) : Unit = {
      val countDBrowsST = connection.createStatement()
      val countDBrs = countDBrowsST.executeQuery("select count(*) from crew_logs")
    while (countDBrs.next()) {
      val rowCount = countDBrs.getLong(1)
      if (logCounter == rowCount)
      {
        println("You have entered: " + attemptedName + " and it doesn't exist in our database or the person doesn't have any personal logs saved. Please try again")
      }
    }
    println(logCounter)
    CrewRecords.askUserChoiceInput()
    connectMySQL.closeConnection()
  }

  def diyReadQuery(connection: Connection): Unit = {
    val diyReadRequest = readLine("Please enter your query in the order of SELECT, FROM, WHERE, GROUP BY, HAVING, ORDER BY; or type BACK to go back to modification selection screen: ")
    println(" ")
    if (diyReadRequest.toLowerCase() == backString.toLowerCase()) {
      MiscManager.toEditCrewLogMenu(diyReadRequest)
    }

    try {
      val diyStatement = connection.createStatement
      val diyDBrs = diyStatement.executeQuery(diyReadRequest)

      breakable {
        while (diyDBrs.next) {
          if (MiscManager.hasColumn(diyDBrs, "crew_name")) {
            crewName = diyDBrs.getString("crew_name")
          }
          if (MiscManager.hasColumn(diyDBrs, "date_saved")) {
            logDate = diyDBrs.getString("date_saved")
          }
          if (MiscManager.hasColumn(diyDBrs, "text")) {
            crewLog = diyDBrs.getString("text")
          }

          println(s"crew_name is: $crewName, date_saved is: $logDate, personal log is:\n $crewLog")
        }
        println(" ")
        CrewRecords.getUserChoiceCR(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
      }
    }
    catch {
      case e: Exception => println("You have entered: " + diyReadRequest + ". Please check your syntax and try again: " + e.toString)
        diyReadQuery(connectionHolder)
    }
    connectMySQL.closeConnection()
  }

  def diyModifyQuery(connection: Connection): Unit = {
    val diyModifyRequest = readLine("Please enter your query to create, update, or delete; or type BACK to go back to modification selection screen: ")
    println(" ")
    MiscManager.toEditCrewLogMenu(diyModifyRequest)

    try {
      val diyStatement = connection.createStatement
      diyStatement.executeUpdate(diyModifyRequest)

      //this is to modify database - update, delete, create.
      breakable {
        println("Successfully modified crew logs. StandBy for update...")
        MiscManager.waitForSecond(500)
        searchNameLogDatabase(connection)
        println(" ")
        CrewRecords.getUserChoiceCR(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
      }
    }
    catch {
      case e: Exception => println("You have entered: " + diyModifyRequest + ". Please check your query and try again: " + e.toString)
        diyModifyQuery(connectionHolder)
    }
    connectMySQL.closeConnection()
  }
}
