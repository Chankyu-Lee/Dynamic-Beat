package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DynamicBeat extends JFrame {

    private Image screenImage;
    private Graphics screenGraphic;

    private Image introBackground = new ImageIcon(Main.class.getResource("/images/introBackground.jpg")).getImage();
    private JLabel menuBar = new JLabel(new ImageIcon(Main.class.getResource("/images/menuBar.png")));
    private JButton exitButton;

    private int mouseX, mouseY;

    public DynamicBeat() throws HeadlessException {
        setUndecorated(true);
        setTitle("Dynamic Beat");
        setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setBackground(new Color(0,0,0,0));
        setLayout(null);

        menuBar.setBounds(0,0, 1280, 30);
        menuBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        menuBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - mouseX, y - mouseY);
            }
        });
        add(menuBar);

        try {
            BufferedImage exitButtonImage = ImageIO.read(Main.class.getResource("/images/exitButton.png"));
            ImageIcon defaultIcon = new ImageIcon(exitButtonImage);
            ImageIcon brightenedIcon = new ImageIcon(changeBrightness(exitButtonImage, 1.5f));

            exitButton = new JButton(defaultIcon);
            exitButton.setRolloverIcon(brightenedIcon);
            exitButton.setBounds(1250, 0, 30, 30);
            exitButton.setBorderPainted(false);
            exitButton.setContentAreaFilled(false);
            exitButton.setFocusPainted(false);
            exitButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    SE buttonEnteredSound = new SE("buttonEnteredSound.mp3", false);
                    buttonEnteredSound.start();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    SE buttonPressedSound = new SE("buttonPressedSound.mp3", false);
                    buttonPressedSound.start();
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    System.exit(0);
                }
            });
            add(exitButton);
        } catch (IOException e ) {
            e.printStackTrace();
        }

        setComponentZOrder(exitButton, 0);  // exitButton을 가장 앞으로
        setComponentZOrder(menuBar, 1);  // menuBar를 exitButton 뒤로

        Music introMusic = new Music("Summer_Peach.mp3", true);
        introMusic.start();
    }

    public void paint(Graphics g) {
        screenImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        screenGraphic = screenImage.getGraphics();
        screenDraw(screenGraphic);
        g.drawImage(screenImage, 0, 0, null);
    }

    public void screenDraw(Graphics g) {
        g.drawImage(introBackground, 0, 0, null);
        paintComponents(g);
        this.repaint();
    }

    private BufferedImage convertToARGB(BufferedImage originalImage) {
        BufferedImage newImage = new BufferedImage(
                originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        g2.drawImage(originalImage, 0, 0, null);
        g2.dispose();;
        return newImage;
    }

    private BufferedImage changeBrightness(BufferedImage original, float scaleFactor) {
        BufferedImage argbImage = convertToARGB(original);
        RescaleOp op = new RescaleOp(scaleFactor, 0, null);
        BufferedImage brightImage = new BufferedImage(
                argbImage.getWidth(), argbImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = brightImage.createGraphics();
        g2.drawImage(argbImage, op, 0, 0);
        g2.dispose();
        return brightImage;
    }
}
