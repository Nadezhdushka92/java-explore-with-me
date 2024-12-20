package ru.practicum.event.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@SuppressWarnings("checkstyle:Regexp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "events")
public class Event {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Size(min = 3, max = 120)
   @Column(nullable = false)
   private String title;

   @Size(min = 20, max = 2000)
   @Column(nullable = false)
   private String annotation;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(nullable = false, name = "category_id")
   private Category category;

   @Column(nullable = false, name = "confirmed_requests")
   private Integer confirmedRequests;

   @Size(min = 20, max = 7000)
   @Column(nullable = false)
   private String description;

   @Column(nullable = false, name = "event_date")
   private LocalDateTime eventDate;

   @Column(nullable = false, name = "created_on")
   private LocalDateTime createdOn;

   @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
   @JoinColumn(nullable = false, name = "initiator_id")
   private User initiator;

   @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
   @JoinColumn(nullable = false, name = "location_id")
   private Location location;

   @Column(nullable = false)
   private Boolean paid;

   @PositiveOrZero
   @Column(nullable = false, name = "participant_limit")
   private Integer participantLimit;

   @Column(name = "published_on")
   private LocalDateTime publishedOn;

   @Column(nullable = false, name = "request_moderation")
   private Boolean requestModeration;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private State state;
}