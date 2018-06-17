import geb.spock.GebReportingSpec
import helpers.DirectoryComparator
import pages.Config
import pages.LoginPage

import java.nio.file.Paths

class FtpUploadTestSharedCredentials extends GebReportingSpec
{
    def run()
    {
        when:
        def loginPage = browser.to LoginPage
        def dashboardPage = loginPage.login(Config.user, Config.password)

        def userManagement = dashboardPage.openUserManagementPage()
        def sharedCredentials = userManagement.clickOnSharedCredentialLink()
        sharedCredentials.clickOnDropdown()
        def credentialName = sharedCredentials.generateRandomCredentials()
        sharedCredentials.credentialsNameField << credentialName
        sharedCredentials.username << Config.ftpUser
        sharedCredentials.password << Config.ftpPassword
        sharedCredentials.saveCredentials()

        then:
        sharedCredentials.checkCreadentialsSaved(credentialName)

        when:
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
        ftpUploadConfiguration.ftpServerUrl << Config.ftpUrlUpload
        ftpUploadConfiguration.cleanUpRemoteDirectoryBeforeUpload = true
        ftpUploadConfiguration.chooseUseSharedCredentials()
        ftpUploadConfiguration.dropDownCredentials = credentialName
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
        DirectoryComparator.verifyDirs(Paths.get(Config.ftpSample), Config.subdirectoryBuildDir)

    }
}