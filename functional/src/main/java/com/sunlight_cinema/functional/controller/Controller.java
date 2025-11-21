@RestController
@RequestMapping("/api/cinema")
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    // --- USERS ---
    @PutMapping("/users/{id}/avatar")
    public ResponseEntity<User> updateAvatar(@PathVariable Long id, @RequestParam String path) {
        return ResponseEntity.ok(cinemaService.updateAvatar(id, path));
    }

    @DeleteMapping("/users/{id}/avatar")
    public ResponseEntity<User> deleteAvatar(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.deleteAvatar(id));
    }

    @GetMapping("/users/{id}/avatar")
    public ResponseEntity<String> getAvatar(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.getAvatar(id));
    }

    // --- FILMS ---
    @PutMapping("/films/{id}/poster")
    public ResponseEntity<Film> addPoster(@PathVariable Long id, @RequestParam String path) {
        return ResponseEntity.ok(cinemaService.addPoster(id, path));
    }

    @GetMapping("/films/genre/{genre}")
    public ResponseEntity<List<Film>> filterFilmsByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(cinemaService.filterFilmsByGenre(genre));
    }

    @GetMapping("/films/{id}/rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.getAverageRating(id));
    }

    @PostMapping("/tickets/purchase")
    public ResponseEntity<Ticket> purchaseTicket(@RequestParam Long userId,
                                                 @RequestParam Long sessionId,
                                                 @RequestParam Long seatId,
                                                 @RequestParam Double price) {
        return ResponseEntity.ok(cinemaService.purchaseTicket(userId, sessionId, seatId, price));
    }

    // --- РАСПИСАНИЕ ФИЛЬМА ---
    @GetMapping("/films/{id}/schedule")
    public ResponseEntity<List<Session>> getFilmSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.getFilmSchedule(id));
    }

     // --- ЗАНЯТЫЕ И СВОБОДНЫЕ МЕСТА ---
    @GetMapping("/sessions/{id}/seats")
    public ResponseEntity<Map<String, List<Seat>>> getSeatsStatus(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.getSeatsStatus(id));
    }

    // --- REVIEWS ---
    @PostMapping("/films/{id}/reviews")
    public ResponseEntity<Review> createReview(@PathVariable Long id,
                                               @RequestParam Long userId,
                                               @RequestParam String text) {
        return ResponseEntity.ok(cinemaService.createReview(userId, id, text));
    }

    @GetMapping("/films/{id}/reviews")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.getReviews(id));
    }

    // --- RATINGS ---
    @PostMapping("/films/{id}/ratings")
    public ResponseEntity<UserRating> createOrUpdateRating(@PathVariable Long id,
                                                           @RequestParam Long userId,
                                                           @RequestParam int rating) {
        return ResponseEntity.ok(cinemaService.createOrUpdateRating(userId, id, rating));
    }

    // --- TICKETS ---
    @GetMapping("/users/{id}/tickets/past")
    public ResponseEntity<List<Ticket>> getPastTickets(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.getPastTickets(id));
    }

    @GetMapping("/users/{id}/tickets/future")
    public ResponseEntity<List<Ticket>> getFutureTickets(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.getFutureTickets(id));
    }

    // --- NOTIFICATIONS ---
    @GetMapping("/users/{id}/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.getUserNotifications(id));
    }
}
