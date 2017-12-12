package db.dao

import slick.jdbc.JdbcProfile
import slick.jdbc.JdbcBackend.Database

/**
  * The class contains necessary options for working with database
  *
  * @param profile database profile
  * @param db      backend database
  */
case class DBContext(profile: JdbcProfile, db: Database)
