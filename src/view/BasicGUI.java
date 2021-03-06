package view;

import controller.ATileMove;
import controller.ControllerInterface;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A visualization of the game.
 */
public class BasicGUI extends JFrame implements ViewInterface {
    private static final long serialVersionUID = 4082949622480526528L;
    private static final int MINSIZE = 4, MAXSIZE = 10;
    private static final int containerSpacing = 10, taskBarSize = 75;
    private static final String gameName = "2048";
    private static final Color backgroundColor = new Color(0, 248, 239);
    private static final Color defaultTextColor = new Color(119, 110, 101);
    private static final Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
    private ViewListener listener;
    private JLabel score, bestScore;
    private Font boldFont, font;
    private JPanel heading;
    private Box above;
    private JButton newGame;
    private ViewBoard viewBoard;
    private JPanel comboBoxes;
    private JPanel below;
    private JLabel instruction;

    /**
     * Creates a GUI for a game with a given number of horizontal and vertical tiles for a given controller.
     * @param numTilesX The number of horizontal tiles.
     * @param numTilesY The number of vertical tiles.
     * @param controller The controller instance controlling this game.
     * @throws IllegalArgumentException If numTilesX < 4 or numTilesX > 20 or numTilesY < 4 or numTilesY > 20.
     * @throws NullPointerException If controller == null.
     */
    public BasicGUI(int numTilesX, int numTilesY, ControllerInterface controller) throws IllegalArgumentException, NullPointerException {
	super(gameName);
	if (numTilesX < MINSIZE || numTilesY < MINSIZE || numTilesX > MAXSIZE || numTilesY > MAXSIZE) throw new IllegalArgumentException();
	listener = new ViewListener(controller); // can throw NullPointerException if controller == null
	ClearSansLoader csl = new ClearSansLoader();
	boldFont = csl.importFont("Bold");
	font = csl.importFont("Regular");
	csl = null;
	createAndShowGUI(numTilesX, numTilesY);
    }

    private synchronized void createAndShowGUI(int numTilesX, int numTilesY) {
	// create a container to hold the title as well as the score and best score
	heading = new JPanel();
	heading.setLayout(new BoxLayout(heading, BoxLayout.X_AXIS));
	heading.setBackground(backgroundColor);
	heading.setAlignmentX(CENTER_ALIGNMENT);
	JLabel title = new JLabel(gameName);
	title.setFont(boldFont.deriveFont(80f));
	title.setForeground(defaultTextColor);
	title.setBackground(backgroundColor);
	heading.add(title);
	heading.add(Box.createHorizontalGlue());
	JPanel scoresPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); // a panel to hold both the score and the best score
	scoresPanel.setBackground(backgroundColor);
	JPanel scorePanel = new someScorePanel("SCORE"); // custom made panel to hold the score
	JPanel bestPanel = new someScorePanel("BEST"); // custom made panel to hold the bestScore
	scoresPanel.add(scorePanel);
	scoresPanel.add(bestPanel);
	heading.add(scoresPanel);

	// create a container to hold a basic message to the player and a "start new game" button.
	above = new Box(BoxLayout.X_AXIS);
	JLabel msg = new JLabel("<html>Merge same numbers together to reach <b>2048!</b></html>");
	msg.setFont(font.deriveFont(18f));
	msg.setForeground(defaultTextColor);
	above.add(msg);
	above.add(Box.createHorizontalGlue());
	newGame = new JButton("New Game");
	newGame.setBorderPainted(false);
	newGame.setFocusable(false);
	newGame.setFont(boldFont.deriveFont(18f));
	newGame.setForeground(new Color(249, 246, 242));
	newGame.setBackground(new Color(230, 122, 102));
	newGame.setActionCommand("restart");
	newGame.addActionListener(listener);
	above.add(newGame);

