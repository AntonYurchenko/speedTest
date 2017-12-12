package db.table.mapping

import java.sql.Timestamp
import db.dao._
import db.table.entity.PingReport

/**
  * The trait contains class for mapping entity [[PingReport]] to table ping_report in database
  */
trait PingReportMapping {
  this: DAO =>

  import this.context.profile.api._

  /**
    * Mapping class
    *
    * @param tag Tag
    */
  class PingReportTable(tag: Tag) extends Table[PingReport](tag, "ping_reports") {
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)

    def dateTime = column[Timestamp]("date_time")

    def minPingTime = column[Int]("min_ping_time")

    def avgPingTime = column[Int]("avg_ping_time")

    def maxPingTime = column[Int]("max_ping_time")

    def comments = column[Option[Array[Byte]]]("comments")

    def * = (id, dateTime, minPingTime, avgPingTime, maxPingTime, comments) <>
      (PingReport.tupled, PingReport.unapply)
  }

  val table = TableQuery[PingReportTable]
}
