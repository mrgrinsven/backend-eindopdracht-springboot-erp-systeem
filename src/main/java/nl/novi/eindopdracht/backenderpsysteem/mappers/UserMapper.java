package nl.novi.eindopdracht.backenderpsysteem.mappers;

import nl.novi.eindopdracht.backenderpsysteem.dtos.UserInputDto;
import nl.novi.eindopdracht.backenderpsysteem.dtos.UserOutputDto;
import nl.novi.eindopdracht.backenderpsysteem.models.ContactInformation;
import nl.novi.eindopdracht.backenderpsysteem.models.Role;
import nl.novi.eindopdracht.backenderpsysteem.models.User;

import java.util.stream.Collectors;

public class UserMapper {
    public static User toEntity(UserInputDto userInputDto) {
        User user = new User();
        user.setUsername(userInputDto.username());

        ContactInformation contactInformation = new ContactInformation();
        contactInformation.setPhoneNumber(userInputDto.phoneNumber());
        contactInformation.setBusinessEmail(userInputDto.businessEmail());

        user.setContactInformation(contactInformation);
        contactInformation.setUser(user);

        return user;
    }

    public static UserOutputDto toDto(User user) {
        return new UserOutputDto(
                user.getUsername(),
                user.getContactInformation().getPhoneNumber(),
                user.getContactInformation().getBusinessEmail(),
                user.getRoles().stream().map(Role::getRole).collect(Collectors.toSet())
        );
    }
}