	// Create a container to group 2 combo boxes to allow user to set the size of the board upon game restart
	comboBoxes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
	comboBoxes.setBackground(backgroundColor);
	JLabel numTilesXLabel = new JLabel("Horizontal Tiles:");
	numTilesXLabel.setFont(boldFont.deriveFont(18f));
	numTilesXLabel.setForeground(defaultTextColor);
	JLabel numTilesYLabel = new JLabel("Vertical Tiles:");
	numTilesYLabel.setFont(boldFont.deriveFont(18f));
	numTilesYLabel.setForeground(defaultTextColor);
	JComboBox<Integer> numTilesXBox = new JComboBox<Integer>();
	numTilesXBox.setActionCommand("width");
	numTilesXBox.setFont(boldFont.deriveFont(18f));
	numTilesXBox.setForeground(defaultTextColor);
	numTilesXBox.setBackground(backgroundColor);
	numTilesXBox.setFocusable(false);
	for (int x = MINSIZE; x <= MAXSIZE; x++)
	    numTilesXBox.addItem(x);
	numTilesXBox.addItemListener(listener);
	numTilesXBox.setSelectedIndex(numTilesXBox.getItemCount() - 1);// sets to last element then moves it to the correct selection
	numTilesXBox.setSelectedItem(numTilesX);
	JComboBox<Integer> numTilesYBox = new JComboBox<Integer>();
	numTilesYBox.setActionCommand("height");
	numTilesYBox.setFont(boldFont.deriveFont(18f));
	numTilesYBox.setForeground(defaultTextColor);
	numTilesYBox.setBackground(backgroundColor);
	numTilesYBox.setFocusable(false);
	for (int y = MINSIZE; y <= MAXSIZE; y++)
	    numTilesYBox.addItem(y);
	numTilesYBox.addItemListener(listener);
	numTilesYBox.setSelectedIndex(numTilesYBox.getItemCount() - 1);// sets to last element then moves it to the correct selection
	numTilesYBox.setSelectedItem(numTilesY);
	// Add labels and combo boxes to the container
	comboBoxes.add(numTilesXLabel);
	comboBoxes.add(numTilesXBox);
	comboBoxes.add(numTilesYLabel);
	comboBoxes.add(numTilesYBox);

	// create a container to hold some basic instructions for the game.
	below = new JPanel();
	below.setLayout(new BoxLayout(below, BoxLayout.Y_AXIS));
	below.setAlignmentX(CENTER_ALIGNMENT);
	below.setBackground(backgroundColor);
	instruction = new JLabel();
	StringBuilder instructions = new StringBuilder("<html><b>HOW TO PLAY:</b> Use your <b>arrow keys</b> or <b>WASD keys</b> to move the tiles. When two tiles");
	instructions.append(" with the same number touch, they merge into one! <b>Restart by pressing r on the keyboard.</b></html>");
	instruction.setText(instructions.toString());
	instruction.setFont(font.deriveFont(18f));
	instruction.setForeground(defaultTextColor);
	instruction.setAlignmentX(CENTER_ALIGNMENT);
	// Add instructions to the below container
	below.add(instruction);

	// create a board to hold the tiles and set its maximum size
			viewBoard = new ViewBoard();
	// Now determine the maximum size that the actual game board can be based on screen size and set the sizes of the other components
	buildBoardAndSetSizes(numTilesX, numTilesY);

	// create a container to align everything properly.
	Box box = new Box(BoxLayout.Y_AXIS);
	box.setAlignmentX(CENTER_ALIGNMENT);
	box.add(Box.createVerticalGlue()); // padding above the board
	box.add(heading);
	box.add(Box.createVerticalStrut(containerSpacing));
	box.add(above);
	box.add(Box.createVerticalStrut(containerSpacing));
	box.add(viewBoard);
	box.add(Box.createVerticalStrut(containerSpacing));
	box.add(comboBoxes);
	box.add(Box.createVerticalStrut(containerSpacing));
	box.add(below);
	box.add(Box.createVerticalGlue()); // padding below the board

