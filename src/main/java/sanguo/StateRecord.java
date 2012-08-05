package sanguo;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import cn.ohyeah.itvgame.model.GameRecord;
import cn.ohyeah.itvgame.model.GameRecordDesc;
import cn.ohyeah.stb.ui.DrawUtil;
import cn.ohyeah.stb.ui.PopupConfirm;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.ui.ImageUtil;
import cn.ohyeah.stb.game.ServiceWrapper;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

public class StateRecord {
	
	public static final byte TYPE_LOAD = 0;
	public static final byte TYPE_SAVE = 1;
	
	private static final byte STATE_FETCH = 0;
	private static final byte STATE_LIST = 1;
	
	private static final byte MAX_RECORD_COUNT = 4;
	
	private static NewSanguoGameEngine engine = NewSanguoGameEngine.instance;
	
	private Record[] recordList = new Record[MAX_RECORD_COUNT];
	private Image smallSeigneurHead[] = new Image[Resource.STR_SEIGNEUR_NAME.length];
	private byte result;
	private byte curRecId;
	private byte type;
	private byte state;
	private byte curGroupIndex;
	private boolean back;
	
	public void loadRecord(int recordId) {
		PopupText pt = Resource.buildPopupText();
		try {
			ServiceWrapper sw = engine.getServiceWrapper();
			GameRecord gameRecord = sw.readRecord(recordId);
			if (sw.isServiceSuccessful()) {
				if (gameRecord != null) {
					int rst = engine.setSaveData(gameRecord.getData());
					if (rst == 0) {
						Record record = recordList[recordId];
						record.setRecordTime(gameRecord.getTime());
						record.setRemark(gameRecord.getRemark());
						engine.playDuration = gameRecord.getPlayDuration();
						engine.gameStartMillis = System.currentTimeMillis();
						
						pt.setText("读取存档成功");
						pt.popup();
						result = 0;
						back = true;
					}
					else {
						pt.setText("读取存档失败, 数据格式错误");
						pt.popup();
						result = -2;
					}
				}
				else {
					pt.setText("读取存档失败, 原因: 没有对应的存档");
					pt.popup();
					result = -2;
				}
			}
			else {
				pt.setText("读取存档失败, 原因: "+sw.getServiceMessage());
				pt.popup();
				result = -2;
			}
		} catch (Exception e) {
			pt.setText("读取存档失败, 原因: "+e.getMessage());
			pt.popup();
			result = -2;
		}
	}
	
	public ServiceWrapper execSaveRecord(int recordId) throws IOException {
		int seigneurId = engine.playerSeigneur;
		Record record = new Record();
		record.setId(recordId);
		record.setValid(true);
		record.setSeigneurId(seigneurId);
		record.setGameTime(engine.month);
		record.setGeneralCount(engine.getSeigneurGeneralCount(seigneurId));
		record.setSoldierCount(engine.getSeigneurSoldierCount(seigneurId));
		record.setCityCount(engine.getSeigneurCityCount(seigneurId));
		record.setWealth(engine.getSeigneurWealth(seigneurId));
		record.setProvisions(engine.getSeigneurProvisions(seigneurId));
		
		GameRecord gameRecord = new GameRecord();
		gameRecord.setRecordId(record.getId());
		gameRecord.setRemark(record.getRemark());
		gameRecord.setData(engine.getSaveData());
		
		int curPlayDuration = (int)(System.currentTimeMillis()-engine.gameStartMillis)/1000;
		int playDuration = engine.playDuration+curPlayDuration;
		gameRecord.setPlayDuration(playDuration);

		ServiceWrapper sw = engine.getServiceWrapper();
		sw.saveRecord(gameRecord);
		if (sw.isServiceSuccessful()) {
			engine.pushProp();
			recordList[recordId] = record;
		}
		return sw;
	}
	
	public void saveRecord(int recordId) {
		PopupText pt = Resource.buildPopupText();
		try {
			ServiceWrapper sw = execSaveRecord(recordId);
			if (sw.isServiceSuccessful()) {
				pt.setText("保存存档成功");
				pt.popup();
				result = 0;
				back = true;
			}
			else {
				pt.setText("保存存档失败, 原因: "+sw.getServiceMessage());
				pt.popup();
				result = -2;
			}
		} catch (Exception e) {
			pt.setText("保存存档失败, 原因: "+e.getMessage());
			pt.popup();
			result = -2;
		}
	}
	
