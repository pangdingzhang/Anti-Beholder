package sg.edu.smu.xposedmoduledemo.pojos;

public class ButtonPermissionBean {
    private String package_name;
    private String class_name;
    private String button_id;
    private String permission;
    private String decision;

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getButton_id() {
        return button_id;
    }

    public void setButton_id(String button_id) {
        this.button_id = button_id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
