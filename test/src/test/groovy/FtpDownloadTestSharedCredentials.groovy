import geb.spock.GebReportingSpec
import helpers.DirectoryComparator
import pages.Config
import pages.LoginPage


import java.nio.file.Paths

class FtpDownloadTestSharedCredentials extends GebReportingSpec
{
    def run()
    {
        when:
        def loginPage = browser.to LoginPage

        def dashboardPage = loginPage.login(Config.user, Config.password)

        def userManagement = dashboardPage.openUserManagementPage()

        def sharedCredentials = userManagement.clickOnSharedCredentialLink()
        sharedCredentials.clickOnDropdown()
        sharedCredentials.waitForCredentialFieldsAreDisplayed() //вынести
        sharedCredentials.generateRandomCredentials()
        sharedCredentials.credentialsNameField << sharedCredentials.credentialName // вынести переменные в тест
        sharedCredentials.username << Config.ftpUser
        sharedCredentials.password << Config.ftpPassword
        sharedCredentials.saveCredentials()

        then:
        sharedCredentials.checkCreadentialsSaved(sharedCredentials.credentialName)

        when:
        def createNewPlanConfigurePlanPage = dashboardPage.createNewPlan()
        createNewPlanConfigurePlanPage.setRandomProjectPlanNames()
        createNewPlanConfigurePlanPage.setNoneRepository()

        def configureTasksPage = createNewPlanConfigurePlanPage.clickConfigurePlanButton()

        def tasks = configureTasksPage.addTask()

        def ftpDownloadConfiguration = tasks.selectFtpDownload()
        ftpDownloadConfiguration.ftpServerUrl << Config.ftpUrlDownload
        ftpDownloadConfiguration.chooseUseSharedCredentials()
        ftpDownloadConfiguration.dropDownCredentials.click() // без клика будет лти работать?
        ftpDownloadConfiguration.dropDownCredentials = sharedCredentials.credentialName
        ftpDownloadConfiguration.clickSave()
        // Edit
        configureTasksPage.enablePlanCheckBox = true

        def createdPlan = configureTasksPage.clickCreateButton()

        def planBuild = createdPlan.runManualBuild()

        then:
        planBuild.waitForSuccessfulHeader()
        DirectoryComparator.verifyDirs(Paths.get(Config.ftpSample), Config.buildDir)
    }
}