package jdf.framework.logic.spi;

public interface ProtocolParam
{

	/**
     * 아래 예와 같이 path의 이름을 정의한다. ex) /anylogic/read-file?path=
     * 
     */
	public final static String URL_PARAMETER_FILEPATH = "path";
	
	
	public final static String URL_PARAMETER_VERSION = "version";
	
	/**
	 * 파일명 변경시 변경후 paremter
	 */
	public final static String URL_PARAMETER_RENAME_TO = "to";
	
	
	/**
	 * 디렉토리 생성 parameter
	 */
	public final static String URL_PARAMETER_MKDIR = "mkdir";
	
	
	/**
	 * name filtering 하기위한 parameter
	 */
	public final static String URL_PARAMETER_NAME_FILTER = "name-filter";

	/**
     * 
     * 파일내용 본문
     * 
     */
	public final static String URL_PARAMETER_CONTENT = "descriptor";

	/**
     * 파일조회시 본문내용은 생략
     * 
     */
	public final static String URL_PARAMETER_CONTENT_SKIP = "content-skip";

	/**
     * HTTP Header 인식용 auth key
     * 
     */
	public final static String HTTP_HEADER_NAME_AUTH_KEY = "anyframe.auth.key";

	/**
     * HTTP Header 인식용 사용자 id 키
     * 
     */
	public final static String HTTP_HEADER_NAME_AUTH_ID = "anyframe.auth.id";

	/**
     * HTTP Header 인식용 사용자 권한 키
     * 
     */
	public final static String HTTP_HEADER_NAME_AUTH_ROLES = "anyframe.auth.roles";

	/**
     * HTTP Header 인식용, 파일 META 정보
     * 
     */
	public final static String HTTP_HEADER_FILE_INFO = "anyframe.file.info";
	
	
	/**
	 * 외부 anylogic 호출용 현재 실행 시각 정보
	 * 
	 */
	public final static String HTTP_HEADER_EXEC_TIME = "anyframe.execute.time";
	
	
	public final static String URL_PARAMETER_NAME_AUTH_KEY = "authTempKey";

	/**
     * 파일을 보는 명
     */
	public final static String COMMAND__READ_FILE = "file-read";

	/**
     * 파일목록 보는 명령
     */
	public final static String COMMAND__LIST_FILE = "file-list";

	/**
     * 파일을 삭제하는 명령
     */
	public final static String COMMAND__DELETE_FILE = "file-delete";

	/**
     * 파일 생성 또는 내용 기록
     */
	public final static String COMMAND__WRITE_FILE = "file-write";

	/**
     * 파일을 검색한다.
     * 
     */
	public final static String COMMAND__SEARCH_FILE = "file-search";
	
	
	/**
	 * 파일명 변경 이동
	 */
	public final static String COMMAND__RENAME_FILE = "file-rename";
	
	
	/**
	 * SQL Query 수행 명령
	 */
	public final static String COMMAND_QUERY_PROCESS = "qprocess";
	
	
	/**
	 * AJAX 및 Eiger를 지원하기 위한 script 출력 command
	 * 
	 */
	public final static String COMMAND_SCRIPT_SUPPORT = "script";

	/**
     * 외부에서 인식하는 BLD 가 위치하는 경로 ex) /anylogic/file-read?path=/anylogic/bld/test.xml
     */
	public final static String PATH_BLD_URL = "/anylogic/bld";

	/**
     * 외부에서 인식하는 DBMS 정보(table,column) 가 위치하는 경로 ex) /anylogic/file-list?path=/anylogic/dbms
     */
	public final static String PATH_DBMS_URL = "/anylogic/dbms";

	/**
     * 외부에서 인식하는 config 가 위치하는 경로 ex) /anylogic/file-read?path=/anylogic/bld/test.xml
     */
	public final static String PATH_CONFIG_URL = "/anylogic/config";
 
	public final static String TYPE_FS_FILE = "F";

	public final static String TYPE_FS_DIR = "D";
	
	// 검색에서 사용하는 디렉토리 정보
	public final static String TYPE_FS_DIR_S = "d";

	public final static String TYPE_DBMS = "B";

	public final static String TYPE_DBMS_CONNECTION = "P";

	
	public final static String TYPE_DBMS_TABLE_SCHEME = "S";
	
	
	public final static String TYPE_DBMS_TABLE = "T";

	public final static String TYPE_DBMS_COMLUMN = "C";
	
	/**
	 * http 통신중 http header에 들어가는 "User-Agent" 값을 결정
	 */
	public final static String HTTP_HEADER_USER_AGENT = "anyframe resource manager";
}