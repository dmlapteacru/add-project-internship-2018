package com.endava.addprojectinternship2018.model;

import com.endava.addprojectinternship2018.model.enums.AdminMessagesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Table(name = "ADMIN_MESSAGES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column
    @NotNull
    private String message;

    @Column
    @NotNull
    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,3}$")
    private String userEmail;

    @Column
    @NotNull
    private String subject;

    @Column
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date date;

    @Column
    @Enumerated(value = EnumType.STRING)
    private AdminMessagesStatus status = AdminMessagesStatus.UNREAD;

    public AdminMessage(String message, String userEmail, String subject) {
        this.message = message;
        this.userEmail = userEmail;
        this.subject = subject;
    }
}
