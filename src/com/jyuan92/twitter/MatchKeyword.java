package com.jyuan92.twitter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchKeyword {
	private static final String regex = "[mM][uU][sS][iI][cC]|[gG][aA][mM][eE]|[aA][mM][aA][zZ][oO][nN]|[mM][oO][vV][iI][eE]|"
			+ "[nN][eE][wW][sS]|[aA][nN][dD][rR][oO][iI][dD]|[jJ][oO][bB]|[hH][aA][lL][lL][oO][wW][eE][eE][nN]|";
	private static final Pattern p = Pattern.compile(regex);
	private static final Pattern music = Pattern.compile("[mM][uU][sS][iI][cC]");
	private static final Pattern game = Pattern.compile("[gG][aA][mM][eE]");
	private static final Pattern amazon = Pattern.compile("[aA][mM][aA][zZ][oO][nN]");
	private static final Pattern movie = Pattern.compile("[mM][oO][vV][iI][eE]");
	private static final Pattern news = Pattern.compile("[nN][eE][wW][sS]");
	private static final Pattern android = Pattern.compile("[aA][nN][dD][rR][oO][iI][dD]");
	private static final Pattern job = Pattern.compile("[jJ][oO][bB]");
	private static final Pattern halloween = Pattern.compile("[hH][aA][lL][lL][oO][wW][eE][eE][nN]");

	public String getkeyword(String str) {
		Matcher match = p.matcher(str);
		if (match.find()) {
			if (music.matcher(str).find()) {
				return "music";
			} else if (game.matcher(str).find()) {
				return "game";
			} else if (amazon.matcher(str).find()) {
				return "amazon";
			} else if (movie.matcher(str).find()) {
				return "movie";
			} else if (news.matcher(str).find()) {
				return "news";
			} else if (android.matcher(str).find()) {
				return "android";
			} else if (job.matcher(str).find()) {
				return "job";
			} else if (halloween.matcher(str).find()) {
				return "halloween";
			}
		}
		return null;
	}
}
