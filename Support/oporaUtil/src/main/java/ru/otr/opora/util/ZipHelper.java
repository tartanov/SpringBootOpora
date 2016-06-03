package ru.otr.opora.util;

import java.io.*;
//import java.util.Base64;
import org.zeroturnaround.zip.ByteSource;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;

import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Created by tartanovmike on 30.04.16.
 */
public class ZipHelper {
    public static void createFile(String zipFileName, String entryName, String content, String sigContent) throws Exception {
        Path filePath = Paths.get(System.getProperty("java.io.tmpdir") + "/" + zipFileName + ".zip");
        ZipEntrySource[] entries;
        if (null != sigContent) {
            entries = (ZipEntrySource[]) java.lang.reflect.Array.newInstance(ZipEntrySource.class, 2);
            entries[0] = new ByteSource(entryName, DatatypeConverter.parseBase64Binary(content));
            entries[1] = new ByteSource(entryName + ".sig", DatatypeConverter.parseBase64Binary(sigContent));

        }
        else    {
            entries = (ZipEntrySource[]) java.lang.reflect.Array.newInstance(ZipEntrySource.class, 1);
            entries[0] = new ByteSource(entryName, DatatypeConverter.parseBase64Binary(content));

        }

        packZip(filePath, entries);
    }

    public static String getFileInBase64(String zipFileName) throws IOException {
        Path filePath = Paths.get(System.getProperty("java.io.tmpdir") + "/" + zipFileName + ".zip");
        String res = DatatypeConverter.printBase64Binary(Files.readAllBytes(filePath));
        Files.delete(filePath);
        return res;
    }

    public static boolean packZip(Path zipFile, ZipEntrySource[] entries) throws Exception {
        Path path = zipFile;
        try {
            if (path.toFile().isFile() && path.toFile().canRead())
                ZipUtil.addOrReplaceEntries(zipFile.toFile(), entries);
            else
                ZipUtil.pack(entries, path.toFile());
            return true;
        } catch (Exception e) {
            Files.deleteIfExists(path);
            throw e;
        }
    }

    public static String listZipFiles(String base64Zip)  throws IOException {
        return listZipFiles(base64Zip,"CP866");
    }

    public static String listZipFiles(String base64Zip, String zipCharset)  throws IOException {
        String result = "<zipEntries>";
        InputStream is = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(base64Zip));
        Charset charset = Charset.forName(zipCharset);
        ZipInputStream zipFileIS = new ZipInputStream(is, charset);
        ZipEntry entry = zipFileIS.getNextEntry();

        while (entry != null) {
            result=result.concat("<zipEntry>"+entry.getName()+"</zipEntry>");
            zipFileIS.closeEntry();
            entry = zipFileIS.getNextEntry();
        }
        result=result.concat("</zipEntries>");
        return result;

    }


    //поскольку в архиве встречаются имена вида "1/fileName.xml" использовать regexp для извлечения файла не целесообразно,
    //извлекаем по точному имени. список имен можно получить методом listZipFiles

    public static ByteArrayOutputStream unpackZipBos(String base64Zip, String fileName, String zipCharset) throws IOException {

        String result = null;
        InputStream is = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(base64Zip));
        Charset charset = Charset.forName(zipCharset);
        ZipInputStream zipFileIS = new ZipInputStream(is, charset);
        ZipEntry entry = zipFileIS.getNextEntry();

        //ищем только первый файл, соответствующий паттерну
        while (entry != null && !entry.getName().contains(fileName)) {
            zipFileIS.closeEntry();
            entry = zipFileIS.getNextEntry();
        }
        // store the incoming data in a byte array stream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int readCount;
        // read in the data until it is all gone
        while ((readCount = zipFileIS.read(buf)) != -1) {
            bos.write(buf, 0, readCount);
        }
        return bos;
    }


    public static String unpackZipXml(String base64Zip, String filePattern) throws IOException {
        return unpackZipXml(base64Zip,filePattern,"CP866");
    }

    public static String unpackZipXml(String base64Zip, String filePattern, String zipCharset) throws IOException {
        String result = null;
        ByteArrayOutputStream bos = unpackZipBos(base64Zip,filePattern, zipCharset);
        // get xml/text
        result = new String(bos.toByteArray(), "UTF-8");
        bos.close();
        return result;
    }

    public static String unpackZipBase64(String base64Zip, String fileName) throws IOException {
        return unpackZipBase64(base64Zip,fileName,"CP866");
    }
    public static String unpackZipBase64(String base64Zip, String fileName, String zipCharset) throws IOException {
        String result = null;
        ByteArrayOutputStream bos = unpackZipBos(base64Zip,fileName, zipCharset);
        // get base64
        result = DatatypeConverter.printBase64Binary(bos.toByteArray());
        bos.close();
        return result;
    };

    public static void main(String args[]) throws Exception {
        Path filePath = FileSystems.getDefault().getPath("/home/yurii/11.xml");

        String fl= new String(Files.readAllBytes(filePath));
        listZipFiles(fl);
        System.out.print(unpackZipBase64(fl,"1\\1703544_18827_парковая 11-я ул. д.57 к.2_3_0_0.dwg"));
    };
}
