package util

import java.io.File

/**
  * The object is controller for checking status of the application.
  */
object RunAppController {

  private val PID = new File("./st.pid")

  /**
    * The method sets status for application - "running right now"
    */
  def run(): Unit = PID.createNewFile()

  /**
    * The method sets status for application - "stopped"
    */
  def stop(): Unit = PID.delete()

  /**
    * The method is for checking of application status
    *
    * @return
    */
  def isRunnable: Boolean = PID.exists()

}
