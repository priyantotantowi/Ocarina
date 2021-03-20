package me.pete.ocarinalibrary.helper;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by Priyanto Tantowi.
 *
 * ZipHelper is helper for help your case in zip.
 */
public abstract class ZipHelper {
    private static final int BUFFER = 1024;
    public static String errorMessage = "";

    /**
     * This function returns file of zip.
     *
     * @param inFolder  File source to zip, exactly your file is path folder
     * @param outFile   File destination for your zip
     * @return
     */
    public static File zipping(File inFolder, File outFile) {
        try {
            String[] files = inFolder.list();
            byte[] data = new byte[BUFFER];
            ZipOutputStream out = null;
            if (files.length > 0){
                // zipped file out stream
                out = new ZipOutputStream(new BufferedOutputStream(
                        new FileOutputStream(outFile)));

                // loop through each
                for (int i = 0; i < files.length; i++) {
                    System.out.println("Adding file : " + files[i]);

                    // get the input stream of the file
                    BufferedInputStream in = new BufferedInputStream(
                            new FileInputStream(inFolder.getPath() + "/" + files[i]),
                            BUFFER);

                    // add the file header info (name, size) etc. to the zip stream
                    out.putNextEntry(new ZipEntry(files[i]));
                    int count;

                    // read the write input file to zip stream
                    while ((count = in.read(data, 0, BUFFER)) != -1) {

                        out.write(data, 0, count);
                    }
                    out.closeEntry(); // close entry

                    cleanUp(in); // close input stream
                } // move to next file
                cleanUp(out); // close zip stream
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return te handle to the output file
        return outFile;
    }

    /**
     * This function returns file of zip with specified filename.
     *
     * @param inFolder      File source to zip, exactly your file is path folder
     * @param outFile       File destination for your zip
     * @param fileNames     Specified your filename you want to zip
     * @return
     */
    public static File zippingSpecifiedFiles(File inFolder, File outFile, String[] fileNames) {
        try {
            String[] files = inFolder.list();
            byte[] data = new byte[BUFFER];
            ZipOutputStream out = null;
            if (files.length > 0){
                // zipped file out stream
                out = new ZipOutputStream(new BufferedOutputStream(
                        new FileOutputStream(outFile)));

                // loop through each
                for (int i = 0; i < files.length; i++) {
                    if (!ArrayHelper.exists(fileNames, files[i])){
                        System.out.println("Adding file : " + files[i]);

                        // get the input stream of the file
                        BufferedInputStream in = new BufferedInputStream(
                                new FileInputStream(inFolder.getPath() + "/" + files[i]),
                                BUFFER);

                        // add the file header info (name, size) etc. to the zip stream
                        out.putNextEntry(new ZipEntry(files[i]));
                        int count;

                        // read the write input file to zip stream
                        while ((count = in.read(data, 0, BUFFER)) != -1) {

                            out.write(data, 0, count);
                        }
                        out.closeEntry(); // close entry

                        cleanUp(in); // close input stream
                    }
                } // move to next file
                cleanUp(out); // close zip stream
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return te handle to the output file
        return outFile;
    }

    /**
     * This function returns file of zip with specified filename on start and end file name.
     *
     * @param inFolder          File source to zip, exactly your file is path folder
     * @param outFile           File destination for your zip
     * @param startFileName     Specified name start with.
     * @param endFileName       Specified name end with.
     * @return
     */
    public static File zipping(File inFolder, File outFile, String startFileName, String endFileName) {
        try {
            String[] files = inFolder.list();
            byte[] data = new byte[BUFFER];
            ZipOutputStream out = null;
            if (files.length > 0 ){
                // zipped file out stream
                out = new ZipOutputStream(new BufferedOutputStream(
                        new FileOutputStream(outFile)));

                // loop through each
                for (int i = 0; i < files.length; i++) {
                    if(!files[i].startsWith(startFileName) && !files[i].endsWith(endFileName)) {
                        continue;
                    } else if(!files[i].endsWith(endFileName)) {
                        continue;
                    }

                    System.out.println("Adding file : " + files[i]);

                    // get the input stream of the file
                    BufferedInputStream in = new BufferedInputStream(
                            new FileInputStream(inFolder.getPath() + "/" + files[i]),
                            BUFFER);

                    // add the file header info (name, size) etc. to the zip stream
                    out.putNextEntry(new ZipEntry(files[i]));
                    int count;

                    // read the write input file to zip stream
                    while ((count = in.read(data, 0, BUFFER)) != -1) {

                        out.write(data, 0, count);
                    }
                    out.closeEntry(); // close entry

                    cleanUp(in); // close input stream
                } // move to next file
                cleanUp(out); // close zip stream
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return te handle to the output file
        return outFile;
    }

    /**
     * This function returns boolean value of success unzipping file.
     * True if system can unzipping file.
     *
     * @param zipFile       File source to zip, exactly your file is path folder
     * @param extractTo     File destination for unzipping. File exactly path of directiory.
     * @return
     */
    public static boolean unzipping(File zipFile, String extractTo) {
        boolean result = false;
        try {
            ZipFile archive = new ZipFile(zipFile);
            Enumeration<? extends ZipEntry> e = archive.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                File file = new File(extractTo, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    InputStream in = archive.getInputStream(entry);
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                    IOUtils.copy(in, out);
                    in.close();
                    out.close();
                }
            }
            result = true;
        } catch (IOException e) {
            Log.e("unzipping", e.toString());
            errorMessage = e.toString();
            result = false;
        } finally {
            zipFile.delete();
        }
        return result;
    }

    private static void cleanUp(InputStream in) throws Exception {
        in.close();
    }

    private static void cleanUp(OutputStream out) throws Exception {
        out.flush();
        out.close();
    }

    /**
     * The function returns true if file is a zip file.
     *
     * @param file  Your file to checking.
     * @return
     */
    public static boolean isValid(final File file) {
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(file);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (zipfile != null) {
                    zipfile.close();
                    zipfile = null;
                }
            } catch (IOException e) {
            }
        }
    }
}
