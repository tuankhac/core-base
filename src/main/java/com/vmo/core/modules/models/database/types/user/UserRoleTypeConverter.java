package com.vmo.core.modules.models.database.types.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleTypeConverter implements AttributeConverter<UserRoleType, String> {
    @Override
    public String convertToDatabaseColumn(UserRoleType userRoleType) {
        if (userRoleType == null) {
            return null;
        }
        return userRoleType.getValue();

    }

    @Override
    public UserRoleType convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        for (UserRoleType role : UserRoleType.values()) {
            if (value.equals(role.getValue())) {
                return role;
            }
        }
        return null;
    }
}
