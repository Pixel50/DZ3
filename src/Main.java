import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Main {

    public static void main(String[] args) {
        String zipFilePath = "/Users/user/Documents/Games/savegames/zip.zip";
        String unzipDirectory = "/Users/user/Documents/Games/savegames";
        openZip(zipFilePath, unzipDirectory);

        String saveFilePath = unzipDirectory + "/save1.dat";
        GameProgress gameProgress = openProgress(saveFilePath);

        //  состояние
        if (gameProgress != null) {
            System.out.println(gameProgress);
        }
    }



    public static void openZip(String zipFilePath, String outputFolder) {
        try (FileInputStream fis = new FileInputStream(zipFilePath);
             ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String filePath = outputFolder + File.separator + entry.getName();
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zis.read(buffer)) >= 0) {
                        fos.write(buffer, 0, length);
                    }
                    System.out.println("File extracted: " + filePath);
                }
                zis.closeEntry();
            }
            System.out.println("Unzipping completed!");
        } catch (IOException e) {
            System.out.println("Failed to unzip file: " + zipFilePath);
            e.printStackTrace();
        }
    }


    public static GameProgress openProgress(String filePath) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            gameProgress = (GameProgress) ois.readObject();
            System.out.println("Game progress loaded from " + filePath);
            System.out.println(gameProgress);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load game progress from " + filePath);
            e.printStackTrace();
        }
        return gameProgress;
    }


    }