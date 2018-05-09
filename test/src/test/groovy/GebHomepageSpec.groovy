import geb.spock.GebReportingSpec
import pages.*

class GebHomepageSpec extends GebReportingSpec
{
    def "login to dashboard"()
    {
        when:
        to LoginPage
        login("admin", "admin")

        then:
        at DashboardPage
    }
}