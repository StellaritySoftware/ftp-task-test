package pages

import geb.Page
import org.openqa.selenium.By


/**
 * Created by Kateryna on 09.12.2017.
 */
class CreatedPlanPage extends Page {

    static at = {$(By.id("breadcrumb:" + Config.projKey.toString() + "-" + Config.planKey.toString())).text() == Config.planName}
    static content = {
        runDropdown{$("button.aui-button.aui-dropdown2-trigger", text : "Run")}
        manualBuild{$(By.id("manualBuild_" + Config.projKey + "-" + Config.planKey))}
    }
    def runManualBuild(){
        runDropdown.click()
        manualBuild.click()
        browser.at PlanBuildPage
    }
}
