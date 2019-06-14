package com.hzecool.fdn.utils.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.hzecool.fdn.utils.StringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 中文相关工具类
 * @author Administrator
 *
 */
public class ChineseUtil {


	private static String[] name = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	private static String[] unit = { "", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾",
			"佰", "仟" };


	/**
	 * 将获取数字的中文大写  忽略小数
	 * @param value
	 * @return
	 */
	public static  String getChineseNum(double value)
	{
		String f="";
	   java.text.DecimalFormat   df=   new   java.text.DecimalFormat("####.##");  
	   String sname=df.format(value);
	   if(sname.startsWith("-")){
		   f+="负";
		   sname=sname.substring(1);
	   }
		String intpart="";
		String decpart="";
		if(sname.indexOf(".")>0){
			String[] part=sname.split("\\.");
			intpart=part[0];
			decpart=part[1];
		}else{
			intpart=sname;
		}
		int [] s=new int[intpart.length()];
		char []c=intpart.toCharArray();
		for(int i=c.length-1;i>=0;i--)
		{
			s[c.length-i-1]=Integer.parseInt(c[i]+"");
		}
		int lastNaNPos=-1;
		for(int j=s.length-1;j>=0;j--)
		{
			if(s[j]==0){
				if(j%4==0){
					if(lastNaNPos%4!=0)
					    f+=unit[j];
				}else{
					if(j-1>=0 && s[j-1]==0){
						continue;
					}else{
						f+="零";
					}
				}
			}else{
				f+=name[s[j]]+unit[j];
				lastNaNPos=j;
			}
		}
		if(decpart.length()>0){
			if(new Long(decpart).longValue()>0){
				f+="点";
				char []c1=decpart.toCharArray();
				for(int i=0;i<c1.length;i++){
					int s1=Integer.parseInt(c1[i]+"");
					f+=name[s1];
				}
			}
		}
		return f;
	}
	
	/**
	 * 将获取数字的中文大写  圆角分
	 * @param value
	 * @return
	 */
	public static  String getChineseNumYJF(double value)
	{
		String f="";
	   java.text.DecimalFormat   df=   new   java.text.DecimalFormat("####.##");  
	   String sname=df.format(value);
	   if(sname.startsWith("-")){
		   f+="负";
		   sname=sname.substring(1);
	   }
		String intpart="";
		String decpart="";
		if(sname.indexOf(".")>0){
			String[] part=sname.split("\\.");
			intpart=part[0];
			decpart=part[1];
		}else{
			intpart=sname;
		}
		int [] s=new int[intpart.length()];
		char []c=intpart.toCharArray();
		for(int i=c.length-1;i>=0;i--)
		{
			s[c.length-i-1]=Integer.parseInt(c[i]+"");
		}
		int lastNaNPos=-1;
		for(int j=s.length-1;j>=0;j--)
		{
			if(s[j]==0){
				if(j%4==0){
					if(lastNaNPos%4!=0)
					    f+=unit[j];
				}else{
					if(j-1>=0 && s[j-1]==0){
						continue;
					}else{
						f+="零";
					}
				}
			}else{
				f+=name[s[j]]+unit[j];
				lastNaNPos=j;
			}
		}
		if(decpart.length()>0){
			if(new Long(decpart).longValue()>0){
				f+="元";
				char []c1=decpart.toCharArray();
				for(int i=0;i<c1.length;i++){
					int s1=Integer.parseInt(c1[i]+"");
					f+=name[s1];
					if(i==0){
						f+="角";
					}
					if(i==1){
						f+="分";
					}
				}
			}
		}else {
			f += "元整";
		}
		
		return f;
	}
	
	// 完整的判断中文汉字和符号
	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i< ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取拼音数组
	 *  
	 * @author fengdg  
	 * @param str
	 * @param pyLen	拼音长度，-1表示全拼（所有长度）；1表示首字母（长度1）
	 * @param maxLen	
	 * @return
	 */
	private static String[] getPinyinFromChinese(String str, int pyLen, int maxLen) {
		List<String> list_py = new ArrayList<String>();
		List<String> list_temp = new ArrayList<String>();
		char[] chs = str.toCharArray();
		for (int i = 0; i < chs.length; i++) {
			list_temp = new ArrayList<String>();
			String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(chs[i]);
			if (pinyins != null) {
				Set<String> exists = new TreeSet<String>();
				for (int j = 0; j < pinyins.length; j++) {
					String pinyin = StringUtils.substring(pinyins[j], 0, pyLen); //全拼也要去掉最后一位声调
					if (exists.contains(pinyin))
						continue;
					int size = list_py.size();
					if (size > 0) {
						if (size > maxLen) {
							size = maxLen;
						}
						for (int k = 0; k < size; k++) {
							list_temp.add(list_py.get(k) + pinyin);
						}
					} else {
						list_temp.add(pinyin);
					}
					exists.add(pinyin);
				}

			} else {
				int size = list_py.size();
				if (size > 0) {
					for (int k = 0; k < size; k++) {
						list_temp.add(list_py.get(k) + chs[i]);
					}
				} else {
					list_temp.add(String.valueOf(chs[i]));
				}
			}
			list_py = list_temp;
		}
		String[] retval = new String[list_py.size()];
		list_py.toArray(retval);
		return retval;
	}

