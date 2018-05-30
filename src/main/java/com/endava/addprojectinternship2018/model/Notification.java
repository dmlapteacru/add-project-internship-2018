package com.endava.addprojectinternship2018.model;

import com.endava.addprojectinternship2018.model.enums.NotificationCase;
import com.endava.addprojectinternship2018.model.enums.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATION")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column
    @Enumerated(value = EnumType.STRING)
    private NotificationCase notificationCase;

    @Column
    @Enumerated(value = EnumType.STRING)
    private NotificationStatus status = NotificationStatus.UNREAD;

    @Column
    private String content;

    @Column
    private LocalDateTime date = LocalDateTime.now();

    @Column
    private String userTo;

    @Column
    private String userFrom;

    @Column
    private int idSearch;

    public Notification(NotificationCase notificationCase, String content, String userTo, String userFrom) {
        this.notificationCase = notificationCase;
        this.content = content;
        this.userTo = userTo;
        this.userFrom = userFrom;
    }

    public Notification(NotificationCase notificationCase, String content, String userTo, int idSearch, String userFrom) {
        this.notificationCase = notificationCase;
        this.content = content;
        this.userTo = userTo;
        this.idSearch = idSearch;
        this.userFrom = userFrom;
    }
}
