package task5.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourseData {
    private int id;
    private String name;
    private int year;
    private String color;
    private String pantone_value;
}
