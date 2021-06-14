package me.pete.ocarinalibrary.helper;

import android.content.Context;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.pete.ocarinalibrary.manager.FileManager;

public final class FileHelper {
    /**
     * Create a file with specified path of directory.
     *
     * @param path          Path of directory.
     * @param filename      Your filename.
     */
    public static void create(String path, String filename) {
        File file = new File(path + filename);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This function return file from path and filename
     *
     * @param path          Path of directory.
     * @param filename      Your filename.
     * @return
     */
    public static File get(String path, String filename) {
        File file = new File(path + filename);
        if(!file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * This function return get type of file
     *
     * @param file          Source file.
     * @return
     */
    public static String getType(File file) {
        return getType(file.getAbsolutePath());
    }

    /**
     * This function return get type of file
     *
     * @param filePath      Path of file location.
     * @return
     */
    public static String getType(String filePath) {
        boolean isFile = false;
        String result = "";
        for(int index = filePath.length(); index == 0; index--) {
            if(!filePath.substring(index-1, index).contentEquals(".")) {
                result = filePath.substring(index-1, index) + result;
            } else {
                isFile = true;
                break;
            }
        }

        if(!isFile) {
            result = "Folder";
        }

        return result;
    }

    /**
     * Tests whether the file or directory denoted by this abstract path of directory
     * and filename
     *
     * @param path          Path of directory.
     * @param filename      Your filename.
     * @return
     */
    public static boolean exists(String path, String filename) {
        File file = new File(path + filename);
        if(file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This function used for delete a file.
     *
     * @param path          Path of directory.
     * @param filename      Your filename.
     * @return
     */
    public static boolean delete(String path, String filename) {
        if(path != null && !path.contentEquals("") && filename != null && !filename.contentEquals("")) {
            File file = new File(path + filename);
            if (file.exists()) {
                return file.delete();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * This function used for delete a all files in a folder
     *
     * @param path          Path of directory.
     * @return
     */
    public static boolean deletes(String path) {
        File files = new File(path);
        if(files.exists()) {
            File[] yfiles = files.listFiles();
            for (File f : yfiles) {
                f.delete();
            }
        }
        return true;
    }

    /**
     * This function used for delete a all files in a folder
     *
     * @param path          Path of directory.
     * @param dayOfKeep     Value for delete old file with day.
     *                      If you set "7" for this parameter then file within past 7 days from now
     *                      will saved and other files is will be deleted.
     * @return
     */
    public static boolean deletes(String path, int dayOfKeep) {
        return deletes(path, DateTimeHelper.getDateNow(), dayOfKeep);
    }

    /**
     * This function used for delete a all files in a folder
     *
     * @param path          Path of directory.
     * @param yourDate      This parameter for set specified date for you saved file.
     * @param dayOfKeep     Value for delete old file with day.
     *                      If you set "7" for this parameter then file within past 7 days from now
     *                      will saved and other files is will be deleted.
     * @return
     */
    public static boolean deletes(String path, String yourDate, int dayOfKeep) {
        File file = new File(path);
        if(file.exists()) {
            File[] yfiles = file.listFiles();
            for(File f : yfiles) {
                Date date = new Date(f.lastModified());
                String dateModified = new SimpleDateFormat("yyyy-MM-dd").format(date);
                if(DateTimeHelper.differenceInDay(dateModified, yourDate) > dayOfKeep) {
                    f.delete();
                } else {
                    if (!f.getName().startsWith("Foto") && !f.getName().startsWith("ESKADB") && !f.getName().startsWith("GPS")) {
                        String[] fileName = f.getName().split("_");
                        String fileDate = fileName[2].replace(".zip", "");
                        fileDate = fileDate.substring(0, 4) + "-" + fileDate.substring(4, 6) + "-" + fileDate.substring(6, 8);
                        if(DateTimeHelper.differenceInDay(fileDate, yourDate) > dayOfKeep) {
                            f.delete();
                        }
                    } else if (f.getName().startsWith("Foto") || f.getName().startsWith("ESKADB") || f.getName().startsWith("GPS")) {
                        String[] fileName = f.getName().split("_");
                        String fileDate = fileName[3].replace(".zip", "");
                        fileDate = fileDate.substring(0, 4) + "-" + fileDate.substring(4, 6) + "-" + fileDate.substring(6, 8);
                        if(DateTimeHelper.differenceInDay(fileDate, yourDate) > dayOfKeep) {
                            f.delete();
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * This function used for delete files using filename with end with of name.
     *
     * @param path          Path of directory.
     * @param endWith       The suffix for end of name.
     * @return
     */
    public static boolean deletes(String path, String endWith) {
        File files = new File(path);
        if(files.exists()) {
            File[] yfiles = files.listFiles();
            for(File f : yfiles) {
                if(f.getName().endsWith(endWith)) {
                    f.delete();
                }
            }
        }
        return true;
    }

    /**
     * This function used for moves files from some folder to specified folder.
     *
     * @param pathFrom      Source directory.
     * @param pathTo        Destination directory.
     * @return
     */
    public static boolean moves(String pathFrom, String pathTo) {
        File file = new File(pathFrom);
        File[] files = file.listFiles();
        for(File f : files) {
            try {
                copy(f, new File(pathTo + f.getName()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            f.delete();
        }
        return true;
    }

    /**
     * This function used for moves files from some folder to specified folder.
     *
     * @param filePathFrom              Source file path.
     * @param destinationDirectory      Destination directory.
     * @return
     */
    public static boolean move(String filePathFrom, String destinationDirectory) {
        File file = new File(filePathFrom);
        try {
            copy(file, new File(destinationDirectory + file.getName()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        file.delete();
        return true;
    }

    /**
     * This function used for moves files from some folder to specified folder
     * and specified suffix for start of name.
     *
     * @param pathFrom      Source directory.
     * @param pathTo        Destination directory.
     * @param startWith     The suffix for start of name.
     * @return
     */
    public static boolean moves(String pathFrom, String pathTo, String startWith) {
        File file = new File(pathFrom);
        File[] files = file.listFiles();
        for(File f : files) {
            if(f.getName().startsWith(startWith)) {
                try {
                    copy(f, new File(pathTo + f.getName()));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                f.delete();
            }
        }
        return true;
    }

    /**
     * This function used for copy file to some directory.
     *
     * @param fileSource            Source file path.
     * @param fileDestination       Destination file path.
     * @return
     * @throws IOException
     */
    public static boolean copy(File fileSource, File fileDestination) throws IOException {
        if(fileSource.exists()) {
            InputStream inputStream = new FileInputStream(fileSource);
            OutputStream outputStream = new FileOutputStream(fileDestination);
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            inputStream.close();
            outputStream.close();
        }
        return true;
    }

    public static boolean duplicateWithOtherName(File fileSource, String otherName) {
        try {
            if(fileSource.exists()) {
                InputStream inputStream = new FileInputStream(fileSource);
                OutputStream outputStream = new FileOutputStream(FilenameUtils.getPath(fileSource.getPath()) + otherName);
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                inputStream.close();
                outputStream.close();
            }
        } catch (Exception e) {

        }

        return true;
    }


    public static ArrayList<File> reads(String path, String startWith) {
        ArrayList<File> files = new ArrayList<>();
        File file = new File(path);
        if(file.exists()) {
            File[] yfiles = file.listFiles();
            for(File f : yfiles) {
                if(f.getName().startsWith(startWith)) {
                    files.add(f);
                }
            }
        }
        return files;
    }

    public static ArrayList<File> reads(String path, String startWith, String endWith) {
        ArrayList<File> files = new ArrayList<>();
        File file = new File(path);
        if(file.exists()) {
            File[] yfiles = file.listFiles();
            for(File f : yfiles) {
                if(f.getName().startsWith(startWith) && f.getName().endsWith(endWith)) {
                    files.add(f);
                }
            }
        }
        return files;
    }

    public static FileManager with(Context context) {
        return new FileManager(context);
    }
}
