

/* 
 *	krcode tab script
 *	http://krcode.kr/
 *	김기락
*/

/*	 < Full sample > 
.*
.*	var rolling_index = new krcodeTab ({
.*		tab : ".tabNavigation a"					//		String		tab btn's  selector
.*		,btnLeft : ".left"							//		String		left btn's  selector
.*		,btnRight : ".right"						//		String		right btn's  selector
.*		,content :".tab"							//		String		Contents  selector
.*		,addClass :  'selected'						//		String		(current class)  
.*		,parentAddClass : "selected"				//		String		parentNode addClass
.*		,changeClass : [ 'off' , 'on' ]				//		Array		[ (nomal class) , (current class) ]
.*		,parentChangeClass : [ 'off' , 'on' ]		//		Array		[ (nomal class) , (current class) ]
.*		,changeSrc : [ '_off.jpg' , '_on.jpg' ]		//		Array		[ (nomal src) , (current src) ]
.*		,eventType : "mouseover"					//		String		eventType ( 'click', 'mouseover', 'focus' )
.*		,effect  : 'slide'							//		String		effectType ( 'slide', 'toggle'  , 'fade' ) 
.*		,interval : 1000							//		int			rolling timer interval
.*		,rolling : false							//		boolean		rolling timer true false
.*		,timerToggle: ".toggle"						//		String or Array		timerToggle btn		".toggle" or	[".start",".stop"]	
.*		,timerToggleSrc: ["_on","_off"]				//		Array				timerToggle btn src cahnge			["_start.gif","_stop.gif"]	
.*		,timerToggleClass: ["on","off"]				//		Array				timerToggle btn class cahnge		["on","off"]	
.*	});
.*	
.*	
.*	< simple sample 1 tab script >
.*	
.*	var rolling_index = new krcodeTab ({
.*		 tab : ".tabNavigation a"					//		String   tab btn's  selector
.*		,content :".tabNavigation .tab"				//		String   Contents  selector
.*		,eventType : "mouseover"					//		String	eventType ( 'click mouseover focus' )
.*	});
.*	
.*	
.*	< simple sample 2 left,right script & rolling >
.*	
.*	var rolling_index = new krcodeTab ({
.*		 btnLeft : ".left"							//		String   left btn's  selector
.*		,btnRight : ".right"						//		String   right btn's  selector
.*		,content :".tabNavigation .tab"				//		String   Contents  selector
.*		,eventType : "mouseover"					//		String	eventType ( 'click', 'mouseover ', 'mouseover focus' )
.*		,interval : 3000							//		int			rolling timer interval
.*		,rolling : true								//		boolean		rolling timer true false
.*	});
.*	
.*	
.*	
*/

var krcodeHover = function(seletor){
	var img = $(seletor);
	if(img[0].nodeName.toLowerCase() != "img"){
		img.each(function(){
			this.chk = "other";
			this.img = $("img",this)[0];
		})
	}
	img.hover(function(){
		if( this.chk == "other"){
			this.img.src = this.img.src.replace("_off","_on");
		}else{
			this.src = this.src.replace("_off","_on");
		}
	},function(){
		if( this.chk == "other"){
			this.img.src = this.img.src.replace("_on","_off");
		}else{
			this.src = this.src.replace("_on","_off");
		}
	})
}


