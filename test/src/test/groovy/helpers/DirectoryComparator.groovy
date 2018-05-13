package helpers

import junit.framework.AssertionFailedError

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import org.apache.commons.codec.digest.DigestUtils


/**
 * Created by Kateryna on 24.12.2017.
 */
class DirectoryComparator {

     private static void verifyDirsAreEqualOneWay(Path one, Path other) throws IOException {
        Files.walkFileTree(one, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                FileVisitResult result = super.visitFile(file, attrs);

                // get the relative file name from path "one"
                Path relativize = one.relativize(file);
                // construct the path for the counterpart file in "other"
                Path fileInOther = other.resolve(relativize);

                def oneHash = DigestUtils.md5Hex(Files.readAllBytes(file))
                def otherHash = DigestUtils.md5Hex(Files.readAllBytes(fileInOther))

                if(!oneHash.equals(otherHash)) {
                    throw new AssertionFailedError("${file} is not equal to ${fileInOther}");
                }
                return result;
            }
        });
     }
    public static void verifyDirs(Path one, Path other){
        verifyDirsAreEqualOneWay(one, other)
        verifyDirsAreEqualOneWay(other, one)
    }
}