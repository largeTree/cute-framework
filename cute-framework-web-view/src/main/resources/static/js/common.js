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
	},
	setList: function(id, codeName) {
		frm.postApi(this.getCtxPath() + '/api.do', 'qd-codes', {codeDomain: codeName}).then(function(data) {
			var rows = data.rows;
			var $sec = $('#' + id);
			for (var item of rows) {
				$sec.append('<option value="' + item.code + '">' + item.caption + '</option>');
			}
		});
	},
	setAcList: function(id, codeName) {
		
	},
	post: function(url, params) {
		return this._ajax(url, params, 'post', null, false);
	},
	get: function(url) {
		return this._ajax(url, {}, 'get', null, false);
	},
	postApi: function(url, apiKey, params, jsonParam) {
		params = params || {};
		if (jsonParam) {
			params.jsonParam = JSON.stringify(jsonParam);
		}
		return this._ajax(this._appendApiKey(url, apiKey), params, 'post', null, true);
	},
	_appendApiKey(url, apiKey) {
		if (url.indexOf('?') > 0) {
			url = url + '&';
		} else {
			url = url + '?';
		}
		return url + 'apiKey=' + apiKey;
	},
	getApi: function(url, apiKey) {
		return this._ajax(this.__appendApiKey(url, apiKey), {}, 'get', null, true);
	},
	_ajax: function (url, params, method, timeout, isApi) {
		if (!timeout) {
			timeout = 15000;
		}
		var sid = $.cookie('sid');
		if (sid && sid.length > 0) {
			params.sessionId = sid;
		}
		return new Promise(function(resolve, reject) { 
			$.ajax({
				url: url,
				data: params,
				type: method,
				timeout: timeout,
				success: function(data, status) {
					if (isApi) {
						if (data.code === 0) {
							resolve(data.data);
						} else {
							reject(data, status);
						}
					}
				},
				error: function(xhr, errorMsg, e) {
					reject({
						xhr: xhr,
						data: JSON.parse(xhr.responseText),
						msg: errorMsg,
						status: xhr.status
					});
				}
			});
		});
	}
};

var httpUtil = {
		
}