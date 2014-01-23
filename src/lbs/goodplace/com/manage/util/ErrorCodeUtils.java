package lbs.goodplace.com.manage.util;

import lbs.goodplace.com.R;
import android.content.Context;

/**
 * 错误代码对应类
 * @author Administrator
 *
 */
public class ErrorCodeUtils {
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static String getErrorStr(Context context,int errorcode){
		String errorstr = "";
		switch (errorcode) {
		case 199006:
			errorstr = context.getResources().getString(R.string.error199006);
			break;
			
		case 199021:
			errorstr = context.getResources().getString(R.string.error199021);
			break;
		case 199022:
			errorstr = context.getResources().getString(R.string.error199022);
			break;
		case 199023:
			errorstr = context.getResources().getString(R.string.error199023);
			break;
		case 199024:
			errorstr = context.getResources().getString(R.string.error199024);
			break;
		case 199025:
			errorstr = context.getResources().getString(R.string.error199025);
			break;
		case 199026:
			errorstr = context.getResources().getString(R.string.error199026);
			break;
		case 199027:
			errorstr = context.getResources().getString(R.string.error199027);
			break;
		case 199028:
			errorstr = context.getResources().getString(R.string.error199028);
			break;
		case 199029:
			errorstr = context.getResources().getString(R.string.error199029);
			break;
		case 199030:
			errorstr = context.getResources().getString(R.string.error199030);
			break;
		case 199031:
			errorstr = context.getResources().getString(R.string.error199031);
			break;
		case 199032:
			errorstr = context.getResources().getString(R.string.error199032);
			break;
		case 199033:
			errorstr = context.getResources().getString(R.string.error199033);
			break;
		case 199034:
			errorstr = context.getResources().getString(R.string.error199034);
			break;
		case 199035:
			errorstr = context.getResources().getString(R.string.error199035);
			break;
		case 199036:
			errorstr = context.getResources().getString(R.string.error199036);
			break;
		case 199037:
			errorstr = context.getResources().getString(R.string.error199037);
			break;
		case 199038:
			errorstr = context.getResources().getString(R.string.error199038);
			break;
			
		case 199042:
			errorstr = context.getResources().getString(R.string.error199042);
			break;
		case 199043:
			errorstr = context.getResources().getString(R.string.error199043);
			break;
		case 199044:
			errorstr = context.getResources().getString(R.string.error199044);
			break;
		case 199045:
			errorstr = context.getResources().getString(R.string.error199045);
			break;
		case 199046:
			errorstr = context.getResources().getString(R.string.error199046);
			break;
		case 199067:
			errorstr = context.getResources().getString(R.string.error199067);
			break;
			
		case 199081:
			errorstr = context.getResources().getString(R.string.error199081);
			break;
		case 199082:
			errorstr = context.getResources().getString(R.string.error199082);
			break;
		case 199083:
			errorstr = context.getResources().getString(R.string.error199083);
			break;
		case 199084:
			errorstr = context.getResources().getString(R.string.error199084);
			break;
		case 199085:
			errorstr = context.getResources().getString(R.string.error199085);
			break;
		case 199086:
			errorstr = context.getResources().getString(R.string.error199086);
			break;
		case 199087:
			errorstr = context.getResources().getString(R.string.error199087);
			break;
		case 199088:
			errorstr = context.getResources().getString(R.string.error199088);
			break;
		case 199089:
			errorstr = context.getResources().getString(R.string.error199089);
			break;
		case 199090:
			errorstr = context.getResources().getString(R.string.error199090);
			break;
		case 199091:
			errorstr = context.getResources().getString(R.string.error199091);
			break;
		case 199092:
			errorstr = context.getResources().getString(R.string.error199092);
			break;
		case 199093:
			errorstr = context.getResources().getString(R.string.error199093);
			break;
		case 199094:
			errorstr = context.getResources().getString(R.string.error199094);
			break;

		default:
			errorstr = context.getResources().getString(R.string.errorFail);
			break;
		}
		
		return errorstr;
	}
}
