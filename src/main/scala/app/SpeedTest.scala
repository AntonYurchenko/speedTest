package app

import java.sql.Timestamp
import java.time.LocalDateTime
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import db.dao.{DBContext, PingReports}
import db.table.entity.PingReport
import util.RunAppController
import ping.SimplePinger
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile
import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Success, Try}

/**
  * The object is running speed test application
  */
object SpeedTest extends App with LazyLogging {

  import logger._

  info(s"running of $getClass")

  private val PAUSE_15_MINUTES = 15
  private val DEFAULT_HOST = "google.com"
  private val DEFAULT_COUNT = 3
  private val theApp = RunAppController

  private val conf = ConfigFactory.load()
  private val finish = Promise[Unit]

  import conf._

  // select mode of running
  Try(args(0)) match {
    case Success("start") if theApp.isRunnable =>
      debug("start second instance")
      println("application is already working")
    case Success("start") => start()
    case Success("stop") if !theApp.isRunnable =>
      debug("stop application but it is not working now")
      println("applications is not working")
    case Success("stop") => stop()
    case _ => other()
  }

  /**
    * The method performed if mode of running is "start"
    */
  def start(): Unit = {

    info("starting ...")
    theApp.run()

    val pause = Try(getInt("main.pause")).getOrElse(PAUSE_15_MINUTES) * 1000 * 60
    val host = Try(getString("main.host")).getOrElse(DEFAULT_HOST)
    val count = Try(getInt("main.count")).getOrElse(DEFAULT_COUNT)
    val profile = DatabaseConfig.forConfig[JdbcProfile]("prod").profile
    val db = Database.forConfig("prod.db")
    implicit val dbContext: DBContext = DBContext(profile, db)

    upPingProcess(host, count, pause)
    upStopHandler(finish)

    info("start [OK]")

    Await.ready(finish.future, Duration.Inf)
    db.close()

    info("application has been stopped")

  }

  /**
    * The method performed if mode of running is "stop"
    */
  def stop(): Unit = {
    info("stopping ...")
    theApp.stop()
  }

  /**
    * The method performed if mode of running is not "start" or "stop"
    */
  def other(): Unit = {
    debug("first argument is not valid")
    println("Available arguments only start|stop")
  }

  /**
    * The method runs writing of statistic about ping to database
    *
    * @param host    host for ping
    * @param count   count of ping
    * @param pause   pause between pings
    * @param context DBContext
    * @return
    */
  def upPingProcess(host: String, count: Int, pause: Int)(implicit context: DBContext): Future[Unit] = Future {
    debug("run ping process")

    val dao = new PingReports

    while (true) {
      val pingTool = SimplePinger(host, count)
      val dateTime = Timestamp.valueOf(LocalDateTime.now())
      val minPingTime = pingTool.minPing
      val avgPingTime = pingTool.avgPing
      val maxPingTime = pingTool.maxPing
      val comments = Option(pingTool.toString.getBytes)
      val pingReport = PingReport(None, dateTime, minPingTime, avgPingTime, maxPingTime, comments)
      dao.save(pingReport)
      Thread.sleep(pause)
    }
  }

  /**
    * The method runs process of waiting finish of application
    *
    * @param finish promise of finish
    * @return
    */
  def upStopHandler(finish: Promise[Unit]): Future[Unit] = Future {
    while (theApp.isRunnable)
      Thread.sleep(5000)
    finish.success()
  }

}
