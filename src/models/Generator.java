package models;

public class Generator {

    public String pathName = "C:\\Users\\YouCode\\Desktop\\generate-code\\Generator\\src\\projects\\";

    public static void main(String[] args) throws Exception {

        String NameOfModel = "Product";
        String[] NameOfAttributes = { "id", "name", "price" };
        String[] TypeOfAttributes = { "Integer", "String", "Double" };

        generate_CRUD(NameOfModel, NameOfAttributes, TypeOfAttributes);

    }

    public static void generate_CRUD(String NameOfModel, String[] NameOfAttributes, String[] TypeOfAttributes) {
        generate_Model(NameOfModel, NameOfAttributes, TypeOfAttributes);
    }

    public static void generate_Model(String NameOfModel, String[] NameOfAttributes, String[] TypeOfAttributes) {
        String ModelCode = "public class " + NameOfModel + " {\n";

        for (int i = 0; i < NameOfAttributes.length; i++) {
            ModelCode += "    public " + TypeOfAttributes[i] + " " + NameOfAttributes[i] + ";\n";
        }

        for (int i = 0; i < NameOfAttributes.length; i++) {
            ModelCode += "    public " + TypeOfAttributes[i] + " get" + NameOfAttributes[i] + "() {\n";
            ModelCode += "        return " + NameOfAttributes[i] + ";\n";
            ModelCode += "    }\n";
        }

        for (int i = 0; i < NameOfAttributes.length; i++) {
            ModelCode += "    public void set" + NameOfAttributes[i] + "(String " + NameOfAttributes[i] + ") {\n";
            ModelCode += "        this." + NameOfAttributes[i] + " = " + NameOfAttributes[i] + ";\n";
            ModelCode += "    }\n";
        }
        
        ModelCode += "}\n";
    }
}
