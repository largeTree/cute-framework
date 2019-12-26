$.fn.combobox.defaults.loadFilter = function(data) {
	var rows;
	if (data && data.code === 0) {
		rows = data.data.rows;
	} else {
		console.error('find codes Error...', new Error());
		rows = [];
	}
	rows.splice(0,0, {code:null, caption: "&nbsp;"});
	return rows;
}