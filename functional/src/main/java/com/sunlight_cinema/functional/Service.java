@Service
public class CinemaService {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;
    private final TicketRepository ticketRepository;
    private final NotificationRepository notificationRepository;
    private final SeatRepository seatRepository;
    private final SessionRepository sessionRepository;

    public CinemaService(UserRepository userRepository,
                         FilmRepository filmRepository,
                         ReviewRepository reviewRepository,
                         RatingRepository ratingRepository,
                         TicketRepository ticketRepository,
                         NotificationRepository notificationRepository,
                         SeatRepository seatRepository,
                         TicketRepository ticketRepository,
                         SessionRepository sessionRepositor) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.reviewRepository = reviewRepository;
        this.ratingRepository = ratingRepository;
        this.ticketRepository = ticketRepository;
        this.notificationRepository = notificationRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        this.sessionRepository = sessionRepository;
    }

    // --- USERS ---
    public User updateAvatar(Long userId, String avatarPath) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setAvatarPath(avatarPath);
        return userRepository.save(user);
    }

    public User deleteAvatar(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setAvatarPath(null);
        return userRepository.save(user);
    }

    public String getAvatar(Long userId) {
        return userRepository.findById(userId).map(User::getAvatarPath).orElse(null);
    }
    
    // --- ЗАНЯТЫЕ И СВОБОДНЫЕ МЕСТА ---
    public Map<String, List<Seat>> getSeatsStatus(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        Long hallId = session.getHall().getHallId();

        // все места в зале
        List<Seat> allSeats = seatRepository.findByHallId(hallId);

        // занятые места (по билетам)
        List<Seat> occupiedSeats = ticketRepository.findOccupiedSeatsBySession(sessionId);

        // свободные
        List<Seat> freeSeats = allSeats.stream()
                .filter(seat -> !occupiedSeats.contains(seat))
                .toList();

        Map<String, List<Seat>> result = new HashMap<>();
        result.put("occupied", occupiedSeats);
        result.put("free", freeSeats);
        return result;
    }

    // --- Фильмы ---
    public Film addPoster(Long filmId, String posterPath) {
        Film film = filmRepository.findById(filmId).orElseThrow();
        film.setPosterPath(posterPath);
        return filmRepository.save(film);
    }

    public List<Film> filterFilmsByGenre(String genre) {
        return filmRepository.findByGenre(genre);
    }

    public Double getAverageRating(Long filmId) {
        return ratingRepository.findAverageRatingByFilmId(filmId);
    }

     // --- ПОКУПКА БИЛЕТА ---
    public Ticket purchaseTicket(Long userId, Long sessionId, Long seatId, Double price) {
        User user = userRepository.findById(userId).orElseThrow();
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        Seat seat = seatRepository.findById(seatId).orElseThrow();

        // Проверка: место уже занято?
        if (ticketRepository.existsBySessionAndSeat(session, seat)) {
            throw new RuntimeException("Seat already booked");
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setSession(session);
        ticket.setSeat(seat);
        ticket.setPrice(price);
        ticket.setStatus("SOLD");
        ticket.setSoldAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    // --- РАСПИСАНИЕ ФИЛЬМА ---
    public List<Session> getFilmSchedule(Long filmId) {
        Film film = filmRepository.findById(filmId).orElseThrow();
        return sessionRepository.findByFilmOrderByStartTimeAsc(film);
    }


    // --- Отзывы ---
    public Review createReview(Long userId, Long filmId, String text) {
        Review review = new Review();
        review.setUserId(userId);
        review.setFilmId(filmId);
        review.setReviewText(text);
        return reviewRepository.save(review);
    }

    public List<Review> getReviews(Long filmId) {
        return reviewRepository.findByFilmIdAndIsApprovedTrueOrderByReviewDateDesc(filmId);
    }

    // --- Рейтинг ---
    public UserRating createOrUpdateRating(Long userId, Long filmId, int rating) {
        return ratingRepository.saveOrUpdate(userId, filmId, rating);
    }

    // --- TICKETS / HISTORY ---
    public List<Ticket> getPastTickets(Long userId) {
        return ticketRepository.findPastTickets(userId);
    }

    public List<Ticket> getFutureTickets(Long userId) {
        return ticketRepository.findFutureTickets(userId);
    }

    // --- NOTIFICATIONS ---
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
}
