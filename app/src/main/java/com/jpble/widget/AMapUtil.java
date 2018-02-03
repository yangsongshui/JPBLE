
package com.jpble.widget;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class AMapUtil {
    public static float calculateLineDistance(double lat0, double lng0, double lat1, double lng1) {
        final double v2 = 0.01745329251994329D;
        double v4 = lng0;
        double v6 = lat0;
        double v8 = lng1;
        double v10 = lat1;
        v4 *= v2;
        v6 *= v2;
        v8 *= v2;
        v10 *= v2;
        double v12 = Math.sin(v4);
        double v14 = Math.sin(v6);
        double v16 = Math.cos(v4);
        double v18 = Math.cos(v6);
        double v20 = Math.sin(v8);
        double v22 = Math.sin(v10);
        double v24 = Math.cos(v8);
        double v26 = Math.cos(v10);

        double[] var28 = new double[3];
        double[] var29 = new double[3];
        var28[0] = v18 * v16;
        var28[1] = v18 * v12;
        var28[2] = v14;
        var29[0] = v26 * v24;
        var29[1] = v26 * v20;
        var29[2] = v22;
        double v30 = Math.sqrt((var28[0] - var29[0]) * (var28[0] - var29[0]) + (var28[1] - var29[1]) * (var28[1] - var29[1]) + (var28[2] - var29[2]) * (var28[2] - var29[2]));
        return (float) (Math.asin(v30 / 2.0D) * 1.2742001579844E7D);
    }


    public static List<LatLng> decode(final String encodePath) {
        int len = encodePath.length();
        final List<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;
        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodePath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodePath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;

            } while (b >= 0x1f);

            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));


        }
        return path;

    }


    public static String encode(final List<LatLng> path) {
        long lastLat = 0;
        long lastLng = 0;
        final StringBuffer result = new StringBuffer();
        if (path.size() > 0)
            for (final LatLng point : path) {
                long lat = Math.round(point.latitude * 1e5);
                long lng = Math.round(point.longitude * 1e5);

                long dLat = lat - lastLat;
                long dLng = lng - lastLng;

                encode(dLat, result);
                encode(dLng, result);

                lastLat = lat;
                lastLng = lng;

            }
        return result.toString();
    }

    public static String encode(final LatLng point) {
        long lastLat = 0;
        long lastLng = 0;
        final StringBuffer result = new StringBuffer();

        long lat = Math.round(point.latitude * 1e5);
        long lng = Math.round(point.longitude * 1e5);

        long dLat = lat - lastLat;
        long dLng = lng - lastLng;

        encode(dLat, result);
        encode(dLng, result);

        lastLat = lat;
        lastLng = lng;


        return result.toString();
    }


    private static void encode(long v, StringBuffer result) {
        v = v < 0 ? ~(v << 1) : v << 1;
        while (v >= 0x20) {
            result.append(Character.toChars((int) ((0x20 | (v & 0x1f)) + 63)));
            v >>= 5;
        }
        result.append(Character.toChars((int) (v + 63)));

    }


}
