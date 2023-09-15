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

        // ====================Add method==============================
        repositoryCode += "public void create" + NameOfModel + "(";

        // Parameters of function
        repositoryCode += NameOfModel + " " + NameOfModel.toLowerCase();

        repositoryCode += ") throws SQLException {\n";
        repositoryCode += "String query = \"INSERT INTO " + NameOfTable + " (";

        // Columns
        for (int i = 1; i < NameOfAttributes.length; i++) {

            String attributeName = NameOfAttributes[i];

            repositoryCode += attributeName;
            if (i < NameOfAttributes.length - 1) {
                repositoryCode += ", ";
            }
        }

        repositoryCode += ") VALUES (";

        // ? ? ? ? ?
        for (int i = 1; i < NameOfAttributes.length; i++) {
            repositoryCode += "?";
            if (i < NameOfAttributes.length - 1) {
                repositoryCode += ", ";
            }
        }

        repositoryCode += ")\";\n";

        repositoryCode += "PreparedStatement preparedStatement = connection.prepareStatement(query);\n";

        // Set ? with values
        for (int i = 1; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            String attributeType = TypeOfAttributes[i];
            repositoryCode += "            preparedStatement.set" + BigFirstChar(attributeType) + "(" + (i + 1) + ", "
                    + NameOfModel.toLowerCase() + ".get" + BigFirstChar(attributeName) + "());\n";
        }

        repositoryCode += "            preparedStatement.executeUpdate();\n";
        repositoryCode += "    }\n\n";

        // ==============================Update method==============================
        repositoryCode += "    public void update" + NameOfModel + "(";
        repositoryCode += NameOfModel + " " + NameOfModel.toLowerCase();

        repositoryCode += ") throws SQLException {\n";
        repositoryCode += "        String query = \"UPDATE " + NameOfTable + " SET ";

        // Set columns for update
        for (int i = 1; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            repositoryCode += attributeName + "=?";
            if (i < NameOfAttributes.length - 1) {
                repositoryCode += ", ";
            }
        }

        repositoryCode += " WHERE " + NameOfAttributes[0] + "=?\";\n";
        repositoryCode += "        PreparedStatement preparedStatement = connection.prepareStatement(query);\n";

        // Set ? with values for update
        int parameterIndex = 1;
        for (int i = 1; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            String attributeType = TypeOfAttributes[i];
            repositoryCode += "        preparedStatement.set" + BigFirstChar(attributeType) + "(" + parameterIndex++
                    + ", "
                    + NameOfModel.toLowerCase() + ".get" + BigFirstChar(attributeName) + "());\n";
        }
        // Set ? for WHERE
        repositoryCode += "        preparedStatement.set" + BigFirstChar(TypeOfAttributes[0]) + "(" + parameterIndex++
                + ", "
                + NameOfModel.toLowerCase() + ".get" + BigFirstChar(NameOfAttributes[0]) + "());\n";
        repositoryCode += "        preparedStatement.executeUpdate();\n";
        repositoryCode += "    }\n\n";

        // ==============================Delete method==============================
        repositoryCode += "    public void delete" + NameOfModel + "(";
        repositoryCode += TypeOfAttributes[0] + " " + NameOfAttributes[0];
        repositoryCode += ") throws SQLException {\n";
        repositoryCode += "        String query = \"DELETE FROM " + NameOfTable + " WHERE " + NameOfAttributes[0]
                + "=?\";\n";
        repositoryCode += "        PreparedStatement preparedStatement = connection.prepareStatement(query);\n";

        // Set ? for WHERE
        repositoryCode += "        preparedStatement.set" + BigFirstChar(TypeOfAttributes[0]) + "(1, "
                + NameOfAttributes[0] + ");\n";
        repositoryCode += "        preparedStatement.executeUpdate();\n";
        repositoryCode += "    }\n\n";

        // ==============================GetAll method==============================
        repositoryCode += "    public List<" + NameOfModel + "> getAll" + NameOfModel + "s() throws SQLException {\n";
        repositoryCode += "        List<" + NameOfModel + "> " + NameOfModel.toLowerCase()
                + "List = new ArrayList<>();\n";
        repositoryCode += "        String query = \"SELECT * FROM " + NameOfTable + "\";\n";
        repositoryCode += "        PreparedStatement preparedStatement = connection.prepareStatement(query);\n";
        repositoryCode += "            ResultSet resultSet = preparedStatement.executeQuery(); \n";
        repositoryCode += "                while (resultSet.next()) {\n";
        repositoryCode += "                    " + NameOfModel + " " + NameOfModel.toLowerCase() + " = new "
                + NameOfModel + "();\n";

        // Set attributes for each record
        for (int i = 0; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            String attributeType = TypeOfAttributes[i];
            repositoryCode += "                    " + NameOfModel.toLowerCase() + ".set" + BigFirstChar(attributeName)
                    + "(resultSet.get" + BigFirstChar(attributeType) + "(\"" + attributeName + "\"));\n";
        }

        repositoryCode += " " + NameOfModel.toLowerCase() + "List.add(" + NameOfModel.toLowerCase() + ");\n";

        repositoryCode += "        }\n";
        repositoryCode += "        return " + NameOfModel.toLowerCase() + "List;\n";
        repositoryCode += "    }\n\n";

        // ==============================GetOne method==============================
        repositoryCode += "    public " + NameOfModel + " getOne" + NameOfModel + "(";
        repositoryCode += TypeOfAttributes[0] + " " + NameOfAttributes[0];
        repositoryCode += ") throws SQLException {\n";
        repositoryCode += "        " + NameOfModel + " " + NameOfModel.toLowerCase() + " = null;\n";
        repositoryCode += "        String query = \"SELECT * FROM " + NameOfTable + " WHERE " + NameOfAttributes[0]
                + "=?\";\n";
        repositoryCode += "        PreparedStatement preparedStatement = connection.prepareStatement(query);\n";
        repositoryCode += "            preparedStatement.set" + BigFirstChar(TypeOfAttributes[0]) + "(1, "
                + NameOfAttributes[0] + ");\n";
        repositoryCode += "            ResultSet resultSet = preparedStatement.executeQuery();\n";
        repositoryCode += "                if (resultSet.next()) {\n";
        repositoryCode += "                    " + NameOfModel.toLowerCase() + " = new " + NameOfModel + "();\n";

        // Set attributes for the found record
        for (int i = 0; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            String attributeType = TypeOfAttributes[i];
            repositoryCode += "                    " + NameOfModel.toLowerCase() + ".set" + BigFirstChar(attributeName)
                    + "(resultSet.get" + BigFirstChar(attributeType) + "(\"" + attributeName + "\"));\n";
        }

        repositoryCode += "        }\n";
        repositoryCode += "        return " + NameOfModel.toLowerCase() + ";\n";
        repositoryCode += "    }\n\n";

        // ============================== search methods ==============================
        for (int i = 0; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            String attributeType = TypeOfAttributes[i];

            // Search method name
            String searchMethodName = "search" + NameOfModel + "sBy" + BigFirstChar(attributeName);

            repositoryCode += "    public List<" + NameOfModel + "> " + searchMethodName + "(" + attributeType + " "
                    + attributeName + ") throws SQLException {\n";
            repositoryCode += "        List<" + NameOfModel + "> " + NameOfModel.toLowerCase()
                    + "List = new ArrayList<>();\n";
            repositoryCode += "        String query = \"SELECT * FROM " + NameOfTable + " WHERE ";

            // Like ? Or =?
            if (attributeType.equals("String")) {
                repositoryCode += attributeName + " LIKE ?";
            } else {
                repositoryCode += attributeName + "=?";
            }

            repositoryCode += "\";\n";
            repositoryCode += "        PreparedStatement preparedStatement = connection.prepareStatement(query);\n";
            repositoryCode += "            ";

            // With % or without
            if (attributeType.equals("String")) {
                repositoryCode += "preparedStatement.setString(1, \"%" + "\" + " + attributeName + " + \"%\");\n";
            } else {
                repositoryCode += "preparedStatement.set" + BigFirstChar(attributeType) + "(1, " + attributeName
                        + ");\n";
            }

            repositoryCode += "            ResultSet resultSet = preparedStatement.executeQuery();\n";
            repositoryCode += "                while (resultSet.next()) {\n";
            repositoryCode += "                    " + NameOfModel + " " + NameOfModel.toLowerCase() + " = new "
                    + NameOfModel + "();\n";

            // Set attributes for each record
            for (int j = 0; j < NameOfAttributes.length; j++) {
                String attrName = NameOfAttributes[j];
                String attrType = TypeOfAttributes[j];
                repositoryCode += "                    " + NameOfModel.toLowerCase() + ".set" + BigFirstChar(attrName)
                        + "(resultSet.get" + BigFirstChar(attrType) + "(\"" + attrName + "\"));\n";
            }

            repositoryCode += "                    " + NameOfModel.toLowerCase() + "List.add("
                    + NameOfModel.toLowerCase() + ");\n";
            repositoryCode += "                }\n";
            repositoryCode += "        return " + NameOfModel.toLowerCase() + "List;\n";
            repositoryCode += "    }\n\n";
        }

        // ==============================GetCount method==============================
        repositoryCode += "    public int getCount" + NameOfModel + "s() throws SQLException {\n";
        repositoryCode += "        int count = 0;\n";
        repositoryCode += "        String query = \"SELECT COUNT(*) AS count FROM " + NameOfTable + "\";\n";
        repositoryCode += "        PreparedStatement preparedStatement = connection.prepareStatement(query);\n";
        repositoryCode += "        ResultSet resultSet = preparedStatement.executeQuery();\n";
        repositoryCode += "                if (resultSet.next()) {\n";
        repositoryCode += "                    count = resultSet.getInt(\"count\");\n";
        repositoryCode += "                }\n";
        repositoryCode += "        return count;\n";
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
