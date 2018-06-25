package com.dark.api.mvc.adapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Source;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.bind.support.DefaultSessionAttributeStore;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.HandlerMethodSelector;
import org.springframework.web.method.annotation.ErrorsMethodArgumentResolver;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.MapMethodProcessor;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.annotation.ModelMethodProcessor;
import org.springframework.web.method.annotation.RequestHeaderMapMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.annotation.SessionAttributesHandler;
import org.springframework.web.method.annotation.SessionStatusMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;
import org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewResolverMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RedirectAttributesMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestPartMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletResponseMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.UriComponentsBuilderMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ViewMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.ViewNameMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.WebUtils;

public class CustomRequestMappingHandlerAdapter extends
		AbstractHandlerMethodAdapter implements BeanFactoryAware,
		InitializingBean {

	private List<HandlerMethodArgumentResolver> customArgumentResolvers;

	private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;

	private List<ModelAndViewResolver> modelAndViewResolvers;

	private List<HttpMessageConverter<?>> messageConverters;

	private WebBindingInitializer webBindingInitializer;

	private int cacheSecondsForSessionAttributeHandlers = 0;

	private boolean synchronizeOnSession = false;

	private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	private ConfigurableBeanFactory beanFactory;

	private SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();

	private boolean ignoreDefaultModelOnRedirect = false;

	private final Map<Class<?>, SessionAttributesHandler> sessionAttributesHandlerCache = new ConcurrentHashMap<Class<?>, SessionAttributesHandler>();

	private HandlerMethodArgumentResolverComposite argumentResolvers;

	private HandlerMethodArgumentResolverComposite initBinderArgumentResolvers;

	private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

	private final Map<Class<?>, Set<Method>> dataBinderFactoryCache = new ConcurrentHashMap<Class<?>, Set<Method>>();

	private final Map<Class<?>, Set<Method>> modelFactoryCache = new ConcurrentHashMap<Class<?>, Set<Method>>();

	public CustomRequestMappingHandlerAdapter() {

		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
		stringHttpMessageConverter.setWriteAcceptCharset(false);

		this.messageConverters = new ArrayList<HttpMessageConverter<?>>();
		this.messageConverters.add(new ByteArrayHttpMessageConverter());
		this.messageConverters.add(stringHttpMessageConverter);
		this.messageConverters.add(new SourceHttpMessageConverter<Source>());
		this.messageConverters.add(new XmlAwareFormHttpMessageConverter());
	}

	public void setCustomArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		this.customArgumentResolvers = argumentResolvers;
	}

	public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers() {
		return this.customArgumentResolvers;
	}

	public void setArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		if (argumentResolvers == null) {
			this.argumentResolvers = null;
		} else {
			this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
			this.argumentResolvers.addResolvers(argumentResolvers);
		}
	}

	public HandlerMethodArgumentResolverComposite getArgumentResolvers() {
		return this.argumentResolvers;
	}

	public void setInitBinderArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		if (argumentResolvers == null) {
			this.initBinderArgumentResolvers = null;
		} else {
			this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite();
			this.initBinderArgumentResolvers.addResolvers(argumentResolvers);
		}
	}

	public HandlerMethodArgumentResolverComposite getInitBinderArgumentResolvers() {
		return this.initBinderArgumentResolvers;
	}

	public void setCustomReturnValueHandlers(
			List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		this.customReturnValueHandlers = returnValueHandlers;
	}

	public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers() {
		return this.customReturnValueHandlers;
	}

	public void setReturnValueHandlers(
			List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		if (returnValueHandlers == null) {
			this.returnValueHandlers = null;
		} else {
			this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
			this.returnValueHandlers.addHandlers(returnValueHandlers);
		}
	}

	public HandlerMethodReturnValueHandlerComposite getReturnValueHandlers() {
		return this.returnValueHandlers;
	}

	public void setModelAndViewResolvers(
			List<ModelAndViewResolver> modelAndViewResolvers) {
		this.modelAndViewResolvers = modelAndViewResolvers;
	}

	public List<ModelAndViewResolver> getModelAndViewResolvers() {
		return modelAndViewResolvers;
	}

	public void setMessageConverters(
			List<HttpMessageConverter<?>> messageConverters) {
		this.messageConverters = messageConverters;
	}

	public List<HttpMessageConverter<?>> getMessageConverters() {
		return messageConverters;
	}

	public void setWebBindingInitializer(
			WebBindingInitializer webBindingInitializer) {
		this.webBindingInitializer = webBindingInitializer;
	}

	public WebBindingInitializer getWebBindingInitializer() {
		return webBindingInitializer;
	}

	public void setSessionAttributeStore(
			SessionAttributeStore sessionAttributeStore) {
		this.sessionAttributeStore = sessionAttributeStore;
	}

	public void setCacheSecondsForSessionAttributeHandlers(
			int cacheSecondsForSessionAttributeHandlers) {
		this.cacheSecondsForSessionAttributeHandlers = cacheSecondsForSessionAttributeHandlers;
	}

	public void setSynchronizeOnSession(boolean synchronizeOnSession) {
		this.synchronizeOnSession = synchronizeOnSession;
	}

	public void setParameterNameDiscoverer(
			ParameterNameDiscoverer parameterNameDiscoverer) {
		this.parameterNameDiscoverer = parameterNameDiscoverer;
	}

	public void setIgnoreDefaultModelOnRedirect(
			boolean ignoreDefaultModelOnRedirect) {
		this.ignoreDefaultModelOnRedirect = ignoreDefaultModelOnRedirect;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		if (beanFactory instanceof ConfigurableBeanFactory) {
			this.beanFactory = (ConfigurableBeanFactory) beanFactory;
		}
	}

	protected ConfigurableBeanFactory getBeanFactory() {
		return this.beanFactory;
	}

	public void afterPropertiesSet() {
		if (this.argumentResolvers == null) {
			List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
			this.argumentResolvers = new HandlerMethodArgumentResolverComposite()
					.addResolvers(resolvers);
		}
		if (this.initBinderArgumentResolvers == null) {
			List<HandlerMethodArgumentResolver> resolvers = getDefaultInitBinderArgumentResolvers();
			this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite()
					.addResolvers(resolvers);
		}
		if (this.returnValueHandlers == null) {
			List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
			this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite()
					.addHandlers(handlers);
		}
	}

	private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
		List<HandlerMethodArgumentResolver> resolvers = new ArrayList<HandlerMethodArgumentResolver>();

		resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(),
				false));
		resolvers.add(new RequestParamMapMethodArgumentResolver());
		resolvers.add(new PathVariableMethodArgumentResolver());
		resolvers.add(new ServletModelAttributeMethodProcessor(false));
		resolvers.add(new RequestResponseBodyMethodProcessor(
				getMessageConverters()));
		resolvers.add(new RequestPartMethodArgumentResolver(
				getMessageConverters()));
		resolvers
				.add(new RequestHeaderMethodArgumentResolver(getBeanFactory()));
		resolvers.add(new RequestHeaderMapMethodArgumentResolver());
		resolvers.add(new ServletCookieValueMethodArgumentResolver(
				getBeanFactory()));
		resolvers.add(new ExpressionValueMethodArgumentResolver(
				getBeanFactory()));

		resolvers.add(new ServletRequestMethodArgumentResolver());
		resolvers.add(new ServletResponseMethodArgumentResolver());
		resolvers.add(new HttpEntityMethodProcessor(getMessageConverters()));
		resolvers.add(new RedirectAttributesMethodArgumentResolver());
		resolvers.add(new ModelMethodProcessor());
		resolvers.add(new MapMethodProcessor());
		resolvers.add(new ErrorsMethodArgumentResolver());
		resolvers.add(new SessionStatusMethodArgumentResolver());
		resolvers.add(new UriComponentsBuilderMethodArgumentResolver());

		if (getCustomArgumentResolvers() != null) {
			resolvers.addAll(getCustomArgumentResolvers());
		}

		resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(),
				true));
		resolvers.add(new ServletModelAttributeMethodProcessor(true));

		return resolvers;
	}

	private List<HandlerMethodArgumentResolver> getDefaultInitBinderArgumentResolvers() {
		List<HandlerMethodArgumentResolver> resolvers = new ArrayList<HandlerMethodArgumentResolver>();

		resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(),
				false));
		resolvers.add(new RequestParamMapMethodArgumentResolver());
		resolvers.add(new PathVariableMethodArgumentResolver());
		resolvers.add(new ExpressionValueMethodArgumentResolver(
				getBeanFactory()));

		resolvers.add(new ServletRequestMethodArgumentResolver());
		resolvers.add(new ServletResponseMethodArgumentResolver());

		if (getCustomArgumentResolvers() != null) {
			resolvers.addAll(getCustomArgumentResolvers());
		}

		resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(),
				true));

		return resolvers;
	}

	private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
		List<HandlerMethodReturnValueHandler> handlers = new ArrayList<HandlerMethodReturnValueHandler>();

		handlers.add(new ModelAndViewMethodReturnValueHandler());
		handlers.add(new ModelMethodProcessor());
		handlers.add(new ViewMethodReturnValueHandler());
		handlers.add(new HttpEntityMethodProcessor(getMessageConverters()));

		handlers.add(new ModelAttributeMethodProcessor(false));
		handlers.add(new RequestResponseBodyMethodProcessor(
				getMessageConverters()));

		handlers.add(new ViewNameMethodReturnValueHandler());
		handlers.add(new MapMethodProcessor());

		if (getCustomReturnValueHandlers() != null) {
			handlers.addAll(getCustomReturnValueHandlers());
		}

		if (!CollectionUtils.isEmpty(getModelAndViewResolvers())) {
			handlers.add(new ModelAndViewResolverMethodReturnValueHandler(
					getModelAndViewResolvers()));
		} else {
			handlers.add(new ModelAttributeMethodProcessor(true));
		}

		return handlers;
	}

	@Override
	protected boolean supportsInternal(HandlerMethod handlerMethod) {
		return supportsMethodParameters(handlerMethod.getMethodParameters())
				&& supportsReturnType(handlerMethod.getReturnType());
	}

	private boolean supportsMethodParameters(MethodParameter[] methodParameters) {
		for (MethodParameter methodParameter : methodParameters) {
			if (!this.argumentResolvers.supportsParameter(methodParameter)) {
				return false;
			}
		}
		return true;
	}

	private boolean supportsReturnType(MethodParameter methodReturnType) {
		return (this.returnValueHandlers.supportsReturnType(methodReturnType) || Void.TYPE
				.equals(methodReturnType.getParameterType()));
	}

	@Override
	protected long getLastModifiedInternal(HttpServletRequest request,
			HandlerMethod handlerMethod) {
		return -1;
	}

	@Override
	protected final ModelAndView handleInternal(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handlerMethod)
			throws Exception {

		if (getSessionAttributesHandler(handlerMethod).hasSessionAttributes()) {
			checkAndPrepare(request, response,
					this.cacheSecondsForSessionAttributeHandlers, true);
		} else {
			checkAndPrepare(request, response, true);
		}

		if (this.synchronizeOnSession) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				Object mutex = WebUtils.getSessionMutex(session);
				synchronized (mutex) {
					return invokeHandlerMethod(request, response, handlerMethod);
				}
			}
		}

		return invokeHandlerMethod(request, response, handlerMethod);
	}

	private SessionAttributesHandler getSessionAttributesHandler(
			HandlerMethod handlerMethod) {
		Class<?> handlerType = handlerMethod.getBeanType();
		SessionAttributesHandler sessionAttrHandler = this.sessionAttributesHandlerCache
				.get(handlerType);
		if (sessionAttrHandler == null) {
			synchronized (this.sessionAttributesHandlerCache) {
				sessionAttrHandler = this.sessionAttributesHandlerCache
						.get(handlerType);
				if (sessionAttrHandler == null) {
					sessionAttrHandler = new SessionAttributesHandler(
							handlerType, sessionAttributeStore);
					this.sessionAttributesHandlerCache.put(handlerType,
							sessionAttrHandler);
				}
			}
		}
		return sessionAttrHandler;
	}

	private ModelAndView invokeHandlerMethod(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handlerMethod)
			throws Exception {

		ServletWebRequest webRequest = new ServletWebRequest(request, response);

		WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
		ModelFactory modelFactory = getModelFactory(handlerMethod,
				binderFactory);
		CustomServletInvocableHandlerMethod requestMappingMethod = createRequestMappingMethod(
				handlerMethod, binderFactory);

		ModelAndViewContainer mavContainer = new ModelAndViewContainer();
		mavContainer.addAllAttributes(RequestContextUtils
				.getInputFlashMap(request));
		modelFactory.initModel(webRequest, mavContainer, requestMappingMethod);
		mavContainer
				.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);

		requestMappingMethod.invokeAndHandle(webRequest, mavContainer);
		modelFactory.updateModel(webRequest, mavContainer);

		if (mavContainer.isRequestHandled()) {
			return null;
		} else {
			ModelMap model = mavContainer.getModel();
			ModelAndView mav = new ModelAndView(mavContainer.getViewName(),
					model);
			if (!mavContainer.isViewReference()) {
				mav.setView((View) mavContainer.getView());
			}
			if (model instanceof RedirectAttributes) {
				Map<String, ?> flashAttributes = ((RedirectAttributes) model)
						.getFlashAttributes();
				RequestContextUtils.getOutputFlashMap(request).putAll(
						flashAttributes);
			}
			return mav;
		}
	}

	private CustomServletInvocableHandlerMethod createRequestMappingMethod(
			HandlerMethod handlerMethod, WebDataBinderFactory binderFactory) {
		CustomServletInvocableHandlerMethod requestMethod;
		requestMethod = new CustomServletInvocableHandlerMethod(
				handlerMethod.getBean(), handlerMethod.getMethod());
		requestMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
		requestMethod
				.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
		requestMethod.setDataBinderFactory(binderFactory);
		requestMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
		return requestMethod;
	}

	private ModelFactory getModelFactory(HandlerMethod handlerMethod,
			WebDataBinderFactory binderFactory) {
		SessionAttributesHandler sessionAttrHandler = getSessionAttributesHandler(handlerMethod);
		Class<?> handlerType = handlerMethod.getBeanType();
		Set<Method> methods = this.modelFactoryCache.get(handlerType);
		if (methods == null) {
			methods = HandlerMethodSelector.selectMethods(handlerType,
					MODEL_ATTRIBUTE_METHODS);
			this.modelFactoryCache.put(handlerType, methods);
		}
		List<InvocableHandlerMethod> attrMethods = new ArrayList<InvocableHandlerMethod>();
		for (Method method : methods) {
			InvocableHandlerMethod attrMethod = new InvocableHandlerMethod(
					handlerMethod.getBean(), method);
			attrMethod
					.setHandlerMethodArgumentResolvers(this.argumentResolvers);
			attrMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
			attrMethod.setDataBinderFactory(binderFactory);
			attrMethods.add(attrMethod);
		}
		return new ModelFactory(attrMethods, binderFactory, sessionAttrHandler);
	}

	private WebDataBinderFactory getDataBinderFactory(
			HandlerMethod handlerMethod) throws Exception {
		Class<?> handlerType = handlerMethod.getBeanType();
		Set<Method> methods = this.dataBinderFactoryCache.get(handlerType);
		if (methods == null) {
			methods = HandlerMethodSelector.selectMethods(handlerType,
					INIT_BINDER_METHODS);
			this.dataBinderFactoryCache.put(handlerType, methods);
		}
		List<InvocableHandlerMethod> binderMethods = new ArrayList<InvocableHandlerMethod>();
		for (Method method : methods) {
			InvocableHandlerMethod binderMethod = new InvocableHandlerMethod(
					handlerMethod.getBean(), method);
			binderMethod
					.setHandlerMethodArgumentResolvers(this.initBinderArgumentResolvers);
			binderMethod.setDataBinderFactory(new DefaultDataBinderFactory(
					this.webBindingInitializer));
			binderMethod
					.setParameterNameDiscoverer(this.parameterNameDiscoverer);
			binderMethods.add(binderMethod);
		}
		return createDataBinderFactory(binderMethods);
	}

	protected ServletRequestDataBinderFactory createDataBinderFactory(
			List<InvocableHandlerMethod> binderMethods) throws Exception {
		return new ServletRequestDataBinderFactory(binderMethods,
				getWebBindingInitializer());
	}

	public static final MethodFilter INIT_BINDER_METHODS = new MethodFilter() {

		public boolean matches(Method method) {
			return AnnotationUtils.findAnnotation(method, InitBinder.class) != null;
		}
	};

	public static final MethodFilter MODEL_ATTRIBUTE_METHODS = new MethodFilter() {

		public boolean matches(Method method) {
			return ((AnnotationUtils.findAnnotation(method,
					RequestMapping.class) == null) && (AnnotationUtils
					.findAnnotation(method, ModelAttribute.class) != null));
		}
	};

}