package nl.novi.eindopdracht.backenderpsysteem.dtos;

import java.util.Set;

public record UserOutputDto(
   String username,
   String phoneNumber,
   String businessEmail,
   Set<String> roles
) {}
