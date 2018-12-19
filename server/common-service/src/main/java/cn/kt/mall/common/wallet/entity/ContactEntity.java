package cn.kt.mall.common.wallet.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class ContactEntity implements Serializable {
    private static final long serialVersionUID = -8242919756593554248L;

    @ApiModelProperty(notes = "id", dataType = "string")
    private String id;
    @ApiModelProperty(notes = "用户id", dataType = "string")
    private String userId;
    @ApiModelProperty(notes = "用户昵称", dataType = "string")
    private String nick;
    @ApiModelProperty(notes = "用户地址", dataType = "string")
    private String address;
    @ApiModelProperty(notes = "创建时间", dataType = "string")
    private Date createTime;

    public ContactEntity(String id, String userId, String nick, String address){
        this.id = id;
        this.userId = userId;
        this.nick = nick;
        this.address = address;
        this.createTime = new Date();
    }
}
