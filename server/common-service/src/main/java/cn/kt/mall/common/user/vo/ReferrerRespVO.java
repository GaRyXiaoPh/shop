package cn.kt.mall.common.user.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.kt.mall.common.user.entity.UserEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class ReferrerRespVO implements Serializable{

	private static final long serialVersionUID = 4433755877669530303L;
	private String id;
	private String mobile;
	private String trueName;
	private String avatar;
	@ApiModelProperty("申请店铺数量（如果为0表示是普通会员，否则则是商户）")
	private Integer shopNum;
	@ApiModelProperty("累计收益：莱姆币")
	private BigDecimal historyLem;

	public static ReferrerRespVO fromEntity(UserEntity userEntity) {
		ReferrerRespVO vo = new ReferrerRespVO();
		BeanUtils.copyProperties(userEntity, vo);
		return vo;
	}

	public static List<ReferrerRespVO> fromEntity(List<UserEntity> userEntity) {

		List<ReferrerRespVO> voList = new ArrayList<>();
		for (UserEntity u : userEntity) {
			voList.add(fromEntity(u));
		}

		return voList;
	}

}
