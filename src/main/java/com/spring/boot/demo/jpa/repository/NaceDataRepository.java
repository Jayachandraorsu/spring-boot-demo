package com.spring.boot.demo.jpa.repository;

import com.spring.boot.demo.jpa.entity.NaceDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaceDataRepository extends JpaRepository< NaceDataEntity,Long> {
}
