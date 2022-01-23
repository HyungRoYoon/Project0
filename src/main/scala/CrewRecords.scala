import scala.io.StdIn._
import java.io.{File, PrintWriter}

// Consider this as terminal interface in video game.
// This object needs to do just terminal related

object CrewRecords {
  def main(args: Array[String]): Unit = {
    //You came across abandoned spaceship, trying to see what happened in the past.
    //You have activated main system and are now trying to access records of crews.

    Init()
    //formatTable(Seq(Seq("head1", "head2", "head3"), Seq("one", "two", "three"), Seq("four", "five", "six")))
  }

  def Init(): Unit = {
    //query name and password
    val inputName = readLine("Please enter your name: ")
    val inputPassword = readLine("Please enter your password: ")

    checkUserInfo(inputName, inputPassword)
  }

  def checkUserInfo(name: String, password: String): Unit = {
    val connectMySQL = new ConnectMySQL
    val authorizationDatabase = new AuthorizationDatabase
    authorizationDatabase.attemptedName = name
    authorizationDatabase.attemptedPassword = password
    connectMySQL.establishConnection(authorizationDatabase)
  }

  def getUserChoice(x: Int) : Unit = x match {
    case 0 => quitProgram()
    case 1 => searchPerson()
  }

  def quitProgram(): Unit = {
    println("Exiting interface...")
    System.exit(0)
  }

  def searchPerson(): Unit = {
    println(" ")
    val crewName = readLine("Please enter crew name: ")
    //query DB and search and return matching
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