	/**
	 * 获取词组首字母组合，如果含有非汉字，保留字符：如“你真牛b”  转化后为nznb
	 * @param str
	 * @return
	 * @deprecated 多音字过多时，容易导致内存占用过大，如"啊靠哦喔喔喔哦喔喔哦喔啦啦啦啦啦可是可是没什么没什么时间就睡觉睡觉睡觉睡觉"
	 * @see #getPinyinFromChinese(String, int, int)
	 */
	public static String[] getFirstPinyinFromChinese(String str){
		return getPinyinFromChinese(str, 1, Integer.MAX_VALUE);
//		List<String> list_py=new ArrayList<String>();
//		List<String> list_temp=new ArrayList<String>();
//		char[] chs=str.toCharArray();
//		for(int i=0;i<chs.length;i++){
//			list_temp=new ArrayList<String>();
//			String[] pinyins=PinyinHelper.toHanyuPinyinStringArray(chs[i]);
//			if(pinyins!=null){
//				Set<String> exists=new TreeSet<String>();
//				for(int j=0;j<pinyins.length;j++){
//					String pinyin=pinyins[j].substring(0,1);
//					if(exists.contains(pinyin))continue;
//					int size=list_py.size();
//					if(size>0){
//						for(int k=0;k<size;k++){
//							list_temp.add(list_py.get(k)+pinyin);
//						}
//					}else{
//						list_temp.add(pinyin);
//					}
//					exists.add(pinyin);
//				}
//			
//			}else{
//				int size=list_py.size();
//				if(size>0){
//					for(int k=0;k<size;k++){
//						list_temp.add(list_py.get(k)+chs[i]);
//					}
//				}else{
//					list_temp.add(String.valueOf(chs[i]));
//				}
//			}
//			list_py=list_temp;
//		}
//		String[] retval=new String[list_py.size()];
//		list_py.toArray(retval);
//		return retval;
	}
	
	/**
	 * 
	 *  
	 * @author fengdg  
	 * @param str
	 * @param seperator
	 * @return
	 * @deprecated
	 * @see #getFirstPinyinFromChinese(String, char, int)
	 */
	public static String getFirstPinyinFromChinese(String str,char seperator){
		return getFirstPinyinFromChinese(str, seperator, Integer.MAX_VALUE);
//		String[] pinyins=getFirstPinyinFromChinese(str);
//		String retVal="";
//		for(int i=0;i<pinyins.length;i++){
//			if(retVal.length()>0)
//				retVal+=seperator+pinyins[i];
//			else
//				retVal=pinyins[i];
//		}
//		return retVal;
	}

	/**
	 * 返回拼音首字母，限定maxLen
	 *  
	 * @author fengdg  
	 * @param str
	 * @param seperator
	 * @param maxLen
	 * @return
	 */
	public static String getFirstPinyinFromChinese(String str, char seperator, int maxLen){
		String[] pinyins=getPinyinFromChinese(str, 1, maxLen);
		String retVal="";
		for(int i=0;i<pinyins.length;i++){
			if (retVal.length() >= maxLen) {
//				return retVal.substring(0, maxLen);
			} else if(retVal.length()>0) {
				retVal+=seperator+pinyins[i];
			} else {
				retVal=pinyins[i];
			}
		}
		if (retVal.length() >= maxLen) {
			retVal= retVal.substring(0, maxLen);
		}
		return retVal;
	}

