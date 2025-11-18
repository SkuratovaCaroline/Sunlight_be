package com.skuratova.cinema.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    private String roleName;
}

@Entity
@Table(name = "locations")
class Location {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;
    private String locationName;
    private String address;
}

@Entity
@Table(name = "users")
class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String passwordHash;
    private String email;
    private String firstName;
    private String lastName;
    private String uAvatar;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Entity
@Table(name = "halls")
class Hall {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hallId;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private String hallName;
    private Integer capacity;
    private String description;
    private LocalDateTime createdAt;
}

@Entity
@Table(name = "films")
class Film {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmId;
    private String title;
    private String description;
    private Integer duration;
    private String ageRating;
    private Integer releaseYear;
    private String genre;
    private String fPoster;
    private LocalDateTime createdAt;
    private LocalDate releaseDate;
    private LocalDate endDate;
}

@Entity
@Table(name = "sessions")
class Session {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double basePrice;
    private LocalDateTime createdAt;
}

@Entity
@Table(name = "seats")
class Seat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;

    private Integer rowNumber;
    private Integer seatNumber;
    private String type;
    private LocalDateTime createdAt;
}

@Entity
@Table(name = "tickets")
class Ticket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private Double price;
    private String status;
    private LocalDateTime soldAt;
    private String qrCode;
    private LocalDateTime createdAt;
}

@Entity
@Table(name = "user_ratings")
class UserRating {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;

    private Short rating;
    private LocalDateTime ratedAt;
}

@Entity
@Table(name = "reviews")
class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;

    private String reviewText;
    private LocalDateTime reviewDate;
    private Boolean isApproved;
    private LocalDateTime createdAt;
}

@Entity
@Table(name = "notifications")
class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime timestamp;
}
