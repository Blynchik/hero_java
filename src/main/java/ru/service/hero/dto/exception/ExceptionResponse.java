package ru.service.hero.dto.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.service.hero.dto.exception.ExceptionInfo;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ExceptionResponse {

    private List<ExceptionInfo> exceptions;
    private Date date;

    public ExceptionResponse(List<ExceptionInfo> exceptions) {
        this.exceptions = exceptions;
        this.date = new Date();
    }
}
