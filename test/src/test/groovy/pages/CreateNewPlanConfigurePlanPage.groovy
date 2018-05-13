package pages

import geb.Page
import org.apache.commons.text.RandomStringGenerator
import org.openqa.selenium.Keys

class CreateNewPlanConfigurePlanPage extends Page
{
    static url = "/bamboo/build/admin/create/newPlan.action"
    static at = { $("#content header h1").text() == "Create plan" && $("#content h2").text() == "Configure plan"}

    static content =
    {
        existingProjectKey(required: false) { $("#createPlan_existingProjectKey") } // enabled if there is at least 1 project
        projectName { $("#projectName") }
        projectKey { $("#createPlan_projectKey") }
        chainName { $("#chainName") }
        chainKey { $("#createPlan_chainKey") }
        newRepositoryOption(required: false) { $("#repositoryTypeCreateOption") }
        noneRepositoryOption(required: false){$("#repositoryTypeNoneOption")}
        selectRepositoryLink { $("#repository-other") }
        tfsRepositoryLink { $("#repository-other-dropdown a", text: "TFS") }
        noneRepositoryLink { $("#repository-other-dropdown a", text: "None") }
        repositoryName { $("#createPlan_repositoryName") }
        tfsUrl { $("#createPlan_stellarity_tfs_repository_url") }
        tfsPath { $("#createPlan_stellarity_tfs_repository_path") }
        tfsUser { $("#createPlan_stellarity_tfs_repository_username") }
        tfsPassword { $("#createPlan_stellarity_tfs_temporary_password") }
        tfsBranch { $("#createPlan_stellarity_tfs_repository_branch_name") }
        tfsTestConnection { $("#test-connection-com-stellarity-bamboo-tfs-repository-plugin-tfs-v2") }
        allowToReuseForAll { $("#createPlan_linkedRepositoryAccessOptionALL_USERS") }
        allowToReuseForCreator { $("#createPlan_linkedRepositoryAccessOptionCREATOR") }
        configurePlanButton(to: CreateNewPlanConfigureTasksPage) { $("#createPlan_save")}

    }

    def setRandomProjectPlanNames(){
        char start = 'A'
        char end = 'Z'
        def generator = new RandomStringGenerator.Builder().withinRange(start as int, end as int).build()

        if (existingProjectKey)
        {
            existingProjectKey = "newProject"
        }

        projectName = generator.generate(8)
        chainName = generator.generate(8)

        Config.projName = projectName.value()
        Config.planName = chainName.value()
        Config.projKey = projectKey.value()
        Config.planKey = chainKey.value()

        println("project name - " + Config.projName)
        println("plan name - " + Config.planName)
        println("project key - " + Config.projKey)
        println("plan key - " + Config.planKey)
    }

    def setNoneRepository(){

        // for Bamboo 6.4 version
        if (noneRepositoryOption.isDisplayed()){
            noneRepositoryOption.click()
            return
        }

        if (newRepositoryOption.isDisplayed()){
            newRepositoryOption.click()
        }
        selectRepositoryLink.click()
        interact{
            moveToElement(noneRepositoryLink)
        }
        noneRepositoryLink.click()
    }

    def clickConfigurePlanButton(){
        interact {
            moveToElement(configurePlanButton)
        }

        configurePlanButton << Keys.ENTER
        browser.at CreateNewPlanConfigureTasksPage
    }
}