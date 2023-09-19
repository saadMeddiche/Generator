package MainClasses;

import helpers.ViewHelper;

public class Repository {

    public static String importation(String NameOfModel) {
        String string = "package repositories;\n";
        string += "import models." + NameOfModel + ";\n";
        string += "import java.sql.Connection;\n";
        string += "import java.sql.PreparedStatement;\n";
        string += "import java.sql.ResultSet;\n";
        string += "import java.sql.SQLException;\n";
        string += "import java.util.ArrayList;\n";
        string += "import java.util.List;\n";

        return string;
    }

    public static String constructor(String NameOfRepository) {

        String string = "public " + NameOfRepository + "(Connection connection) {\n";
        string += "  this.connection = connection;\n";
        string += "}\n\n";

        return string;
    }

    public static String insert(String NameOfTable, String NameOfModel, String[] NameOfAttributes,
            String[] TypeOfAttributes) {

        String string = "";
        string += "public void create" + NameOfModel + "(";

        // Parameters of function
        string += NameOfModel + " " + NameOfModel.toLowerCase();

        string += ") throws SQLException {\n";
        string += "String query = \"INSERT INTO " + NameOfTable + " (";

        // Columns
        for (int i = 1; i < NameOfAttributes.length; i++) {

            String attributeName = NameOfAttributes[i];

            string += attributeName;
            if (i < NameOfAttributes.length - 1) {
                string += ", ";
            }
        }

        string += ") VALUES (";

        // ? ? ? ? ?
        for (int i = 1; i < NameOfAttributes.length; i++) {
            string += "?";
            if (i < NameOfAttributes.length - 1) {
                string += ", ";
            }
        }

        string += ")\";\n";

        string += "PreparedStatement preparedStatement = connection.prepareStatement(query);\n";

        // Set ? with values
        for (int i = 1; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            String attributeType = TypeOfAttributes[i];
            string += "            preparedStatement.set" + ViewHelper.BigFirstChar(attributeType) + "(" + (i + 1)
                    + ", "
                    + NameOfModel.toLowerCase() + ".get" + ViewHelper.BigFirstChar(attributeName) + "());\n";
        }

        string += "            preparedStatement.executeUpdate();\n";
        string += "    }\n\n";
        return string;
    }

    public static String update(String NameOfTable, String NameOfModel, String[] NameOfAttributes,
            String[] TypeOfAttributes) {

        String string = "";
        string += "    public void update" + NameOfModel + "(";
        string += NameOfModel + " " + NameOfModel.toLowerCase();

        string += ") throws SQLException {\n";
        string += "        String query = \"UPDATE " + NameOfTable + " SET ";

        // Set columns for update
        for (int i = 1; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            string += attributeName + "=?";
            if (i < NameOfAttributes.length - 1) {
                string += ", ";
            }
        }

        string += " WHERE " + NameOfAttributes[0] + "=?\";\n";
        string += "        PreparedStatement preparedStatement = connection.prepareStatement(query);\n";

        // Set ? with values for update
        int parameterIndex = 1;
        for (int i = 1; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            String attributeType = TypeOfAttributes[i];
            string += "        preparedStatement.set" + ViewHelper.BigFirstChar(attributeType) + "(" + parameterIndex++
                    + ", "
                    + NameOfModel.toLowerCase() + ".get" + ViewHelper.BigFirstChar(attributeName) + "());\n";
        }
        // Set ? for WHERE
        string += "        preparedStatement.set" + detectDesignatedParameter(TypeOfAttributes[0]) + "("
                + parameterIndex++
                + ", "
                + NameOfModel.toLowerCase() + ".get" + ViewHelper.BigFirstChar(NameOfAttributes[0]) + "());\n";
        string += "        preparedStatement.executeUpdate();\n";
        string += "    }\n\n";

        return string;
    }

