package client;

import server.Message;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MyClient extends JFrame {

    private ServerService serverService;


    //чат
    JLabel chatName = new JLabel("Текстовый чат GeekBrains");
    JTextArea area = new JTextArea();
    JLabel label = new JLabel("Введите сообщение:");
    JTextField message = new JTextField(25);
    JButton send = new JButton("Отправить");
    JButton exit = new JButton("Выход");

    //авторизация
    JLabel labelLogin = new JLabel("Логин:    ");
    JTextField loginField = new JTextField(35);
    JLabel labelPass = new JLabel("Пароль:");
    JPasswordField passField = new JPasswordField(35);
    JButton authButton = new JButton("Вход");



    public MyClient(){

        serverService = new SocketServerService();
        serverService.openConnection();

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(exit);
        exit.addActionListener(actionEvent -> {
            sendMessage("/end");
            serverService.closeConnection();
            System.exit(0);
        });

        setTitle("Чат");
        setBounds(200, 200, 550,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel authorizedPanel = getAuthorizedPanel();
        JPanel topPanel = getTopPanel();
        JPanel centerPanel = getCenterPanel();
        JPanel rightPanel = getRightPanel();
        JPanel bottomPanel = getBottomPanel();

        send.addActionListener((actionEvent) -> {
            sendMessage(message);
        });
        message.addActionListener((actionEvent) -> {
            sendMessage(message);
        });

        authButton.addActionListener(actionEvent -> {
            String lgn = loginField.getText();
            String psw = String.valueOf(passField.getPassword());
            if (lgn != null && psw != null && !lgn.isEmpty() && !psw.isEmpty()) {
                try {
                        serverService.authorization(lgn, psw);
                        if (serverService.isConnected()) {
                            authorizedPanel.setVisible(false);
                            add(BorderLayout.CENTER, centerPanel);
                            add(BorderLayout.SOUTH, bottomPanel);
                            add(BorderLayout.EAST, rightPanel);
                            setJMenuBar(menuBar);

                            new Thread(() -> {
                                while (true) {
                                    printToUI(area, serverService.readMessages());
                                }
                            }).start();
                        } else {
                            return;
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        add(BorderLayout.NORTH, topPanel);
        add(BorderLayout.CENTER, authorizedPanel);

        if (serverService.isConnected()) {
            new Thread(() -> {
                while (true) {
                    printToUI(area, serverService.readMessages());
                }
            }).start();
        }

        setVisible(true);


    }

    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(chatName);
        return topPanel;

    }

    private JPanel getCenterPanel(){
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerPanel.setBackground(Color.GREEN);
        area.setBackground(Color.GREEN);
        centerPanel.add(area);
        return centerPanel;
    }

    private JPanel getAuthorizedPanel(){
        JPanel authorizedPanel = new JPanel();
        JPanel login = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pass = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel btn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        authorizedPanel.add(login);
        authorizedPanel.add(pass);
        authorizedPanel.add(btn);
        login.add(labelLogin);
        login.add(loginField);
        pass.add(labelPass);
        pass.add(passField);
        btn.add(authButton);
        return authorizedPanel;
    }

    private JPanel getBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(label);
        bottomPanel.add(message);
        bottomPanel.add(send);
        return bottomPanel;
    }

    private JPanel getRightPanel(){
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JList<String> userList = new JList<>();
        return rightPanel;
    }

    private void sendMessage(JTextField message) {
        serverService.sendMessage(message.getText());
        message.setText("");
    }

    private void sendMessage(String message) {
        serverService.sendMessage(message);
    }

    private void printToUI(JTextArea mainChat, Message message) {
        mainChat.append("\n");
        mainChat.append((message.getNick() != null ? message.getNick() : "Сервер") + " написал: " + message.getMessage());
    }
}
