package sdiag.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
//import java.util.HashMap;



import sdiag.com.service.FileVO;
import sdiag.com.service.SdiagProperties;

/**
 * @Class Name  : EgovFileMngUtil.java
 * @Description : 메시지 처리 관련 유틸리티
 * @Modification Information
 *
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.02.13       이삼섭                  최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 02. 13
 * @version 1.0
 * @see
 *
 */
@Component("FileManager")
public class FileManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileManager.class);
	
	/**
	 * 파일 실제 저장
	 * @param file
	 * @param fileNameAndPath
	 * @throws Exception
	 */
	public void writeFile(MultipartFile file, String fileNameAndPath) throws Exception{
		
		// 저장할 파일명과 패스 정보가 없으면 에러 처리
		if ("".equals(fileNameAndPath) || fileNameAndPath == null) {
			throw new Exception();
		}
		file.transferTo(new File(fileNameAndPath));
	}
	
	/**
	 * 파일 업로드 프로세스
	 * @param files		file객체
	 * @param KeyStr	업무 구분자 = ATT_ : 소명파일
	 * @return
	 * @throws Exception
	 */
	
	public List<FileVO> fileUploadProcess(Map<String, MultipartFile> files, String KeyStr, String attachPath, List<FileVO> vo) throws Exception{
		
		if (vo == null ){
			return null;
		}
		
		String orginFileName = "";
		String fileNewName = "";
		String fileExt = "";
		String fileSize = "";
		String fileName = "";
	
		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, MultipartFile> entry = itr.next();
			MultipartFile file = entry.getValue();
			orginFileName = file.getOriginalFilename();
			if ("".equals(orginFileName)) {
		    	continue;
		    }
			//System.out.println(SdiagProperties.getProperty("Globals.fileStorePath") + "][" + orginFileName);
			int index = orginFileName.lastIndexOf(".");
			//String randomString = StringUtil.getRandomStr('a', 'g') + StringUtil.getRandomStr('a', 'g') + StringUtil.getRandomStr('a', 'g');
			fileExt = orginFileName.substring(index + 1);							// 파일 확장자
			
			
			String regEx = "(jpg|jpeg|png|gif|bmp|JPG|JPEG|PNG|GIF|BPM)";
			
			
			fileNewName = KeyStr + CommonUtil.CreateUUID();		// 파일 새로운 이름
			fileSize = String.valueOf(file.getSize());									// 파일 크기
			//filePath = 	propertiesService.getString("fileStorePath");		// 실제 파일 경로
			fileName = fileNewName + "." + fileExt.toLowerCase();

			FileVO fvo = new FileVO();
			fvo.setEvid_file_name(orginFileName);
			fvo.setEvid_file_loc(fileName);
			fvo.setEvid_file_size(Integer.parseInt(fileSize));
			vo.add(fvo);
			//SdiagProperties.getProperty("Globals.fileStorePath")
			
			if(fileExt.matches(regEx)){
			this.writeFile(file, attachPath + "/" + fileName);
			}
		}
		
		return vo;
		
	}
	
	
	/**
	 * 파일 다운로드
	 * @param response
	 * @param downFileName	물리적인 경로와 파일명
	 * @throws Exception
	 */
	
    public static void fileDownloadProcess(HttpServletRequest request,
    										HttpServletResponse response, 
    										FileVO vo,
    										String filePath) throws Exception {  
    	String filePathName = filePath + "/" + vo.getEvid_file_loc();
    	System.out.println(filePathName + "][" + vo.getEvid_file_name());
    	File file = new File(filePathName);
    	//long filespace = file.getTotalSpace();
    	//vo.setEvid_file_size(595284);
		if (!file.exists()) {
		    throw new FileNotFoundException(vo.getEvid_file_loc());
		}
		
		if (!file.isFile()) {
		    throw new FileNotFoundException(vo.getEvid_file_loc());
		}
		
		byte[] b = new byte[Integer.parseInt(SdiagProperties.getProperty("Globals.buffSize"))]; //buffer size
				
		String header = getBrowser(request);
		
		if (header.contains("MSIE")) {
	       String docName = URLEncoder.encode(vo.getEvid_file_name(),"UTF-8").replaceAll("\\+", "%20");
	       response.setHeader("Content-Disposition", "attachment;filename=" + docName + ";");
		} else if (header.contains("Firefox")) {
	       String docName = new String(vo.getEvid_file_name().getBytes("UTF-8"), "ISO-8859-1");
	       response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		} else if (header.contains("Opera")) {
	       String docName = new String(vo.getEvid_file_name().getBytes("UTF-8"), "ISO-8859-1");
	       response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		} else if (header.contains("Chrome")) {
	       String docName = new String(vo.getEvid_file_name().getBytes("UTF-8"), "ISO-8859-1");
	       response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		}
		
		response.setHeader("Content-Type", "application/octet-stream");
		//response.setContentLength(vo.getEvid_file_size());
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
	
		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(new FileInputStream(file));
		    outs = new BufferedOutputStream(response.getOutputStream());
		    int read = 0;
	
			while ((read = fin.read(b)) != -1) {
			    outs.write(b, 0, read);
			}
			
		} finally {
		    if (outs != null) {
				try {
					outs.flush();
				    outs.close();
				} catch (Exception ignore) {
					LOGGER.debug("IGNORED: {}", ignore.getMessage());
				}
			}
		    if (fin != null) {
				try {
				    fin.close();
				} catch (Exception ignore) {
					LOGGER.debug("IGNORED: {}", ignore.getMessage());
				}
			}
		}
    }
    
    public static boolean fileDelete(FileVO vo, String filePath) throws Exception{
    	String filePathName = filePath + "/" + vo.getEvid_file_loc();
    	File file = new File(filePathName);
    	return file.delete();
    }
    
    /**
     * 브라우저 알아내기
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request) {
        String header =request.getHeader("User-Agent");
                
        if (header.contains("Firefox")) {
               return "Firefox";
        } else if(header.contains("Chrome")) {
               return "Chrome";
        } else if(header.contains("Opera")) {
               return "Opera";
        } else if(header.contains("Safari")) {
            return "Chrome";
        }
        return "MSIE";
    }
}
