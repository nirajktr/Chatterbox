import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketClient extends JFrame implements ActionListener, Runnable {
    private JTextArea chatHistory;
    private JTextField messageInput;
    private JButton sendButton;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public SocketClient() {
        super("Chatterbox - Client");
        initializeUI();
    }

    private void initializeUI() {
        // Set Look and Feel (System Look)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Unable to set Look and Feel: " + e.getMessage());
        }

        // Main Layout
        setLayout(new BorderLayout());

        // Header Panel (Dark with Green Text)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(0, 0, 0));  // Black background
        JLabel titleLabel = new JLabel("The Dark Room...");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 255, 0));  // Green text for old-school look
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Chat History Area
        chatHistory = new JTextArea();
        chatHistory.setFont(new Font("Monospaced", Font.PLAIN, 14));
        chatHistory.setEditable(false);
        chatHistory.setBackground(new Color(0, 0, 0));  // Black background
        chatHistory.setForeground(new Color(0, 255, 0));  // Green text
        chatHistory.setLineWrap(true);
        chatHistory.setWrapStyleWord(true);
        JScrollPane chatScrollPane = new JScrollPane(chatHistory);
        chatScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(chatScrollPane, BorderLayout.CENTER);

        // Input Panel (Dark with Green Text)
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messageInput = new JTextField();
        messageInput.setFont(new Font("Monospaced", Font.PLAIN, 14));
        messageInput.setBackground(new Color(20, 20, 20));  // Very dark gray for input
        messageInput.setForeground(new Color(0, 255, 0));  // Green text for input
        messageInput.setCaretColor(Color.GREEN);  // Green caret for consistency
        messageInput.setToolTipText("Type your message here...");
        inputPanel.add(messageInput, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        sendButton.setBackground(new Color(0, 0, 128)); // Dark Blue button
        sendButton.setForeground(Color.BLACK);  // Black text
        sendButton.setFocusPainted(false);
        sendButton.setPreferredSize(new Dimension(100, 40));
        sendButton.addActionListener(this);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect for button
        sendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sendButton.setBackground(new Color(70, 130, 180)); // Lighter on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                sendButton.setBackground(new Color(0, 0, 128)); // Reset on exit
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // Add ActionListener to messageInput for Enter key press
        messageInput.addActionListener(this);

        // Frame Settings
        setSize(600, 700);
        setLocationRelativeTo(null);  // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        messageInput.requestFocusInWindow(); // Auto-focus on the input field when the app starts
    }

    public void connectToServer() {
        try {
            String serverIP = JOptionPane.showInputDialog(this, "Enter Server IP:", "127.0.0.1");
            socket = new Socket(serverIP, 1234);

            String nickname = JOptionPane.showInputDialog(this, "Enter your nickname:");
            if (nickname == null || nickname.trim().isEmpty()) {
                nickname = "Anonymous";
            }

            // Initialize Reader and Writer
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Send nickname to server
            writer.println(nickname);

            // Start listening to server in a new thread
            new Thread(this).start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unable to connect to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                // Assuming the message is formatted as "[nickname] message"
                int nicknameEnd = message.indexOf(']') + 1;  // Find the end of the nickname
                String nickname = message.substring(1, nicknameEnd - 1);  // Extract nickname from the message
                String messageContent = message.substring(nicknameEnd + 1).trim();  // Extract the actual message

                // Create a timestamp for the message
                String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());

                // Format message as "timestamp: [nickname] message"
                String formattedMessage = String.format("%s: [%s] %s", timestamp, nickname, messageContent);

                // Append the formatted message to chatHistory
                chatHistory.append(formattedMessage + "\n");

                // Auto-scroll to the latest message if the chat history was scrolled down
                chatHistory.setCaretPosition(chatHistory.getText().length());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Connection lost: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            writer.println(message);  // Send the message
            messageInput.setText("");  // Clear the input field after sending
            messageInput.requestFocusInWindow();  // Keep focus on the input field
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SocketClient client = new SocketClient();
            client.connectToServer();  // Start connection after UI is loaded
        });
    }
}
