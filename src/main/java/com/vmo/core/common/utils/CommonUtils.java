package com.vmo.core.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.vmo.core.common.CommonConstants;
import com.vmo.core.common.exception.ExceptionResponse;
import com.vmo.core.common.json.*;
import com.vmo.core.modules.models.response.ErrorCode;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class CommonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CommonUtils.class);

    public static <T> void stream(List<T> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) == null) {
                    if (list.get(j) == null) {
                        list.remove(j);
                        j--;
                    }
                } else if (list.get(i).equals(list.get(j))) {
                    list.remove(j);
                    j--;
                }
            }
        }
    }

    public static <T> void stream(List<T> list, Compare<T, T> comp) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (comp.compare(list.get(i), list.get(j)) == 0) {
                    list.remove(j);
                    j--;
                }
            }
        }
    }


    public static String getKeyHash() {
        return new MessageDigestPasswordEncoder("MD5").encode(Date.from(Instant.now()) + CommonConstants.MD5_SALT)
//        return new Md5PasswordEncoder().encodePassword(Date.from(Instant.now()).toString(), Constants.MD5_SALT)
                + RandomStringUtils.random(3, "adf@slaskdjg@349556fhdfpaosdfhahfklashdflkashdflkasjdhf");
    }

    public static String getKeyHashEndNameFileImage() {
        String key = new MessageDigestPasswordEncoder("MD5").encode(Date.from(Instant.now()) + CommonConstants.MD5_SALT)
//                new Md5PasswordEncoder().encodePassword(Date.from(Instant.now()).toString(), Constants.MD5_SALT)
                + RandomStringUtils.random(3, "adf@slaskdjg@349556fhdfpaosdfhahfklashdflkashdflkasjdhf");
        if (key.length() <= 6) {
            return key;
        } else {
            int start = key.length() - 6;
            return key.substring(start);
        }
    }


    public static <T> T findObject(List<T> list, Action1Result<T, Boolean> actionCheck) {
        for (T t : list) {
            if (actionCheck.call(t)) {
                return t;
            }
        }
        return null;
    }

    public static <T> List<T> findObjects(List<T> list, Action1Result<T, Boolean> actionCheck) {
        List<T> newList = new ArrayList<>();
        for (T t : list) {
            if (actionCheck.call(t)) {
                newList.add(t);
            }
        }
        return newList;
    }

    public static <T1, T2> List<T1> findObjects(List<T1> list1, List<T2> list2, Compare<T1, T2> compare) {
        List<T1> newList = new ArrayList<>();
        for (T1 t1 : list1) {
            for (T2 t2 : list2) {
                if (compare.compare(t1, t2) == 0) {
                    newList.add(t1);
                    break;
                }
            }
        }
        return newList;
    }

    public static <T> boolean contain(T[] ts, T t, Compare<T, T> compare) {
        for (T t1 : ts) {
            if (compare.compare(t1, t) == 0) {
                return true;
            }
        }
        return false;
    }

    public static Observable<Object> getObObj(ActionResult<Object> action) {
        return Observable.create(em -> {
            em.onNext(action.call());
            em.onComplete();
        }).subscribeOn(Schedulers.newThread());
    }

    public static <T> Observable<T> getOb(ActionResult<T> action) {
        return Observable.create((ObservableOnSubscribe<T>) em -> {
            em.onNext(action.call());
            em.onComplete();
        }).subscribeOn(Schedulers.newThread());
    }

    public static <T> Observable<List<T>> getObList(ActionResult<List<T>> action) {
        return Observable.create((ObservableOnSubscribe<List<T>>) em -> {
            em.onNext(action.call());
            em.onComplete();
        }).subscribeOn(Schedulers.newThread());
    }

    public static MappingJackson2HttpMessageConverter converterJack() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setPrettyPrint(false);
        //todo add (although at least one Creator exists): no String-argument constructor/factory method to deserialize from String value
        converter.getObjectMapper().enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        updateTimeConvertObjectMapper(converter.getObjectMapper());
        return converter;
    }

    public static void updateTimeConvertObjectMapper(ObjectMapper objectMapper) {
        SimpleModule javaTimeModule = new SimpleModule();

        // date
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        // time
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
//        javaTimeModule.addDeserializer(Long.class, new LongDeserializer());

        //other
//        javaTimeModule.addDeserializer(ConditionInventory.class, new ConditionInventoryDeserializer());

        //month
        javaTimeModule.addSerializer(YearMonth.class, new YearMonthSerializer());

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(javaTimeModule);
    }

    public static MappingJackson2HttpMessageConverter converterJacksonOtherService() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        updateObjectMapperOtherService(converter.getObjectMapper());
        return converter;
    }

    public static void updateObjectMapperOtherService(ObjectMapper objectMapper) {
        SimpleModule javaTimeModule = new SimpleModule();

        // date
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());

        // time
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializerEbay());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializerEbay());

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(javaTimeModule);
    }

    public static void updateTimeConvertXmlMapperEbay(XmlMapper objectMapper) {
        SimpleModule javaTimeModule = new SimpleModule();

        // date
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());

        // time
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializerEbay());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializerEbay());
//        javaTimeModule.addDeserializer(Long.class, new LongDeserializer());


        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(javaTimeModule);
    }

    public static String toXmlString(Object object, XmlMapper xmlMapper) {
        try {
            return xmlMapper.writeValueAsString(object).replace(" xmlns=\"\"", "");
        } catch (Exception e) {
            LOG.error("Fail to map request object to xml string");
            return null;
        }
    }

    public static <T> T toObjectXml(XmlMapper xmlMapper, String contentXml, Class<T> clazz) {
        try {
            return xmlMapper.readValue(contentXml, clazz);
        } catch (Exception e) {
            LOG.error("Fail to map request object to xml string");
            e.printStackTrace();
            return null;
        }
    }

    public static String readString(InputStream in) throws IOException {
        byte[] b = new byte[1024];
        StringBuilder content = new StringBuilder();
        int le = in.read(b);
        while (le >= 0) {
            content.append(new String(b, 0, le));
            le = in.read(b);
        }
        in.close();
        return content.toString();
    }


    public static <T1, T2> List<T1> findNotContain(List<T1> first, List<T2> second, Compare<T1, T2> compare) {
        List<T1> result = new ArrayList<>();
        for (T1 t1 : first) {
            boolean isContain = false;
            for (T2 t2 : second) {
                if (compare.compare(t1, t2) == 0) {
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                result.add(t1);
            }
        }
        return result;
    }


    public static <T1, T2> boolean contain(List<T1> t1s, T2 t2, Compare<T1, T2> compare) {
        for (T1 t1 : t1s) {
            if (compare.compare(t1, t2) == 0) {
                return true;
            }
        }
        return false;
    }


    public static void checkEmailFormatValid(String email) throws ExceptionResponse {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(email).matches()) {
//            throw new ExceptionResponse(new ObjectError(ObjectError.ERROR_PARAM, "Email is invalid"),
//                    HttpStatus.BAD_REQUEST);
            throw new ExceptionResponse(
                    "Email is invalid",
                    ErrorCode.INVALID_PARAM
            );
        }
    }


    public static <T> T findMin(List<T> values, Compare<T, T> compare) {
        T min = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            if (compare.compare(min, values.get(i)) > 0) {
                min = values.get(i);
            }
        }
        return min;
    }

    public static <T> T findMax(List<T> values, Compare<T, T> compare) {
        T max = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            if (compare.compare(max, values.get(i)) < 0) {
                max = values.get(i);
            }
        }
        return max;
    }

    public static String convertToListCallApi(List<String> ids) {
        String idsString = "";
        if (ids.size() > 0) {
            idsString = ids.get(0);
            if (ids.size() > 1) {
                for (int i = 1; i < ids.size(); i++) {
                    idsString = idsString + "," + ids.get(i);
                }
            }
        }
        return idsString;
    }

    public static int countNumberCharacter(String value, char c, boolean isMatchLowUpCase) {
        if (isMatchLowUpCase) {
            value = value.toUpperCase();
            c = ("" + c).toUpperCase().charAt(0);
        }
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == c) {
                count += 1;
            }
        }
        return count;
    }

    public static boolean isNumber(String value) {
        if (value == null || value.equals("")) {
            return false;
        }
        if (value.contains(".")) {
            if (countNumberCharacter(value, '.', false) > 1) {
                return false;
            }
            if (value.charAt(value.length() - 1) == 'f' || value.charAt(value.length() - 1) == 'F' ||
                    value.charAt(value.length() - 1) == 'l' || value.charAt(value.length() - 1) == 'L') {
                value = value.substring(0, value.length() - 1);
            }
        } else {
            if (value.charAt(value.length() - 1) == 'l' || value.charAt(value.length() - 1) == 'L') {
                value = value.substring(0, value.length() - 1);
            }
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if ((c < '0' || c > '9') && c != '.') {
                return false;
            }
        }
        return true;

    }

    public static boolean equals(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        if (o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    public static <T, Tc> Tc getValue(List<T> objects, Action1Result<T, Boolean> actionCheck, Action1Result<T, Tc> actionGetValue) {
        T t = findObject(objects, actionCheck);
        if (t == null) {
            return null;
        }
        return actionGetValue.call(t);
    }

    public static <T, ItemResult> List<ItemResult> findListExtract(List<List<T>> oss, Action1Result<T, ItemResult> action) {
        List<ItemResult> results = new ArrayList<>();
        for (List<T> os : oss) {
            for (T o : os) {
                results.add(action.call(o));
            }
        }
        return results;
    }

    public static boolean needUpdate(Object param, Object current) {
        return param != null && !equals(param, current);
    }

    public static String defaultEmpty(String s) {
        if (StringUtils.isBlank(s)) {
            return "";
        }
        return s;
    }

    public static String defaultEmpty(String s, String prefix, String suffix) {
        if (StringUtils.isBlank(s)) {
            return "";
        }
        return defaultEmpty(prefix) + s + defaultEmpty(suffix);
    }

    public static <T> T safeRunQuery(ActionResult<T> query, Session session) {
        if (session == null) {
            throw new NullPointerException();
        }

        try {
            return query.call();
        } finally {
            session.close();
        }
    }

    /**
     * @param uri
     * @return url without query
     */
    public static String toMinimizeUrl(URI uri) {
        return defaultEmpty(uri.getScheme(), null, "://")
                + defaultEmpty(uri.getHost(), null, null) //skip auth user:pass@host
                + defaultEmpty(uri.getPort() > 0 ? uri.getPort() + "" : null, ":", null)
                + defaultEmpty(uri.getPath(), null, null)
                + defaultEmpty(uri.getFragment(), "#", null);
    }

    public static String getClientIP(HttpServletRequest request) {
        String clientIP = null;
        String headerXForward = request.getHeader(CommonConstants.HEADER_PROXY_FORWARD_IP);
        if (StringUtils.isNotBlank(headerXForward)) {
            if (headerXForward.contains(",")) {
                clientIP = headerXForward.split(",")[0];
            } else {
                clientIP = headerXForward;
            }
        } else {
            clientIP = request.getRemoteHost();
        }
        return clientIP;
    }

    public static boolean equalsPrices(Double oldPrice, Double newPrice) {
        if (oldPrice == null) {
            return newPrice == null;
        } else {
            if (newPrice == null) {
                return false;
            } else {
                oldPrice = Math.round(oldPrice * 100.0) / 100.0;
                newPrice = Math.round(newPrice * 100.0) / 100.0;
                return Math.abs(newPrice - oldPrice) <= 0.01;
            }
        }
    }
}
