import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        String savegamesDir = "E:/Games/savegames/";

        GameProgress[] gameProgress = {
                new GameProgress(94, 10, 2, 254.32),
                new GameProgress(23, 0, 22, 689.99),
                new GameProgress(52, 3, 6, 159.49)};

        String[] saveFiles = new String[3];
        for (int i = 0; i < gameProgress.length; i++) {
            String filename = savegamesDir + "save" + i + ".dat";
            saveFiles[i] = filename;
            saveGame(filename, gameProgress[i]);
        }

        zipFiles("E:/Games/savegames/save.zip", saveFiles);

        deleteNotZip(savegamesDir);
    }

    static boolean saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    static boolean zipFiles(String zipPath, String[] filesPath) {
        try (ZipOutputStream zout =
                     new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (int i = 0; i < filesPath.length; i++) {
                String filename = "packed_save" + i + ".dat";
                ZipEntry entry = new ZipEntry(filename);
                zout.putNextEntry(entry);

                FileInputStream fis = new FileInputStream(filesPath[i]);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
                fis.close();
            }

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    static boolean deleteNotZip(String path) {
        return deleteNotZip(path, "E:\\Games\\savegames\\save.zip");
    }

    static boolean deleteNotZip(String path, String zipPart) {
        File dir = new File(path);
        boolean flag = true;
        try {
            for (File item : dir.listFiles()) {
                if (!item.getPath().equals(zipPart))
                    if (!item.delete())
                        flag = false;
            }
            return flag;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
