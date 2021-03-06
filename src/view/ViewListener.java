package view;

import controller.ControllerInterface;
import controller.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;

/**
 * Listens for certain user actions in the Environment, primarily from the keyboard and the mouse, and then sends those actions to the controller of this game.
 */
public class ViewListener extends KeyAdapter implements MouseListener, ActionListener, ItemListener, WindowListener {
    private ControllerInterface controller;
    private Point drag_start;
    private Point drag_end;
    private int nextBoardWidth;
    private int nextBoardHeight;
    private static final List<Integer> UP = Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_W);
    private static final List<Integer> RIGHT = Arrays.asList(KeyEvent.VK_RIGHT, KeyEvent.VK_D);
    private static final List<Integer> DOWN = Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_S);
    private static final List<Integer> LEFT = Arrays.asList(KeyEvent.VK_LEFT, KeyEvent.VK_A);
    private static final List<Integer> RESTART = Arrays.asList(KeyEvent.VK_R);

    /**
     * Constructs this ViewListener and directs it to send all pertinent user input to the controller provided.
     * @param controller The controller that this ViewListener will send all pertinent user actions to.
     * @throws NullPointerException If controller == null.
     */
    public ViewListener(ControllerInterface controller) throws NullPointerException {
	this.controller = controller;
    }

    /**
     * Captures keyPressed events from the environment, interprets the events as movement directions or as a restart command, if possible, then communicates them to
     * the controller.
     * @param e The key event captured from the keyboard.
     * @see java.awt.event.KeyListener#keyPressed(KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
	if (!e.isAltDown() && !e.isControlDown() && !e.isShiftDown()) {
	    if (UP.contains(e.getKeyCode())) controller.makeMove(Direction.UP);
	    else if (RIGHT.contains(e.getKeyCode())) controller.makeMove(Direction.RIGHT);
	    else if (DOWN.contains(e.getKeyCode())) controller.makeMove(Direction.DOWN);
	    else if (LEFT.contains(e.getKeyCode())) controller.makeMove(Direction.LEFT);
	    else if (RESTART.contains(e.getKeyCode())) controller.restartGame(nextBoardWidth, nextBoardHeight);
	}
    }

    /**
     * Captures mousePressed events from the environment and saves its location on the screen.
     * @param e The mousePressed event captured from the mouse.
     * @see MouseListener#mousePressed(MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
	if (e.getButton() == MouseEvent.BUTTON1) drag_start = e.getLocationOnScreen();
    }

    /**
     * Captures mouseReleased events from the environment then communicates the direction moved between the last mousePressed event and this event to the controller,
     * unless the distance between the two events is <= 20 px.
     * @param e The mouseReleased event captured from the mouse.
     * @see MouseListener#mouseReleased(MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
	if (e.getButton() == MouseEvent.BUTTON1) {
	    drag_end = e.getLocationOnScreen();
	    if (drag_end.distance(drag_start) > 20) {
		int hor = drag_end.x - drag_start.x;
		int vert = drag_end.y - drag_start.y;
		if (Math.abs(hor) >= Math.abs(vert)) { // horizontal movement larger or equal to vertical movement
		    controller.makeMove(hor > 0 ? Direction.RIGHT : Direction.LEFT);
		} else { // horizontal movement less than vertical movement
		    controller.makeMove(vert > 0 ? Direction.DOWN : Direction.UP);
		}
	    }
	}
    }

    /**
     * Captures ActionEvents and sends their commands to the controller, if valid.
     * @param e A captured ActionEvent
     * @see ActionListener#actionPerformed(ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
	String cmd = e.getActionCommand();
	if (cmd != null && cmd.equalsIgnoreCase("restart")) controller.restartGame(nextBoardWidth, nextBoardHeight);
    }

    /**
     * Captures itemStateChanged events from JComboBoxes and saves their values until the next restart game event.
     * @param e A captured ItemEvent.
     * @see ItemListener#itemStateChanged(ItemEvent)
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
	if (e.getStateChange() != ItemEvent.SELECTED && e.getSource() instanceof JComboBox) {
	    JComboBox<?> eventSource = (JComboBox<?>) e.getSource();
	    Object item = eventSource.getSelectedItem();
	    if (item instanceof Integer && eventSource.getActionCommand().equalsIgnoreCase("width")) {
		nextBoardWidth = (int) (Integer) item;
	    } else if (item instanceof Integer && eventSource.getActionCommand().equalsIgnoreCase("height")) {
		nextBoardHeight = (int) (Integer) item;
	    }
	}
    }

    /**
     * Captures a windowClosing event and informs the controller to end the game.
     * @see WindowListener#windowClosing(WindowEvent)
     */
    @Override
    public void windowClosing(WindowEvent arg) {
	controller.endGame();
    }

    /**
     * Captures a windowClosed event and informs the controller to end the game.
     * @see WindowListener#windowClosed(WindowEvent)
     */
    @Override
    public void windowClosed(WindowEvent arg) {
	controller.endGame();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void windowActivated(WindowEvent arg) {}

    @Override
    public void windowDeactivated(WindowEvent arg) {}

    @Override
    public void windowDeiconified(WindowEvent arg) {}

    @Override
    public void windowIconified(WindowEvent arg) {}

    @Override
    public void windowOpened(WindowEvent arg) {}

}