	public void handle(KeyState KeyState) {
		switch(state) {
		case STATE_FETCH: break;
		case STATE_LIST: 
			handleList(KeyState);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}

	private void handleList(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (curGroupIndex == 0) {
				if (curRecId > 0) {
					--curRecId;
				}
			}
			else {
				curGroupIndex = 0;
			}
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (curGroupIndex == 0) {
				if (curRecId < MAX_RECORD_COUNT-1) {
					++curRecId;
				}
				else {
					curGroupIndex = 1;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			if (curGroupIndex == 0) {
				if (type == TYPE_LOAD) {
					if (recordList[curRecId]!=null && recordList[curRecId].isValid()) {
						PopupText pt = Resource.buildPopupTextWithoutBtn();
						pt.setText("正在读取存档......");
						pt.show(engine.getGraphics());
						engine.flushGraphics();
						loadRecord(curRecId);
					}
				}
				else {
					boolean isSave = true;
					if (recordList[curRecId]!=null && recordList[curRecId].isValid()) {
						PopupConfirm pc = Resource.buildPopupConfirm();
						pc.setText("此位置已有存档，是否覆盖原来的存档");
						if (pc.popup() != 0) {
							isSave = false;
						}
					}
					if (isSave) {
						PopupText pt = Resource.buildPopupTextWithoutBtn();
						pt.setText("正在保存存档......");
						pt.show(engine.getGraphics());
						engine.flushGraphics();
						saveRecord(curRecId);
					}
				}
			}
			else {
				result = -1;
				back = true;
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			result = -1;
			back = true;
		}
	}

	public void show(Graphics g) {
		switch(state) {
		case STATE_FETCH: 
			showFetch(g);
			break;
		case STATE_LIST: 
			showList(g);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	
	private Image loadSeigneurSmallHead(int seigneurId) {
		if (smallSeigneurHead[seigneurId] == null) {
			Image normalHead = Resource.loadImage(Resource.PIC_ID_SEIGNEUR_HEAD);
			int headW = normalHead.getWidth()/Resource.STR_SEIGNEUR_NAME.length;
			smallSeigneurHead[seigneurId] = ImageUtil.zoomImage(normalHead, headW*seigneurId, 
					0, headW, normalHead.getHeight(), 45, 45);
		}
		return smallSeigneurHead[seigneurId];
	}
	
	private void showRecordBg(Graphics g) {
		Image title = null;
		if (type == TYPE_LOAD) {
			title = Resource.loadImage(Resource.PIC_ID_RECORD_LOAD_TITLE);
		}
		else {
			title = Resource.loadImage(Resource.PIC_ID_RECORD_SAVE_TITLE);
		}
		int bgX = Resource.X_RECORD_BG;
		int bgY = Resource.Y_RECORD_BG;
		g.drawImage(title, bgX, bgY, 20);
		
		int bodyX = bgX;
		int bodyY = bgY+title.getHeight();;
		
		Image body = Resource.loadImage(Resource.PIC_ID_RECORD_BG_BODY);
		int bodyH = body.getHeight();
		
		for (int i = 0; i < MAX_RECORD_COUNT; ++i) {
			g.drawImage(body, bodyX, bodyY, 20);
			bodyY += bodyH;
		}
		g.drawImage(body, bodyX, bodyY, 20);
		bodyY += bodyH;
		g.drawRegion(body, 0, bodyH-(bodyH>>1), body.getWidth(), bodyH>>1, 0, bodyX, bodyY, 20);
	}
	
	private void showReocrdItems(Graphics g) {
		int bgX = Resource.X_RECORD_BG;
		int bgY = Resource.Y_RECORD_BG;
		Image title = null;
		if (type == TYPE_LOAD) {
			title = Resource.loadImage(Resource.PIC_ID_RECORD_LOAD_TITLE);
		}
		else {
			title = Resource.loadImage(Resource.PIC_ID_RECORD_SAVE_TITLE);
		}
		int bodyX = bgX;
		int bodyY = bgY+title.getHeight();;
		
		Image body = Resource.loadImage(Resource.PIC_ID_RECORD_BG_BODY);
		int bodyW = body.getWidth();
		int bodyH = body.getHeight();
		
		Image itemBg = Resource.loadImage(Resource.PIC_ID_RECORD_ITEM_BG);
		int itemH = itemBg.getHeight();
		int itemW = itemBg.getWidth();
		int itemYOffset = (bodyH-itemH)>>1;
		int itemXOffset = (bodyW-itemW)>>1;
		int itemX = 0, itemY = 0;
		
		int sx = 0, sy = 0;
		String ss = null;
		itemX = bodyX+itemXOffset;
		for (int i = 0; i < MAX_RECORD_COUNT; ++i) {
			itemY = bodyY+itemYOffset;
			g.drawImage(itemBg, itemX, itemY, 20);
			bodyY += bodyH;
			
			if (curGroupIndex == 0 && i==curRecId) {
				g.setColor(0XFFFF00);
			}
			else {
				g.setColor(0XFFFFFF);
			}
			
			Record record = recordList[i];
			if (record != null && record.isValid()) {
				sx = itemX+1;
				sy = itemY+1;
				Image head = loadSeigneurSmallHead(record.getSeigneurId());
				g.drawImage(head, itemX+1, itemY+1, 20);
				sx += head.getWidth()+1;
				
				ss = "君主："+Resource.STR_SEIGNEUR_NAME[record.getSeigneurId()];
				g.drawString(ss, sx, sy, 20);
				sx += 110;
				
				ss = "城池："+record.getCityCount();
				g.drawString(ss, sx, sy, 20);
				sx += 130;
				
				ss = "年代："+engine.getGameTimeStr(record.getGameTime());
				g.drawString(ss, sx, sy, 20);
				sx += 110;
				
				sx = itemX+1+head.getWidth()+1;
				sy += 23;
				ss = "武将："+record.getGeneralCount();
				g.drawString(ss, sx, sy, 20);
				sx += 110;

				ss = "兵力："+record.getSoldierCount();
				g.drawString(ss, sx, sy, 20);
				sx += 130;
				
				String recTime = record.getRecordTime();
				recTime = recTime.substring(0, recTime.lastIndexOf(':'));
				ss = "更新时间："+recTime;
				g.drawString(ss, sx, sy, 20);
				
			}
			else {
				g.drawString("没有记录!", itemX+1, itemY+1, 20);
			}
			
			if (curGroupIndex == 0 && i==curRecId) {
				DrawUtil.drawRect(g, itemX, itemY, itemW, itemH, 2, 0XFFFF00);
			}
		}
		bodyY += bodyH>>1;
		Font font = g.getFont();
		ss = "请按0键返回";
		sx = bodyX+((bodyW-font.stringWidth(ss))>>1);
		sy = bodyY;
		if (curGroupIndex == 0) {
			g.setColor(0XFFFFFF);
		}
		else {
			g.setColor(0XFFFF00);
		}
		g.drawString(ss, sx, sy, 20);
		
		Image btn = Resource.loadImage(Resource.PIC_ID_CONFIRM_BTN_BG);
		sx = itemX+itemW-Resource.W_CONFIRM_BTN-8;
		g.drawRegion(btn, 0, 0, Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 0, sx, sy, 20);
		
		ss = "返回";
		g.drawString(ss, 
				sx+((Resource.W_CONFIRM_BTN-font.stringWidth(ss))>>1), 
				sy+((Resource.H_CONFIRM_BTN-font.getHeight())>>1), 
				20);
		
		if (curGroupIndex == 1) {
			DrawUtil.drawRect(g, sx, sy, Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 2, 0XFFFF00);
		}
	}

	private void showList(Graphics g) {
		showRecordBg(g);
		showReocrdItems(g);
	}

	private void showFetch(Graphics g) {
		showRecordBg(g);
		String info = "正在读取存档，请稍后......";
		PopupText pt = Resource.buildPopupTextWithoutBtn();
		pt.setText(info);
		pt.show(g);
	}
	
	public void execute() {
		switch(state) {
		case STATE_FETCH: 
			executeFetch();
			break;
		case STATE_LIST: break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}

	private void executeFetch() {
		PopupText pt = Resource.buildPopupText();
		try {
			ServiceWrapper sw = engine.getServiceWrapper();
			GameRecordDesc[] descList = sw.queryRecordDescList();
			if (sw.isServiceSuccessful()) {
				Record record = null;
				if (descList != null && descList.length > 0) {
					for (int i = 0; i < descList.length; ++i) {
						if (recordList[descList[i].getRecordId()] != null) {
							record = recordList[descList[i].getRecordId()];
						}
						else {
							record = new Record();
							recordList[descList[i].getRecordId()] = record;
						}
						record.setId(descList[i].getRecordId());
						record.setPlayDuration(descList[i].getPlayDuration());
						record.setRemark(descList[i].getRemark());
						record.setScores(descList[i].getScores());
						record.setRecordTime(descList[i].getTime());
						record.setValid(true);
					}
				}
				state = STATE_LIST;
			}
			else {
				pt.setText("读取存档描述列表失败, 原因: "+sw.getServiceMessage());
				pt.popup();
				result = -2;
				state = STATE_LIST;
			}
		} catch (Exception e) {
			pt.setText("读取存档描述列表失败, 原因: "+e.getMessage());
			pt.popup();
			result = -2;
			state = STATE_LIST;
		}
	}
	
	public int popup(int type) {
		result = -1;
		back = false;
		this.type = (byte)type;
		Graphics g = engine.getGraphics();
		KeyState KeyState = engine.getKeyState();
		boolean run = true;
		try {
			while (run) {
				long t1 = System.currentTimeMillis();
				handle(KeyState);
				show(g);
				engine.flushGraphics();
				execute();
				
				if (back) {
					break;
				}

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
		catch (Throwable t) {
			t.printStackTrace();
			result = -2;
		}
		finally {
			clear();
		}
		return result;
	}

	public void clear() {
		Resource.freeImage(Resource.PIC_ID_RECORD_LOAD_TITLE);
		Resource.freeImage(Resource.PIC_ID_RECORD_SAVE_TITLE);
		Resource.freeImage(Resource.PIC_ID_RECORD_BG_BODY);
		Resource.freeImage(Resource.PIC_ID_RECORD_ITEM_BG);
		for (int i = 0; i < smallSeigneurHead.length; ++i) {
			smallSeigneurHead[i] = null;
		}
	}

	public void init() {
		curGroupIndex = 0;
	}
}
