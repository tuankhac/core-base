package com.vmo.core.common.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
public class LocaleServiceImpl implements LocaleService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private HttpServletRequest request;

    public String getMessage(String code){
        String language = request.getHeader("Accept-Language");
        Locale locale = null;
        if (language == null || language.isEmpty()) {
            locale = Locale.US;
            return messageSource.getMessage(code, null, locale);
        }else{
            switch (language){
                case "en":
                    return messageSource.getMessage(code, null, Locale.US);
                case "vi":
                    return messageSource.getMessage(code, null, new Locale("vi", "VN"));
                case "fr":
                    return messageSource.getMessage(code, null, Locale.FRANCE);
            }
        }
        return messageSource.getMessage(code, null, Locale.US);
    }

    public String getMessage(String code, HttpServletRequest request){
        return messageSource.getMessage(code, null, request.getLocale());
    }

    public String getMessage(String code, String[] args){
        return messageSource.getMessage(code, args, request.getLocale());
    }
}
