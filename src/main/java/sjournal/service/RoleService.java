package sjournal.service;



import sjournal.model.service.RoleServiceModel;

import java.util.Set;

public interface RoleService {

    //RoleServiceModel findByName(String name);

    Set<RoleServiceModel> findAllRoles();

    RoleServiceModel findByAuth(String authority);
}
