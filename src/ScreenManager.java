package io.github.vhoyer.GameBase;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;

public class ScreenManager {
	private GraphicsDevice vc;
	private DisplayMode compatibleDisplayMode;

	public ScreenManager(){
		init();
	}

	public void init(){
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = e.getDefaultScreenDevice();
	}

	public DisplayMode[] getCompatibleDisĺayMode(){
		return vc.getDisplayModes();
	}

	public DisplayMode findFirsCompatibleMode(DisplayMode[] modes){
		DisplayMode goodModes[] = vc.getDisplayModes();
		for(int x=0; x<modes.length; x++){
			for(int y=0;y<goodModes.length; y++){
				if (displayModesMatch(modes[x], goodModes[y])){
					return modes[x];
				}
			}
		}
		return null;
	}

	public DisplayMode getCurrentDisplayMode(){
		return vc.getDisplayMode();
	}

	public boolean displayModesMatch(DisplayMode m1, DisplayMode m2){
		if ((m1.getWidth() != m2.getWidth()) || (m1.getHeight() != m2.getHeight())){
			return false;
		}
		if (m1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && m2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && m1.getBitDepth() != m2.getBitDepth()){
			return false;
		}
		if (m1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && m2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && m1.getRefreshRate() != m2.getRefreshRate()){
			return false;
		}
		return true;
	}

	public void setFullScreen(DisplayMode dm){
		compatibleDisplayMode = dm;
		JFrame f = new JFrame();
		f.setUndecorated(true);
		f.setIgnoreRepaint(true);
		//f.setResizable(false);
		vc.setFullScreenWindow(f);

		if(dm != null && vc.isDisplayChangeSupported()){
			try{
				vc.setDisplayMode(dm);
			}catch(Exception e) { }
		}
		f.createBufferStrategy(5);
	}

	public Graphics2D getGraphics(){
		for(int i = 0; i < 5; i++){
			Window w = getFullScreenWindow(compatibleDisplayMode);
			if(w != null){
				BufferStrategy s = w.getBufferStrategy();
				return (Graphics2D)s.getDrawGraphics();
			}
			init();
		}

		System.out.println("getGraphics: window is null");
		throw new java.lang.NullPointerException();
	}

	public void update(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			BufferStrategy s = w.getBufferStrategy();
			if(!s.contentsLost()){
				s.show();
			}
		}
	}

	public synchronized Window getFullScreenWindow(DisplayMode dm){
		Window result = vc.getFullScreenWindow();

		int i = 0;
		while(result == null && i < 5){
			i++;

			init();
			setFullScreen(dm);
			result = vc.getFullScreenWindow();
		}

		if(result != null){
			return result;
		}

		System.out.println("getFullScreenWindow: window is null");
		throw new java.lang.NullPointerException();
	}

	public int getWidth(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			return w.getWidth();
		}
		return 0;
	}

	public int getHeight(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			return w.getHeight();
		}
		return 0;
	}

	public synchronized void restoreScreen(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			w.dispose();
		}
		vc.setFullScreenWindow(null);
	}

	public BufferedImage createCompatibleImage(int w, int h, int t){
		Window win = vc.getFullScreenWindow();
		if(win != null){
			GraphicsConfiguration gc = win.getGraphicsConfiguration();
			return gc.createCompatibleImage(w, h, t);
		}
		return null;
	}

	public Cursor blankCursor(){
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		return Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank");
	}
}
