package org.quantum.paramresolver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.quantum.dao.UserDao;
import org.quantum.service.UserService;

public class UserServiceParamResolver implements ParameterResolver {

	@Override
	public boolean supportsParameter(ParameterContext parameterContext,
									 ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext.getParameter().getType() == UserService.class;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext,
								   ExtensionContext extensionContext) throws ParameterResolutionException {
		var store = extensionContext.getStore(Namespace.GLOBAL);
		return store.getOrComputeIfAbsent(UserService.class, k -> new UserService(new UserDao()));
	}

}
