/* Demo Note:  This demo uses a FileProgress class that handles the UI for displaying the file name and percent complete.
The FileProgress class is not part of SWFUpload.
*/


/* **********************
   Event Handlers
   These are my custom event handlers to make my
   web application behave the way I went when SWFUpload
   completes different tasks.  These aren't part of the SWFUpload
   package.  They are part of my application.  Without these none
   of the actions SWFUpload makes will show up in my application.
   ********************** */
var __numFilesQueued = 0;
var isContainError = false;
function swfUploadPreLoad() {
	var self = this;
	var loading = function () {
		//document.getElementById("divSWFUploadUI").style.display = "none";
		document.getElementById("divLoadingContent").style.display = "";
		

		var longLoad = function () {
			document.getElementById("divLoadingContent").style.display = "none";
			document.getElementById("divLongLoading").style.display = "";
			//msgStart(msg_com_code_052, "danger");
		};
		this.customSettings.loadingTimeout = setTimeout(function () {
				longLoad.call(self)
			},
			15 * 1000
		);
	};
	
	this.customSettings.loadingTimeout = setTimeout(function () {
			loading.call(self);
		},
		1*1000
	);
}
function swfUploadLoaded() {
	var self = this;
	clearTimeout(this.customSettings.loadingTimeout);
	//document.getElementById("divSWFUploadUI").style.visibility = "visible";
	//document.getElementById("divSWFUploadUI").style.display = "block";
	document.getElementById("divLoadingContent").style.display = "none";
	document.getElementById("divLongLoading").style.display = "none";
	//document.getElementById("divAlternateContent").style.display = "none";
	
	//document.getElementById("btnBrowse").onclick = function () { self.selectFiles(); };
	try
	{
		document.getElementById(this.customSettings.cancelButtonId).onclick = function () { self.cancelQueue(); };
	} catch(e)
	{}
}
   
function swfUploadLoadFailed() {
	clearTimeout(this.customSettings.loadingTimeout);
	//document.getElementById("divSWFUploadUI").style.display = "none";
	document.getElementById("divLoadingContent").style.display = "none";
	document.getElementById("divLongLoading").style.display = "none";
	//document.getElementById("divAlternateContent").style.display = "";
	
	msgStart(msg_mng_code_051, 'danger');
	
}
    
function fileQueued(file) {
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setStatus("Pending..."+file.name);
		progress.toggleCancel(true, this);

	} catch (ex) {
		this.debug(ex);
	}

}

