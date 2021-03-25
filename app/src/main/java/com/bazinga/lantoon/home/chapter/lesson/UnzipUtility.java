package com.bazinga.lantoon.home.chapter.lesson;

import android.util.Log;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UnzipUtility
{

    public void unzip(String zipFilePath, String unzipAtLocation) throws Exception {

        File archive = new File(zipFilePath);

        try {

            ZipFile zipfile = new ZipFile(archive);

            for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {

                ZipEntry entry = (ZipEntry) e.nextElement();

                unzipEntry(zipfile, entry, unzipAtLocation);
            }

        } catch (Exception e) {

            Log.e("Unzip zip", "Unzip exception", e);
        }
    }

    private void unzipEntry(ZipFile zipfile, ZipEntry entry, String outputDir) throws IOException {

        if (entry.isDirectory()) {
            createDir(new File(outputDir, entry.getName()));
            return;
        }

        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            createDir(outputFile.getParentFile());
        }

        Log.v("ZIP E", "Extracting: " + entry);

        InputStream zin = zipfile.getInputStream(entry);
        BufferedInputStream inputStream = new BufferedInputStream(zin);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        try {

            //IOUtils.copy(inputStream, outputStream);

            try {

                for (int c = inputStream.read(); c != -1; c = inputStream.read()) {
                    outputStream.write(c);
                }

            } finally {

                outputStream.close();
            }

        } finally {
            outputStream.close();
            inputStream.close();
        }
    }

    private void createDir(File dir) {

        if (dir.exists()) {
            return;
        }

        Log.v("ZIP E", "Creating dir " + dir.getName());

        if (!dir.mkdirs()) {

            throw new RuntimeException("Can not create dir " + dir);
        }
    }
}