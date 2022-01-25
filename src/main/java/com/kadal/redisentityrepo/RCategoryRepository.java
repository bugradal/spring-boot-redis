package com.kadal.redisentityrepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RCategoryRepository extends JpaRepository<RCategory,String> {

    Optional<RCategory> findByCid(Integer id);
}
