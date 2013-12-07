package com.xynxs.main.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class StringUtil {
	/**
	 * 格式化long型的日期，将其转换成字符串，（例：今天11：23， 2013年1月3日 22：30等， 今天，昨天，前天，今年，去年）
	 */
	public static String getDateStr(long targetTime) {
		String dateStr = "";

		SimpleDateFormat timeDf = new SimpleDateFormat("HH:mm");
		SimpleDateFormat dayDf = new SimpleDateFormat("yyyy年MM月dd日");

		Date targetDate = new Date(targetTime);

		String targetDayStr = dayDf.format(targetDate);

		// 今天
		Date today = new Date(System.currentTimeMillis());
		// 判断目标日期是否是今天
		if (dayDf.format(today).equals(targetDayStr)) {
			dateStr = "今天" + timeDf.format(targetDate);
		} else {
			// 昨天
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.DATE, -1);
			Date yesterday = ca.getTime();

			// 判断目标日期是否是昨天
			if (dayDf.format(yesterday).equals(targetDayStr)) {
				dateStr = "昨天" + timeDf.format(targetDate);
			} else {
				// 前天
				ca.add(Calendar.DATE, -1);
				Date beforeYesDay = ca.getTime();
				// 判断目标日期是否是前天
				if (dayDf.format(beforeYesDay).equals(targetDayStr)) {
					dateStr = "前天" + timeDf.format(targetDate);
				} else {

					SimpleDateFormat yearDf = new SimpleDateFormat("yyyy年");

					String currentYearStr = yearDf.format(today);
					String targetYearStr = yearDf.format(targetDate);

					// 判断目标日期是否是今年
					if (currentYearStr.equals(targetYearStr)) {
						dateStr = targetDayStr.replace(currentYearStr, "") + " " + timeDf.format(targetDate);
					} else {
						dateStr = targetDayStr + " " + timeDf.format(targetDate);
					}

				}
			}
		}

		return dateStr;
	}

	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}
	
	
	public static int getCurrentYear(){
		int year = 2013;
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		String yearStr = df.format(new Date(System.currentTimeMillis()));
		try{
			year = Integer.parseInt(yearStr);
		}catch(Exception e){
			
		}
		return year;
	}
}
