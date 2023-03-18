package com.babcock.samaritan.repository;

import com.babcock.samaritan.entity.Officer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficerRepo extends JpaRepository<Officer, String> {
    List<Officer> findByRegisteredBy_IdIgnoreCase(String id);
    boolean existsByEmailIgnoreCase(String email);
}
