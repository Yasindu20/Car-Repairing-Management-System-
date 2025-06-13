package com.example.computer_item_repair.repositories;

import com.example.computer_item_repair.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//CRUD operations on the Database
@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    // project id
    Project findByProjectIdentifier(String projectId);

    // find all projects
    @Override
    Iterable<Project> findAll();

    Iterable<Project> findAllByProjectLeader(String username);

}
