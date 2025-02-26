package com.example.flyfishshop.model.search;

import com.example.flyfishshop.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class SearchUserModel extends User {
    private String keyword;
    private String birthdayRange;
    private LocalDate birthdayFrom;//起始日期
    private LocalDate birthdayTo;//截止日期


    public void setBirthdayRange(String birthdayRange) {
        this.birthdayRange = birthdayRange;
        if (StringUtils.hasText(birthdayRange)) {
            String[] range = birthdayRange.split("\\s*~\\s*");
            birthdayFrom = LocalDate.parse(range[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            birthdayTo = LocalDate.parse(range[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }
}
