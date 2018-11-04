package pages

import configuration.CommonConfig

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by Kateryna on 04.11.2017.
 */
class Config {
    static String ftpUrlDownload = "${System.env.FTP_URL}/pub/downloadDir"
    static String ftpUrlUpload = "${System.env.FTP_URL}/pub/uploadDir"
    static String ftpUser = "user1"
    static String ftpPassword = "pass1"
    static String ftpInvalidUser = "katya"
    static String ftpInvalidPassword = "katya"
    static String subdirectory = "ftpFolder"
    static URI ftpSample = getClass().getResource('/ftpSample').toURI()
    static URI ftpSampleInclude = getClass().getResource('/ftpSampleInclude').toURI()
    static URI ftpSampleExclude = getClass().getResource('/ftpSampleExclude').toURI()
    static URI ftpSampleIncludeExclude = getClass().getResource('/ftpSampleIncludeExclude').toURI()

    static Path getSubdirectoryBuildDir(){
        return Paths.get(CommonConfig.bambooHome, "xml-data", "build-dir", "${CommonConfig.projKey}-${CommonConfig.planKey}-JOB1", subdirectory)
    }
}
