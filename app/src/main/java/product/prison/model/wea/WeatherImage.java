package product.prison.model.wea;



import product.prison.R;

public class WeatherImage {
    public static int parseIcon(String strIcon) {
        if (strIcon == null)
            return R.drawable.weather_sunny;
        if ("晴".equals(strIcon) || "sunny".equals(strIcon))
            return R.drawable.weather_sunny;
        if ("晴转多云".equals(strIcon) || "clear to cloudy".equals(strIcon))
            return R.drawable.weather_partly;
//		if ("雾".equals(strIcon) || "fog".equals(strIcon))
//			return R.drawable.weather_mist;
        if ("多云".equals(strIcon) || "cloudy".equals(strIcon)
                || "partly cloudy".equals(strIcon))
            return R.drawable.weather_partly;
        if ("阴".equals(strIcon) || "overcast".equals(strIcon))
            return R.drawable.weather_cloudy;
        if ("小雨".equals(strIcon) || "light rain".equals(strIcon)
                || "rain".equals(strIcon))
            return R.drawable.weather_chance_of_rain;
        if ("中雨".equals(strIcon) || "moderate rain".equals(strIcon))
            return R.drawable.weather_rain;
        if ("大雨".equals(strIcon) || "heavy rain".equals(strIcon)
                || "中到大雨".equals(strIcon))
            return R.drawable.weather_storm;
        if ("雷阵雨".equals(strIcon) || "阵雨".equals(strIcon)
                || "thunder shower".equals(strIcon) || "shower".equals(strIcon))
            return R.drawable.weather_chance_of_tstorm;
        if ("暴雨".equals(strIcon) || "rainstorm".equals(strIcon))
            return R.drawable.weather_thunderstorm;
        if ("小雪".equals(strIcon) || "light snow".equals(strIcon)
                || "snow".equals(strIcon) || "中雪".equals(strIcon)
                || "moderate snow".equals(strIcon)
                || "大雪".equals(strIcon) || "heavy snow".equals(strIcon))
            return R.drawable.weather_snow;
        if ("雨夹雪".equals(strIcon) || "sleet".equals(strIcon))
            return R.drawable.weather_sleet;
        if ("冰雹 ".equals(strIcon) || "hail".equals(strIcon))
            return R.drawable.weather_sleet;

        return R.drawable.weather_sunny;
    }
}
