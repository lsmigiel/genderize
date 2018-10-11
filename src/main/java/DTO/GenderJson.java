package DTO;

import lombok.Data;

@Data
public class GenderJson {
    String name;
    String gender;
    Double probability;
    Integer count;
}
