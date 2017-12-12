package db.dao

import db.table.entity.PingReport
import db.table.mapping.PingReportMapping
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * The class is data access object to table ping_report in database
  *
  * @param context options of connection
  */
class PingReports(implicit val context: DBContext) extends PingReportMapping with DAO {

  import context._
  import profile.api._

  /**
    * The method writes [[PingReport]] to database
    *
    * @param item ping report
    * @return
    */
  def save(item: PingReport): Future[Unit] = {

    val actions = DBIO.seq(
      createSchemaIfNotExist, // creating schema if not exist
      table += item // add item to table
    ).transactionally

    db.run(actions)

  }

  /**
    * The method returns data base I/O action for creating of table ping_report
    * if the table is not exist yet
    *
    * @return
    */
  private def createSchemaIfNotExist: DBIOAction[Unit, NoStream, Effect.Read with Effect.Schema] =
    MTable.getTables.flatMap {
      mTables =>
        val tableNames = mTables.map(_.name.name)
        if (!tableNames.contains(table.baseTableRow.tableName))
          table.schema.create
        else
          DBIO.successful()
    }

  /**
    * The method returns sequence of all [[PingReport]] from database
    *
    * @return
    */
  def read(): Future[Seq[PingReport]] = db.run(table.result)

}