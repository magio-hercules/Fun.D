//package group.study.playsample;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.annotation.NonNull;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.GlideBuilder;
//import com.bumptech.glide.Registry;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.load.engine.cache.LruResourceCache;
//import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
//import com.bumptech.glide.load.model.GlideUrl;
//import com.bumptech.glide.module.AppGlideModule;
//import com.bumptech.glide.request.RequestOptions;
//import com.bumptech.glide.signature.ObjectKey;
//
//import java.io.InputStream;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.OkHttpClient;
//
//import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;
//
//@GlideModule
//public class MyGlideModule extends AppGlideModule {
//    @Override
//    public void applyOptions(Context context, GlideBuilder builder) {
//        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
//                .setMemoryCacheScreens(2)
//                .build();
//        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
//    }
//
//    @Override
//    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
//        OkHttpClient client = new OkHttpClient.Builder()
//                .readTimeout(20, TimeUnit.SECONDS)
//                .connectTimeout(20, TimeUnit.SECONDS)
//                .build();
//
//        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);
//
//        glide.getRegistry().replace(GlideUrl.class, InputStream.class, factory);
//    }
//
//    private static RequestOptions requestOptions(Context context){
//        return new RequestOptions()
//                .signature(new ObjectKey(
//                        System.currentTimeMillis() / (24 * 60 * 60 * 1000)))
//                .override(200, 200)
//                .centerCrop()
//                .encodeFormat(Bitmap.CompressFormat.PNG)
//                .encodeQuality(100)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .format(PREFER_ARGB_8888)
//                .skipMemoryCache(false);
//    }
//}