	/**
	 * 获取词组首字母组合，如果含有非汉字，保留字符：如“你真牛b”  转化后为nizhenniub
	 * @param str
	 * @return
	 * @deprecated 多音字多时，容易导致内存占用过大，如"啊靠哦喔喔喔哦喔喔哦喔啦啦啦啦啦可是可是没什么没什么时间就睡觉睡觉睡觉睡觉"
	 * @see #getPinyinFromChinese(String, int, int)
	 */
	public static String[] getFullPinyinFromChinese(String str){
		return getPinyinFromChinese(str, -1, Integer.MAX_VALUE);
//		List<String> list_py=new ArrayList<String>();
//		List<String> list_temp=new ArrayList<String>();
//		char[] chs=str.toCharArray();
//		for(int i=0;i<chs.length;i++){
//			list_temp=new ArrayList<String>();
//			String[] pinyins=PinyinHelper.toHanyuPinyinStringArray(chs[i]);
//			if(pinyins!=null){
//				Set<String> exists=new TreeSet<String>();
//				for(int j=0;j<pinyins.length;j++){
//					String pinyin=pinyins[j].substring(0,pinyins[j].length()-1);
//					if(exists.contains(pinyin))continue;
//					int size=list_py.size();
//					if(size>0){
//						for(int k=0;k<size;k++){
//							list_temp.add(list_py.get(k)+pinyin);
//						}
//					}else{
//						list_temp.add(pinyin);
//					}
//					exists.add(pinyin);
//				}
//
//			}else{
//				int size=list_py.size();
//				if(size>0){
//					for(int k=0;k<size;k++){
//						list_temp.add(list_py.get(k)+chs[i]);
//					}
//				}else{
//					list_temp.add(String.valueOf(chs[i]));
//				}
//			}
//			list_py=list_temp;
//		}
//		String[] retval=new String[list_py.size()];
//		list_py.toArray(retval);
//		return retval;
	}
	
	public static String getFullPinyinFromChinese(String str, char seperator){
		return getFirstPinyinFromChinese(str, seperator, Integer.MAX_VALUE);
//		String[] pinyins=getFullPinyinFromChinese(str);
//		String retVal="";
//		for(int i=0;i<pinyins.length;i++){
//			if(retVal.length()>0)
//				retVal+=seperator+pinyins[i];
//			else
//				retVal=pinyins[i];
//		}
//		return retVal;
	}
	
	/**
	 * 获取全拼，限定maxLen
	 *  
	 * @author fengdg  
	 * @param str
	 * @param seperator
	 * @param maxLen
	 * @return
	 */
	public static String getFullPinyinFromChinese(String str, char seperator, int maxLen){
		String[] pinyins=getPinyinFromChinese(str, -1, maxLen);
		
		String retVal="";
		for(int i=0;i<pinyins.length;i++){
			if (retVal.length() >= maxLen) {
//				return retVal.substring(0, maxLen);
			} else if(retVal.length()>0) {
				retVal+=seperator+pinyins[i];
			} else {
				retVal=pinyins[i];
			}
		}
		if (retVal.length() >= maxLen) {
			retVal= retVal.substring(0, maxLen);
		}
		return retVal;
	}
	
	/**
	 * 返回拼音首字母和全拼的组合，包含所有多音字的组合，如果中间含非汉字原字符返回
	 * 例如：长城1 分割符为|  则返回 cc1|zc1|changcheng1|zhangcheng1
	 * @param str  汉字字符串
	 * @param seperator  分割符
	 * @return
	 * @deprecated 多音字多时，容易导致内存占用过大，如"啊靠哦喔喔喔哦喔喔哦喔啦啦啦啦啦可是可是没什么没什么时间就睡觉睡觉睡觉睡觉"
	 * @see #getFullSimplePinyinFromChinese(String, char, int)
	 */
	public static String getFullSimplePinyinFromChinese(String str,char seperator){
//		String first=getFirstPinyinFromChinese(str,seperator);
//		String full=getFullPinyinFromChinese(str,seperator);
//		return  first+seperator+full;
		return getFullSimplePinyinFromChinese(str, seperator, Integer.MAX_VALUE);
	}
	
	/**
	 * 返回拼音首字母和全拼的组合，包含所有多音字的组合，如果中间含非汉字原字符返回,只取前maxlen个字符内容
	 * @param str
	 * @param seperator
	 * @param maxLen
	 * @return
	 */
	public static String getFullSimplePinyinFromChinese(String str,char seperator,int maxLen){
		String first = getFirstPinyinFromChinese(str, seperator, maxLen);
		String full = getFullPinyinFromChinese(str, seperator, maxLen);
		str = first+seperator+full;
//	    str = getFullSimplePinyinFromChinese(str,seperator);
		if(str.length() > maxLen)
			str = str.substring(0, maxLen);
		return str;
	}

	public static void main(String[] args) {
//		ChineseUtil.getFirstPinyinFromChinese("啊靠哦喔喔喔哦喔喔哦喔啦啦啦啦啦可是可是没什么没什么时间就睡觉睡觉睡觉睡觉", '|', 100);
//		String str = ChineseUtil.getFirstPinyinFromChinese("哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈", '|', 100);
		String str = ChineseUtil.getFirstPinyinFromChinese("四季青市场", '|', 2);
		System.out.println(str);
	}
}

