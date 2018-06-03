package pages

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by Kateryna on 04.11.2017.
 */
class Config {
    static user = System.getProperty('user')
    static password = System.getProperty('password')
    static context = "/bamboo"
    static String projKey
    static String planKey
    static String projName
    static String planName
    static String bambooHome = "${System.env.BAMBOO_HOME}"
    static String ftpUrlDownload = "${System.env.FTP_URL}/pub/downloadDir"
    static String ftpUrlUpload = "${System.env.FTP_URL}/pub/uploadDir"
    static String ftpUser = "user1"
    static String ftpPassword = "pass1"
    static String ftpInvalidPassword = "katya"
    static String subdirectory = "ftpFolder"
    static URI ftpSample = getClass().getResource('/ftpSample').toURI()
    static URI ftpSampleInclude = getClass().getResource('/ftpSampleInclude').toURI()
    static URI ftpSampleExclude = getClass().getResource('/ftpSampleExclude').toURI()
    static URI ftpSampleIncludeExclude = getClass().getResource('/ftpSampleIncludeExclude').toURI()
    static URI ftpSampleUploadWithoutClearingFolder = getClass().getResource('/ftpSampleUploadWithoutClearingFolder').toURI()

    static Path getBuildDir(){
        return Paths.get(bambooHome, "xml-data", "build-dir", "${projKey}-${planKey}-JOB1")
    }

    static Path getSubdirectoryBuildDir(){
        return Paths.get(bambooHome, "xml-data", "build-dir", "${projKey}-${planKey}-JOB1", subdirectory)
    }
}
