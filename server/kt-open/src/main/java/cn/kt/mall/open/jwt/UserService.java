package cn.kt.mall.open.jwt;

import org.springframework.stereotype.Service;

import cn.kt.mall.common.exception.ForbiddenException;
import cn.kt.mall.common.exception.UnauthorizedException;
import cn.kt.mall.common.jwt.JwtValidator;
import cn.kt.mall.common.jwt.PermissionValidator;
import cn.kt.mall.common.jwt.SubjectInstance;

@Service
public class UserService implements JwtValidator, PermissionValidator {

	@Override
	public void check(String userId, String[] roles, String[] privileges) throws ForbiddenException {

	}

	@Override
	public SubjectInstance verify(String jwt) throws UnauthorizedException {
		return null;
	}

	@Override
	public SubjectInstance verifyShop(String userId) throws UnauthorizedException {
		return null;
	}

}
