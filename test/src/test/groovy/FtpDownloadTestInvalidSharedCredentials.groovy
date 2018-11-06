import configuration.CommonConfig
import geb.spock.GebReportingSpec
import helpers.DirectoryComparator
import pages.Config
import commonpages.LoginPage
import pages.TaskTypesPage

import java.nio.file.NoSuchFileException
import java.nio.file.Paths

class FtpDownloadTestInvalidSharedCredentials extends GebReportingSpec
{
    def invalidSharedCredentials()
    {
        when:
        def loginPage = browser.to LoginPage

        def dashboardPage = loginPage.login(CommonConfig.user, CommonConfig.password)

        def userManagement = dashboardPage.openUserManagementPage()

        def sharedCredentials = userManagement.clickOnSharedCredentialLink()
        sharedCredentials.clickOnDropdown()
        def credentialName = sharedCredentials.generateRandomCredentials()
        sharedCredentials.credentialsNameField << credentialName
        sharedCredentials.username << Config.ftpInvalidUser
        sharedCredentials.password << Config.ftpInvalidPassword
        sharedCredentials.saveCredentials()

        then:
        sharedCredentials.checkCreadentialsSaved(credentialName)

        when:
        def createNewPlanConfigurePlanPage = dashboardPage.createNewPlan()
        createNewPlanConfigurePlanPage.setRandomProjectPlanNames()
        createNewPlanConfigurePlanPage.setNoneRepository()

        def configureTasksPage = createNewPlanConfigurePlanPage.clickConfigurePlanButton()

        def tasks = configureTasksPage.addTask(TaskTypesPage)

        def ftpDownloadConfiguration = tasks.selectFtpDownload()
        ftpDownloadConfiguration.ftpServerUrl << Config.ftpUrlDownload
        ftpDownloadConfiguration.chooseUseSharedCredentials()
        ftpDownloadConfiguration.dropDownCredentials = credentialName
        ftpDownloadConfiguration.clickSave()

        configureTasksPage.enablePlanCheckBox = true

        def createdPlan = configureTasksPage.clickCreateButton()

        def planBuild = createdPlan.runManualBuild()

        then:
        planBuild.waitForFailedHeader()

        when:
        DirectoryComparator.verifyDirs(Paths.get(Config.ftpSample), CommonConfig.buildDir)

        then:
        thrown(NoSuchFileException)
    }
}