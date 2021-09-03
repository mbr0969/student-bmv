package edu.bmv.studentorder.domain.register;

public class CityRegisterResponse {
    private boolean registered;
    private boolean temporal;

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public boolean isTemporary() {
        return temporal;
    }

    public void setTemporary(boolean temporary) {
        this.temporal = temporary;
    }

    @Override
    public String toString() {
        return "CityRegisterCheckerResponse{" +
                "existing=" + registered +
                ", temporary=" + temporal +
                '}';
    }
}
