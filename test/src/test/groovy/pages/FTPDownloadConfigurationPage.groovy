package pages

import geb.Page

/**
 * Created by Kateryna on 05.12.2017.
 */
class FTPDownloadConfigurationPage extends Page{

    static url = {Config.context + "/build/admin/create/createPlanTasks.action"}
    static at = { ($("#createTask h2").text() == "FTP Download configuration" ||
                $("#updateTask h2").text() == "FTP Download configuration")
    }

    static content = {
        ftpServerUrl {$("#ftpTaskUrl")}
        usernameFtp {$("#ftpTaskUsername")}
        passwordFtp {$("#ftpTaskPlainPassword")}
        buttonSave {$("#createTask_defaultSave")}
        successfulTaskCreationText {$("div.aui-message.aui-message-success").text() == "Task created successfully."}
        successfulTaskUpdatedText {$("div.aui-message.aui-message-success").text() == "Task saved successfully."}
        changePassword {$("input#ftpTaskPasswordChange")}
        includePatternField {$("input#ftpTaskIncludePattern")}
        excludePatternField {$("input#ftpTaskExcludePattern")}
        subdirectory {$("input#ftpTaskSubdirectory")}
        advancedOptionCollapse{$("form#updateTask div span.icon.icon-expand")}
        advancedOptionsRetryCount{$("input#ftpTaskRetryCount")}
        advancedOptionsRetryDelay{$("input#ftpTaskRetryDelay")}
    }

    def clickSave(){
        js.exec(
            "var createSave = document.getElementById('createTask_save');" +
            "var updateSave = document.getElementById('updateTask_save');" +
            "createSave ? createSave.click() : updateSave.click();"
        )
        browser.waitFor{successfulTaskCreationText || successfulTaskUpdatedText}
        browser.at CreateNewPlanConfigureTasksPage
    }

    def uncollapseAdvancedOptions(){
        js."document.querySelector('form#updateTask div span.icon.icon-expand').click()"
        waitFor{advancedOptionsRetryCount.isDisplayed()}
    }

    def changePassword(){
        js.exec("scroll(0, 250)")
        changePassword.click()
        waitFor {passwordFtp.isDisplayed()}
    }
}
