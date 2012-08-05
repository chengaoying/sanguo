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
		
		/*��ʾ��������*/
		g.setColor(Resource.COLOR_ID_MENU_NORMAL_TEXT);
		String ss = propList[propIndex].getName();
		sx = bgX+38+((131-font.stringWidth(ss))>>1);
		sy = bgY+130+ydelta;
		g.drawString(ss,sx, sy,	20);
		
		/*��ʾ��������*/
		ss = Integer.toString(engine.getDepotPropCount(propList[propIndex]));
		sx = bgX+123+((45-font.stringWidth(ss))>>1);
		sy = bgY+166+ydelta;
		g.drawString(ss,sx, sy,	20);
		
		/*��ʾ���߼۸�*/
		ss = Integer.toString(engineService.calcExpendAmount(propList[propIndex].getPrice()));
		sx = bgX+123+((45-font.stringWidth(ss))>>1);
		sy = bgY+202+ydelta;
		g.drawString(ss,sx, sy,	20);
		
		/*��ʾ������Ϣ����*/
		TextView.showMultiLineText(g, propList[propIndex].getIntro(), 0, bgX+186, bgY+130, 130, 91);
		
		/*��ʾ���Ԫ����*/
		ss = Integer.toString(engineService.getBalance());
		sx = bgX+185+((88-font.stringWidth(ss))>>1);
		sy = bgY+271+ydelta;
		g.drawString(ss,sx, sy,	20);
		
		/*��ʾ��ť*/
		Image btnBg = Resource.loadImage(Resource.PIC_ID_CONFIRM_BTN_BG);
		sx = bgX+Resource.X_USE_PROP_BTN;
		sy = bgY+Resource.Y_USE_PROP_BTN;
		int sw = Resource.W_CONFIRM_BTN;
		int sh = Resource.H_CONFIRM_BTN;
		g.setColor(0XFFFF00);
		
		g.drawRegion(btnBg, 0, 0, sw, sh, 0, sx, sy, 20);
		TextView.showSingleLineText(g, "ʹ��", sx, sy, sw, sh, TextView.STYLE_ALIGN_CENTER);
		sx += Resource.XSPACE_USE_PROP_BTN;
		
		g.drawRegion(btnBg, 0, 0, sw, sh, 0, sx, sy, 20);
		TextView.showSingleLineText(g, "����", sx, sy, sw, sh, TextView.STYLE_ALIGN_CENTER);
		sx += Resource.XSPACE_USE_PROP_BTN;
		
		g.drawRegion(btnBg, 0, 0, sw, sh, 0, sx, sy, 20);
		TextView.showSingleLineText(g, engineService.getRechargeCommand(), sx, sy, sw, sh, TextView.STYLE_ALIGN_CENTER);
		sx += Resource.XSPACE_USE_PROP_BTN;
		
		g.drawRegion(btnBg, 0, 0, sw, sh, 0, sx, sy, 20);
		TextView.showSingleLineText(g, "����", sx, sy, sw, sh, TextView.STYLE_ALIGN_CENTER);
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
			pt.setText("������Ϣ����ʧ�ܣ��޷�ʹ�õ���");
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
					pt.setText("Կ�ײ���������Ҫ����Կ�ײſ��Դ򿪱���");
					pt.popup();
				}
			}
			else {
				pt.setText("���䲻���������Թ���ǳػ��");
				pt.popup();
			}
		}
		else if (prop.getId() == 0) {
			if (engine.getDepotPropCountById(0) > 0) {
				if (engine.getDepotPropCountById(18) > 0) {
					canUse = true;
				}
				else {
					pt.setText("���䲻���������Թ���ǳػ��");
					pt.popup();
				}
			}
			else {
				if (engine.getDepotPropCountById(18) > 0) {
					pc.setText("��ȷ������ʹ��"+prop.getName()+"��? �۸�:"+engineService.calcExpendAmount(prop.getPrice())+engineService.getExpendAmountUnit());
					if (pc.popup() == 0) {
						if (buyProp(false)) {
							canUse = true;
						}
					}
				}
				else {
					pt.setText("���䲻���������Թ���ǳػ��");
					pt.popup();
				}
			}
		}
		else {
			if (engine.getDepotPropCountById(prop.getId()) > 0) {
				canUse = true;
			}
			else {
				pc.setText("��ȷ������ʹ��"+prop.getName()+"��? �۸�:"+engineService.calcExpendAmount(prop.getPrice())+engineService.getExpendAmountUnit());
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
			case 0: //Կ��
				useResult = true;
				engine.useDepotPropById(0);
				engine.useDepotPropById(18);
				pid = Rule.luckyDrawForProp();
				if (pid > 0) {
					pt.setText("���򿪱���\n�����"+engine.propList[pid].getName());
					engine.addDepotPropById(pid);
				}
				else {
					int rand = RandomValue.getRandInt(100);
					if (rand >= 50) {
						city.incWealth(1000);
						pt.setText("���򿪱���\n"+"���1000���, �ǳ�"+city.getName()+"�������1000");
					}
					else {
						city.incProvisions(1000);
						pt.setText("���򿪱���\n"+"���1000����, �ǳ�"+city.getName()+"��������1000");
					}
				}
				pt.popup();
				break;
			case 1: //�۱���(��������ٶȷ���������6����) 
				if (city.getCommerceEnhance() > 0) {
					pt.setText("�Ѿ�ʹ�ù�"+prop.getName()+", ʣ��ʱ��"+city.getCommerceEnhance()+"����"
							+", Ч��������ſ��Լ���ʹ��");
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
					pt.setText("ʹ��"+prop.getName()+", ���гǳؽ�������ٶȷ���,����6����");
					pt.popup();
				}
				break;
			case 2: //���ʰ������ӳ��н��3000��
				if (city.getWealth() >= City.MAX_WEALTH_VALUE) {
					pt.setText("�ǳ�"+city.getName()+"����Ѵﵽ���ޣ��޷�ʹ��"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					city.incWealth(3000);
					pt.setText("ʹ��"+prop.getName()+", "+city.getName()+"�ǽ������3000");
					pt.popup();
				}
				break;
			case 3: //����Ҫ������ʳ�����ٶȷ���������6���£� 
				if (city.getFarmingEnhance() > 0) {
					pt.setText("�Ѿ�ʹ�ù�"+prop.getName()+", ʣ��ʱ��"+city.getFarmingEnhance()+"����"
							+", Ч��������ſ��Լ���ʹ��");
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
					pt.setText("ʹ��"+prop.getName()+", ���гǳ���ʳ�����ٶȷ���,����6����");
					pt.popup();
				}
				break;

			case 4: //�����������ӳ�����ʳ3000��
				if (city.getProvisions() >= City.MAX_PROVISIONS_VALUE) {
					pt.setText("�ǳ�"+city.getName()+"�����Ѵﵽ���ޣ��޷�ʹ��"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					city.incProvisions(3000);
					pt.setText("ʹ��"+prop.getName()+", "+city.getName()+"����������3000");
					pt.popup();
				}
				break;

			case 5: //����״(��ǰ�ǳ���������100) 
				if (city.getLoyalty() >= City.MAX_LOYALTY_VALUE) {
					pt.setText("�ǳ�"+city.getName()+"�����Ѵﵽ���ޣ��޷�ʹ��"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					city.incLoyalty(100);
					pt.setText("ʹ��"+prop.getName()+", �ǳ�"+city.getName()+"��������100");
					pt.popup();
				}
				break;
			case 6: //̫ƽگ��(���гǳ���������100) 
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
					pt.setText("ʹ��"+prop.getName()+", ���гǳ���������100");
					pt.popup();
				}
				else {
					pt.setText("���гǳ����Ҿ��ﵽ����ֵ���޷�ʹ��"+prop.getName());
					pt.popup();
				}
				break;
			case 7: //��ҩ���ָ������佫��������100%�� 
				if (general.getStamina() >= Rule.INITIAL_STAMINA 
						&& general.getStamina() >= Rule.INITIAL_MAGIC) {
					pt.setText("�佫"+general.getName()+"���������������޷�ʹ��"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.setStamina(Rule.INITIAL_STAMINA);
					general.setMagic(Rule.INITIAL_MAGIC);
					pt.setText("ʹ��"+prop.getName()+", �佫"+general.getName()+"���������ָ�100%");
					pt.popup();
				}
				break;
			case 8: //����ɢ���ָ���ǰ�ǳ��佫��������100%�� 
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
					pt.setText("ʹ��"+prop.getName()+", ���������佫���������ָ�100%");
					pt.popup();
				}
				else {
					pt.setText("���������佫�����������ﵽ100%���޷�ʹ��"+prop.getName());
					pt.popup();
				}
				break;
			
			case 9: //���ӱ���(��������������������20) 
				if (general.getForce()>=General.MAX_FORCE_VALUE
						&& general.getCharm()>=General.MAX_CHARM_VALUE 
						&& general.getWit()>=General.MAX_WIT_VALUE) {
					pt.setText("�佫"+general.getName()+"���������������������ﵽ���ޣ��޷�ʹ��"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.incForce((short)20);
					general.incCharm((short)20);
					general.incWit((short)20);
					pt.setText("ʹ��"+prop.getName()+", �佫"+general.getName()+"����,����,����������20");
					pt.popup();
				}
				break;
			case 10: //�������������佫����10��
				if (general.getForce() >= General.MAX_FORCE_VALUE) {
					pt.setText("�佫"+general.getName()+"�����Ѵﵽ���ޣ��޷�ʹ��"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.incForce((short)10);
					pt.setText("ʹ��"+prop.getName()+", �佫"+general.getName()+"��������10");
					pt.popup();
				}
				break;
			case 11: //�������������佫����10�� 
				if (general.getWit() >= General.MAX_WIT_VALUE) {
					pt.setText("�佫"+general.getName()+"�����Ѵﵽ���ޣ��޷�ʹ��"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.incWit((short)10);
					pt.setText("ʹ��"+prop.getName()+", �佫"+general.getName()+"��������10");
					pt.popup();
				}
				break;
			case 12: //������(�����佫����10) 
				if (general.getCharm() >= General.MAX_CHARM_VALUE) {
					pt.setText("�佫"+general.getName()+"�����Ѵﵽ���ޣ��޷�ʹ��"+prop.getName());
					pt.popup();
				}
				else {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.incCharm((short)10);
					pt.setText("ʹ��"+prop.getName()+", �佫"+general.getName()+"��������10");
					pt.popup();
				}
				break;
			case 13: //�����죨�ǳز�������20%������������3���£� 
				if (city.getAttackEnhance() > 0) {
					pt.setText("�Ѿ�ʹ�ù�"+prop.getName()+", ʣ��ʱ��"+city.getAttackEnhance()+"����"
							+", Ч��������ſ��Լ���ʹ��");
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
					pt.setText("ʹ��"+prop.getName()+", ���гǳز��ӹ���������20%,����3����");
					pt.popup();
				}
				break;
			case 14: //����죨�ǳز�������20%������������3���£� 
				if (city.getDefenseEnhance() > 0) {
					pt.setText("�Ѿ�ʹ�ù�"+prop.getName()+", ʣ��ʱ��"+city.getDefenseEnhance()+"����"
							+", Ч��������ſ��Լ���ʹ��");
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
					pt.setText("ʹ��"+prop.getName()+", ���гǳز��ӷ���������20%,����3����");
					pt.popup();
				}
				break;
			case 15: //�󽫾��������������2400�� 
				if (general.getLead() < 2400) {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.setLead((short)2400);
					pt.setText("ʹ��"+prop.getName()+", �佫"+general.getName()+"����Ϊ"+Rule.getMilitaryTitle(general.getLead())+",�����������ӵ�2400");
					pt.popup();
				}
				else {
					pt.setText("�޷�ʹ��"+prop.getName()+", �佫"+general.getName()+"�Ѿ�����Ϊ"+Rule.getMilitaryTitle(general.getLead()));
					pt.popup();
				}
				break;
			case 16: //���н��������������1800��
				if (general.getLead() < 1800) {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.setLead((short)1800);
					pt.setText("ʹ��"+prop.getName()+", �佫"+general.getName()+"����Ϊ"+Rule.getMilitaryTitle(general.getLead())+",�����������ӵ�1800");
					pt.popup();
				}
				else {
					pt.setText("�޷�ʹ��"+prop.getName()+", �佫"+general.getName()+"�Ѿ�����Ϊ"+Rule.getMilitaryTitle(general.getLead()));
					pt.popup();
				}
				break;
			case 17: //Уξ�������������1500��
				if (general.getLead() < 1500) {
					useResult = true;
					engine.useDepotPropById(prop.getId());
					general.setLead((short)1500);
					pt.setText("ʹ��"+prop.getName()+", �佫"+general.getName()+"����Ϊ"+Rule.getMilitaryTitle(general.getLead())+",�����������ӵ�1500");
					pt.popup();
				}
				else {
					pt.setText("�޷�ʹ��"+prop.getName()+", �佫"+general.getName()+"�Ѿ�����Ϊ"+Rule.getMilitaryTitle(general.getLead()));
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
			pt.setText("������Ϣ����ʧ�ܣ��޷��������");
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
			pt.setText(prop.getName()+"���ܹ���ֻ�ܹ���ǳػ��");
			pt.popup();
		}
		else {
			if (engine.getDepotPropCount(prop)>= 99) {
				canBuy = false;
			}
		}
		
		if (canBuy && buyConfirm) {
			pc.setText("�Ƿ�Ҫ�������"+prop.getName()+", "+engineService.calcExpendAmount(prop.getPrice())+engineService.getExpendAmountUnit()+"/��");
			if (pc.popup() == 1) {
				canBuy = false;
			}
		}
		
		if (canBuy) {
			if (engineService.calcExpendAmount(prop.getPrice())>engineService.getBalance()) {
				pt.setText("�������㣬��"+engineService.getRechargeCommand()+"����");
				pt.popup();
			}
			else {
				try {
					ServiceWrapper sw = engine.getServiceWrapper();
					sw.purchaseProp(prop.getPropId(), 1, "�������"+prop.getName());
					int result = sw.getServiceResult();
					if (result == 0) {
						buyResult = true;
						engine.addDepotProp(prop);
					}
					else {
						pt.setText("�������ʧ��"+", ԭ��"+sw.getServiceMessage());
						pt.popup();
					}
				} catch (Exception e) {
					e.printStackTrace();
					pt.setText("�������ʧ��, ԭ��: "+e.getMessage());
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
						pt.setText("��Ϸ�ڲ�֧�ֳ�ֵ"+engineService.getExpendAmountUnit()+"���뷵�ش�����ֵ");
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
