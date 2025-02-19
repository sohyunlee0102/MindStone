package Spring.MindStone.domain.listener;

public class ListenerUtil {
    private static final ThreadLocal<Boolean> listenerFlag = new ThreadLocal<>();

    // 리스너를 비활성화하는 설정
    public static void disableListener() {
        listenerFlag.set(Boolean.FALSE);
    }

    // 리스너 활성화 설정
    public static void enableListener() {
        listenerFlag.set(Boolean.TRUE);
    }

    // 리스너 활성화 여부 확인
    public static boolean isListenerEnabled() {
        return listenerFlag.get() == null || listenerFlag.get();
    }
}
