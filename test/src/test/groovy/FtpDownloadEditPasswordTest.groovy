import geb.spock.GebReportingSpec
import helpers.DirectoryComparator
import pages.Config
import pages.LoginPage

import java.nio.file.NoSuchFileException
import java.nio.file.Paths

class FtpDownloadEditPasswordTest extends GebReportingSpec
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

        def tasks = configureTasksPage.addTask()

        def ftpDownloadConfiguration = tasks.selectFtpDownload()
        ftpDownloadConfiguration.ftpServerUrl << Config.ftpUrlDownload
        ftpDownloadConfiguration.usernameFtp << Config.ftpUser
        ftpDownloadConfiguration.passwordFtp << Config.ftpInvalidPassword
        ftpDownloadConfiguration.clickSave()
        configureTasksPage.enablePlanCheckBox = true

        def createdPlan = configureTasksPage.clickCreateButton()
        def planBuild = createdPlan.runManualBuild()

        then:
        planBuild.waitForFailedHeader()

        when:
        DirectoryComparator.verifyDirs(Paths.get(Config.ftpSample), Config.buildDir)

        then:
        thrown(NoSuchFileException)

        when:
        planBuild.clickEditPalnLink()
        planBuild.clickDefaultJobLink()
        configureTasksPage.selectFTPDownloadtask()

        ftpDownloadConfiguration.changePassword()
        ftpDownloadConfiguration.passwordFtp = Config.ftpPassword

        ftpDownloadConfiguration.clickSave()
        createdPlan.runManualBuild()

        then:
        planBuild.waitForSuccessfulHeader()
        DirectoryComparator.verifyDirs(Paths.get(Config.ftpSample), Config.buildDir)
    }
}