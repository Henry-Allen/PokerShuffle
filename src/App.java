public class App {
    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new CardShufflerFrame().setVisible(true);
        });
    }
}

class CardShufflerFrame extends javax.swing.JFrame {
    private static final java.awt.Color TABLE_GREEN = new java.awt.Color(0, 102, 0);
    private static final int COLS = 13;
    private static final int ROWS = 4;

    // Target display size for each card
    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 116;

    private final javax.swing.JPanel gridPanel = new javax.swing.JPanel();
    private final java.util.List<javax.swing.JLabel> cardLabels = new java.util.ArrayList<>();
    private final java.util.List<javax.swing.ImageIcon> cardIcons = new java.util.ArrayList<>();

    CardShufflerFrame() {
        super("Poker Shuffle");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(new java.awt.BorderLayout(10, 10));
        getContentPane().setBackground(TABLE_GREEN);

        // Center panel: grid of cards
        gridPanel.setLayout(new java.awt.GridLayout(ROWS, COLS, 8, 8));
        gridPanel.setBackground(TABLE_GREEN);
        gridPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Bottom panel: controls
        javax.swing.JPanel controls = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
        controls.setBackground(TABLE_GREEN);
        javax.swing.JButton shuffleBtn = new javax.swing.JButton("Shuffle");
        shuffleBtn.addActionListener(e -> shuffleCards());
        controls.add(shuffleBtn);

        add(gridPanel, java.awt.BorderLayout.CENTER);
        add(controls, java.awt.BorderLayout.SOUTH);

        loadCards();
        layoutCardsInOrder();

        pack();
        setLocationRelativeTo(null);
    }

    private void loadCards() {
        cardLabels.clear();
        cardIcons.clear();

        java.nio.file.Path dir = java.nio.file.Paths.get("cards");
        if (!java.nio.file.Files.isDirectory(dir)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Cards directory not found: " + dir.toAbsolutePath(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (java.util.stream.Stream<java.nio.file.Path> stream = java.nio.file.Files.list(dir)) {
            java.util.List<java.nio.file.Path> files = stream
                .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".png"))
                .sorted((a, b) -> a.getFileName().toString().compareToIgnoreCase(b.getFileName().toString()))
                .toList();

            if (files.size() != 52) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Expected 52 card images in '" + dir.toAbsolutePath() + "' but found " + files.size() + ".",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            for (java.nio.file.Path p : files) {
                javax.swing.ImageIcon icon = scaleIcon(new javax.swing.ImageIcon(p.toString()), CARD_WIDTH, CARD_HEIGHT);
                cardIcons.add(icon);
                javax.swing.JLabel lbl = new javax.swing.JLabel(icon);
                lbl.setOpaque(false);
                lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                lbl.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
                cardLabels.add(lbl);
            }
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
    }

    private void layoutCardsInOrder() {
        gridPanel.removeAll();
        for (javax.swing.JLabel lbl : cardLabels) {
            gridPanel.add(lbl);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void shuffleCards() {
        java.util.List<javax.swing.JLabel> shuffled = new java.util.ArrayList<>(cardLabels);
        java.util.Collections.shuffle(shuffled);
        gridPanel.removeAll();
        for (javax.swing.JLabel lbl : shuffled) {
            gridPanel.add(lbl);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private static javax.swing.ImageIcon scaleIcon(javax.swing.ImageIcon src, int width, int height) {
        java.awt.Image img = src.getImage();
        java.awt.Image scaled = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new javax.swing.ImageIcon(scaled);
    }
}
