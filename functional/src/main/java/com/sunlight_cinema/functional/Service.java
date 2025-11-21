@Service
public class CinemaService {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;
    private final TicketRepository ticketRepository;
    private final NotificationRepository notificationRepository;

    public CinemaService(UserRepository userRepository,
                         FilmRepository filmRepository,
                         ReviewRepository reviewRepository,
                         RatingRepository ratingRepository,
                         TicketRepository ticketRepository,
                         NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.reviewRepository = reviewRepository;
        this.ratingRepository = ratingRepository;
        this.ticketRepository = ticketRepository;
        this.notificationRepository = notificationRepository;
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

    // --- FILMS ---
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

    // --- REVIEWS ---
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

    // --- RATINGS ---
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
