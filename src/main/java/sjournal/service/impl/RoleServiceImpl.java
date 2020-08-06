package sjournal.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sjournal.model.entity.Role;
import sjournal.model.service.RoleServiceModel;
import sjournal.repository.RoleRepository;
import sjournal.service.RoleService;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init(){
        if(this.roleRepository.count()==0){
            this.roleRepository.save(new Role("ADMIN"));
            this.roleRepository.save(new Role("USER"));
        }
    }

    @Override
    public Set<RoleServiceModel> findAllRoles() {
        return this.roleRepository.findAll()
                .stream()
                .map(r -> this.modelMapper.map(r, RoleServiceModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public RoleServiceModel findByAuth(String authority) {
        return this.modelMapper.map(this.roleRepository.findByAuthority(authority),RoleServiceModel.class);
    }



}
