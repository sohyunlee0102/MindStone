package Spring.MindStone.domain.id;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class DailyEmotionStatisticId implements Serializable {
    //Daily
    private Long memberInfo; // member_id
    private LocalDate date;  // 생성 날짜
}
