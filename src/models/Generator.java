package models;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Generator {

    // public static String projectPath =
    // "C:\\Users\\YouCode\\Desktop\\generate-code\\Generator\\src\\projects\\products\\";

    public static String projectPath = "C:\\Users\\Saad\\Desktop\\Generator-Code-Java\\src\\projects\\products\\";

    public static void main(String[] args) throws Exception {

        String NameOfModel = "Product";
        String[] NameOfAttributes = { "id", "name", "price" };
        String[] TypeOfAttributes = { "Integer", "String", "Double" };

        generate_CRUD(NameOfModel, NameOfAttributes, TypeOfAttributes);

    }

    public static void generate_CRUD(String NameOfModel, String[] NameOfAttributes, String[] TypeOfAttributes)
            throws Exception {
        generate_Model(NameOfModel, NameOfAttributes, TypeOfAttributes);
    }

    public static void generate_Model(String NameOfModel, String[] NameOfAttributes, String[] TypeOfAttributes)
            throws Exception {

        String ModelCode = "package models;\n";
        ModelCode += "public class " + NameOfModel + " {\n";

        for (int i = 0; i < NameOfAttributes.length; i++) {
            ModelCode += "    public " + TypeOfAttributes[i] + " " + NameOfAttributes[i] + ";\n";
        }

        for (int i = 0; i < NameOfAttributes.length; i++) {
            ModelCode += "    public " + TypeOfAttributes[i] + " get" + NameOfAttributes[i] + "() {\n";
            ModelCode += "        return " + NameOfAttributes[i] + ";\n";
            ModelCode += "    }\n";
            
            ModelCode += "    public void set" + NameOfAttributes[i] + "(" + TypeOfAttributes[i] + " "+ NameOfAttributes[i] + ") {\n";
            ModelCode += "        this." + NameOfAttributes[i] + " = " + NameOfAttributes[i] + ";\n";
            ModelCode += "    }\n";
        }

        ModelCode += "}\n";

        create_file(projectPath, "models/", NameOfModel + ".java", ModelCode);
    }

    public static void create_file(String projectPath, String folderName, String fileName, String content)
            throws Exception {
        Path pathOfFolders = Path.of(projectPath + folderName);

        File file = pathOfFolders.toFile();

        if (!file.exists()) {
            file.mkdirs();
        }

        Path fullPath = Path.of(projectPath + folderName + fileName);

        Files.writeString(fullPath, content);
    }
}
