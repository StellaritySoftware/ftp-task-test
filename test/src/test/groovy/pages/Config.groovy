package pages

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by Kateryna on 04.11.2017.
 */
class Config {
    static user = System.getProperty('user')
    static password = System.getProperty('password')
    static context = ""
    static String projKey
    static String planKey
    static String projName
    static String planName
    static String bambooHome = "${System.getProperty('bambooHome')}"
    static String ftpUrlDownload = "${System.getProperty('url')}//downloadDir"
    static String ftpUrlUpload = "${System.getProperty('url')}//uploadDir"
    static String ftpUser = "one"
    static String ftpPassword = "one"
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
