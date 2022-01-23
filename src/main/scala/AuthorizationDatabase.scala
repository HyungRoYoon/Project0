import java.sql.Connection
import util.control.Breaks.{break, _}

class AuthorizationDatabase {
  var attemptedName: String = _
  var attemptedPassword: String = _
  val logDatabase = new LogDatabase


  def authorize(connection: Connection): Unit =
  {
    if (attemptedName != null && attemptedPassword != null)
    {
      try {

        val authorizeStatement = connection.createStatement
        //val rs = statement.executeQuery("SELECT host, user FROM user")
        val authorizationRS = authorizeStatement.executeQuery("SELECT name, password, `rank` FROM authentication")

        breakable {
          while (authorizationRS.next) {
            //need a way to cross check name and rank
            val authorizedName = authorizationRS.getString("name")
            val authorizedPassword = authorizationRS.getString("password")
            val authorizedRank = authorizationRS.getString("rank")
            if (attemptedName == authorizedName)
            {
              if (authorizedRank != "Cadet")
              {
                if (attemptedPassword == authorizedPassword)
                {
                  println("Authorization confirmed. Welcome aboard, " + authorizedRank + " "
                    + authorizedName)
                  break
                }
              }
              else if (authorizedRank == "Cadet")
              {
                println("Access Denied - Reason: " + attemptedName + "'s Rank: " + authorizedRank + " does not have permission to access this record." )
                break
              }
            }
          }
        }
        //println("host = %s, user = %s".format(host,user))
      }
      catch {
        case e: Exception => e.printStackTrace
      }
      val closeMySQL = new ConnectMySQL
      closeMySQL.closeConnection()
    }
    else
      {
        println("attempted Name is " + attemptedName + " and password is " + attemptedPassword)
      }
  }

}
