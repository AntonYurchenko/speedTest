package ping

/**
  * The trait describes functions of ping tools
  */
trait Pinger {

  /**
    * The method returns minimum time of ping
    *
    * @return
    */
  def minPing: Int

  /**
    * The method returns maximum time of ping
    *
    * @return
    */
  def maxPing: Int

  /**
    * The method returns average time of ping
    *
    * @return
    */
  def avgPing: Int

  /**
    * The method returns count of success ping
    *
    * @return
    */
  def successPing: Int

  /**
    * The method returns count of failed ping
    *
    * @return
    */
  def failedPing: Int

  /**
    * The method returns sequence of all times of ping
    *
    * @return
    */
  def allPing: Seq[Option[Int]]
}
