import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import static java.awt.Color.red;

public class gamepanel extends JPanel implements ActionListener {

    static final int screen_width = 600;
    static final int screen_height = 600;
    static final int unit_size = 18;
    // will control the dimensions of the objectes in the game
    static final int game_units = ((screen_width * screen_height) / unit_size);
    static final int delay = 75; // time delay
    final int[] x = new int[game_units]; // body of snake in x coord
    final int[] y = new int[game_units]; // body of snake in y coord

    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    gamepanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(screen_width, screen_height));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startgame();
    }

    public void startgame() {
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // for (int i = 0; i < screen_height / unit_size; i++) {
            // g.drawLine(i * unit_size, 0, i * unit_size, screen_height);
            // g.drawLine(0, i * unit_size, screen_width, i * unit_size);
            // } // used to make grids to make the game

            g.setColor(red);
            g.fillOval(appleX, appleY, unit_size, unit_size);

            for (int i = 0; i < bodyParts; i++) { // draw the snake
                if (i == 0) { // head of the snake
                    g.setColor(Color.yellow);
                    g.fillRect(x[i], y[i], unit_size, unit_size);
                } else {
                    g.setColor(new Color(22, 100, 7));
                    // multi colour snake
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], unit_size, unit_size);
                }
            }
            // to draw current score
            g.setColor(Color.cyan);
            g.setFont(new Font("Ink Free", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score" + applesEaten, (screen_width - metrics.stringWidth("Score : " + applesEaten)),
                    g.getFont().getSize());

        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (screen_width / unit_size)) * unit_size;
        // * by unit size to place apples evenly within the grids

        appleY = random.nextInt((int) (screen_height / unit_size)) * unit_size;

    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1]; // shifting coordinated
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - unit_size;
                break;
            case 'D':
                y[0] = y[0] + unit_size;
                break;
            case 'L':
                x[0] = x[0] - unit_size;
                break;
            case 'R':
                x[0] = x[0] + unit_size;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
            ;
        }
    }

    public void checkCollision() {
        // checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // check if head touched left border
        if (x[0] < 0) {
            running = false;
        }

        // check if head touched right border
        if (x[0] > screen_width) {
            running = false;
        }
        // if head touched top border
        if (y[0] < 0) {
            running = false;
        }
        // check if head touched bottom border
        if (y[0] > screen_height) {
            running = false;
        }
        if (!running) { // gameOver
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // to draw current score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score" + applesEaten, (screen_width - metrics1.stringWidth("Score : " + applesEaten)) / 2,
                screen_height / 3);

        // GameOver text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (screen_width - metrics2.stringWidth("Game Over ")) / 2, screen_height / 2);
    }

    public void actionPerformed(ActionEvent e) {// move function

        if (running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
