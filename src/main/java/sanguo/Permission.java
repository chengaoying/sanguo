package sanguo;

public class Permission {
	private boolean hasPermission;
	private boolean needTip;
	private boolean useProp;
	private String message;
	private int code;
	private int value;
	
	public boolean hasPermission() {
		return hasPermission;
	}
	public void setHasPermission(boolean hasPermission) {
		this.hasPermission = hasPermission;
	}
	public boolean needTip() {
		return needTip;
	}
	public void setNeedTip(boolean needTip) {
		this.needTip = needTip;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	
	public void reset() {
		code = 0;
		useProp = false;
	}
	public void setUseProp(boolean useProp) {
		this.useProp = useProp;
	}
	public boolean isUseProp() {
		return useProp;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
