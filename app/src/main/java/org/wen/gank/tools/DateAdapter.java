package org.wen.gank.tools;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * created by Jiahui.wen 2017-07-26
 */
public class DateAdapter {

    //2017-07-26T16:57:39.343Z  yyyy-MM-dd HH:mm:ss
    private static final String pattern = "yyyy-MM-dd'T'HH:mm:ss.S'Z'";

    private SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);

    @ToJson
    public String toJson(Date date) {
        return dateFormat.format(date);
    }

    @FromJson
    public Date fromJson(String json) {
        try {
            return dateFormat.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