function fileQueueError(file, errorCode, message) {
	try {
		if (errorCode === SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
			alert("You have attempted to queue too many files.\n" + (message === 0 ? "You have reached the upload limit." : "You may select " + (message > 1 ? "up to " + message + " files." : "one file.")));
			return;
		}

		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setError();
		progress.toggleCancel(false);

		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			progress.setStatus("File is too big.");
			this.debug("Error Code: File too big, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			progress.setStatus("Cannot upload Zero Byte files.");
			this.debug("Error Code: Zero byte file, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			progress.setStatus("Invalid File Type.");
			this.debug("Error Code: Invalid File Type, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		default:
			if (file !== null) {
				progress.setStatus("Unhandled Error");
			}
			this.debug("Error Code: " + errorCode + ", File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		}
	} catch (ex) {
        this.debug(ex);
    }
}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
	try {
		if (numFilesSelected > 0) {
			document.getElementById(this.customSettings.progressTarget).style.display = '';
			document.getElementById(this.customSettings.cancelButtonId).disabled = false;
		} 
		//__numFilesQueued += numFilesQueued;
		setNumFilesQueued(numFilesQueued);
		/* I want auto start the upload and I can do that here */
		//this.startUpload();
	} catch (ex)  {
        this.debug(ex);
	}
}

function uploadStart(file) {
	try {
		/* I don't want to do any file validation or anything,  I'll just update the UI and
		return true to indicate that the upload should start.
		It's important to update the UI here because in Linux no uploadProgress events are called. The best
		we can do is say we are uploading.
		 */
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setStatus("Uploading...");
		progress.toggleCancel(true, this);
	}
	catch (ex) {}
	
	return true;
}

function uploadProgress(file, bytesLoaded, bytesTotal) {
	try {
		var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);

		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setProgress(percent);
		progress.setStatus("Uploading...");
	} catch (ex) {
		this.debug(ex);
	}
}
/*
 * modified by advan94
 * serverData :
 * 
		 <?xml version='1.0' encoding='utf-8'?>
		 <result>	
		 	<code>200</code>	
		 	<msg><![CDATA[success]]></msg>	
		 	<data>
			 	<file>
					<filename>
						1372830803529.jpg
					</filename>
					<path>
						E:\project\sample\web\WebContent\WEB-INF\config\data\201307
					</path>
					<extname>
						jpg
					</extname>
					<filesize>
						620888
					</filesize>
				</file>
			</data>
		</result>
		 
 * */
function uploadSuccess(file, serverData) 
{
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		var fileInfoHiddenFields = this.customSettings.fileInfoHiddenFields;
		
		serverData = serverData.replace(/\n/g,'');
		serverData = serverData.replace(/\r/g,'');
		
		var xmlDoc = createXMLFromString(serverData);
		
		var code = jQuery(xmlDoc).find('code').text();
		var msg = jQuery(xmlDoc).find('msg').text();
		var user_filename = file.name;
		if(code == '200')
		{
			var filename = jQuery(xmlDoc).find('filename').text();	
			var path = jQuery(xmlDoc).find('path').text();	
			var extname = jQuery(xmlDoc).find('extname').text();	
			var filesize = jQuery(xmlDoc).find('filesize').text();
			
			var tmps = filesize.replace(/[^0-9]/g, '');
            var tmps2 = tmps.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
			
			progress.setComplete();
			progress.setStatus("Complete. file name :"+user_filename+", File size:"+tmps2+" Bytes.");
			progress.toggleCancel(false);
			
			if(typeof(fileInfoHiddenFields) === "object" )
    		{
    			for (var name in fileInfoHiddenFields)
    			{
    				if (fileInfoHiddenFields.hasOwnProperty(name))
    				{
    					//console.log(name.toString()+"==>"+fileInfoHiddenFields[name].toString());
    					try
    					{
    						
	    					if(name.toString() == 'filename')
	    					{
	    						if(document.getElementById(fileInfoHiddenFields[name].toString()).value == '')
	    							document.getElementById(fileInfoHiddenFields[name].toString()).value = filename;
	    						else
	    							document.getElementById(fileInfoHiddenFields[name].toString()).value = document.getElementById(fileInfoHiddenFields[name].toString()).value+","+filename;
	    					} else if(name.toString() == 'path')
	    					{
	    						if(document.getElementById(fileInfoHiddenFields[name].toString()).value == '')
	    							document.getElementById(fileInfoHiddenFields[name].toString()).value = path;
	    						else
	    							document.getElementById(fileInfoHiddenFields[name].toString()).value = document.getElementById(fileInfoHiddenFields[name].toString()).value+","+path;	    						
	    					} else if(name.toString() == 'extname')
	    					{
	    						if(document.getElementById(fileInfoHiddenFields[name].toString()).value == '')
	    							document.getElementById(fileInfoHiddenFields[name].toString()).value = extname;
	    						else
	    							document.getElementById(fileInfoHiddenFields[name].toString()).value = document.getElementById(fileInfoHiddenFields[name].toString()).value+","+extname;
	    					} else if(name.toString() == 'filesize')
	    					{
	    						if(document.getElementById(fileInfoHiddenFields[name].toString()).value == '')
	    							document.getElementById(fileInfoHiddenFields[name].toString()).value = filesize;
	    						else
	    							document.getElementById(fileInfoHiddenFields[name].toString()).value = document.getElementById(fileInfoHiddenFields[name].toString()).value+","+filesize;	    						
	    					} else if(name.toString() == 'user_filename')
	    					{
	    						if(document.getElementById(fileInfoHiddenFields[name].toString()).value == '')
	    							document.getElementById(fileInfoHiddenFields[name].toString()).value = user_filename;
	    						else
	    							document.getElementById(fileInfoHiddenFields[name].toString()).value = document.getElementById(fileInfoHiddenFields[name].toString()).value+","+user_filename;	 
	    					}
    					} catch(e)
    					{
    						//%console.log(name.toString()+"==>"+fileInfoHiddenFields[name].toString()+" ==>"+e);
    						this.debug(e);
    					}
    					//fileInfoHiddenFieldsPairs.push();
    				}
    			}
    		}
			
		} else
		{
			//%console.log(code+':'+msg);
			progress.setError();
			progress.toggleCancel(false);
			setNumFilesQueued(-1);
			progress.setStatus("Upload Error("+code+"):"+msg);
			this.debug("Error Code: Custom Error, File name: " + file.name + ", File size: " + file.size + ", Message: " + msg);
			isContainError = true;
		}

	} catch (ex) {
		this.debug(ex);
	}
}


function uploadSuccessByjQueurySelector(file, serverData) 
{
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		var fileInfoHiddenFields = this.customSettings.fileInfoHiddenFields;
		
		serverData = serverData.replace(/\n/g,'');
		serverData = serverData.replace(/\r/g,'');
		
		var xmlDoc = createXMLFromString(serverData);
		
		var code = jQuery(xmlDoc).find('code').text();
		var msg = jQuery(xmlDoc).find('msg').text();
		var user_filename = file.name;
		if(code == '200')
		{
			var filename = jQuery(xmlDoc).find('filename').text();	
			var path = jQuery(xmlDoc).find('path').text();	
			var extname = jQuery(xmlDoc).find('extname').text();	
			var filesize = jQuery(xmlDoc).find('filesize').text();
			
			var tmps = filesize.replace(/[^0-9]/g, '');
            var tmps2 = tmps.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
			
			progress.setComplete();
			progress.setStatus("Complete. file name :"+user_filename+", File size:"+tmps2+" Bytes.");
			progress.toggleCancel(false);
			
			if(typeof(fileInfoHiddenFields) === "object" )
    		{
    			for (var name in fileInfoHiddenFields)
    			{
    				if (fileInfoHiddenFields.hasOwnProperty(name))
    				{
    					//console.log(name.toString()+"==>"+fileInfoHiddenFields[name].toString());
    					try
    					{
	    					if(name.toString() == 'filename')
	    					{
	    						if(jQuery('#'+fileInfoHiddenFields[name].toString()).val() == '')
	    						{
	    							jQuery('#'+fileInfoHiddenFields[name].toString()).val(filename);
	    						} else
	    						{
	    							jQuery('#'+fileInfoHiddenFields[name].toString()).val(jQuery('#'+fileInfoHiddenFields[name].toString()).val()+','+filename);
	    						}	    						
	    					} else if(name.toString() == 'path')
	    					{
	    						if(jQuery('#'+fileInfoHiddenFields[name].toString()).val() == '')
	    						{
	    							jQuery('#'+fileInfoHiddenFields[name].toString()).val(path);
	    						} else
	    						{
	    							jQuery('#'+fileInfoHiddenFields[name].toString()).val(jQuery('#'+fileInfoHiddenFields[name].toString()).val()+','+path);
	    						}
	    					} else if(name.toString() == 'extname')
	    					{
	    						if(jQuery('#'+fileInfoHiddenFields[name].toString()).val() == '')
	    						{
	    							jQuery('#'+fileInfoHiddenFields[name].toString()).val(extname);
	    						} else
	    						{
	    							jQuery('#'+fileInfoHiddenFields[name].toString()).val(jQuery('#'+fileInfoHiddenFields[name].toString()).val()+','+extname);
	    						}
	    					} else if(name.toString() == 'filesize')
	    					{
	    						if(jQuery('#'+fileInfoHiddenFields[name].toString()).val() == '')
	    						{
	    							jQuery('#'+fileInfoHiddenFields[name].toString()).val(filesize);
	    						} else
	    						{
	    							jQuery('#'+fileInfoHiddenFields[name].toString()).val(jQuery('#'+fileInfoHiddenFields[name].toString()).val()+','+filesize);
	    						}	    						
	    					} else if(name.toString() == 'user_filename')
	    					{
	    						if(jQuery('#'+fileInfoHiddenFields[name].toString()).val() == '')
	    						{
	    							jQuery('#'+fileInfoHiddenFields[name].toString()).val(user_filename);
	    						} else
	    						{
	    							jQuery('#'+fileInfoHiddenFields[name].toString()).val(jQuery('#'+fileInfoHiddenFields[name].toString()).val()+','+user_filename);
	    						}
	    					}
    					} catch(e)
    					{
    						console.log(name.toString()+"==>"+fileInfoHiddenFields[name].toString()+" ==>"+e);
    						this.debug(e);
    					}
    					//fileInfoHiddenFieldsPairs.push();
    				}
    			}
    		}
			
		} else
		{
			console.log(code+':'+msg);
			progress.setError();
			progress.toggleCancel(false);
			setNumFilesQueued(-1);
			progress.setStatus("Upload Error("+code+"):"+msg);
			this.debug("Error Code: Custom Error, File name: " + file.name + ", File size: " + file.size + ", Message: " + msg);
			isContainError = true;
		}

	} catch (ex) {
		this.debug(ex);
	}
}



