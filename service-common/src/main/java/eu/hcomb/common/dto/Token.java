package eu.hcomb.common.dto;

import io.swagger.annotations.ApiModel;

@ApiModel(value="Token", description="JWT Token")
public class Token {

	protected boolean valid;
	protected Long expire;
	protected String value;

	public Token(boolean valid) {
		this.valid = valid;
	}

	public Token() {

	}
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public Long getExpire() {
		return expire;
	}
	public void setExpire(Long expire) {
		this.expire = expire;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
