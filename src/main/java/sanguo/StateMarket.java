package sanguo;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.EngineService;
import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.game.ServiceWrapper;
import cn.ohyeah.stb.game.StateRecharge;
import cn.ohyeah.stb.ui.PopupConfirm;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.ui.ScrollBar;
import cn.ohyeah.stb.ui.TextView;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

public class StateMarket {
	private static final byte NUM_MARKET_PROP_LIST_ITEMS = 6;
	
	private static final byte STATE_START = 0;
	private static final byte STATE_MARKET = 1;
	
	public static final byte TYPE_PROP_MARKET = 0;
	public static final byte TYPE_GENERAL_MARKET = 1;

	private static NewSanguoGameEngine engine = NewSanguoGameEngine.instance;
	private static EngineService engineService = engine.getEngineService();
	
	private Prop[] propList;
	private int propCount;
	private Image propIcons[];
	private byte type;
	private byte state;
	private byte totalPage;
	private byte curPagePropIndex;
	private byte curPageIndex;
	private byte curSelectedBox;
	private byte curConfirmIndex;
	private ScrollBar scroll;
	private City targetCity;
	private boolean back;
	
	public StateMarket(int type) {
		this.type = (byte)type;
		propIcons = new Image[NUM_MARKET_PROP_LIST_ITEMS];
		if (type == TYPE_PROP_MARKET) {
			propList = engine.propList;
			propCount = propList.length-1;
		}
		else {
			propList = engine.generalPropList;
			propCount = propList.length;
		}
		
		if (propCount <= 6) {
			totalPage = 1;
		}
		else {
			totalPage = (byte)((propCount+3-1)/3-1);
		}
	}
	
