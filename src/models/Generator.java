package models;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Generator {

    // public static String projectPath =
    // "C:\\Users\\YouCode\\Desktop\\generate-code\\Generator\\src\\projects\\products\\";

    public static String projectPath = "C:\\Users\\Saad\\Desktop\\testing-generated-code\\src\\";

    public static void main(String[] args) throws Exception {
        String NameOfModel = "Product";
        String[] NameOfAttributes = { "id", "name", "price" };
        String[] TypeOfAttributes = { "int", "String", "Double" };

        generate_crud(NameOfModel, NameOfAttributes, TypeOfAttributes);
        generate_repository(NameOfModel, NameOfAttributes, TypeOfAttributes);
    }

    public static void generate_crud(String NameOfModel, String[] NameOfAttributes, String[] TypeOfAttributes)
            throws Exception {
        generate_model(NameOfModel, NameOfAttributes, TypeOfAttributes);
    }

    public static void generate_model(String NameOfModel, String[] NameOfAttributes, String[] TypeOfAttributes)
            throws Exception {

        // Importations
        String ModelCode = "package models;\n";

        // Start
        ModelCode += "public class " + NameOfModel + " {\n";

        // Fields
        for (int i = 0; i < NameOfAttributes.length; i++) {
            ModelCode += "    public " + TypeOfAttributes[i] + " " + NameOfAttributes[i] + ";\n";
        }

        // Getters And Setters
        for (int i = 0; i < NameOfAttributes.length; i++) {
            ModelCode += "    public " + TypeOfAttributes[i] + " get" + BigFirstChar(NameOfAttributes[i]) + "() {\n";
            ModelCode += "        return " + NameOfAttributes[i] + ";\n";
            ModelCode += "    }\n";

            ModelCode += "    public void set" + BigFirstChar(NameOfAttributes[i]) + "(" + TypeOfAttributes[i] + " "
                    + NameOfAttributes[i] + ") {\n";
            ModelCode += "        this." + NameOfAttributes[i] + " = " + NameOfAttributes[i] + ";\n";
            ModelCode += "    }\n";
        }

        // End
        ModelCode += "}\n";

        create_file(projectPath, "models/", NameOfModel + ".java", ModelCode);
    }

    public static void generate_repository(String NameOfModel, String[] NameOfAttributes, String[] TypeOfAttributes)
            throws Exception {

        String NameOfRepository = NameOfModel + "Repository";
        String NameOfTable = NameOfModel.toLowerCase() + "s";

        // Importations
        String repositoryCode = "package repositories;\n";
        repositoryCode += "import models." + NameOfModel + ";\n";
        repositoryCode += "import java.sql.Connection;\n";
        repositoryCode += "import java.sql.PreparedStatement;\n";
        repositoryCode += "import java.sql.ResultSet;\n";
        repositoryCode += "import java.sql.SQLException;\n";
        repositoryCode += "import java.util.ArrayList;\n";
        repositoryCode += "import java.util.List;\n";

        // Start
        repositoryCode += "public class " + NameOfRepository + " {\n";

        // Fileds
        repositoryCode += "private Connection connection;\n\n";

        // Constructor
        repositoryCode += "public " + NameOfRepository + "(Connection connection) {\n";
        repositoryCode += "  this.connection = connection;\n";
        repositoryCode += "}\n\n";

        // Add method
        repositoryCode += "public void create" + NameOfModel + "(";

        // Parameters of function
        repositoryCode += NameOfModel + " " + NameOfModel.toLowerCase();

        repositoryCode += ") throws SQLException {\n";
        repositoryCode += "String query = \"INSERT INTO " + NameOfTable + " (";

        // Columns
        for (int i = 0; i < NameOfAttributes.length; i++) {

            String attributeName = NameOfAttributes[i];

            repositoryCode += attributeName;
            if (i < NameOfAttributes.length - 1) {
                repositoryCode += ", ";
            }
        }

        repositoryCode += ") VALUES (";

        // ? ? ? ? ?
        for (int i = 0; i < NameOfAttributes.length; i++) {
            repositoryCode += "?";
            if (i < NameOfAttributes.length - 1) {
                repositoryCode += ", ";
            }
        }

        repositoryCode += ")\";\n";

        repositoryCode += "PreparedStatement preparedStatement = connection.prepareStatement(query);\n";

        for (int i = 0; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            String attributeType = TypeOfAttributes[i];
            repositoryCode += "            preparedStatement.set" + BigFirstChar(attributeType) + "(" + (i + 1) + ", "
                    + NameOfModel.toLowerCase() + ".get" + BigFirstChar(attributeName) + "());\n";
        }

        repositoryCode += "            preparedStatement.executeUpdate();\n";
        repositoryCode += "    }\n\n";

        // End
        repositoryCode += "}\n";

        create_file(projectPath, "repositories/", NameOfRepository + ".java", repositoryCode);
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

    public static String BigFirstChar(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
