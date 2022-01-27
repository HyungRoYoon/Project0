import java.sql.{Connection, ResultSet, SQLException}
import scala.util.control.Breaks.breakable

//singleton that deals with miscellaneous jobs
object MiscManager {

  val backString = "Back"

  @throws[SQLException]
  def hasColumn(rs: ResultSet, columnName: String): Boolean = {
    val rsmd = rs.getMetaData
    val columns = rsmd.getColumnCount
    for (x <- 1 to columns) {
      if (columnName == rsmd.getColumnName(x)) return true
    }
    false
  }

  def waitForSecond(ms: Int): Unit = {
    Thread.sleep(ms)
  }

  def showAllDatabases(connection: Connection): Unit = {
    println(" ")
    val showAllDB = connection.createStatement
    println("Available databases: ")
    val showAllDBrs = showAllDB.executeQuery("show tables")
    println(" ")

    breakable {
      while (showAllDBrs.next) {
        println(showAllDBrs.getString(1))
      }
    }
  }

  def toEditCrewDataMenu(userInput: String): Unit = {
    if (userInput == backString.toLowerCase())
    {
      CrewRecords.editCrewData()
    }
  }

  def toEditCrewLogMenu(userInput: String): Unit = {
    if (userInput == backString.toLowerCase())
    {
      CrewRecords.editCrewLogs()
    }
  }
}