	public void handle(KeyState KeyState) {
		switch (state) {
		case STATE_START:
			break;
		case STATE_MARKET:
			handleMarket(KeyState);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}

	private void handleMarket(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP))	{
			if (curSelectedBox == 0) {
				if (curPagePropIndex%3 > 0) {
					--curPagePropIndex;
				}
			}
			else {
				if (curConfirmIndex == 0) {
					curConfirmIndex = 1;
				}
				else {
					curConfirmIndex = 0;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (curSelectedBox == 0) {
				if (getCurItemOrder() < propCount-1) {
					if (curPagePropIndex%3 < 2) {
						++curPagePropIndex;
					}
				}
			}
			else {
				if (curConfirmIndex == 0) {
					curConfirmIndex = 1;
				}
				else {
					curConfirmIndex = 0;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (curSelectedBox == 0) {
				if (curPagePropIndex/3 > 0) {
					curPagePropIndex -= 3;
				}
				else {
					if (curPageIndex > 0) {
						--curPageIndex;
						if (scroll != null) {
							scroll.setCurPage(curPageIndex);
						}
						for (int i = 0; i < propIcons.length; ++i) {
							propIcons[i] = null;
						}
					}
					else {
						curSelectedBox = 1;
						curConfirmIndex = 0;
					}
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (curSelectedBox == 0) {
				if (curPageIndex < totalPage-1) {
					if (curPagePropIndex/3 > 0) {
						++curPageIndex;
						if (scroll != null) {
							scroll.setCurPage(curPageIndex);
						}
						for (int i = 0; i < propIcons.length; ++i) {
							propIcons[i] = null;
						}
						if (getCurItemOrder() > propCount-1) {
							curPagePropIndex = (byte)((propCount%NUM_MARKET_PROP_LIST_ITEMS)+3-1);
						}
					}
					else { 
						curPagePropIndex += 3;
					}
				}
				else {
					if (curPagePropIndex/3 <= 0) {
						curPagePropIndex += 3;
						if (getCurItemOrder() > propCount-1) {
							curPagePropIndex = (byte)((propCount%NUM_MARKET_PROP_LIST_ITEMS)+3-1);
						}
					}
				}
			}
			else {
				curSelectedBox = 0;
			}
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			Prop prop = propList[curPageIndex*3+curPagePropIndex];
			PopupText pt = Resource.buildPopupText();
			PopupConfirm pc = Resource.buildPopupConfirm();
			if (curSelectedBox == 0) {
				boolean canBuy = true;
				if (type == TYPE_GENERAL_MARKET) {
					/*武将已经派遣*/
					if (!engine.generalList[((GeneralProp)prop).getGeneralId()].isIdentityProp()) {
						canBuy = false;
					}
					else {
						/*武将已经购买*/
						if (engine.getDepotGeneralPropCountById(prop.getId())> 0) {
							canBuy = false;
							engine.useDepotGeneralProp(prop);
							dispatchGeneral((GeneralProp)prop);
						}
					}
				}
				else {
					if (engine.getDepotPropCount(prop)>= 99) {
						canBuy = false;
						pt.setText(prop.getName()+"数量已达到上限，无法购买");
						pt.popup();
					}
				}
				
				String remark = null;
				if (canBuy) {
					if (type == TYPE_GENERAL_MARKET) {
						remark = "聘用武将"+prop.getName();
						pc.setText("是否"+remark+", "+engineService.calcExpendAmount(prop.getPrice())+engineService.getExpendAmountUnit());
					}
					else {
						remark = "购买道具"+prop.getName();
						pc.setText("是否"+remark+", "+engineService.calcExpendAmount(prop.getPrice())+engineService.getExpendAmountUnit()+"/个");
					}
					if (pc.popup() == 1) {
						canBuy = false;
					}
				}
				
				if (canBuy) {
					if (prop.getPrice()>engineService.getBalance()) {
						if (engineService.isSupportRecharge()) {
							PopupConfirm confirm = Resource.buildPopupConfirm();
							confirm.setText("您的"+engineService.getExpendAmountUnit()+"不足，请"
									+engineService.getRechargeCommand()+"后购买");
							if (confirm.popup() == 0) {
								StateRecharge recharge = new StateRecharge(engine);
								recharge.recharge();
								engine.stateMap.showCommonMapBgInfo(engine.getSGraphics());
								engine.stateMap.clearCommonMapBgRes();
							}
						}
						else {
							pt.setText("您的"+engineService.getExpendAmountUnit()+"不足，请返回大厅充值后购买");
							pt.popup();
						}
					}
					else {
						try {
							ServiceWrapper sw = engine.getServiceWrapper();
							sw.purchaseProp(prop.getPropId(), 1, remark);
							if (sw.isServiceSuccessful()) {
								if (type == TYPE_GENERAL_MARKET) {
									engine.addDepotGeneralProp(prop);
									engine.useDepotGeneralProp(prop);
									dispatchGeneral((GeneralProp)prop);
								}
								else {
									engine.addDepotProp(prop);
								}
							}
							else {
								pt.setText(type == TYPE_GENERAL_MARKET?"聘用武将失败":"购买道具失败"+", 原因："+sw.getServiceMessage());
								pt.popup();
							}
							
						} catch (Exception e) {
							e.printStackTrace();
							pt.setText(type == TYPE_GENERAL_MARKET?("聘用武将失败, 原因: "+e.getMessage()):("购买道具失败, 原因: "+e.getMessage()));
							pt.popup();
						}
					}
				}
			}
			else {
				if (curConfirmIndex == 0) {
					if (engineService.isSupportRecharge()) {
						StateRecharge recharge = new StateRecharge(engine);
						recharge.recharge();
						engine.stateMap.showCommonMapBgInfo(engine.getSGraphics());
						engine.stateMap.clearCommonMapBgRes();
					}
					else {
						pt.setText("游戏内不支持充值"+engineService.getExpendAmountUnit()+"，请返回大厅充值");
						pt.popup();
					}
				}
				else {
					key.clear();
					back = true;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			back = true;
		}
	}
	
	private void dispatchGeneral(GeneralProp prop) {
		General general = engine.generalList[prop.getGeneralId()];
		general.setIdentityNormal();
		general.setSeigneurId((short)engine.playerSeigneur);
		if (targetCity != null) {
			general.setCity(targetCity);
		}
		else {
			general.setCity(engine.getPlayerSeigneurCity());
		}
		
		PopupText pt = Resource.buildPopupText();
		pt.setText(general.getName()+"进驻我军"+general.getCity().getName());
		pt.popup();
	}
	
	private int getCurItemOrder() {
		int order = -1;
		if (curPageIndex > 0) {
			order = 3+(curPageIndex-1)*3+curPagePropIndex;
		}
		else {
			order = curPagePropIndex;
		}
		return order;
	}

	public void show(SGraphics g) {
		switch (state) {
		case STATE_START:
			showMarket(g);
			break;
		case STATE_MARKET:
			showMarket(g);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}

	private void showMarket(SGraphics g) {
		Image marketBg = Resource.loadImage(Resource.PIC_ID_MARKET_BG);
		int bgX = (engine.getScreenWidth()-marketBg.getWidth())>>1;
		int bgY = (engine.getScreenHeight()-marketBg.getHeight())>>1;
		bgY += 37;
		g.drawImage(marketBg, bgX, bgY, 20);
		Image btn = Resource.loadImage(Resource.PIC_ID_CONFIRM_BTN_BG);
		int btnW = Resource.W_CONFIRM_BTN;
		int btnH = Resource.H_CONFIRM_BTN;
		Font font = g.getFont();
		int fontH = font.getHeight();
		
		int xspace = 0, yspace = 0;
		int start = curPageIndex*3;
		for (int i = 0; i < NUM_MARKET_PROP_LIST_ITEMS; ++i) {
			if (i+start < propCount) {
				if (propIcons[i] == null) {
					propIcons[i] = Resource.loadImage(propList[i+start].getIcon());
				}
				xspace = bgX+(i/3)*Resource.XSPACE_MARKET_PROP_LIST_ITEM;
				yspace = bgY+(i%3)*Resource.YSPACE_MARKET_PROP_LIST_ITEM;
				
				if (type == TYPE_GENERAL_MARKET) {
					/*画道具图标*/
					g.drawImage(propIcons[i], 238+xspace, 90+yspace, 20);
					g.setColor(0xffffff);
					g.drawString(propList[i+start].getName(), 302+xspace+21, 90+yspace, 20);
					g.drawString(engineService.calcExpendAmount(propList[i+start].getPrice())+engineService.getExpendAmountUnit(), 302+xspace+21, 120+yspace, 20);
					if (!engine.generalList[engine.generalPropList[i+start].getGeneralId()].isIdentityProp()) {
						g.drawString("已聘用", Resource.X_MARKET_PROP_LIST_NUM+xspace+21, 
								Resource.Y_MARKET_PROP_LIST_NUM+yspace, 20);
					}
				}
				else {
					/*画道具图标*/
					g.drawImage(propIcons[i], 238+xspace-6, 90+yspace, 20);
					g.setColor(0xffffff);
					g.drawString(propList[i+start].getName(), 302+xspace-6, 90+yspace, 20);
					g.drawString(engineService.calcExpendAmount(propList[i+start].getPrice())+engineService.getExpendAmountUnit(), 302+xspace-6, 120+yspace, 20);
					g.drawString("X"+engine.getDepotPropCount(propList[i+start]), Resource.X_MARKET_PROP_LIST_NUM+xspace-6, 
							Resource.Y_MARKET_PROP_LIST_NUM+yspace, 20);
				}
				
				if (i == curPagePropIndex && curSelectedBox == 0) {
					g.drawImage(propIcons[i], Resource.X_MARKET_PROP_INTRO_ICON+bgX, 
							Resource.Y_MARKET_PROP_INTRO_ICON+bgY, 20);
					
					if (type == TYPE_GENERAL_MARKET) {
						g.drawString(propList[i+start].getName(), Resource.X_MARKET_PROP_INTRO_NAME+bgX+21, 
								Resource.Y_MARKET_PROP_INTRO_NAME+bgY, 20);
						g.drawString(engineService.calcExpendAmount(propList[i+start].getPrice())+engineService.getExpendAmountUnit(), Resource.X_MARKET_PROP_INTRO_PRICE+bgX+21, 
								Resource.Y_MARKET_PROP_INTRO_PRICE+bgY, 0);
						
						General g1 = engine.generalList[engine.generalPropList[i+start].getGeneralId()];
						int sx = Resource.X_MARKET_PROP_INTRO_TEXT+bgX;
						int sy = Resource.Y_MARKET_PROP_INTRO_TEXT+bgY+21;
						
						String ss = "武力: "+g1.getForce();
						g.setColor(0xffffff);
						g.drawString(ss, sx, sy, 20);
						sy += fontH+1;
						
						ss = "智力: "+g1.getWit();
						g.drawString(ss, sx, sy, 20);
						sy += fontH+1;
						
						ss = "魅力: "+g1.getCharm();
						g.drawString(ss, sx, sy, 20);
						sy += fontH+1;
						
						ss = "将技: "+g1.getMagicPower();
						g.drawString(ss, sx, sy, 20);
						sy += fontH+1;
						
						ss = "统率: "+g1.getLead();
						g.drawString(ss, sx, sy, 20);
						sy += fontH+1;
						
					}
					else {
						g.drawString(propList[i+start].getName(), Resource.X_MARKET_PROP_INTRO_NAME+bgX, 
								Resource.Y_MARKET_PROP_INTRO_NAME+bgY, 20);
						g.drawString(engineService.calcExpendAmount(propList[i+start].getPrice())+engineService.getExpendAmountUnit(), Resource.X_MARKET_PROP_INTRO_PRICE+bgX, 
								Resource.Y_MARKET_PROP_INTRO_PRICE+bgY, 0);
						g.setColor(0XFFFFFF);
						TextView.showMultiLineText(g, propList[i+start].getIntro(), 1, 
								Resource.X_MARKET_PROP_INTRO_TEXT+bgX, Resource.Y_MARKET_PROP_INTRO_TEXT+bgY, 
								Resource.W_MARKET_PROP_INTRO_TEXT, Resource.H_MARKET_PROP_INTRO_TEXT);
					}
					
					g.setColor(0xFFFF00);
					g.drawRect(Resource.X_MARKET_PROP_LIST_ITEM+xspace, Resource.Y_MARKET_PROP_LIST_ITEM+yspace, 
							Resource.W_MARKET_PROP_LIST_ITEM, Resource.H_MARKET_PROP_LIST_ITEM);
					
					if (type == TYPE_GENERAL_MARKET) {
						if (engine.generalList[engine.generalPropList[i+start].getGeneralId()].isIdentityProp()) {
							if (engine.getDepotGeneralPropCount(propList[i+start]) > 0) {
								g.drawRegion(btn, btnW, 0, btnW, btnH, 0, 
										Resource.X_MARKET_PROP_LIST_NUM+xspace+21, 
										Resource.Y_MARKET_PROP_LIST_NUM+yspace, 20);
								g.drawString("派遣", Resource.X_MARKET_PROP_LIST_NUM+xspace+10+21, 
										Resource.Y_MARKET_PROP_LIST_NUM+yspace+4, 20);
							}
							else {
								g.drawRegion(btn, btnW, 0, btnW, btnH, 0, 
										Resource.X_MARKET_PROP_LIST_NUM+xspace+21, 
										Resource.Y_MARKET_PROP_LIST_NUM+yspace, 20);
								g.drawString("聘用", Resource.X_MARKET_PROP_LIST_NUM+xspace+10+21, 
										Resource.Y_MARKET_PROP_LIST_NUM+yspace+4, 20);
							}
						}
					}
					else {
						g.drawRegion(btn, btnW, 0, btnW, btnH, 0, 
								Resource.X_MARKET_PROP_BUY_BTN+xspace-6, 
								Resource.Y_MARKET_PROP_BUY_BTN+yspace, 20);
						g.drawString("购买", Resource.X_MARKET_PROP_BUY_BTN+xspace+10-6, 
								Resource.Y_MARKET_PROP_BUY_BTN+yspace+4, 20);
					}
				}
				else {
					if (type == TYPE_GENERAL_MARKET) {
						if (engine.generalList[engine.generalPropList[i+start].getGeneralId()].isIdentityProp()) {
							if (engine.getDepotGeneralPropCount(propList[i+start]) > 0) {
								g.drawRegion(btn, 0, 0, btnW, btnH, 0, 
										Resource.X_MARKET_PROP_LIST_NUM+xspace+21, 
										Resource.Y_MARKET_PROP_LIST_NUM+yspace, 20);
								g.drawString("派遣", Resource.X_MARKET_PROP_LIST_NUM+xspace+10+21, 
										Resource.Y_MARKET_PROP_LIST_NUM+yspace+4, 20);
							}
							else {
								g.drawRegion(btn, 0, 0, btnW, btnH, 0, 
										Resource.X_MARKET_PROP_LIST_NUM+xspace+21, 
										Resource.Y_MARKET_PROP_LIST_NUM+yspace, 20);
								g.drawString("聘用", Resource.X_MARKET_PROP_LIST_NUM+xspace+10+21, 
										Resource.Y_MARKET_PROP_LIST_NUM+yspace+4, 20);
							}
						}
					}
					else {
						g.drawRegion(btn, 0, 0, btnW, btnH, 0, 
								Resource.X_MARKET_PROP_BUY_BTN+xspace-6, 
								Resource.Y_MARKET_PROP_BUY_BTN+yspace, 20);
						g.drawString("购买", Resource.X_MARKET_PROP_BUY_BTN+xspace+10-6, 
								Resource.Y_MARKET_PROP_BUY_BTN+yspace+4, 20);
					}
				}
			}
			else {
				break;
			}
		}
		
		if (scroll == null) {
			createScrollBar(bgX, bgY);
		}
		scroll.show(g);
		
		/*显示元宝数量*/
		String amt = Integer.toString(engineService.getBalance());
		int sx = bgX+Resource.X_MARKET_AMOUNT_TEXT+
				((Resource.W_MARKET_AMOUNT_TEXT-font.stringWidth(amt))>>1);
		int sy = bgY+Resource.Y_MARKET_AMOUNT_TEXT+((22-fontH)>>1);
		g.setColor(0xFFFF00);
		g.drawString(amt, sx, sy, 20);
		
		sx = bgX+Resource.X_MARKET_BTN+
				((Resource.W_MARKET_BTN-btnW)>>1);
		sy = bgY+Resource.Y_MARKET_RECHARGE_BTN;
		if (curSelectedBox==1 && curConfirmIndex == 0) {
			g.drawRegion(btn, btnW, 0, btnW, btnH, 0, sx, sy, 20);
			g.setColor(0xFFFF00);
		}
		else {
			g.drawRegion(btn, 0, 0, btnW, btnH, 0, sx, sy, 20);
			g.setColor(0XFFFFFF);
		}
		sx = sx+((btnW-font.stringWidth(engineService.getRechargeCommand()))>>1);
		sy = sy+((btnH-fontH)>>1);
		g.drawString(engineService.getRechargeCommand(), sx, sy, 20);
		
		sx = bgX+Resource.X_MARKET_BTN+
				((Resource.W_MARKET_BTN-btnW)>>1);
		sy = bgY+Resource.Y_MARKET_BACK_BTN;
		if (curSelectedBox==1 && curConfirmIndex == 1) {
			g.drawRegion(btn, btnW, 0, btnW, btnH, 0, sx, sy, 20);
			g.setColor(0xFFFF00);
		}
		else {
			g.drawRegion(btn, 0, 0, btnW, btnH, 0, sx, sy, 20);
			g.setColor(0XFFFFFF);
		}
		sx = sx+((btnW-font.stringWidth("返回"))>>1);
		sy = sy+((btnH-fontH)>>1);
		g.drawString("返回", sx, sy, 20);
	}

	private void createScrollBar(int bgX, int bgY) {
		scroll = new ScrollBar();
		scroll.setImage(Resource.loadImage(Resource.PIC_ID_SCROLL_BAR));
		scroll.setFrameRegion(Resource.REGION_SCROLL_BAR_FRAME);
		scroll.setPosition(225+bgX, 399+bgY);
		scroll.setSize(379, 16);
		scroll.setContentLen((propCount+3-1)/3);
		scroll.setViewLen(2);
		scroll.setTotalPage(totalPage);
		scroll.setScrollType(ScrollBar.SCROLL_LEFT_RIGHT);
	}

	public void execute() {
		switch (state) {
		case STATE_START:
			state = STATE_MARKET;
			break;
		case STATE_MARKET:
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	
	public void popup() {
		back = false;
		this.type = (byte)type;
		SGraphics g = engine.getSGraphics();
		KeyState KeyState = engine.getKeyState();
		boolean run = true;
		try {
			if (engine.pullProp() != 0) {
				PopupText pt = Resource.buildPopupText();
				pt.setText((type==TYPE_GENERAL_MARKET?"武将":"道具")+"信息加载失败，无法进入商城");
				pt.popup();
				return;
			}
			while (run) {
				long t1 = System.currentTimeMillis();
				handle(KeyState);
				//KeyState.clear();
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
		}
		finally {
			clear();
		}
	}

	public void clear() {
		Resource.freeImage(Resource.PIC_ID_MARKET_BG);
		setTargetCity(null);
	}

	public void setTargetCity(City targetCity) {
		this.targetCity = targetCity;
	}
}
