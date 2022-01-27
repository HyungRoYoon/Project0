import java.sql.{Connection, DatabaseMetaData}
import util.control.Breaks.{break, _}
import scala.io.StdIn._


//Crew Information DB query related
class CrewInfoDatabase {

  var attemptedName: String = _
  val connectMySQL = new ConnectMySQL
  var connectionHolder : Connection = _
  var searchedName, personalID, birthdate, race, birthplace = " "

  def createNameTable(): Unit = {
    //create name table based on queried data
  }

  def showNameTable(connection: Connection): Unit = {
    connectionHolder = connection
    //show created name table
    try {
      val showDBStatement = connection.createStatement
      val showDBrs = showDBStatement.executeQuery("SELECT * FROM crewinformation")

      breakable {
        while (showDBrs.next) {
          searchedName = showDBrs.getString("crew_name")
          personalID = showDBrs.getString("personal_id")
          birthdate = showDBrs.getString("birthdate")
          race = showDBrs.getString("race")
          birthplace = showDBrs.getString("planet_of_origin")

          println(s"crew_name: $searchedName, personal_id: $personalID, " +
            s"birthdate: $birthdate, race: $race, planet_of_origin: $birthplace")
        }
        MiscManager.showAllDatabases(connection)
      }
    }
    catch {
      case e: Exception => println("CrewInfoDatabase showNameTable caught: " + e.toString)
    }
    connectMySQL.closeConnection()
  }

  def searchNameDatabase(connection: Connection): Unit = {
    //search crew name from database
    if (attemptedName != null) {
      try {
        val searchDBStatement = connection.createStatement
        val searchDBrs = searchDBStatement.executeQuery("SELECT * FROM crewinformation")

        breakable {
          while (searchDBrs.next) {
            searchedName = searchDBrs.getString("crew_name")
            personalID = searchDBrs.getString("personal_id")
            birthdate = searchDBrs.getString("birthdate")
            race = searchDBrs.getString("race")
            birthplace = searchDBrs.getString("planet_of_origin")

            if (attemptedName == searchedName) {
              println(s"crew_name: $searchedName, personal_id: $personalID, " +
                s"birthdate: $birthdate, race: $race, planet_of_origin: $birthplace")
              println(" ")
              CrewRecords.getUserChoiceCR(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
            }
          }
        }
      }
      catch {
        case e: Exception => println("CrewInfoDatabase searchNameDatabase caught: " + e.toString)
      }
      connectMySQL.closeConnection()
    }
  }

  //be able to Create, Read, Update, Delete
    def diyReadQuery(connection: Connection): Unit = {
      val diyReadRequest = readLine("Please enter your query in the order of SELECT, FROM, WHERE, GROUP BY, HAVING, ORDER BY; or type BACK to go back to modification selection screen: ")
      println(" ")
      MiscManager.toEditCrewDataMenu(diyReadRequest)

      //I want to get all strings between select and from based on user input
      //val diyRegex = "^select(.*)from$"
      //println("1111 "+(diyRegex.findAllIn(diyReadRequest)).mkString(","))

      try {
        val diyStatement = connection.createStatement
        val diyDBrs = diyStatement.executeQuery(diyReadRequest)

        breakable {
          while (diyDBrs.next) {
            if (MiscManager.hasColumn(diyDBrs,"crew_name"))
            {
              searchedName = diyDBrs.getString("crew_name")
            }
            if (MiscManager.hasColumn(diyDBrs, "personal_id"))
            {
              personalID = diyDBrs.getString("personal_id")
            }
            if (MiscManager.hasColumn(diyDBrs, "birthdate"))
            {
              birthdate = diyDBrs.getString("birthdate")
            }
            if (MiscManager.hasColumn(diyDBrs, "race"))
            {
              race = diyDBrs.getString("race")
            }
            if (MiscManager.hasColumn(diyDBrs, "planet_of_origin"))
            {
              birthplace = diyDBrs.getString("planet_of_origin")
            }

            println(s"crew_name: $searchedName, personal_id: $personalID, " +
              s"birthdate: $birthdate, race: $race, planet_of_origin: $birthplace")
          }
          println(" ")
          CrewRecords.getUserChoiceCR(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
        }
      }
      catch {
        case e: Exception => println("You have entered: " + diyReadRequest + ". Please check your query and try again: " + e.toString)
        diyReadQuery(connectionHolder)
      }
      connectMySQL.closeConnection()
    }

  def diyModifyQuery(connection: Connection): Unit = {
    val diyModifyRequest = readLine("Please enter your query to create, update, or delete; or type BACK to go back to modification selection screen: ")
    println(" ")
    MiscManager.toEditCrewDataMenu(diyModifyRequest)

    try {
      val diyStatement = connection.createStatement
      diyStatement.executeUpdate(diyModifyRequest)

      //this is to modify database - insert, update, delete.
      breakable {
        println("Successfully modified crew information. StandBy for update...")
        Thread.sleep(1000)
        println(" ")
        showNameTable(connection)
        println(" ")
        CrewRecords.getUserChoiceCR(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
      }
    }
    catch {
      case e: Exception => println("You have entered: " + diyModifyRequest + ". Please check your query and try again: " + e.toString)
        println(" ")
        diyModifyQuery(connectionHolder)
    }
    connectMySQL.closeConnection()
  }
}
