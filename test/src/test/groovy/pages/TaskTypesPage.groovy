package pages

import geb.Page

/**
 * Created by Kateryna on 03.12.2017.
 */
class TaskTypesPage extends Page{

    static url = "/bamboo/build/admin/edit/addTask.action"
    static at = { $("#task-types-dialog h2.dialog-title").text() == "Task types"}

    static content =
    {
       ftpDownload{ $("div#task-types-dialog a", 0, title:"FTP Download") }
       ftpUpload{ $("div#task-types-dialog a", 0, title:"FTP Upload") }
    }

    def selectFtpDownload(){
        ftpDownload.click()
        browser.at FTPDownloadConfigurationPage
    }

    def selectFtpUpload(){
        ftpUpload.click()
        browser.at FTPUploadConfigurationPage
    }
}
