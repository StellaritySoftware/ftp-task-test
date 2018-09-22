package pages

import geb.Page
import org.openqa.selenium.By

class CreateNewPlanConfigureTasksPage extends Page
{
    static url =  Config.context + "/build/admin/create/createPlanTasks.action"
    static at = { $("#content header h1").text().matches("Create(.*)plan") && $("#onePageCreate > h2").text() ==~ /Configure (tasks|Job)/ || $("#content header h1 a").text() == "Configuration - ${Config.planName}" }

    static content =
    {
        buttonAddTask(to: TaskTypesPage){$("#addTask")}
        enablePlanCheckBox(required: false){$("input#finalisePlanCreation_chainEnabled", type:"checkbox")}
        buttonCreate(required: false){$("#finalisePlanCreation_save")}
        buttonCreatePlan{$("#createPlan")} // Bamboo version 6.4.0
        editTaskLink {$(By.cssSelector("a[href='/bamboo/build/admin/edit/editTask.action?planKey=${Config.projKey}-${Config.planKey}-JOB1&taskId=1']"))}
    }

    def TaskTypesPage addTask(){
       buttonAddTask.click()
       browser.at TaskTypesPage
    }

    def clickCreateButton(){
        if(!buttonCreate.empty){
            buttonCreate.click()
        }
        else if(!buttonCreatePlan.empty){
            buttonCreatePlan.click()
        }
        browser.at CreatedPlanPage
    }

    def FTPDownloadConfigurationPage selectFTPDownloadtask(){
        editTaskLink.click()
        browser.at FTPDownloadConfigurationPage
    }

    def markEnablePlanCheckbox(){
        if(enablePlanCheckBox.isDisplayed()){
            enablePlanCheckBox = true
        }
    }
}