package com.qiuxs.ws.anno;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.xml.ws.WebServiceFeature;

@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface WSReference {

	Class<WebServiceFeature>[] webServiceFeatures() default {};

}
