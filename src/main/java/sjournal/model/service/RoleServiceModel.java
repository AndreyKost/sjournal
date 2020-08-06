package sjournal.model.service;

public class RoleServiceModel extends BaseServiceModel {
    private String name;

    public RoleServiceModel(String name) {
        this.name = name;
    }

    public RoleServiceModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
