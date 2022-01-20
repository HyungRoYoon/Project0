import scala.io.StdIn._
import java.io.{File, PrintWriter}

// Consider this as terminal interface in video game.
// This object needs to do just terminal related

object CrewRecords {
  def main(array: Array[String]): Unit = {
    //You came across abandoned spaceship, trying to see what happened in the past.
    //You have activated main system and are now trying to access records of crews.
    Init()
  }
  def Init(): Unit = {
    //query username and password
    println("Creating username and password...")

  }

  def warningMessage():Unit = {
    val userName = readLine("Please enter your name: ")
    val password = readLine("Please enter your password: ")

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
}
