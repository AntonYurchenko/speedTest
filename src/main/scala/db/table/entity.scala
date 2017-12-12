package db.table

import java.sql.Timestamp

/**
  * The object contains all entity of database
  */
object entity {

  /**
    * The class is entity fo table ping_report
    *
    * @param id          identifier of entity
    * @param dateTime    data and time of ping test
    * @param minPingTime minimum of ping's time
    * @param avgPingTime average of ping's time
    * @param maxPingTime maximum of ping's time
    * @param comments    comments to ping test
    */
  case class PingReport(id: Option[Int], dateTime: Timestamp, minPingTime: Int, avgPingTime: Int, maxPingTime: Int, comments: Option[Array[Byte]])

}
