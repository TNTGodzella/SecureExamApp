package com.exam;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Pattern;

public class SecureExamApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginWindow::new);
    }
}

// ------------------- LOGIN -------------------
class LoginWindow {
    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passField;
    private JLabel errorLabel;

    public LoginWindow() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        emailField = new JTextField(20);
        passField = new JPasswordField(20);
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(errorLabel, gbc);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(0, 120, 215));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.addActionListener(e -> handleLogin());

        gbc.gridy = 3;
        panel.add(loginBtn, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String pass = new String(passField.getPassword()).trim();

        if (email.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Email and Password required.");
            return;
        }

        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", email)) {
            errorLabel.setText("Invalid email format.");
            return;
        }

        if (email.equals("student@gmail.com") && pass.equals("exam123")) {
            frame.dispose();
            new PhotoUploadWindow();
        } else {
            errorLabel.setText("Invalid credentials.");
        }
    }
}

// ------------------- PHOTO UPLOAD -------------------
class PhotoUploadWindow {
    private JFrame frame;
    private JLabel imageLabel;
    private BufferedImage selectedImage;

    public PhotoUploadWindow() {
        frame = new JFrame("Upload Photo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        imageLabel = new JLabel("Upload a recent photo", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(480, 360));

        JButton uploadBtn = new JButton("Upload");
        JButton continueBtn = new JButton("Continue to Exam");
        continueBtn.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(uploadBtn);
        buttonPanel.add(continueBtn);

        frame.add(imageLabel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        uploadBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    selectedImage = ImageIO.read(file);
                    ImageIcon icon = new ImageIcon(selectedImage.getScaledInstance(480, 360, Image.SCALE_SMOOTH));
                    imageLabel.setIcon(icon);
                    imageLabel.setText("");
                    continueBtn.setEnabled(true);

                    ImageIO.write(selectedImage, "jpg", new File("student_photo.jpg"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to load image.");
                }
            }
        });

        continueBtn.addActionListener(e -> {
            frame.dispose();
            new SecureExamWindow();
        });
    }
}

// ------------------- SECURE EXAM WINDOW -------------------
class SecureExamWindow {
    private final JFrame frame;
    private final GraphicsDevice gd;
    private int currentQuestion = 0;
    private int cheatCount = 0;

    private final String[] questions = {
            "What is the capital of India?",
            "Which language is used for Android Development?",
            "What does JVM stand for?",
            "Which data structure uses LIFO?",
            "Which keyword is used to inherit a class in Java?"
    };

    private final String[][] options = {
            {"Delhi", "Mumbai", "Kolkata", "Chennai"},
            {"Java", "Python", "Swift", "C++"},
            {"Java Virtual Machine", "Java Visual Model", "Joint Variable Module", "None"},
            {"Queue", "Stack", "Array", "Graph"},
            {"extends", "implements", "import", "super"}
    };

    private final int[] correctAnswers = {0, 0, 0, 1, 0};
    private final int[] selectedAnswers = new int[5];
    private final ButtonGroup optionGroup = new ButtonGroup();
    private final JRadioButton[] optionBtns = new JRadioButton[4];
    private final JLabel questionLabel = new JLabel();
    private final JButton nextBtn = new JButton("Next");
    private final JButton prevBtn = new JButton("Previous");

    public SecureExamWindow() {
        frame = new JFrame("Exam");
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        center.setBackground(new Color(224, 255, 255));

        questionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionLabel.setForeground(new Color(0, 51, 102));
        center.add(questionLabel);

        for (int i = 0; i < 4; i++) {
            optionBtns[i] = new JRadioButton();
            optionBtns[i].setFont(new Font("Arial", Font.PLAIN, 18));
            optionBtns[i].setBackground(new Color(224, 255, 255));
            optionBtns[i].setForeground(Color.DARK_GRAY);
            optionGroup.add(optionBtns[i]);
            center.add(optionBtns[i]);
        }

        JPanel nav = new JPanel();
        nav.setBackground(new Color(240, 248, 255));
        JButton submitBtn = new JButton("Submit");
        JButton exitBtn = new JButton("Exit");

        nav.add(prevBtn);
        nav.add(nextBtn);
        nav.add(submitBtn);
        nav.add(exitBtn);

        prevBtn.addActionListener(e -> {
            saveAnswer();
            if (currentQuestion > 0) currentQuestion--;
            loadQuestion();
        });

        nextBtn.addActionListener(e -> {
            saveAnswer();
            if (currentQuestion < questions.length - 1) currentQuestion++;
            loadQuestion();
        });

        submitBtn.addActionListener(e -> autoSubmit());

        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Submit and exit?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) autoSubmit();
        });

        frame.getContentPane().add(center, BorderLayout.CENTER);
        frame.getContentPane().add(nav, BorderLayout.SOUTH);
        frame.setVisible(true);
        goFullscreen();
        loadQuestion();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            int code = e.getKeyCode();
            if ((e.isAltDown() && code == KeyEvent.VK_TAB) || (code == KeyEvent.VK_WINDOWS) ||
                    (e.isControlDown() && code == KeyEvent.VK_ESCAPE)) {
                cheatCount++;
                if (cheatCount == 1) showWarning();
                else autoSubmit();
                return true;
            }
            return false;
        });
    }

    private void showWarning() {
        gd.setFullScreenWindow(null);
        JOptionPane.showMessageDialog(frame, "⚠ Cheating detected! Next time will auto-submit.");
        goFullscreen();
    }

    private void goFullscreen() {
        if (gd.isFullScreenSupported()) gd.setFullScreenWindow(frame);
        else frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void loadQuestion() {
        questionLabel.setText("Q" + (currentQuestion + 1) + ": " + questions[currentQuestion]);
        optionGroup.clearSelection();
        for (int i = 0; i < 4; i++) {
            optionBtns[i].setText(options[currentQuestion][i]);
            optionBtns[i].setSelected(selectedAnswers[currentQuestion] == i + 1);
        }
        prevBtn.setEnabled(currentQuestion > 0);
    }

    private void saveAnswer() {
        for (int i = 0; i < 4; i++) {
            if (optionBtns[i].isSelected()) {
                selectedAnswers[currentQuestion] = i + 1;
                return;
            }
        }
    }

    private void autoSubmit() {
        saveAnswer();
        gd.setFullScreenWindow(null);
        frame.dispose();
        int score = 0;
        for (int i = 0; i < questions.length; i++) {
            if (selectedAnswers[i] - 1 == correctAnswers[i]) score++;
        }
        new ResultWindow(score, questions.length);
    }
}

// ------------------- RESULT WINDOW -------------------
class ResultWindow {
    public ResultWindow(int score, int total) {
        JFrame frame = new JFrame("Result");
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(245, 255, 250));

        JLabel msg = new JLabel("Your exam has been submitted!", SwingConstants.CENTER);
        JLabel scoreLabel = new JLabel("Score: " + score + "/" + total, SwingConstants.CENTER);
        JLabel resultLabel = new JLabel(score >= 3 ? "✅ Passed" : "❌ Failed", SwingConstants.CENTER);

        resultLabel.setForeground(score >= 3 ? Color.GREEN.darker() : Color.RED);

        panel.add(msg);
        panel.add(scoreLabel);
        panel.add(resultLabel);
        frame.add(panel);
        frame.setVisible(true);
    }
}
