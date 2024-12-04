import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient extends JFrame implements ActionListener, Runnable {
    private JTextArea chatHistory;
    private JTextField messageInput;
    private JButton sendButton;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public SocketClient() {
        super("Chat Application");
        initializeUI();
    }

    private void initializeUI() {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Unable to set Look and Feel: " + e.getMessage());
        }

        // Main Layout
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(30, 144, 255));
        JLabel titleLabel = new JLabel("Chat Application");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Chat History Area
        chatHistory = new JTextArea();
        chatHistory.setFont(new Font("Monospaced", Font.PLAIN, 14));
        chatHistory.setEditable(false);
        chatHistory.setBackground(new Color(245, 245, 245));
        JScrollPane chatScrollPane = new JScrollPane(chatHistory);
        add(chatScrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        messageInput = new JTextField();
        messageInput.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageInput.setToolTipText("Type your message here...");
        inputPanel.add(messageInput, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.addActionListener(this);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // Frame Settings
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        messageInput.requestFocusInWindow();
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

            // Start listening to server
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
                chatHistory.append(message + "\n");
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
            writer.println(message);
            messageInput.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SocketClient client = new SocketClient();
            client.connectToServer();
        });
    }
}
