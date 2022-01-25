import scala.io.StdIn._
import java.io.{File, PrintWriter}

// Consider this as terminal interface in video game.
// This object needs to do just terminal related

object CrewRecords {
  val connectMySQL = new ConnectMySQL
  val authorizationDatabase = new AuthorizationDatabase
  val logDatabase = new LogDatabase
  val crewInfoDatabase = new CrewInfoDatabase

  def main(args: Array[String]): Unit = {
    //You came across abandoned spaceship, trying to see what happened in the past.
    //You have activated main system and are now trying to access records of crews.

    //show authorization name and password here for user to try to enter

    Init()
    //formatTable(Seq(Seq("head1", "head2", "head3"), Seq("one", "two", "three"), Seq("four", "five", "six")))
    if (authorizationDatabase.canSearchDB)
      {
        println(" ")
        getUserChoice(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
        println(" ")
      }
  }

  def Init(): Unit = {
    //create temp table for every database
    connectMySQL.showHackedData(authorizationDatabase)

    //query name and password
    println(" ")
    val inputName = readLine("Please enter your name: ")
    val inputPassword = readLine("Please enter your password: ")
    println(" ")

    checkUserInfo(inputName, inputPassword)
  }

  def checkUserInfo(name: String, password: String): Unit = {

    authorizationDatabase.attemptedName = name
    authorizationDatabase.attemptedPassword = password
    connectMySQL.establishConnection(authorizationDatabase)
    //connectMySQL.establishConnection(classOf[AuthorizationDatabase])
  }

  def getUserChoice(x: Int) : Unit = x match {
    case 0 => quitProgram()
    case 1 => searchPerson()
  }

  def quitProgram(): Unit = {
    println(" ")
    println("Exiting interface...")
    System.exit(0)
  }

  def searchPerson(): Unit = {
    //show all crew information before initiating search functionality
    connectMySQL.establishConnection(crewInfoDatabase)

    println(" ")
    val crewName = readLine("Please enter crew name OR CUSTOMIZE to manually query crew information: ")
    //query DB and search and return matching
    println(" ")
    if (crewName.toLowerCase() != "CUSTOMIZE".toLowerCase()) {
      logDatabase.attemptedName = crewName
      connectMySQL.establishConnection(logDatabase)
    }
    else if (crewName.toLowerCase() == "CUSTOMIZE".toLowerCase())
    {
      editCrewData()
    }
  }

  def editCrewData(): Unit = {
    println(" ")
    val modifyCrewData = readLine("Please enter READ if you want to customize crew search, or MODIFY if you want to create, update, or delete crew data")
    println(" ")
    if (modifyCrewData.toLowerCase() == "READ".toLowerCase()) {
      connectMySQL.customizedSearchConnection(crewInfoDatabase)
    }
    else if (modifyCrewData.toLowerCase() == "MODIFY".toLowerCase())
    {
      connectMySQL.modifyCrewDataConnection(crewInfoDatabase)
    }
    else
    {
      println("Incorrect response - Please choose again: ")
      editCrewData()
    }
  }

  def formatTable(table: Seq[Seq[Any]]): String = {
    if (table.isEmpty) ""
    else {
      // Get column widths based on the maximum cell width in each column (+2 for a one character padding on each side)
      val colWidths = table.transpose.map(_.map(cell => if (cell == null) 0 else cell.toString.length).max + 2)
      // Format each row
      val rows = table.map(_.zip(colWidths).map { case (item, size) => (" %-" + (size - 1) + "s").format(item) }
        .mkString("|", "|", "|"))
      // Formatted separator row, used to separate the header and draw table borders
      val separator = colWidths.map("-" * _).mkString("+", "+", "+")
      // Put the table together and return
      (separator +: rows.head +: separator +: rows.tail :+ separator).mkString("\n")
    }
  }



}
