var MAIN_TAB = "mainTabs";
var frm = {
	getCtxPath : function() {
		var pathName = document.location.pathname;
		var index = pathName.substr(1).indexOf("/");
		var result = pathName.substr(0, index + 1);
		return result;
	},
	opTab : function(tabId, name, href) {
		if (!tabId) {
			tabId = MAIN_TAB;
		}
		var $tabs = $('#' + tabId);
		if ($tabs.tabs('exists', name)) {
			$tabs.tabs('select', name);
			return;
		}
		var content = constants.tabsContent.replace('${href}', this.getCtxPath() + href);
		$tabs.tabs('add', {
			title : name,
			closable : true,
			content : content
		});
	},
	opWin : function (id, title, href, onOpen, onClose) {
		var $div = $(document.createElement('div'));
		$div.attr('id', id);
		var content = constants.tabsContent.replace('${href}', this.getCtxPath() + href);
		var height = window.innerHeight * 0.85;
		var width = window.innerWidth * 0.85;
		$div.window({
			height: height,
			width: width,
			title: title,
			resizable: false,
			draggable: false,
			content: content,
			onOpen: function () {
				if (onOpen && typeof(onOpen) === 'function') {
					onOpen.call(this);
				}
			},
			onClose: function () {
				if(onClose && typeof(onClose) === 'function') {
					onClose.call(this);
				}
			}
		});
	},
	showLoading : function() {
		$("#my-mask").show();
	},
	finishLoading : function() {
		$("#my-mask").hide();
	},
	clearCombValue: function(id) {
		$('#' + id).combo('clear');
	},
	collectSearchParams: function (selector) {
		var $fields = $(selector).find('[name]');
		var fromData = {};
		var jsonParam = {};
		for (var i = 0;i < $fields.length; i++) {
			var f = $fields[i];
			var val = f.value;
			if (val === null || val.length === 0) {
				continue;
			}
			if (fromData[f.name]) {
				val = fromData[f.name] + ',' + val;
			}
			if (f.type === 'hidden') {
				fromData[f.name] = val;
			} else {
				jsonParam[f.name] = val;
			}
		}
		fromData.jsonParam = JSON.stringify(jsonParam);
		return fromData;
	},
	fromData: function(selector) {
		var $fields = $(selector).find('[name]');
		var fromData = {};
		for (var i = 0;i < $fields.length; i++) {
			var f = $fields[i];
			var val = f.value;
			if (val === null || val.length === 0) {
				continue;
			}
			if (fromData[f.name]) {
				val = fromData[f.name] + ',' + val;
			}
			fromData[f.name] = f.value;
		}
		return fromData;
	}
};