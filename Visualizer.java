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
    private int connectX, connectY;
    private int currentComIndex;
    private boolean showNumbers;

    Visualizer(City city) {
        this.city = city;
        this.communities = city.getCommunities();
        this.connectX = -1;
        this.connectY = -1;

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

    /**
     * addButtons
     * makes a JPanel with buttons to edit the city
     * @return the JPanel with the buttons
     */
    private JPanel addButtons() {
        JPanel buttonPanel = new JPanel();
        JButton numberButton = new JButton("Toggle numbers");
        JButton addButton = new JButton("Add Community");
        JButton deleteButton = new JButton("Delete Community");
        JButton deleteAllButton = new JButton("Delete all");
        JButton algorithmButton = new JButton("Run algorithm");
        JButton saveButton = new JButton("Save to File");

        numberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNumbers = !showNumbers;
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                communities.add(new Community(500, 500, false,
                        new ArrayList<Integer>(Math.max(1, communities.size() - 1))));
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNumbers = true;
                city.removeCommunity(Integer.parseInt(JOptionPane.showInputDialog("Enter the number of the community:")));
            }
        });

        deleteAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showInputDialog("Type \"CONFIRM\" to confirm").equals("CONFIRM")) {
                    communities.clear();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Saves the city to a file
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
                city.locateClinics();
            }
        });

        buttonPanel.add(numberButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(deleteAllButton);
        buttonPanel.add(algorithmButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    }


    public void refresh() {
        frame.repaint();
    }

    class GridAreaPanel extends JPanel {

        private Color lightBlue = new Color(71, 114, 246);
        private Color lightGreen = new Color(112, 218, 102);
        private Color lightRed = new Color(250, 122, 122);
        public void paintComponent(Graphics g) {
            //super.repaint();

            setDoubleBuffered(true);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2.5f));
            g.setColor(Color.black);

            try {
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

                if (connectX != -1) {
                    g.setColor(Color.orange);
                    Community community = communities.get(currentComIndex);
                    g.drawLine(community.getX() + 30, community.getY() + 30,
                            connectX - 8, connectY - 68);
                }



                for (int i = 0; i < communities.size(); i ++) {
                    Community community = communities.get(i);
                    if (community.isClinic()) {
                        g.setColor(lightBlue);
                    } else if (community.isSafe()) {
                        g.setColor(lightGreen);
                    } else {
                        g.setColor(lightRed);
                    }
                    drawCircle(community.getX(), community.getY(), 60, g);
                    if (showNumbers) {
                        g.setFont(new Font("serif", Font.PLAIN, 32));
                        g.drawString(String.valueOf(i), community.getX() + 20, community.getY() + 40);
                    }
                }
            } catch (Exception e) {
                System.out.println("Node deleted during displayal");
            }


            g.setFont(new Font("serif", Font.PLAIN, 24));
            g.drawString("Left Click: Toggle clinic", 20, 25);
            g.drawString("Left Click and Drag: Move clinic", 20, 65);
            g.drawString("Right Click and Drag: Add/Remove connection ", 20, 105);
            g.setColor(lightBlue);
            drawCircle(40, 115, 40, g);
            g.drawString("= Clinic", 100, 145);
            g.setColor(lightGreen);
            drawCircle(40, 155, 40, g);
            g.drawString("= Safe community", 100, 185);
            g.setColor(lightRed);
            drawCircle(40, 195, 40, g);
            g.drawString("= Unsafe community", 100, 225);
        }

        private void drawCircle(int x, int y, int d, Graphics g) {
            g.fillOval(x, y, d, d);
            g.setColor(Color.black);
            g.drawOval(x, y, d, d);
        }
    }//end of GridAreaPanel

    class Mouse implements MouseListener, MouseMotionListener {
        private int lastX, lastY;
        private String typeClick;

        Mouse() {
            currentComIndex = -1;
            this.lastX = 0;
            this.lastY = 0;
            this.typeClick = "none";
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                for (int i = 0; i < communities.size(); i++) {
                    if (communities.get(i).collides(e.getX(), e.getY())) {
                        if (communities.get(i).isClinic()) {
                            city.removeClinic(communities.get(i));
                        } else {
                            city.addClinic(communities.get(i));

                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            for (int i = 0; i < communities.size(); i++) {
                if (communities.get(i).collides(e.getX(), e.getY())) {
                    currentComIndex = i;
                    this.lastX = e.getX();
                    this.lastY = e.getY();
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        this.typeClick = "left";
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        this.typeClick = "right";
                    }
                }
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
                    if (communities.get(currentComIndex).removeConnection(otherIndex)) {
                        communities.get(otherIndex).removeConnection(currentComIndex);
                    } else {
                        communities.get(currentComIndex).getConnections().add(otherIndex);
                        communities.get(otherIndex).getConnections().add(currentComIndex);
                    }
                    city.checkSafety(communities.get(otherIndex));
                    city.checkSafety(communities.get(currentComIndex));
                }

            }
            currentComIndex = -1;
            connectX = -1;
            connectY = -1;
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
            if (currentComIndex != -1) {
                if (this.typeClick.equals("left")) {
                    communities.get(currentComIndex).translate(e.getX() - this.lastX, e.getY() - this.lastY);
                    this.lastX = e.getX();
                    this.lastY = e.getY();
                } else if (this.typeClick.equals("right")){
                    connectX = e.getX();
                    connectY = e.getY();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

} //end of DisplayGrid