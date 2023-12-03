import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

public class Visualizer {
    private JFrame frame;
    private ArrayList<Community> communities;

    Visualizer(ArrayList<Community> communities) {
        this.communities = communities;

        this.frame = new JFrame("Communities");

        GridAreaPanel worldPanel = new GridAreaPanel();

        frame.getContentPane().add(BorderLayout.CENTER, worldPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);
    }


    public void refresh() {
        frame.repaint();
    }

    class GridAreaPanel extends JPanel {
        public void paintComponent(Graphics g) {
            //super.repaint();

            setDoubleBuffered(true);


            for (int i = 0; i < communities.size(); i ++) {
                Community community = communities.get(i);
                if (community.isClinic()) {
                    g.setColor(Color.blue);
                } else if (community.isSafe()) {
                    g.setColor(new Color(112, 218, 102));
                } else {
                    g.setColor(new Color(250, 122, 122));
                }
                g.fillOval(community.getX(), community.getY(), 50, 50);
                g.setColor(Color.black);
                g.drawOval(community.getX(), community.getY(), 50, 50);
                g.setFont(new Font("serif", Font.PLAIN, 24));
                g.drawString(String.valueOf(i), community.getX() + 20, community.getY() + 30);
            }
        }
    }//end of GridAreaPanel

} //end of DisplayGrid



