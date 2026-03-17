package com.GimPay.Integration_APIs.dtos;

import java.time.LocalDateTime;

public class UserDto {

    // ── Réponse ───────────────────────────────────────────
    public static class Response {
        private Long id;
        private String email;
        private String firstName;
        private String lastName;
        private String phone;
        private String address;
        private LocalDateTime createdAt;

        public Response() {}

        public Long getId() { return id; }
        public String getEmail() { return email; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public LocalDateTime getCreatedAt() { return createdAt; }

        public void setId(Long id) { this.id = id; }
        public void setEmail(String email) { this.email = email; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setAddress(String address) { this.address = address; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private final Response r = new Response();
            public Builder id(Long v) { r.id = v; return this; }
            public Builder email(String v) { r.email = v; return this; }
            public Builder firstName(String v) { r.firstName = v; return this; }
            public Builder lastName(String v) { r.lastName = v; return this; }
            public Builder phone(String v) { r.phone = v; return this; }
            public Builder address(String v) { r.address = v; return this; }
            public Builder createdAt(LocalDateTime v) { r.createdAt = v; return this; }
            public Response build() { return r; }
        }
    }

    // ── Requête (utilisé par le Controller) ───────────────
    public static class Request {
        private String email;
        private String firstName;
        private String lastName;
        private String phone;
        private String address;

        public Request() {}

        public String getEmail() { return email; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }

        public void setEmail(String email) { this.email = email; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setAddress(String address) { this.address = address; }
    }

    // ── Alias CreateRequest → Request ────────────────────
    // Utilisé par UserService.createOrLogin(UserDto.CreateRequest req)
    public static class CreateRequest extends Request {
        public CreateRequest() { super(); }
    }
}