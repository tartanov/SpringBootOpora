package ru.otr.opora.util;

import org.junit.Before;
import org.junit.Test;
import org.zeroturnaround.zip.ZipUtil;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by tartanovmike on 14.05.16.
 */
public class ZipHelperTest {
    private String zipFileName;
    private String entryName;

    private String content;
    private String sigContent;
    private String counter;
    private Properties p;
    private Path filePath;

    @Before
    public void setUp() throws Exception {
        p = new Properties();
        InputStream fis = getClass().getClassLoader().getResourceAsStream("test.properties");
        p.load(fis);
        fis.close();
    }

    @Test
    public void testCreateFile() throws Exception {
        zipFileName = "testFileName";
        entryName = "entry";


        content = p.getProperty("content");
        //sigContent = p.getProperty("sigContent");
        sigContent = null;

        ZipHelper zipHelper = new ZipHelper();
        zipHelper.createFile(zipFileName,entryName,content,sigContent);
        filePath = Paths.get(System.getProperty("java.io.tmpdir") + "/" + zipFileName + ".zip");
        assertTrue(filePath.toFile().isFile() && filePath.toFile().canRead());

        ClassLoader classLoader = getClass().getClassLoader();
        File fileRes = new File(classLoader.getResource("testFileName.zip").getFile());
        assertTrue(ZipUtil.entryEquals(fileRes, filePath.toFile(),"entry.txt",entryName));
    }

    @Test
    public void testPackZip() throws Exception {

    }

    @Test
    public void testUnpackZip() throws Exception {

    }
}