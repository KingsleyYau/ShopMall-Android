package lbs.goodplace.com.obj;

import java.io.Serializable;

/**
 * 用户对商家的点评情况
 * @author Administrator
 *
 */
public class Commentsituation implements Serializable{
	public int total;
	public String curuser;
	public int star;
	public String curcomment;	//最后评论内容
	public long time;	//最新点评时间（自1970年1月1日起的秒数）
}