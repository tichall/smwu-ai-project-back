package smwu.server.domain.dto;

import lombok.Getter;

@Getter
public class UserInputRequestDto {
    private int depositAmount; // 예치금 (1원 단위) ex. 20만원의 경우 200000으로 입력
    private int periodMonths; // 예치 기간 ex. 12개월의 경우 12로 입력
    private String depositType; // 예치 유형 ex. 예금 or 적금
    private String conditions; // 조건 ex. 여자, 직업
}
