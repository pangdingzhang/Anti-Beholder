package sg.edu.smu.xposedmoduledemo.pojos;

import java.util.Arrays;

public class PermissionModeRequestCommand {
    private final String appRequestingPackageName;
    private final String libRequestingName;
    private final int op;
    private final String permissionRequestingPackageName;
    private final String[] stacktrace;
    private final int uid;

    public PermissionModeRequestCommand(String permissionRequestingPackageName2, String appRequestingPackageName2, String libRequestingName2, int op2, int uid2, String[] s) {
        this.permissionRequestingPackageName = permissionRequestingPackageName2;
        this.appRequestingPackageName = appRequestingPackageName2;
        this.libRequestingName = libRequestingName2;
        this.op = op2;
        this.uid = uid2;
        this.stacktrace = s;
    }

    public String getPermissionRequestingPackageKey() {
        return this.permissionRequestingPackageName;
    }

    public String getAppRequestingPackageKey() {
        return this.appRequestingPackageName;
    }

    public int getOp() {
        return this.op;
    }

    public String[] getStacktrace() {
        return this.stacktrace;
    }

    public int getUid() {
        return this.uid;
    }

    public String getLibRequestingName() {
        return this.libRequestingName;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PermissionModeRequestCommand that = (PermissionModeRequestCommand) o;
        if (this.op != that.op || this.uid != that.uid) {
            return false;
        }
        if (this.permissionRequestingPackageName != null) {
            if (!this.permissionRequestingPackageName.equals(that.permissionRequestingPackageName)) {
                return false;
            }
        } else if (that.permissionRequestingPackageName != null) {
            return false;
        }
        if (this.appRequestingPackageName != null) {
            if (!this.appRequestingPackageName.equals(that.appRequestingPackageName)) {
                return false;
            }
        } else if (that.appRequestingPackageName != null) {
            return false;
        }
        return Arrays.equals(this.stacktrace, that.stacktrace);
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.permissionRequestingPackageName != null) {
            result = this.permissionRequestingPackageName.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.appRequestingPackageName != null) {
            i = this.appRequestingPackageName.hashCode();
        }
        return ((((((i2 + i) * 31) + this.op) * 31) + this.uid) * 31) + Arrays.hashCode(this.stacktrace);
    }

    public String toString() {
        return "PermissionModeRequestCommand{permissionRequestingPackageName='" + this.permissionRequestingPackageName + '\'' + ", appRequestingPackageName='" + this.appRequestingPackageName + '\'' + ", op=" + this.op + ", uid=" + this.uid + ", stacktrace=" + Arrays.toString(this.stacktrace) + '}';
    }
}
