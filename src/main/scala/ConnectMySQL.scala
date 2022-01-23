import java.sql.DriverManager
import java.sql.Connection


class ConnectMySQL {

  val url = "jdbc:mysql://localhost:3306/project0"
  val driver = "com.mysql.cj.jdbc.Driver"
  val username = "root"
  val password = "Root1234"
  var connection: Connection = _


  Class.forName(driver)
  connection = DriverManager.getConnection(url, username, password)

  def establishConnection(){

  }

  def establishConnection(authorizationDatabase: AuthorizationDatabase){
    authorizationDatabase.authorize(connection)
  }

  def closeConnection(): Unit =
  {
    if (connection != null)
    {
      connection.close
    }
  }
}
