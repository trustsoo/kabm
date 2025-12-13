/**
 * @license Copyright (c) 2003-2014, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */
CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	config.language = "ko";
    config.font_defaultLabel    = '맑은 고딕';
    config.font_names           = '맑은 고딕;';
	//config.font_names           = '맑은 고딕;나눔 고딕;굴림/Gulim;돋움/Dotum;바탕/Batang;궁서/Gungsuh;Arial/Arial;Comic Sans MS/Comic Sans MS;Courier New/Courier New;Georgia/Georgia;Lucida Sans Unicode/Lucida Sans Unicode;Tahoma/Tahoma;Times New Roman/Times New Roman;Trebuchet MS/Trebuchet MS;Verdana/Verdana';
    config.uiColor = '#eaebe7';								// ui 색깔지정
   
    /*ckeditor4 기능(쓸 일 있을때 주석풀어서 쓰면됨)  */
   
    config.enterMode = CKEDITOR.ENTER_BR;					// Enter Key 입력시 <br>
    config.shiftEnterMode = CKEDITOR.ENTER_P;				// Enter Key 입력시 <p>
    config.startupFocus = false;								// 시작 포커스 지정
    config.fontSize_defaultLabel = '16px'; 
    config.fontSize_sizes    = '출처 13px/13px;기본 16px/16px;타이틀 18px/18px;';	//폰트 사이즈 지정
    config.resize_enabled = false;							// 에디터 리사이즈 여부
    
    
    // config.plugins='dialogui,dialog,fakeobjects,lineheight,iframe';
    //config.removePlugins = 'elementspath';					// 에디터 하단 상태바(사용안할시)
    config.extraPlugins = 'lineheight,iframe,youtube';
    config.line_height="출처 1em/1em; 기본 1.9em/1.9em" ;
    //You Tube 동영상 
    config.youtube_responsive = true;

   
    //툴바 모양 지정.
    config.toolbar =[
       ['Source','Font','FontSize','lineheight','Undo','Redo'],
       ['Bold','Italic','Underline','Strike'],
       '/',
       ['TextColor','BGColor'],
       ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock' ],
       ['NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv'],
       ['Table','Link','Unlink','HorizontalRule','SpecialChar'],
       ['Image','Iframe','Youtube']
    ];
    //config.toolbar =[{name : 'insert', items : ['Image', 'Youtube']}];
};

CKEDITOR.on( 'dialogDefinition', function( ev ){
    var dialogName = ev.data.name;
    var dialogDefinition = ev.data.definition;

    if ( dialogName == 'image' ){
        dialogDefinition.removeContents( 'advanced' );  		//상세정보 탭 제거

        var infoTab = dialogDefinition.getContents( 'info' );  	
        
        //image info 탭 내에 불필요한 엘레멘트들 제거
        //infoTab.remove( 'txtHSpace'); 						//수평여백
        //infoTab.remove( 'txtVSpace');							//수직여백
        //infoTab.remove( 'txtBorder');							//테두리
        //infoTab.remove( 'txtWidth');							//너비
        //infoTab.remove( 'txtHeight');							//높이
        //infoTab.remove( 'ratioLock');							//정렬
    }
    
    if( dialogName == 'table' ){
    	var tableInfoTab = dialogDefinition.getContents( 'info' );
    	
    	//table info 탭 내에 불필요한 엘레멘트들 제거
    	tableInfoTab.remove( 'selHeaders' );					//Headers
    }
});