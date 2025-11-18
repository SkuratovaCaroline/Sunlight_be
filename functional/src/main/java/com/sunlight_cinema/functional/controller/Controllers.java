@RestController
@RequestMapping("/api")
public class Controllers {
    @Autowired private UserRepository userRepo;
    @Autowired private FilmRepository filmRepo;
    @Autowired private FileStorageService fileService;

    @PostMapping("/users/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable Long id, @RequestParam("avatar") MultipartFile file) throws IOException {
        User user = userRepo.findById(id).orElseThrow();
        String path = fileService.saveAvatar(file);
        user.setUAvatar(path);
        userRepo.save(user);
        return ResponseEntity.ok("Аватарка сохранена");
    }

    @GetMapping("/users/{id}/avatar")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long id) throws IOException {
        User user = userRepo.findById(id).orElseThrow();
        byte[] data = fileService.loadFile(user.getUAvatar());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(data);
    }

    @PostMapping("/films/{id}/poster")
    public ResponseEntity<?> uploadPoster(@PathVariable Long id, @RequestParam("poster") MultipartFile file) throws IOException {
        Film film = filmRepo.findById(id).orElseThrow();
        String path = fileService.savePoster(file);
        film.setFPoster(path);
        filmRepo.save(film);
        return ResponseEntity.ok("Постер сохранён");
    }

    @GetMapping("/films/{id}/poster")
    public ResponseEntity<byte[]> getPoster(@PathVariable Long id) throws IOException {
        Film film = filmRepo.findById(id).orElseThrow();
        byte[] data = fileService.loadFile(film.getFPoster());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(data);
    }
}
