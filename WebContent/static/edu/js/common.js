
function check_yearend()
{
	var today = new Date();
	var month = today.getMonth()+1;
	var date = today.getDate();	
	
	if( month >= 12 && date >=18 && date <= 31)
		return true;
	else 
		return false;
}
