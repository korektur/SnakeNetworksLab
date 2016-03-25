import java.awt.EventQueue;
import javax.swing.*;


public class Snake extends JFrame {

    private Snake() {
        Board board = new Board();
        add(board);
        setResizable(false);
        pack();

        setTitle("Network snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}