package com.compremelhor.model.entity.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
	
	@Override
	public Timestamp convertToDatabaseColumn(final LocalDateTime localDateTime) {
		return (localDateTime == null ? null : Timestamp.valueOf(localDateTime));
	}
	
	@Override
	public LocalDateTime convertToEntityAttribute(final Timestamp sqlTimestamp) {
		return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
	}
}
