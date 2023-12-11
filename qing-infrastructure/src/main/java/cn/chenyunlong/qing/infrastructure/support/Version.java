package cn.chenyunlong.qing.infrastructure.support;

public final class Version {
    private static final int MAJOR = 1;
    private static final int MINOR = 0;
    private static final int PATCH = 1;
    public static final long SERIAL_VERSION_UID = (long) getVersion().hashCode();

    public Version() {
    }

    public static String getVersion() {
        return "1.0.1";
    }
}
