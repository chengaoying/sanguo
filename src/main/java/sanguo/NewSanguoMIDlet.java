package sanguo;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class NewSanguoMIDlet extends MIDlet {

	private static NewSanguoMIDlet instance;

	public NewSanguoMIDlet() {
		instance = this;
	}
	
	public static NewSanguoMIDlet getInstance() {
		return instance;
	}

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {}

	protected void pauseApp() {}

	protected void startApp() throws MIDletStateChangeException {
		Display.getDisplay(this).setCurrent(NewSanguoGameEngine.instance);
		new Thread(NewSanguoGameEngine.instance).start();
	}

}
