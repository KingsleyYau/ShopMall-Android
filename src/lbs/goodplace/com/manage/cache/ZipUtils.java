package lbs.goodplace.com.manage.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-10-12]
 */
public class ZipUtils {

	public static byte[] gzip(byte[] bs) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream(1000);
		GZIPOutputStream gzout = null;
		try {
			gzout = new GZIPOutputStream(bout);
			gzout.write(bs);
			gzout.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (gzout != null) {
				try {
					gzout.close();
				} catch (Exception ex) {
				}
			}
		}
		return bout.toByteArray();

	}

	public static byte[] ungzip(byte[] bs) throws Exception {
		GZIPInputStream gzin = null;
		ByteArrayInputStream bin = null;
		try {
			bin = new ByteArrayInputStream(bs);
			gzin = new GZIPInputStream(bin);
			return toByteArray(gzin);
		} catch (Exception e) {
			throw e;
		} finally {
			if (bin != null) {
				bin.close();
			}
		}
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	public static int copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024 * 4];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static byte[] zip(byte[] bs) throws Exception {

		ByteArrayOutputStream o = null;
		try {
			o = new ByteArrayOutputStream();
			Deflater compresser = new Deflater();
			compresser.setInput(bs);
			compresser.finish();
			byte[] output = new byte[1024];
			while (!compresser.finished()) {
				int got = compresser.deflate(output);
				o.write(output, 0, got);
			}
			o.flush();
			return o.toByteArray();
		} catch (Exception ex) {
			throw ex;

		} finally {
			if (o != null) {
				try {
					o.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}

	}

	public static byte[] unzip(byte[] bs) throws Exception {
		ByteArrayOutputStream o = null;
		try {
			o = new ByteArrayOutputStream();
			Inflater decompresser = new Inflater();
			decompresser.setInput(bs);
			byte[] result = new byte[1024];
			while (!decompresser.finished()) {
				int resultLength = decompresser.inflate(result);
				o.write(result, 0, resultLength);
			}
			decompresser.end();
			o.flush();
			return o.toByteArray();
		} catch (Exception ex) {
			throw ex;

		} finally {
			if (o != null) {
				try {
					o.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
	}

}
