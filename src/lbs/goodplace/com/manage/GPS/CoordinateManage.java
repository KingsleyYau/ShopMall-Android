package lbs.goodplace.com.manage.GPS;

import android.util.Log;

/**
 * 坐标转换计算类
 * (美国GPS使用的是WGS84的坐标系统
 * 国内必须使用国测局制定的GCJ-02的坐标系统
 * 百度坐标在此基础上，进行了BD-09二次加密措施)
 * 因接口给的是WGS84的坐标，所以需要转化
 * @author Administrator
 *
 */
public class CoordinateManage {	
	//[地球坐标]与[百度坐标]转化代码 
	public static double getLatGG2BD(double gg_lat, double gg_lon){
		return getLatHX2BD(getLatGG2HX(gg_lat,gg_lon),getLngGG2HX(gg_lat,gg_lon));
	}
	
	public static double getLogGG2BD(double gg_lat, double gg_lon){
		return getLogHX2BD(getLatGG2HX(gg_lat,gg_lon),getLngGG2HX(gg_lat,gg_lon));
	}
	
	//[火星坐标]与[百度坐标]转化代码 
	private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0; 
	
	// china--->baidu
	public static double getLatHX2BD(double hx_lat, double hx_lon){
		double x = hx_lon, y = hx_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bd_lat = z * Math.sin(theta) + 0.006;

//		Log.i("zjj", "（火星）坐标   lat:" + hx_lat + "转化（百度）lat:" + bd_lat);
		return bd_lat;
	}
	
	public static double getLogHX2BD(double gg_lat, double gg_lon){
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		
//		Log.i("zjj", "（火星）坐标   lat:" + gg_lon + "转化（百度）lat:" + bd_lon);
		return bd_lon;
	}
	
	// baidu--->china
	public static double getLatBD2HX(double bd_lat, double bd_lon){
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
	    double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
	    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
	    double hx_lat = z * Math.sin(theta);
	    
	    return hx_lat;
	}
	
	public static double getLogBD2HX(double bd_lat, double bd_lon){
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
	    double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
	    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
	    double hx_lon = z * Math.cos(theta);
	    return hx_lon;
	}
	
	//[火星坐标]与[地球坐标]转化代码 
	private static double pi = 3.14159265358979324;
	private static double a = 6378245.0;
	private static double ee = 0.00669342162296594323;

	// World Geodetic System ==> Mars Geodetic System
    public static double getLatGG2HX(double wgLat, double wgLon)
    {
    	double mgLat;
    	
        if (outOfChina(wgLat, wgLon))
        {
            mgLat = wgLat;
//            mgLon = wgLon;
            return mgLat;
        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        mgLat = wgLat + dLat;
//        mgLon = wgLon + dLon;
//        Log.i("zjj", "（GG）坐标   lat:" + wgLat + "转化（火星）lat:" + mgLat);
        return mgLat;
    }
    
    public static double getLngGG2HX(double wgLat, double wgLon)
    {
    	double mgLon;
    	
        if (outOfChina(wgLat, wgLon))
        {
//            mgLat = wgLat;
            mgLon = wgLon;
            return mgLon;
        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
//        mgLat = wgLat + dLat;
        mgLon = wgLon + dLon;
        
//        Log.i("zjj", "（GG）坐标   lng:" + wgLon + "转化（火星）lng:" + mgLon);
        return mgLon;
    }

    static boolean outOfChina(double lat, double lon)
    {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    static double transformLat(double x, double y)
    {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    static double transformLon(double x, double y)
    {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    //自己计算的偏移
    private static double bd2ggLatOffset = 1.0001060560374870356413240623423;
    private static double bd2ggLngOffset = 0.99995198001065201215937969524898;
    
    //baidu-->google
    public static double getLatBD2GG(double bd_lat){
    	return bd_lat * bd2ggLatOffset;
    }
    
    public static double getLngBD2GG(double bd_lng){
    	return bd_lng * bd2ggLngOffset;
    }
    
    //自己计算的偏移
    private static double hx2ggLatOffset = 1.0001209926583383386843776872253;
    private static double hx2ggLngOffset = 0.99995588572638562933422738261192;
    
    //baidu-->google
    public static double getLatHX2GG(double hx_lat){
    	return hx_lat * hx2ggLatOffset;
    }
    
    public static double getLngHX2GG(double hx_lng){
    	return hx_lng * hx2ggLngOffset;
    }
}
