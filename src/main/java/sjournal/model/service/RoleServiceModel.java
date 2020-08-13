package sjournal.model.service;

public class RoleServiceModel extends BaseServiceModel {

    private String authority;

    public RoleServiceModel() {
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public RoleServiceModel(String authority) {
        this.authority = authority;
    }




}
