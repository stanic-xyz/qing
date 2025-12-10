package cn.chenyunlong.qing.auth.domain.user.valueObject;

/**
 * 用户名可用性DTO
 */
public record UsernameAvailability(String username, boolean available, String message) {
    public UsernameAvailability(String username, boolean available) {
        this(username, available, available ? "用户名可用" : "用户名已存在");
    }

    // Getters...
}
