package pages

import geb.Page

class UserManagementPage extends Page
{
    static url = "/bamboo/admin/user/viewUsers.action"
    static at = { $("section.aui-page-panel-content h1").text() == "User management" || $("section.aui-page-panel-content h1").text() == "Users"}

    static content =
    {
        sharedCredentialLink{$("a#sharedCredentialsConfig")}

    }

    def clickOnSharedCredentialLink(){
        waitFor {sharedCredentialLink.isDisplayed()}
        sharedCredentialLink.click()
        browser.at SharedCredentialsPage
    }
}