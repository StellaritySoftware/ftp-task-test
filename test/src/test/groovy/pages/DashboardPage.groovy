package pages

import geb.Page

class DashboardPage extends Page
{
    static url = "/bamboo/start.action"
    static at = { $("#content h1").text() == "Build Dashboard" }

    static content =
    {
    }
}