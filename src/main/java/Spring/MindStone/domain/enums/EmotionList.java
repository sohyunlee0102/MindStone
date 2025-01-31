package Spring.MindStone.domain.enums;

import java.util.Arrays;

public enum EmotionList {
    ANGER, DEPRESSION, SAD, CALM, JOY, THRILL, HAPPINESS;

    public static EmotionList fromString(String emotion) {
        return Arrays.stream(values()) // ✅ 배열을 Stream으로 변환
                .filter(e -> e.name().equalsIgnoreCase(emotion)) // ✅ 감정 이름과 일치하는지 필터링
                .findFirst() // ✅ 첫 번째로 찾은 값 반환
                .orElseThrow(() -> new IllegalArgumentException("일치하는 감정이 없습니다.: " + emotion)); // ✅ 없으면 예외 발생
    }
}
