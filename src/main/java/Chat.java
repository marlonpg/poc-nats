import io.nats.client.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.Scanner;

public class Chat {
    private static final String CHAT_SUBJECT = "chat.messages";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java Chat <username>");
            System.exit(1);
        }
        
        String username = args[0];
        Connection nc = Nats.connect("nats://localhost:4222");
        
        // Subscribe to chat messages
        Subscription sub = nc.subscribe(CHAT_SUBJECT);
        
        // Handle incoming messages in separate thread
        Thread.startVirtualThread(() -> {
            try {
                while (true) {
                    Message msg = sub.nextMessage(Duration.ofSeconds(1));
                    if (msg != null) {
                        String message = new String(msg.getData());
                        if (!message.startsWith(username + ":")) {
                            System.out.println(message);
                        }
                    }
                }
            } catch (Exception e) {
                // Connection closed
            }
        });
        
        System.out.println("Connected to chat as: " + username);
        System.out.println("Type messages (or 'quit' to exit):");
        
        Scanner scanner = new Scanner(System.in);
        String input;
        
        while (!(input = scanner.nextLine()).equals("quit")) {
            String timestamp = LocalTime.now().format(TIME_FORMAT);
            String message = "[" + timestamp + "] " + username + ": " + input;
            nc.publish(CHAT_SUBJECT, message.getBytes());
        }
        
        nc.close();
        System.out.println("Disconnected from chat");
    }
}