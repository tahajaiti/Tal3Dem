package org.kyojin.dto.response;

import org.kyojin.entity.User;

public record AuthResponseDTO(User user, String message) {
}