	addKeyListener(listener);
	addMouseListener(listener);
	addWindowListener(listener);
	setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
	add(box);
	getContentPane().setBackground(backgroundColor);
	pack();
	setMinimumSize(new Dimension(getSize().width + 10, getSize().height + 10));
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setResizable(true);
	setLocationRelativeTo(null); // center on the screen
	setVisible(true);
    }

    private void buildBoardAndSetSizes(int numTilesX, int numTilesY) {
	int maxBoardWidth = scrnSize.width - taskBarSize;
	int boardWidth = viewBoard.setHorizontalConstraints(numTilesX, maxBoardWidth);
	heading.setPreferredSize(new Dimension(boardWidth, heading.getPreferredSize().height));
	heading.setMaximumSize(heading.getPreferredSize());
	above.setPreferredSize(new Dimension(boardWidth, above.getPreferredSize().height));
	above.setMaximumSize(above.getPreferredSize());
	comboBoxes.setPreferredSize(new Dimension(boardWidth, comboBoxes.getPreferredSize().height));
	comboBoxes.setMaximumSize(comboBoxes.getPreferredSize());
	javax.swing.text.View vw = (javax.swing.text.View) instruction.getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey);
	vw.setSize(boardWidth, 0);
	float w = vw.getPreferredSpan(javax.swing.text.View.X_AXIS);
	float h = vw.getPreferredSpan(javax.swing.text.View.Y_AXIS);
	below.setPreferredSize(new Dimension((int) Math.ceil(w), (int) Math.ceil(h)));
	below.setMaximumSize(below.getPreferredSize());
	int maxBoardHeight = scrnSize.height - heading.getPreferredSize().height - above.getPreferredSize().height - comboBoxes.getPreferredSize().height
		- below.getPreferredSize().height - 4 * containerSpacing - taskBarSize;
			viewBoard.setVerticalConstraints(numTilesY, maxBoardHeight);
			viewBoard.createGrid();
    }

    @Override
    public synchronized void resetBoard(int numTilesX, int numTilesY) throws IllegalArgumentException {
	if (numTilesX < MINSIZE || numTilesY < MINSIZE || numTilesX > MAXSIZE || numTilesY > MAXSIZE) throw new IllegalArgumentException();
			viewBoard.resetBoard();
	buildBoardAndSetSizes(numTilesX, numTilesY);
	setMinimumSize(new Dimension(0, 0));
	pack();
	setMinimumSize(new Dimension(getSize().width + 10, getSize().height + 10));
	setLocationRelativeTo(null);
    }

    @Override
    public synchronized void moveTiles(List<ATileMove> moves) {
			viewBoard.moveTiles(moves);
	revalidate();
    }

    @Override
    public synchronized void addTiles(List<ATileMove> newTiles) {
			viewBoard.addTiles(newTiles);
    }

    @Override
    public synchronized void showWin() {
	JOptionPane.showMessageDialog(null, "Congratulations, you have won the game!");
    }

    @Override
    public synchronized void showLoss() {
	String[] options = { "Start New Game", "Exit" };
	int i = JOptionPane.showOptionDialog(null, "You lose", "Game Over!", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	if (i == 0) {
	    newGame.doClick();
	}
	if (i == 1) {
	    dispose();
	}
    }

    @Override
    public synchronized void showScore(int score, int bestScore) {
	this.score.setText(String.valueOf(score));
	this.bestScore.setText(String.valueOf(bestScore));
    }

    private class someScorePanel extends JPanel {
	private static final long serialVersionUID = -8332252455499210670L;

	someScorePanel(String name) {
	    super(new FlowLayout(FlowLayout.CENTER, 20, 20));
	    setBackground(new Color(187, 173, 160));

	    JLabel scoreHeader = new JLabel(name);
	    scoreHeader.setAlignmentX(CENTER_ALIGNMENT);
	    scoreHeader.setFont(boldFont.deriveFont(18f));
	    scoreHeader.setForeground(new Color(238, 228, 218));

	    JLabel scoreValue = new JLabel("4096");
	    scoreValue.setAlignmentX(CENTER_ALIGNMENT);
	    scoreValue.setFont(boldFont.deriveFont(25f));
	    scoreValue.setForeground(Color.WHITE);

	    if (name.equals("SCORE")) score = scoreValue;
	    else if (name.equals("BEST")) bestScore = scoreValue;

	    Box box = new Box(BoxLayout.Y_AXIS);
	    box.add(Box.createVerticalStrut(8));
	    box.add(scoreHeader);
	    box.add(scoreValue);
	    box.add(Box.createVerticalStrut(8));
	    add(box);
	    setPreferredSize(new Dimension(100, 115));
	}

	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    CustomPainter.paintComponentRounded(this, g, 3);
	}
    }

}