function uploadError(file, errorCode, message) {
	try {
		isContainError = true;
		//console.log(this.customSettings.progressTarget);
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		//console.log('1-1');
		progress.setError();
		//console.log('1-2');
		progress.toggleCancel(false);
		//console.log('1-3');
		setNumFilesQueued(-1);
		//console.log('1-4');
		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
			progress.setStatus("Upload Error: " + message);
			this.debug("Error Code: HTTP Error, File name: " + file.name + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
			progress.setStatus("Upload Failed.");
			this.debug("Error Code: Upload Failed, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.IO_ERROR:
			progress.setStatus("Server (IO) Error");
			this.debug("Error Code: IO Error, File name: " + file.name + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
			progress.setStatus("Security Error");
			this.debug("Error Code: Security Error, File name: " + file.name + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			progress.setStatus("Upload limit exceeded.");
			this.debug("Error Code: Upload Limit Exceeded, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
			progress.setStatus("Failed Validation.  Upload skipped.");
			this.debug("Error Code: File Validation Failed, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
			// If there aren't any files left (they were all cancelled) disable the cancel button
			if (this.getStats().files_queued === 0) {
				document.getElementById(this.customSettings.cancelButtonId).disabled = true;
			}
			progress.setStatus("Cancelled");
			progress.setCancelled();
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			progress.setStatus("Stopped");
			break;
		case SWFUpload.UPLOAD_ERROR.CUSTEM_ERROR :
			//%console.log('2'+errorCode+message);
			progress.setStatus("Upload Error("+errorCode+"):"+message);
			this.debug("Error Code: Custom Error, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		default:
			progress.setStatus("Unhandled Error: " + errorCode);
			this.debug("Error Code: " + errorCode + ", File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		}
	} catch (ex) {
		//console.log(ex);
        this.debug(ex);
    }
}

function uploadComplete(file) {
	
	if (this.getStats().files_queued === 0) {
		document.getElementById(this.customSettings.cancelButtonId).disabled = true;
	}
}

// This event comes from the Queue Plugin
function queueComplete(numFilesUploaded) 
{
	//%console.log('queueComplete:'+numFilesUploaded);
	var status = document.getElementById(this.customSettings.statusDivId);
	status.innerHTML = numFilesUploaded + " file" + (numFilesUploaded === 1 ? "" : "s") + " uploaded.";
	var formParam = this.customSettings.paramForm;
	if(this.customSettings.callbackFunc != null) 
	{
		var fn = window[this.customSettings.callbackFunc];
		try
		{
			if(isContainError)
			{
				if(confirm(msg_com_code_034))
				{
					fn.call(window, formParam);
				} else
				{
					alert(msg_com_code_035);
				}
			} else
			{
				fn.call(window, formParam);
			}
		} catch(e)
		{
			this.debug(this.customSettings.callbackFunc+' is not exist!!');
		}
	}
}

function setNumFilesQueued(num)
{
	__numFilesQueued += num;
	//%console.log('__numFilesQueued:'+__numFilesQueued)
}
