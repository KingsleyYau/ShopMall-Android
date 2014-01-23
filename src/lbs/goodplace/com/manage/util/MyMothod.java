package lbs.goodplace.com.manage.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * 图片处理方法
 * @author zhaojunjie
 *
 */
public class MyMothod {
	/**
	 * dp转px
	 * @param context
	 * @param dpvalue
	 * @return
	 */
	public static int Dp2Px(Context context , int dpvalue){
		int px =(int)( dpvalue * (double)context.getResources().getDisplayMetrics().densityDpi/160);
		return px;
	}
	
	/**
	 * px转dp
	 * @param context
	 * @param pxvalue
	 * @return
	 */
	public static int Px2Dp(Context context , int pxvalue){
		int dp =(int)( pxvalue / ((double)context.getResources().getDisplayMetrics().densityDpi/160));
		return dp;
	}
	
	/**
	 * get the pivot point(中心点)
	 * @param left
	 * @param right
	 * @param width
	 * @param height
	 * @return
	 */
	public static Point getPivot(int left, int top, int width, int height){
		Point point = new Point();
		point.x = left + width/2;
		point.y = top + height/3;
		return point;
	}
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap Byte2Bitmap(byte[] b){
		try{
			if(b.length != 0){
				//如果我们把它设为true，
				//那么BitmapFactory.decodeFile(String path, Options opt)并不会真的返回一个Bitmap给你，
				//它仅仅会把它的宽，高取回来给你，这样就不会占用太多的内存，也就不会那么频繁的发生OOM了
				BitmapFactory.Options options = new BitmapFactory.Options(); 
				options.inJustDecodeBounds = true;
				Bitmap bitmap=BitmapFactory.decodeByteArray(b, 0, b.length, options);
				options.inJustDecodeBounds = false;
				//ARGB_8888 图片更平滑
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				/* 下面两个字段需要组合使用 */
				options.inPurgeable = true;
				options.inInputShareable = true;
				//把图片缩小一半
//				int be = (int)(options.outHeight / (float)200); 
//				if (be <= 0)   
//					be = 1; 
//		         options.inSampleSize = be;
				Bitmap backb = BitmapFactory.decodeByteArray(b, 0, b.length,options);
				bitmap = null;
				b = null;
				return backb; 
			}else{
				b = null;
				return Bitmap.createBitmap(1, 1, Config.ARGB_8888);
			}
		}catch (Exception e) {
			b = null;
			return Bitmap.createBitmap(1, 1, Config.ARGB_8888);
		}
		
	}
	
//	/**
//	 * get the X scale（X轴缩放比例）
//	 * @param width
//	 * @return
//	 */
//	public static float getXScale(int width){
//		float x = Gallery1Activity.mWidth/((float)width);
//		System.out.println("xxxxxxxxxxxxxx" + x);
//		return x;
//	}
//	
//	/**
//	 * get the Y scale (Y轴缩放比例)
//	 * @param height
//	 * @return
//	 */
//	public static float getYScale(int height){
//		float y = (Gallery1Activity.mWidth*3)/((float)height*2);
//		System.out.println("yyyyyyyyyyyyyyyyy" + y);
//		return y;
//	}
	
	 /**
     * 合成图片
     * create the bitmap from a byte array
     *
     * @param src the bitmap object you want proecss
     * @param watermark the water mark above the src
     * @param x 坐标X
     * @param y 坐标Y
     * @return return a bitmap object ,if paramter's length is 0,return null
     */
    public static Bitmap mixBitmap( Bitmap src, Bitmap mark , int x, int y)
    {
        if( src == null )
        {
            return null;
        }
 
        int w = 1;
        int h = 1;
        //create the new blank bitmap
        Bitmap newb ;
        try{
        	w = src.getWidth();
            h = src.getHeight();
        	newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );//创建一个新的和SRC长度宽度一样的位图
        }catch (OutOfMemoryError e) {
            w = w * 2/3;
            h = h * 2/3;
            src = zoomBitmap(src,w,h);
        	newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );//内存不足，则创建一个新的和SRC长度宽度 2/3 的位图
        	
