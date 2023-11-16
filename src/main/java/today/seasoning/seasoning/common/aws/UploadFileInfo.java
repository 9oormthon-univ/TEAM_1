package today.seasoning.seasoning.common.aws;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadFileInfo {

	private final String filename;
	private final String url;
}
