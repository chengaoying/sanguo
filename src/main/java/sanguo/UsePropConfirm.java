package sanguo;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.EngineService;
import cn.ohyeah.stb.game.ServiceWrapper;
import cn.ohyeah.stb.game.StateRecharge;
import cn.ohyeah.stb.ui.DrawUtil;
import cn.ohyeah.stb.ui.PopupConfirm;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.ui.TextView;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;
import cn.ohyeah.stb.util.RandomValue;

public class UsePropConfirm {
	private static NewSanguoGameEngine engine = NewSanguoGameEngine.instance;
	private static EngineService engineService = engine.getEngineService();
	private static UsePropConfirm instance = new UsePropConfirm();
	
	private byte groupIndex;
	private byte propIndex;
	private byte btnIndex;
	private byte propCount;
	private boolean back;
	private boolean needPaintPreBg;
	private City city;
	private General general;
	private Prop[] propList;
	private Image[] propIcons;
	
	private UsePropConfirm() {
		propIcons = new Image[3];
		propList = new Prop[3];
	}
	
	public static UsePropConfirm getInstance() {
		return instance;
	}
	
	public void clear() {
		groupIndex = 0;
		propIndex = 0;
		btnIndex = 0;
		propCount = 0;
		back = false;
		needPaintPreBg = false;
		city = null;
		general = null;
		for (int i = 0; i < propIcons.length; ++i) {
			propIcons[i] = null;
		}
		for (int i = 0; i < propList.length; ++i) {
			propList[i] = null;
		}
	}
	
	public void setCity(City city) {
		this.city = city;
	}
	
	public void setGeneral(General general) {
		this.general = general;
	}
	
	public void addProp(Prop prop) {
		propList[propCount] = prop;
		propIcons[propCount] = Resource.loadImage(prop.getIcon());
		++propCount;
	}
	
	public void show(Graphics g) {
		Image bg = Resource.loadImage(Resource.PIC_ID_USE_PROP_BG);
		int bgX = (engine.getScreenWidth()-bg.getWidth())>>1;
		int bgY = (engine.getScreenHeight()-bg.getHeight())>>1;
		bgY += 30;
		g.drawImage(bg, bgX, bgY, 20);
		
		int sx = bgX+Resource.X_USE_PROP_ICON;
		int sy = bgY+Resource.Y_USE_PROP_ICON;
		int xdelta = 0;
		for (int i = 0; i < propCount; ++i) {
			g.drawImage(propIcons[i], sx+xdelta, sy, 20);
			if (i == propIndex) {
				if (groupIndex == 0) {
					DrawUtil.drawRect(g, sx+xdelta, sy, 50, 50, 2, 0XFFFF00);
				}
				else {
					DrawUtil.drawRect(g, sx+xdelta, sy, 50, 50, 2, 0XFF00FF);
				}
			}
			xdelta += Resource.XSPACE_USE_PROP_ICON;
		}
		
		Font font = g.getFont();
		int ydelta = (Resource.H_USE_PROP_TEXT-font.getHeight())>>1;
		
		/*显示道具名称*/
		g.setColor(Resource.COLOR_ID_MENU_NORMAL_TEXT);
		String ss = propList[propIndex].getName();
		sx = bgX+38+((131-font.stringWidth(ss))>>1);
		sy = bgY+130+ydelta;
		g.drawString(ss,sx, sy,	20);
		
		/*显示道具数量*/
		ss = Integer.toString(engine.getDepotPropCount(propList[propIndex]));
		sx = bgX+123+((45-font.stringWidth(ss))>>1);
		sy = bgY+166+ydelta;
		g.drawString(ss,sx, sy,	20);
		
		/*显示道具价格*/
		ss = Integer.toString(engineService.calcExpendAmount(propList[propIndex].getPrice()));
		sx = bgX+123+((45-font.stringWidth(ss))>>1);
		sy = bgY+202+ydelta;
		g.drawString(ss,sx, sy,	20);
		
		/*显示道具信息文字*/
		TextView.showMultiLineText(g, propList[propIndex].getIntro(), 0, bgX+186, bgY+130, 130, 91);
		
		/*显示玩家元宝数*/
		ss = Integer.toString(engineService.getBalance());
		sx = bgX+185+((88-font.stringWidth(ss))>>1);
		sy = bgY+271+ydelta;
		g.drawString(ss,sx, sy,	20);
		
		/*显示按钮*/
		Image btnBg = Resource.loadImage(Resource.PIC_ID_CONFIRM_BTN_BG);
		sx = bgX+Resource.X_USE_PROP_BTN;
		sy = bgY+Resource.Y_USE_PROP_BTN;
		int sw = Resource.W_CONFIRM_BTN;
		int sh = Resource.H_CONFIRM_BTN;
		g.setColor(0XFFFF00);
		
		g.drawRegion(btnBg, 0, 0, sw, sh, 0, sx, sy, 20);
		TextView.showSingleLineText(g, "使用", sx, sy, sw, sh, TextView.STYLE_ALIGN_CENTER);
		sx += Resource.XSPACE_USE_PROP_BTN;
		
		g.drawRegion(btnBg, 0, 0, sw, sh, 0, sx, sy, 20);
		TextView.showSingleLineText(g, "购买", sx, sy, sw, sh, TextView.STYLE_ALIGN_CENTER);
		sx += Resource.XSPACE_USE_PROP_BTN;
		
		g.drawRegion(btnBg, 0, 0, sw, sh, 0, sx, sy, 20);
		TextView.showSingleLineText(g, engineService.getRechargeCommand(), sx, sy, sw, sh, TextView.STYLE_ALIGN_CENTER);
		sx += Resource.XSPACE_USE_PROP_BTN;
		
		g.drawRegion(btnBg, 0, 0, sw, sh, 0, sx, sy, 20);
		TextView.showSingleLineText(g, "返回", sx, sy, sw, sh, TextView.STYLE_ALIGN_CENTER);
		sx += Resource.XSPACE_USE_PROP_BTN;
		
		g.setColor(0XFFFF00);
		if (groupIndex == 1) {
			DrawUtil.drawRect(g, 
					bgX+Resource.X_USE_PROP_BTN+btnIndex*Resource.XSPACE_USE_PROP_BTN, 
					bgY+Resource.Y_USE_PROP_BTN, 
					60, 27, 2);
		}
	}
	
