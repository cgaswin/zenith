    package org.auth.auth.dto;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.auth.auth.model.Role;

    import java.util.UUID;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class RegistrationResponseDTO {
        private String userId;
        private String name;
        private String username;
        private String email;
        private Role role;
        private String roleId;
    }
