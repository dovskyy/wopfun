package com.dovskyy.wopfun.controller;

import com.dovskyy.wopfun.model.Role;
import com.dovskyy.wopfun.model.User;
import com.dovskyy.wopfun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("isEdit", false);
        model.addAttribute("roles", Role.values());
        return "users/form";
    }

    @PostMapping
    public String createUser(
            @RequestParam String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam Role role,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.createUser(username, email, password, firstName, lastName, role);
            redirectAttributes.addFlashAttribute("successMessage", "Użytkownik został pomyślnie utworzony");
            return "redirect:/admin/users";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/users/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie znaleziony"));
            model.addAttribute("user", user);
            model.addAttribute("isEdit", true);
            model.addAttribute("roles", Role.values());
            return "users/form";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/{id}")
    public String updateUser(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam String email,
            @RequestParam(required = false) String password,
            @RequestParam Role role,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.updateUser(id, username, email, password, firstName, lastName, role);
            redirectAttributes.addFlashAttribute("successMessage", "Użytkownik został zaktualizowany");
            return "redirect:/admin/users";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/users/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Użytkownik został usunięty");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/toggle")
    public String toggleUserEnabled(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleUserEnabled(id);
            redirectAttributes.addFlashAttribute("successMessage", "Status użytkownika został zmieniony");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
