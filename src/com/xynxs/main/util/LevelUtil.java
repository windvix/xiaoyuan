package com.xynxs.main.util;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class LevelUtil {

	public static int getLevel(long score){
		int level = 0;
		
		if(score<=1000){
			level = 1;
		}
		if(score>1000 && score<=2400){
			level = 2;
		}
		
		if(score>2400 && score<=4200){
			level = 3;
		}
		
		if(score>4200 && score<=6400){
			level = 4;
		}
		
		if(score>6400 && score<=9000){
			level = 5;
		}
		
		if(score>9000 && score<=12000){
			level = 6;
		}
		
		if(score>12000 && score<=15400){
			level = 7;
		}
		
		if(score>15400 && score<=19200){
			level = 8;
		}
		
		if(score>19200 && score<=23400){
			level = 9;
		}
		
		if(score>23400 && score<=28000){
			level = 10;
		}
		
		if(score>28000 && score<=33000){
			level = 11;
		}
		
		if(score>33000 && score<=38400){
			level = 12;
		}
		
		if(score>38400 && score<=44200){
			level = 13;
		}
		
		if(score>44200 && score<=50400){
			level = 14;
		}
		
		if(score>50400 && score<=57000){
			level = 15;
		}
		
		if(score>57000 && score<=64000){
			level = 16;
		}
		
		if(score>64000 && score<=71400){
			level = 17;
		}
		
		if(score>71400 && score<=79200){
			level = 18;
		}
		
		if(score>79200 && score<=87400){
			level = 19;
		}
		
		if(score>87400){
			level = 20;
		}
		
		return level;
	}
	
	public static String getLevelNameByLevel(int level){
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(0, "幼童");
		map.put(1, "书童");
		map.put(2, "书生");
		map.put(3, "秀才");
		map.put(4, "举人");
		map.put(5, "解元");
		map.put(6, "贡士");
		map.put(7, "会元");
		map.put(8, "进士");
		map.put(9, "探花");
		map.put(10, "榜眼");
		map.put(11, "状元");
		map.put(12, "编修");
		map.put(13, "府丞");
		map.put(14, "翰林学士");
		map.put(15, "御史中丞");
		map.put(16, "詹士");
		map.put(17, "侍郎");
		map.put(18, "大学士");
		map.put(19, "文曲星");
		map.put(20, "圣人");
		
		return map.get(level);
	}
	
	public static String getLevelNameByScore(long score){
		return getLevelNameByLevel(getLevel(score));
	}
}
