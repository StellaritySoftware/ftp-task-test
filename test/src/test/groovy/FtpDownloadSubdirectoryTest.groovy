import geb.spock.GebReportingSpec
import helpers.DirectoryComparator
import pages.Config
import commonpages.LoginPage
import pages.TaskTypesPage

import java.nio.file.Paths

class FtpDownloadSubdirectoryTest extends GebReportingSpec
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
        ftpDownloadConfiguration.includePatternField << "**/*.xml,**/*.java,**/*.txt"
        ftpDownloadConfiguration.excludePatternField << "**/*.txt"
        ftpDownloadConfiguration.subdirectory << Config.subdirectory
        ftpDownloadConfiguration.clickSave()
        configureTasksPage.enablePlanCheckBox = true

        def createdPlan = configureTasksPage.clickCreateButton()

        def planBuild = createdPlan.runManualBuild()

        then:
        planBuild.waitForSuccessfulHeader()

        DirectoryComparator.verifyDirs(Paths.get(Config.ftpSampleIncludeExclude), Config.subdirectoryBuildDir)
    }
}