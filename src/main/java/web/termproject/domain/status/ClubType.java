package web.termproject.domain.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ClubType {
    CENTRAL("중앙동아리"),
    DEPARTMENT("학과동아리");

    private final String koreanName;

    ClubType(String koreanName) {
        this.koreanName = koreanName;
    }

    @JsonValue
    public String getKoreanName() {
        return koreanName;
    }

    @JsonCreator
    public static ClubType fromKoreanName(String koreanName) {
        for (ClubType type : ClubType.values()) {
            if (type.koreanName.equals(koreanName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum type " + koreanName);
    }
}