import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the Troll entity.
 */
class TrollGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://127.0.0.1:8080"""

    val httpConf = http
        .baseURL(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connection("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "X-CSRF-TOKEN" -> "${csrf_token}"
    )

    val scn = scenario("Test the Troll entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))
        .check(headerRegex("Set-Cookie", "CSRF-TOKEN=(.*); [P,p]ath=/").saveAs("csrf_token")))
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authentication")
        .headers(headers_http_authenticated)
        .formParam("j_username", "admin")
        .formParam("j_password", "admin")
        .formParam("remember-me", "true")
        .formParam("submit", "Login"))
        .pause(1)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200))
        .check(headerRegex("Set-Cookie", "CSRF-TOKEN=(.*); [P,p]ath=/").saveAs("csrf_token")))
        .pause(10)
        .repeat(2) {
            exec(http("Get all trolls")
            .get("/api/trolls")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new troll")
            .post("/api/trolls")
            .headers(headers_http_authenticated)
            .body(StringBody("""{"id":null, "number":null, "name":"SAMPLE_TEXT", "race":null, "birthDate":"2020-01-01T00:00:00.000Z", "x":"0", "y":"0", "z":"0", "attack":"0", "dodge":"0", "damage":"0", "regeneration":"0", "hitPoint":"0", "currentHitPoint":"0", "view":"0", "rm":"0", "mm":"0", "armor":"0", "turn":null, "weight":null, "focus":"0", "attackP":"0", "dodgeP":"0", "damageP":"0", "regenerationP":"0", "hitPointP":"0", "attackM":"0", "dodgeM":"0", "damageM":"0", "regenerationM":"0", "hitPointM":"0", "viewP":"0", "rmP":"0", "mmP":"0", "armorP":"0", "weightP":null, "viewM":"0", "rmM":"0", "mmM":"0", "armorM":"0", "weightM":null, "level":"0", "kill":"0", "death":"0", "restrictedPassword":"SAMPLE_TEXT", "deleted":null, "hidden":null, "invisible":null, "intangible":null, "strain":"0", "pa":"0", "dla":"2020-01-01T00:00:00.000Z"}""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_troll_url")))
            .pause(10)
            .repeat(5) {
                exec(http("Get created troll")
                .get("${new_troll_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created troll")
            .delete("${new_troll_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(100) over (1 minutes))
    ).protocols(httpConf)
}
