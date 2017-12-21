package db.dao

import utest._
import java.sql.Timestamp
import com.typesafe.scalalogging.LazyLogging
import db.table.entity.PingReport
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.jdbc.JdbcBackend.Database
import scala.concurrent.duration.Duration
import scala.concurrent.Await

/**
  * This is test suite for DAO [[PingReports]]
  * The test suite uses test configuration of database from application.conf
  */
object PingReportsTest extends TestSuite with LazyLogging {

  import logger._

  implicit val dbContext: DBContext = DBContext(
    profile = DatabaseConfig.forConfig[JdbcProfile]("test").profile,
    db = Database.forConfig("test.db")
  )

  import dbContext._

  private val dao = new PingReports
  private val testPingReport = PingReport(
    Some(1), Timestamp.valueOf("2017-12-31 23:12:00.0"), 1, 2, 3, None
  )

  debug("Start test of PingReports")
  debug(s"Test profile: ${profile.getClass}")
  debug(s"Test report: $testPingReport")

  override def tests: Tests = Tests {

    'savePingReport - Await.ready(dao.save(testPingReport), Duration.Inf)

    'readPingReport - {
      val seq = Await.result(dao.read(), Duration.Inf)
      debug(s"It is result set: $seq")
      debug(s"Test report: $testPingReport")
      assert(seq.contains(testPingReport))
    }

  }

  override def utestAfterAll(): Unit = {
    dbContext.db.close()
  }
}
