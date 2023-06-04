package com.momo.theta.utils;

import com.alibaba.fastjson.JSONObject;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.InvalidPropertyException;

/**
 * <p>Map类型转换为Bean自定义类型，需要指定泛型</p>
 */
public class MapToBean {

  private static final Logger logger = LoggerFactory.getLogger(MapToBean.class);

  public static void mapToBean(Map<String, Object> paramMap, Object bean) {
    logger.debug("covert map to bean, map: " + paramMap + ", bean: " + bean);
    try {
      BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
        String propertyName = propertyDescriptor.getName();
        if (!paramMap.containsKey(propertyName)) {
          continue;
        }
        Object arg = paramMap.get(propertyName);
        if (arg == null) {
          continue;
        }
        Object value = convert(arg, propertyDescriptor.getPropertyType());
        Method method = propertyDescriptor.getWriteMethod();
        method.invoke(bean, value);
      }
    } catch (Exception e) {
      throw new RuntimeException("cannot covert map:" + paramMap + " to bean:" + bean, e);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private static <T> T convert(Object value, Class<T> type) throws IntrospectionException {
    if (type.isInstance(value)) {
      return type.cast(value);
    }
    String strValue = value.toString();
    if (strValue.length() == 0) {
      return null;
    }
    Object convertValue = null;
    if (type.isAssignableFrom(String.class)) {
      convertValue = strValue;
    } else if (type.isAssignableFrom(short.class)) {
      convertValue = Short.parseShort(strValue);
    } else if (type.isAssignableFrom(int.class)) {
      convertValue = Integer.parseInt(strValue);
    } else if (type.isAssignableFrom(long.class)) {
      convertValue = Long.parseLong(strValue);
    } else if (type.isAssignableFrom(float.class)) {
      convertValue = Float.parseFloat(strValue);
    } else if (type.isAssignableFrom(double.class)) {
      convertValue = Double.parseDouble(strValue);
    } else if (type.isAssignableFrom(boolean.class)) {
      convertValue = Boolean.parseBoolean(strValue);
    } else if (type.isAssignableFrom(BigDecimal.class)) {
      convertValue = new BigDecimal(strValue);
    } else if (type.isEnum()) {
      Class<? extends Enum> enumType = (Class<? extends Enum>) type;
      convertValue = Enum.valueOf(enumType, strValue);
    } else if (type.isAssignableFrom(Date.class)) {
      convertValue = new Date(Long.parseLong(strValue));
    } else if (type.isArray()) {
      Object[] nodeArray = null;
      if (value instanceof List) {
        List nodeList = (List) value;
        nodeArray = new Object[nodeList.size()];
        nodeList.toArray(nodeArray);
      } else {
        nodeArray = (Object[]) value;
      }
      if (nodeArray == null || nodeArray.length == 0) {
        return null;
      }
      Object toNodeArray = Array.newInstance(type.getComponentType(), nodeArray.length);
      Class componentType = type.getComponentType();
      for (int i = 0; i < nodeArray.length; i++) {
        Object instance = nodeArray[i];
        Object nodeValue = convert(instance, componentType);
        ((Object[]) toNodeArray)[i] = nodeValue;
      }
      convertValue = toNodeArray;
    } else if (value instanceof Map) {
      convertValue = mapToBean((Map) value, type);
    } else {
      try {

        //convertValue = JSONObject.parseObject(value.toString(), type);
        convertValue = mapToBean(JSONObject.parseObject(value.toString()), type);
      } catch (Exception e) {
        throw new RuntimeException("cannot resolve type " + type, e);
      }
    }
    return (T) convertValue;
  }

  public static <T> T mapToBean(Map<String, Object> paramMap, Class<T> beanType)
      throws IntrospectionException {
    if (beanType.isPrimitive() || beanType.isAssignableFrom(Date.class)
        || beanType.isAssignableFrom(Number.class) || beanType.isEnum()) {
      throw new IllegalStateException(
          "Unsupported beanType '" + beanType + "' for map converting bean");
    }
    BeanWrapper bw = new BeanWrapperImpl(beanType);
    for (String name : paramMap.keySet()) {
      PropertyDescriptor pd = null;
      try {
        pd = bw.getPropertyDescriptor(name);
      } catch (InvalidPropertyException e) {
        if (logger.isWarnEnabled()) {
          logger.warn(
              "bean of {} no such property named {},Ensure this beanType is proper use, or the value of input-parameter '{}' is no use",
              beanType, name, name);
        }
        continue;
      }
      Object value = paramMap.get(name);
      if (value == null) {
        continue;
      }
      Class<?> type = pd.getPropertyType();
      Object convertValue = null;
      Exception convertException = null;
      try {
        convertValue = convert(value, type);
      } catch (NumberFormatException e) {
        convertException = e;
      } catch (IllegalArgumentException e) {
        convertException = e;
      }
      if (convertException != null) {
        throw new IllegalArgumentException("'" + value + "' error converting " + type,
            convertException);
      } else if (convertValue == null) {
        throw new IllegalArgumentException("'" + value + "' unable converting " + type);
      }
      bw.setPropertyValue(pd.getName(), convertValue);
    }
    return (T) bw.getWrappedInstance();
  }

  public static Class<?> instanceType(PropertyDescriptor pd) {

    if (!pd.getPropertyType().isAssignableFrom(List.class)) {
      return null;
    }
    Type returnType = pd.getReadMethod().getGenericReturnType();
    Class<?> instanceType = null;
    if (returnType instanceof ParameterizedType) {
      Type[] args = ((ParameterizedType) returnType).getActualTypeArguments();
      if (args[0] instanceof Class) {
        instanceType = (Class<?>) args[0];
      }
    }
    return instanceType;
  }

}
