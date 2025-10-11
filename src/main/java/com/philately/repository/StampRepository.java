package com.philately.repository;

import com.philately.model.entity.Stamp;
import com.philately.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StampRepository extends JpaRepository<Stamp, UUID> {

    List<Stamp> findAllByOwner(User owner);
    void deleteById(UUID id);

}
