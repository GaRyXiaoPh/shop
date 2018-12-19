package cn.kt.mall.web.shop.interceptor;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import cn.kt.mall.common.exception.BusinessException;

/**
 * 入参校验拦截切面
 * 
 * @author TS
 *
 */
@Component
@Aspect
public class RequestParamValidAspect {
	private static final Logger logger = LoggerFactory.getLogger(RequestParamValidAspect.class);
	ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final ExecutableValidator validator = factory.getValidator().forExecutables();

	@Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void ctrlPointcut() {
	}

	@Before("ctrlPointcut()")
	public void before(JoinPoint point) throws Exception {
		logger.debug("======come into the controller point  ===========");
		Object controller = point.getThis();
		Object[] checkParams = point.getArgs();
		Method method = ((MethodSignature) point.getSignature()).getMethod();
		Set<ConstraintViolation<Object>> validResult = checkParameters(controller, method, checkParams);
		if (!CollectionUtils.isEmpty(validResult)) {
			throw new BusinessException(1, getErrorMsg(method, validResult));
		}
	}

	/**
	 * @Description get error message for parameters checking
	 * @param method
	 * @param validResult
	 * @return
	 */
	private String getErrorMsg(Method method, Set<ConstraintViolation<Object>> validResult) {
		Iterator<ConstraintViolation<Object>> iterator = validResult.iterator();
		String msg = null;
		while (iterator.hasNext()) {
			ConstraintViolation<Object> violation = iterator.next();
			if (violation.getMessage().startsWith("{") && violation.getMessage().endsWith("}")) {
				msg = violation.getMessage();
				msg = msg.substring(1, msg.length() - 1);
			} else {
				msg = violation.getMessage();
			}
			return msg;
		}
		return msg;
	}

	/**
	 * @Description check the parameters
	 * @param obj
	 * @param method
	 * @param params
	 * @param <T>
	 * @return
	 * @throws Exception
	 *             if the usage range is out of the annotation Target will throw
	 *             javax.validation.UnexpectedTypeException and so on.
	 */
	private <T> Set<ConstraintViolation<T>> checkParameters(T obj, Method method, Object[] params) throws Exception {
		try {
			return validator.validateParameters(obj, method, params);
		} catch (Exception e) {
			logger.error(
					"fail to check the controller parameters, please check whether the validator annotations are correct!",
					e);
			throw new Exception("参数验证异常.");
		}
	}
}
