import configuration.CommonConfig
import geb.spock.GebReportingSpec
import pages.Config
import commonpages.LoginPage
import pages.FTPDownloadConfigurationPage
import pages.TaskTypesPage


class FtpDownloadEditTest extends GebReportingSpec
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
        ftpDownloadConfiguration.includePatternField << "**/*.xml"
        ftpDownloadConfiguration.excludePatternField << "**/*.txt"
        ftpDownloadConfiguration.subdirectory << "ftpFolder"
        ftpDownloadConfiguration.clickSave()
        configureTasksPage.markEnablePlanCheckbox()

        configureTasksPage.editTask(FTPDownloadConfigurationPage)

        ftpDownloadConfiguration.ftpServerUrl = "ftp://katya.com"
        ftpDownloadConfiguration.usernameFtp = "login"
        ftpDownloadConfiguration.includePatternField = "**/*.txt"
        ftpDownloadConfiguration.excludePatternField = "**/*.java"
        ftpDownloadConfiguration.subdirectory = "katyaFolder"
        ftpDownloadConfiguration.uncollapseAdvancedOptions()
        ftpDownloadConfiguration.advancedOptionsRetryCount = "4"
        ftpDownloadConfiguration.advancedOptionsRetryDelay = "40"
        ftpDownloadConfiguration.clickSave()

        configureTasksPage.editTask(FTPDownloadConfigurationPage)

        then:
        ftpDownloadConfiguration.ftpServerUrl.value() ==  "ftp://katya.com"
        ftpDownloadConfiguration.usernameFtp.value() == "login"
        ftpDownloadConfiguration.includePatternField.value() == "**/*.txt"
        ftpDownloadConfiguration.excludePatternField.value() == "**/*.java"
        ftpDownloadConfiguration.subdirectory.value() == "katyaFolder"
        ftpDownloadConfiguration.advancedOptionsRetryCount.value() == "4"
        ftpDownloadConfiguration.advancedOptionsRetryDelay.value() == "40"
    }
}