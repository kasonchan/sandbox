import io.gatling.core.Predef._
import io.gatling.http.Predef.http

/**
  * @author kason.chan
  * @since Dec-2017
  */
class GatlingSimulation extends Simulation {

  val userNum = 10

  val httpConf = http
    .baseURL("https://www.google.ie")
    .acceptHeader(
      "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader(
      "Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("BasicSimulation")
    .exec(
      http("request_1")
        .get("/"))
    .pause(5)

  setUp(
    scn.inject(atOnceUsers(userNum))
  ).protocols(httpConf)

}
