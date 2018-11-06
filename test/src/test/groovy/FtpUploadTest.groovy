import commonpages.LoginPage
import configuration.CommonConfig
import geb.spock.GebReportingSpec
import helpers.DirectoryComparator
import pages.Config
import pages.TaskTypesPage
import spock.lang.Title
import spock.lang.Unroll

import java.nio.file.Paths

@Unroll
@Title("Upload files to the FTP server")
class FtpUploadTest extends GebReportingSpec
{
    def run()
    {
        when:
        def loginPage = browser.to LoginPage

        def dashboardPage = loginPage.login(CommonConfig.user, CommonConfig.password)

        def createNewPlanConfigurePlanPage = dashboardPage.createNewPlan()
        createNewPlanConfigurePlanPage.setRandomProjectPlanNames()
        createNewPlanConfigurePlanPage.setNoneRepository()

        def configureTasksPage = createNewPlanConfigurePlanPage.clickConfigurePlanButton()

        def tasks = configureTasksPage.addTask(TaskTypesPage)
        def ftpDownloadConfiguration = tasks.selectFtpDownload()
        ftpDownloadConfiguration.ftpServerUrl << Config.ftpUrlDownload
        ftpDownloadConfiguration.usernameFtp << Config.ftpUser
        ftpDownloadConfiguration.passwordFtp << Config.ftpPassword
        ftpDownloadConfiguration.clickSave()

        tasks = configureTasksPage.addTask(TaskTypesPage)
        def ftpUploadConfiguration = tasks.selectFtpUpload()
        ftpUploadConfiguration.ftpServerUrl << Config.ftpUrlUpload
        ftpUploadConfiguration.cleanUpRemoteDirectoryBeforeUpload = true
        ftpUploadConfiguration.usernameFtp << Config.ftpUser
        ftpUploadConfiguration.passwordFtp << Config.ftpPassword
        ftpUploadConfiguration.clickSave()

        tasks = configureTasksPage.addTask(TaskTypesPage)
        ftpDownloadConfiguration = tasks.selectFtpDownload()
        ftpDownloadConfiguration.ftpServerUrl << Config.ftpUrlUpload
        ftpDownloadConfiguration.usernameFtp << Config.ftpUser
        ftpDownloadConfiguration.passwordFtp << Config.ftpPassword
        ftpDownloadConfiguration.subdirectory << Config.subdirectory
        ftpDownloadConfiguration.clickSave()

        configureTasksPage.enablePlanCheckBox = true
        def createdPlan = configureTasksPage.clickCreateButton()

        def planBuild = createdPlan.runManualBuild()

        then:
        planBuild.waitForSuccessfulHeader()

        DirectoryComparator.verifyDirs(Paths.get(Config.ftpSample), Config.subdirectoryBuildDir)
    }
}