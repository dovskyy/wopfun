package com.dovskyy.wopfun.service;

import com.dovskyy.wopfun.model.Child;
import com.dovskyy.wopfun.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;

    @Transactional(readOnly = true)
    public List<Child> getAllChildren() {
        return childRepository.findAllByOrderByLastNameAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Child> getChildById(Long id) {
        return childRepository.findById(id);
    }

    @Transactional
    public Child saveChild(Child child) {
        return childRepository.save(child);
    }

    @Transactional
    public void deleteChild(Long id) {
        childRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Child> searchByLastName(String lastName) {
        return childRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    @Transactional(readOnly = true)
    public List<Child> getChildrenByGroup(String groupName) {
        return childRepository.findByGroupName(groupName);
    }
}
