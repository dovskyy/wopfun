package com.dovskyy.wopfun.service;

import com.dovskyy.wopfun.model.Group;
import com.dovskyy.wopfun.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    @Transactional(readOnly = true)
    public List<Group> getAllGroups() {
        return groupRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Group> getGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    @Transactional
    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}
