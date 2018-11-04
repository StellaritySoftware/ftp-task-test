import commonpages.LoginPage
import geb.spock.GebReportingSpec
import helpers.DirectoryComparator
import pages.Config
import pages.TaskTypesPage

import java.nio.file.Paths

class FtpUploadIncludeExcludeTest extends GebReportingSpec
{
    def run()
    {
        when:
        def loginPage = browser.to LoginPage

        // Download from server
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
        ftpDownloadConfiguration.clickSave()

        tasks = configureTasksPage.addTask(TaskTypesPage)
        def ftpUploadConfiguration = tasks.selectFtpUpload()
        ftpUploadConfiguration.ftpServerUrl << Config.ftpUrlUpload
        ftpUploadConfiguration.cleanUpRemoteDirectoryBeforeUpload = true
        ftpUploadConfiguration.usernameFtp << Config.ftpUser
        ftpUploadConfiguration.passwordFtp << Config.ftpPassword
        ftpUploadConfiguration.includePatternField << includePattern
        ftpUploadConfiguration.excludePatternField << excludePattern
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

        DirectoryComparator.verifyDirs(Paths.get(sample), Config.subdirectoryBuildDir)

        where:
        includePattern                 | excludePattern       || sample
        "**/*.xml,**/*.java, **/*.txt" | "**/*.txt"           || Config.ftpSampleIncludeExclude
        "**/*.xml,**/*.txt"            | ""                   || Config.ftpSampleInclude
        ""                             | "**/*.java,**/*.txt" || Config.ftpSampleExclude
    }
}