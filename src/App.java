import java.nio.file.Files;
import java.util.Scanner;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        String pathName = "C:\\Users\\YouCode\\Desktop\\generate-code\\Generator\\src\\projects\\";

        System.out.println("Enter The Text :");
        String content = scanner.nextLine();
        System.out.println();

        System.out.println("Enter The Name Of File :");
        String fileName = scanner.nextLine();
        System.out.println();

        scanner.close();

        Path path = Path.of(pathName + fileName);

        Files.writeString(path, content);
    }

}
