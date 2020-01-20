package com.viu.test1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viu.test1.entity.TestEntity1;

@Repository
public interface TestRepo1 extends JpaRepository<TestEntity1, Long>{

}
