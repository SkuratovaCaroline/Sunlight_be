package com.example.cinema.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "roles")
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(nullable = false, unique = true, length = 50)
    private String roleName;
    // getters/setters
}

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(unique = true, length = 100)
    private String email;

    private String firstName;
    private String lastName;

    @Column(name = "avatar_path")
    private String avatarPath;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    // getters/setters
}

@Entity
@Table(name = "locations")
class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    private String locationName;
    private String address;
    // getters/setters
}

@Entity
@Table(name = "halls")
class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hallId;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private String hallName;
    private Integer capacity;
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    // getters/setters
}

@Entity
@Table(name = "films")
class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    private String ageRating;
    private Integer releaseYear;
    private String genre;

    @Column(name = "poster_path")
    private String posterPath;

    private LocalDate releaseDate;
    private LocalDate endDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    // getters/setters
}

@Entity
@Table(name = "sessions")
class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double basePrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    // getters/setters
}

@Entity
@Table(name = "seats")
class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    private Integer rowNumber;
    private Integer seatNumber;
    private String seatType;
    // getters/setters
}

@Entity
@Table(name = "tickets")
class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private Double price;
    private String status;
    private LocalDateTime soldAt;
    private String qrCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    // getters/setters
}

@Entity
@Table(name = "user_ratings")
class UserRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    private Integer rating;
    private LocalDateTime ratedAt = LocalDateTime.now();
    // getters/setters
}

@Entity
@Table(name = "reviews")
class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    private String reviewText;
    private LocalDateTime reviewDate = LocalDateTime.now();
    private Boolean isApproved = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    // getters/setters
}

@Entity
@Table(name = "notifications")
class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Boolean isRead = false;
    // getters/setters
}
