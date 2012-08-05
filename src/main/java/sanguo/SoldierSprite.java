package sanguo;

import javax.microedition.lcdui.Graphics;

import cn.ohyeah.stb.ui.RegularSprite;

public class SoldierSprite extends RegularSprite {
	
	private static final int FIGHT_INTERVAL_FRAME = 8;
	private static final int FIGHT_FRAME = 4;
	private static final int DEATH_FRAME = 4;
	
	public static final byte BELONG_TO_ATTACK = 0;
	public static final byte BELONG_TO_DEFENSE = 1;
	
	private static final byte STATE_STAND = 0;
	private static final byte STATE_MOVE = 1;
	private static final byte STATE_ATTACK = 2;
	private static final byte STATE_WAIT_ATTACK = 3;
	private static final byte STATE_DEATH = 4;
	private static final byte STATE_DISAPPEAR = 5;
	private static final byte STATE_RETREAT = 6;
	
	private static final byte ACTION_STAND = 0;
	private static final byte ACTION_ASSAULT = 1;
	private static final byte ACTION_RETREAT = 2;

	/*士兵ID*/
	private Soldier soldier;
	/*生命值*/
	private int life;
	/*攻击帧*/
	private byte stateFrame;
	/*攻击间隔帧*/
	private byte fightIntervalFrame;
	/*士兵状态*/
	private byte state;
	/*士兵属于进攻方，还是防守方*/
	private byte belongTo;
	
	private int singleLife;
	private int hurt;
	private short soldierWidth;
	private short securityArea;
	private byte action;
	
	public void clearResource() {
		if (belongTo == BELONG_TO_ATTACK) {
			Resource.freeImage(Resource.PIC_INFO_SOLDIERS[soldier.getId()][0]);
			Resource.freeImage(Resource.PIC_INFO_SOLDIERS_ATTACK[soldier.getId()][0]);
		}
		else {
			Resource.freeImage(Resource.PIC_INFO_SOLDIERS[soldier.getId()][1]);
			Resource.freeImage(Resource.PIC_INFO_SOLDIERS_ATTACK[soldier.getId()][1]);
		}
	}
	
	public void setStandAction() {
		action = ACTION_STAND;
	}
	
	public boolean isStandAction() {
		return action==ACTION_STAND;
	}
	
	public void setAssaultAction() {
		action = ACTION_ASSAULT;
	}
	
	public boolean isAssaultAction() {
		return action==ACTION_ASSAULT;
	}
	
	public void setRetreatAction() {
		action = ACTION_RETREAT;
	}
	
	public boolean isRetreatAction() {
		return action == ACTION_RETREAT;
	}
	
	public void setHurt(int hurt) {
		this.hurt = hurt;
	}
	
	public int getHurt() {
		return hurt;
	}

	public void setSingleLife(int singleLife) {
		this.singleLife = singleLife;
	}

