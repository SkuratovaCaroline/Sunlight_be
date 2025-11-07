@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    @Autowired
    private WebSocketHandler webSocketHandler;

    public void sendNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        repository.save(notification);
        webSocketHandler.broadcast(message);
    }
}
