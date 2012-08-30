package sanguo;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.ui.DrawUtil;
import cn.ohyeah.stb.ui.TextView;
import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

public class StateHelp {
	private static NewSanguoGameEngine engine = NewSanguoGameEngine.instance;
	private byte choiceIndex;
	private byte pageIndex;
	private String helpText;
	private boolean running;
	
	private void handleHelp(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (choiceIndex > 0) {
				--choiceIndex;
				pageIndex = 0;
				helpText = Resource.LoadString(Resource.STR_HELP_TEXT_PATH[choiceIndex][pageIndex]);
			}
		}
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (choiceIndex < Resource.STR_HELP_TEXT_PATH.length-1) {
				++choiceIndex;
				pageIndex = 0;
				helpText = Resource.LoadString(Resource.STR_HELP_TEXT_PATH[choiceIndex][pageIndex]);
			}
		}
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (pageIndex > 0) {
				--pageIndex;
				helpText = Resource.LoadString(Resource.STR_HELP_TEXT_PATH[choiceIndex][pageIndex]);
			}
		}
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (pageIndex < Resource.STR_HELP_TEXT_PATH[choiceIndex].length-1) {
				++pageIndex;
				helpText = Resource.LoadString(Resource.STR_HELP_TEXT_PATH[choiceIndex][pageIndex]);
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			running = false;
		}
	}
	
	private void showHelp(SGraphics g) {
		Image helpBg = Resource.loadImage(Resource.PIC_ID_HELP_BG);
		g.drawImage(helpBg, 0, 0, 20);
		
		g.setColor(0XFFFF00);
		DrawUtil.drawRect(g, 34, 90+choiceIndex*93, 120, 71, 2, 0XFFFF00);
		
		if (helpText == null) {
			helpText = Resource.LoadString(Resource.STR_HELP_TEXT_PATH[choiceIndex][pageIndex]);
		}
		g.setColor(0XFFFFFF);
		TextView.showMultiLineText(g, helpText, 1, 176, 96, 434, 370);
		
		g.setColor(0XFFFFFF);
		g.drawString((pageIndex+1)+"/"+Resource.STR_HELP_TEXT_PATH[choiceIndex].length, 517, 483, 20);
	}
	
	public void popup() {
		running = true;
		try {
			KeyState KeyState = engine.getKeyState();
			SGraphics g = engine.getSGraphics();
			while (running) {
				handleHelp(KeyState);
				if (running) {
					long t1 = System.currentTimeMillis();
					showHelp(g);
					engine.flushGraphics();
					System.gc();
					int sleepTime = (int)(125-(System.currentTimeMillis()-t1));
					if (sleepTime <= 0) {
						Thread.sleep(0);
					}
					else {
						Thread.sleep(sleepTime);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			clear();
		}
	}

	public void clear() {
		helpText = null;
		Resource.freeImage(Resource.PIC_ID_HELP_BG);
	}
	
	public void init() {
		
	}
}
