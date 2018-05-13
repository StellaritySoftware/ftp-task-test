package pages

import geb.Page

class DashboardPage extends Page
{
    static url = "/bamboo/start.action"
    static at = { $("#content h1").text() == "Build Dashboard" }

    static content =
    {
        buttonCreate { $(" #createPlanLink") }
        createPlan(to: CreateNewPlanConfigurePlanPage) { $("#createNewPlan") }
    }

    def CreateNewPlanConfigurePlanPage createNewPlan()
    {
        buttonCreate.click()
        createPlan.click()
        browser.at CreateNewPlanConfigurePlanPage
    }
}