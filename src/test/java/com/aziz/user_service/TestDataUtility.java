package com.aziz.user_service;

import com.aziz.user_service.dto.*;
import com.aziz.user_service.model.Address;
import com.aziz.user_service.model.User;
import com.aziz.user_service.util.enums.City;
import com.aziz.user_service.util.enums.PreferredLanguage;
import com.aziz.user_service.util.enums.Role;

import java.time.LocalDate;
import java.util.List;

public class TestDataUtility {
    private static final Long USER_ID = 1L;
    private static final String FIRST_NAME = "Mohamed";
    private static final String LAST_NAME = "Ahmed";
    private static final String EMAIL = "MohamedAhmed123@gmail.com";
    private static final String PASSWORD = "^_mohamedahmed123_^";
    private static final String PHONE_NUMBER = "01211111111";
    private static final Role ROLE = Role.ROLE_USER;
    private static final PreferredLanguage PREFERRED_LANGUAGE = PreferredLanguage.ARABIC;
    private static final LocalDate CREATED_AT = LocalDate.of(2005, 12, 11);
    private static final LocalDate LAST_MODIFIED_AT = LocalDate.of(2005, 12, 11);

    // Address
    private static final Long ADDRESS_ID = 1L;
    private static final String STREET_LINE1 = "Street 1";
    private static final String STREET_LINE2 = "Street 2";
    private static final City CITY = City.EL_BEHEIRA;
    private static final String STATE = "Kafr-El Draw";
    private static final String POSTAL_CODE = "22621";
    private static final boolean IS_DEFAULT_SHIPPING = true;
    private static final LocalDate ADDRESS_CREATED_AT = LocalDate.of(2025, 11, 13);
    private static final LocalDate ADDRESS_LAST_MODIFIED_AT = LocalDate.of(2025, 11, 13);

    public static User createUser() {
        return User.builder()
                .userId(USER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .role(ROLE)
                .preferredLanguage(PREFERRED_LANGUAGE)
                .createdAt(CREATED_AT)
                .lastModifiedAt(LAST_MODIFIED_AT)
                .address(List.of(createAddress(), createAddress()))
                .build();
    }

    public static Address createAddress() {
        return Address.builder()
                .addressId(ADDRESS_ID)
                .streetLine1(STREET_LINE1)
                .streetLine2(STREET_LINE2)
                .city(CITY)
                .state(STATE)
                .postalCode(POSTAL_CODE)
                .isDefaultShipping(IS_DEFAULT_SHIPPING)
                .createdAt(ADDRESS_CREATED_AT)
                .lastModifiedAt(ADDRESS_LAST_MODIFIED_AT)
                .build();
    }
    public static UserDto createUserDto() {
        return new UserDto(USER_ID, FIRST_NAME, LAST_NAME);
    }

    public static UserUpdateRequest createUserUpdateRequest() {
        return new UserUpdateRequest(USER_ID, "Ramadan", "Karim", "ramadankarim123", "01233333333");
    }

    public static AddressDto createAddressDto() {
        return new AddressDto(ADDRESS_ID, STREET_LINE1, STREET_LINE2, CITY, STATE, POSTAL_CODE, IS_DEFAULT_SHIPPING);
    }

    public static AddressRegisterRequest createAddressRegisterRequest() {
        return new AddressRegisterRequest(STREET_LINE1, STREET_LINE2, CITY, STATE, POSTAL_CODE, IS_DEFAULT_SHIPPING);
    }

    public static PendingUserData createPendingUserData() {
        return new PendingUserData("this-is-verification-id", FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, PHONE_NUMBER);
    }

    public static RegistrationRequest createRegistrationRequest() {
        return new RegistrationRequest(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, PHONE_NUMBER);
    }

    public static OtpVerificationRequest createOtpVerificationRequest() {
        return new OtpVerificationRequest("this-is-verification-id", EMAIL, "123456");
    }

    public static AuthUserDto createAuthUserDto() {
        return new AuthUserDto(USER_ID, ROLE);
    }
}
