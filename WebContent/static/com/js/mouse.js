var isMotion = false;

$(document).on('DOMMouseScroll mousewheel', function(event) {
	if( isMotion ) {
		event.stopPropagation();
	    event.preventDefault();
	    event.returnValue = false;
	    return false;
	}
});

$(document).mousewheel(function(event, delta) {
	if( isMotion ) return;
	
	if( delta < 0 ) {
		if( !$('.topArea').hasClass( 'cmbMotion' ) ) {
			isMotion = true;
			$( '.headerBnr' ).animate({
				'margin-top': -430
			}, {
				duration: 700,
				easing:'easeOutCirc',
				complete: function() {
					$('.topArea').addClass('cmbMotion');
					$('.headerWrap').addClass('top');
					//$( 'div.main_visual' ).remove();
					$('.gnbSubWrap').css('top', '225px');
					setTimeout(function() {
						isMotion = false;
					}, 500);
				}
			});
			$( 'html, body' ).animate({scrollTop: 0 }, {easing:'easeOutCirc'});
		}
	}
});

$(document).ready(function(e) {
    $( window ).scrollTop( 0 );
	setTimeout(function() {
		$( window ).scrollTop( 0 );
	}, 500);
});