    public static String delete(String NameOfTable, String NameOfModel, String[] NameOfAttributes,
            String[] TypeOfAttributes) {

        String string = "";

        string += "    public void delete" + NameOfModel + "(";
        string += TypeOfAttributes[0] + " " + NameOfAttributes[0];
        string += ") throws SQLException {\n";
        string += "        String query = \"DELETE FROM " + NameOfTable + " WHERE " + NameOfAttributes[0]
                + "=?\";\n";
        string += "        PreparedStatement preparedStatement = connection.prepareStatement(query);\n";

        // Set ? for WHERE
        string += "        preparedStatement.set" + detectDesignatedParameter(TypeOfAttributes[0]) + "(1, "
                + NameOfAttributes[0] + ");\n";
        string += "        preparedStatement.executeUpdate();\n";
        string += "    }\n\n";

        return string;
    }

    public static String getAll(String NameOfTable, String NameOfModel, String[] NameOfAttributes,
            String[] TypeOfAttributes) {
        String string = "";
        string += "    public List<" + NameOfModel + "> getAll" + NameOfModel + "s() throws SQLException {\n";
        string += "        List<" + NameOfModel + "> " + NameOfModel.toLowerCase()
                + "List = new ArrayList<>();\n";
        string += "        String query = \"SELECT * FROM " + NameOfTable + "\";\n";
        string += "        PreparedStatement preparedStatement = connection.prepareStatement(query);\n";
        string += "            ResultSet resultSet = preparedStatement.executeQuery(); \n";
        string += "                while (resultSet.next()) {\n";
        string += "                    " + NameOfModel + " " + NameOfModel.toLowerCase() + " = new "
                + NameOfModel + "();\n";

        // Set attributes for each record
        for (int i = 0; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            String attributeType = TypeOfAttributes[i];
            string += "                    " + NameOfModel.toLowerCase() + ".set"
                    + ViewHelper.BigFirstChar(attributeName)
                    + "(resultSet.get" + detectDesignatedParameter(attributeType) + "(\"" + attributeName + "\"));\n";
        }

        string += " " + NameOfModel.toLowerCase() + "List.add(" + NameOfModel.toLowerCase() + ");\n";

        string += "        }\n";
        string += "        return " + NameOfModel.toLowerCase() + "List;\n";
        string += "    }\n\n";

        return string;
    }

    public static String getOne(String NameOfTable, String NameOfModel, String[] NameOfAttributes,
            String[] TypeOfAttributes) {
        String string = "";

        string += "    public " + NameOfModel + " getOne" + NameOfModel + "(";
        string += TypeOfAttributes[0] + " " + NameOfAttributes[0];
        string += ") throws SQLException {\n";
        string += "        " + NameOfModel + " " + NameOfModel.toLowerCase() + " = null;\n";
        string += "        String query = \"SELECT * FROM " + NameOfTable + " WHERE " + NameOfAttributes[0]
                + "=?\";\n";
        string += "        PreparedStatement preparedStatement = connection.prepareStatement(query);\n";
        string += "            preparedStatement.set" + detectDesignatedParameter(TypeOfAttributes[0]) + "(1, "
                + NameOfAttributes[0] + ");\n";
        string += "            ResultSet resultSet = preparedStatement.executeQuery();\n";
        string += "                if (resultSet.next()) {\n";
        string += "                    " + NameOfModel.toLowerCase() + " = new " + NameOfModel + "();\n";

        // Set attributes for the found record
        for (int i = 0; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            String attributeType = TypeOfAttributes[i];
            string += "                    " + NameOfModel.toLowerCase() + ".set"
                    + ViewHelper.BigFirstChar(attributeName)
                    + "(resultSet.get" + detectDesignatedParameter(attributeType) + "(\"" + attributeName + "\"));\n";
        }

        string += "        }\n";
        string += "        return " + NameOfModel.toLowerCase() + ";\n";
        string += "    }\n\n";
        return string;
    }

