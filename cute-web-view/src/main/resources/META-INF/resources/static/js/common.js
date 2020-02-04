var MAIN_TAB = "mainTabs";

String.prototype.startWith = function(str) {
	var reg = new RegExp("^" + str);
	return reg.test(this);
}
String.prototype.endWith = function(str) {
	var reg=new RegExp(str + "$");
	return reg.test(this);
}

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
	opWin : function (id, title, href, pk, onOpen, onClose, autoClose) {
		var $div = $(document.createElement('div'));
		$div.attr('id', id);
		
		href = this.appendQueryString(href, 'id', pk);
		href = this.appendQueryString(href, 'dlgId', id);
		href = this.appendQueryString(href, 'autoClose', autoClose);
		
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
	closeWin: function(id) {
		$('#' + id).window('close');
	},
	appendQueryString:function(href, key, val) {
		if (href && val) {
			if (href.indexOf('?') > 0) {
				href = href + '&';
			} else {
				href = href + '?';
			}
			href = href + key + '=' + val;
		}
		return href;
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
	setList: function(id, codeName, defval) {
		frm.postApi('qd-codes', {codeDomain: codeName}).then(function(data) {
			var rows = data.data.rows;
			var $sec = $('#' + id);
			for (var item of rows) {
				if (defval && defval == item.code) {
					$sec.append('<option value="' + item.code + '" selected>' + item.caption + '</option>');
				} else {
					$sec.append('<option value="' + item.code + '">' + item.caption + '</option>');
				}
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
	postApi: function(apiKey, params, jsonParam) {
		params = params || {};
		if (jsonParam && (typeof jsonParam) != 'string') {
			params.jsonParam = JSON.stringify(jsonParam);
		}
		return this._ajax(this._appendApiKey(this.getCtxPath() + '/api.do', apiKey), params, 'post', null, true);
	},
	getApi: function(apiKey) {
		return this._ajax(this._appendApiKey(this.getCtxPath() + '/api.do', apiKey), {}, 'get', null, true);
	},
	_appendApiKey(url, apiKey) {
		if (url.indexOf('?') > 0) {
			url = url + '&';
		} else {
			url = url + '?';
		}
		return url + 'apiKey=' + apiKey;
	},
	_ajax: function (url, params, method, timeout, isApi) {
		if (!timeout) {
			timeout = 15000;
		}
		var sid = $.cookie('sid');
		if (!sid) {
			var userLite = localStorage.getItem('userLite');
			if (userLite) {
				sid = userLite.sessionId;
			}
		}
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
							resolve(data);
						} else {
							reject(data, status);
						}
					} else {
						resolve(data);
					}
				},
				error: function(xhr, errorMsg, e) {
					var status = xhr.status;
					// 会话已过期或没有登陆
					if (status === 401) {
						var loginPath = xhr.getResponseHeader('loginPath');
						top.location.href = top.frm.getCtxPath() + loginPath;
						return;
					}
					reject({
						xhr: xhr,
						data: JSON.parse(xhr.responseText),
						msg: errorMsg,
						status: status
					});
				}
			});
		});
	}
};