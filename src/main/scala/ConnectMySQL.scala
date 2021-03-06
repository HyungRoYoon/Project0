import java.sql.DriverManager
import java.sql.Connection

//Anything MySQL Connection related goes here
class ConnectMySQL {

  val url = "jdbc:mysql://localhost:3306/project0"
  val driver = "com.mysql.cj.jdbc.Driver"
  val username = "root"
  val password = "Root1234"
  var connection: Connection = _

  Class.forName(driver)
  connection = DriverManager.getConnection(url, username, password)

  def showHackedData(authorizationDatabase: AuthorizationDatabase) {
    authorizationDatabase.showNames(connection)
  }

  def establishConnection() {

  }

  //for reusability optimization
  //  def establishConnection(c: Class[_]): Any = c match {
  //
  //    //authorizationDatabase.authorize(connection)
  //    case a if a == classOf[AuthorizationDatabase] =>
  //    case _ => print("try again")
  //  }

  def establishConnection(authorizationDatabase: AuthorizationDatabase) {
    authorizationDatabase.authorize(connection)
  }

  def establishConnection(logDatabase: LogDatabase) {
    logDatabase.searchNameLogDatabase(connection)
  }

  def establishConnection(crewInfoDatabase: CrewInfoDatabase) {
    crewInfoDatabase.showNameTable(connection)
  }


  def customizedSearchConnection(logDatabase: LogDatabase) {
    logDatabase.diyReadQuery(connection)
  }

  def modifyCrewLogConnection(logDatabase: LogDatabase) {
    logDatabase.diyModifyQuery(connection)
  }

  def customizedSearchConnection(crewInfoDatabase: CrewInfoDatabase) {
    crewInfoDatabase.diyReadQuery(connection)
  }

  def modifyCrewDataConnection(crewInfoDatabase: CrewInfoDatabase) {
    crewInfoDatabase.diyModifyQuery(connection)
  }

  def closeConnection(): Unit = {
    if (connection != null) {
      connection.close
    }
  }
}