    public static String getCount(String NameOfTable, String NameOfModel, String[] NameOfAttributes,
            String[] TypeOfAttributes) {
        String string = "";
        string += "    public int getCount" + NameOfModel + "s() throws SQLException {\n";
        string += "        int count = 0;\n";
        string += "        String query = \"SELECT COUNT(*) AS count FROM " + NameOfTable + "\";\n";
        string += "        PreparedStatement preparedStatement = connection.prepareStatement(query);\n";
        string += "        ResultSet resultSet = preparedStatement.executeQuery();\n";
        string += "                if (resultSet.next()) {\n";
        string += "                    count = resultSet.getInt(\"count\");\n";
        string += "                }\n";
        string += "        return count;\n";
        string += "    }\n\n";
        return string;
    }

    public static String search(String NameOfTable, String NameOfModel, String[] NameOfAttributes,
            String[] TypeOfAttributes) {

        String string = "";

        for (int i = 0; i < NameOfAttributes.length; i++) {
            String attributeName = NameOfAttributes[i];
            String attributeType = TypeOfAttributes[i];

            // Search method name
            String searchMethodName = "search" + NameOfModel + "sBy" + ViewHelper.BigFirstChar(attributeName);

            string += "    public List<" + NameOfModel + "> " + searchMethodName + "(" + attributeType + " "
                    + attributeName + ") throws SQLException {\n";
            string += "        List<" + NameOfModel + "> " + NameOfModel.toLowerCase()
                    + "List = new ArrayList<>();\n";
            string += "        String query = \"SELECT * FROM " + NameOfTable + " WHERE ";

            // Like ? Or =?
            if (attributeType.equals("String")) {
                string += attributeName + " LIKE ?";
            } else {
                string += attributeName + "=?";
            }

            string += "\";\n";
            string += "        PreparedStatement preparedStatement = connection.prepareStatement(query);\n";
            string += "            ";

            // With % or without
            if (attributeType.equals("String")) {
                string += "preparedStatement.setString(1, \"%" + "\" + " + attributeName + " + \"%\");\n";
            } else {
                string += "preparedStatement.set" + detectDesignatedParameter(attributeType) + "(1, " + attributeName
                        + ");\n";
            }

            string += "            ResultSet resultSet = preparedStatement.executeQuery();\n";
            string += "                while (resultSet.next()) {\n";
            string += "                    " + NameOfModel + " " + NameOfModel.toLowerCase() + " = new "
                    + NameOfModel + "();\n";

            // Set attributes for each record
            for (int j = 0; j < NameOfAttributes.length; j++) {
                String attrName = NameOfAttributes[j];
                String attrType = TypeOfAttributes[j];
                string += "                    " + NameOfModel.toLowerCase() + ".set"
                        + ViewHelper.BigFirstChar(attrName)
                        + "(resultSet.get" + detectDesignatedParameter(attrType) + "(\"" + attrName + "\"));\n";
            }

            string += "                    " + NameOfModel.toLowerCase() + "List.add("
                    + NameOfModel.toLowerCase() + ");\n";
            string += "                }\n";
            string += "        return " + NameOfModel.toLowerCase() + "List;\n";
            string += "    }\n\n";
        }

        return string;
    }

    public static String checkIfExist(String NameOfTable, String NameOfModel, String[] NameOfAttributes,
            String[] TypeOfAttributes) {

        String string = "";

        string += "    public boolean checkIf" + NameOfModel + "Exist(Integer id) throws SQLException {\n";
        string += "         Boolean exist = false;\n";
        string += "         String query = \"Select * FROM " + NameOfTable + " WHERE id=?\";\n";
        string += "         PreparedStatement preparedStatement = connection.prepareStatement(query);\n";
        string += "         preparedStatement.setInt(1, id);\n";
        string += "         ResultSet resultSet = preparedStatement.executeQuery();\n";
        string += "         if (resultSet.next()) { \n";
        string += "          int count = resultSet.getInt(1);\n";
        string += "          exist = count > 0;\n";
        string += "         }\n";
        string += "         return exist;";
        string += "    }\n";

        return string;

    }

    public static String detectDesignatedParameter(String type) {

        if (type.equals("Integer")) {
            return "Int";
        }

        return ViewHelper.BigFirstChar(type);
    }
}
