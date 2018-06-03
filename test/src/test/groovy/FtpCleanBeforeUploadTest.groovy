import geb.spock.GebReportingSpec
import helpers.DirectoryComparator
import pages.Config
import pages.LoginPage

import java.nio.file.Paths

class FtpCleanBeforeUploadTest extends GebReportingSpec
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
        ftpDownloadConfiguration.passwordFtp << Config.ftpPassword
        ftpDownloadConfiguration.clickSave()

        tasks = configureTasksPage.addTask()
        def ftpUploadConfiguration = tasks.selectFtpUpload()
        ftpUploadConfiguration.cleanUpRemoteDirectoryBeforeUpload = true
        ftpUploadConfiguration.ftpServerUrl << Config.ftpUrlUpload
        ftpUploadConfiguration.usernameFtp << Config.ftpUser
        ftpUploadConfiguration.passwordFtp << Config.ftpPassword
        ftpUploadConfiguration.clickSave()

        tasks = configureTasksPage.addTask()
        ftpUploadConfiguration = tasks.selectFtpUpload()
        ftpUploadConfiguration.cleanUpRemoteDirectoryBeforeUpload = true
        ftpUploadConfiguration.ftpServerUrl << Config.ftpUrlUpload
        ftpUploadConfiguration.usernameFtp << Config.ftpUser
        ftpUploadConfiguration.passwordFtp << Config.ftpPassword
        ftpUploadConfiguration.includePatternField << "**/*.xml,**/*.java,**/*.txt"
        ftpUploadConfiguration.excludePatternField << "**/*.txt"
        ftpUploadConfiguration.clickSave()

        tasks = configureTasksPage.addTask()
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
        DirectoryComparator.verifyDirs(Paths.get(Config.ftpSampleIncludeExclude), Config.subdirectoryBuildDir)
    }
}