package MainClasses;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import helpers.ViewHelper;

public class Generator {

    // public static String projectPath =
    public static String projectPath = "C:\\Users\\YouCode\\Desktop\\test-generator-code\\src\\";

    // public static String projectPath =
    // "C:\\Users\\Saad\\Desktop\\testing-generated-code\\src\\";

    public static void main(String[] args) throws Exception {
        String NameOfModel = "User";
        String[] NameOfAttributes = { "id", "name", "email" };
        String[] TypeOfAttributes = { "int", "String", "String" };

        generate_crud(NameOfModel, NameOfAttributes, TypeOfAttributes);
        generate_repository(NameOfModel, NameOfAttributes, TypeOfAttributes);
        generate_service(NameOfModel, NameOfAttributes, TypeOfAttributes);
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
        ModelCode += Models.fields(NameOfAttributes, TypeOfAttributes);

        // Getters And Setters
        ModelCode += Models.gettersAndSetters(NameOfAttributes, TypeOfAttributes);

        // End
        ModelCode += "}\n";

        create_file(projectPath, "models/", NameOfModel + ".java", ModelCode);
    }

    public static void generate_repository(String NameOfModel, String[] NameOfAttributes, String[] TypeOfAttributes)
            throws Exception {

        String NameOfRepository = NameOfModel + "Repository";
        String NameOfTable = NameOfModel.toLowerCase() + "s";

        // Importations
        String repositoryCode = Repository.importation(NameOfModel);

        // Start
        repositoryCode += "public class " + NameOfRepository + " {\n";

        // Fileds
        repositoryCode += "private Connection connection;\n\n";

        // Constructor
        repositoryCode += Repository.constructor(NameOfRepository);

        // ====================Add method==============================
        repositoryCode += Repository.insert(NameOfTable, NameOfModel, NameOfAttributes, TypeOfAttributes);

        // ==============================Update method==============================
        repositoryCode += Repository.update(NameOfTable, NameOfModel, NameOfAttributes, TypeOfAttributes);

        // ==============================Delete method==============================
        repositoryCode += Repository.delete(NameOfTable, NameOfModel, NameOfAttributes, TypeOfAttributes);

        // ==============================GetAll method==============================
        repositoryCode += Repository.getAll(NameOfTable, NameOfModel, NameOfAttributes, TypeOfAttributes);

        // ==============================GetOne method==============================
        repositoryCode += Repository.getOne(NameOfTable, NameOfModel, NameOfAttributes, TypeOfAttributes);

        // ============================== search methods ==============================
        repositoryCode += Repository.search(NameOfTable, NameOfModel, NameOfAttributes, TypeOfAttributes);

        // ==============================GetCount method==============================
        repositoryCode += Repository.getCount(NameOfTable, NameOfModel, NameOfAttributes, TypeOfAttributes);

        // End
        repositoryCode += "}\n";

        create_file(projectPath, "repositories/", NameOfRepository + ".java", repositoryCode);
    }

    public static void generate_service(String NameOfModel, String[] NameOfAttributes, String[] TypeOfAttributes)
            throws Exception {

        String NameOfService = NameOfModel + "Service";

        // Importations
        String serviceCode = Service.importation(NameOfModel);

        // Start
        serviceCode += "public class " + NameOfService + " {\n";

        // Fields
        serviceCode += "    private " + NameOfModel + "Repository " + NameOfModel.toLowerCase() + "Repository;\n\n";

        // Constructor
        serviceCode += Service.constructor(NameOfService, NameOfModel);

        // Create method
        serviceCode += Service.insert(NameOfModel);

        // Update method
        serviceCode += Service.update(NameOfModel);

        // Delete method
        serviceCode += Service.delete(NameOfModel, NameOfAttributes, TypeOfAttributes);

        // Search methods for each attribute
        serviceCode += Service.search(NameOfModel, NameOfAttributes, TypeOfAttributes);

        // GetAll method
        serviceCode += Service.getAll(NameOfModel);

        // GetOne method
        serviceCode += Service.getOne(NameOfModel, NameOfAttributes, TypeOfAttributes);

        // Count method
        serviceCode += Service.getCount(NameOfModel);

        // End
        serviceCode += "}\n";

        create_file(projectPath, "services/", NameOfService + ".java", serviceCode);
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
