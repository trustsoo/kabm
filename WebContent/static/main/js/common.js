
var gnbMenu  = function(pageNum,subNum,lastNum){
	var GNB = $('#gnb');
	if(!GNB.length) return false;
	var acTime = 500;
	var subTag = 'div';
	var one = GNB.find(".menu>li");
	var oneBtn = GNB.find(".menu>li>a").each(function(i){this.n = i});
	var subTag = GNB.find(".gnbSubWrap");
	var twoBtn = GNB.find(".gnbSubWrap  a");
	var anchor = GNB.find("a");

	oneBtn.bind("mouseover focus",function(e,o){
		clear();
		jQuery('.main_container').css('margin-top' , '260px');
		jQuery('.main_container').attr('style' , 'margin-top:260px;');
		jQuery('.container').css('margin-top' , '250px');
		jQuery('.container').attr('style' , 'margin-top:250px;');
		GNB.addClass("on");
		one.eq(this.n).addClass("on");
		if(subTag) subTag.show();
	});
	oneBtn.bind('mouseout blur',function(){
		stop();
		GNB.removeClass("on");
		start();		
	});
	twoBtn.bind("mouseover focus",function(){
		stop();
		jQuery('.main_container').css('margin-top' , '260px');
		jQuery('.main_container').attr('style' , 'margin-top:260px;');
		jQuery('.container').css('margin-top' , '250px');
		jQuery('.container').attr('style' , 'margin-top:250px;');
		twoBtn.removeClass("on");
		$(this).addClass("on");
		GNB.addClass("on");
	});
	twoBtn.bind("mouseout blur",function(){
		//stop();
		//GNB.removeClass("on");
		//start();
	});
	var clear = function(){
		stop();
		twoBtn.removeClass("on");
		one.removeClass("on");
		subTag.hide();
		jQuery('.main_container').css('margin-top' , '0px');
		jQuery('.main_container').attr('style' , 'margin-top:0px;');
		jQuery('.container').css('margin-top' , '0px');
		jQuery('.container').attr('style' , 'margin-top:0px;');
	},
	start = function(){
		GNB.act = setTimeout(function(){
			if(!GNB.state) active();
		},acTime);
	},
	stop = function(){
		clearTimeout(GNB.act);
	},
	active = function(){
		clear();
		if(!pageNum || !one[pageNum-1]){
			return false
		};
		if(pageNum) one.eq(pageNum-1).addClass("on");
		if (subNum>0)
		{
			subTag.find("ul").eq(pageNum-1).find("a").eq(subNum-1).addClass("on");
		}
	};
	active();

}



function submenu(){
	$(".sMenu .tit a").click(function(){
		$(".sMenu .tit").toggleClass("on").next().toggle();	
	});
}


function footerLinkSelect(){
	$(".selects  div > a").click(function(){
		if($(this).next().css("display") == "block"){
			$(".selects div ul").hide();
		}else{
			$(".selects div ul").hide();
			$(this).next().show();
		}
		return false;
	});
	$(".container").click(function(){
			$(".selects div ul").hide();
		
	})
}



function foot_slide(){
	var wrap = $("#mainFootBanner");
	var body = $("#mainFootBanner ul");
	var list = $("#mainFootBanner ul li");
	var btn_l = $("#mainFootBanner .btns a.right");
	var btn_r = $("#mainFootBanner .btns a.left");
	var btn_s = $("#mainFootBanner .btns a.stop");

	var length = list.length;
	var defaultNum = list.length*1000;
	var statusNum = defaultNum;
	var interval = 3000

	
	var action_1 = function(){
		body.animate({
			left: -list[0].offsetWidth
		},function(){
			list.parent().append(list[0]);
			body.css({left:0})
			list = $("#mainFootBanner ul li");
		})
	}

	var action_2 = function(){
		list.parent().prepend(list[length-1]);
		body.css({left: -list[length-1].offsetWidth})
		list = $("#mainFootBanner ul li");
		body.animate({
			left: 0
		});
	}


	var timer = ""
	var stop = function(){
		if(timer) clearInterval(timer);
	};
	var start = function(){
		stop();
		//if(rolling){
			stop();
			timer = setInterval(function(){
				statusNum--;
				action_1();
			},interval);
		//}
	};
	start();

	btn_s.click(function(){
		if(this.status){
			start();
			this.status = false;
		}else{
			stop();
			this.status = true;
		}
	});
	btn_l.click(function(){
		statusNum--;
		action_1();
		start();
		return false;
	});
	btn_r.click(function(){
		statusNum++;
		action_2("R");
		start();
		return false;
	});
}


function stabAction(){
	var btnL = $(".sTab .btnTab_l");
	var btnR = $(".sTab .btnTab_r");
	var cont = $(".sTab ul");
	var wid = cont.width();
	var wrap = $("body").width();
	if(wid - wrap < 0){
		btnR.hide();
		btnL.hide();
		return false;
		
	}
	var sum = (wrap*0.7) > (wid - wrap) ?  (wid - wrap) : (wid - wrap)/2;

	btnR.click(function(){
		if((wrap - wid) == parseInt(cont.css("left"),10)){
			return false;
		}
		cont.animate({
			left:"-=" + sum
		},function(){
			if((wrap - wid) == parseInt(cont.css("left"),10)){
				btnR.hide();
				btnL.show();
			}	
			if( 0 > parseInt(cont.css("left"),10)){
			}
		});
		return false;
	});
	btnL.click(function(){
		if( 0 <= parseInt(cont.css("left"),10)){
			return false;
		}

		cont.animate({
			left:"+=" + sum
		},function(){
			if(0 <= parseInt(cont.css("left"),10)){
				btnL.hide();
				btnR.show();
			}	
		});
		return false;
	});
};



$(function(){
	submenu();	
	footerLinkSelect();
	stabAction();
	
	$("#snsIcons").click(function(){
		if(this.status){
			$("#snsLinks").animate({width:0});
			this.status = false;
		}else{
			$("#snsLinks").animate({width:121});
			this.status = true;
		}
	})
});
