package com.example.computer_item_repair.repositories;

import com.example.computer_item_repair.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsername(String username);
    User getById(Long id);

}
