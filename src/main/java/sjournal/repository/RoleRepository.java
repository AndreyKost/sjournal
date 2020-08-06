package sjournal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sjournal.model.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    //Optional<Role> findByName(String name);

    Role findByAuthority(String authority);

}
