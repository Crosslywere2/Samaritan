package com.babcock.samaritan.repository;

import com.babcock.samaritan.entity.StudentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentItemRepo extends JpaRepository<StudentItem, Long> {
}
