@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService service;

    @PostMapping
    public ResponseEntity<Void> send(@RequestBody String message) {
        service.sendNotification(message);
        return ResponseEntity.ok().build();
    }
}
