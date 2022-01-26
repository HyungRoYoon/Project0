import scala.io.StdIn._


// Consider this as terminal interface in video game.
// This object needs to do just terminal function related

object CrewRecords {
  val connectMySQL = new ConnectMySQL
  val authorizationDatabase = new AuthorizationDatabase
  val logDatabase = new LogDatabase
  val crewInfoDatabase = new CrewInfoDatabase


  def main(args: Array[String]): Unit = {
    //You came across abandoned spaceship, trying to see what happened in the past.
    //You have activated main system and are now trying to access records of crews.

    Init()
    //formatTable(Seq(Seq("head1", "head2", "head3"), Seq("one", "two", "three"), Seq("four", "five", "six")))
    askUserChoiceInput()
  }

  def Init(): Unit = {
    //TODO: create temp table for every database
    connectMySQL.showHackedData(authorizationDatabase)

    println(" ")
    val inputName = readLine("Please enter your name: ")
    val inputPassword = readLine("Please enter your password: ")
    println(" ")

    checkUserInfo(inputName.trim, inputPassword.trim)
  }

  def checkUserInfo(name: String, password: String): Unit = {
    //query name and password
    authorizationDatabase.attemptedName = name.capitalize
    authorizationDatabase.attemptedPassword = password
    connectMySQL.establishConnection(authorizationDatabase)
    //connectMySQL.establishConnection(classOf[AuthorizationDatabase])
  }

  def askUserChoiceInput() : Unit = {
    if (authorizationDatabase.canSearchDB) {
      println(" ")
      try
      {
        getUserChoiceCR(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
      }
      catch {
        case e: Exception => println("Please choose from existing options: ")
          askUserChoiceInput()
      }
      println(" ")
    }
  }

  def getUserChoiceCR(x: Int) : Unit = x match {
    case 0 => quitProgram()
    case 1 => searchPerson()
  }

  def getUserChoiceLogDB(): Unit = {
    try {
      userChoicesLogDB(readLine("Please enter 1 to customize crew search, 2 to modify crew's log database, 9 to go back, or 0 to exit the program: ").toInt)
      println(" ")
    }
    catch {
      case e: Exception => println("Please enter correct number choice.")
        askUserChoiceInput()
    }
  }

  def userChoicesLogDB(x: Int): Unit = x match {
    case 0 => quitProgram()
    case 1 => connectMySQL.customizedSearchConnection(logDatabase)
    case 2 => connectMySQL.modifyCrewLogConnection(logDatabase)
    case 9 => goBack()
    case _ => reEnterNumber(x)
  }

  def reEnterNumber(x: Int) : Unit = {
    if (x != 0 && x != 9) println("Please select one of given numbers.")
    getUserChoiceLogDB()
  }

  def goBack(): Unit = {
    getUserChoiceCR(readLine("Please enter 1 to search crew or 0 to exit the program: ").toInt)
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
    val crewNameModify = readLine("Please enter crew name (Case Sensitive) OR CUSTOMIZE to manually query crew information: ")
    //query DB and search and return matching
    println(" ")
    if (crewNameModify.toLowerCase() != "CUSTOMIZE".toLowerCase()) {
      logDatabase.attemptedName = crewNameModify
      connectMySQL.establishConnection(logDatabase)
    }
    else if (crewNameModify.toLowerCase() == "CUSTOMIZE".toLowerCase())
    {
      editCrewData()
    }
  }

  def editCrewData(): Unit = {
    val modifyCrewData = readLine("Please enter READ if you want to customize crew search, or MODIFY if you want to create, update, or delete crew data: ")
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
