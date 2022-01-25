import java.sql.Connection
import util.control.Breaks.{break, _}
import java.util._
import scala.io.StdIn.readLine

//Authorization DB query related
class AuthorizationDatabase {
  var attemptedName: String = _
  var attemptedPassword: String = _
  var canSearchDB = false;
  var attemptedCounter = 3;
  val closeMySQL = new ConnectMySQL
  var rsArray = new ArrayList[String]()

  def showNames(connection: Connection) : Unit =
  {
    //show created name table
    try {
      val showDBStatement = connection.createStatement
      val showDBrs = showDBStatement.executeQuery("SELECT name, password FROM authentication")

      breakable {
        while (showDBrs.next) {
          val showHackedName = showDBrs.getString("name")
          val showHackPassword = showDBrs.getString("password")

          println(s"name: $showHackedName, password: $showHackPassword")
        }
      }
    }
    catch {
      case e: Exception => e.printStackTrace
    }
    closeMySQL.closeConnection()
  }

  def authorize(connection: Connection): Unit =
  {
    if (attemptedName != null && attemptedPassword != null)
    {
      try {
        val authorizeStatement = connection.createStatement
        val authorizationRS = authorizeStatement.executeQuery("SELECT name, password, `rank` FROM authentication")

        breakable {
          while (authorizationRS.next)
          {
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
                  canSearchDB = true;
                  break
                }
                else
                {
                  if (attemptedCounter > 1) {
                    attemptedCounter -= 1
                      println("You have entered wrong password. Please try again. Remaining attempt: " + attemptedCounter.toString)
                    CrewRecords.Init()

                  }
                  else if (attemptedCounter <= 0)
                  {
                    val adminPassword = readLine("Terminal lockdown in progress due to too many failed attempt. Please enter admin password to continue: ")
                  }
                }
              }
              else if (authorizedRank == "Cadet")
              {
                println("Access Denied - Reason: " + attemptedName + "'s Rank: " + authorizedRank + " does not have permission to access this record." )
                break
              }
            }
            else
              {
//                println(attemptedCounter)
//                println("You have entered: " + attemptedName + ", and it doesn't exist in our database. Please enter user from existing table.")
//                println(" ")
//                CrewRecords.Init()
              }
          }
        }
      }
      catch {
        case e: Exception => println("AuthorizationDB caught: "+e.toString)
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
