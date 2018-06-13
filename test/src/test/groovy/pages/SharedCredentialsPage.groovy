package pages

import geb.Page
import org.openqa.selenium.By

class SharedCredentialsPage extends Page
{
    static url = "/bamboo/admin/credentials/configureSharedCredentials.action"
    static at = { $("section.aui-page-panel-content h1").text() == "Shared credentials" }

    static content =
    {
        addNewCredentialButton{$("button#createSharedCredentials")}
        usernameAndPassword {$("div#dropdown-shared-credentials li:nth-of-type(3) a.update-credentials")}
        credentialsNameField{$("#credentialsName")}
        username{$("#username")}
        password{$("#password")}
        saveCredentialButton(required:false){$("#createSharedCredentials_save")}
        tableCells{$(By.xpath("/html//section[@id='content']//section[@class='aui-page-panel-content']//table[@class='aui']/tbody/tr/td[1]"))} //remove 1st cell
    }

    def credentialName = null

    def clickOnDropdown(){
        waitFor {addNewCredentialButton.isDisplayed()}
        addNewCredentialButton.click()
        usernameAndPassword.click()
    }

    def waitForCredentialFieldsAreDisplayed(){
        waitFor {credentialsNameField.isDisplayed()}
    }

    def saveCredentials(){
        saveCredentialButton.click()
    }

    def checkCreadentialsSaved(String credentials){
        waitFor {!saveCredentialButton.displayed}
        def size = tableCells.size()
        tableCells[size-1].text().trim() == credentials
    }

    def generateRandomCredentials(){
        Random r = new Random()
        credentialName = "positive_${Math.abs(r.nextInt() - Integer.parseInt("2147483647"))}"
    }
}