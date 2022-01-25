import java.sql.DriverManager
import java.sql.Connection
import scala.reflect._
import java.sql.ResultSet
import java.sql.SQLException


class ConnectMySQL {

  val url = "jdbc:mysql://localhost:3306/project0"
  val driver = "com.mysql.cj.jdbc.Driver"
  val username = "root"
  val password = "Root1234"
  var connection: Connection = _


  Class.forName(driver)
  connection = DriverManager.getConnection(url, username, password)

  def establishConnection() {

  }

  //for reusability optimization
//  def establishConnection(c: Class[_]): Any = c match {
//
//    //authorizationDatabase.authorize(connection)
//    case a if a == classOf[AuthorizationDatabase] =>
//    case _ => print("try again")
//
//
//  }
  def showHackedData(authorizationDatabase: AuthorizationDatabase)
  {
    authorizationDatabase.showNames(connection)
  }

  def establishConnection(authorizationDatabase: AuthorizationDatabase)
  {
    authorizationDatabase.authorize(connection)
  }

  def establishConnection(logDatabase: LogDatabase)
  {
    logDatabase.searchNameDatabase(connection)
  }

  def establishConnection(crewInfoDatabase: CrewInfoDatabase)
  {
    crewInfoDatabase.showNameTable(connection)
  }

  //currently not being used
  def customizedConnection(logDatabase: LogDatabase) =
  {
    logDatabase.diyReadQuery(connection)
  }

  def customizedSearchConnection(crewInfoDatabase: CrewInfoDatabase)
  {
    crewInfoDatabase.diyReadQuery(connection)
  }

  def modifyCrewDataConnection(crewInfoDatabase: CrewInfoDatabase)
  {
    crewInfoDatabase.diyModifyQuery(connection)
  }



  def closeConnection(): Unit =
  {
    if (connection != null)
    {
      connection.close
    }
  }


  @throws[SQLException]
  def hasColumn(rs: ResultSet, columnName: String): Boolean = {
    val rsmd = rs.getMetaData
    val columns = rsmd.getColumnCount
    for (x <- 1 to columns) {
      if (columnName == rsmd.getColumnName(x)) return true
    }
    false
  }
}
