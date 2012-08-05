package sanguo;

public class Record {
	private int id;
	private int seigneurId;
	private int gameTime;
	private int cityCount;
	private int generalCount;
	private int soldierCount;
	private int wealth;
	private int provisions;
	private int playDuration;
	private int scores;
	private boolean valid;
	private String recordTime;
	private byte[] data;
	
	public String buildRemark() {
		String remark = Integer.toString(seigneurId)+"#"
			+Integer.toString(gameTime)+"#"
			+Integer.toString(cityCount)+"#"
			+Integer.toString(generalCount)+"#"
			+Integer.toString(soldierCount)+"#"
			+Integer.toString(wealth)+"#"
			+Integer.toString(provisions);
		return remark;
	}
	
	public String getRemark() {
		return buildRemark();
	}
	
	public void setRemark(String remark) {
		splitRemark(remark);
	}
	
	private void splitRemark(String remark) {
		int spos = 0;
		int epos = remark.indexOf('#', spos);
		seigneurId = Integer.valueOf(remark.substring(spos, epos)).intValue();
		
		spos = epos+1;
		epos = remark.indexOf('#', spos);
		gameTime = Integer.valueOf(remark.substring(spos, epos)).intValue();
		
		spos = epos+1;
		epos = remark.indexOf('#', spos);
		cityCount = Integer.valueOf(remark.substring(spos, epos)).intValue();
		
		spos = epos+1;
		epos = remark.indexOf('#', spos);
		generalCount = Integer.valueOf(remark.substring(spos, epos)).intValue();
		
		spos = epos+1;
		epos = remark.indexOf('#', spos);
		soldierCount = Integer.valueOf(remark.substring(spos, epos)).intValue();
		
		spos = epos+1;
		epos = remark.indexOf('#', spos);
		wealth = Integer.valueOf(remark.substring(spos, epos)).intValue();
		
		spos = epos+1;
		provisions = Integer.valueOf(remark.substring(spos)).intValue();
		
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSeigneurId() {
		return seigneurId;
	}
	public void setSeigneurId(int seigneurId) {
		this.seigneurId = seigneurId;
	}
	public int getGameTime() {
		return gameTime;
	}
	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}
	
	public int getCityCount() {
		return cityCount;
	}
	
	public void setCityCount(int cityCount) {
		this.cityCount = cityCount;
	}
	
	public int getGeneralCount() {
		return generalCount;
	}
	
	public void setGeneralCount(int generalCount) {
		this.generalCount = generalCount;
	}
	public int getSoldierCount() {
		return soldierCount;
	}
	
	public void setSoldierCount(int soldierCount) {
		this.soldierCount = soldierCount;
	}
	
	public int getWealth() {
		return wealth;
	}
	
	public void setWealth(int wealth) {
		this.wealth = wealth;
	}
	
	public int getProvisions() {
		return provisions;
	}
	
	public void setProvisions(int provisions) {
		this.provisions = provisions;
	}
	
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}

	public void setPlayDuration(int playDuration) {
		this.playDuration = playDuration;
	}

	public int getPlayDuration() {
		return playDuration;
	}

	public void setScores(int scores) {
		this.scores = scores;
	}

	public int getScores() {
		return scores;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isValid() {
		return valid;
	}

}
