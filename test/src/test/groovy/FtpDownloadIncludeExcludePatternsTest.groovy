import geb.spock.GebReportingSpec
import helpers.DirectoryComparator
import pages.Config
import commonpages.LoginPage
import pages.TaskTypesPage

import java.nio.file.Paths

class FtpDownloadIncludeExcludePatternsTest extends GebReportingSpec
{

    def run()
    {
        when:
        def loginPage = browser.to LoginPage

        def dashboardPage = loginPage.login(Config.user, Config.password)

        def createNewPlanConfigurePlanPage = dashboardPage.createNewPlan()
        createNewPlanConfigurePlanPage.setRandomProjectPlanNames()
        createNewPlanConfigurePlanPage.setNoneRepository()

        def configureTasksPage = createNewPlanConfigurePlanPage.clickConfigurePlanButton()

        def tasks = configureTasksPage.addTask(TaskTypesPage)

        def ftpDownloadConfiguration = tasks.selectFtpDownload()
        ftpDownloadConfiguration.ftpServerUrl << Config.ftpUrlDownload
        ftpDownloadConfiguration.usernameFtp << Config.ftpUser
        ftpDownloadConfiguration.passwordFtp << Config.ftpPassword
        ftpDownloadConfiguration.includePatternField << includePattern
        ftpDownloadConfiguration.excludePatternField << excludePattern
        ftpDownloadConfiguration.clickSave()
        configureTasksPage.enablePlanCheckBox = true

        def createdPlan = configureTasksPage.clickCreateButton()

        def planBuild = createdPlan.runManualBuild()

        then:

        planBuild.waitForSuccessfulHeader()
        DirectoryComparator.verifyDirs(Paths.get(sample), Config.buildDir)

        where:
        includePattern                | excludePattern       || sample
        "**/*.xml,**/*.java,**/*.txt" | "**/*.txt"           || Config.ftpSampleIncludeExclude
        "**/*.xml,**/*.txt"           | ""                   || Config.ftpSampleInclude
        ""                            | "**/*.java,**/*.txt" || Config.ftpSampleExclude
    }
}