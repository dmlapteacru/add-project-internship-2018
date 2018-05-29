package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.AdminMessage;
import com.endava.addprojectinternship2018.model.Category;
import com.endava.addprojectinternship2018.model.Notification;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.ChangeMessageStatusDto;
import com.endava.addprojectinternship2018.model.dto.ChangeUserStatusDto;
import com.endava.addprojectinternship2018.service.*;
import com.endava.addprojectinternship2018.validation.Validator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class AdminPanelRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private Validator validator;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AdminMessageService adminMessageService;

    private static final Logger LOGGER = Logger.getLogger(AdminPanelRestController.class);

    @GetMapping(value = "")
    public String adminMain(Model model){
        model.addAttribute("users", userService.getAllUsersWithProfile());
        return "admin/admin";
    }

    @GetMapping(value = "status")
    public String approveNewUser(@RequestParam(name = "username")String username){
        userService.changeUserStatus(username);
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/services")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }


    @GetMapping(value = "/admin/categories")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @PutMapping(value = "/admin/newCategory")
    public ResponseEntity<?> saveNewCategory(@RequestBody Category category, BindingResult error) {
        validator.validate(category, error);
        if (error.hasErrors()) {
            return new ResponseEntity<>(error.getFieldError().getCode(), HttpStatus.BAD_REQUEST);
        }
        categoryService.saveCategory(category);
        LOGGER.info(String.format("admin has created new category %s:%s", category.getId(), category.getName()));
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @DeleteMapping(value = "/admin/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        LOGGER.info(String.format("admin is trying to delete category with id %s", id));
        String categoryName = categoryService.getCategoryById(id).getName();
        categoryService.deleteCategory(id);
        LOGGER.info(String.format("category %s:%s was deleted", id, categoryName));
        return "OK";
    }

    @GetMapping(value = "/admin/messages")
    public List<AdminMessage> showMessages() {
        return adminMessageService.getAllMessages();
    }

    @GetMapping(value = "/admin/messages/unread")
    public List<AdminMessage> showMessagesByStatusUnread() {
        return adminMessageService.getAllMessagesByStatusUnread();
    }

    @GetMapping(value = "/admin/messages/read")
    public List<AdminMessage> showMessagesByStatusRead() {
        return adminMessageService.getAllMessagesByStatusRead();
    }

    @PostMapping(value = "/message/send")
    public String addNewMessage(@RequestBody AdminMessage adminMessage) {
        adminMessageService.save(adminMessage);
        return "OK";
    }

    @PutMapping(value = "/admin/message/changeStatus/{id}")
    public String changeMessageStatus(@PathVariable int id) {
        adminMessageService.changeMessageStatus(id);
        return "OK";
    }

    @PutMapping(value = "/admin/message/changeStatus/read")
    public String changeMessageStatusOnRead(@RequestBody List<ChangeMessageStatusDto> changeMessageStatusDtoList) {
        adminMessageService.changeMessageStatusOnRead(changeMessageStatusDtoList);
        return "OK";
    }

    @PutMapping(value = "/admin/message/changeStatus/unread")
    public String changeMessageStatusOnUnRead(@RequestBody List<ChangeMessageStatusDto> changeMessageStatusDtoList) {
        adminMessageService.changeMessageStatusOnUnRead(changeMessageStatusDtoList);
        return "OK";
    }

    @DeleteMapping(value = "/admin/message/delete/{id}")
    public String deleteMessage(@PathVariable int id) {
        adminMessageService.deleteById(id);
        return "OK";
    }

    @DeleteMapping(value = "/admin/message/bulkDelete")
    public String deleteMessage(@RequestBody List<ChangeMessageStatusDto> changeMessageStatusDtoList) {
        adminMessageService.deleteMessages(changeMessageStatusDtoList);
        return "OK";
    }

    @PostMapping(value = "/admin/changeUserStatus/active")
    public String changeUserStatusOnActive(@RequestBody List<ChangeUserStatusDto> changeUserStatusDto) {
        userService.changeUserStatusOnActive(changeUserStatusDto);
        return "OK";
    }

    @PostMapping(value = "/admin/changeUserStatus/inactive")
    public String changeUserStatusOnInactive(@RequestBody List<ChangeUserStatusDto> changeUserStatusDto) {
        userService.changeUserStatusOnInactive(changeUserStatusDto);
        return "OK";
    }

    @GetMapping(value = "/admin/notifications/unread")
    public List<Notification> getAllNotificationByStatusUnread(){
        return notificationService.getAllByStatusUnread("admin");
    }

    @GetMapping(value = "/notifications/unread")
    public List<Notification> getAllUsersNotificationByStatusUnread(){
        return notificationService.getAllByStatusUnread(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PutMapping(value = "/admin/notifications/changeStatus")
    public String changeNotificationStatus(@RequestParam("id") int id){
        notificationService.changeNotificationStatusOnRead(id);
        return "OK";
    }
    @PutMapping(value = "/notifications/changeStatus")
    public String changeCustomerNotificationStatus(@RequestParam("id") int id){
        notificationService.changeNotificationStatusOnRead(id);
        return "OK";
    }
}
