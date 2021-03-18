package sg.edu.smu.xposedmoduledemo;

import java.io.Serializable;

public class AppopsResponse implements Serializable {
    private static final long serialVersionUID = -5367196313629369685L;
    private final int id;
    private final Serializable response;

    public AppopsResponse(Serializable response2, int id2) {
        this.response = response2;
        this.id = id2;
    }

    public Serializable getResponse() {
        return this.response;
    }

    public int getId() {
        return this.id;
    }

    public int hashCode() {
        int hashCode;
        int i = (this.id + 31) * 31;
        if (this.response == null) {
            hashCode = 0;
        } else {
            hashCode = this.response.hashCode();
        }
        return i + hashCode;
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
        AppopsResponse other = (AppopsResponse) obj;
        if (this.id != other.id) {
            return false;
        }
        return this.response == null ? other.response == null : this.response.equals(other.response);
    }

    public String toString() {
        return "AppopsResponse [response=" + this.response + ", id=" + this.id + "]";
    }
}
