package lbs.goodplace.com.manage.cache;

/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-10-12]
 */
public class MD5 {
	static final int S11 = 7;

	static final int S12 = 12;

	static final int S13 = 17;

	static final int S14 = 22;

	static final int S21 = 5;

	static final int S22 = 9;

	static final int S23 = 14;

	static final int S24 = 20;

	static final int S31 = 4;

	static final int S32 = 11;

	static final int S33 = 16;

	static final int S34 = 23;

	static final int S41 = 6;

	static final int S42 = 10;

	static final int S43 = 15;

	static final int S44 = 21;

	static final byte[] PADDING = { -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	/*
	 * 下面的三个成员是MD5计算过程中用到的3个核心数据，在原始的C实现中
	 * 
	 * 被定义到MD5_CTX结构中
	 */
	private long[] mState = new long[4]; // state (ABCD)

	private long[] mCount = new long[2]; // number of bits, modulo 2^64 (lsb
											// first)

	private byte[] mBuffer = new byte[64]; // input buffer

	/*
	 * digestHexStr是MD5的唯一一个公共成员，是最新一次计算结果的 16进制ASCII表示.
	 */
	public String digestHexStr;

	/*
	 * digest,是最新一次计算结果的2进制内部表示，表示128bit的MD5值.
	 */
	private byte[] mDigest = new byte[16];

	/*
	 * getMD5ofStr是类MD5最主要的公共方法，入口参数是你想要进行MD5变换的字符串
	 * 返回的是变换完的结果，这个结果是从公共成员digestHexStr取得的．
	 */
	public String getMD5ofStr(String inbuf) {
		md5Init();
		md5Update(inbuf.getBytes(), inbuf.length());
		md5Final();
		digestHexStr = "";
		for (int i = 0; i < 16; i++) {
			digestHexStr += byteHEX(mDigest[i]);
		}
		return digestHexStr;

	}

	// 这是MD5这个类的标准构造函数，JavaBean要求有一个public的并且没有参数的构造函数

	public MD5() {
		md5Init();

		return;
	}

	/* md5Init是一个初始化函数，初始化核心变量，装入标准的幻数 */
	private void md5Init() {
		mCount[0] = 0L;
		mCount[1] = 0L;
		// /* Load magic initialization constants.

		mState[0] = 0x67452301L;
		mState[1] = 0xefcdab89L;
		mState[2] = 0x98badcfeL;
		mState[3] = 0x10325476L;

		return;
	}

	/*
	 * F, G, H ,I 是4个基本的MD5函数，在原始的MD5的C实现中，由于它们是
	 * 
	 * 简单的位运算，可能出于效率的考虑把它们实现成了宏，在java中，我们把它们
	 * 
	 * 实现成了private方法，名字保持了原来C中的。
	 * TODO:LIGUOLIANG 由于checkStyle的关系,将F,G,H,I改成小写
	 */

	private long f(long x, long y, long z) {
		return (x & y) | ((~x) & z);

	}

	private long g(long x, long y, long z) {
		return (x & z) | (y & (~z));

	}

	private long h(long x, long y, long z) {
		return x ^ y ^ z;
	}

	private long i(long x, long y, long z) {
		return y ^ (x | (~z));
	}

	/*
	 * FF,GG,HH和II将调用F,G,H,I进行近一步变换
	 * TODO:LIGUOLIANG 由于checkStyle的关系,将FF,GG,HH改成小写
	 * 
	 * FF, GG, HH, and II transformations for rounds 1, 2, 3, and 4. Rotation is
	 * separate from addition to prevent recomputation.
	 */

	private long ff(long a, long b, long c, long d, long x, long s, long ac) {
		a += f(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}

	private long gg(long a, long b, long c, long d, long x, long s, long ac) {
		a += g(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}

	private long hh(long a, long b, long c, long d, long x, long s, long ac) {
		a += h(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}

	private long ii(long a, long b, long c, long d, long x, long s, long ac) {
		a += i(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}

	/*
	 * md5Update是MD5的主计算过程，inbuf是要变换的字节串，inputlen是长度，这个
	 * 函数由getMD5ofStr调用，调用之前需要调用md5init，因此把它设计成private的
	 */
	private void md5Update(byte[] inbuf, int inputLen) {

		int i, index, partLen;
		byte[] block = new byte[64];
		index = (int) (mCount[0] >>> 3) & 0x3F;
		// /* Update number of bits */
		if ((mCount[0] += inputLen << 3) < inputLen << 3) {
			mCount[1]++;
		}
		mCount[1] += inputLen >>> 29;

		partLen = 64 - index;

		// Transform as many times as possible.
		if (inputLen >= partLen) {
			md5Memcpy(mBuffer, inbuf, index, 0, partLen);
			md5Transform(mBuffer);

			for (i = partLen; i + 63 < inputLen; i += 64) {

				md5Memcpy(block, inbuf, 0, i, 64);
				md5Transform(block);
			}
			index = 0;

		} else {
			i = 0;
		}

		// /* Buffer remaining input */
		md5Memcpy(mBuffer, inbuf, index, i, inputLen - i);

	}

	/*
	 * md5Final整理和填写输出结果
	 */
	private void md5Final() {
		byte[] bits = new byte[8];
		int index, padLen;

		// /* Save number of bits */
		encode(bits, mCount, 8);

		// /* Pad out to 56 mod 64.
		index = (int) (mCount[0] >>> 3) & 0x3f;
		padLen = (index < 56) ? (56 - index) : (120 - index);
		md5Update(PADDING, padLen);

		// /* Append length (before padding) */
		md5Update(bits, 8);

		// /* Store state in digest */
		encode(mDigest, mState, 16);

	}

	/*
	 * md5Memcpy是一个内部使用的byte数组的块拷贝函数，从input的inpos开始把len长度的
	 * 
	 * 字节拷贝到output的outpos位置开始
	 */

	private void md5Memcpy(byte[] output, byte[] input, int outpos, int inpos, int len) {
		int i;

		for (i = 0; i < len; i++) {
			output[outpos + i] = input[inpos + i];
		}
	}

	/*
	 * md5Transform是MD5核心变换程序，有md5Update调用，block是分块的原始字节
	 */
	private void md5Transform(byte block[]) {
		long a = mState[0], b = mState[1], c = mState[2], d = mState[3];
		long[] x = new long[16];

		decode(x, block, 64);

		/* Round 1 */
		a = ff(a, b, c, d, x[0], S11, 0xd76aa478L); /* 1 */
		d = ff(d, a, b, c, x[1], S12, 0xe8c7b756L); /* 2 */
		c = ff(c, d, a, b, x[2], S13, 0x242070dbL); /* 3 */
		b = ff(b, c, d, a, x[3], S14, 0xc1bdceeeL); /* 4 */
		a = ff(a, b, c, d, x[4], S11, 0xf57c0fafL); /* 5 */
		d = ff(d, a, b, c, x[5], S12, 0x4787c62aL); /* 6 */
		c = ff(c, d, a, b, x[6], S13, 0xa8304613L); /* 7 */
		b = ff(b, c, d, a, x[7], S14, 0xfd469501L); /* 8 */
		a = ff(a, b, c, d, x[8], S11, 0x698098d8L); /* 9 */
		d = ff(d, a, b, c, x[9], S12, 0x8b44f7afL); /* 10 */
		c = ff(c, d, a, b, x[10], S13, 0xffff5bb1L); /* 11 */
		b = ff(b, c, d, a, x[11], S14, 0x895cd7beL); /* 12 */
		a = ff(a, b, c, d, x[12], S11, 0x6b901122L); /* 13 */
		d = ff(d, a, b, c, x[13], S12, 0xfd987193L); /* 14 */
		c = ff(c, d, a, b, x[14], S13, 0xa679438eL); /* 15 */
		b = ff(b, c, d, a, x[15], S14, 0x49b40821L); /* 16 */

		/* Round 2 */
		a = gg(a, b, c, d, x[1], S21, 0xf61e2562L); /* 17 */
		d = gg(d, a, b, c, x[6], S22, 0xc040b340L); /* 18 */
		c = gg(c, d, a, b, x[11], S23, 0x265e5a51L); /* 19 */
		b = gg(b, c, d, a, x[0], S24, 0xe9b6c7aaL); /* 20 */
		a = gg(a, b, c, d, x[5], S21, 0xd62f105dL); /* 21 */
		d = gg(d, a, b, c, x[10], S22, 0x2441453L); /* 22 */
		c = gg(c, d, a, b, x[15], S23, 0xd8a1e681L); /* 23 */
		b = gg(b, c, d, a, x[4], S24, 0xe7d3fbc8L); /* 24 */
		a = gg(a, b, c, d, x[9], S21, 0x21e1cde6L); /* 25 */
		d = gg(d, a, b, c, x[14], S22, 0xc33707d6L); /* 26 */
		c = gg(c, d, a, b, x[3], S23, 0xf4d50d87L); /* 27 */
		b = gg(b, c, d, a, x[8], S24, 0x455a14edL); /* 28 */
		a = gg(a, b, c, d, x[13], S21, 0xa9e3e905L); /* 29 */
		d = gg(d, a, b, c, x[2], S22, 0xfcefa3f8L); /* 30 */
		c = gg(c, d, a, b, x[7], S23, 0x676f02d9L); /* 31 */
		b = gg(b, c, d, a, x[12], S24, 0x8d2a4c8aL); /* 32 */

		/* Round 3 */
		a = hh(a, b, c, d, x[5], S31, 0xfffa3942L); /* 33 */
		d = hh(d, a, b, c, x[8], S32, 0x8771f681L); /* 34 */
		c = hh(c, d, a, b, x[11], S33, 0x6d9d6122L); /* 35 */
		b = hh(b, c, d, a, x[14], S34, 0xfde5380cL); /* 36 */
		a = hh(a, b, c, d, x[1], S31, 0xa4beea44L); /* 37 */
		d = hh(d, a, b, c, x[4], S32, 0x4bdecfa9L); /* 38 */
		c = hh(c, d, a, b, x[7], S33, 0xf6bb4b60L); /* 39 */
		b = hh(b, c, d, a, x[10], S34, 0xbebfbc70L); /* 40 */
		a = hh(a, b, c, d, x[13], S31, 0x289b7ec6L); /* 41 */
		d = hh(d, a, b, c, x[0], S32, 0xeaa127faL); /* 42 */
		c = hh(c, d, a, b, x[3], S33, 0xd4ef3085L); /* 43 */
		b = hh(b, c, d, a, x[6], S34, 0x4881d05L); /* 44 */
		a = hh(a, b, c, d, x[9], S31, 0xd9d4d039L); /* 45 */
		d = hh(d, a, b, c, x[12], S32, 0xe6db99e5L); /* 46 */
		c = hh(c, d, a, b, x[15], S33, 0x1fa27cf8L); /* 47 */
		b = hh(b, c, d, a, x[2], S34, 0xc4ac5665L); /* 48 */

		/* Round 4 */
		a = ii(a, b, c, d, x[0], S41, 0xf4292244L); /* 49 */
		d = ii(d, a, b, c, x[7], S42, 0x432aff97L); /* 50 */
		c = ii(c, d, a, b, x[14], S43, 0xab9423a7L); /* 51 */
		b = ii(b, c, d, a, x[5], S44, 0xfc93a039L); /* 52 */
		a = ii(a, b, c, d, x[12], S41, 0x655b59c3L); /* 53 */
		d = ii(d, a, b, c, x[3], S42, 0x8f0ccc92L); /* 54 */
		c = ii(c, d, a, b, x[10], S43, 0xffeff47dL); /* 55 */
		b = ii(b, c, d, a, x[1], S44, 0x85845dd1L); /* 56 */
		a = ii(a, b, c, d, x[8], S41, 0x6fa87e4fL); /* 57 */
		d = ii(d, a, b, c, x[15], S42, 0xfe2ce6e0L); /* 58 */
		c = ii(c, d, a, b, x[6], S43, 0xa3014314L); /* 59 */
		b = ii(b, c, d, a, x[13], S44, 0x4e0811a1L); /* 60 */
		a = ii(a, b, c, d, x[4], S41, 0xf7537e82L); /* 61 */
		d = ii(d, a, b, c, x[11], S42, 0xbd3af235L); /* 62 */
		c = ii(c, d, a, b, x[2], S43, 0x2ad7d2bbL); /* 63 */
		b = ii(b, c, d, a, x[9], S44, 0xeb86d391L); /* 64 */

		mState[0] += a;
		mState[1] += b;
		mState[2] += c;
		mState[3] += d;

	}

	/*
	 * Encode把long数组按顺序拆成byte数组，因为java的long类型是64bit的， 只拆低32bit，以适应原始C实现的用途
	 */
	private void encode(byte[] output, long[] input, int len) {
		int i, j;

		for (i = 0, j = 0; j < len; i++, j += 4) {
			output[j] = (byte) (input[i] & 0xffL);
			output[j + 1] = (byte) ((input[i] >>> 8) & 0xffL);
			output[j + 2] = (byte) ((input[i] >>> 16) & 0xffL);
			output[j + 3] = (byte) ((input[i] >>> 24) & 0xffL);
		}
	}

	/*
	 * Decode把byte数组按顺序合成成long数组，因为java的long类型是64bit的，
	 * 只合成低32bit，高32bit清零，以适应原始C实现的用途
	 */
	private void decode(long[] output, byte[] input, int len) {
		int i, j;

		for (i = 0, j = 0; j < len; i++, j += 4) {
			output[i] = b2iu(input[j]) | (b2iu(input[j + 1]) << 8) | (b2iu(input[j + 2]) << 16)
					| (b2iu(input[j + 3]) << 24);
		}

		return;
	}

	/*
	 * b2iu是我写的一个把byte按照不考虑正负号的原则的＂升位＂程序，因为java没有unsigned运算
	 */
	public static long b2iu(byte b) {
		return b < 0 ? b & 0x7F + 128 : b;
	}

	/*
	 * byteHEX()，用来把一个byte类型的数转换成十六进制的ASCII表示，
	 * 
	 * 因为java中的byte的toString无法实现这一点，我们又没有C语言中的 sprintf(outbuf,"%02X",ib)
	 */
	public static String byteHEX(byte ib) {
		char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
				'F' };
		char[] ob = new char[2];
		ob[0] = digit[(ib >>> 4) & 0X0F];
		ob[1] = digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}

	// 加密
	public static String encode(String source) {
		if (source == null) {
			return null;
		}
		MD5 md5 = new MD5();
		return md5.getMD5ofStr(source).toLowerCase();
	}
}
