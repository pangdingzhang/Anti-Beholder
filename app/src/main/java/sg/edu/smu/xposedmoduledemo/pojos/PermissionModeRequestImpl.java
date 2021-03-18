package sg.edu.smu.xposedmoduledemo.pojos;

import java.io.Serializable;
import java.util.Arrays;

public class PermissionModeRequestImpl implements Serializable, PermissionModeRequest {
    private static final long serialVersionUID = 7670156221995274859L;
    private final int opNum;
    private final StackTraceElement[] stackTrace;
    private final int uid;

    public PermissionModeRequestImpl(int opNum2, int uid2) {
        this.opNum = opNum2;
        this.stackTrace = null;
        this.uid = uid2;
    }

    public PermissionModeRequestImpl(int opNum2, StackTraceElement[] stackTrace2, int uid2) {
        this.opNum = opNum2;
        this.stackTrace = stackTrace2;
        this.uid = uid2;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override // org.synergylabs.pojos.PermissionModeRequest
    public int getOpNum() {
        return this.opNum;
    }

    @Override // org.synergylabs.pojos.PermissionModeRequest
    public StackTraceElement[] getStackTrace() {
        return this.stackTrace;
    }

    @Override // org.synergylabs.pojos.PermissionModeRequest
    public int getUid() {
        return this.uid;
    }

    public int hashCode() {
        return ((this.opNum + 31) * 31) + this.uid;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PermissionModeRequestImpl other = (PermissionModeRequestImpl) obj;
        if (this.opNum != other.opNum) {
            return false;
        }
        if (this.uid != other.uid) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return "PermissionModeRequestImpl [opNum=" + this.opNum + ", stackTrace=" + Arrays.toString(this.stackTrace) + ", uid=" + this.uid + "]";
    }
}