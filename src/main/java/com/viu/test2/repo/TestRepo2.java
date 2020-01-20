package com.viu.test2.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viu.test2.entity.TestEntity2;

@Repository
public interface TestRepo2 extends JpaRepository<TestEntity2, Long>{

}
