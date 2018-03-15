package Go;

import java.util.Random;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import Go.State;

/**
 * Provides I/O.
 * 
 *
 */
public class RandomAI extends JPanel {

    int gridSize;
    int numberOfTiles;
    int tileSize;
    int borderSize;

    private State current_player;
    private Grid grid;
    private Point lastMove;
    public boolean surrender = false;
    public RandomAI(int x, int y, JTextArea textArea) {

        gridSize = x;
        numberOfTiles = gridSize - 1;
        tileSize = 40;
        borderSize = tileSize;
         
    	this.setBackground(Color.ORANGE);
    	grid = new Grid(x, y);
    	// Black always starts
    	current_player = State.BLACK;
    	this.addMouseListener(new MouseAdapter() {

            @Override
    	    public void mouseReleased(MouseEvent e) {

                // Converts to float for float division and then rounds to
                // provide nearest intersection.
                if (!surrender) {
		if (current_player == State.BLACK) {
                	int row = Math.round((float) (e.getY() - borderSize)
    				 / tileSize);
                	int col = Math.round((float) (e.getX() - borderSize)
    				 / tileSize);
                	System.out.println("row index: " + row);
                	System.out.println("col index: " + col);

                	Point newPoint = new Point(col,row);
		} else {
			Random rand = new Random();
			int row = rand.nextInt(gridSize + 1);
			int col = rand.nextInt(gridSize + 1);

			Point newPoint = new Point(col,row);
		}

                // Check wherever it's valid
                if (newPoint.getY() >= gridSize || newPoint.getX() >= gridSize || newPoint.getY() < 0 || newPoint.getX() < 0) {
                    return;
                }

                //check if each rule is not being violated
                if(grid.isMoveAllowed(new Stone(newPoint,current_player,0),current_player)){
                    grid.addStone(newPoint, current_player);

                    textArea.append("\nMove number: " + grid.getMoveNumber());
                    textArea.append("\nBlack score: " + grid.getBlackScore());
                    textArea.append("\nWhite score: " + grid.getWhiteScore() + "\n");

                    lastMove = new Point(col, row);

                        // Switch current player
                        if (current_player == State.BLACK) {
                            current_player = State.WHITE;
                        } else {
                            current_player = State.BLACK;
                        }
                }
                }
                repaint();
            }
	    });
    }

@Override
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setColor(Color.BLACK);
    // Draw rows.
    for (int i = 0; i < gridSize; i++) {
        g2.drawLine(borderSize, i * tileSize + borderSize, tileSize
		    * numberOfTiles + borderSize, i * tileSize + borderSize);
    }
    // Draw columns.
    for (int i = 0; i < gridSize; i++) {
        g2.drawLine(i * tileSize + borderSize, borderSize, i * tileSize
		    + borderSize, tileSize * numberOfTiles + borderSize);
    }
    // Iterate over intersections
    for (int row = 0; row < gridSize; row++) {
        for (int col = 0; col < gridSize; col++) {
            State state = grid.getState(col,row);
            if (state != null) {
                if (state == State.BLACK) {
                    g2.setColor(Color.BLACK);
                } else {
                    g2.setColor(Color.WHITE);
                }
                g2.fillOval(col * tileSize + borderSize - tileSize / 2,
			    row * tileSize + borderSize - tileSize / 2,
			    tileSize, tileSize);
            }
        }
    }
    // Highlight last move
    if (lastMove != null) {
        g2.setColor(Color.RED);
        g2.drawOval(lastMove.x * tileSize + borderSize - tileSize / 2,
		    lastMove.y * tileSize + borderSize - tileSize / 2,
		    tileSize, tileSize);
    }
}

@Override
    public Dimension getPreferredSize() {
    return new Dimension(numberOfTiles * tileSize + borderSize * 2,
			 numberOfTiles * tileSize + borderSize * 2);
}

}