        	x = x * 2/3;	//标记的坐标也变为原来 2/3
        	y = y * 2/3;
		}
        
        Canvas cv = new Canvas( newb );
        //draw src into
        cv.drawBitmap( src, 0, 0, null );//在 0，0坐标开始画入src
        //draw watermark into
        cv.drawBitmap( mark, x, y, null );//在src的右下角画入水印
        //save all clip
        cv.save( Canvas.ALL_SAVE_FLAG );//保存
        //store
        cv.restore();//存储
        
        src = null;
        mark = null;
        
        return newb;
    }
    
	 /**
     * 合成图片(图片上标文字)
     * create the bitmap from a byte array
     *
     * @param src the bitmap object you want proecss
     * @param watermark the water mark above the src
     * @param x 坐标X
     * @param y 坐标Y
     * @return return a bitmap object ,if paramter's length is 0,return null
     */
    public static Bitmap mixBitmap( Bitmap src, String text,int textsize)
    {
        if( src == null )
        {
            return null;
        }
 
        int w = 1;
        int h = 1;
        
    	int x = (src.getWidth() / 2 - (textsize*text.length())/2)<1? 2:(src.getWidth() / 2 - (textsize*text.length())/2);
    	int y = src.getHeight() / 2;
    	
        //create the new blank bitmap
        Bitmap newb ;
        try{
        	w = src.getWidth();
            h = src.getHeight();
        	newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );//创建一个新的和SRC长度宽度一样的位图
        }catch (OutOfMemoryError e) {
            w = w * 2/3;
            h = h * 2/3;
            src = zoomBitmap(src,w,h);
        	newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );//内存不足，则创建一个新的和SRC长度宽度 2/3 的位图
        	
        	x = x * 2/3;	//标记的坐标也变为原来 2/3
        	y = y * 2/3;
		}
        
        final int color = 0xff424242;   
        final Paint paint = new Paint();   
        paint.setColor(color);
        paint.setTextSize(textsize);
        
        
        
        Canvas cv = new Canvas( newb );
        //draw src into
        cv.drawBitmap( src, 0, 0, null );//在 0，0坐标开始画入src
        //draw watermark into
        cv.drawText(text, x, y, paint);	//在src的右下角画入水印
        //save all clip
        cv.save( Canvas.ALL_SAVE_FLAG );//保存
        //store
        cv.restore();//存储
        
        src = null;
        
        return newb;
    }

    /**
     * Bitmap2Bytes
     * @param bm
     * @return
     */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		bm = null;
		return baos.toByteArray();
	}
	public static byte[] Bitmap2Bytes_PNG(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		bm = null;
		return baos.toByteArray();
	}
	public static Drawable Bitmap2Drawable(Bitmap bm) {
		Drawable drawable =new BitmapDrawable(bm);
		return drawable;
	}
	public static Bitmap Drawable2Bitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
	}
	
	/**
	 * 以短边为准将获取图片转换为目标图片
	 * @param des_width   目标图片宽度
	 * @param des_height  目标图片高度
	 * @param source	  源bitmap byte数组
	 * @return
	 */
	public static byte[] zoomSourceByte(int des_width, int des_height, byte[] source){
    	Bitmap oldBitmp = MyMothod.Byte2Bitmap(source);
    	int width = oldBitmp.getWidth();
    	int height = oldBitmp.getHeight();
    	float scaleWidth = ((float)des_width / width);  
        float scaleHeight = ((float)des_height/ height);
        float scaleValue = Math.max(scaleWidth, scaleHeight);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleValue, scaleValue);
        Bitmap newBitmap = Bitmap.createBitmap(oldBitmp, 0, 0, width, height, matrix, true);
//        if(!oldBitmp.isRecycled()){
//        	oldBitmp.recycle();
        oldBitmp = null;
//        }
        return getBitmapByte(Bitmap.createBitmap(newBitmap, 0, 0, des_width, des_height));
    }
	
    /**
     * 放大缩小图片   
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap,int w,int h){   
        int width = bitmap.getWidth();   
        int height = bitmap.getHeight();   
        Matrix matrix = new Matrix();   
        float scaleWidht = ((float)w / width);   
        float scaleHeight = ((float)h / height);   
        matrix.postScale(scaleWidht, scaleHeight);   
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);   
        bitmap = null;
        return newbmp;   
    } 
	
	/**
	 * 获取指定bitmap的byte数组
	 * @param bitmap
	 * @return
	 */
	private static byte[] getBitmapByte(Bitmap bitmap){

    	ByteArrayOutputStream out = new ByteArrayOutputStream();

    	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

    	try {

    	out.flush();

    	out.close();

    	} catch (IOException e) {

    	e.printStackTrace();

    	}
    	return out.toByteArray();
    }
	
    /**
     * 获得圆角图片的方法   
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){   
           
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap   
                .getHeight(), Config.ARGB_8888);   
        Canvas canvas = new Canvas(output);   
    
        final int color = 0xff424242;   
        final Paint paint = new Paint();   
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());   
        final RectF rectF = new RectF(rect);   
    
        paint.setAntiAlias(true);   
        canvas.drawARGB(0, 0, 0, 0);   
        paint.setColor(color);   
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);   
    
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));   
        canvas.drawBitmap(bitmap, rect, rect, paint);   
    
        return output;   
    }
}