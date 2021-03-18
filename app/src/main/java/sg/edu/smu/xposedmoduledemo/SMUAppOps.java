package sg.edu.smu.xposedmoduledemo;

import android.app.AppOpsManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class SMUAppOps {
    static final int PMP_PORT = 60288;
    private AppopsManagerCreatingStub manager;
    private ServerSocket srvr;

    public SMUAppOps(Object appOpsManager) {
        this.manager = new AppopsManagerCreatingStub(appOpsManager);

    }

    public void run() {
        try {
            this.srvr = new ServerSocket(PMP_PORT);
            while (true) {
                Socket sock = this.srvr.accept();
                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
                while (true) {
                    try {
                        AppopsRequest req = (AppopsRequest) in.readObject();
                        if (req == null) {
                            break;
                        }
                        Serializable response = dealWithReq(req);
                        if (response != null) {
                            out.writeObject(new AppopsResponse(response, req.getReqId()));
                        }
                    } catch (Exception e) {
                        if (sock != null) {
                            sock.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (Throwable th) {
                        if (sock != null) {
                            sock.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                        throw th;
                    }
                }
                if (sock != null) {
                    sock.close();
                }
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e2) {
        }
    }

    private Serializable dealWithReq(AppopsRequest req) {
        try {
            switch (req.getType()) {
                case GET:
                    return this.manager.unsafeCheckOp(req.getOp(), req.getUid(), req.getPackageName());
                case SET:
                    this.manager.setMode(req.getOp(), req.getUid(), req.getPackageName(), req.getMode());
                    return 0;
                case GETALL:
                    return this.manager.getAllPackages();
            }
        } catch (Exception e) {
        }
        return null;
    }

}
