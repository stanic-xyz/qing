package cn.chenyunlong.qing.auth.domain.user;

import cn.chenyunlong.qing.auth.domain.user.event.EmailChangedEvent;
import cn.chenyunlong.qing.auth.domain.user.valueObject.*;
import cn.hutool.core.collection.CollUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("should update basic info successfully")
    void shouldUpdateBasicInfo() {
        User user = User.register(UserId.generate(), Username.of("testuser"), RawPassword.of("123456@Admin"), PhoneNumber.of("13800000000"), Email.of("test@example.com"), "nickname");

        user.updateBasicInfo("newNick", "newAvatar", "newDesc");

        assertThat(user.getNickname()).isEqualTo("newNick");
        assertThat(user.getAvatar()).isEqualTo("newAvatar");
        assertThat(user.getDescription()).isEqualTo("newDesc");
    }

    @Test
    @DisplayName("should update partial basic info")
    void shouldUpdatePartialBasicInfo() {
        User user = User.register(UserId.generate(), Username.of("testuser"), RawPassword.of("123456@Admin"), PhoneNumber.of("13800000000"), Email.of("test@example.com"), "nickname");

        // updateBasicInfo checks for null and empty trim
        user.updateBasicInfo(null, "newAvatar", "");

        assertThat(user.getNickname()).isEqualTo("nickname"); // Unchanged
        assertThat(user.getAvatar()).isEqualTo("newAvatar");
        // description check depends on implementation logic for empty string.
        // Logic: if (description != null) { this.description = description; }
        // So empty string will set description to empty.
        assertThat(user.getDescription()).isEqualTo("");
    }

    @Test
    @DisplayName("should change email and publish event")
    void shouldChangeEmail() {
        User user = User.register(UserId.generate(), Username.of("testuser"), RawPassword.of("123456@Admin"), PhoneNumber.of("13800000000"), Email.of("test@example.com"), "nickname");
        user.clearDomainEvents(); // clear register events

        Email newEmail = Email.of("new@example.com");
        user.changeEmail(newEmail);

        assertThat(user.getEmail()).isEqualTo(newEmail);
        Collection<Object> objects = user.domainEvents();
        assertThat(objects).hasSize(1);
        assertThat(CollUtil.get(objects, 0)).isInstanceOf(EmailChangedEvent.class);
    }

    @Test
    @DisplayName("should change phone")
    void shouldChangePhone() {
        User user = User.register(UserId.generate(), Username.of("testuser"), RawPassword.of("123456@Admin"), PhoneNumber.of("13800000000"), Email.of("test@example.com"), "nickname");

        PhoneNumber newPhone = PhoneNumber.of("13900000000");
        user.changePhone(newPhone);

        assertThat(user.getPhone()).isEqualTo(newPhone);
    }
}
