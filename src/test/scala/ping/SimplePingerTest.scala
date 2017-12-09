package ping

import com.typesafe.scalalogging.LazyLogging
import utest._

/**
  * The class is test of SimplePinger
  * Host for testing is localhost
  */
object SimplePingerTest extends TestSuite with LazyLogging {

  import logger._

  private val GOOD_HOST = "localhost"
  private val BAD_HOST = s"_$GOOD_HOST"

  debug("Start test of SimplePingerTest")
  debug(s"host: $GOOD_HOST")
  debug(s"bad host: $BAD_HOST")

  def tests: Tests = Tests {

    'goodPing - {
      val pingTool = SimplePinger(GOOD_HOST)
      debug(s"Result of ping $GOOD_HOST: ${pingTool.allPing}")
      assert(pingTool.successPing == 3)
      assert(pingTool.failedPing == 0)
      assert(pingTool.minPing >= 0)
      assert(pingTool.maxPing >= 0)
      assert(pingTool.avgPing >= 0)
    }

    'badPing - {
      val pingTool = SimplePinger(BAD_HOST)
      debug(s"Result of ping $BAD_HOST: ${pingTool.allPing}")
      assert(pingTool.successPing == 0)
      assert(pingTool.failedPing == 3)
      assert(pingTool.minPing == -1)
      assert(pingTool.maxPing == -1)
      assert(pingTool.avgPing == -1)
    }

  }

}
