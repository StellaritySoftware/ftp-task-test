package pages



import geb.Page
import org.openqa.selenium.By


/**
 * Created by Kateryna on 09.12.2017.
 */
class PlanBuildPage extends Page {

    static at = {$(By.id("breadcrumb:${Config.projKey}-${Config.planKey}"))}
    static content = {
        successfulHeader{$("div#sr-build.status-ribbon-status.Successful")}
        failedHeader{$("div#sr-build.status-ribbon-status.Failed")}
        buttonActions{$("button.aui-button.aui-dropdown2-trigger span.aui-icon.aui-icon-small.aui-iconfont-configure")}
        configurePlanLink{$(By.id("editBuild:${Config.projKey}-${Config.planKey}"))}
        defaultJobLink{$(By.id("viewJob_${Config.projKey}-${Config.planKey}-JOB1"))}
    }

    def waitForSuccessfulHeader() {
        waitFor {successfulHeader.isDisplayed()}
    }

    def waitForFailedHeader() {
        waitFor {failedHeader.isDisplayed()}
    }

    def clickEditPalnLink(){
        buttonActions.click()
        configurePlanLink.click()
    }

    def clickDefaultJobLink(){
        waitFor {defaultJobLink.isDisplayed()}
        defaultJobLink.click()
    }

 }
