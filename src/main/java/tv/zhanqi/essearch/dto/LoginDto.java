package tv.zhanqi.essearch.dto;


import lombok.Data;

@Data
public class LoginDto {

    private String cmdId;

    private Integer uid;

    private String token;
}
