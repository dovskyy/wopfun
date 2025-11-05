package com.dovskyy.wopfun.controller;

import com.dovskyy.wopfun.model.Child;
import com.dovskyy.wopfun.model.ChildNote;
import com.dovskyy.wopfun.model.Group;
import com.dovskyy.wopfun.service.ChildNoteService;
import com.dovskyy.wopfun.service.ChildService;
import com.dovskyy.wopfun.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/children")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;
    private final ChildNoteService childNoteService;
    private final GroupService groupService;

    @GetMapping
    public String selectGroup(Model model) {
        List<Group> groups = groupService.getAllGroups();
        model.addAttribute("groups", groups);
        return "children/select-group";
    }

    @GetMapping("/group/{groupId}")
    public String listChildrenByGroup(@PathVariable Long groupId, Model model) {
        Group group = groupService.getGroupById(groupId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono grupy o ID: " + groupId));
        List<Child> children = childService.getChildrenByGroup(group);
        model.addAttribute("children", children);
        model.addAttribute("group", group);
        return "children/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("child", new Child());
        model.addAttribute("groups", groupService.getAllGroups());
        return "children/form";
    }

    @PostMapping
    public String createChild(@ModelAttribute Child child, @RequestParam(required = false) Long groupId) {
        if (groupId != null) {
            Group group = groupService.getGroupById(groupId)
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono grupy o ID: " + groupId));
            child.setGroup(group);
        }
        childService.saveChild(child);
        return "redirect:/children";
    }

    @GetMapping("/{id}")
    public String viewChild(@PathVariable Long id, Model model) {
        Child child = childService.getChildById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono dziecka o ID: " + id));
        List<ChildNote> notes = childNoteService.getNotesByChildId(id);

        model.addAttribute("child", child);
        model.addAttribute("notes", notes);
        return "children/view";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Child child = childService.getChildById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono dziecka o ID: " + id));
        model.addAttribute("child", child);
        model.addAttribute("groups", groupService.getAllGroups());
        return "children/form";
    }

    @PostMapping("/{id}")
    public String updateChild(@PathVariable Long id, @ModelAttribute Child child, @RequestParam(required = false) Long groupId) {
        // Pobierz istniejące dziecko z bazy
        Child existingChild = childService.getChildById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono dziecka o ID: " + id));

        // Zaktualizuj tylko podstawowe pola, nie ruszając relacji
        existingChild.setFirstName(child.getFirstName());
        existingChild.setLastName(child.getLastName());
        existingChild.setBirthDate(child.getBirthDate());
        existingChild.setDiagnosis(child.getDiagnosis());

        // Zaktualizuj grupę
        if (groupId != null) {
            Group group = groupService.getGroupById(groupId)
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono grupy o ID: " + groupId));
            existingChild.setGroup(group);
        } else {
            existingChild.setGroup(null);
        }

        childService.saveChild(existingChild);
        return "redirect:/children/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteChild(@PathVariable Long id) {
        childService.deleteChild(id);
        return "redirect:/children";
    }

    @PostMapping("/{id}/notes")
    public String addNote(@PathVariable Long id,
                          @RequestParam String content,
                          @RequestParam(required = false) String noteDate,
                          @RequestParam(required = false) String category) {
        ChildNote note = new ChildNote();
        note.setContent(content);
        if (noteDate != null && !noteDate.isEmpty()) {
            note.setNoteDate(java.time.LocalDate.parse(noteDate));
        }
        if (category != null && !category.isEmpty()) {
            note.setCategory(category);
        }
        childNoteService.addNoteToChild(id, note);
        return "redirect:/children/" + id;
    }

    @PostMapping("/notes/{noteId}/delete")
    public String deleteNote(@PathVariable Long noteId, @RequestParam Long childId) {
        childNoteService.deleteNote(noteId);
        return "redirect:/children/" + childId;
    }
}
