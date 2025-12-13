package com.kabm.util;

import java.util.HashMap;
import java.util.Map;



public class KoreanSyllablesSeperator
{
	private static final char SYLLABLES_BEGIN = 0xAC00;
	private static final char SYLLABLES_END = 0xD7A3;
	
	private static final char COMPATIBILITY_JAMO_BEGIN = 0x3130;
	private static final char COMPATIBILITY_JAMO_END = 0x318E;

	/**
	 * <p>입력한 문자가 한글인지 판단한다.</p>
	 * <p>(코드값이 한글 영역(0xAC00-0xD7A3)인지 판단)</p>
	 * 
	 * @param c
	 * @return 한글이면 <code>true</code>, 아니면 <code>false</code>
	 */
	public static boolean isKoreanSyllables(char ch) {
		return ch >= SYLLABLES_BEGIN && ch <= SYLLABLES_END;
	}
	
	/**
	 * <p>입력한 문자가 한글 호환 자모인지 판단한다.</p>
	 * <p>(코드값이 한글 호환 자모 영역(0x1100-0x318E)인지 판단)</p>
	 * 
	 * @param ch
	 * @return 한글 호환 자모이면 <code>true</code>, 아니면 <code>false</code>
	 */
	public static boolean isKoreanCompatibilityJamo(char ch) {
		return ch >= COMPATIBILITY_JAMO_BEGIN && ch <= COMPATIBILITY_JAMO_END;
	}
	
	/**
	 * 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
	 */
	private static final char[] CHOSEONG = {0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};
	/**
	 * 'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
	 */
	private static final char[] JUNGSEONG = {0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163};
	/**
	 * ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
	 */
	private static final char[] JONGSEONG = {0x0000, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};

	public static final int CHOSEONG_SIZE = CHOSEONG.length;	// 19
	public static final int JUNGSEONG_SIZE = JUNGSEONG.length;	// 21
	public static final int JONGSEONG_SIZE = JONGSEONG.length;	// 28

	private static final Map<Character, Integer> CHOSEONG_CODE_TABLE = new HashMap<Character, Integer>();
	private static final Map<Character, Integer> JUNGSEONG_CODE_TABLE = new HashMap<Character, Integer>();
	private static final Map<Character, Integer> JONGSEONG_CODE_TABLE = new HashMap<Character, Integer>();

	static {
		for (int i = 0; i < CHOSEONG.length; i++) {
			CHOSEONG_CODE_TABLE.put(CHOSEONG[i], i);
		}
		for (int i = 0; i < JUNGSEONG.length; i++) {
			JUNGSEONG_CODE_TABLE.put(JUNGSEONG[i], i);
		}
		for (int i = 0; i < JONGSEONG.length; i++) {
			JONGSEONG_CODE_TABLE.put(JONGSEONG[i], i);
		}
	}

	/**
	 * <p>초성을 추출한다.</p>
	 * 
	 * @param ch
	 * @return
	 */
	public static char getChoseong(char ch) {
		if (isKoreanSyllables(ch) == false) {
			throw new IllegalArgumentException("입력값이 잘못되었습니다. (" + ch + ")");
		}
		int hCode = (ch - SYLLABLES_BEGIN) / (JUNGSEONG_SIZE * JONGSEONG_SIZE);
		return CHOSEONG[hCode];
	}

	/**
	 * <p>중성을 추출한다.</p>
	 * 
	 * @param ch
	 * @return
	 */
	public static char getJungseong(char ch) {
		if (isKoreanSyllables(ch) == false) {
			throw new IllegalArgumentException("입력값이 잘못되었습니다. (" + ch + ")");
		}
		int hCode = ((ch - SYLLABLES_BEGIN) % (JUNGSEONG_SIZE * JONGSEONG_SIZE)) / JONGSEONG_SIZE;
		return JUNGSEONG[hCode];
	}

	/**
	 * <p>종성을 추출한다.</p>
	 * 
	 * @param ch
	 * @return
	 */
	public static char getJongseong(char ch) {
		if (isKoreanSyllables(ch) == false) {
			throw new IllegalArgumentException("입력값이 잘못되었습니다. (" + ch + ")");
		}
		int hCode = ((ch - SYLLABLES_BEGIN) % (JUNGSEONG_SIZE * JONGSEONG_SIZE)) % JONGSEONG_SIZE;
		return JONGSEONG[hCode];
	}

	/**
	 * <p>초/중/종성을 추출한다.
	 * </p>
	 * @param ch
	 * @return [초성, 중성, 종성] or [초성, 중성]
	 */
	public static char[] getJamo(char ch) {
		char[] jamo = new char[3];
		jamo[0] = getChoseong(ch);
		jamo[1] = getJungseong(ch);
		jamo[2] = getJongseong(ch);
		if (jamo[2] == JONGSEONG[0]) {
			char[] temp = new char[2];
			temp[0] = jamo[0];
			temp[1] = jamo[1];
			jamo = temp;
		}
		return jamo;
	}

	/**
	 * <p>종성이 존재하는지 여부를 판단한다.</p>
	 * 
	 * @param ch
	 * @return 종성이 존재하면 <code>true</code>, 아니면 <code>false</code>
	 */
	public static boolean hasJongseong(char ch) {
		return getJongseong(ch) != JONGSEONG[0];
	}
	
	
	public static void main(String[] args)
	{
		/*String str = "안녕하a세a요";
		StringBuffer sb = new StringBuffer();
		char[] r;
		for(int idx=0; idx < str.length();idx++)
		{
			char c = str.charAt(idx);
			if(Hangul.isHangulSyllables(c))
			{
				sb.append(getJamo(c));
				r = getJamo(c);
			} else
			{
				sb.append(c);				
			}
		}
		
		
		
		System.out.println(sb.toString().indexOf("ㅇㅏㄴㄴㅕㅇㅎㅏ"));*/
		
		
		
		//System.out.println(Hangul.isHangulSyllables('ㄱ')); false
		//System.out.println(Hangul.isHangulCompatibilityJamo('ㄱ')); true
		//System.out.println(getJamo('한'));
		//System.out.println(new String(getJamo('한')).indexOf("ㅎㅏㄴ"));

	}
}