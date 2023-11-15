package today.seasoning.seasoning.common.util;

import com.github.f4b6a3.tsid.Tsid;
import com.github.f4b6a3.tsid.TsidCreator;

public class TsidUtil {

	public static Long createLong() {
		return TsidCreator.getTsid().toLong();
	}

	public static String toString(Long id) {
		return Tsid.from(id).encode(62);
	}

	public static Long toLong(String id) {
		return Tsid.decode(id, 62).toLong();
	}

}
