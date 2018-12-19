package cn.kt.mall.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JsonTreeVO {

    private String id;
    private String pid;
    private String text;
    private String img;
    private String state;
    private Integer sort;
    private List<JsonTreeVO> children;
}
