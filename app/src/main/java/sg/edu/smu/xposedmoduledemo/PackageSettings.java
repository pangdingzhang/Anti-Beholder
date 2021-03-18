package sg.edu.smu.xposedmoduledemo;

import java.io.Serializable;

public class PackageSettings implements Serializable {
    private static final long serialVersionUID = -1486163240907149677L;
    private final Integer mode;
    private final Integer op;
    private final String pkgName;
    private final Integer uid;

    public PackageSettings(int uid2, int op2, String pkgName2, int mode2) {
        this.uid = Integer.valueOf(uid2);
        this.op = Integer.valueOf(op2);
        this.pkgName = pkgName2;
        this.mode = Integer.valueOf(mode2);
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((((((this.mode == null ? 0 : this.mode.hashCode()) + 31) * 31) + (this.op == null ? 0 : this.op.hashCode())) * 31) + (this.pkgName == null ? 0 : this.pkgName.hashCode())) * 31;
        if (this.uid != null) {
            i = this.uid.hashCode();
        }
        return hashCode + i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PackageSettings other = (PackageSettings) obj;
        if (this.mode == null) {
            if (other.mode != null) {
                return false;
            }
        } else if (!this.mode.equals(other.mode)) {
            return false;
        }
        if (this.op == null) {
            if (other.op != null) {
                return false;
            }
        } else if (!this.op.equals(other.op)) {
            return false;
        }
        if (this.pkgName == null) {
            if (other.pkgName != null) {
                return false;
            }
        } else if (!this.pkgName.equals(other.pkgName)) {
            return false;
        }
        return this.uid == null ? other.uid == null : this.uid.equals(other.uid);
    }

    public Integer getUid() {
        return this.uid;
    }

    public Integer getOp() {
        return this.op;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public Integer getMode() {
        return this.mode;
    }

    public String toString() {
        return "PackageSettings [uid=" + this.uid + ", op=" + this.op + ", pkgName=" + this.pkgName + ", mode=" + this.mode + "]";
    }

}
