package com.babcock.samaritan.repository;

import com.babcock.samaritan.entity.StudentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentItemRepo extends JpaRepository<StudentItem, Long> {
    List<StudentItem> findByOwnedBy_Id(String id);
}
