import java.sql.Connection
import util.control.Breaks.{break, _}
import scala.io.StdIn.readLine

//Authorization DB query related
class AuthorizationDatabase {

  var attemptedName: String = _
  var attemptedPassword: String = _
  var canSearchDB = false;
  var attemptedCounter = 3;
  val connectMySQL = new ConnectMySQL
  var connectionHolder : Connection = _
  val overrideSecurityPassword = 123456
  var overrideSecurityInput = 0
  var authorizedName, authorizedPassword, authorizedRank = " "

  def showNames(connection: Connection) : Unit =
  {
    connectionHolder = connection
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
      case e: Exception => println("AuthorizationDB - showNames caught: "+e.toString)
    }
    connectMySQL.closeConnection()
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
            authorizedName = authorizationRS.getString("name")
            authorizedPassword = authorizationRS.getString("password")
            authorizedRank = authorizationRS.getString("rank")

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
                  if (attemptedCounter > 0) {
                    attemptedCounter -= 1
                    if (attemptedCounter > 0) {
                      println("You have entered wrong password. Please try again. Remaining attempt: " + attemptedCounter.toString)
                      println(" ")
                      CrewRecords.Init()
                    }
                    else
                    {
                      try {
                        overrideSecurityInput = readLine("Terminal lockdown in progress due to too many failed attempt. Please enter admin password to continue: ").toInt
                        if (overrideSecurityInput == overrideSecurityPassword)
                        {
                          println(" ")
                          println("System Override authorized.")
                          println(" ")
                          attemptedCounter = 3
                          CrewRecords.Init()
                        }
                        else
                        {
                          println(" ")
                          println("Unauthorized access: shutting down terminal.")
                          MiscManager.waitForSecond(1000)
                          CrewRecords.quitProgram()
                        }
                      }
                      catch {
                        case e: Exception => println("Please enter numbers: ")
                      }
                    }
                    println(" ")
                  }
                }
              }
              else if (authorizedRank == "Cadet")
              {
                MiscManager.waitForSecond(500)
                println("Access Denied - Reason: " + attemptedName + "'s Rank: " + authorizedRank + " does not have permission to access this record." )
                break
              }
            }
          }
          if (attemptedName != authorizedName)
          {
            println("You have entered: " + attemptedName + ", and it doesn't exist in our database. Please enter user from existing table.")
            println(" ")
            MiscManager.waitForSecond(1000)
            CrewRecords.Init()
          }
        }
      }
      catch {
        case e: Exception => println("AuthorizationDB - authorize caught: " + e.toString)
      }
      connectMySQL.closeConnection()
    }
    else
      {
        println("attempted Name is " + attemptedName + " and password is " + attemptedPassword)
      }
  }
}
