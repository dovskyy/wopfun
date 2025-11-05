package com.dovskyy.wopfun.controller;

import com.dovskyy.wopfun.model.Group;
import com.dovskyy.wopfun.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public String listGroups(Model model) {
        List<Group> groups = groupService.getAllGroups();
        model.addAttribute("groups", groups);
        return "groups/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("group", new Group());
        return "groups/form";
    }

    @PostMapping
    public String createGroup(@ModelAttribute Group group) {
        groupService.saveGroup(group);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Group group = groupService.getGroupById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono grupy o ID: " + id));
        model.addAttribute("group", group);
        return "groups/form";
    }

    @PostMapping("/{id}")
    public String updateGroup(@PathVariable Long id, @ModelAttribute Group group) {
        Group existingGroup = groupService.getGroupById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono grupy o ID: " + id));

        existingGroup.setName(group.getName());
        existingGroup.setDescription(group.getDescription());

        groupService.saveGroup(existingGroup);
        return "redirect:/groups";
    }

    @PostMapping("/{id}/delete")
    public String deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return "redirect:/groups";
    }
}
