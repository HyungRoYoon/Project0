import java.sql.Connection
import util.control.Breaks.{break, _}

//this class needs to be shown in table before searching data
class CrewInfoDatabase {
  var attemptedName: String = _

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

          println(s"crew name: $searchedName, personalID: $personalID, " +
            s"birthdate: $birthdate, race: $race, birthplace: $birthplace")
        }
      }
    }
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

            val crewLog = searchDBrs.getString("text")
            if (attemptedName == searchedName) {

            }
          }
        }
      }
    }
  }
}