	public SoldierSprite(Soldier soldier, int belongTo) {
		this.belongTo = (byte)belongTo;
		this.soldier = soldier;
		short soldierId = soldier.getId();
		if (belongTo == BELONG_TO_ATTACK) {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS[soldierId][0]), 
					Resource.PIC_INFO_SOLDIERS[soldierId][2], Resource.PIC_INFO_SOLDIERS[soldierId][3]);
			setFrameSequence(Resource.SEQ_SOLDIERS[soldierId][0]);
		}
		else {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS[soldierId][1]), 
					Resource.PIC_INFO_SOLDIERS[soldierId][2], Resource.PIC_INFO_SOLDIERS[soldierId][3]);
			setFrameSequence(Resource.SEQ_SOLDIERS[soldierId][0]);
		}
		soldierWidth = (short)getFrameWidth();
	}

	public Soldier getSoldier() {
		return soldier;
	}
	
	public void setSoldier(Soldier soldier) {
		this.soldier = soldier;
	}
	
	public boolean isDeath() {
		return state == STATE_DEATH;
	}
	
	public boolean isStand() {
		return state == STATE_STAND;
	} 
	
	public boolean isMove() {
		return state == STATE_MOVE;
	}
	
	public boolean isAttack() {
		return state == STATE_ATTACK;
	}
	
	public boolean isRetreat() {
		return state == STATE_RETREAT;
	}
	
	public boolean isDisappear() {
		return state == STATE_DISAPPEAR;
	}
	
	public boolean isRetreatShowState() {
		return isRetreat()||(!isAttack()&&isRetreatAction());
	}
	
	public void show(Graphics g) {
		if (state != STATE_DISAPPEAR) {
			int correctY = Resource.PIC_CORRECTION_SOLDIERS_ATTACK[soldier.getId()][1];
			if (state == STATE_ATTACK) {
				setY(getY()-correctY);
			}
			if (isRetreatShowState()) {
				super.setTransMirrorHorizontal();
			}
			else {
				super.setTransNone();
			}
			super.show(g);
			if (state == STATE_ATTACK) {
				setY(getY()+correctY);
			}
			int x = getX();
			int y = getY();
			int h = 5;
			int soldierCount = life/singleLife+1;
			g.setColor(0XFF0000);
			g.fillRect(x, y-h, soldierCount*soldierWidth/Resource.SOLDIER_COUNT_PER_SPRITE, h);
			g.setColor(0XFFFF00);
			g.drawRect(x, y-h, soldierWidth, h);
		}
	}
	
	public void nextFrame() {
		switch(state) {
		case STATE_STAND:
			if (isStandAction()) {
				super.nextFrame();
			}
			else if (isAssaultAction()){
				goStateMove();
			}
			else if (isRetreatAction()) {
				goStateRetreat();
			}
			break;
		case STATE_MOVE: 
			if (isAssaultAction()) {
				super.nextFrame();
			}
			else if (isStandAction()){
				goStateStand();
			}
			else if (isRetreatAction()) {
				goStateRetreat();
			}
			break;
		case STATE_ATTACK: 
			++stateFrame;
			++fightIntervalFrame;
			if (stateFrame > FIGHT_FRAME) {
				goStateWaitAttack();
			}
			else {
				super.nextFrame();
			}
			break;
		case STATE_WAIT_ATTACK:
			++fightIntervalFrame;
			if (fightIntervalFrame > FIGHT_INTERVAL_FRAME) {
				if (isStandAction()) { 
					goStateStand();
				}
				else if (isAssaultAction()){
					goStateMove();
				}
				else if (isRetreatAction()) {
					goStateRetreat();
				}
			}
			else {
				if (isRetreatAction()) {
					goStateRetreat();
				}
				else {
					super.nextFrame();
				}
			}
			break;
		case STATE_DEATH: 
			++stateFrame;
			if (stateFrame > DEATH_FRAME) {
				goStateDisappear();
			}
			else {
				super.nextFrame();
			}
			break;
		case STATE_DISAPPEAR:
			break;
		case STATE_RETREAT:
			super.nextFrame();
			break;
		default: throw new RuntimeException("无效的士兵类型, state="+state);
		}
	}
	
	private void goStateRetreat() {
		state = STATE_RETREAT;
		stateFrame = 0;
		short soldierId = soldier.getId();
		if (belongTo == BELONG_TO_ATTACK) {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS[soldierId][0]), 
					Resource.PIC_INFO_SOLDIERS[soldierId][2], Resource.PIC_INFO_SOLDIERS[soldierId][3]);
		}
		else {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS[soldierId][1]), 
					Resource.PIC_INFO_SOLDIERS[soldierId][2], Resource.PIC_INFO_SOLDIERS[soldierId][3]);
		}
		setFrameSequence(Resource.SEQ_SOLDIERS[soldierId][0]);
		setFrameIndex(0);
	}
	
	private void goStateAttack() {
		state = STATE_ATTACK;
		stateFrame = 0;
		fightIntervalFrame = 0;
		short soldierId = soldier.getId();
		if (belongTo == BELONG_TO_ATTACK) {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS_ATTACK[soldierId][0]), 
					Resource.PIC_INFO_SOLDIERS_ATTACK[soldierId][2], Resource.PIC_INFO_SOLDIERS_ATTACK[soldierId][3]);
		}
		else {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS_ATTACK[soldierId][1]), 
					Resource.PIC_INFO_SOLDIERS_ATTACK[soldierId][2], Resource.PIC_INFO_SOLDIERS_ATTACK[soldierId][3]);
		}
		setFrameSequence(Resource.SEQ_SOLDIERS[soldierId][1]);
		setFrameIndex(0);
	}

	private void goStateDeath() {
		state = STATE_DEATH;
		stateFrame = 0;
		short soldierId = soldier.getId();
		if (belongTo == BELONG_TO_ATTACK) {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS[soldierId][0]), 
					Resource.PIC_INFO_SOLDIERS[soldierId][2], Resource.PIC_INFO_SOLDIERS[soldierId][3]);
		}
		else {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS[soldierId][1]), 
					Resource.PIC_INFO_SOLDIERS[soldierId][2], Resource.PIC_INFO_SOLDIERS[soldierId][3]);
		}
		setFrameSequence(Resource.SEQ_SOLDIERS[soldierId][0]);
		setFrameIndex(0);
	}
	
	private void goStateStand() {
		state = STATE_STAND;
		stateFrame = 0;
		short soldierId = soldier.getId();
		if (belongTo == BELONG_TO_ATTACK) {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS[soldierId][0]), 
					Resource.PIC_INFO_SOLDIERS[soldierId][2], Resource.PIC_INFO_SOLDIERS[soldierId][3]);
		}
		else {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS[soldierId][1]), 
					Resource.PIC_INFO_SOLDIERS[soldierId][2], Resource.PIC_INFO_SOLDIERS[soldierId][3]);
		}
		setFrameSequence(Resource.SEQ_SOLDIERS[soldierId][0]);
		setFrameIndex(0);
	}
	
	private void goStateMove() {
		state = STATE_MOVE;
		stateFrame = 0;
		short soldierId = soldier.getId();
		if (belongTo == BELONG_TO_ATTACK) {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS[soldierId][0]), 
					Resource.PIC_INFO_SOLDIERS[soldierId][2], Resource.PIC_INFO_SOLDIERS[soldierId][3]);
		}
		else {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS[soldierId][1]), 
					Resource.PIC_INFO_SOLDIERS[soldierId][2], Resource.PIC_INFO_SOLDIERS[soldierId][3]);
		}
		setFrameSequence(Resource.SEQ_SOLDIERS[soldierId][0]);
		setFrameIndex(0);
	}
	
	private void goStateWaitAttack() {
		state = STATE_WAIT_ATTACK;
		short soldierId = soldier.getId();
		if (belongTo == BELONG_TO_ATTACK) {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS[soldierId][0]), 
					Resource.PIC_INFO_SOLDIERS[soldierId][2], Resource.PIC_INFO_SOLDIERS[soldierId][3]);
		}
		else {
			setImage(Resource.loadImage(Resource.PIC_INFO_SOLDIERS[soldierId][1]), 
					Resource.PIC_INFO_SOLDIERS[soldierId][2], Resource.PIC_INFO_SOLDIERS[soldierId][3]);
		}
		setFrameSequence(Resource.SEQ_SOLDIERS[soldierId][0]);
		setFrameIndex(0);
	}
	
	private void goStateDisappear() {
		state = STATE_DISAPPEAR;
	}
	
	public boolean inSecurityArea() {
		if (belongTo == BELONG_TO_ATTACK) {
			if (getX() <= securityArea-soldierWidth) {
				return true;
			}
		}
		else {
			if (getX() >= securityArea) {
				return true;
			}
		}
		return false;
	}
	
	public void retreat() {
		if (!inSecurityArea()) {
			if (belongTo == BELONG_TO_ATTACK) {
				setX(getX()-(soldier.getMovingSpeed()+1));
			}
			else {
				setX(getX()+(soldier.getMovingSpeed()+1));
			}
		}
	}
	
	public boolean canAction() {
		return !isDisappear();
	}
	
	public void action(SoldierSprite[] list) {
		if (isRetreatAction()) {
			retreat();
			if (inSecurityArea()) {
				goStateDisappear();
			}
		}
		else {
			SoldierSprite nearestSprite = null;
			if (canMove() || canAttack()) {
				nearestSprite = findNearestSoldier(list);
			}
			if (nearestSprite != null && canMove()) {
				moveToNearestSoldier(nearestSprite);
			}
			if (nearestSprite!=null && canAttackTo(nearestSprite)) {
				goStateAttack();
				attackOther(nearestSprite);
			}
		}
	}
	
	public void injured(int hurt) {
		if (life <= hurt) {
			life = 0;
			goStateDeath();
		}
		else {
			life -= hurt;
		}
	}

	public void attackOther(SoldierSprite other) {
		other.injured(hurt);
	}
	
	public int getSoldierCount() {
		if (life > 0) {
			return life/singleLife;
		}
		return 0;
	}

	public boolean canAttackTo(SoldierSprite other) {
		if (canAttack() && inAttackScope(other)) {
			return true;
		}
		return false;
	}
	
	public boolean canAttack() {
		if (state == STATE_STAND || state == STATE_MOVE) {
			return true;
		}
		return false;
	}
	
	private boolean canMove() {
		if (state == STATE_MOVE || state == STATE_WAIT_ATTACK) {
			return true;
		}
		return false;
	}
	
	private boolean inAttackScope(SoldierSprite other) {
		int yAttackScope = (soldier.getAttackScope()>>1);
		boolean xCanAttack = false;
		boolean yCanAttack = false;
		if (getX() < other.getX()) {
			if (getX()+soldierWidth+soldier.getAttackScope() >= other.getX()) {
				xCanAttack = true;
			}
		}
		else {
			if (other.getX()+other.soldierWidth+soldier.getAttackScope() >= getX()) {
				xCanAttack = true;
			}
		}
		if (Math.abs(getY() - other.getY()) <= yAttackScope) {
			yCanAttack = true;
		}
		return xCanAttack&&yCanAttack;
	}
	
	public SoldierSprite findNearestSoldier(SoldierSprite[] list) {
		int distance = 9999;
		SoldierSprite fighter = null;
		for (int j = 0; j < list.length; ++j) //找出最近的目标
		{
			if (list[j] != null && list[j].life > 0) {
				int d = Math.abs(getX() - list[j].getX())+ Math.abs(getY() - list[j].getY());
				if (d < distance) {
					distance = d;
					fighter = list[j];
				}
			}
		}
		return fighter;
	}
	
	public void moveToNearestSoldier(SoldierSprite other) {
		int pos = getX();
		int opos = other.getX();
		int as = soldier.getAttackScope();
		int ms = soldier.getMovingSpeed();
		if (pos < opos) {
			if (pos+soldierWidth+as < opos) {
				if (pos+soldierWidth+as+ms >= opos) {
					setX(opos-as-soldierWidth);
				}
				else {
					setX(pos+ms);
				}
			}
		}
		else {
			if (opos+other.soldierWidth+as < getX()) {
				if (opos+other.soldierWidth+as+ms >= pos) {
					setX(opos+other.soldierWidth+as);
				}
				else {
					setX(pos-ms);
				}
			}
		}
		
		pos = getY();
		opos = other.getY();
		as = (soldier.getAttackScope()>>1);	/*Y轴的攻击范围*/
		if (pos < opos) {
			if (pos+as < opos) {
				if (pos+ms >= opos-as) {
					setY(opos-as);
				}
				else {
					setY(pos+ms);
				}
			}
		}
		else {
			if (pos-as > opos) {
				if (pos-ms <= opos+as) {
					setY(opos+as);
				}
				else {
					setY(pos-ms);
				}
			}
		}
	}

	public void setLife(int spriteLife) {
		life = spriteLife;
	}

	public int getLife() {
		return life;
	}
	
	public void setSecurityArea(short securityArea) {
		this.securityArea = securityArea;
	}

}