var krcodeTab = function(mass){

	var This = this;
	// elements
	this.btn = $(mass.tab).each(function(i){this.n =i;this.img = this.getElementsByTagName('img')[0];});
	this.btnLeft = $(mass.btnLeft);
	this.btnRight = $(mass.btnRight);
	this.cont = $(mass.content);
	this.contParent = this.cont.parent();
	this.timerToggle = mass.timerToggle;
	this.swipeCont = mass.swipeCont;
	this.timerToggleSrc = mass.timerToggleSrc;
	this.timerToggleClass = mass.timerToggleClass;

	//  number
	this.length = this.cont.length;
	this.defaultNum = this.cont.length*1000;
	this.statusNum = this.defaultNum;
	this.interval = mass.interval ? mass.interval : 3000;
	this.initNum = mass.index | 0;
	this.timer =null;

	// array
	this.tabClass = mass.changeClass;
	this.parentClass_rpl = mass.parentChangeClass || false;
	this.addClass = mass.addClass;
	this.parentClass_add = mass.parentAddClass || false;
	this.tabSrc = mass.changeSrc;
	
	if(this.parentClass_rpl){
		This.tabClass = This.parentClass_rpl;
	}
	if(this.parentClass_add){
		This.addClass = This.parentClass_add;
	}


	// boolean
	this.rolling = mass.rolling || false;
	this.motionChk = false;

	// string
	var effect = mass.effect;

	// event setting
	this.eventType = mass.eventType ? mass.eventType : "mouseover focus";
	this.start = function(){
		this.stop();
		if(This.rolling){
			This.stop();
			this.timer = setInterval(function(){
				This.statusNum++;
				This.action((This.statusNum)%This.cont.length);
			},this.interval);
		}
	};

	this.tabAction = function(num,btn){
		if((this.tabClass || this.parentClass_rpl) && this.btn.length){
			this.changeClassAction(num,btn);
		};
		if((this.addClass || this.parentClass_add) && this.btn.length){
			this.addClassAction(num,btn);
		};
		if(this.tabSrc && this.btn.length){
			this.tabSrcReplace(num,btn);
		}

	};
	this.action = function(num,btn){
		if(mass.effect){
			this.effect[mass.effect](num,btn);
		}else{
			this.effect["toggle"](num,btn);
			//mass.action(This,num);
		};

		if(This.length == num && !btn) num = 0;
		else if(This.length == num && btn=="R") num = 0;
		else if(This.length == num && btn=="L") num--;
		
		this.tabAction(num,btn);

		if(mass.callback){
			mass.callback.call("",num,this.btn,this.cont)
		}

	};


	// class  &  src change
	this.changeClassAction = function(num){
		this.btn.each(function(i){
			var obj = this;
			if(This.parentClass_rpl){
				obj = this.parentNode;
			}
			if(i != num ){
				obj.className = obj.className.replace(This.tabClass[1],This.tabClass[0]);
			}else{
				obj.className = obj.className.replace(This.tabClass[0],This.tabClass[1]);
			}
		});
	};
	this.addClassAction = function(num){
		var obj = this.btn;
		if(this.parentClass_add){
			obj.parent().removeClass(This.parentClass_add);
			obj.parent().eq(num).addClass(This.parentClass_add);
		}else{
			obj.removeClass(This.addClass).eq(num).addClass(This.addClass);
		}
	};
	this.tabSrcReplace = function(num){
		This.btn.each(function(j){
			if(j == num) this.img.src = this.img.src.replace(This.tabSrc[0],This.tabSrc[1]);
			else if(j==0 &&  num== This.length) this.img.src = this.img.src.replace(This.tabSrc[0],This.tabSrc[1]);
			else this.img.src = this.img.src.replace(This.tabSrc[1],This.tabSrc[0]);
		});
	};


	// rolling setting
	this.stop = function(){
		if(this.timer) clearInterval(this.timer);
	};
	this.cont.hover(function(){
		This.stop();
		if(This.timerToggleSrc){
			This.timerToggleImg.src = This.timerToggleImg.src.replace(This.timerToggleSrc[0],This.timerToggleSrc[1])
		}
			
		if(This.timerToggleClass){
			This.timerToggle.removeClass(This.timerToggleClass[0]).addClass(This.timerToggleClass[1])
		}
	},function(){
		This.start();
		if(This.timerToggleSrc){
			This.timerToggleImg.src = This.timerToggleImg.src.replace(This.timerToggleSrc[1],This.timerToggleSrc[0])
		}
		if(This.timerToggleClass){
			This.timerToggle.removeClass(This.timerToggleClass[1]).addClass(This.timerToggleClass[0])
		}
	});
	this.cont.find("a").focus(function(){
		This.stop();
	});
	this.cont.find("a").blur(function(){
		This.start();
	});
	this.btn.find("a").focus(function(){
		This.stop();
	});
	this.btn.find("a").blur(function(){
		This.start();
	});
	if(this.timerToggle){
		if((typeof this.timerToggle).toLowerCase() == "string"){

			this.timerToggle = $(this.timerToggle);
			
			if(this.timerToggleSrc){
				if(this.timerToggle[0].nodeName == "img"){
					this.timerToggleImg = this.timerToggle[0];
				}else{
					this.timerToggleImg = this.timerToggle.find("img")[0];
				}
			}
			if(this.timerToggleClass){
				this.timerToggle.addClass(This.timerToggleClass[0]);
			}
			this.timerToggle.click(function(){
				if(This.rolling){
					This.stop();
					This.rolling = false;
					if(This.timerToggleSrc){
						This.timerToggleImg.src = This.timerToggleImg.src.replace(This.timerToggleSrc[0],This.timerToggleSrc[1])
					}
					if(This.timerToggleClass){
						This.timerToggle.removeClass(This.timerToggleClass[0]).addClass(This.timerToggleClass[1])
					}
				}else{
					This.rolling = true;
					This.start();
					if(This.timerToggleSrc){
						This.timerToggleImg.src = This.timerToggleImg.src.replace(This.timerToggleSrc[1],This.timerToggleSrc[0])
					}
					if(This.timerToggleClass){
						This.timerToggle.removeClass(This.timerToggleClass[1]).addClass(This.timerToggleClass[0])
					}
				}
			});
		}else if(this.timerToggle.length){
			$(this.timerToggle[0]).click(function(){
				This.rolling = true;
				This.start();
			});
			$(this.timerToggle[1]).click(function(){
				This.stop();
				This.rolling = false;
			});
		}
	};


	// effect setting
	this.effect = {
		toggle : function(num){
			This.cont.hide().eq(num).show();
		},
		slideInit : function(num){
			var wid = This.cont.width();
			This.cont[This.length-1].parentNode.appendChild(This.cont[0].cloneNode(true));
			This.cont = $(mass.content);
			This.contParent.css({
				position:'absolute',
				width: This.cont.length * wid
			});
			This.contParent.itemWid =  wid;
			
			This.statusNum = This.cont.length*1000;
			This.cont.css({
				position:'absolute'
			}).each(function(i){
				this.style.left = wid * (i-This.initNum) + "px";
			});
		},
		slide : function(num,btn){ 
			This.motionChk = true;
			if(num == This.length && btn == "L"){
				This.statusNum--; num--;
				This.contParent[0].style.left = -This.length*This.contParent.itemWid + "px";
			}
			This.contParent.stop().animate({left:-(num-This.initNum)*This.contParent.itemWid},function(){
				This.motionChk = false;
				if(num == This.length && btn!="L"){
					This.statusNum = This.cont.length *500;
					this.style.left = 0 + "px";
				} ;
			});
		},
		newslideInit : function(num){
			var wid = mass.slideWith;
			
			This.cont[This.length-1].parentNode.appendChild(This.cont[0].cloneNode(true));
			This.cont = $(mass.content);
			This.statusNum = This.cont.length *1000
			This.cont.each(function(i){
				$(this).css({
					position:'absolute',
					left : wid * (i-This.initNum)
				});
			});
			This.contParent.itemWid =  wid;
			//This.statusNum = This.cont.length*1000;
		},
		newslide : function(num,btn){ 
			var wid = mass.slideWith;
			This.motionChk = true;
			if(btn){   // left right btn
				if(num == This.length && btn == "L"){}

			}else{		// tab 
				This.cont.each(function(i){
					$(this).stop().animate({
						left : wid * (i-num)
					},function(){
						if(i==This.length && num==This.length){
							if(num == This.length){
								This.statusNum = 0; 
								This.cont.each(function(i){
									$(this).stop().css({
										left : wid * (i)
									});
								});
							}
						}
					});
				});
			}
		},
		fadeInit : function(num){
			var top = This.cont.eq(0).css("top") ? This.cont.eq(0).css("top") : 0;
			var left = This.cont.eq(0).css("left") ? This.cont.eq(0).css("left") : 0;
			This.cont.css({'position':'absolute','top':top,'left':left,"opacity":0}).eq(This.initNum).css({"opacity":1,zIndex:2});
			
			var tabCHk = 0
			This.cont.each(function(n){
				$("a",this).focus(function(){
					tabCHk++
					This.cont.eq(n).css({"opacity":1,zIndex:tabCHk});
				})
			})
		},
		fade : function(num){
			This.cont.each(function(k){
				var $this = $(this);
				if(k == num){
					$this.animate({
						opacity:1
					},function(){
						$this.css({
							zIndex:2
						})	
					});
				}else{
					$this.animate({
						opacity:0
					},function(){
						$this.css({
							zIndex:1
						})	
					});
				};
			});
		}
	};

	if(effect  == 'slide'){
		this.effect.slideInit();
	}else if(effect  == 'newslide'){
		this.effect.newslideInit();
	}else if(effect  == 'fade'){
		this.effect.fadeInit();
	};

	//  btn event   tab style
	if(this.btn.length){
		this.btn.hover(function(){
			This.stop();
		},function(){
			This.start();
		});
		this.btn.bind(mass.eventType , function(){
			This.statusNum = this.n;
			This.action(This.statusNum%This.cont.length);
			This.start();
			return false;
		});
		this.btn.bind("focus" , function(){
			This.contParent.parent().scrollLeft(0);
		});
	};
	
	if(mass.eventType.indexOf("swipe")>=0){
		this.cont.swipe( {
		  swipe:function(event, direction, distance, duration, fingerCount, fingerData) {
		//alert(direction)
			  if(direction == "left"){
					This.statusNum = This.cont.length + (This.statusNum-1);
					This.action(This.statusNum%This.cont.length,"L");
			  }else if(direction == "right"){
					This.statusNum++;
					This.action(This.statusNum%This.cont.length,"R");
			  }
			This.start();
			//$("#test").text("You swiped " + direction + " with " + fingerCount + " fingers");
		  },
		  threshold:50
		});
	}

	//  btn event   left & right style
	if(this.btnLeft.length){
		this.btnLeft.click(function(){
			if(This.motionChk ) return false;
			This.statusNum = This.cont.length + (This.statusNum-1);
			This.action(This.statusNum%This.cont.length,"L");
			This.start();
			return false;
		});
	};
	if(this.btnRight.length){
		this.btnRight.click(function(){
			if(This.motionChk ) return false;
			This.statusNum++;
			This.action(This.statusNum%This.cont.length,"R");
			This.start();
			return false;
		});
	};

	//  init
	this.action(this.initNum);
	this.start();
};


