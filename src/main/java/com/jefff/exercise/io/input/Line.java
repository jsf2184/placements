package com.jefff.exercise.io.input;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Line {
    String text;
    int lineNumber;
}
