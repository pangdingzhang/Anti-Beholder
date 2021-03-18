package sg.edu.smu.xposedmoduledemo;

import java.io.Serializable;
import sg.edu.smu.xposedmoduledemo.RequestType;

public class AppopsRequest implements Serializable {
    private static final long serialVersionUID = -2959812570329587233L;
    private final int mode;
    private final int op;
    private final String packageName;
    private final int reqId;
    private final RequestType type;
    private final int uid;

    private AppopsRequest(RequestType type2, int reqId2, int op2, int uid2, String packageName2, int mode2) {
        this.type = type2;
        this.uid = uid2;
        this.reqId = reqId2;
        this.op = op2;
        this.packageName = packageName2;
        this.mode = mode2;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static AppopsRequest getNewSetReq(int num, int op2, int uid2, String packageName2, int mode2) {
        return new AppopsRequest(RequestType.SET, num, op2, uid2, packageName2, mode2);
    }

    public static AppopsRequest getNewGetReq(int num, int op2, int uid2, String packageName2) {
        return new AppopsRequest(RequestType.GET, num, op2, uid2, packageName2, -1);
    }

    public static AppopsRequest getNewGetAllReq(int num) {
        return new AppopsRequest(RequestType.GETALL, num, -1, -1, "", -1);
    }

    public static AppopsRequest getNewGetFakesReq(int num) {
        return new AppopsRequest(RequestType.GETALL, num, -1, -1, "", -1);
    }

    public RequestType getType() {
        return this.type;
    }

    public int getReqId() {
        return this.reqId;
    }

    public int getUid() {
        return this.uid;
    }

    public int getOp() {
        return this.op;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public int getMode() {
        return this.mode;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int i2 = (((((this.mode + 31) * 31) + this.reqId) * 31) + this.op) * 31;
        if (this.packageName == null) {
            hashCode = 0;
        } else {
            hashCode = this.packageName.hashCode();
        }
        int i3 = (i2 + hashCode) * 31;
        if (this.type != null) {
            i = this.type.hashCode();
        }
        return ((i3 + i) * 31) + this.uid;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AppopsRequest other = (AppopsRequest) obj;
        if (this.mode != other.mode || this.reqId != other.reqId || this.op != other.op) {
            return false;
        }
        if (this.packageName == null) {
            if (other.packageName != null) {
                return false;
            }
        } else if (!this.packageName.equals(other.packageName)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.uid != other.uid) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return "AppopsRequest [type=" + this.type + ", num=" + this.reqId + ", uid=" + this.uid + ", op=" + this.op + ", packageName=" + this.packageName + ", mode=" + this.mode + "]";
    }
}
