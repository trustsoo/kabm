/**
 * ubicus.common app 패키지
 * 
 * @author neoxeni neoxeni@ubicus.co.kr
 * @version 1.0
 */
(function(ubicus, $, window, document, undefined) {
    window.ubicus = ubicus;
    
    $(function() {  //document.ready
        ubicus.common.addResizeFunction(function(){
            //화면에 보이는 bootstrap 테이블의 column 사이즈를 재 정렬
            $('body').find('.dataTables_scrollBody .dataTable:visible').each(function(){
                var dataTableInstance = $(this).data('instance'); 
                dataTableInstance.columns.adjust();
            });
        });
    });
    
    var module = ubicus.app = {
        
        /**
         * datatables 의 기본 옵션 https://datatables.net/
         */
        TABLE_DEFAULT_OPTION : {
            destroy : true, // 기존 정보를 새로운 정보로 대체
            ordering : false, // 컬럼 정렬 사용 여부
            orderClasses : false, // 정렬중인 컬럼 하이라이트 여부(css)
            order : [], // 초기화시 기본 정렬 안먹도록
            searching : false, // 기본 검색 사용 여부
            info : false, // 페이징 정보 노출 여부
            processing : false, // 데이터 처리중 표시 여부
            serverSide : false, // 로컬 or 서버자료 조회
            scrollX : true, // 테이블 사이즈 오버시 가로 스크롤 허용(창 크기 조절시 버그)
            scrollCollapse : false,
            //scrollY:dt_height,              // 테이블 세로 사이즈
            
            dom : '<"dataTables_top"i>rt<"dataTables_bottom"lp>',// 포지셔닝 l:length, f,filter, i:info, p:pagination
            //dom:str_dom,                  // 컬럼 리사이즈(colResize) 기능 사용
            //data:json_data,               // JsonObject
            //columns:col_data,             // 컬럼 정보
            
            //paging:true,                    // scroller 기능 사용시 true 셋팅(1)
            //scroller:true,                  // scroller 기능 사용시 true 셋팅(2)
            //deferRender:true,               // scroller 기능 사용시 true 셋팅(3)
            
            autoWidth : false, // 자동 컬럼 크기 조절 여부
            colResize : {
                tableWidthFixed : false
            },
            language : {
                emptyTable : "조회된 자료가 없습니다.", // 조회된 자료가 없을 경우 메세지
                processing : "조회중", // 자료 조회중일 경우 메세지
                lengthMenu : "_MENU_ entries"//하단 pagingcount selectbox
            },
            lengthMenu:[10, 25, 50, 100]
        },
        
        /**
         * 사용자에게 보여지는 알림을 처리 한다. notify는 상태변화, 저장 상태등 자동으로 사라지는 내용을 표현. 내부에서
         * 사용한다. 모듈은 http://sciactive.com/pnotify/#demos-simple 를 참조
         * 
         * @param {String} type 메시지의 종류 [success|info|warning|error]
         * @param {String} text 사용자에 전달할 메시지
         * @param {Object} 확장을 위해 추가된 option으로 아직 상세 구현은 없음
         */
        notify : function(type, text, options) {
            if(!isNaN(type)){//번호라면
                if(type == '200'){
                    type = 'success';
                }else{
                    type = 'warning';
                }
            }
            
            var notifyOpt = {
                text: text,
                type: type||'',
                before_init: function(opts) {
                },
                after_init: function(notice) {
                },
                before_open: function(notice) {
                },
                after_open: function(notice) {
                },
                before_close: function(notice, timer_hide) {
                },
                after_close: function(notice, timer_hide) {
                }
            };
            
            $.extend(true,notifyOpt,options);
            
            if(notifyOpt.timestamp !== false){
                notifyOpt['text'] = '[' + ubicus.common.getCurrentTime(':') + '] ' + notifyOpt['text'];
            }
            
            return new PNotify(notifyOpt);
            //$.notify('[' + ubicus.common.getCurrentTime(':') + '] ' + msg, type);
        },
        
        /**
         * 사용자에게 보여지는 알림을 처리 한다. message는 alert, confirm과 같이 사용자의 확인을 받아야 하는
         * 메세지의 종류를 표현한다.
         * 
         * @param {String} type 메시지의 종류 [alert|confirm]
         * @param {String} msg 사용자에 전달할 메시지
         * @param {Object} 확장을 위해 추가된 option으로 아직 상세 구현은 없음
         */
        message : function(type, msg, options) {
            options = $.extend(true, {}, options);
            if (type == 'alert') {
                return eModal.alert(msg, options.title);
            } else if(type == 'confirm'){
                return eModal.confirm(msg, options.title);
            }
        },
        
        calendar : function(selector1, selector2) {
            var $start = $(selector1);
            
            if(selector2 === undefined){
                //single calendar
                $start.datetimepicker({
                    format : "L",
                    defaultDate : moment(),
                    focusOnShow : false,//for IE8 bug
                    showClose : true    //for IE8 bug
                });
                
                $start.on("dp.change", function(e) {
                    $start.datetimepicker('hide');
                });
            }else{
                //range calendar
                var $end = $(selector2);
                $start.datetimepicker({
                    format : "L",
                    defaultDate : moment().subtract(1, 'months'),
                    focusOnShow : false,//for IE8 bug
                    showClose : true    //for IE8 bug
                });
                
                $end.datetimepicker({
                    format : "L",
                    defaultDate : moment(),
                    focusOnShow : false,
                    showClose : true,
                    useCurrent : false   //Important! See issue #1075
                });
                
                $start.on("dp.change", function(e) {
                    $end.data("DateTimePicker").minDate(e.date);
                    $start.datetimepicker('hide');
                });
                
                $end.on("dp.change", function(e) {
                    $start.data("DateTimePicker").maxDate(e.date);
                    $end.datetimepicker('hide');
                });
            }
        },
        
        /**
         * bootstrap과의 충돌이 나는지 fileupload-progress, template-upload, template-download 등에
         * fade 클래스를 적용시 download callback이벤트가 제대로 동작하지 않음.. fade class제거
         * 
         * acceptFileTypes: /(\.|\/)(jpe?g|bmp|png|gif|doc|hwp|txt)$/i
         * maxNumberOfFiles: 4
         * */
        uploader : function uploader(selector,options){
            options = options || {};
            
            var preset = options['preset'];
            if(preset !== undefined){
                if(preset == 'default'){
                    options = $.extend(true,{
                        url: '/upload',
                        template: 'ul',
                        autoUpload:false,
                        uploaded:function(file,$row,i){
                            var inputs = '<input type="hidden" class="js-upload-file-field" name="file_nm" value="'+file['file_nm']+'"/>';
                            inputs    += '<input type="hidden" class="js-upload-file-field" name="file_path" value="'+file['file_path']+'"/>';
                            inputs    += '<input type="hidden" class="js-upload-file-field" name="file_size" value="'+file['size']+'"/>';
                            inputs    += '<input type="hidden" class="js-upload-file-field" name="user_file_nm" value="'+file['name']+'"/>';
                            inputs    += '<input type="hidden" class="js-upload-file-field" name="ext_nm" value="'+file['ext_nm']+'"/>';
                            $row.append(inputs);
                        }
                    },options);
                }
            }
            
            var preset = options['preset'];
            if(preset !== undefined){
                if(preset == 'default'){
                    options = $.extend(true,{
                        url: '/upload',
                        template: 'ul',
                        autoUpload:false,
                        uploaded:function(file,$row,i){
                            var inputs = '<input type="hidden" class="js-upload-file-field" name="file_nm" value="'+file['file_nm']+'"/>';
                            inputs    += '<input type="hidden" class="js-upload-file-field" name="file_path" value="'+file['file_path']+'"/>';
                            inputs    += '<input type="hidden" class="js-upload-file-field" name="file_size" value="'+file['size']+'"/>';
                            inputs    += '<input type="hidden" class="js-upload-file-field" name="user_file_nm" value="'+file['name']+'"/>';
                            inputs    += '<input type="hidden" class="js-upload-file-field" name="ext_nm" value="'+file['ext_nm']+'"/>';
                            $row.append(inputs);
                        }
                    },options);
                }
            }
            
            var getFileExt = function(name){
                var idx = name.lastIndexOf('.');
                if(idx > -1){
                    name = name.substring(idx+1);
                }
                return name.toUpperCase();
            }
            
            var WRAPPER_TEMPLATE = {
                "table" : {
                    html : function(){
                        var html = '<div class="fileupload-wrapper fileupload-type-table">';
                        
                        html += '<div class="fileupload-files-area">';
                        html +=     '<div class="fileupload-buttonbar">';
                        html +=         '<span class="btn btn-success fileinput-button"><i class="fa fa-folder-open icon-2x"></i><input type="file" name="files[]" multiple></span>';
                        html +=         '<button type="submit" class="btn btn-primary start" style="display:none;"><i class="fa fa-upload"></i> <span>Start upload</span></button>';
                        //html +=         ' <button type="reset" class="btn btn-warning cancel"><i class="fa fa-minus-circle"></i> <span>Cancel upload</span></button>';
                        //html +=         ' <button type="button" class="btn btn-danger delete"><i class="fa fa-trash"></i> <span>Delete</span></button>';
                        html +=         '<span class="fileupload-process"></span>';
                        html +=     '</div>';
                        html +=     '<div class="fileupload-progress">';
                        html +=         '<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">';
                        html +=             '<div class="progress-bar progress-bar-success" style="width:0%;"></div>';
                        html +=         '</div>';
                        html +=         '<div class="progress-extended">&nbsp;</div>';
                        html +=     '</div>';
                        html += '</div>';
                        
                        html += '<table role="presentation" class="table table-striped table-bordered">';
                        html += '<colgroup><col width="40px"/><col/><col  width="120px"/></colgroup>';
                        
                        html += '<thead><tr>';
                        html += '<th class="text-center"><input type="checkbox" class="toggle"></th>';
                        html += '<th>File Name</th>';
                        html += '<th>File Size</th>';
                        html += '</tr></thead>';
                        
                        html += '<tbody class="files"></tbody></table>';
                        
                        html += '</div>';
                        return html;
                    },
                    upload : function(o,i,file){
                        var row = '<tr class="template-upload">';
                        
                        row    += '<td class="text-center">';
                        row    +=     '<button class="btn btn-warning cancel"><i class="fa fa-minus-circle"></i></button>';
                        row    += '</td>';
                        
                        row    += '<td class="fileupload-name">';
                        row    +=     '<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>';
                        row    +=     '<div class="name">'+file.name;
                        row    +=         '<div class="label label-danger error" style="display:none"></div>';
                        row    +=     '</div>';
                        row    += '</td>';
                        
                        row    += '<td class="text-right"><span class="size">Processing...</span></td>';
                        
                        row    += '</tr>';
                        
                        return row;
                    },
                    download : function(o,i,file){
                        var row = '<tr class="template-download">';
                        
                        row    += '<td class="text-center">';
                        if (file.deleteUrl) {
                            row    += '<button class="btn btn-danger delete" data-type="'+file.deleteType+'" data-url="'+file.deleteUrl+'"';
                            if (file.deleteWithCredentials) { 
                                row    += ' data-xhr-fields="{\"withCredentials\":true}'; 
                            }
                            row    += ' ><i class="fa fa-trash"></i></button>';
                        } else {
                            row    += '<button class="btn btn-warning cancel"><i class="fa fa-minus-circle"></i></button>';
                        }
                        row    += '</td>';
                        
                        
                        row    += '<td class="fileupload-name">';
                        if(file.error){
                            row    += '<div class="progress progress-striped" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0;"></div></div>';
                        }else{
                            row    += '<div class="progress progress-striped" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:100%;"></div></div>';
                        }
                        
                        row    +=     '<div class="name">';
                        if (file.url) {
                            row        += '<a href="'+file.url+'" title="'+file.name+'" download="'+file.name+'" '+(file.thumbnailUrl?'data-gallery':'')+'>'+file.name+'</a>';
                        } else {
                            row        += file.name;
                        }
                        
                        if(file.error){
                            row        += '<div class="label label-danger error">'+file.error+'</div>';
                        }
                        row    +=     '</div>';
                        row    += '</td>';
                        
                        
                        row    += '<td class="text-right"><span class="size">'+o.formatFileSize(file.size)+'</span></td>';
                        row    += '</tr>'; 
                        
                        return row;
                    }
                },
                
                "ul" : {
                    html : function(){
                        var html = '<div class="fileupload-wrapper fileupload-type-ul">';
                        
                        html += '<div class="fileupload-progress">';
                        html +=     '<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">';
                        html +=         '<div class="progress-bar progress-bar-success" style="width:0%;"></div>';//end of fileupload-progress
                        html +=     '</div>';
                        html +=     '<div class="progress-extended" style="display: none;">&nbsp;</div>';
                        html +='</div>';//end of fileupload-progress
                        
                        
                        html += '<div class="fileupload-files-area">';
                        html +=     '<div class="fileupload-list-area"><div class="fileupload-list-scroller"><ul role="presentation" class="files"></ul></div></div>';
                        html +=     '<div class="fileupload-buttonbar">';
                        html +=         '<span class="btn btn-success fileinput-button"><i class="fa fa-folder-open icon-2x"></i><input type="file" name="files[]" multiple></span>';
                        html +=         '<button type="submit" class="btn btn-primary start" style="display:none;"><i class="fa fa-upload"></i><span>Start upload</span></button>';
                        //html +=         '<button type="reset" class="btn btn-warning cancel"><i class="fa fa-minus-circle"></i><span>Cancel upload</span></button>';
                        //html +=         '<button type="button" class="btn btn-danger delete"><i class="fa fa-trash"></i><span>Delete</span></button>';
                        html +=         '<span class="fileupload-process"></span>';
                        html +=     '</div>';
                        html += '</div>';//end of fileupload-files-area
                        
                        html +='</div>';//end of fileupload-wrapper
                        
                        return html;
                    },
                    upload : function(o,i,file){
                        var row = '<li class="template-upload">';
                        row    += '<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>';
                        row    += '<div class="fileupload-name">';
                        row    +=     '<img src="/static/com/img/fileImg/'+getFileExt(file.name)+'.png" onerror="this.src=\'/static/com/img/fileImg/Default.png\'" style="vertical-align: middle;">';
                        row    +=     '<div class="name">'+file.name+'</div>';
                        row    +=     '<div class="label label-danger error" style="display:none"></div>';
                        
                        if (!i && !o.options.autoUpload) {
                            row    += '<button class="btn btn-primary start" disabled style="display:none;"><i class="fa fa-upload"></i> <span>Start</span></button>';
                        }
                        
                        if (!i) {
                            row    += '<button class="btn btn-warning cancel"><i class="fa fa-minus-circle"></i></button>';
                        }
                        row    += '</div>';
                        
                        row    += '</li>';
                        return row;
                    },
                    download : function(o,i,file){
                        var row = '<li class="template-download">';
                        if(file.error){
                            row    += '<div class="progress progress-striped" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0;"></div></div>';
                        }else{
                            row    += '<div class="progress progress-striped" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:100%;"></div></div>';
                        }
                        
                        row    += '<div class="fileupload-name">';
                        row    +=     '<img src="/static/com/img/fileImg/'+getFileExt(file.name)+'.png" onerror="this.src=\'/static/com/img/fileImg/Default.png\'">';
                        
                        row    +=     '<div class="name">';
                        if (file.url) {
                            row    += '<a href="'+file.url+'" title="'+file.name+'" download="'+file.name+'" '+(file.thumbnailUrl?'data-gallery':'')+'>'+file.name+'</a>';
                        } else {
                            row    += file.name;
                        }
                        row    +=     '</div>';
                        
                        if(file.error){
                            row    +=     '<div class="label label-danger error">'+file.error+'</div>';
                        }
                        
                        if (file.deleteUrl) {
                            row    += '<button class="btn btn-danger delete" data-type="'+file.deleteType+'" data-url="'+file.deleteUrl+'"';
                            if (file.deleteWithCredentials) { 
                                row    += ' data-xhr-fields="{\"withCredentials\":true}'; 
                            }
                            row    += ' ><i class="fa fa-trash"></i></button>';
                        } else {
                            row    += '<button class="btn btn-warning cancel"><i class="fa fa-minus-circle"></i></button>';
                        }
                        row    += '</div>';
                        
                        
                        row    += '</li>';
                        return row;
                    }
                }
            }
            
            var templateType = options.template || 'ul';
            
            var $wrapper = $(selector); 
            
            var TEMPLATE = WRAPPER_TEMPLATE[templateType];
            
            $wrapper.append(TEMPLATE['html']());
            
            var defaultOptions = {
                dataType: 'json',
                autoUpload: false,
                hideProgress:false,
                singleFileUploads:true,
                filesContainer: $('.files',$wrapper),
                uploadTemplateId: null,
                downloadTemplateId: null,
                uploadTemplate: function (o) {
                    var rows = $();
                    $.each(o.files, function (i, file) {
                        rows = rows.add($(TEMPLATE['upload'](o,i,file)));
                    });
                    return rows;
                },
                downloadTemplate: function (o) {
                    var rows = $();
                    $.each(o.files, function (i, file) {
                        var $rowTem = $(TEMPLATE['download'](o,i,file));
                        rows = rows.add($rowTem);
                        if(typeof options['uploaded'] != 'undefined'){
                            options['uploaded'](file,$rowTem,i);
                        }
                    });
                    return rows;
                }
                //jquery.fileupload-process.js && jquery.fileupload-validate.js
                //acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
                //maxFileSize: 999000,
                //maxNumberOfFiles:10
            };
            
            options = $.extend(true, defaultOptions, options);
            
            var obj = $wrapper.fileupload(options);
            
            
            
            /*
            obj
            .on('fileuploadadd', function (e, data) { console.log('fileuploadadd') })
            .on('fileuploadsubmit', function (e, data) { console.log('fileuploadsubmit') })
            .on('fileuploadsend', function (e, data) { console.log('fileuploadsend') })
            .on('fileuploaddone', function (e, data) { console.log('fileuploaddone') })
            .on('fileuploadfail', function (e, data) { console.log('fileuploadfail') })
            .on('fileuploadalways', function (e, data) { console.log('fileuploadalways') })
            .on('fileuploadprogress', function (e, data) { console.log('fileuploadprogress') })
            .on('fileuploadprogressall', function (e, data) { console.log('fileuploadprogressall') })
            .on('fileuploadstart', function (e) { console.log('fileuploadstart') })
            .on('fileuploadstop', function (e) { console.log('fileuploadstop') })
            .on('fileuploadchange', function (e, data) { console.log('fileuploadchange') })
            .on('fileuploadpaste', function (e, data) { console.log('fileuploadpaste') })
            .on('fileuploaddrop', function (e, data) { console.log('fileuploaddrop') })
            .on('fileuploaddragover', function (e) { console.log('fileuploaddragover') })
            .on('fileuploadchunksend', function (e, data) { console.log('fileuploadchunksend') })
            .on('fileuploadchunkdone', function (e, data) { console.log('fileuploadchunkdone') })
            .on('fileuploadchunkfail', function (e, data) { console.log('fileuploadchunkfail') })
            .on('fileuploadchunkalways', function (e, data) { console.log('fileuploadchunkalways') });
            */
            
            obj.startUpload = function(callback){
                var errorCount = obj.getErrorCount();
                if(errorCount > 0){
                    var canStart = false;
                    $wrapper.find('.files .start').each(function(){
                        if(!$(this).prop('disabled')){
                            canStart = true;
                            return true;//break;
                        }
                    });
                    if(canStart){
                        $wrapper.find('.fileupload-buttonbar .start').click();
                        obj.off('fileuploadstop').on('fileuploadstop',function(e,data){
                            if(typeof callback == 'function'){                            	
                            	callback(obj.getErrorCount());
                            }
                        });
                    }else{
                    	callback(errorCount);
                    }
                }else{
                    if(typeof callback == 'function'){
                        callback(errorCount);
                    }
                }
            };
            
            
            obj.resetUpload = function(){
                options.filesContainer.empty();
            };
            
            obj.getErrorCount = function(){
                return obj.find('.files .fa-minus-circle').size();
            };
            
            return obj;
        },
        
        /**
         * f 객체로부터 uploader에 의해 추가된 file관련 input data만 string 으로 리턴한다.
         * */
        getUploadedData : function(f){
            var data = '';
            $(f).find('.js-upload-file-field').each(function(){
                data += '&'+this.name+'='+this.value;
            });
            
            if(data.length > 0){
                data = data.substring(1);//최초 &제거
            }
            
            return data;
        },
        
        datatable : function datatable(formId, tableId, options) {
            var dataTableObject = undefined;//아래에서 할당됨
            
            var $f = $(formId);
            formId = $f.attr('id')
            
            var $table = $(tableId);
            tableId = $table.attr('id');
            
            var f = $f[0];
            
            var defaultOptions = {
                currentPageName : 'cur_pg',     //데이터 테이블에서 페이지 값을 가지고 있는 필드명
                rowPerPageName : 'row_per_page',//데이터 테이블에서 페이지당 row의 갯수를 가지는 필드명
                onreload : function(data){      //ajax 데이터가 조회되고 난 이후에 data mapping 처리
                    return {//output, OutBlock1, tot_cnt는 변경 될수 있다. 
                        data : data['output']['OutBlock1'],
                        recordsTotal:data['output']['tot_cnt']
                    }
                },
                tableOptions : $.extend(true, {}, ubicus.app.TABLE_DEFAULT_OPTION)
            };
            
            if (typeof options['onclick'] == 'function') {
                $table.on('click', 'td', function(event) {
                    options['onclick'](this, dataTableObject.row(this.parentNode).data(), dataTableObject, event);
                });
                $table.find('tbody').css({
                    cursor : 'pointer'
                });
                
                //extend 전에 설정해야 사용자가 기본값을 덮을수 있다.
                defaultOptions.tableOptions['select'] = {
                    style : 'os'
                };
            }
            
            if (typeof options['dblclick'] == 'function') {
                $table.on('dblclick', 'td', function(event) {
                    options['dblclick'](this, dataTableObject.row(this.parentNode).data(), dataTableObject, event);
                });
                $table.find('tbody').css({
                    cursor : 'pointer'
                });
                
                //extend 전에 설정해야 사용자가 기본값을 덮을수 있다.
                defaultOptions.tableOptions['select'] = {
                    style : 'os'
                };
            }
            
            options = $.extend(true, defaultOptions, options);
            
            var currentPageName = options['currentPageName'];
            if (currentPageName !== undefined) {
                //current page field가 존재하지 않으면 form에 자동으로 추가
                if(typeof f[currentPageName] == "undefined"){
                    var x = document.createElement("INPUT");
                    x.setAttribute("type", "hidden");
                    x.setAttribute("name", currentPageName);
                    x.setAttribute("value", "1");
                    $f.prepend($(x));
                }
                
                
                f[currentPageName].value = "1";//초기화시 1로 세팅하여 첫 페이지
                $table.on('page.dt', function() {//페이징에 의한거면 info에서 추출
                    var info = $(this).DataTable().page.info();
                    f[currentPageName].value = info.page + 1;
                });
                
                $table.on('length.dt', function(e, settings, len) {
                    f[currentPageName].value = "1";//조회시 무조건 1로 세팅
                });
            }
            
            var tableOptions = options.tableOptions;
            
            //테이블에 선언된 options에서 tableOptions를 추출
            if ($table.attr('data-options') !== undefined) {
                $.parseJSON($table.attr('data-options'));
                tableOptions = $.extend(true, tableOptions, $.parseJSON($table.attr('data-options')));
            }
            
            if (tableOptions['columns'] === undefined) {
                // 선언된 th로부터 기본 column정보를 추출
                var columns = [];
                var hasCheckBox = false;
                $table.find('thead th').each(function(idx) {
                    var column = {
                        data : this.getAttribute('data-data'),
                        title : this.getAttribute('data-title') || this.innerHTML,
                        //width:this.getAttribute('data-width'),//colgroup을 사용
                        className : this.getAttribute('data-class')
                    };
                    
                    if (this.getAttribute('data-type') == 'checkbox') {
                        column['render'] = function(data, type, row) {
                            if (type === 'display') {
                                return '<input type="checkbox" name="radio_' + column['data'] + '" value="' + data + '">';
                            }
                            return data;
                        }
                        
                        var $checkBox = $(this).find('input[type="checkbox"]');
                        if($checkBox.size() == 1){
                            $checkBox.on('click',function(){
                                var isChecked = $(this).prop('checked');
                                $table.find('TBODY TR').each(function(){
                                    $(this).find('TD:eq('+idx+') input[type="checkbox"]').prop('checked',isChecked);
                                });
                            });
                        }
                    } else if (this.getAttribute('data-type') == 'radio') {
                        column['render'] = function(data, type, row) {
                            if (type === 'display') {
                                return '<input type="radio" name="radio_' + column['data'] + '" value="' + data + '">';
                            }
                            return data;
                        }
                    } else if (this.getAttribute('data-type') == 'a') {
                        column['render'] = function(data, type, row) {
                            if (type === 'display') {
                                return '<a href="#">' + data + '</a>';
                            }
                            return data;
                        }
                    } else if (this.getAttribute('data-render') !== null) {
                        var dataRender = this.getAttribute('data-render');
                        if(options['render'] && options['render'][dataRender]){
                            column['render'] = options['render'][dataRender];
                        }else{
                            column['render'] = eval(dataRender);
                        }
                    }
                    
                    if (column['className'] && column['className'].indexOf('select-checkbox') > -1) {
                        hasCheckBox = true;
                        column['data'] = null;//null로 변환해줘야 아래에서 defaultContent가 세팅됨
                    }
                    
                    if (column['data'] === null || column['data'] == "null") {
                        column['defaultContent'] = '';
                    }
                    
                    columns.push(column);
                    
                });
                tableOptions['columns'] = columns;
                
                //체크박스가 있다면 셀렉트 이벤트를 부여 해야 한다. 이미 설정되어 있다면 필요 없음..
                if (hasCheckBox && tableOptions['select'] === undefined) {
                    tableOptions['select'] = {
                        style : 'os'
                    };
                    $table.find('tbody').css({
                        cursor : 'pointer'
                    });
                }
            }
            
            tableOptions['serverSide'] = true; //serverSide가 true여야 ajax의 recoedsTotal이 적용됨
            tableOptions['deferLoading'] = 0; //초기화 되면서 조회되는것을 막음
            tableOptions['ajax'] = function(data, callback, settings) {
                xAjax({
                    form : f,
                    success : function(data, textStatus, jqXHR) {
                        var gridData = options['onreload'](data,textStatus, jqXHR);
                        if(gridData['recordsFiltered'] === undefined){
                            gridData['recordsFiltered'] = gridData['recordsTotal']
                        }
                        
                        callback(gridData);
                    }
                });
            };
            
            var dataTableObject = $table.DataTable(tableOptions);
            $table.data('instance',dataTableObject);
            
            //page length가 있으면 field 이름을 row_per_page로 변경(bld와 맞추려고..)
            if (typeof f[tableId + '_length'] != 'undefined') {
                f[tableId + '_length'].name = options['rowPerPageName'];
            }
            
            return dataTableObject;
        },
        
        getSelectedRowData : function(dataTable,elem){
            if(elem){
                var tr = $(elem).closest('TR')[0];
                if(tr !== undefined){
                    if(dataTable){
                        return dataTable.row(tr).data();
                    }else{
                        return $(tr).closest('TABLE').DataTable().row(tr).data();
                    }
                }
            }else{
                return dataTable.row({ selected: true }).data();
            }
        },
        
        getFileEntries : function getFileEntries (target_div, isEnableDelete, attach_div_cd, data_no) {
            $('#'+target_div).children().remove();
            $('#'+target_div).html('<span>첨부파일이 없습니다.</span>');
            
            xAjax({
                url: '/base/file.jspx?cmd=getFileEntries',
                data: 'attach_div_cd=' + attach_div_cd + '&data_no=' + data_no,
                type: 'POST',
                success: function(data){
                    if(data['rcode'] == '200'){
                        var listHtml = [];
                        var json = data['output']['OutBlock1'];
                        var jsonCnt = json.length;
                        if(jsonCnt > 0){
                            listHtml.push('<thead>');
                            listHtml.push('     <th style="text-align:center" class="filelist-header">파일명</th>');
                            listHtml.push('     <th style="width:17%; text-align:center" class="filelist-header">파일사이즈</th>');
                            listHtml.push('     <th style="width:23%; text-align:center" class="filelist-header">등록일시</th>');
                            listHtml.push('     <th style="display:none" class="filelist-header">파일번호</th>');
                            listHtml.push('     <th style="display:none" class="filelist-header">파일종류</th>');
                            listHtml.push('     <th style="display:none" class="filelist-header">데이터번호</th>');
                            listHtml.push('     <th style="display:none" class="filelist-header">분류코드</th>');
                            listHtml.push('     <th style="display:none" class="filelist-header">서버파일명</th>');
                            
                            if(isEnableDelete) {
                                listHtml.push(' <th style="width:13%; text-align:center" class="filelist-header">삭제</td>')
                            } else {
                                
                            }
                            
                            listHtml.push('</thead>');
                            listHtml.push('<tbody style="border-bottom:0px !important;">');
                            
                            for(var i=0; i<jsonCnt; i++) {
                                listHtml.push('     <tr id="trFileID_'+json[i].file_no+'" style="height:30px;">');
                                listHtml.push('         <td class="grid_link_download"><a href="/base/file.jspx?cmd=doDownload&file_no=' + json[i].file_no+'" target="blank">' + ubicus.app.getFileImg(json[i].ext_nm) + json[i].user_file_nm + '</a></td>');
                                listHtml.push('         <td style="text-align:center">' + ubicus.common.getFileSizeKB(json[i].file_size) + '</td>');
                                listHtml.push('         <td style="text-align:center">' + json[i].reg_dt + '</td>');
                                listHtml.push('         <td style="display:none">' + json[i].file_no + '</td>');
                                listHtml.push('         <td style="display:none">' + json[i].ext_nm + '</td>');
                                listHtml.push('         <td style="display:none">' + json[i].data_no + '</td>');
                                listHtml.push('         <td style="display:none">' + json[i].attach_div_cd + '</td>');
                                listHtml.push('         <td style="display:none">' + json[i].file_nm + '</td>');

                                if(isEnableDelete) {
                                    listHtml.push('     <td align="center"><a class="btn btn-danger" onclick="ubicus.app.delFileEntries(\'' + json[i].file_no + '\');"><i class="fas fa-times"></i></a></td>');
                                } else {
                                    
                                }
                                
                                listHtml.push('     </tr>');
                            }
                            listHtml.push('</tbody>');
                            
                            var listEntries ="fileEntriesDiv_"+target_div;
                            var pagerDiv = "pager_"+target_div;

                            $('#'+target_div).html("<table id=\""+listEntries+"\" class=\"table-bordered table-hover\" width=\"100%\"></table><div id=\"pagerDiv\"></div>");
                            $("#"+listEntries).html(listHtml.join(''));
                            
                            $("#"+listEntries).find('.grid_link_download a').on('click',function(e){
                                e.preventDefault();
                                $.fileDownload($(this).prop('href'), {
                                    failCallback:function(responseHtml, url, error){
                                        ubicus.app.notify('error','요청 파일을 찾을 수 없습니다.');
                                    }
                                });
                            });
                            
                            listHtml = [];
                        }
                    }
                }
            });
        },
        
        fileDownload : function(ele,event){
            if(event){
                event.preventDefault();
            }
            
            $.fileDownload($(ele).prop('href'), {
                failCallback:function(responseHtml, url, error){
                    ubicus.app.notify('error','요청 파일을 찾을 수 없습니다.');
                }
            });
        },
        
        delFileEntries : function js_delFileEntries(file_no , _delCallBackFn) {
            ubicus.app.message('confirm',msg_upload_code_11).then(function (/* DOM */) { 
                xAjax({
                    url: "/base/file.jspx?cmd=doDelete&file_no="+file_no,
                    success: function(data){
                        ubicus.app.notify(data['rcode'],data['rmsg']);
                        if(data['rcode'] == '200'){
                            var $table = $('#trFileID_'+file_no).closest('table');
                            $('#trFileID_'+file_no).remove();
                            if($table.find('tbody tr').size() == 0){
                                $('<span>첨부파일이 없습니다.</span>').insertBefore($table);
                                $table.remove();
                            }
                            
                            if(typeof _delCallBackFn == 'function'){
                                _delCallBackFn(file_no);
                            }
                        }
                    }
                });
            })
            .fail(function (/*null*/) {  
                
            });
        },
        
        // 첨부파일 아이콘
        getFileImg: function getFileImg(extFile) {
            var img = "";
            try{
                //extFile = extFile.substring(1);
                extFile = extFile.toUpperCase();
                img = '<img src="/static/com/img/fileImg/' + extFile + '.png" height="16" width="16" style="vertical-align:middle; float: left; margin: 2px 4px 0px 4px;" onerror="jQuery(this).attr(\'src\', \'/static/com/img/fileImg/Default.png\')" />';
            }
            catch(e){
                img = '<img src="/static/com/img/fileImg/Default.png" height="16" width="16" style="vertical-align:middle; float: left;" />';
            }
            
            return img;
        },
        
        modalEmpTree : function(options){
            var defaultOptions = {
                selected:function(data){} //선택되었을때 호출되는 이벤트
            };
            
            options = $.extend(true,defaultOptions,options);
            
            
            var message = '';
            message += '<form class="form-inline">';
            message += '    <input class="form-control" type="text" name="search_field" placeholder="사용자명 검색"/>';
            message += '    <input type="submit" class="btn btn-sm btn-primary" value="검색" />';
            message += '    <div id="modal-emp-tree-form-tree"></div>';
            message += '</form>';
            
            var modalOptions = {
                message: message,
                title:'상담원',
                buttons: [
                    {text: 'Close', style: 'default',   close: true, click: function(){} },
                    {text: 'Use', style: 'primary',   close: true, click: function(){
                        var $modal = this;
                        var targetTree      = $modal.find('#modal-emp-tree-form-tree').data('instance');
                        var selectedData    = targetTree.get_selected();
                        
                        if(selectedData !== undefined){
                            if(selectedData['type'] == 'human'){
                                selectedData['emp_no'] = selectedData['dept_no'];
                                selectedData['emp_nm'] = selectedData['dept_nm'];
                                
                                selectedData['dept_no'] = selectedData['p_dept_no'];
                                selectedData['dept_nm'] = targetTree.get_parent().text;//이게 필요
                            }
                            options.selected(selectedData);
                        }
                    }},
                ],
            };
            
            eModal.alert(modalOptions).then(function(){
                var $modal = $(this);
                var $f = $modal.find('form'), f = $f[0];
                
                var empTree = new JsTree({
                    elemId : "modal-emp-tree-form-tree",
                    url : "/base/common.jspx?cmd=listEmpTree"
                }).loadData().create();
                
                $f.on('submit',function(e){
                    empTree.search(this.search_field.value);
                    return false;//화면전환 방지
                });
            });
        },
        
        
        
        sample : function(anyParams) {
            
        }
    };
    
})(window.ubicus || {}, jQuery, this, this.document);