/*!
 * @fileOverview TouchSwipe - jQuery Plugin
 * @version 1.6.18
 *
 * @author Matt Bryson http://www.github.com/mattbryson
 * @see https://github.com/mattbryson/TouchSwipe-Jquery-Plugin
 * @see http://labs.rampinteractive.co.uk/touchSwipe/
 * @see http://plugins.jquery.com/project/touchSwipe
 * @license
 * Copyright (c) 2010-2015 Matt Bryson
 * Dual licensed under the MIT or GPL Version 2 licenses.
 *
 */
!function(factory){"function"==typeof define&&define.amd&&define.amd.jQuery?define(["jquery"],factory):factory("undefined"!=typeof module&&module.exports?require("jquery"):jQuery)}(function($){"use strict";function init(options){return!options||void 0!==options.allowPageScroll||void 0===options.swipe&&void 0===options.swipeStatus||(options.allowPageScroll=NONE),void 0!==options.click&&void 0===options.tap&&(options.tap=options.click),options||(options={}),options=$.extend({},$.fn.swipe.defaults,options),this.each(function(){var $this=$(this),plugin=$this.data(PLUGIN_NS);plugin||(plugin=new TouchSwipe(this,options),$this.data(PLUGIN_NS,plugin))})}function TouchSwipe(element,options){function touchStart(jqEvent){if(!(getTouchInProgress()||$(jqEvent.target).closest(options.excludedElements,$element).length>0)){var event=jqEvent.originalEvent?jqEvent.originalEvent:jqEvent;if(!event.pointerType||"mouse"!=event.pointerType||0!=options.fallbackToMouseEvents){var ret,touches=event.touches,evt=touches?touches[0]:event;return phase=PHASE_START,touches?fingerCount=touches.length:options.preventDefaultEvents!==!1&&jqEvent.preventDefault(),distance=0,direction=null,currentDirection=null,pinchDirection=null,duration=0,startTouchesDistance=0,endTouchesDistance=0,pinchZoom=1,pinchDistance=0,maximumsMap=createMaximumsData(),cancelMultiFingerRelease(),createFingerData(0,evt),!touches||fingerCount===options.fingers||options.fingers===ALL_FINGERS||hasPinches()?(startTime=getTimeStamp(),2==fingerCount&&(createFingerData(1,touches[1]),startTouchesDistance=endTouchesDistance=calculateTouchesDistance(fingerData[0].start,fingerData[1].start)),(options.swipeStatus||options.pinchStatus)&&(ret=triggerHandler(event,phase))):ret=!1,ret===!1?(phase=PHASE_CANCEL,triggerHandler(event,phase),ret):(options.hold&&(holdTimeout=setTimeout($.proxy(function(){$element.trigger("hold",[event.target]),options.hold&&(ret=options.hold.call($element,event,event.target))},this),options.longTapThreshold)),setTouchInProgress(!0),null)}}}function touchMove(jqEvent){var event=jqEvent.originalEvent?jqEvent.originalEvent:jqEvent;if(phase!==PHASE_END&&phase!==PHASE_CANCEL&&!inMultiFingerRelease()){var ret,touches=event.touches,evt=touches?touches[0]:event,currentFinger=updateFingerData(evt);if(endTime=getTimeStamp(),touches&&(fingerCount=touches.length),options.hold&&clearTimeout(holdTimeout),phase=PHASE_MOVE,2==fingerCount&&(0==startTouchesDistance?(createFingerData(1,touches[1]),startTouchesDistance=endTouchesDistance=calculateTouchesDistance(fingerData[0].start,fingerData[1].start)):(updateFingerData(touches[1]),endTouchesDistance=calculateTouchesDistance(fingerData[0].end,fingerData[1].end),pinchDirection=calculatePinchDirection(fingerData[0].end,fingerData[1].end)),pinchZoom=calculatePinchZoom(startTouchesDistance,endTouchesDistance),pinchDistance=Math.abs(startTouchesDistance-endTouchesDistance)),fingerCount===options.fingers||options.fingers===ALL_FINGERS||!touches||hasPinches()){if(direction=calculateDirection(currentFinger.start,currentFinger.end),currentDirection=calculateDirection(currentFinger.last,currentFinger.end),validateDefaultEvent(jqEvent,currentDirection),distance=calculateDistance(currentFinger.start,currentFinger.end),duration=calculateDuration(),setMaxDistance(direction,distance),ret=triggerHandler(event,phase),!options.triggerOnTouchEnd||options.triggerOnTouchLeave){var inBounds=!0;if(options.triggerOnTouchLeave){var bounds=getbounds(this);inBounds=isInBounds(currentFinger.end,bounds)}!options.triggerOnTouchEnd&&inBounds?phase=getNextPhase(PHASE_MOVE):options.triggerOnTouchLeave&&!inBounds&&(phase=getNextPhase(PHASE_END)),phase!=PHASE_CANCEL&&phase!=PHASE_END||triggerHandler(event,phase)}}else phase=PHASE_CANCEL,triggerHandler(event,phase);ret===!1&&(phase=PHASE_CANCEL,triggerHandler(event,phase))}}function touchEnd(jqEvent){var event=jqEvent.originalEvent?jqEvent.originalEvent:jqEvent,touches=event.touches;if(touches){if(touches.length&&!inMultiFingerRelease())return startMultiFingerRelease(event),!0;if(touches.length&&inMultiFingerRelease())return!0}return inMultiFingerRelease()&&(fingerCount=fingerCountAtRelease),endTime=getTimeStamp(),duration=calculateDuration(),didSwipeBackToCancel()||!validateSwipeDistance()?(phase=PHASE_CANCEL,triggerHandler(event,phase)):options.triggerOnTouchEnd||options.triggerOnTouchEnd===!1&&phase===PHASE_MOVE?(options.preventDefaultEvents!==!1&&jqEvent.preventDefault(),phase=PHASE_END,triggerHandler(event,phase)):!options.triggerOnTouchEnd&&hasTap()?(phase=PHASE_END,triggerHandlerForGesture(event,phase,TAP)):phase===PHASE_MOVE&&(phase=PHASE_CANCEL,triggerHandler(event,phase)),setTouchInProgress(!1),null}function touchCancel(){fingerCount=0,endTime=0,startTime=0,startTouchesDistance=0,endTouchesDistance=0,pinchZoom=1,cancelMultiFingerRelease(),setTouchInProgress(!1)}function touchLeave(jqEvent){var event=jqEvent.originalEvent?jqEvent.originalEvent:jqEvent;options.triggerOnTouchLeave&&(phase=getNextPhase(PHASE_END),triggerHandler(event,phase))}function removeListeners(){$element.unbind(START_EV,touchStart),$element.unbind(CANCEL_EV,touchCancel),$element.unbind(MOVE_EV,touchMove),$element.unbind(END_EV,touchEnd),LEAVE_EV&&$element.unbind(LEAVE_EV,touchLeave),setTouchInProgress(!1)}function getNextPhase(currentPhase){var nextPhase=currentPhase,validTime=validateSwipeTime(),validDistance=validateSwipeDistance(),didCancel=didSwipeBackToCancel();return!validTime||didCancel?nextPhase=PHASE_CANCEL:!validDistance||currentPhase!=PHASE_MOVE||options.triggerOnTouchEnd&&!options.triggerOnTouchLeave?!validDistance&&currentPhase==PHASE_END&&options.triggerOnTouchLeave&&(nextPhase=PHASE_CANCEL):nextPhase=PHASE_END,nextPhase}function triggerHandler(event,phase){var ret,touches=event.touches;return(didSwipe()||hasSwipes())&&(ret=triggerHandlerForGesture(event,phase,SWIPE)),(didPinch()||hasPinches())&&ret!==!1&&(ret=triggerHandlerForGesture(event,phase,PINCH)),didDoubleTap()&&ret!==!1?ret=triggerHandlerForGesture(event,phase,DOUBLE_TAP):didLongTap()&&ret!==!1?ret=triggerHandlerForGesture(event,phase,LONG_TAP):didTap()&&ret!==!1&&(ret=triggerHandlerForGesture(event,phase,TAP)),phase===PHASE_CANCEL&&touchCancel(event),phase===PHASE_END&&(touches?touches.length||touchCancel(event):touchCancel(event)),ret}function triggerHandlerForGesture(event,phase,gesture){var ret;if(gesture==SWIPE){if($element.trigger("swipeStatus",[phase,direction||null,distance||0,duration||0,fingerCount,fingerData,currentDirection]),options.swipeStatus&&(ret=options.swipeStatus.call($element,event,phase,direction||null,distance||0,duration||0,fingerCount,fingerData,currentDirection),ret===!1))return!1;if(phase==PHASE_END&&validateSwipe()){if(clearTimeout(singleTapTimeout),clearTimeout(holdTimeout),$element.trigger("swipe",[direction,distance,duration,fingerCount,fingerData,currentDirection]),options.swipe&&(ret=options.swipe.call($element,event,direction,distance,duration,fingerCount,fingerData,currentDirection),ret===!1))return!1;switch(direction){case LEFT:$element.trigger("swipeLeft",[direction,distance,duration,fingerCount,fingerData,currentDirection]),options.swipeLeft&&(ret=options.swipeLeft.call($element,event,direction,distance,duration,fingerCount,fingerData,currentDirection));break;case RIGHT:$element.trigger("swipeRight",[direction,distance,duration,fingerCount,fingerData,currentDirection]),options.swipeRight&&(ret=options.swipeRight.call($element,event,direction,distance,duration,fingerCount,fingerData,currentDirection));break;case UP:$element.trigger("swipeUp",[direction,distance,duration,fingerCount,fingerData,currentDirection]),options.swipeUp&&(ret=options.swipeUp.call($element,event,direction,distance,duration,fingerCount,fingerData,currentDirection));break;case DOWN:$element.trigger("swipeDown",[direction,distance,duration,fingerCount,fingerData,currentDirection]),options.swipeDown&&(ret=options.swipeDown.call($element,event,direction,distance,duration,fingerCount,fingerData,currentDirection))}}}if(gesture==PINCH){if($element.trigger("pinchStatus",[phase,pinchDirection||null,pinchDistance||0,duration||0,fingerCount,pinchZoom,fingerData]),options.pinchStatus&&(ret=options.pinchStatus.call($element,event,phase,pinchDirection||null,pinchDistance||0,duration||0,fingerCount,pinchZoom,fingerData),ret===!1))return!1;if(phase==PHASE_END&&validatePinch())switch(pinchDirection){case IN:$element.trigger("pinchIn",[pinchDirection||null,pinchDistance||0,duration||0,fingerCount,pinchZoom,fingerData]),options.pinchIn&&(ret=options.pinchIn.call($element,event,pinchDirection||null,pinchDistance||0,duration||0,fingerCount,pinchZoom,fingerData));break;case OUT:$element.trigger("pinchOut",[pinchDirection||null,pinchDistance||0,duration||0,fingerCount,pinchZoom,fingerData]),options.pinchOut&&(ret=options.pinchOut.call($element,event,pinchDirection||null,pinchDistance||0,duration||0,fingerCount,pinchZoom,fingerData))}}return gesture==TAP?phase!==PHASE_CANCEL&&phase!==PHASE_END||(clearTimeout(singleTapTimeout),clearTimeout(holdTimeout),hasDoubleTap()&&!inDoubleTap()?(doubleTapStartTime=getTimeStamp(),singleTapTimeout=setTimeout($.proxy(function(){doubleTapStartTime=null,$element.trigger("tap",[event.target]),options.tap&&(ret=options.tap.call($element,event,event.target))},this),options.doubleTapThreshold)):(doubleTapStartTime=null,$element.trigger("tap",[event.target]),options.tap&&(ret=options.tap.call($element,event,event.target)))):gesture==DOUBLE_TAP?phase!==PHASE_CANCEL&&phase!==PHASE_END||(clearTimeout(singleTapTimeout),clearTimeout(holdTimeout),doubleTapStartTime=null,$element.trigger("doubletap",[event.target]),options.doubleTap&&(ret=options.doubleTap.call($element,event,event.target))):gesture==LONG_TAP&&(phase!==PHASE_CANCEL&&phase!==PHASE_END||(clearTimeout(singleTapTimeout),doubleTapStartTime=null,$element.trigger("longtap",[event.target]),options.longTap&&(ret=options.longTap.call($element,event,event.target)))),ret}function validateSwipeDistance(){var valid=!0;return null!==options.threshold&&(valid=distance>=options.threshold),valid}function didSwipeBackToCancel(){var cancelled=!1;return null!==options.cancelThreshold&&null!==direction&&(cancelled=getMaxDistance(direction)-distance>=options.cancelThreshold),cancelled}function validatePinchDistance(){return null!==options.pinchThreshold?pinchDistance>=options.pinchThreshold:!0}function validateSwipeTime(){var result;return result=options.maxTimeThreshold?!(duration>=options.maxTimeThreshold):!0}function validateDefaultEvent(jqEvent,direction){if(options.preventDefaultEvents!==!1)if(options.allowPageScroll===NONE)jqEvent.preventDefault();else{var auto=options.allowPageScroll===AUTO;switch(direction){case LEFT:(options.swipeLeft&&auto||!auto&&options.allowPageScroll!=HORIZONTAL)&&jqEvent.preventDefault();break;case RIGHT:(options.swipeRight&&auto||!auto&&options.allowPageScroll!=HORIZONTAL)&&jqEvent.preventDefault();break;case UP:(options.swipeUp&&auto||!auto&&options.allowPageScroll!=VERTICAL)&&jqEvent.preventDefault();break;case DOWN:(options.swipeDown&&auto||!auto&&options.allowPageScroll!=VERTICAL)&&jqEvent.preventDefault();break;case NONE:}}}function validatePinch(){var hasCorrectFingerCount=validateFingers(),hasEndPoint=validateEndPoint(),hasCorrectDistance=validatePinchDistance();return hasCorrectFingerCount&&hasEndPoint&&hasCorrectDistance}function hasPinches(){return!!(options.pinchStatus||options.pinchIn||options.pinchOut)}function didPinch(){return!(!validatePinch()||!hasPinches())}function validateSwipe(){var hasValidTime=validateSwipeTime(),hasValidDistance=validateSwipeDistance(),hasCorrectFingerCount=validateFingers(),hasEndPoint=validateEndPoint(),didCancel=didSwipeBackToCancel(),valid=!didCancel&&hasEndPoint&&hasCorrectFingerCount&&hasValidDistance&&hasValidTime;return valid}function hasSwipes(){return!!(options.swipe||options.swipeStatus||options.swipeLeft||options.swipeRight||options.swipeUp||options.swipeDown)}function didSwipe(){return!(!validateSwipe()||!hasSwipes())}function validateFingers(){return fingerCount===options.fingers||options.fingers===ALL_FINGERS||!SUPPORTS_TOUCH}function validateEndPoint(){return 0!==fingerData[0].end.x}function hasTap(){return!!options.tap}function hasDoubleTap(){return!!options.doubleTap}function hasLongTap(){return!!options.longTap}function validateDoubleTap(){if(null==doubleTapStartTime)return!1;var now=getTimeStamp();return hasDoubleTap()&&now-doubleTapStartTime<=options.doubleTapThreshold}function inDoubleTap(){return validateDoubleTap()}function validateTap(){return(1===fingerCount||!SUPPORTS_TOUCH)&&(isNaN(distance)||distance<options.threshold)}function validateLongTap(){return duration>options.longTapThreshold&&DOUBLE_TAP_THRESHOLD>distance}function didTap(){return!(!validateTap()||!hasTap())}function didDoubleTap(){return!(!validateDoubleTap()||!hasDoubleTap())}function didLongTap(){return!(!validateLongTap()||!hasLongTap())}function startMultiFingerRelease(event){previousTouchEndTime=getTimeStamp(),fingerCountAtRelease=event.touches.length+1}function cancelMultiFingerRelease(){previousTouchEndTime=0,fingerCountAtRelease=0}function inMultiFingerRelease(){var withinThreshold=!1;if(previousTouchEndTime){var diff=getTimeStamp()-previousTouchEndTime;diff<=options.fingerReleaseThreshold&&(withinThreshold=!0)}return withinThreshold}function getTouchInProgress(){return!($element.data(PLUGIN_NS+"_intouch")!==!0)}function setTouchInProgress(val){$element&&(val===!0?($element.bind(MOVE_EV,touchMove),$element.bind(END_EV,touchEnd),LEAVE_EV&&$element.bind(LEAVE_EV,touchLeave)):($element.unbind(MOVE_EV,touchMove,!1),$element.unbind(END_EV,touchEnd,!1),LEAVE_EV&&$element.unbind(LEAVE_EV,touchLeave,!1)),$element.data(PLUGIN_NS+"_intouch",val===!0))}function createFingerData(id,evt){var f={start:{x:0,y:0},last:{x:0,y:0},end:{x:0,y:0}};return f.start.x=f.last.x=f.end.x=evt.pageX||evt.clientX,f.start.y=f.last.y=f.end.y=evt.pageY||evt.clientY,fingerData[id]=f,f}function updateFingerData(evt){var id=void 0!==evt.identifier?evt.identifier:0,f=getFingerData(id);return null===f&&(f=createFingerData(id,evt)),f.last.x=f.end.x,f.last.y=f.end.y,f.end.x=evt.pageX||evt.clientX,f.end.y=evt.pageY||evt.clientY,f}function getFingerData(id){return fingerData[id]||null}function setMaxDistance(direction,distance){direction!=NONE&&(distance=Math.max(distance,getMaxDistance(direction)),maximumsMap[direction].distance=distance)}function getMaxDistance(direction){return maximumsMap[direction]?maximumsMap[direction].distance:void 0}function createMaximumsData(){var maxData={};return maxData[LEFT]=createMaximumVO(LEFT),maxData[RIGHT]=createMaximumVO(RIGHT),maxData[UP]=createMaximumVO(UP),maxData[DOWN]=createMaximumVO(DOWN),maxData}function createMaximumVO(dir){return{direction:dir,distance:0}}function calculateDuration(){return endTime-startTime}function calculateTouchesDistance(startPoint,endPoint){var diffX=Math.abs(startPoint.x-endPoint.x),diffY=Math.abs(startPoint.y-endPoint.y);return Math.round(Math.sqrt(diffX*diffX+diffY*diffY))}function calculatePinchZoom(startDistance,endDistance){var percent=endDistance/startDistance*1;return percent.toFixed(2)}function calculatePinchDirection(){return 1>pinchZoom?OUT:IN}function calculateDistance(startPoint,endPoint){return Math.round(Math.sqrt(Math.pow(endPoint.x-startPoint.x,2)+Math.pow(endPoint.y-startPoint.y,2)))}function calculateAngle(startPoint,endPoint){var x=startPoint.x-endPoint.x,y=endPoint.y-startPoint.y,r=Math.atan2(y,x),angle=Math.round(180*r/Math.PI);return 0>angle&&(angle=360-Math.abs(angle)),angle}function calculateDirection(startPoint,endPoint){if(comparePoints(startPoint,endPoint))return NONE;var angle=calculateAngle(startPoint,endPoint);return 45>=angle&&angle>=0?LEFT:360>=angle&&angle>=315?LEFT:angle>=135&&225>=angle?RIGHT:angle>45&&135>angle?DOWN:UP}function getTimeStamp(){var now=new Date;return now.getTime()}function getbounds(el){el=$(el);var offset=el.offset(),bounds={left:offset.left,right:offset.left+el.outerWidth(),top:offset.top,bottom:offset.top+el.outerHeight()};return bounds}function isInBounds(point,bounds){return point.x>bounds.left&&point.x<bounds.right&&point.y>bounds.top&&point.y<bounds.bottom}function comparePoints(pointA,pointB){return pointA.x==pointB.x&&pointA.y==pointB.y}var options=$.extend({},options),useTouchEvents=SUPPORTS_TOUCH||SUPPORTS_POINTER||!options.fallbackToMouseEvents,START_EV=useTouchEvents?SUPPORTS_POINTER?SUPPORTS_POINTER_IE10?"MSPointerDown":"pointerdown":"touchstart":"mousedown",MOVE_EV=useTouchEvents?SUPPORTS_POINTER?SUPPORTS_POINTER_IE10?"MSPointerMove":"pointermove":"touchmove":"mousemove",END_EV=useTouchEvents?SUPPORTS_POINTER?SUPPORTS_POINTER_IE10?"MSPointerUp":"pointerup":"touchend":"mouseup",LEAVE_EV=useTouchEvents?SUPPORTS_POINTER?"mouseleave":null:"mouseleave",CANCEL_EV=SUPPORTS_POINTER?SUPPORTS_POINTER_IE10?"MSPointerCancel":"pointercancel":"touchcancel",distance=0,direction=null,currentDirection=null,duration=0,startTouchesDistance=0,endTouchesDistance=0,pinchZoom=1,pinchDistance=0,pinchDirection=0,maximumsMap=null,$element=$(element),phase="start",fingerCount=0,fingerData={},startTime=0,endTime=0,previousTouchEndTime=0,fingerCountAtRelease=0,doubleTapStartTime=0,singleTapTimeout=null,holdTimeout=null;try{$element.bind(START_EV,touchStart),$element.bind(CANCEL_EV,touchCancel)}catch(e){$.error("events not supported "+START_EV+","+CANCEL_EV+" on jQuery.swipe")}this.enable=function(){return this.disable(),$element.bind(START_EV,touchStart),$element.bind(CANCEL_EV,touchCancel),$element},this.disable=function(){return removeListeners(),$element},this.destroy=function(){removeListeners(),$element.data(PLUGIN_NS,null),$element=null},this.option=function(property,value){if("object"==typeof property)options=$.extend(options,property);else if(void 0!==options[property]){if(void 0===value)return options[property];options[property]=value}else{if(!property)return options;$.error("Option "+property+" does not exist on jQuery.swipe.options")}return null}}var VERSION="1.6.18",LEFT="left",RIGHT="right",UP="up",DOWN="down",IN="in",OUT="out",NONE="none",AUTO="auto",SWIPE="swipe",PINCH="pinch",TAP="tap",DOUBLE_TAP="doubletap",LONG_TAP="longtap",HORIZONTAL="horizontal",VERTICAL="vertical",ALL_FINGERS="all",DOUBLE_TAP_THRESHOLD=10,PHASE_START="start",PHASE_MOVE="move",PHASE_END="end",PHASE_CANCEL="cancel",SUPPORTS_TOUCH="ontouchstart"in window,SUPPORTS_POINTER_IE10=window.navigator.msPointerEnabled&&!window.navigator.pointerEnabled&&!SUPPORTS_TOUCH,SUPPORTS_POINTER=(window.navigator.pointerEnabled||window.navigator.msPointerEnabled)&&!SUPPORTS_TOUCH,PLUGIN_NS="TouchSwipe",defaults={fingers:1,threshold:75,cancelThreshold:null,pinchThreshold:20,maxTimeThreshold:null,fingerReleaseThreshold:250,longTapThreshold:500,doubleTapThreshold:200,swipe:null,swipeLeft:null,swipeRight:null,swipeUp:null,swipeDown:null,swipeStatus:null,pinchIn:null,pinchOut:null,pinchStatus:null,click:null,tap:null,doubleTap:null,longTap:null,hold:null,triggerOnTouchEnd:!0,triggerOnTouchLeave:!1,allowPageScroll:"auto",fallbackToMouseEvents:!0,excludedElements:".noSwipe",preventDefaultEvents:!0};$.fn.swipe=function(method){var $this=$(this),plugin=$this.data(PLUGIN_NS);if(plugin&&"string"==typeof method){if(plugin[method])return plugin[method].apply(plugin,Array.prototype.slice.call(arguments,1));$.error("Method "+method+" does not exist on jQuery.swipe")}else if(plugin&&"object"==typeof method)plugin.option.apply(plugin,arguments);else if(!(plugin||"object"!=typeof method&&method))return init.apply(this,arguments);return $this},$.fn.swipe.version=VERSION,$.fn.swipe.defaults=defaults,$.fn.swipe.phases={PHASE_START:PHASE_START,PHASE_MOVE:PHASE_MOVE,PHASE_END:PHASE_END,PHASE_CANCEL:PHASE_CANCEL},$.fn.swipe.directions={LEFT:LEFT,RIGHT:RIGHT,UP:UP,DOWN:DOWN,IN:IN,OUT:OUT},$.fn.swipe.pageScroll={NONE:NONE,HORIZONTAL:HORIZONTAL,VERTICAL:VERTICAL,AUTO:AUTO},$.fn.swipe.fingers={ONE:1,TWO:2,THREE:3,FOUR:4,FIVE:5,ALL:ALL_FINGERS}});
