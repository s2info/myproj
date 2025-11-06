package sdiag.exanal.util;

import java.io.InputStream;
import java.util.Hashtable;

public interface TestUploadDTO {
	
	void setRow(String i) throws Exception;
	
	int saveTestYn(Hashtable ht,TestUploadDTO dto) throws Exception;

}
