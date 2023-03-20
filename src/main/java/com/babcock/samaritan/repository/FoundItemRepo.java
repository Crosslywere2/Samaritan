package com.babcock.samaritan.repository;

import com.babcock.samaritan.entity.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoundItemRepo extends JpaRepository<FoundItem, Long> {
    List<FoundItem> findByClaimedByNull();
}
