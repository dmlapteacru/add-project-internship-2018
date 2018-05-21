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

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

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

    private static final Logger LOGGER = Logger.getLogger(RestController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String adminMain(Model model){
        model.addAttribute("users", userService.getAllUsersWithProfile());
        return "admin/admin";
    }

    @RequestMapping(value = "status", method = RequestMethod.GET)
    public String approveNewUser(@RequestParam(name = "username")String username){
        userService.changeUserStatus(username);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/services", method = GET)
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }


    @RequestMapping(value = "/admin/categories", method = GET)
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @RequestMapping(value = "/admin/newCategory", method = PUT)
    public ResponseEntity<?> saveNewCategory(@RequestBody Category category, BindingResult error) {
        validator.validate(category, error);
        if (error.hasErrors()) {
            return new ResponseEntity<>(error.getFieldError().getCode(), HttpStatus.BAD_REQUEST);
        }
        categoryService.saveCategory(category);
        LOGGER.info(String.format("admin has created new category %s:%s", category.getId(), category.getName()));
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/deleteCategory/{id}", method = RequestMethod.DELETE)
    public String deleteCategory(@PathVariable Integer id) {
        LOGGER.info(String.format("admin is trying to delete category with id %s", id));
        String categoryName = categoryService.getCategoryById(id).getName();
        categoryService.deleteCategory(id);
        LOGGER.info(String.format("category %s:%s was deleted", id, categoryName));
        return "OK";
    }

    @RequestMapping(value = "/admin/messages", method = GET)
    public List<AdminMessage> showMessages() {
        return adminMessageService.getAllMessages();
    }

    @RequestMapping(value = "/admin/messages/unread", method = GET)
    public List<AdminMessage> showMessagesByStatusUnread() {
        return adminMessageService.getAllMessagesByStatusUnread();
    }

    @RequestMapping(value = "/admin/messages/read", method = GET)
    public List<AdminMessage> showMessagesByStatusRead() {
        return adminMessageService.getAllMessagesByStatusRead();
    }

    @RequestMapping(value = "/message/send", method = POST)
    public String addNewMessage(@RequestBody AdminMessage adminMessage) {
        adminMessageService.save(adminMessage);
        return "OK";
    }

    @RequestMapping(value = "/admin/message/changeStatus/{id}", method = PUT)
    public String changeMessageStatus(@PathVariable int id) {
        adminMessageService.changeMessageStatus(id);
        return "OK";
    }

    @RequestMapping(value = "/admin/message/changeStatus/read", method = PUT)
    public String changeMessageStatusOnRead(@RequestBody List<ChangeMessageStatusDto> changeMessageStatusDtoList) {
        adminMessageService.changeMessageStatusOnRead(changeMessageStatusDtoList);
        return "OK";
    }

    @RequestMapping(value = "/admin/message/changeStatus/unread", method = PUT)
    public String changeMessageStatusOnUnRead(@RequestBody List<ChangeMessageStatusDto> changeMessageStatusDtoList) {
        adminMessageService.changeMessageStatusOnUnRead(changeMessageStatusDtoList);
        return "OK";
    }

    @RequestMapping(value = "/admin/message/delete/{id}", method = RequestMethod.DELETE)
    public String deleteMessage(@PathVariable int id) {
        adminMessageService.deleteById(id);
        return "OK";
    }

    @RequestMapping(value = "/admin/message/bulkDelete", method = RequestMethod.DELETE)
    public String deleteMessage(@RequestBody List<ChangeMessageStatusDto> changeMessageStatusDtoList) {
        adminMessageService.deleteMessages(changeMessageStatusDtoList);
        return "OK";
    }

    @RequestMapping(value = "/admin/changeUserStatus/active", method = POST)
    public String changeUserStatusOnActive(@RequestBody List<ChangeUserStatusDto> changeUserStatusDto) {
        userService.changeUserStatusOnActive(changeUserStatusDto);
        return "OK";
    }

    @RequestMapping(value = "/admin/changeUserStatus/inactive", method = POST)
    public String changeUserStatusOnInactive(@RequestBody List<ChangeUserStatusDto> changeUserStatusDto) {
        userService.changeUserStatusOnInactive(changeUserStatusDto);
        return "OK";
    }

    @RequestMapping(value = "/admin/notifications/unread", method = GET)
    public List<Notification> getAllNotificationByStatusUnread(){
        return notificationService.getAllByStatusUnread("admin");
    }

    @RequestMapping(value = "/notifications/unread", method = GET)
    public List<Notification> getAllUsersNotificationByStatusUnread(){
        return notificationService.getAllByStatusUnread(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @RequestMapping(value = "/admin/notifications/changeStatus", method = PUT)
    public String changeNotificationStatus(@RequestParam("id") int id){
        notificationService.changeNotificationStatusOnRead(id);
        return "OK";
    }
    @RequestMapping(value = "/notifications/changeStatus", method = PUT)
    public String changeCustomerNotificationStatus(@RequestParam("id") int id){
        notificationService.changeNotificationStatusOnRead(id);
        return "OK";
    }
}