	private boolean useProp() {
		if (engine.pullProp() != 0) {
			PopupText pt = Resource.buildPopupText();
			pt.setText("道具信息加载失败，无法使用道具");
			pt.popup();
			return false;
		}
		boolean useResult = false;
		boolean canUse = false;
		PopupConfirm pc = Resource.buildPopupConfirm();
		PopupText pt = Resource.buildPopupText();
		Prop prop = propList[propIndex];
		if (prop.getId() == 18) {
			if (engine.getDepotPropCountById(18) > 0) {
				if (engine.getDepotPropCountById(0) > 0) {
					canUse = true;
				}
				else {
					pt.setText("钥匙不够，您需要购买钥匙才可以打开宝箱");
					pt.popup();
				}
			}
			else {
				pt.setText("宝箱不够，您可以攻打城池获得");
				pt.popup();
			}
		}
		else if (prop.getId() == 0) {
			if (engine.getDepotPropCountById(0) > 0) {
				if (engine.getDepotPropCountById(18) > 0) {
					canUse = true;
				}
				else {
					pt.setText("宝箱不够，您可以攻打城池获得");
					pt.popup();
				}
			}
			else {
				if (engine.getDepotPropCountById(18) > 0) {
					pc.setText("您确定购买并使用"+prop.getName()+"吗? 价格:"+engineService.calcExpendAmount(prop.getPrice())+engineService.getExpendAmountUnit());
					if (pc.popup() == 0) {
						if (buyProp(false)) {
							canUse = true;
						}
					}
				}
				else {
					pt.setText("宝箱不够，您可以攻打城池获得");
					pt.popup();
				}
			}
		}
		else {
			if (engine.getDepotPropCountById(prop.getId()) > 0) {
				canUse = true;
			}
			else {
				pc.setText("您确定购买并使用"+prop.getName()+"吗? 价格:"+engineService.calcExpendAmount(prop.getPrice())+engineService.getExpendAmountUnit());
				if (pc.popup() == 0) {
					if (buyProp(false)) {
						canUse = true;
					}
				}
			}
		}
		if (canUse) {
			int pid;
			switch (prop.getId()) {
			case 18:
			case 0: //钥匙
				useResult = true;
				engine.useDepotPropById(0);
				engine.useDepotPropById(18);
				pid = Rule.luckyDrawForProp();
				if (pid > 0) {
					pt.setText("您打开宝箱\n获得了"+engine.propList[pid].getName());
					engine.addDepotPropById(pid);
				}
				else {
					int rand = RandomValue.getRandInt(100);
					if (rand >= 50) {
						city.incWealth(1000);
						pt.setText("您打开宝箱\n"+"获得1000金币, 城池"+city.getName()+"金币增加1000");
					}
					else {
						city.incProvisions(1000);
						pt.setText("您打开宝箱\n"+"获得1000粮草, 城池"+city.getName()+"粮草增加1000");
					}
				}
				pt.popup();
				break;
			case 1: //聚宝盆(金币增长速度翻倍，持续6个月) 
				if (city.getCommerceEnhance() > 0) {
					pt.setText("已经使用过"+prop.getName()+", 剩余时间"+city.getCommerceEnhance()+"个月"
							+", 效果结束后才可以继续使用");
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					for (int i = 0; i < engine.cityList.length; ++i) {
						if (engine.cityList[i].belongToPlayer()) {
							engine.cityList[i].setCommerceEnhance((short)6);
						}
					}
					pt.setText("使用"+prop.getName()+", 所有城池金币增长速度翻倍,持续6个月");
					pt.popup();
				}
				break;
			case 2: //军资包（增加城市金币3000）
				if (city.getWealth() >= City.MAX_WEALTH_VALUE) {
					pt.setText("城池"+city.getName()+"金币已达到上限，无法使用"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					city.incWealth(3000);
					pt.setText("使用"+prop.getName()+", "+city.getName()+"城金币增加3000");
					pt.popup();
				}
				break;
			case 3: //齐民要术（粮食增长速度翻倍，持续6个月） 
				if (city.getFarmingEnhance() > 0) {
					pt.setText("已经使用过"+prop.getName()+", 剩余时间"+city.getFarmingEnhance()+"个月"
							+", 效果结束后才可以继续使用");
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					for (int i = 0; i < engine.cityList.length; ++i) {
						if (engine.cityList[i].belongToPlayer()) {
							engine.cityList[i].setFarmingEnhance((short)6);
						}
					}
					pt.setText("使用"+prop.getName()+", 所有城池粮食增长速度翻倍,持续6个月");
					pt.popup();
				}
				break;

			case 4: //军粮包（增加城市粮食3000）
				if (city.getProvisions() >= City.MAX_PROVISIONS_VALUE) {
					pt.setText("城池"+city.getName()+"粮草已达到上限，无法使用"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					city.incProvisions(3000);
					pt.setText("使用"+prop.getName()+", "+city.getName()+"城粮草增加3000");
					pt.popup();
				}
				break;

			case 5: //安民状(当前城池民忠增加100) 
				if (city.getLoyalty() >= City.MAX_LOYALTY_VALUE) {
					pt.setText("城池"+city.getName()+"民忠已达到上限，无法使用"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					city.incLoyalty(100);
					pt.setText("使用"+prop.getName()+", 城池"+city.getName()+"民忠增加100");
					pt.popup();
				}
				break;
			case 6: //太平诏书(所有城池民忠增加100) 
				canUse = false;
				for (int i = 0; i < engine.cityList.length; ++i) {
					if (engine.cityList[i].belongToPlayer()) {
						if (engine.cityList[i].getLoyalty() < City.MAX_LOYALTY_VALUE) {
							canUse = true;
							break;
						}
					}
				}
				
				if (canUse) {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					for (int i = 0; i < engine.cityList.length; ++i) {
						if (engine.cityList[i].belongToPlayer()) {
							engine.cityList[i].incLoyalty(100);
						}
					}
					pt.setText("使用"+prop.getName()+", 所有城池民忠增加100");
					pt.popup();
				}
				else {
					pt.setText("所有城池民忠均达到上限值，无法使用"+prop.getName());
					pt.popup();
				}
				break;
			case 7: //金疮药（恢复单个武将体力精力100%） 
				if (general.getStamina() >= Rule.INITIAL_STAMINA 
						&& general.getStamina() >= Rule.INITIAL_MAGIC) {
					pt.setText("武将"+general.getName()+"体力精力已满，无法使用"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.setStamina(Rule.INITIAL_STAMINA);
					general.setMagic(Rule.INITIAL_MAGIC);
					pt.setText("使用"+prop.getName()+", 武将"+general.getName()+"体力精力恢复100%");
					pt.popup();
				}
				break;
			case 8: //还魂散（恢复当前城池武将体力精力100%） 
				canUse = false;
				for (int i = 0; i < engine.generalList.length; ++i) {
					General g1 = engine.generalList[i];
					if (g1.isIdentityNormal()&&g1.belongToCity(city)) {
						if (g1.getStamina()<Rule.INITIAL_STAMINA
								||g1.getMagic()<Rule.INITIAL_MAGIC) {
							canUse = true;
							break;
						}
					}
				}
				if (canUse) {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					for (int i = 0; i < engine.generalList.length; ++i) {
						General g1 = engine.generalList[i];
						if (g1.isIdentityNormal()&&g1.belongToCity(city)) {
							g1.setStamina(Rule.INITIAL_STAMINA);
							g1.setMagic(Rule.INITIAL_MAGIC);
						}
					}
					pt.setText("使用"+prop.getName()+", 本城所有武将体力精力恢复100%");
					pt.popup();
				}
				else {
					pt.setText("本城所有武将体力精力都达到100%，无法使用"+prop.getName());
					pt.popup();
				}
				break;
			
			case 9: //孙子兵法(武力，魅力，智力增加20) 
				if (general.getForce()>=General.MAX_FORCE_VALUE
						&& general.getCharm()>=General.MAX_CHARM_VALUE 
						&& general.getWit()>=General.MAX_WIT_VALUE) {
					pt.setText("武将"+general.getName()+"的武力，智力，魅力均达到上限，无法使用"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.incForce((short)20);
					general.incCharm((short)20);
					general.incWit((short)20);
					pt.setText("使用"+prop.getName()+", 武将"+general.getName()+"武力,智力,魅力各增加20");
					pt.popup();
				}
				break;
			case 10: //武力丹（增加武将武力10）
				if (general.getForce() >= General.MAX_FORCE_VALUE) {
					pt.setText("武将"+general.getName()+"武力已达到上限，无法使用"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.incForce((short)10);
					pt.setText("使用"+prop.getName()+", 武将"+general.getName()+"武力增加10");
					pt.popup();
				}
				break;
			case 11: //智力丹（增加武将智力10） 
				if (general.getWit() >= General.MAX_WIT_VALUE) {
					pt.setText("武将"+general.getName()+"智力已达到上限，无法使用"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.incWit((short)10);
					pt.setText("使用"+prop.getName()+", 武将"+general.getName()+"智力增加10");
					pt.popup();
				}
				break;
			case 12: //魅力丹(增加武将魅力10) 
				if (general.getCharm() >= General.MAX_CHARM_VALUE) {
					pt.setText("武将"+general.getName()+"魅力已达到上限，无法使用"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.incCharm((short)10);
					pt.setText("使用"+prop.getName()+", 武将"+general.getName()+"魅力增加10");
					pt.popup();
				}
				break;
			case 13: //武神旗（城池部队增加20%攻击力，持续3个月） 
				if (city.getAttackEnhance() > 0) {
					pt.setText("已经使用过"+prop.getName()+", 剩余时间"+city.getAttackEnhance()+"个月"
							+", 效果结束后才可以继续使用");
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					for (int i = 0; i < engine.cityList.length; ++i) {
						if (engine.cityList[i].belongToPlayer()) {
							engine.cityList[i].setAttackEnhance((short)3);
						}
					}
					pt.setText("使用"+prop.getName()+", 所有城池部队攻击力增加20%,持续3个月");
					pt.popup();
				}
				break;
			case 14: //武侯旗（城池部队增加20%防御力，持续3个月） 
				if (city.getDefenseEnhance() > 0) {
					pt.setText("已经使用过"+prop.getName()+", 剩余时间"+city.getDefenseEnhance()+"个月"
							+", 效果结束后才可以继续使用");
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					for (int i = 0; i < engine.cityList.length; ++i) {
						if (engine.cityList[i].belongToPlayer()) {
							engine.cityList[i].setDefenseEnhance((short)3);
						}
					}
					pt.setText("使用"+prop.getName()+", 所有城池部队防御力增加20%,持续3个月");
					pt.popup();
				}
				break;
			case 15: //大将军晋升令（带兵上限2400） 
				if (general.getLead() < 2400) {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.setLead((short)2400);
					pt.setText("使用"+prop.getName()+", 武将"+general.getName()+"晋升为"+Rule.getMilitaryTitle(general.getLead())+",带兵上限增加到2400");
					pt.popup();
				}
				else {
					pt.setText("无法使用"+prop.getName()+", 武将"+general.getName()+"已经晋升为"+Rule.getMilitaryTitle(general.getLead()));
					pt.popup();
				}
				break;
			case 16: //郎中将晋升令（带兵上限1800）
				if (general.getLead() < 1800) {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.setLead((short)1800);
					pt.setText("使用"+prop.getName()+", 武将"+general.getName()+"晋升为"+Rule.getMilitaryTitle(general.getLead())+",带兵上限增加到1800");
					pt.popup();
				}
				else {
					pt.setText("无法使用"+prop.getName()+", 武将"+general.getName()+"已经晋升为"+Rule.getMilitaryTitle(general.getLead()));
					pt.popup();
				}
				break;
			case 17: //校尉晋升令（带兵上限1500）
				if (general.getLead() < 1500) {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.setLead((short)1500);
					pt.setText("使用"+prop.getName()+", 武将"+general.getName()+"晋升为"+Rule.getMilitaryTitle(general.getLead())+",带兵上限增加到1500");
					pt.popup();
				}
				else {
					pt.setText("无法使用"+prop.getName()+", 武将"+general.getName()+"已经晋升为"+Rule.getMilitaryTitle(general.getLead()));
					pt.popup();
				}
				break;
			default: break;
			}
		}
		return useResult;
	}
	
	private boolean buyProp(boolean buyConfirm) {
		if (engine.pullProp() != 0) {
			PopupText pt = Resource.buildPopupText();
			pt.setText("道具信息加载失败，无法购买道具");
			pt.popup();
			return false;
		}
		
		boolean buyResult = false;
		PopupText pt = Resource.buildPopupText();
		PopupConfirm pc = Resource.buildPopupConfirm();
		boolean canBuy = true;
		Prop prop = propList[propIndex];
		if (prop.getId() == 18) {
			canBuy = false;
			pt.setText(prop.getName()+"不能购买，只能攻打城池获得");
			pt.popup();
		}
		else {
			if (engine.getDepotPropCount(prop)>= 99) {
				canBuy = false;
			}
		}
		
		if (canBuy && buyConfirm) {
			pc.setText("是否要购买道具"+prop.getName()+", "+engineService.calcExpendAmount(prop.getPrice())+engineService.getExpendAmountUnit()+"/个");
			if (pc.popup() == 1) {
				canBuy = false;
			}
		}
		
		if (canBuy) {
			if (engineService.calcExpendAmount(prop.getPrice())>engineService.getBalance()) {
				pt.setText("您的余额不足，请"+engineService.getRechargeCommand()+"后购买");
				pt.popup();
			}
			else {
				try {
					ServiceWrapper sw = engine.getServiceWrapper();
					sw.purchaseProp(prop.getPropId(), 1, "购买道具"+prop.getName());
					int result = sw.getServiceResult();
					if (result == 0) {
						buyResult = true;
						engine.addDepotProp(prop);
					}
					else {
						pt.setText("购买道具失败"+", 原因："+sw.getServiceMessage());
						pt.popup();
					}
				} catch (Exception e) {
					e.printStackTrace();
					pt.setText("购买道具失败, 原因: "+e.getMessage());
					pt.popup();
				}
			}
		}
		return buyResult;
	}

	public void handle(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (groupIndex > 0) {
				--groupIndex;
			}
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (groupIndex < 1) {
				++groupIndex;
				btnIndex = 0;
			}
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (groupIndex == 0) {
				propIndex = (byte)((propIndex+propCount-1)%propCount);
			}
			else {
				btnIndex = (byte)((btnIndex+4-1)%4);
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (groupIndex == 0) {
				propIndex = (byte)((propIndex+1)%propCount);
			}
			else {
				btnIndex = (byte)((btnIndex+1)%4);
			}
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			if (groupIndex == 0) {
				groupIndex = 1;
				btnIndex = 0;
			}
			else {
				if (btnIndex == 0) {
					useProp();
				}
				else if (btnIndex == 1) {
					buyProp(true);
				}
				else if (btnIndex == 2) {
					if (engineService.isSupportRecharge()) {
						StateRecharge recharge = new StateRecharge(engine);
						recharge.recharge();
						needPaintPreBg = true;
					}
					else {
						PopupText pt = Resource.buildPopupText();
						pt.setText("游戏内不支持充值"+engineService.getExpendAmountUnit()+"，请返回大厅充值");
						pt.popup();
					}
				}
				else {
					back = true;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			back = true;
		}
	}

	public boolean isBack() {
		return back;
	}

	public void setNeedPaintPreBg(boolean needPaintPreBg) {
		this.needPaintPreBg = needPaintPreBg;
	}

	public boolean isNeedPaintPreBg() {
		return needPaintPreBg;
	}
}
