package com.webflux.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectInput {
    private String subjectName;
    private double marksObtained;
}