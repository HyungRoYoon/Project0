import java.sql.{Connection, DatabaseMetaData}
import util.control.Breaks.{break, _}
import scala.io.StdIn._


//this class needs to be shown in table before searching data
class CrewInfoDatabase {
  var attemptedName: String = _
  val closeMySQL = new ConnectMySQL

  def main(args: Array[String]): Unit = {

  }

  def createNameTable(): Unit = {
    //create name table based on queried data
  }

  def showNameTable(connection: Connection): Unit = {
    //show created name table
    try {
      val showDBStatement = connection.createStatement
      val showDBrs = showDBStatement.executeQuery("SELECT * FROM crewinformation")

      breakable {
        while (showDBrs.next) {
          val searchedName = showDBrs.getString("crew_name")
          val personalID = showDBrs.getString("personal_id")
          val birthdate = showDBrs.getString("birthdate")
          val race = showDBrs.getString("race")
          val birthplace = showDBrs.getString("planet_of_origin")

          println(s"crew_name: $searchedName, personal_id: $personalID, " +
            s"birthdate: $birthdate, race: $race, planet_of_origin: $birthplace")


        }
      }
    }
    catch {
      case e: Exception => e.printStackTrace
    }
    val closeMySQL = new ConnectMySQL
    closeMySQL.closeConnection()
  }

  def searchNameDatabase(connection: Connection): Unit = {
    //search crew name from database
    if (attemptedName != null) {
      try {
        val searchDBStatement = connection.createStatement
        val searchDBrs = searchDBStatement.executeQuery("SELECT * FROM crewinformation")

        breakable {
          while (searchDBrs.next) {
            val searchedName = searchDBrs.getString("crew_name")
            val personalID = searchDBrs.getString("personal_id")
            val birthdate = searchDBrs.getString("birthdate")
            val race = searchDBrs.getString("race")
            val birthplace = searchDBrs.getString("planet_of_origin")

            if (attemptedName == searchedName) {
              println(s"crew_name: $searchedName, personal_id: $personalID, " +
                s"birthdate: $birthdate, race: $race, planet_of_origin: $birthplace")
              println(" ")
              CrewRecords.getUserChoice(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
            }
          }
        }
      }
      catch {
        case e: Exception => e.printStackTrace
      }

      closeMySQL.closeConnection()
    }
  }

  //be able to Create, Read, Update, Delete
    def diyReadQuery(connection: Connection): Unit = {
      val diyRequest = readLine("Please enter your query in the order of SELECT, FROM, WHERE, GROUP BY, HAVING, ORDER BY: ").toLowerCase()
      //I want to get all strings between select and from based on user input
      val diyRegex = "^select(.*)from$".r
      println("1111 "+(diyRegex.findAllIn(diyRequest)).mkString(","))
      println("update crewinformation set crew_name = \"Damian Santos II\",race = \"Human\" where planet_of_origin =  \"Sarpsborg\"")

      try {
        val diyStatement = connection.createStatement
        val diyDBrs = diyStatement.executeQuery(diyRequest)

        //this is to modify database - update, delete, create.
        //val diyDBrs = diyStatement.executeUpdate(diyRequest)


//        breakable {
//          while (diyDBrs.next) {
//
//            var searchedName, personalID, birthdate, race, birthplace = " "
//
//            if (closeMySQL.hasColumn(diyDBrs,"crew_name"))
//            {
//              searchedName = diyDBrs.getString("crew_name")
//            }
//            if (closeMySQL.hasColumn(diyDBrs, "personal_id"))
//            {
//              personalID = diyDBrs.getString("personal_id")
//            }
//            if (closeMySQL.hasColumn(diyDBrs, "birthdate"))
//            {
//              birthdate = diyDBrs.getString("birthdate")
//            }
//            if (closeMySQL.hasColumn(diyDBrs, "race"))
//            {
//              race = diyDBrs.getString("race")
//            }
//            if (closeMySQL.hasColumn(diyDBrs, "planet_of_origin"))
//            {
//              birthplace = diyDBrs.getString("planet_of_origin")
//            }
//
//            println(s"crew_name: $searchedName, personal_id: $personalID, " +
//              s"birthdate: $birthdate, race: $race, planet_of_origin: $birthplace")
//          }
//          println(" ")
//          CrewRecords.getUserChoice(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
//        }
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
        CrewRecords.getUserChoice(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
      }
    }
    catch {
      case e: Exception => e.printStackTrace
    }
    closeMySQL.closeConnection()
  }
}
