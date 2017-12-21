package ping

import scala.concurrent.ExecutionContext.Implicits.global
import java.net.InetAddress

import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Try

/**
  * The class is simple implementation of ping tool
  */
class SimplePinger private(host: String, count: Int, timeOut: Int) extends Pinger {

  // sequence of all times of ping
  private lazy val pingTimes: Seq[Option[Int]] = for (_ <- 1 to count) yield {

    // async ping
    val pingAction = Future {
      val ts = currentTime
      InetAddress.getByName(host).isReachable(Int.MaxValue)
      (currentTime - ts).toInt
    }

    // wait result with time out
    val pingTime = Try {
      Await.ready(pingAction, Duration.apply(timeOut, MILLISECONDS))
      pingAction.value.get.get
    }

    pingTime.toOption

  }

  // only successful results of ping
  private lazy val pingSuccessTimes: Seq[Int] = pingTimes.collect { case Some(t) => t }

  /**
    * The method return current time in milliseconds
    *
    * @return
    */
  private def currentTime: Long = System.currentTimeMillis()

  /**
    * @see [[ping.Pinger.minPing]]
    * @return
    */
  override def minPing: Int =
    Try(pingSuccessTimes.min).toOption.getOrElse(-1)

  /**
    * @see [[ping.Pinger.maxPing]]
    * @return
    */
  override def maxPing: Int =
    Try(pingSuccessTimes.max).toOption.getOrElse(-1)

  /**
    * @see [[ping.Pinger.avgPing]]
    * @return
    */
  override def avgPing: Int =
    Try(pingSuccessTimes.sum / pingSuccessTimes.size).toOption.getOrElse(-1)

  /**
    * @see [[ping.Pinger.successPing]]
    * @return
    */
  override def successPing: Int = pingSuccessTimes.size

  /**
    * @see [[ping.Pinger.failedPing]]
    * @return
    */
  override def failedPing: Int = count - successPing

  /**
    * @see [[ping.Pinger.allPing]]
    * @return
    */
  override def allPing: Seq[Option[Int]] = pingTimes

  /**
    * The method prints ping statistic
    *
    * @return
    */
  override def toString: String =
    s"""Ping to $host:
       |Minimum time of ping: $minPing ms
       |Average time of ping: $avgPing ms
       |Maximum time of ping: $maxPing ms
       |Sent packages: $count
       |Received packages: $successPing
       |Lost packages: $failedPing""".stripMargin
}

/**
  * The object is intended for easy creating instance of SimplePinger
  */
object SimplePinger {

  private val DEFAULT_TIME_OUT = 30000
  private val DEFAULT_COUNT = 3

  /**
    * The method return instance of SimplePinger
    *
    * @param host    address of host for ping
    * @param count   count of requests
    * @param timeOut time out of request
    * @return
    * @see [[ping.SimplePinger]]
    */
  def apply(host: String, count: Int, timeOut: Int): SimplePinger =
    new SimplePinger(host, count, timeOut)

  /**
    * The method return instance of SimplePinger
    * Time out of ping uses by default (3000 ms)
    *
    * @param host  address of host for ping
    * @param count count of requests
    * @return
    * @see [[ping.SimplePinger]]
    */
  def apply(host: String, count: Int): SimplePinger = apply(host, count, DEFAULT_TIME_OUT)

  /**
    * The method return instance of SimplePinger
    * Time out of ping uses by default (3000 ms)
    * Count of requests uses by default (3)
    *
    * @param host address of host for ping
    * @return
    * @see [[ping.SimplePinger]]
    */
  def apply(host: String): SimplePinger = apply(host, DEFAULT_COUNT, DEFAULT_TIME_OUT)

}
