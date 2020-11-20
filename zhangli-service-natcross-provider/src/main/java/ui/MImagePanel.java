package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MImagePanel extends JPanel {

    private static final long serialVersionUID = 3602544785116642939L;
    private final ImageIcon imageIcon;

    public MImagePanel() {

        super();
        Image image = null;
        try {
            image = ImageIO.read(new File("G:\\学习\\github\\natcross2\\src\\main\\resources\\images\\intro_bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        imageIcon = new ImageIcon(image);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // TODO 自动生成的方法存根
        super.paintComponent(g);

        if (imageIcon != null) {
            float width = this.getWidth();
            float height = this.getHeight();
            int iconWidth = imageIcon.getIconWidth();
            int iconHeight = imageIcon.getIconHeight();

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.scale(width / iconWidth, height / iconHeight);
            g2d.drawImage(imageIcon.getImage(), 0, 0, null);
        }
    }

    public static void main(String[] args) {
        JFrame imageFrame = new JFrame("My Image JPanel Test!");
        MImagePanel panel = new MImagePanel();
        imageFrame.setContentPane(panel);
        imageFrame.setBounds(200, 160, 640, 380);
        imageFrame.setVisible(true);
    }
}