import java.nio.file.Files;
import java.util.Scanner;

import helpers.viewHelper;

import java.nio.file.Path;
import java.io.File;

public class App {
    public static void main(String[] args) throws Exception {

        viewHelper.clearConsole();

        Scanner scanner = new Scanner(System.in);

        String pathName = "C:\\Users\\YouCode\\Desktop\\generate-code\\Generator\\src\\projects\\";

        viewHelper.colorText("Enter The Text :", "red");
        String content = scanner.nextLine();
        System.out.println();

        viewHelper.colorText("Enter The Folder Path :", "green");
        String folderName = scanner.nextLine() + "/";
        System.out.println();

        viewHelper.colorText("Enter The File Name :", "yellow");
        String fileName = scanner.nextLine();
        System.out.println();

        scanner.close();

        Path pathFolders = Path.of(pathName + folderName);

        File file = pathFolders.toFile();

        if (!file.exists()) {
            file.mkdirs();
        }

        Path fullPath = Path.of(pathName + folderName + fileName);

        Files.writeString(fullPath, content);
    }

}
