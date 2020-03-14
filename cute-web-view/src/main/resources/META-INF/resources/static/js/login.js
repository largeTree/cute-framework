function doLogin(loginApi, redirect) {
	var $userName = $('#uid');
	var userName = $userName.val();
	var pwd = $('#pwd').val();
	if (!userName || userName.length === 0) {
		setError('uid');
		return;
	}
	if (!pwd || pwd.length === 0) {
		setError('pwd');
		return;
	}

	frm.login(loginApi, {
		userName : userName,
		pwd : pwd
	}).then(function(data) {
		data = data.data;
		localStorage.setItem("userLite", data);
		$.cookie('sid', data.sessionId);
		if (!redirect) {
			redirect = '/view';
		}
		if (redirect.startWith('http')) {
			if (redirect.indexOf('?') > 0) {
				redirect = redirect + '&sid=' + sid;
			} else {
				redirect = redirect + '?sid=' + sid;
			}
			location.href = redirect;
		} else {
			location.href = location.protocol + '//' + location.host + redirect;
		}
	}, function(data) {
		if (data.status === 401) {
			data = data.data;
			$('#err').html(data.msg);
		} else {
			$('#err').html('登陆失败!!!');
		}
	});

}
function setError(id) {
	var $item = $('#' + id);
	$item.addClass('err-input');
	$item.focus();
}
function unSetError(item) {
	var $item = $(item);
	var val = $item.val();
	if (val && val.length > 0) {
		$item.removeClass('err-input');
	}
}
function onKeyPress(event) {
	// 按下回车键
	if (event.keyCode === 13) {
		var itemId = event.currentTarget.id;
		if (itemId === 'uid') {
			$('#pwd').focus();
		} else if (itemId == 'pwd') {
			$('#login-btn').click();
		}
	}
}