package com.dovskyy.wopfun.repository;

import com.dovskyy.wopfun.model.Child;
import com.dovskyy.wopfun.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    List<Child> findByLastNameContainingIgnoreCase(String lastName);

    List<Child> findByGroup(Group group);

    List<Child> findAllByOrderByLastNameAsc();
}
