package Spring.MindStone.domain.listener;

public class ListenerUtil {
    private static final ThreadLocal<Boolean> listenerFlag = new ThreadLocal<>();

    public static void disableListener() {
        listenerFlag.set(Boolean.FALSE);
    }

    public static void enableListener() {
        listenerFlag.set(Boolean.TRUE);
    }

    public static boolean isListenerEnabled() {
        return listenerFlag.get() == null || listenerFlag.get();
    }
}
