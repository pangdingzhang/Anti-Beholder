package sg.edu.smu.xposedmoduledemo.pojos;

public interface PermissionModeRequest {
    int getOpNum();

    StackTraceElement[] getStackTrace();

    int getUid();

}
