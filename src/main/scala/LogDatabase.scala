import java.sql.Connection
import util.control.Breaks.{break, _}

class LogDatabase {
  var attemptedName: String = _

  def main(args: Array[String]): Unit = {

  }

  def searchNameDatabase(connection: Connection): Unit = {
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
            }
          }
        }
      }
    }
  }
}
