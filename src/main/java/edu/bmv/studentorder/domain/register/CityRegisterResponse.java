package edu.bmv.studentorder.domain.register;

public class CityRegisterResponse {
    private boolean existing;
    private Boolean temporary ;

    public boolean isExisting() {
        return existing;
    }

    public void setExisting(boolean existing) {
        this.existing = existing;
    }

    public Boolean getTemporary() {
        return temporary;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    @Override
    public String toString() {
        return "CityRegisterCheckerResponse{" +
                "existing=" + existing +
                ", temporary=" + temporary +
                '}';
    }
}
