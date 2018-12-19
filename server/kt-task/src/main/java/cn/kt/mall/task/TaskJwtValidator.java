package cn.kt.mall.task;

import cn.kt.mall.common.exception.UnauthorizedException;
import cn.kt.mall.common.jwt.JwtValidator;
import cn.kt.mall.common.jwt.SubjectInstance;
import org.springframework.stereotype.Component;

//不然会提示cn.kt.mall.common.jwt.JwtValidator没有实例
@Component
public class TaskJwtValidator implements JwtValidator

{
    @Override
    public SubjectInstance verify(String jwt) throws UnauthorizedException {
        return null;
    }

    @Override
    public SubjectInstance verifyShop(String userId) throws UnauthorizedException {
        return null;
    }
}
