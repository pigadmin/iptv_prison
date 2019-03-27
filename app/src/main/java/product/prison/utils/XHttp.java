package product.prison.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import product.prison.R;

public class XHttp {
    /**
     * 请求头参数
     *
     * @param context 上下文
     * @param url     网址
     * @param Type    类型
     * @return
     */
    public static RequestParams getHeader(Context context, String url, String Type) {

        String appId = SpUtils.getString(context, "appid", "");
        String userId = SpUtils.getString(context, "userId", "");
        String token = SpUtils.getString(context, "token", "");

        RequestParams params = new RequestParams();
        params.addHeader("Content-Type", Type);
        params.addHeader("token", token);
        params.addHeader("appId", appId);
        params.addHeader("userId", userId);

        return params;
    }

    /**
     * x get 请求方法
     *
     * @param params
     * @param context
     * @param from
     */
    public static void XGET(RequestParams params, final Context context, final FromNetDataBack fromNetDataBack, final String from) {

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });

    }

    /**
     * x post 请求方法
     *
     * @param params  请求体
     * @param context 上下文
     * @param from    请求位置
     */
    public static void XPOST(RequestParams params, final Context context, final FromNetDataBack fromNetDataBack, final String from) {
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("tag", "onSuccess: post---" + ex);

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //数据返回接口
    public interface FromNetDataBack {
        /**
         * 网络请求成功 正常返回数据
         *
         * @param data
         * @param from
         */
        void onNetSuccess(String data, String from);

        /**
         * 网络请求成功 错误码
         *
         * @param code
         * @param from
         */
        void onNetSuccessYiviErrorCode(String code, String from);

        /**
         * 请求失败 网络错误码
         *
         * @param ex
         * @param from
         */
        void onNetError(Throwable ex, String from);


    }


    //绑定图片方法
    public static void XBindImageview(ImageView img, String netUrl) {
        ImageOptions options = new ImageOptions.Builder().setFailureDrawableId(R.mipmap.ic_launcher).setLoadingDrawableId(R.mipmap.ic_launcher).build();
        x.image().bind(img, netUrl, options);
    }
}
