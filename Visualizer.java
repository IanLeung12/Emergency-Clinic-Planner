import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;;
import javax.swing.*;
import java.awt.*;


public class Visualizer {
    private JFrame frame;
    private City city;
    private ArrayList<Community> communities;
    private String currentAction = "move";

    Visualizer(City city) {
        this.city = city;
        this.communities = city.getCommunities();

        this.frame = new JFrame("Communities");


        Mouse mouse = new Mouse();

        JPanel buttonPanel = addButtons();
        frame.addMouseListener(mouse);
        frame.addMouseMotionListener(mouse);
        frame.getContentPane().add(BorderLayout.NORTH, buttonPanel);
        frame.getContentPane().add(BorderLayout.CENTER, new GridAreaPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);
    }

    private JPanel addButtons() {
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Community");
        JButton deleteButton = new JButton("Delete Community");
        JButton editButton = new JButton("Edit Community");
        JButton algorithmButton = new JButton("Run algorithm");
        JButton saveButton = new JButton("Save to File");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentAction = "add";
                frame.repaint();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentAction = "delete";
                frame.repaint();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentAction = "edit";
                frame.repaint();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane.showInputDialog("What is the name of your file (without .txt)");
                try {
                    PrintWriter output = new PrintWriter(new File(fileName + ".txt"));
                    for (int i = 0; i < communities.size(); i ++) {
                        output.println(communities.get(i).toString());
                    }
                    output.close();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        algorithmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                city.addClinics();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(algorithmButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    }


    public void refresh() {
        frame.repaint();
    }

    class GridAreaPanel extends JPanel {
        public void paintComponent(Graphics g) {
            //super.repaint();

            setDoubleBuffered(true);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(3f));
            g2d.setColor(Color.black);
            for (int i = 0; i < communities.size(); i ++) {
                Community community = communities.get(i);
                ArrayList<Integer> connections = community.getConnections();
                for (int c = 0; c < connections.size(); c ++) {
                    if (connections.get(c) > i) {
                        Community other = communities.get(connections.get(c));
                        // Draw a line between the centers of the connected communities
                        g.drawLine(
                                community.getX() + 30, community.getY() + 30,
                                other.getX() + 30, other.getY() + 30
                        );
                    }
                }
            }

            for (int i = 0; i < communities.size(); i ++) {
                Community community = communities.get(i);
                if (community.isClinic()) {
                    g.setColor(Color.blue);
                } else if (community.isSafe()) {
                    g.setColor(new Color(112, 218, 102));
                } else {
                    g.setColor(new Color(250, 122, 122));
                }
                g.fillOval(community.getX(), community.getY(), 60, 60);
                g.setColor(Color.black);
                g.drawOval(community.getX(), community.getY(), 60, 60);
                g.setFont(new Font("serif", Font.PLAIN, 32));
                g.drawString(String.valueOf(i), community.getX() + 20, community.getY() + 40);

                g.setColor(Color.gray); // Set the color for connections


            }


        }
    }//end of GridAreaPanel

    class Mouse implements MouseListener, MouseMotionListener {
        private int currentComIndex;
        private int lastX, lastY;
        private String typeClick;

        Mouse() {
            this.currentComIndex = -1;
            this.lastX = 0;
            this.lastY = 0;
            this.typeClick = "none";
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {

            for (int i = 0; i < communities.size(); i++) {
                if (communities.get(i).collides(e.getX(), e.getY())) {
                    this.currentComIndex = i;
                    this.lastX = e.getX();
                    this.lastY = e.getY();
                }
            }
            if (e.getButton() == MouseEvent.BUTTON1) {
                this.typeClick = "left";
                System.out.println("left");
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                this.typeClick = "right";
                System.out.println("right");
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (this.typeClick == "right") {
                int otherIndex = -1;
                for (int i = 0; i < communities.size(); i++) {
                    if (communities.get(i).collides(e.getX(), e.getY())) {
                        otherIndex = i;
                    }
                }
                if (otherIndex != -1) {
                    communities.get(this.currentComIndex).getConnections().add(otherIndex);
                    communities.get(otherIndex).getConnections().add(this.currentComIndex);
                }
            }
            this.currentComIndex = -1;
            this.typeClick = "none";
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (this.currentComIndex != -1) {
                if (this.typeClick.equals("left")); {
                    communities.get(this.currentComIndex).translate(e.getX() - this.lastX, e.getY() - this.lastY);
                    this.lastX = e.getX();
                    this.lastY = e.getY();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

} //end of DisplayGrid



