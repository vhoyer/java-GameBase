package io.github.vhoyer.GameBase;

import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyEvent;

public abstract class ScreenContent {
	private Window win;

	public ScreenContent(Window win){
		this.win = win;

		setup();
	}

	public abstract void setup();
	public abstract void update(long timePassed);
	public abstract void update(Graphics2D g);

	////////////////////////////////////////
	public void mousePressed(MouseEvent e){ }
	public void mouseReleased(MouseEvent e){ }
	public void mouseClicked(MouseEvent e){ }
	public void mouseEntered(MouseEvent e){ }
	public void mouseExited(MouseEvent e){ }

	public void mouseDragged(MouseEvent e){ }
	public void mouseMoved(MouseEvent e){ }

	public void mouseWheelMoved(MouseWheelEvent e){ }

	public void keyPressed(KeyEvent e){ }
	public void keyReleased(KeyEvent e){ }
	public void keyTyped(KeyEvent e){ }
}
