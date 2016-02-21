package com.compremelhor.model.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.compremelhor.model.entity.User;
import com.compremelhor.model.entity.User.DocumentType;

@Converter(autoApply = true)
public class DocumentTypeAttributeConverter implements AttributeConverter<User.DocumentType, String> {

	@Override
	public String convertToDatabaseColumn(DocumentType attribute) {
		return attribute.name().toLowerCase();
	}

	@Override
	public DocumentType convertToEntityAttribute(String dbData) {
		for (User.DocumentType dt : User.DocumentType.values()) {
			if (dbData.equals(dt.name())) {
				return dt;
			}
		}
		throw new IllegalArgumentException("Unknow attribute value dbData");
	}
}
