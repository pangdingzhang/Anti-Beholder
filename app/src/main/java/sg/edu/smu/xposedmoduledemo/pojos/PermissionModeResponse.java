package sg.edu.smu.xposedmoduledemo.pojos;

import java.io.Serializable;

public class PermissionModeResponse implements Serializable {
    private static final long serialVersionUID = -1219875582513275898L;
    private final PermissionModeRequestCommand cmd;
    private final int pendingIntentID;
    private final int response;

    public PermissionModeResponse(int response2, int pendingIntentID2, PermissionModeRequestCommand cmd2) {
        this.response = response2;
        this.cmd = cmd2;
        this.pendingIntentID = pendingIntentID2;
    }

    public PermissionModeResponse(int response2) {
        this.response = response2;
        this.cmd = null;
        this.pendingIntentID = -1;
    }

    public PermissionModeRequestCommand getCmd() {
        return this.cmd;
    }

    public int getResponse() {
        return this.response;
    }

    public int getPendingIntentID() {
        return this.pendingIntentID;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PermissionModeResponse that = (PermissionModeResponse) o;
        if (this.response != that.response || this.pendingIntentID != that.pendingIntentID) {
            return false;
        }
        if (this.cmd != null) {
            z = this.cmd.equals(that.cmd);
        } else if (that.cmd != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((this.response * 31) + (this.cmd != null ? this.cmd.hashCode() : 0)) * 31) + this.pendingIntentID;
    }
}
