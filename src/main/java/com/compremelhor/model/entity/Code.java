package com.compremelhor.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Code {	
	@Column(name = "type_code")
	@Enumerated(EnumType.STRING) @NotNull
	private CodeType type;
	
	@Column(name = "code")
	@NotNull @Size(max=20)
	private String code;
	
	public CodeType getType() {
		return type;
	}

	public void setType(CodeType type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public enum CodeType {
		QR_C0DE, BARCODE, OTHER;
	}
	
	@Override
	public String toString() {
		return code;
	}
	
	@Override
	public int hashCode() {
		return (code.hashCode() + type.name()).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Code 
				&& ((Code)obj).getCode().equals(code)
				&& ((Code)obj).getType().name().equals(type.name()))
			return true;
		
		return false;
	}

}
