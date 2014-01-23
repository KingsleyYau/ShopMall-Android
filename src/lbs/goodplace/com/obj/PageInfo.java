package lbs.goodplace.com.obj;

/**
 * 分页对象
 * @author shazhuzhu
 *
 */
public class PageInfo {
	public int mCurpag;//当次查询返回列表结果的第几页（整形）
	public int mPagecount; //符合条件记录总页数（整形）
	public int mPagemaxrow; //每页包含最大记录数（整形）
	public int mTotalrecordcount; //符合条件的总记录数（整形）

}
